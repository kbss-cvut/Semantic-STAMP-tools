package cz.cvut.kbss.reporting.dto.event;

import cz.cvut.kbss.jopa.model.annotations.Id;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.reporting.model.Vocabulary;

import java.net.URI;

@OWLClass(iri = Vocabulary.ONTOLOGY_IRI_model_A + "/factor_graph_edge")
public class FactorGraphEdge {

    @Id
    private URI uri;

    @OWLDataProperty(iri = Vocabulary.ONTOLOGY_IRI_model_A + "/edge_from")
    private Integer from;

    @OWLDataProperty(iri = Vocabulary.ONTOLOGY_IRI_model_A + "/edge_to")
    private Integer to;

    @OWLObjectProperty(iri = Vocabulary.ONTOLOGY_IRI_model_A + "/edge_type")
    private URI linkType;

    public FactorGraphEdge() {
    }

    public FactorGraphEdge(URI uri, Integer from, Integer to, URI linkType) {
        this.uri = uri;
        this.from = from;
        this.to = to;
        this.linkType = linkType;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getTo() {
        return to;
    }

    public void setTo(Integer to) {
        this.to = to;
    }

    public URI getLinkType() {
        return linkType;
    }

    public void setLinkType(URI linkType) {
        this.linkType = linkType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FactorGraphEdge that = (FactorGraphEdge) o;

        if (from != null ? !from.equals(that.from) : that.from != null) return false;
        if (to != null ? !to.equals(that.to) : that.to != null) return false;
        return linkType != null ? linkType.equals(that.linkType) : that.linkType == null;

    }

    @Override
    public int hashCode() {
        int result = from != null ? from.hashCode() : 0;
        result = 31 * result + (to != null ? to.hashCode() : 0);
        result = 31 * result + (linkType != null ? linkType.hashCode() : 0);
        return result;
    }
}
