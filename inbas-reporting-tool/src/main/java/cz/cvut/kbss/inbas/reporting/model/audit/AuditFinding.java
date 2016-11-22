package cz.cvut.kbss.inbas.reporting.model.audit;

import cz.cvut.kbss.inbas.reporting.model.AbstractEntity;
import cz.cvut.kbss.inbas.reporting.model.CorrectiveMeasureRequest;
import cz.cvut.kbss.inbas.reporting.model.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.*;

import java.net.URI;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@OWLClass(iri = Vocabulary.s_c_audit_finding)
public class AuditFinding extends AbstractEntity {

    @ParticipationConstraints(nonEmpty = true)
    @OWLDataProperty(iri = Vocabulary.s_p_description)
    private String description;

    @OWLDataProperty(iri = Vocabulary.s_p_has_severity_assessment)
    private Integer level;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_factor)
    private Set<URI> factors;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_corrective_measure, fetch = FetchType.EAGER)
    private Set<CorrectiveMeasureRequest> correctiveMeasures;

    // For SAFA audits. This would ideally be in a subclass of AuditFinding
    @OWLObjectProperty(iri = Vocabulary.s_p_has_document_phase)
    private URI status;

    @OWLDataProperty(iri = Vocabulary.s_p_modified)
    private Date statusLastModified;

    @Types
    private Set<String> types;

    public AuditFinding() {
        this.types = new HashSet<>(4);
        types.add(Vocabulary.s_c_Event);
    }

    public AuditFinding(AuditFinding other) {
        Objects.requireNonNull(other);
        this.description = other.description;
        this.level = other.level;
        this.factors = other.factors != null ? new HashSet<>(other.factors) : null;
        this.types = new HashSet<>(other.types);
        this.correctiveMeasures = other.correctiveMeasures != null ?
                                  other.correctiveMeasures.stream().map(CorrectiveMeasureRequest::new).collect(
                                          Collectors.toSet()) : null;
        this.status = other.status;
        this.statusLastModified = other.statusLastModified;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Set<URI> getFactors() {
        return factors;
    }

    public void setFactors(Set<URI> factors) {
        this.factors = factors;
    }

    public Set<CorrectiveMeasureRequest> getCorrectiveMeasures() {
        if (correctiveMeasures == null) {
            this.correctiveMeasures = new HashSet<>();
        }
        return correctiveMeasures;
    }

    public void setCorrectiveMeasures(
            Set<CorrectiveMeasureRequest> correctiveMeasures) {
        this.correctiveMeasures = correctiveMeasures;
    }

    public URI getStatus() {
        return status;
    }

    public void setStatus(URI status) {
        this.status = status;
    }

    public Date getStatusLastModified() {
        return statusLastModified;
    }

    public void setStatusLastModified(Date statusLastModified) {
        this.statusLastModified = statusLastModified;
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
                "level=" + level +
                ", description='" + description + '\'' +
                "} " + super.toString();
    }
}
