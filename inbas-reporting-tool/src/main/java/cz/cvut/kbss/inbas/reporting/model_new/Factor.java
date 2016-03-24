package cz.cvut.kbss.inbas.reporting.model_new;

import cz.cvut.kbss.jopa.model.annotations.*;

import java.io.Serializable;
import java.net.URI;

@OWLClass(iri = Vocabulary.Factor)
public class Factor implements Serializable {

    @Id(generated = true)
    private URI uri;

    @ParticipationConstraints(nonEmpty = true)
    @OWLObjectProperty(iri = Vocabulary.p_hasFactor, fetch = FetchType.EAGER)
    private Event event;

    @ParticipationConstraints(nonEmpty = true)
    @OWLDataProperty(iri = Vocabulary.p_factorType)
    private FactorType type;

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public FactorType getType() {
        return type;
    }

    public void setType(FactorType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return event + "(" + type + ')';
    }
}
