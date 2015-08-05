# Indigo Deployment 

The following steps describe how to deploy Indigo to a remote (or local) server.


#### Access 

* Make sure you have access to the server via SSH, either directly or via a proxy.

#### Pre-requisites

-- Install Ansible and GIT

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