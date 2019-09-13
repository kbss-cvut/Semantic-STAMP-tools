package cz.cvut.kbss.datatools.xmlanalysis.common.graphml;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

public class WorkingEnvironmentBuilder extends UMLGMLBuilder {

    public Vertex addOrganizationalUnit(NodeData nodeData){
        return addVertexWithTitleAndLabel(nodeData);
    }

    public Vertex addPerformer(NodeData nodeData){
        return addVertexWithTitleAndLabel(nodeData);
    }

    public Vertex addRole(NodeData nodeData){
        return addVertexWithTitleAndLabel(nodeData);
    }

    public Edge addIsSubordinated(EdgeData ed){
        ed.setLabel("isSubordinated");
        Edge edge = addEdgeSafe(ed);
        if(edge != null){
            edge.setProperty("graphics",mapOf(
                    "targetArrow", "plain"
                    ));
        }
        return edge;
    }

    public Edge addBelongsTo(EdgeData ed){
        ed.setLabel("belongsTo");
        Edge edge = addEdgeSafe(ed);
        if(edge != null){
            edge.setProperty("graphics",mapOf(
                    "sourceArrow", "circle",
                    "targetArrow", "delta"
                    ));
        }
        return edge;
    }

    public Edge addIsManager(EdgeData ed){
        ed.setLabel("isManager");
        Edge edge = addEdgeSafe(ed);
        if(edge != null){
            edge.setProperty("graphics",mapOf(
                    "sourceArrow", "t_shape",
                    "targetArrow", "plain"
                    ));
        }
        return edge;
    }


    public Edge addHasRole(EdgeData ed){
        ed.setLabel("hasRole");
        Edge edge = addEdgeSafe(ed);
        if(edge != null){
            edge.setProperty("graphics",mapOf(
                    "style", "dashed",
                    "targetArrow", "plainn"
                    ));
        }
        return edge;
    }

    protected Vertex addVertexWithTitleAndLabel(NodeData nodeData){
//        String html = String.format("<html><body><h3 align='center'>%s</h3>%s</body></html>",typeName, label);
        setTitledLabel(nodeData);
        return add(nodeData); // label could be type + label
    }
}
