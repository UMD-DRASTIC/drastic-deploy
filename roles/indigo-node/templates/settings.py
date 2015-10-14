KEYSPACE = 'indigo'
LOG_LEVEL = 'INFO'
CASSANDRA_HOSTS = ('127.0.0.1', )  # {% for host in groups['indigo-databases'] %}{{ "'" }}{{ hostvars[host]['ansible_eth0']['ipv4']['address'] }}{{ "', " }}{% endfor %})
