# Installing DRAS-TIC

This document will guide you through an installation of DRAS-TIC on a cluster for production or testing. If you wish to contribute to developing DRAS-TIC, we recommend that you see the [Development Guide](DEVELOP.md) and use the Vagrant install method.

A minimal install of DRAS-TIC will include a web server and two or more database hosts for Cassandra. These can be physical or virtual servers, as you like. However, Cassandra's storage should be a local volume or attached from a local filer. Network-attached storage is a bad idea for Cassandra. For more details on managing DRAS-TIC clusters, see the [Clustering Guide](CLUSTER.me).

# Local Preparation

Drastic deployment is automated using [Ansible](http://docs.ansible.com/). To run the Ansible playbooks, you will need to install Ansible on your local machine. You also need Git.
```
$ sudo apt-get install software-properties-common
$ sudo apt-add-repository ppa:ansible/ansible
$ sudo apt-get update
$ sudo apt-get install ansible git
```

Once you have the Ansible and Git, you can clone the playbooks in this repository to your local machine:
```
$ git clone https://github.com/UMD-DRASTIC/drastic-deploy.git
$ cd drastic-deploy
```


# Server Preparation

Most of the server setup is automated by Ansible, but a couple of preliminary steps are required. DRAS-TIC has Cassandra database hosts and web server hosts. A typical installation might include four database servers and one web server.

1) Make sure you have access to the servers via SSH, either directly or via a proxy.

2) All servers need the Python package, used by Ansible:
```
$ sudo apt-get install python
```

3) On the web server(s) Ansible expects the user _drastic_ to exist with sudo rights. You can create the user with these commands:
```
$ sudo adduser drastic
$ sudo usermod -G sudo,adm drastic
```

5) For production: On the Cassandra database hosts, create a appropriate storage path for the Cassandra data. Use the same path consistently on all of your database servers. This storage should be local volumes or local attached storage, not network attached volumes. For example, the database hosts in the demonstration cluster use 16TB SATA volumes exported from a NetApp filer and formatted to XFS. We will refer to this location as the _cassandra_data_path_ from here on. Make Cassandra the owner of this location:
```
$ sudo adduser cassandra
$ chown -R casssandra:cassandra <your cassandra_data_path>
```


# Ansible Inventory and Configuration

All your local settings will be pulled into the Ansible playbooks via your Ansible server inventory. You will want to keep these settings in a local folder outside of the drastic-deploy project. For this manual we'll assume there is an _ansible_ folder in the home directory for this purpose.

1) Create an Ansible inventory file at `~/ansible/drastic_hosts`. Your inventory will include the IP address or hostname of each server as accessible by SSH. Organize the servers under the appropriate group. It should look something like this:
```
[drastic-databases]
node-1

[drastic-webservers]
node-2
node-3
node-4

[drastic:children]
drastic-databases
drastic-webservers
```

2) Create a YAML variables file for the _drastic_ server group defined above (`~/ansible/group_vars/drastic.yaml`). A typical file will look like this:
```
cassandra_interface: ens5
cassandra_seed_server: node-2
cassandra_replication_factor: 2
use_datastax: yes
datastax_email: joe@example.com
datastax_password: joes_datastax_password
LDAP_SERVER_URI: ldap://ldap.example.com
LDAP_USER_DN_TEMPLATE: uid=%(user)s,ou=people,dc=example,dc=com
LDAP_BIND_DN: uid=mybinddn,cn=auth,ou=ldap,dc=example,dc=com
LDAP_BIND_PASSWORD: lkjeojfkjafjk
```

3) Configure the variables as appropriate for your installation. See the variables reference below for details.


# Configuration Reference

* *cassandra_interface*: You must provide the Ethernet interface on which
Cassandra communicates. Usually this is eth0, but it may be something different
on more complex topologies (It changed in Ubuntu > 15.10). (required)

* *cassandra_seed_server*: This will determine the seed server within the Cassandra cluster. (required)

* *cassandra_replication_factor*: Set this to an integer, up to the total number of nodes in your database cluster. This impacts storage capacity, performance, and how many servers you can lose at any one time without data loss, see Cassandra documentation. Three is a good number for production installations.

* *https_mode*: The default behavior is to use HTTP, but we recommend that you set this to 'yes' and use HTTPS. The nginx server will use the SSL certificate(s) in `/etc/nginx/ssl/nginx.crt` and the key in `/etc/nginx/ssl/nginx.key`. If you specify *https_mode* as 'yes' and these files are not present a self-signed certificate is created during deployment.

* *use_datastax*: Two distributionns of Cassandra are available, one from the Apache project and one from the DataStax company. Current Drastic development uses the DataStax version, leveraging the DataStax Enterprise Graph feature to provide a graph database of collections. The DataStax distribution of Cassandra comes from their corporate APT repository, which requires your DataStax login below. Set this to 'yes' if you are on the current Drastic code.

* *datastax_email*: Your DataStax email account used for login.

* *datastax_password*: Your DataStax password.

* *LDAP_SERVER_URI*: You can delegate authentication to an LDAP server, specified here. Leave this variable out if you don't want LDAP authentication.

* *LDAP_USER_DN_TEMPLATE*: Authentication code will use this template to query the LDAP server for the user DN. E.G. "uid=%(user)s,ou=people,dc=example,dc=com"

* *LDAP_BIND_DN*: Authentication code will use this DN to bind to the LDAP server. E.G. "uid=mybinddn,cn=auth,ou=ldap,dc=example,dc=com"

* *LDAP_BIND_PASSWORD*: A password for the LDAP bind.


# Deploying

1) Run the Cassandra deployment with the following command:
```
ansible-playbook databases.yml -i ~/ansible/hosts --ask-become-pass
```

2) Make a cup of tea as the steps can take quite a while. Unfortunately Cassandra also takes a while to start up. The playbook has a built-in wait of 60 seconds before taking next steps. If you see errors about Cassandra being unreachable, timing out or being offline, you can set *cassandra_restart_seconds* variable to a larger number.

3) Run the front-end deployment with the following command:
```
ansible-playbook webservers.yml -i ~/ansible/hosts --ask-become-pass
```

# Problems

On some occasions the drastic web service fails to start after installation. You can also start it manually like this:
```
$ sudo systemctl start drastic-web
```


# Post Install Tasks

Creating a user is a good place to start. See the [Administrator's Guide](https://github.com/UMD-DRASTIC/drastic-deploy/ADMINISTRATION.md) for full details. If you are on the front-end server, you can use the DRAS-TIC admin tool for this:
```
$ ssh drastic@<target>
$ export DRASTIC_CONFIG=settings
$ source ~/web/bin/activate
$ drastic user-create
```

You might also create a group or two using the server-side drastic comment:
```
$ drastic group_create <group_name>  <user_name_that_owns_group>
```


# Agent Configuration

Note: The Drastic agent apparatus has been deprecated in favor of a more extensible system based on MQTT messaging. See the [drastic-jobs](https://github.com/UMD-DRASTIC/drastic-jobs) project for more information.

The file /etc/init/drastic-agent needs to have these lines ( the last one should be there, the first two may not ).
```
env CQLENG_ALLOW_SCHEMA_MANAGEMENT=1
env AGENT_CONFIG=/usr/lib/drastic/agent/project/agent.config

exec /usr/lib/drastic/agent/bin/python /usr/lib/drastic/agent/project/wsgi....
```
