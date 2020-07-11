package cz.cvut.kbss.datatools.bpm2stampo.common.graphml;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

public class RiskPoolGMLBuilder extends UMLGMLBuilder {


    public Vertex addRisk(NodeData nodeData){
        setTitledLabel(nodeData);
        return addWithShape(nodeData,"triangle", "fill", "#FF0000");
    }

    public Edge addGroupedIntoRisk(EdgeData ed){
        ed.setLabel("groupedIntoRisk");
        Edge edge = addEdgeSafe(ed);
        if(edge != null){
            edge.setProperty("graphics",mapOf(
                    "sourceArrow", "t_shape"
            ));
        }
        return edge;
    }

}
