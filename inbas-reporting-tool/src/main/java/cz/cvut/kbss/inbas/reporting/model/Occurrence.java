package cz.cvut.kbss.inbas.reporting.model;

import cz.cvut.kbss.inbas.reporting.model.qam.Form;
import cz.cvut.kbss.inbas.reporting.model.util.HasOwlKey;
import cz.cvut.kbss.inbas.reporting.model.util.factorgraph.FactorGraphItem;
import cz.cvut.kbss.inbas.reporting.model.util.factorgraph.clone.EdgeCloningVisitor;
import cz.cvut.kbss.inbas.reporting.model.util.factorgraph.clone.NodeCloningVisitor;
import cz.cvut.kbss.inbas.reporting.model.util.factorgraph.traversal.FactorGraphTraverser;
import cz.cvut.kbss.jopa.model.annotations.*;

import java.io.Serializable;
import java.net.URI;
import java.util.*;

@OWLClass(iri = Vocabulary.Occurrence)
public class Occurrence implements HasOwlKey, FactorGraphItem, Serializable {

    @Id(generated = true)
    private URI uri;

    @ParticipationConstraints(nonEmpty = true)
    @OWLDataProperty(iri = Vocabulary.p_hasKey)
    private String key;

    @ParticipationConstraints(nonEmpty = true)
    @OWLAnnotationProperty(iri = Vocabulary.p_name)
    private String name;

    @ParticipationConstraints(nonEmpty = true)
    @OWLDataProperty(iri = Vocabulary.p_startTime)
    private Date startTime;

    @ParticipationConstraints(nonEmpty = true)
    @OWLDataProperty(iri = Vocabulary.p_endTime)
    private Date endTime;

    @ParticipationConstraints(nonEmpty = true)
    @OWLObjectProperty(iri = Vocabulary.p_hasEventType)
    private URI eventType;

    @OWLObjectProperty(iri = Vocabulary.p_hasFactor, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Factor> factors;

    @OWLObjectProperty(iri = Vocabulary.p_hasPart, fetch = FetchType.EAGER, cascade = {CascadeType.MERGE,
            CascadeType.REMOVE})
    private Set<Event> children;

    @OWLObjectProperty(iri = Vocabulary.p_hasQuestion, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Form form;

    @Types
    private Set<String> types;

    public Occurrence() {
        this.types = new HashSet<>();
        // Occurrence is a subclass of Event
        types.add(Vocabulary.Event);
    }

    public Occurrence(Occurrence other) {
        this.name = other.name;
        this.startTime = other.startTime;
        this.endTime = other.endTime;
        this.eventType = other.eventType;
        this.types = new HashSet<>(other.types);
        if (other.form != null) {
            this.form = new Form(other.form);
        }
    }

    @Override
    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public URI getEventType() {
        return eventType;
    }

    /**
     * Sets type of this occurrence.
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

    public Set<Factor> getFactors() {
        return factors;
    }

    public void setFactors(Set<Factor> factors) {
        this.factors = factors;
    }

    @Override
    public void addFactor(Factor factor) {
        Objects.requireNonNull(factor);
        if (factors == null) {
            this.factors = new HashSet<>();
        }
        factors.add(factor);
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

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public Set<String> getTypes() {
        return types;
    }

    public void setTypes(Set<String> types) {
        this.types = types;
    }

    @Override
    public String toString() {
        return "Occurrence{" + name + " <" + uri + ">, types=" + types + '}';
    }

    public static Occurrence copyOf(Occurrence original) {
        final Map<URI, FactorGraphItem> instanceMap = new HashMap<>();
        final NodeCloningVisitor nodeVisitor = new NodeCloningVisitor(instanceMap);
        final FactorGraphTraverser traverser = new FactorGraphTraverser(nodeVisitor, null);
        traverser.traverse(original);
        final EdgeCloningVisitor edgeVisitor = new EdgeCloningVisitor(instanceMap);
        traverser.setFactorGraphEdgeVisitor(edgeVisitor);
        traverser.traverse(original);
        return (Occurrence) instanceMap.get(original.getUri());
    }
}
