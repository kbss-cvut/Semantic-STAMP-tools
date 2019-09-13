/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.kbss.datatools.xmlanalysis.common.graphml;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.blueprints.util.io.gml.GMLWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
public class GraphMLBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(GraphMLBuilder.class);
    
    protected TinkerGraphIdTranslation g = new TinkerGraphIdTranslation();


    protected long currentEdgeId = 0;

    public long getNextEdgeId(){
        return currentEdgeId++;
    }

    public Graph getG() {
        return g;
    }

    public void setG(TinkerGraphIdTranslation g) {this.g = g;}



    protected Vertex addWithCustomShape(NodeData nodeData, String shape, String ...props){
        Vertex v = add(nodeData);
        v.setProperty("graphics", addToMap(mapOf("customconfiguration", shape), props));
        return v;
    }

    protected Vertex addWithShape(NodeData nodeData, String shape, String ... props){
        Vertex v = add(nodeData);

        v.setProperty("graphics", addToMap(mapOf("type", shape),props));
        return v;
    }

    protected Vertex add(NodeData nodeData){
        return addVertexSafe(nodeData.getLabel(), nodeData.getId());
    }

    public Vertex addVertexSafe(String label, String sid){
        Vertex v = g.getVertex(sid);
        if(v == null){
            v = addVertex(label, sid);
        }
        return v;
    }
    
    protected Vertex addVertex(String label, String sid){
        Vertex v = g.addVertex(sid);
        v.setProperty("label", label);
//        v.setProperty("sid", getNextNodeId());
        return v;
    }

    public Vertex addGroup(NodeData nodeData){
        Vertex v = addVertexSafe(nodeData.getLabel(), nodeData.getId());
//        v.setProperty("graphics", mapOf(
//                "group", 	"1",
//                "autoResize", "1",
//                "closed", "0",
//                "customconfiguration",	"YED_TABLE_NODE",
//                "property", mapOf(
//                        "name", "yed.table.header.color.main",
//                        "valueClass",	"java.lang.String",
//                        "value",	"lane.style.columns"
//                    )
//                )
//        );
        v.setProperty("isGroup", 1);
        return v;
    }

    /**
     * Gets the vertecis from the graph and an edge between them using Graph.addEdge
     * @param id
     * @param p
     * @param c
     * @param label
     * @return
     */
    public Edge addEdge(Object id, Object p, Object c, String label) {
        Vertex pv = g.getVertex(p);
        Vertex cv = g.getVertex(c);
        if(pv == null || cv == null){
            LOG.error("some of the vertices are missing, (\"{}\" = \"{}\" -> \"{}\" = \"{}\")", p, pv, c, cv);
            // do not add the edge!
            return null;
        }
        return g.addEdge(id, pv, cv, label);
    }

    public Edge addEdge(Object p, Object c, String label) {
        return addEdge(getNextEdgeId(), p, c, label);
    }
    
    public Edge addEdgeSafe(EdgeData edgeData) {
        return addEdgeSafe(edgeData.getSource().getId(), edgeData.getTarget().getId(), edgeData.getLabel());
    }

    public Edge addEdgeSafe(Object p, Object c, String label) {
        return addEdgeSafe(getNextEdgeId(), p, c, label);
    }


    public Edge addEdgeSafe(Object id, Object p, Object c, String label) {
        Edge edge = g.getEdge(id);
        if(edge != null){
            return edge;
        }
        return addEdge(id, p, c, label);
    }

    public Vertex addToGroup(Object childG, Object parentG){
        Vertex c = g.getVertex(childG);
        Integer pid = g.translateId(parentG);
        if(c == null || pid == null){
            LOG.warn("Found a corrupted part of group relation between parent <{}>, child <{}>", parentG, childG);
            return c;
        }
        c.setProperty("gid", pid);
        return c;
    }

    public void setTitledLabel(NodeData nodeData){
        String html = titledLabel(nodeData);
        nodeData.setLabel(html);
    }

    public String titledLabel(NodeData nodeData){
        return String.format(
                "<html><body>" +
                        "<div align='center' style='font-weight:bold;border-bottom: 1px solid #000;'>%s</div>" +
                        "<div align='center'>%s</div>" +
                        "</body></html>",
                nodeData.getNodeType(), nodeData.getName()
        );
    }
    
    public void writeToConsole() {
        try{
            write(System.out);
        }catch(IOException ex){
            LOG.error("Error writing graph ml to console.", ex);
        }
    }

    public void write(OutputStream os) throws IOException{
        GMLWriter w = new GMLWriter(g);
        w.setUseId(true);
        w.outputGraph(os);
    }

    public static class NodeData{
        protected String nodeType;
        protected String name;
        protected String label;

        public NodeData() {
        }

        public NodeData(String nodeType, String name) {
            this.nodeType = nodeType;
            this.name = name;
            this.label = name; // default label
        }

        public String getNodeType() {
            return nodeType;
        }

        public void setNodeType(String nodeType) {
            this.nodeType = nodeType;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getId(){
            return String.format("%s-%s", nodeType, name);
        }
    }

    public static class EdgeData{
        protected NodeData source;
        protected NodeData target;
        protected String label;

        public EdgeData() {

        }

        public EdgeData(String sourceType, String source, String targetType, String target) {
            this(sourceType,source,targetType,target,"");
        }

        public EdgeData(String sourceType, String source, String targetType, String target, String label) {
            this(new NodeData(sourceType, source), new NodeData(targetType, target), label);
        }

        public EdgeData(NodeData source, NodeData target, String label) {
            this.source = source;
            this.target = target;
            this.label = label;
        }

        public NodeData getSource() {
            return source;
        }

        public void setSource(NodeData source) {
            this.source = source;
        }

        public NodeData getTarget() {
            return target;
        }

        public void setTarget(NodeData target) {
            this.target = target;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }

    
    public static <T> Map<T,T> mapOf(Class<T> cls, T ... entries){
        Map<T, T> map = new HashMap<>();
        addToMap(map, entries);
        return map;
    }

    public static <T> Map<T,T> addToMap(Map<T,T> map, T ... entries){
        for(int i = 0; i < entries.length; i += 2){
            if(i + 1 == entries.length ){
                LOG.warn("Ignoring the last key-valy with key {}, the pair has no value.");
            }else{
                map.put(entries[i], entries[i+1]);
            }
        }
        return map;
    }

    public static Map<String,String> mapOf(String ... entries){
        return mapOf(String.class, entries);
    }

    public static Map<Object,Object> mapOf(Object ... entries){
        return mapOf(Object.class, entries);
    }
    
    public static void main(String[] args) {
        
        UMLGMLBuilder gb = new UMLGMLBuilder();
        gb.addEntity("Person", "1", mapOf("age", "Integer", "name", "String"));
        gb.addEntity("Brain", "2", mapOf("age", "Integer", "weight", "Integer"));
        gb.addPartOf(gb, "1", "2", "1", "1");
        String fileName = "tmp.gml";
        try(FileOutputStream fs = new FileOutputStream(fileName)){
            gb.write(fs);
        }catch(IOException ex){
            LOG.error(String.format("Could not write graph to file \"%s\"", fileName), ex);
        }
    }   
}