import com.datastax.bdp.graph.api.query.Geo
import com.datastax.bdp.graph.api.query.Search
:remote connect tinkerpop.server conf/remote.yaml session-managed
:remote config timeout max
:remote console
system.graph('drasticgraph').replication("{'class' : 'NetworkTopologyStrategy', 'datacenter1' : 1}").systemReplication("{'class' : 'NetworkTopologyStrategy', 'datacenter1' : 1}").option("graph.schema_mode").set("Development").option("graph.allow_scan").set("true").option("graph.default_property_key_cardinality").set("multiple").option("graph.tx_groups.*.write_consistency").set("ONE").ifNotExists().create()
:remote config alias g drasticgraph.g
schema.clear()
schema.propertyKey("URI").Text().single().create()
schema.propertyKey("type").Text().single().create()
schema.propertyKey("value").Text().single().create()
schema.propertyKey("graph").Text().single().create()
schema.vertexLabel("resource").properties('graph','URI').create()
schema.vertexLabel('resource').index('ResourceByURI').secondary().by('URI').add()
schema.vertexLabel('resource').index('ResourceByGraph').secondary().by('graph').add()
schema.vertexLabel("literal").create()
schema.vertexLabel('literal').index('LiteralByValue').secondary().by('value').add()
schema.vertexLabel('literal').index('LiteralByGraph').secondary().by('graph').add()
schema.edgeLabel('statement').properties('graph', 'URI').connection('resource','literal').create()
schema.edgeLabel('statement').connection('resource','resource').add()
