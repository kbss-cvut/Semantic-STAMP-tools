package cz.cvut.kbss.inbas.reporting.dto.event;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EventGraph {

    private List<EventDto> nodes;

    private Set<EventGraphEdge> edges;

    public List<EventDto> getNodes() {
        return nodes;
    }

    public void setNodes(List<EventDto> nodes) {
        this.nodes = nodes;
    }

    public void addNode(EventDto node) {
        if (nodes == null) {
            this.nodes = new ArrayList<>();
        }
        nodes.add(node);
    }

    public Set<EventGraphEdge> getEdges() {
        return edges;
    }

    public void setEdges(Set<EventGraphEdge> edges) {
        this.edges = edges;
    }

    public void addEdge(EventGraphEdge edge) {
        if (edges == null) {
            this.edges = new HashSet<>();
        }
        edges.add(edge);
    }
}
