# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant.configure(2) do |config|
  config.vm.provider "virtualbox" do |vb|
    config.vm.box = "ubuntu/trusty64"
  end
#  config.vm.provider "libvirt" do |lv|
#    config.vm.box = "naelyn/ubuntu-trusty64-libvirt"
#  end

  config.vm.define "indigo"

  # Create a forwarded port mapping which allows access to a specific port
  # within the machine from a port on the host machine. In the example below,
  # accessing "localhost:8080" will access port 80 on the guest machine.
#  config.vm.network "public_network", ip: "192.168.111.222"
  config.vm.network "forwarded_port", guest: 80, host: 8080  # The web app
  config.vm.network "forwarded_port", guest: 443, host: 8443  # The https web app 
  config.vm.network "forwarded_port", guest: 9000, host: 9000  # The agent
  config.vm.network "forwarded_port", guest: 9042, host: 9042  # Cassandra native protocol

  # Share an additional folder to the guest VM. The first argument is
  # the path on the host to the actual folder. The second argument is
  # the path on the guest to mount the folder. And the optional third
  # argument is a set of non-required options.
  # config.vm.synced_folder "../data", "/vagrant_data"

  config.vm.hostname = "indigo"

  config.vm.provider "virtualbox" do |vb|
    vb.name = "Indigo"
    vb.memory = "2048"
  end

#  config.vm.provider "libvirt" do |lv|
#    lv.memory = 2048
#  end

  config.vm.provision "ansible" do |ansible|
    ansible.playbook = "deploy_standalone.yml"

    ansible.groups = {
      "indigo-databases" => ["indigo"],
      "indigo-webservers" => ["indigo"],
      "indigo:children" => ["indigo-databases", "indigo-webservers"]
    }

    # We override these variables to account for the default user being "vagrant" rather than "indigo".
    ansible.extra_vars = {
      ansible_ssh_user: "vagrant",
      install_dir: "/home/vagrant/",
      cassandra_interface: "eth0",
#      cassandra_seed_server: "indigo",
      cassandra_replication_factor: "1",
      LDAP_SERVER_URI: "ldap://ldap.umd.edu",
      LDAP_USER_DN_TEMPLATE: "uid=%(user)s,ou=people,dc=umd,dc=edu"
    }
  end
end
