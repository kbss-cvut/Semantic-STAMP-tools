package cz.cvut.kbss.inbas.reporting.model_new;

import cz.cvut.kbss.inbas.reporting.model_new.util.HasUri;
import cz.cvut.kbss.jopa.model.annotations.*;

import java.io.Serializable;
import java.net.URI;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@OWLClass(iri = Vocabulary.Event)
public class Event implements HasUri, Serializable {

    @Id(generated = true)
    private URI uri;

    @OWLObjectProperty(iri = Vocabulary.p_hasFactor, fetch = FetchType.EAGER)
    private Set<Factor> factors;

    @OWLObjectProperty(iri = Vocabulary.p_hasPart, fetch = FetchType.EAGER, cascade = {CascadeType.MERGE,
            CascadeType.REMOVE})
    private Set<Event> children;

    @OWLObjectProperty(iri = Vocabulary.p_hasEventType, fetch = FetchType.EAGER)
    private EventType type;

    @Types
    private Set<String> types;

    @Override
    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public Set<Factor> getFactors() {
        return factors;
    }

    public void setFactors(Set<Factor> factors) {
        this.factors = factors;
    }

    public void addFactor(Factor f) {
        Objects.requireNonNull(f);
        if (factors == null) {
            this.factors = new HashSet<>();
        }
        factors.add(f);
    }

    public Set<Event> getChildren() {
        return children;
    }

    public void setChildren(Set<Event> children) {
        this.children = children;
    }

    public EventType getType() {
        return type;
    }

    /**
     * Sets type of this event.
     * <p>
     * Also adds the event type's URI to this instance's types.
     *
     * @param type The type to set
     * @see Vocabulary#p_hasEventType
     */
    public void setType(EventType type) {
        this.type = type;
        if (type != null) {
            if (types == null) {
                this.types = new HashSet<>(4);
            }
            assert type.getUri() != null;
            types.add(type.getUri().toString());
        }
    }

    public Set<String> getTypes() {
        return types;
    }

    public void setTypes(Set<String> types) {
        this.types = types;
    }

    @Override
    public String toString() {
        return "Event{" + uri +
                ", types=" + types +
                '}';
    }
}
