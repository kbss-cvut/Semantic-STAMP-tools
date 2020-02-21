package cz.cvut.kbss.datatools.xmlanalysis.common.graphml;

import cz.cvut.kbss.onto.safety.stamp.Vocabulary;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Consumer;

public abstract class BaseGMLRenderer<T extends GraphMLBuilder> implements IGraphMLRenderer<T> {

    private static final Logger LOG = LoggerFactory.getLogger(BaseGMLRenderer.class);

    protected T gmlBuilder;
    protected List<String> nodeMappingOrder;
    protected List<String> edgeMappingOrder;
    protected Map<String, NodeRenderer> nodeRendererMap;
    protected Map<String, EdgeRenderer> edgeRendererMap;
    protected EdgeRenderer defaultEdgeRenderer;
    protected PartOfGroupRenderer partOfGroupRenderer;
    protected LabelRenderer labelRenderer = new LabelRenderer();

    protected Property from = ResourceFactory.createProperty(Vocabulary.s_p_from_structure_component);
    protected Property to = ResourceFactory.createProperty(Vocabulary.s_p_to_structure_component);

    public void init(){
        nodeRendererMap = new HashMap<>();
        edgeRendererMap = new HashMap<>();
        nodeMappingOrder = new ArrayList<>();
        edgeMappingOrder = new ArrayList<>();
        gmlBuilder = createGMLBuilder();
        // edge renderers should be initialized after the gml builder;
        defaultEdgeRenderer = new EdgeRenderer();
        partOfGroupRenderer = new PartOfGroupRenderer();

        buildRendererMaps();
    }

    protected abstract T createGMLBuilder();

    protected abstract void buildRendererMaps();

    protected void bindTypeToRenderer(String typeURI, Consumer<GraphMLBuilder.NodeData> renderer){
        bindTypeToRenderer(ResourceFactory.createResource(typeURI), renderer);
    }

    protected void bindTypeToRenderer(Resource typeURI, Consumer<GraphMLBuilder.NodeData> renderer){
        nodeMappingOrder.add(typeURI.toString());
        nodeRendererMap.put(typeURI.toString(), new NodeRenderer(renderer, typeURI));
    }

    protected void bindPropertyToRenderer(String typeURI, Consumer<GraphMLBuilder.EdgeData> renderer){
        bindPropertyToRenderer(typeURI, new EdgeRenderer(renderer));
    }

    protected void bindPropertyToRenderer(Resource propertyURI, Consumer<UMLGMLBuilder.EdgeData> renderer){
        bindPropertyToRenderer(propertyURI, new EdgeRenderer(renderer));
    }

    protected void bindPropertyToRenderer(Resource propertyURI, EdgeRenderer renderer){
        bindPropertyToRenderer(propertyURI.toString(), renderer);
    }

    protected void bindPropertyToRenderer(String propertyURI, EdgeRenderer renderer){
        edgeMappingOrder.add(propertyURI);
        edgeRendererMap.put(propertyURI, renderer);
    }

    public T render(Model m){
        renderNodes(m);
        renderEdges(m);
        return gmlBuilder;
    }

    protected List<Resource> getNodesToRender(Model m){
        return m.listResourcesWithProperty(RDF.type).toList();
//        return m.listStatements(null, RDF.type, (Resource)null)
//                .mapWith(s -> s.getObject())
//                .filterKeep(n -> n.isURIResource())
//                .mapWith(n -> n.asResource())
//                .filterKeep(t -> )
    }


    protected void renderNodes(Model m){
        // find nodes to be rendered
        List<Resource> nodes = getNodesToRender(m);
        for(Resource n : nodes) {
            renderNode(n);
        }
    }

    protected void renderNode(Resource res){
//        res.listProperties(RDF.type)
//                .mapWith(s -> s.getObject())
//                .filterKeep(n -> n.isURIResource())
//                .mapWith(n -> nodeRendererMap.get(n.toString()))
//                .filterDrop(r -> r == null)
//                .nextOptional()
//                .ifPresent(r -> r.render(res));
        String type = getTypeWithMaxPriorityRenderer(res);
        Optional.ofNullable(type)
                .map(t -> nodeRendererMap.get(t))
                .ifPresent(r -> r.render(res));
    }

    public void renderEdges(Model m){
        Set<String> renderedEdges = new HashSet<>();
        List<Resource> edges = getNodesToRender(m);
        // render reified edges
        for(Resource e : edges){
            Pair<RDFNode, RDFNode> s = getSourceAndTarget(e);
            if(s == null) continue;
            Resource propType = renderEdge(e, s.getLeft(), s.getRight());
            if(propType != null)
                renderedEdges.add(toId(s.getLeft(), propType, s.getRight()));
        }
        // render regular edges if they were not rendered as reified
        StmtIterator iter = m.listStatements();
        while(iter.hasNext()){
            Statement s = iter.nextStatement();
            if(renderedEdges.contains(toId(s))) continue; // skip edges which were rendered as reified
            renderEdge(s);
        }
    }

    public Pair<RDFNode, RDFNode> getSourceAndTarget(Resource r) {
        RDFNode fromN = Optional.ofNullable(r.getProperty(BaseGMLRenderer.this.from)).map(s -> s.getObject()).orElse(null);
        RDFNode toN = Optional.ofNullable(r.getProperty(BaseGMLRenderer.this.to)).map(s -> s.getObject()).orElse(null);
        if(fromN != null && toN != null) {
            return Pair.of(fromN, toN);
        }
        return null;
    }

    protected String toId(RDFNode s, RDFNode p, RDFNode o){
        return s + "-" + p + "-" + s;
    }
    protected String toId(Statement s){
        return toId(s.getSubject(), s.getPredicate(), s.getObject());
    }

    public void renderEdge(Statement s){
        EdgeRenderer renderer = edgeRendererMap.get(s.getPredicate().toString());
        if(renderer != null) {
            renderer.render(s.getPredicate(), null, s.getSubject(), s.getObject());
        }
    }

    protected Resource renderEdge(Resource prop, RDFNode from, RDFNode to){
        Resource propType =
                prop.listProperties(RDF.type)
                .mapWith(s -> s.getObject())
                .filterKeep(n -> n.isURIResource())
                .mapWith(n -> n.asResource())
                .filterKeep(r -> edgeRendererMap.containsKey(r.toString()))
                .nextOptional().orElse(null);
        if(propType == null)
            return null;
        EdgeRenderer renderer = edgeRendererMap.get(propType.toString());
        if(renderer != null){
            LOG.info("rendering edge <{}>", propType);
            if(renderer.render(propType, prop, from, to)){
                return propType;
            }
        }
        return null;
    }

    protected String getLabel(Resource res){
        RDFNode n = Optional.ofNullable(res)
                .map(r -> r.getProperty(RDFS.label))
                .map(s -> s.getObject()).orElse(null);
        if(n == null){
            LOG.error("Found resource <{}> with no label!", res.toString());
        }
        LOG.debug("{}", Optional.ofNullable(n).map(o -> o.toString()).orElse(null));
        return  Optional.ofNullable(n)
                .map(l -> l.toString())
                .map(l -> l.trim())
                .orElse(null);
    }


    protected String getTypeWithMaxPriorityRenderer(Resource res){
        List<String> types = res.listProperties(RDF.type)
                .mapWith(s -> s.getObject())
                .filterKeep(n -> n.isURIResource())
                .mapWith(n -> n.toString())
                .toList();
        return getMaxPriorityMapping(types, nodeMappingOrder);
    }

    protected String getMaxPriorityMapping(Collection<String> applicable, List<String> order){
        for(String t : order){
            if(applicable.contains(t))
                return t;
        }
        return null;
    }

    class NodeRenderer{
        protected Consumer<GraphMLBuilder.NodeData> renderer;
        protected Resource nodeType;


        // cache
        protected String type;

        public NodeRenderer() {
        }

        public NodeRenderer(Consumer<GraphMLBuilder.NodeData> renderer) {
            this.renderer = renderer;
        }

        public NodeRenderer(Consumer<GraphMLBuilder.NodeData> renderer, Resource nodeType) {
            this.renderer = renderer;
            this.nodeType = nodeType;
        }

        public Resource getNodeType() {
            return nodeType;
        }

        public void setNodeType(Resource nodeType) {
            this.nodeType = nodeType;
        }

        public String getType() {
            if(type == null){
                type = calculateType();
            }
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        protected String calculateType(){
            return Optional.ofNullable(nodeType)
                    .map(r -> labelRenderer.localName(r.toString()))
                    .orElse(null);
        }

        public Consumer<GraphMLBuilder.NodeData> getRenderer() {
            return renderer;
        }

        public void setRenderer(Consumer<GraphMLBuilder.NodeData> renderer) {
            this.renderer = renderer;
        }

        public void render(Resource r) {
            GraphMLBuilder.NodeData nodeData = createNodeData(r);
            if(nodeData == null)
                return;
            renderImpl(nodeData);
        }

        public void renderImpl(GraphMLBuilder.NodeData nodeData){
            renderer.accept(nodeData);
        }

        public GraphMLBuilder.NodeData createNodeData(Resource r){
            String name = r.toString();
            String label = getLabel(r);
            if(label == null || label.isEmpty()) {
                LOG.warn("the resource <{}> does not have label.", r);
                label = "";
//                return null;
            }

            label = label.replaceAll("&", "&amp;");// #&26;

            LOG.debug("rendering node <{}>  with label \"{}\"", r.toString(), label);

            return new GraphMLBuilder.NodeData(getType(), name, label);
        }

    }

    class EdgeRenderer{
        protected String edgeLabel;
        protected Consumer<UMLGMLBuilder.EdgeData> renderer;

        public EdgeRenderer() {
            this("");
        }

        public EdgeRenderer(Consumer<UMLGMLBuilder.EdgeData> renderer) {
            this("", renderer);
        }

        public EdgeRenderer(String edgeLabel) {
            this(edgeLabel, gmlBuilder::addEdgeSafe);
        }

        public EdgeRenderer(String edgeLabel, Consumer<UMLGMLBuilder.EdgeData> renderer) {
            this.edgeLabel = edgeLabel;
            this.renderer = renderer;
        }

        public String getEdgeLabel() {
            return edgeLabel;
        }

        public void setEdgeLabel(String edgeLabel) {
            this.edgeLabel = edgeLabel;
        }

        protected GraphMLBuilder.NodeData toNodeData(RDFNode edgeEnd){
            if(edgeEnd == null)
                 return null;

            if(edgeEnd.isResource()){
                // find the type of the edge end
                Resource r = edgeEnd.asResource();
                Resource type = Optional.ofNullable(getTypeWithMaxPriorityRenderer(r))
                        .map(ResourceFactory::createResource)
                        .orElse(null);
//                        r.listProperties(RDF.type)
//                        .mapWith(s -> s.getObject())
//                        .filterKeep(o -> o.isURIResource())
//                        .mapWith(o -> o.asResource())
//                        .filterKeep(t -> nodeRendererMap.containsKey(t.toString()))
//                        .nextOptional().orElse(null);

                // Hack - reusing NodeData setup code from NodeRenderer
                NodeRenderer nr = new NodeRenderer();
                nr.setNodeType(type);
                return nr.createNodeData(r);
            }else {
                return new GraphMLBuilder.NodeData("Literal", edgeEnd.toString());
            }
        }


        public boolean render(Resource edgeType, Resource edgeInstance, RDFNode fromN, RDFNode toN){
            GraphMLBuilder.NodeData from = toNodeData(fromN);
            GraphMLBuilder.NodeData to = toNodeData(toN);

            if(from == null || to == null){
                LOG.warn("reified edge (from-to reification style) is missing a source or a target. \ns={}\np={} - <{}>\no={}",
                        fromN,
                        edgeInstance, edgeType,
                        toN
                        );
                return false;
            }
            return render(from, to);
        }

        public boolean render(GraphMLBuilder.NodeData source, GraphMLBuilder.NodeData target) {
            try {
                renderImpl(source, target, edgeLabel);
                return true;
            }catch(NullPointerException e){
                LOG.warn("One of the end nodes of the edge is not found in the graphs nodes.", e);
            }
            return false;
        }

        protected void renderImpl(GraphMLBuilder.NodeData source, GraphMLBuilder.NodeData target, String edgeLabel){
            renderer.accept(new UMLGMLBuilder.EdgeData(source, target, edgeLabel));
        }
    }

    class PartOfGroupRenderer extends EdgeRenderer{

        public PartOfGroupRenderer() {
            this.edgeLabel = "";
            this.renderer = gmlBuilder::addPartOfEdgeSafe;
        }

//        @Override
//        public void renderImpl(GraphMLBuilder.NodeData source, GraphMLBuilder.NodeData target, String edgeLabel){
////            gmlBuilder.addToGroup(source.getId(), target.getId());
//            gmlBuilder.addPartOfEdge(source.getId(), target.getId());
//        }
    }
}
