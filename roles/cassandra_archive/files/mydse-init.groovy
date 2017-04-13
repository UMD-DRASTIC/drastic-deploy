import com.datastax.bdp.graph.api.query.Geo
import com.datastax.bdp.graph.api.query.Search
:remote connect tinkerpop.server conf/remote.yaml session-managed
:remote config timeout max
:remote console
system.graph('drasticgraph').replication("{'class' : 'NetworkTopologyStrategy', 'datacenter1' : 1}").systemReplication("{'class' : 'NetworkTopologyStrategy', 'datacenter1' : 1}").option("graph.schema_mode").set("Development").option("graph.allow_scan").set("true").option("graph.default_property_key_cardinality").set("multiple").option("graph.tx_groups.*.write_consistency").set("ONE").ifNotExists().create()
:remote config alias g drasticgraph.g
schema.clear()
schema.propertyKey("drastic:uuid").Text().single().create()
schema.propertyKey("drastic:name").Text().single().create()
schema.vertexLabel("resource").create()
schema.vertexLabel('resource').index('byResourceUUID').secondary().by('drastic:uuid').add()
schema.vertexLabel('resource').index('byResourceName').secondary().by('drastic:name').add()
