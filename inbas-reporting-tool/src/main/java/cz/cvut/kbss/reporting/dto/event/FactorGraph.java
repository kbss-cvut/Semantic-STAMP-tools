package cz.cvut.kbss.reporting.dto.event;

import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.reporting.model.Vocabulary;

import java.util.List;
import java.util.Set;

@OWLClass(iri = Vocabulary.ONTOLOGY_IRI_model_A + "/factor_graph")
public class FactorGraph {

    @OWLObjectProperty(iri = Vocabulary.ONTOLOGY_IRI_model_A + "/has_nodes")
    private List<EventDto> nodes;

    @OWLObjectProperty(iri = Vocabulary.ONTOLOGY_IRI_model_A + "/has_edges")
    private Set<FactorGraphEdge> edges;

    public List<EventDto> getNodes() {
        return nodes;
    }

    public void setNodes(List<EventDto> nodes) {
        this.nodes = nodes;
    }

    public Set<FactorGraphEdge> getEdges() {
        return edges;
    }

    public void setEdges(Set<FactorGraphEdge> edges) {
        this.edges = edges;
    }
}
