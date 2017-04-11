KEYSPACE = 'drastic'
LOG_LEVEL = 'INFO'
CASSANDRA_HOSTS = [ {% for item in groups['drastic-databases'] %}"{{ hostvars[item]['ansible_' ~ hostvars[item]['cassandra_interface']]['ipv4']['address'] }}",{% endfor %} ]
GREMLIN_HOST = "{{ hostvars[cassandra_seed_server]['ansible_' ~ hostvars[cassandra_seed_server]['cassandra_interface']]['ipv4']['address'] }}"
REPLICATION_FACTOR = {{ cassandra_replication_factor }}
CONSISTENCY_LEVEL = {{ groups['drastic-databases']|length - cassandra_replication_factor + 1 }}
