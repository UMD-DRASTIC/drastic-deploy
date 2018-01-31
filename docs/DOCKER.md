# Running DRAS-TIC in Containers

We leverage Ansible's Container feature in order to build, deploy and run
DRAS-TIC on a Docker Swarm. This sames a tremendous amount of time by avoiding the setting of individual server nodes and managing each as a separate operating system, with security patches, etc.. Instead we rely on a managed swarm environment that also has flexible scaling up of new nodes. Many advantages.

The DRAS-TIC Deploy project comes with a central file called container.yml, that is used alongside the `ansible-container` command to orchestrate deployments across one or more physical host machines. Since container environments are designed to each and every service or daemon in a separate container, you will find that the ansible roles are smaller, with fewer processes running in each virtual machine.


TODO Notes:

* DONE nginx config still saying "127.0.0.1" for upstream url
* DONE django process quits, get logs
* DONE separate the django config into environment variable
* DONE add collectstatic and make migrations to django role
* add migrate to django startup
* Initialize the drastic keyspace
 * use cqlsh in django container to init from startup script
 * use a separate cassandra container that only executes CMD and exits
 * switch to using the default keyspace
 * Must be done after deploy (Cassandra volumes are external)
 * Adds schema creation to django startup?
 * How does this impact DJANGO migrations/etc?
* deploy static files to shared volume or nginx server
* clean up APT/YUM cache afterwards when connection is docker
