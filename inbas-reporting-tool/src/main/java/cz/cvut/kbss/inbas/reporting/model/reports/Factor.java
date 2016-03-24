package cz.cvut.kbss.inbas.reporting.model.reports;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import cz.cvut.kbss.inbas.reporting.util.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.*;

import java.io.Serializable;
import java.net.URI;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
@OWLClass(iri = Vocabulary.Factor)
public class Factor implements Serializable {

    @Id(generated = true)
    private URI uri;

    @OWLDataProperty(iri = Vocabulary.p_startTime)
    @ParticipationConstraints(nonEmpty = true)
    private Date startTime;

    @OWLDataProperty(iri = Vocabulary.p_endTime)
    @ParticipationConstraints(nonEmpty = true)
    private Date endTime;

    @OWLObjectProperty(iri = Vocabulary.p_hasChild, cascade = {CascadeType.MERGE,
            CascadeType.REMOVE}, fetch = FetchType.EAGER)
    private Set<Factor> children;

    @OWLObjectProperty(iri = Vocabulary.p_hasCause, fetch = FetchType.EAGER)
    private Set<Factor> causes;

    @OWLObjectProperty(iri = Vocabulary.p_hasMitigation, fetch = FetchType.EAGER)
    private Set<Factor> mitigatingFactors;

    @OWLObjectProperty(iri = Vocabulary.p_hasEventTypeAssessment, cascade = {CascadeType.REMOVE,
            CascadeType.MERGE}, fetch = FetchType.EAGER)
    private EventTypeAssessment assessment;

    // Used for link mapping (causes and mitigatingFactors)
    private transient Integer referenceId;

    public Factor() {
    }

    public Factor(EventTypeAssessment typeAssessment) {
        this.assessment = new EventTypeAssessment(typeAssessment);
    }

    public Factor(Factor other) {
        assert other != null;
        if (other.assessment != null) {
            this.assessment = new EventTypeAssessment(other.assessment);
        }
        this.startTime = other.startTime;
        this.endTime = other.endTime;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
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

    public Set<Factor> getChildren() {
        if (children == null) {
            this.children = new HashSet<>();
        }
        return children;
    }

    public void addChild(Factor child) {
        getChildren().add(child);
    }

    public void setChildren(Set<Factor> children) {
        this.children = children;
    }

    public Set<Factor> getCauses() {
        if (causes == null) {
            this.causes = new HashSet<>();
        }
        return causes;
    }

    public void setCauses(Set<Factor> causes) {
        this.causes = causes;
    }

    public void addCause(Factor cause) {
        assert cause != null;
        if (causes == null) {
            this.causes = new HashSet<>();
        }
        causes.add(cause);
    }

    public Set<Factor> getMitigatingFactors() {
        if (mitigatingFactors == null) {
            this.mitigatingFactors = new HashSet<>();
        }
        return mitigatingFactors;
    }

    public void setMitigatingFactors(Set<Factor> mitigatingFactors) {
        this.mitigatingFactors = mitigatingFactors;
    }

    public void addMitigatingFactor(Factor mitigation) {
        assert mitigation != null;
        if (mitigatingFactors == null) {
            this.mitigatingFactors = new HashSet<>();
        }
        mitigatingFactors.add(mitigation);
    }

    public EventTypeAssessment getAssessment() {
        return assessment;
    }

    public void setAssessment(EventTypeAssessment assessment) {
        this.assessment = assessment;
    }

    public Integer getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Integer referenceId) {
        this.referenceId = referenceId;
    }

    @Override
    public String toString() {
        return "Factor{" +
                "uri=" + uri +
                ",startTime=" + startTime +
                ", endTime=" + endTime +
                ", assessment=" + assessment +
                '}';
    }
}
