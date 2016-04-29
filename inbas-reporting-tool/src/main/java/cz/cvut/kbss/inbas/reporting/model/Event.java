package cz.cvut.kbss.inbas.reporting.model;

import cz.cvut.kbss.inbas.reporting.model.util.FactorGraphItem;
import cz.cvut.kbss.jopa.model.annotations.*;

import java.io.Serializable;
import java.net.URI;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@OWLClass(iri = Vocabulary.Event)
public class Event implements FactorGraphItem, Serializable {

    @Id(generated = true)
    private URI uri;

    @OWLObjectProperty(iri = Vocabulary.p_hasFactor, fetch = FetchType.EAGER)
    private Set<Factor> factors;

    @OWLObjectProperty(iri = Vocabulary.p_hasPart, fetch = FetchType.EAGER, cascade = {CascadeType.MERGE,
            CascadeType.REMOVE})
    private Set<Event> children;

    @ParticipationConstraints(nonEmpty = true)
    @OWLDataProperty(iri = Vocabulary.p_startTime)
    private Date startTime;

    @ParticipationConstraints(nonEmpty = true)
    @OWLDataProperty(iri = Vocabulary.p_endTime)
    private Date endTime;

    @OWLObjectProperty(iri = Vocabulary.p_hasEventType)
    private URI eventType;

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

    @Override
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

    @Override
    public void addChild(Event child) {
        Objects.requireNonNull(child);
        if (children == null) {
            this.children = new HashSet<>();
        }
        children.add(child);
    }

    public URI getEventType() {
        return eventType;
    }

    /**
     * Sets type of this event.
     * <p>
     * Also adds the event type's URI to this instance's types.
     *
     * @param eventType The type to set
     * @see Vocabulary#p_hasEventType
     */
    public void setEventType(URI eventType) {
        this.eventType = eventType;
        if (eventType != null) {
            if (types == null) {
                this.types = new HashSet<>(4);
            }
            types.add(eventType.toString());
        }
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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
