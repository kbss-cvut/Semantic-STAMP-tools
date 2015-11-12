package cz.cvut.kbss.inbas.audit.model.reports;

import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.Id;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;

import java.io.Serializable;
import java.net.URI;

@OWLClass(iri = Vocabulary.CorrectiveMeasure)
public class CorrectiveMeasure implements ReportingStatement, Serializable {

    @Id(generated = true)
    URI uri;

    @OWLDataProperty(iri = Vocabulary.p_description)
    private String description;

    public CorrectiveMeasure() {
    }

    public CorrectiveMeasure(String description) {
        this.description = description;
    }

    /**
     * Copy constructor.
     * <p>
     * It uses only the description from the other corrective measure, URI is not copied.
     *
     * @param other The other corrective measure
     */
    public CorrectiveMeasure(CorrectiveMeasure other) {
        assert other != null;
        this.description = other.description;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "CorrectiveMeasure{" +
                "uri=" + uri +
                ", description='" + description + '\'' +
                '}';
    }
}
