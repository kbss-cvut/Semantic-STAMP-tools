package cz.cvut.kbss.inbas.reporting.model.audit;

import cz.cvut.kbss.inbas.reporting.model.AbstractEntity;
import cz.cvut.kbss.inbas.reporting.model.CorrectiveMeasureRequest;
import cz.cvut.kbss.inbas.reporting.model.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.*;

import java.net.URI;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@OWLClass(iri = Vocabulary.s_c_audit_finding)
public class AuditFinding extends AbstractEntity {

    @ParticipationConstraints(nonEmpty = true)
    @OWLDataProperty(iri = Vocabulary.s_p_description)
    private String description;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_factor)
    private Set<URI> factors;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_corrective_measure, fetch = FetchType.EAGER)
    private Set<CorrectiveMeasureRequest> correctiveMeasures;

    @Types
    private Set<String> types;

    public AuditFinding() {
        this.types = new HashSet<>(4);
        types.add(Vocabulary.s_c_Event);
    }

    public AuditFinding(AuditFinding other) {
        Objects.requireNonNull(other);
        this.description = other.description;
        this.factors = other.factors != null ? new HashSet<>(other.factors) : null;
        this.types = new HashSet<>(other.types);
        this.correctiveMeasures = other.correctiveMeasures != null ?
                                  other.correctiveMeasures.stream().map(CorrectiveMeasureRequest::new).collect(
                                          Collectors.toSet()) : null;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<URI> getFactors() {
        return factors;
    }

    public void setFactors(Set<URI> factors) {
        this.factors = factors;
    }

    public Set<CorrectiveMeasureRequest> getCorrectiveMeasures() {
        return correctiveMeasures;
    }

    public void setCorrectiveMeasures(
            Set<CorrectiveMeasureRequest> correctiveMeasures) {
        this.correctiveMeasures = correctiveMeasures;
    }

    public Set<String> getTypes() {
        return types;
    }

    public void setTypes(Set<String> types) {
        this.types = types;
    }

    @Override
    public String toString() {
        return "AuditFinding{" +
                "types=" + types +
                ", description='" + description + '\'' +
                "} " + super.toString();
    }
}
