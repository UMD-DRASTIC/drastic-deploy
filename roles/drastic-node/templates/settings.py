KEYSPACE = 'drastic'
LOG_LEVEL = 'INFO'
CASSANDRA_HOSTS = [ {% for item in groups['drastic-databases'] %}"{{ hostvars[item]['ansible_' ~ hostvars[item]['cassandra_interface']]['ipv4']['address'] }}",{% endfor %} ]
REPLICATION_FACTOR = {{ cassandra_replication_factor }}
CONSISTENCY_LEVEL = {{ groups['drastic-databases']|length - cassandra_replication_factor + 1 }}
