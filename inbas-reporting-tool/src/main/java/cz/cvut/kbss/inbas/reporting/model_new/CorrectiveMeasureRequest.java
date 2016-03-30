package cz.cvut.kbss.inbas.reporting.model_new;

import cz.cvut.kbss.jopa.model.annotations.*;

import java.io.Serializable;
import java.net.URI;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@OWLClass(iri = Vocabulary.CorrectiveMeasureRequest)
public class CorrectiveMeasureRequest implements Serializable {

    @Id(generated = true)
    private URI uri;

    @OWLDataProperty(iri = Vocabulary.p_description)
    private String description;

    @OWLObjectProperty(iri = Vocabulary.p_hasResponsibleAgent, fetch = FetchType.EAGER)
    private Set<Agent> responsibleAgents;

    public CorrectiveMeasureRequest() {
    }

    /**
     * Copy constructor.
     * <p>
     * Responsible agents are reused.
     *
     * @param other The instance to copy
     */
    public CorrectiveMeasureRequest(CorrectiveMeasureRequest other) {
        Objects.requireNonNull(other);
        this.description = other.description;
        this.responsibleAgents = other.getResponsibleAgents() != null ? new HashSet<>(other.responsibleAgents) : null;
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

    public Set<Agent> getResponsibleAgents() {
        return responsibleAgents;
    }

    public void setResponsibleAgents(Set<Agent> responsibleAgents) {
        this.responsibleAgents = responsibleAgents;
    }

    @Override
    public String toString() {
        // First 50 characters of the description
        if (description != null) {
            return "CorrectiveMeasureRequest{" +
                    description.substring(0, description.length() > 50 ? 50 : description.length()) + "..." + '}';
        }
        return "CorrectiveMeasureRequest{" + uri + "}";
    }
}
