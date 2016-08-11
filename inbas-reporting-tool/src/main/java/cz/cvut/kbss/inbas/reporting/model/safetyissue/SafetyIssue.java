package cz.cvut.kbss.inbas.reporting.model.safetyissue;

import cz.cvut.kbss.inbas.reporting.model.*;
import cz.cvut.kbss.inbas.reporting.model.util.factorgraph.FactorGraphItem;
import cz.cvut.kbss.inbas.reporting.model.util.factorgraph.FactorGraphNodeVisitor;
import cz.cvut.kbss.inbas.reporting.model.util.factorgraph.clone.EdgeCloningVisitor;
import cz.cvut.kbss.inbas.reporting.model.util.factorgraph.clone.NodeCloningVisitor;
import cz.cvut.kbss.inbas.reporting.model.util.factorgraph.traversal.DefaultFactorGraphTraverser;
import cz.cvut.kbss.inbas.reporting.model.util.factorgraph.traversal.FactorGraphTraverser;
import cz.cvut.kbss.jopa.model.annotations.*;

import java.io.Serializable;
import java.net.URI;
import java.util.*;

@OWLClass(iri = Vocabulary.s_c_safety_issue)
public class SafetyIssue extends AbstractEntity implements Serializable, FactorGraphItem {

    @ParticipationConstraints(nonEmpty = true)
    @OWLAnnotationProperty(iri = Vocabulary.s_p_label)
    private String name;

    @Types
    private Set<String> types;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_factor, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Factor> factors;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_part, fetch = FetchType.EAGER, cascade = {CascadeType.MERGE,
            CascadeType.REMOVE})
    private Set<Event> children;

    @OWLObjectProperty(iri = Vocabulary.s_p_based_on, fetch = FetchType.EAGER)
    private Set<OccurrenceReport> basedOn;

    public SafetyIssue() {
        this.types = new HashSet<>(4);
        types.add(Vocabulary.s_c_event_type);
    }

    public SafetyIssue(SafetyIssue other) {
        this();
        this.name = other.name;
        this.types.addAll(other.getTypes());
        if (other.basedOn != null) {
            this.basedOn = new HashSet<>(other.basedOn);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getTypes() {
        return types;
    }

    public void setTypes(Set<String> types) {
        this.types = types;
    }

    @Override
    public Set<Factor> getFactors() {
        return factors;
    }

    @Override
    public void setFactors(Set<Factor> factors) {
        this.factors = factors;
    }

    @Override
    public void addFactor(Factor factor) {
        Objects.requireNonNull(factor);
        if (factors == null) {
            this.factors = new LinkedHashSet<>();
        }
        factors.add(factor);
    }

    @Override
    public Set<Event> getChildren() {
        return children;
    }

    @Override
    public void setChildren(Set<Event> children) {
        this.children = children;
    }

    @Override
    public void addChild(Event child) {
        Objects.requireNonNull(child);
        if (children == null) {
            this.children = new LinkedHashSet<>();
        }
        children.add(child);
    }

    public Set<OccurrenceReport> getBasedOn() {
        return basedOn;
    }

    public void setBasedOn(Set<OccurrenceReport> basedOn) {
        this.basedOn = basedOn;
    }

    @Override
    public String toString() {
        return "SafetyIssue{" + name + '}';
    }

    @Override
    public void accept(FactorGraphNodeVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Creates deep copy of the specified safety issue.
     *
     * @param original The instance to copy
     * @return Deep copy of {@code original}
     */
    public static SafetyIssue copyOf(SafetyIssue original) {
        Objects.requireNonNull(original);
        final Map<URI, FactorGraphItem> instanceMap = new HashMap<>();
        final NodeCloningVisitor nodeVisitor = new NodeCloningVisitor(instanceMap);
        final FactorGraphTraverser traverser = new DefaultFactorGraphTraverser(nodeVisitor, null);
        traverser.traverse(original);
        final EdgeCloningVisitor edgeVisitor = new EdgeCloningVisitor(instanceMap);
        traverser.setFactorGraphEdgeVisitor(edgeVisitor);
        traverser.traverse(original);
        return (SafetyIssue) instanceMap.get(original.getUri());
    }
}
