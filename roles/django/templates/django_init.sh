#!/bin/sh
env
while ! nc -w 1 -z $CASSANDRA_SEED_SERVER 9042; do
  sleep 0.1;
done;
echo "Seed server $CASSANDRA_SEED_SERVER is up, waiting 10 secs for Voltron.."
sleep 10

{{ install_dir }}/web/bin/python - <<DOC
from cassandra.cluster import Cluster
import os
import socket
cassandra_host = os.getenv('CASSANDRA_SEED_SERVER', 'cassandra-1')
cassandra_host = socket.gethostbyname(cassandra_host)
cluster = Cluster([cassandra_host,])
session = cluster.connect()
session.execute("CREATE KEYSPACE IF NOT EXISTS drastic WITH replication = {'class': 'SimpleStrategy', 'replication_factor': {{ cassandra_replication_factor }} };")
cluster.shutdown()
print('drastic KEYSPACE CREATED')
DOC

sleep 2
python {{ install_dir }}/web/project/manage.py collectstatic --noinput
drastic-admin create
sleep 2
drastic-admin root-collection-create
