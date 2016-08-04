KEYSPACE = 'indigo'
LOG_LEVEL = 'INFO'
CASSANDRA_HOSTS = [ {% for item in groups['indigo-databases'] %}"{{ hostvars[item]['ansible_' ~ hostvars[item]['cassandra_interface']]['ipv4']['address'] }}",{% endfor %} ]
REPLICATION_FACTOR = {{ cassandra_replication_factor }}
CONSISTENCY_LEVEL = {{ groups['indigo-databases']|length - cassandra_replication_factor + 1 }}
