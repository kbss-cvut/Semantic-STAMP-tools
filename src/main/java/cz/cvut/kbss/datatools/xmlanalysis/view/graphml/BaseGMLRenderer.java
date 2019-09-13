package cz.cvut.kbss.datatools.xmlanalysis.view.graphml;

import cz.cvut.kbss.datatools.xmlanalysis.common.graphml.GraphMLBuilder;
import cz.cvut.kbss.datatools.xmlanalysis.common.graphml.UMLGMLBuilder;
import cz.cvut.kbss.datatools.xmlanalysis.voc.Vocabulary;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class BaseGMLRenderer<T extends GraphMLBuilder> implements IGraphMLRenderer<T> {

    private static final Logger LOG = LoggerFactory.getLogger(BaseGMLRenderer.class);

    protected T gmlBuilder;
    protected Map<String, NodeRenderer> nodeRendererMap;
    protected Map<String, EdgeRenderer> edgeRendererMap;
    protected EdgeRenderer defaultEdgeRenderer;
    protected PartOfGroupRenderer partOfGroupRenderer;

    public void init(){
        nodeRendererMap = new HashMap<>();
        edgeRendererMap = new HashMap<>();
        gmlBuilder = createGMLBuilder();
        // edge renderers should be initialized after the gml builder;
        defaultEdgeRenderer = new EdgeRenderer();
        partOfGroupRenderer = new PartOfGroupRenderer();

        buildRendererMaps();
    }

    protected abstract T createGMLBuilder();

    protected abstract void buildRendererMaps();

    protected void bindTypeToRenderer(Resource typeURI, Consumer<GraphMLBuilder.NodeData> renderer){
        nodeRendererMap.put(typeURI.toString(), new NodeRenderer(renderer, typeURI));
    }

    protected void bindPropertyToRenderer(Resource propertyURI, Consumer<UMLGMLBuilder.EdgeData> renderer){
        bindPropertyToRenderer(propertyURI, new EdgeRenderer(renderer));
    }
    protected void bindPropertyToRenderer(Resource propertyURI, EdgeRenderer renderer){
        edgeRendererMap.put(propertyURI.toString(), renderer);
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
        res.listProperties(RDF.type)
                .mapWith(s -> s.getObject())
                .filterKeep(n -> n.isURIResource())
                .mapWith(n -> nodeRendererMap.get(n.toString()))
                .filterDrop(r -> r == null)
                .nextOptional()
                .ifPresent(r -> r.render(res));
    }

    public void renderEdges(Model m){
        List<Resource> edges = getNodesToRender(m);
        for(Resource e : edges){
            renderEdge(e);
        }
    }

    protected void renderEdge(Resource res){
        res.listProperties(RDF.type)
                .mapWith(s -> s.getObject())
                .filterKeep(n -> n.isURIResource())
                .mapWith(n -> edgeRendererMap.get(n.toString()))
                .filterDrop(r -> r == null)
                .nextOptional()
                .ifPresent(r ->
                {
                    LOG.info("rendering edge <{}>", res);
                    r.render(res);
                });
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
                    .map(r -> Vocabulary.localName(r.toString()))
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
            String label = getLabel(r);
            if(label == null || label.isEmpty()) {
                LOG.warn("the resource <{}> does not have label.", r);
                return null;
            }
            LOG.debug("rendering node <{}>  with label \"{}\"", r.toString(), label);

            return new GraphMLBuilder.NodeData(getType(), label);
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

        public void render(Resource r) {
//            Pair<String, String> pair = Pair.of(
//                    r.getProperty(Vocabulary.j_p_from).getObject().toString(),
//                    r.getProperty(Vocabulary.j_p_to).getObject().toString()
//            );

            GraphMLBuilder.NodeData from = toNodeData(r.getProperty(Vocabulary.j_p_from).getObject());
            GraphMLBuilder.NodeData to = toNodeData(r.getProperty(Vocabulary.j_p_to).getObject());

            if(from == null || to == null){
                LOG.warn("reified edge (from-to reification style) is missing a source or a target. Edge uri = <{}>", r.toString());
                return;
            }
            render(from, to);
        }

        protected GraphMLBuilder.NodeData toNodeData(RDFNode edgeEnd){
            if(edgeEnd.isResource()){
                // find the type of the edge end
                Resource r = edgeEnd.asResource();
                Resource type = r.listProperties(RDF.type)
                        .mapWith(s -> s.getObject())
                        .filterKeep(o -> o.isURIResource())
                        .mapWith(o -> o.asResource())
                        .filterKeep(t -> nodeRendererMap.containsKey(t.toString()))
                        .nextOptional().orElse(null);

                // Hack - reusing NodeData setup code from NodeRenderer
                NodeRenderer nr = new NodeRenderer();
                nr.setNodeType(type);
                return nr.createNodeData(r);
            }else {
                return new GraphMLBuilder.NodeData("Literal", edgeEnd.toString());
            }
        }

        public void render(GraphMLBuilder.NodeData source, GraphMLBuilder.NodeData target) {
            try {
                renderImpl(source, target, edgeLabel);
            }catch(NullPointerException e){
                LOG.warn("One of the end nodes of the edge is not found in the graphs nodes.", e);
            }
        }

        protected void renderImpl(GraphMLBuilder.NodeData source, GraphMLBuilder.NodeData target, String edgeLabel){
            renderer.accept(new UMLGMLBuilder.EdgeData(source, target, edgeLabel));
        }
    }

    class PartOfGroupRenderer extends EdgeRenderer{
        @Override
        public void renderImpl(GraphMLBuilder.NodeData source, GraphMLBuilder.NodeData target, String edgeLabel){
            gmlBuilder.addToGroup(source.getId(), target.getId());
        }
    }
}
