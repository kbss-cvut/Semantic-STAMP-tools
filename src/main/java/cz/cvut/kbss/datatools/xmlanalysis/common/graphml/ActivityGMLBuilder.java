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


    public Vertex addBranch(NodeData nodeData){
        return addWithShape(nodeData, "diamond");
    }
    
    public Vertex addCircle(NodeData nodeData){
        return addWithShape(nodeData, "ellipse");
    }

    public Vertex addProcessStart(NodeData nodeData){
        return addWithCustomShape(nodeData, "com.yworks.bpmn.Event.withShadow");
    }

    public Vertex addProcessEnd(NodeData nodeData){
        return addWithCustomShape(nodeData, "com.yworks.bpmn.Event.withShadow");
    }

    public Vertex addTrigger(NodeData nodeData){
        return addWithCustomShape(nodeData, "com.yworks.bpmn.Event.withShadow");
    }

    public Vertex addSwimLane(NodeData nodeData){
        return addGroup(nodeData);
    }
    
    public Vertex addActivity(NodeData nodeData){
        return addWithCustomShape(nodeData, "com.yworks.flowchart.process");
    }

    public Vertex addSubProcess(NodeData nodeData){
        return addWithCustomShape(nodeData, "com.yworks.flowchart.predefinedProcess");
    }

    public Vertex addUpTriangle(NodeData nodeData) {
        return addWithShape(nodeData, "triangle");
    }

    public Vertex addDownTriagle(NodeData nodeData) {
        return addWithShape(nodeData, "triangle2");
    }

}
