# DRAS-TIC Cluster Management

TODO: This document is surely a work in progress..

## Add Cassandra Nodes

If you already have a working Cassandra cluster, the process for adding a new node is straightforward. This method will interrupt service, but it can be done manually (without Ansible) with no downtime. (The manual method will rely on retracing the setup tasks in the cassandra role for the new server.)

1) Create the new machine and provision the Cassandra storage. The storage path should be consistent with your other Cassandra nodes and belong to the `cassandra` user, as per the [Install Guide](INSTALL.md).

2) Edit your Ansible inventory, adding the new database host to the database group.

3) Stop your DRAS-TIC web service. We don't have a guide for doing this work uninterrupted yet.

4) Run the Ansible playbook for database provisioning:
```
$ ansible-playbook -i ~/ansible/hosts databases.yml
```

5) Make a cup of coffee or tea. The new server should join the cluster eventually after all the nodes finish restarting. You can track this progress once your first Cassandra node is back online using the `nodetool`. It will report a node as UN (up and normal) when it has joined the cluster normally:
```
$ nodetool status
```

6) Wait for all the new and old nodes to show up in the output from the `nodetool` command and be in state **UN**.

7) Run `nodetool cleanup` on all the nodes in the cluster. This may take some time, so perhaps best done in TMUX or screen.
