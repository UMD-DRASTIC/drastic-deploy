# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant.configure(2) do |config|
  # config.vm.box = "ubuntu/xenial64"
  config.vm.box = "centos/7"
  config.vm.provider "virtualbox" do |vb|
    vb.memory = "2048"
  end

  config.vm.provider "libvirt" do |lv, override|
    # override.vm.box = "sergk/xenial64-minimal-libvirt"
    override.vm.box = "centos7"
    lv.memory = 2048
    override.ssh.shell = "bash -c 'BASH_ENV=/etc/profile exec bash'"
  end

  N = 3
  (1..N).each do |i|
    config.vm.define "node-#{i}" do |node|
      node.vm.network "private_network", ip: "192.168.77.#{20+i}",
        virtualbox__intnet: "mynetwork"
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
      end
      $script = <<SCRIPT
echo Installing Python as required by Ansible
if [ $(dpkg-query -W -f='${Status}' python 2>/dev/null | grep -c "ok installed") -eq 0 ];
then
  sudo apt-get update;
  sudo apt-get install python rsync -q -y;
fi
SCRIPT
      $script_yum = <<SCRIPT
echo Installing Python as required by Ansible
if [ $(dpkg-query -W -f='${Status}' python 2>/dev/null | grep -c "ok installed") -eq 0 ];
then
sudo yum install python rsync -q -y;
fi
SCRIPT
      config.vm.provision "shell", inline: $script_yum

      if i == N # last machine, run ansible on all
        node.vm.provision :ansible do |ansible|
          ansible.playbook = "vagrant.yml"
          ansible.limit = "all"
          # ansible.tags = ["graph", "code"]  # for graph dev
          # ansible.tags = ["init"]  # for dev

          ansible.groups = {
            "drastic-databases" => ["node-1", "node-2"],
            "drastic-webservers" => ["node-3"],
            "drastic:children" => ["drastic-databases", "drastic-webservers"]
          }

          ansible.extra_vars = {
            django_host: "localhost",
            http_port: 80,
            https_port: 443,
            deploy_local_code: true, # copies local code instead of git clone
            install_dir: "/opt/drastic",
            #cassandra_interface: "enp0s8",
            cassandra_interface: "eth1",
            # cassandra_seed_server: "node-1",
            cassandra_replication_factor: 1,
            cassandra_restart_seconds: 120,
            cassandra_data_dirs: ["/mnt/vol-1/data-files"],
            https_mode: false
          }
        end
      end
    end
  end
end
