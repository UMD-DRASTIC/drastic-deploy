# Contributing to DRAS-TIC

TODO: Work in progress. Topics still to come:
* Contributing via Git
* Overview of components and code repositories
* Where to report issues
* Contribution agreements

## Vagrant Machines

There is a Vagrantfile with this repo that enables you to install Drastic to a set of virtual machines with minimum fuss. This is meant only for development and everything resides locally.

First, get Vagrant:
```
sudo apt-get install vagrant
```

You will also need Ansible - get it as described above.

Then in the root of this repo, run `vagrant up` If this is the first time you've run this command, it will go and fetch a Vagrant "box", which is just a VM base image. In this case it's a bare-bones Ubuntu image.

Vagrant will then go and run the Ansible script automatically, asking all the usual questions. If you change the Ansible provisioning scripts, just run `vagrant provision` to re-run Ansible.

Use `vagrant ssh node-3` to SSH into the Drastic web host. You can do the same for node-1 and node-2, the database hosts.

In the course of development you will find these variables interesting in the Ansible section of Vagrantfile:

* *deploy_local_code*: This specifies that code is copied to the local, vagrant-hosted machines from local directories, alongside this folder, instead of the public repositories. It should be set to 'yes' already in Vagrantfile.

* *ansible.tags*: This line tells Ansible to only run tasks marked with the specified tags. Use the tag 'code' to deploy new code and restart while skipping other steps. (Note that it expects a list, even for a single tag value.)

To pause the VMs, run `vagrant suspend` and to resume run `vagrant resume`. `vagrant halt` will shut the VMs down gracefully and `vagrant up` will restart them. Finally, `vagrant destroy` will shut down and delete the VMs.
