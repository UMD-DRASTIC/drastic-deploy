# Indigo Deployment

The following steps describe how to deploy Indigo to a remote (or local) server.

[ Note, if you intend to run multiple servers, know beforehand the details of the networks, as it is best to set up the system with the 'real' network rather than using localhost. ]

####  PREREQUISTES
Ansible expects a user _indigo_ to exist with sudo rights.  
e.g. 
```
sudo adduser  indigo
sudo usermod -G sudo,adm indigo
sudo mkdir ~indigo/.ssh
sudo cat ~/.ssh/authorized_keys >> ~indigo/.ssh/authorized_keys # If you want to propagate ssh certificates
```

#### Vagrant

There is a Vagrantfile with this repo that enables you to install Indigo to a virtual machine with minimum fuss.
This is meant only for development and everything resides locally, so if you're deploying on a production machine,
use the usual Ansible instructions below.

First, get Vagrant:

```
sudo apt-get install vagrant
```

You will also need Ansible - get it as described below.

Then in the root of this repo, run `vagrant up` If this is the first time you've run this command, it will go and fetch
a Vagrant "box", which is just a VM base image. In this case it's a bare-bones Ubuntu Trusty 64-bit image.

Vagrant will then go and run the Ansible script automatically, asking all the usual questions. If you change the
Ansible provisioning scripts, just run `vagrant provision` to re-run Ansible.

To pause the VM, run `vagrant suspend` and to resume it `vagrant resume`. `vagrant halt` will shut the VM down
gracefully and `vagrant up` will restart it. Finally, `vagrant destroy` will shut down and delete the VM.

There is a default user on the VM called `vagrant` and the password is `vagrant`, so you can ssh into it as normal.

#### Access

* Make sure you have access to the server via SSH, either directly or via a proxy.

#### Pre-requisites

-- Install Ansible and GIT on the host from which you are installing ( not the target).

```
sudo apt-get install software-properties-common
sudo apt-add-repository ppa:ansible/ansible
sudo apt-get update
sudo apt-get install ansible git
```
-- Fetch _this_ package
```
git clone <URL copied from above>
```

#### Configuration

* By default the user account on the server should be 'indigo' who should have sudo access.  If different then the user field in deploy-standalone.yml should be changed.

* Cassandra stores it data by default in /var/lib/cassandra -- this should be redirected to an appropriate storage volume, either via a symbolic link or using something like
```
mkdir /var/lib/cassandra
mount --bind <target> /var/lib/cassandra
mkdir /var/lib/cassandra/data
# Ensure that permissions and ownership are appropriately set
chown -R casssandra:cassandra /var/lib/cassandra
```
* Precaution:  If you are re-installing then it is probably worth doing a complete removal of cassandra
e.g.
``` sudo apt-get purge cassandra ```

* Create a ```hosts``` file containing the IP address of the servers.  It should look like..

```
[indigo-databases]
192.168.20.20 cassandra_interface=eth0

[indigo-webservers]
192.168.20.20

[indigo:children]
indigo-databases
indigo-webservers
```

Where the IP addresses should be replaced with the host name/IP address of each machine. For each [indigo-databases] you must provide the Ethernet interface on which Cassandra communicates. Usually this is eth0, but it may be something different on more complex topologies.

* The default behavior is to use HTTPS. If needed this can be changed in the
webservers.yml file with the variable https_mode. The nginx server is using the
SSL certificate in /etc/nginx/ssl/nginx.crt. If they are not present a self-signed
version is created during deployment.

#### Deploying


Run the deployment with the following command:

```
ansible-playbook deploy_standalone.yml -i hosts --ask-become-pass
```

* Once started the script will ask for some details. The sudo-password for the user specified in deploy-standalone.yml, your bitbucket username and bitbucket password.  This is so that the script can retrieve the code from the private repositories.

* Make a cup of tea.


#### Problems

Unfortunately Cassandra can take a while to start. The process list will show the Java process running although Cassandra is still not available. To resolve this the script pauses once the Cassandra installation is complete.

On some occasions the indigo-node fails to start after installation.  This is being investigated.
** As a temporary workaround execute
```sudo service indigo-web start```
on the target machine if the web-service fails to work. **

### Post install tasks
Create Users

See this [LINK](https://bitbucket.org/archivea/indigo) for full details, but the short version is
```
ssh indigo@<target>
export INDIGO_CONFIG=settings
source ~/web/bin/activate
indigo user-create
# and you probably want to create a group or two .... especially since you need to for ingesting
indigo group_create <group_name>  <user_name_that_owns_group>
```
If you get an error on logging in then on the target machine
```
deactivate
. /usr/lib/indigo/web/bin/activate
cd /usr/lib/indigo/web/project
sudo ../bin/python manage.py syncdb

And just choose No if it asks any questions...
```

***AGENT CONFIG***

The file /etc/init/indigo-agent needs to have these lines ( the last one should be there, the first two may not ).
```

env CQLENG_ALLOW_SCHEMA_MANAGEMENT=1
env AGENT_CONFIG=/usr/lib/indigo/agent/project/agent.config

exec /usr/lib/indigo/agent/bin/python /usr/lib/indigo/agent/project/wsgi....
```
#### Cluster set up.

[[[ **note:  To deploy to _multiple_ servers or add a server achieved 
-- In order to do this then the installation needs to know 
- the interface of the Cassandra network , i.e. the  one that Cassandra uses to coordinate
- the address of at least one pre-existing node, ... since the cluster has to have something to Join
- the name of the Cassandra cluster.

This should now be in the installation scripts, but the nuts and bolts is that in /etc/cassandra/cassandra.yaml

- set 

```
auto_bootstrap: true
listen_interface: eth<n>
broadcast_address: 
```

- in the seed_provider: stanza add the addresses of known members 

```
seeds: "<ip1>[,<ip2>]"
``` 
- ensure the clustername is the same 

- on the new node node,stop Cassandra ( service cassandra stop ), remove _everything_ from the data directory ( /var/lib/cassandra/data/ ), 

- Restart Cassandra, leave it to sort itself out by waiting for all the nodes to show up in the output from the command line ```nodetool status```  and be in state **UN** and then run ```nodetool cleanup``` on all the nodes in the cluster ( which may take som time, so perhaps best done in TMUX).
 ]
 
### Install on lxc container

For some reasons Cassandra configuration is failing when trying to deploy on a
lxc container. If you experiment a connection error when ansible is trying to 
finish Cassandra initialization you can try to log on the container, and restart
cassandra manually, it should now listen on eth0 correctly rather than 127.0.0.1.

For the listener the Docker install is failing on the first time but works if
ansible is restarted

