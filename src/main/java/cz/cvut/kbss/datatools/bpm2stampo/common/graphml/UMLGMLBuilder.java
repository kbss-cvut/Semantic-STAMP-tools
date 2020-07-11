/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.kbss.datatools.bpm2stampo.common.graphml;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
public class UMLGMLBuilder extends GraphMLBuilder{

    private static final Logger LOG = LoggerFactory.getLogger(UMLGMLBuilder.class);

    public Vertex addAggregation(NodeData nodeData){
        return addGroup(nodeData);
    }

    public Vertex addCrossReference(NodeData nodeData){
        return addWithCustomShape(nodeData, "com.yworks.flowchart.offPageReference");
    }

    public Vertex addEntity(String entityType, String vertexId, Map<String, String> entityAttributes) {
        String atts = "";
        for (Map.Entry<String, String> e : entityAttributes.entrySet()) {
            atts += "\n - " + e.getKey() + " : " + e.getValue();
        }
        Vertex v = addVertex(entityType + atts, vertexId);
        // styling of the node goes here
        // v.setProperty(atts, v);
        return v;
    }

    public Edge addPartOf(Object id, Object whole, Object part, String min, String max) {
        Edge e = addComposition(id, whole, part, min, max);// whole - source, part - target
        e.setProperty("graphics", mapOf("sourceArrow", "diamond"));
        return e;
    }
    
    public Edge addLinkToEntity(Object id, Object p, Object c, String min, String max) {
        Edge e = addComposition(id, p, c, min, max);
        e.setProperty("graphics", mapOf("sourceArrow", "white_diamond", "fill", "#00FF00"));
        return e;
    }

    public Edge addComposition(Object id, Object source, Object target, String min, String max) {
        Edge e = addEdge(id, source, target, "");
        if (!min.isEmpty() || !max.isEmpty()) {
            String card = "[" + min + (!min.isEmpty() ? ".." : "") + ("unbounded".equals(max) ? "*" : max) + "]";
            e.setProperty("LabelGraphics", mapOf("text", card, "model", "six_pos", "position", "ttail"));
        }
        return e;
    }

}
