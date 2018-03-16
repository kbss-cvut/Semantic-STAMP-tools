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
    
    protected Graph g = new TinkerGraph();

    public Graph getG() {
        return g;
    }

    public void setG(Graph g) {
        this.g = g;
    }
    
    public Vertex addVertexSafe(String label, String id){
        Vertex v = g.getVertex(id);
        if(v == null){
            v = addVertex(label, id);
        }
        return v;
    }
    
    public Vertex addVertex(String label, String id){
        Vertex v = g.addVertex(id);
        v.setProperty("label", label);
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
        return g.addEdge(id, pv, cv, label);
    }
    
    public Edge addEdgeSafe(Object id, Object p, Object c, String label) {
        Edge edge = g.getEdge(id);
        if(edge != null){
            return edge;
        }
        return addEdge(id, p, c, label);
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
        w.outputGraph(os);
    }
    
    
    public static Map<String,String> mapOf(String ... entries){
        Map<String,String> map = new HashMap<>();
        for(int i = 0; i < entries.length; i += 2){
            if(i + 1 == entries.length ){
                LOG.warn("Ignoring the last key-valy with key {}, the pair has no value.");
            }else{
                map.put(entries[i], entries[i+1]);
            }
        }
        return map;
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