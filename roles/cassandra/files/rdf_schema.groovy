system.graph('testdrasticgraph').replication("{'class' : 'NetworkTopologyStrategy', 'datacenter1' : 1}").systemReplication("{'class' : 'NetworkTopologyStrategy', 'datacenter1' : 1}").option("graph.schema_mode").set("Development").option("graph.allow_scan").set("true").option("graph.default_property_key_cardinality").set("multiple").option("graph.tx_groups.*.write_consistency").set("ONE").ifNotExists().create()
:remote config alias g testdrasticgraph.g

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

l = get_g().addV('literal').property('graph','uuid:1').property('type','xsd:string').property('value','Foo').next().id
r = get_g().addV('resource').property('URI','uuid:1').property('graph','uuid:1').next().id
e = get_g().V(r).addE('statement').property('URI','dcterms:title').to(get_g().V(l)).next()


# More succinct traversal with RDF literal edge
l = get_g().addV('literal').property('graph','uuid:1').property('type','xsd:string').property('value','Foo')
r = get_g().addV('resource').property('URI','uuid:1').property('graph','uuid:1')
g = r.addE('statement').property('URI','dcterms:title').to(l).inV()

# Programmatic building the RDF graph update w/multiple statements
q = get_g().addV('resource').property('URI','uuid:1').property('graph','uuid:1')
# Repeat for all statements:
q = q.addE('statement').property('URI','dcterms:title')
q = q.to(get_g().addV('literal').property('graph','uuid:1').property('type','xsd:string').property('value','Foo')).outV()
q.valueMap().next()
