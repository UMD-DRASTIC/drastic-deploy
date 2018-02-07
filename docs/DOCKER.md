# Running DRAS-TIC in Containers
We leverage Ansible's Container script in order to build, deploy and run
DRAS-TIC on a Docker Swarm. This sames a tremendous amount of time by avoiding the setting of individual server nodes and managing each as a separate operating system, with security patches, etc.. Instead we rely on a managed swarm environment that also has flexible scaling up of new nodes. Many advantages.

The DRAS-TIC Deploy project comes with a central file called container.yml, that is used alongside the `ansible-container` command to orchestrate deployments across one or more physical host machines. Since container environments are designed to each and every service or daemon in a separate container, you will find that the ansible roles are smaller, with fewer processes running in each virtual machine.

Ansible Container's container.yml file tells us what services it can run and in many ways it resembles a Docker Compose file, though it defines each service with Ansible roles, rather than Dockerfile configuration.


## Container Services
DRAS-TIC Docker swarms include four distinct service types, outlined below. Each may be scaled out independently with Docker handling networking between them.

* Nginx - A service is dedicated to running Nginx, using a slightly modified Nginx base image. The service shares a storage volume with the Django service, so that it can serve the static files from the DRAS-TIC Django application.

* Django - These containers runs the DRAS-TIC Django service inside of a gunicorn process.

* Django Init - This service is identical to the main Django service, but it is started up first. It creates the Cassandra schema, creates Django static files, creates and runs Django database migrations, and then stops. It will run again each time you start the swarm, but the operations finish quickly.

* Mosquitto - This is the MQTT broker service. Not configured to scale out at this point.

* Cassandra[1-3] - These are the Cassandra nodes, with each node defined as a separate service. We currently want to map specific storage volumes


## Installation and Setup



## Running DRAS-TIC Commands
The "django" service image contains the drastic command-line tools. You can easily run "drastic" or "drastic-admin" against your repository cluster with the `docker exec` command:
```
$ docker exec -it <container id> drastic-admin group-create mygroup
```

The docker container executes your commands within the Drastic virtual environment, a Python virtualenv with command-line tools.

NOTE: You must use `docker exec` to set up your initial user groups and administrative users.


## Cassandra Queries
The Cassandra service containers and the Django container(s) come with the `cqlsh` command, which you can use to connect and query the Cassandra cluster. It's easiest to do this on the cassandra-1 node, since you don't need to supply a hostname to `cqlsh`.

Query the keyspace:
```
$ docker exec -it <container id> cqlsh
```

Monitor Cassandra nodes:
```
$ docker exec -it <container id> nodetool status
```


## Implementation Details
* DONE nginx config still saying "127.0.0.1" for upstream url
* DONE django process quits, get logs
* DONE separate the django config into environment variables
* DONE separate DRAS-TIC config into environment variables
* DONE add collectstatic and make migrations to django role
* DONE add migrate to django startup
* DONE Initialize the drastic keyspace
* deploy static files to shared volume or nginx server
* clean up APT/YUM cache afterwards when connection is docker
