KEYSPACE = 'drastic'
LOG_LEVEL = 'INFO'
CASSANDRA_HOSTS = [ {% for item in cassandra_hosts %}"{{ item }}",{% endfor %} ]
GREMLIN_HOST = "{{ gremlin_host }}"
REPLICATION_FACTOR = {{ cassandra_replication_factor }}
CONSISTENCY_LEVEL = {{ cassandra_hosts|length - cassandra_replication_factor + 1 }}
