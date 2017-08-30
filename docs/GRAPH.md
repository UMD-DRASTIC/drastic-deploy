# Using the Graph Database

The DRAS-TIC deployment playbooks for the DataStax Enterprise Edition of Cassandra include the option to setup their Graph Database product. DRAS-TIC does not use this graph database by default, however, you can configure a workflow using drastic-jobs that will index your content and metadata in the graph database. An [example graph database indexing workflow](https://github.com/UMD-DRASTIC/drastic-jobs/blob/master/jobs/graph.py) is provided in the drastic-jobs project. Because DRAS-TIC integrates with the graph database via it's messaging system, changes to the repository are indexed after they happen. In most cases this is suitable for repository or archival systems. If you have more transactional needs in a graph database, we recommend a different or additional solution.

Note that the DSE Graph product works of a WebSocket connection, instead of normal HTTP. Your clients will need to support WebSockets.


## Gremlin and Tinkerpop

DRAS-TIC interacts with the DSEGraph product through standard Tinkerpop and Gremlin protocols. This means that you are free to install a different standalone or Cassandra-based product if needed. One that we will try at some point is JanusGraph, which also runs on Cassandra clusters. The Gremlin code should be the same, but the setup playbook will need to be different.


## Graph Schema

The graph database schema is detailed in a Gremlin initialization script that we use to create the database:

https://github.com/UMD-DRASTIC/drastic-deploy/blob/master/roles/cassandra/files/mydse-init.groovy

Note that we currently do not use the "literal" vertex label or the "statement" edge label from that schema. These are RDF concepts that may be explored further later on. For now we use simple vertex properties for key/value pairs, including user metadata from DRAS-TIC. We have one edge label for the contains relationship, between folders and their contents. There is also some indexing to improve retrieval times.


## A Quick Start into Queries

The fastest way to get started is to open the DSE Gremlin Console on the command-line of your seed Cassandra server.

```
$ dse gremlin-console
```

This will start the client, which takes a minute. Then you can use Gremlin commands to connect to your server and send a query:

```
TODO: Supply a tested connection line and query..
```


## Querying the Graph in Code

To query the graph you will need to attach to the Gremlin server with a Gremlin client. Example Python code for this is found in the graph workflow script above. You will need the gremlin library as well:

```
$ pip install gremlinpython
```

There is a long list of additional language drivers and clients at http://tinkerpop.apache.org/.
