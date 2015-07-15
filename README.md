# Indigo Deployment 

Run the deployment with the following command:

```
ansible-playbook deploy_standalone.yml -i hosts
```

The file ```hosts``` should contain something like:

```
[indigo]
192.168.20.20
192.168.20.21
```

## TODO:

* Include all IPs in Cassandra and Indigo config so it sees the entire cluster

