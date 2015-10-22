KEYSPACE = 'indigo'
LOG_LEVEL = 'INFO'
CASSANDRA_HOSTS = ("{{ hostvars[inventory_hostname]['ansible_' ~ cassandra_interface]['ipv4']['address'] }}", )
REPLICATION_FACTOR = {{ (groups['indigo-databases']|length) - 1 }}