import socket

def ips(hostnames):
    result = []
    for v in hostnames:
        try:
            ip = socket.gethostbyname(v)
            result.append(ip)
        except Exception as e:
            pass
    return result

KEYSPACE = 'drastic'
LOG_LEVEL = 'INFO'
hostnames = [ {% for item in cassandra_hosts %}'{{ item }}',{% endfor %} ]
CASSANDRA_HOSTS = ips(hostnames)
GREMLIN_HOST = "{{ gremlin_host }}"
REPLICATION_FACTOR = {{ cassandra_replication_factor }}
CONSISTENCY_LEVEL = {{ cassandra_hosts|length - cassandra_replication_factor + 1 }}
