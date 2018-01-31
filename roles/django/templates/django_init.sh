#!/bin/sh
while ! nc -w 1 -z cassandra-1 9042;
do
    sleep 0.1;
done;
{{ install_dir }}/web/bin/python - <<DOC
from cassandra.cluster import Cluster
import os
import socket
cassandra_host = os.getenv('CASSANDRA_HOST', 'cassandra-1')
cassandra_host = socket.gethostbyname(cassandra_host)
cluster = Cluster([cassandra_host,])
session = cluster.connect()
session.execute("CREATE KEYSPACE IF NOT EXISTS drastic WITH replication = {'class': 'SimpleStrategy', 'replication_factor': {{ cassandra_replication_factor }} };")
cluster.shutdown()
DOC

{{ install_dir }}/web/bin/python {{ install_dir }}/web/project/manage.py collectstatic --noinput

{{ install_dir }}/web/bin/python {{ install_dir }}/web/project/manage.py makemigrations

{{ install_dir }}/web/bin/python {{ install_dir }}/web/project/manage.py migrate
