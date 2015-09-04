# Indigo Deployment 

The following steps describe how to deploy Indigo to a remote (or local) server.
[[[ **note:   How is the deployment to _multiple_ servers or, the addition of a server achieved?** ] 


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
[indigo]
192.168.20.20
```


#### Deploying


Run the deployment with the following command:

```
ansible-playbook deploy_standalone.yml -i hosts --ask-sudo-pass 
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
. /usr/lib/indigo/agent/bin/activate
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