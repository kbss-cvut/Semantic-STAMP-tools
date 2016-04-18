package cz.cvut.kbss.inbas.reporting.model;

import cz.cvut.kbss.inbas.reporting.model.util.HasUri;
import cz.cvut.kbss.jopa.model.annotations.Id;
import cz.cvut.kbss.jopa.model.annotations.OWLAnnotationProperty;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;

import java.io.Serializable;
import java.net.URI;

/**
 * Severity levels should be defined in some ontology and the application should only reference them, never create or
 * modify them.
 */
@OWLClass(iri = Vocabulary.SeverityLevel)
public class SeverityLevel implements HasUri, Serializable {

    @Id
    private URI uri;

    @OWLAnnotationProperty(iri = Vocabulary.p_name)
    private String name;

    @OWLAnnotationProperty(iri = Vocabulary.p_comment)
    private String description;

    @Override
    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "SeverityLevel{" + name + " <" + uri + ">}";
    }
}
