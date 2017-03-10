package cz.cvut.kbss.reporting.model.safetyissue;

import cz.cvut.kbss.jopa.model.annotations.*;
import cz.cvut.kbss.reporting.model.*;
import cz.cvut.kbss.reporting.model.audit.AuditFinding;
import cz.cvut.kbss.reporting.model.util.factorgraph.FactorGraphItem;
import cz.cvut.kbss.reporting.model.util.factorgraph.FactorGraphNodeVisitor;
import cz.cvut.kbss.reporting.model.util.factorgraph.clone.EdgeCloningVisitor;
import cz.cvut.kbss.reporting.model.util.factorgraph.clone.NodeCloningVisitor;
import cz.cvut.kbss.reporting.model.util.factorgraph.traversal.DefaultFactorGraphTraverser;
import cz.cvut.kbss.reporting.model.util.factorgraph.traversal.FactorGraphTraverser;

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

    @OWLObjectProperty(iri = Vocabulary.s_p_based_on_occurrence, fetch = FetchType.EAGER)
    private Set<Occurrence> basedOnOccurrences;

    @OWLObjectProperty(iri = Vocabulary.s_p_based_on_finding, fetch = FetchType.EAGER)
    private Set<AuditFinding> basedOnFindings;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_safety_issue_state)
    private URI state;

    public SafetyIssue() {
        this.types = new HashSet<>(4);
        types.add(Vocabulary.s_c_event_type);
    }

    public SafetyIssue(SafetyIssue other) {
        this();
        this.name = other.name;
        this.types.addAll(other.getTypes());
        if (other.basedOnOccurrences != null) {
            this.basedOnOccurrences = new HashSet<>(other.basedOnOccurrences);
        }
        if (other.basedOnFindings != null) {
            this.basedOnFindings = new HashSet<>(other.basedOnFindings);
        }
        this.state = other.state;
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

    public Set<Occurrence> getBasedOnOccurrences() {
        return basedOnOccurrences;
    }

    public void setBasedOnOccurrences(Set<Occurrence> basedOnOccurrences) {
        this.basedOnOccurrences = basedOnOccurrences;
    }

    public void addBase(Occurrence occurrence) {
        Objects.requireNonNull(occurrence);
        if (basedOnOccurrences == null) {
            this.basedOnOccurrences = new HashSet<>();
        }
        basedOnOccurrences.add(occurrence);
    }

    public Set<AuditFinding> getBasedOnFindings() {
        return basedOnFindings;
    }

    public void setBasedOnFindings(Set<AuditFinding> basedOnFindings) {
        this.basedOnFindings = basedOnFindings;
    }

    public void addBase(AuditFinding finding) {
        Objects.requireNonNull(finding);
        if (basedOnFindings == null) {
            this.basedOnFindings = new HashSet<>();
        }
        basedOnFindings.add(finding);
    }

    public URI getState() {
        return state;
    }

    public void setState(URI state) {
        this.state = state;
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
