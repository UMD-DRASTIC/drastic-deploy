# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant.configure(2) do |config|
  config.vm.box = "ubuntu/xenial64"
  config.vm.provider "virtualbox" do |vb|
    vb.memory = "2048"
  end

  config.vm.provider "libvirt" do |lv, override|
    override.vm.box = "sergk/xenial64-minimal-libvirt"
    lv.memory = 2048
    override.ssh.shell = "bash -c 'BASH_ENV=/etc/profile exec bash'"
  end

  N = 3
  (1..N).each do |i|
    config.vm.define "node-#{i}" do |node|
      # node.vm.network "private_network", ip: "192.168.77.#{20+i}"
      if i == 1
        node.vm.hostname = "node-1"
        # node.vm.network "forwarded_port", guest: 9042, host: 9042  # Cassandra native protocol
      end
      if i == 2
        node.vm.hostname = "node-2"
        # node.vm.network "forwarded_port", guest: 9042, host: 9042  # Cassandra native protocol
      end
      if i == 3
        node.vm.hostname = "node-3"
        node.vm.network "forwarded_port", guest: 80, host: 8080 #, adapter: "lo" # The web app
        node.vm.network "forwarded_port", guest: 443, host: 8443 #, adapter: "lo"  # The https web app
        node.vm.network "forwarded_port", guest: 9000, host: 9000 #, adapter: "lo"  # The agent
      end
      # if i == 4
      #   node.vm.hostname = "jobs"
      # end
      $script = <<SCRIPT
echo Installing Python as required by Ansible
if [ $(dpkg-query -W -f='${Status}' python 2>/dev/null | grep -c "ok installed") -eq 0 ];
then
  sudo apt-get update;
  sudo apt-get install python -q -y;
fi
echo Installing Rsync as required by Ansible
if [ $(dpkg-query -W -f='${Status}' rsync 2>/dev/null | grep -c "ok installed") -eq 0 ];
then
  sudo apt-get update;
  sudo apt-get install rsync -q -y;
fi
SCRIPT
      config.vm.provision "shell", inline: $script

      if i == N # last machine, run ansible on all
        node.vm.provision :ansible do |ansible|
          ansible.playbook = "deploy_standalone.yml"
          ansible.limit = "all"
          # ansible.tags = ["graph", "code"]  # for dev

          ansible.groups = {
            "drastic-databases" => ["node-1", "node-2"],
            "drastic-webservers" => ["node-3"],
            "drastic:children" => ["drastic-databases", "drastic-webservers"]
          }

          # We override these variables to account for the default user being "vagrant" rather than "drastic".
          ansible.extra_vars = {
            ansible_ssh_user: "vagrant", # for Vagrant Ansible SSH, not in plays
            deploy_local_code: true, # copies local code instead of git clone
            install_dir: "/opt/drastic",
            use_datastax: true,
            datastax_email: $datastax_email,
            datastax_password: $datastax_password,
            cassandra_interface: "eth0",
            cassandra_seed_server: "node-1",
            cassandra_replication_factor: 1,
            LDAP_SERVER_URI: "ldap://ldap.umd.edu",
            LDAP_USER_DN_TEMPLATE: "uid=%(user)s,ou=people,dc=umd,dc=edu",
            cassandra_data_dirs: ["/mnt/vol-1/data-files"]
          }
        end
      end
    end
  end

  # config.vm.define "db1" do |db1|
  # end
  #
  # config.vm.define "db2" do |db2|
  #   db2.vm.hostname = "db2-drastic"
  #   db2.vm.network "forwarded_port", guest: 9042, host: 9042  # Cassandra native protocol
  # end
  #
  # config.vm.define "web" do |web|
  # end
  #
  # config.vm.define "jobs" do |jobs|
  # end

  # Create a forwarded port mapping which allows access to a specific port
  # within the machine from a port on the host machine. In the example below,
  # accessing "localhost:8080" will access port 80 on the guest machine.
#  config.vm.network "public_network", ip: "192.168.111.222"

  # Share an additional folder to the guest VM. The first argument is
  # the path on the host to the actual folder. The second argument is
  # the path on the guest to mount the folder. And the optional third
  # argument is a set of non-required options.
  # config.vm.synced_folder "../data", "/vagrant_data"

end
