/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.kbss.datatools.xmlanalysis.common.graphml;

import com.tinkerpop.blueprints.Vertex;

/**
 *
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
public class ActivityGMLBuilder extends UMLGMLBuilder{
    
    public Vertex addBranch(String label){
        Vertex v = addVertex(label, label);
        v.setProperty("graphics", mapOf("type", "diamond"));
        // styling of the node goes here
        // v.setProperty(atts, v);
        return v;
    }
    
    public Vertex addCircle(String label){
        Vertex v = addVertex(label, label);
        v.setProperty("graphics", mapOf("type", "ellipse"));
        return v;
    }
    
    public Vertex addActivity(String label){
        Vertex v = addVertex(label, label);
        return v;
    }

    public void addUpTriangle(String activityName) {
        addWithShape(activityName, "triangle");
    }

    public void addDownTriagle(String activityName) {
        addWithShape(activityName, "triangle2");
    }
    
    protected Vertex addWithShape(String label, String shape){
        Vertex v = addVertex(label, label);
        v.setProperty("graphics", mapOf("type", shape));
        return v;
    }
}
