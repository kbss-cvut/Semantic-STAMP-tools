package cz.cvut.kbss.inbas.audit.model.reports;

import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.*;

import java.net.URI;
import java.util.Date;
import java.util.Set;

@OWLClass(iri = Vocabulary.Factor)
public class Factor {

    @Id(generated = true)
    private URI uri;

    @OWLDataProperty(iri = Vocabulary.p_startTime)
    @ParticipationConstraints(nonEmpty = true)
    private Date startTime;

    @OWLDataProperty(iri = Vocabulary.p_endTime)
    @ParticipationConstraints(nonEmpty = true)
    private Date endTime;

    @OWLObjectProperty(iri = Vocabulary.p_hasChild, fetch = FetchType.EAGER)
    private Set<Factor> children;

    @OWLObjectProperty(iri = Vocabulary.p_hasCause, fetch = FetchType.EAGER)
    private Set<Factor> causes;

    @OWLObjectProperty(iri = Vocabulary.p_hasMitigation, fetch = FetchType.EAGER)
    private Set<Factor> mitigationFactors;

    @OWLObjectProperty(iri = Vocabulary.p_hasEventType, fetch = FetchType.EAGER)
    @ParticipationConstraints(nonEmpty = true)
    private EventType type;

    @OWLObjectProperty(iri = Vocabulary.p_hasEventTypeAssessment, cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private EventTypeAssessment assessment;

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
        return children;
    }

    public void setChildren(Set<Factor> children) {
        this.children = children;
    }

    public Set<Factor> getCauses() {
        return causes;
    }

    public void setCauses(Set<Factor> causes) {
        this.causes = causes;
    }

    public Set<Factor> getMitigationFactors() {
        return mitigationFactors;
    }

    public void setMitigationFactors(Set<Factor> mitigationFactors) {
        this.mitigationFactors = mitigationFactors;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public EventTypeAssessment getAssessment() {
        return assessment;
    }

    public void setAssessment(EventTypeAssessment assessment) {
        this.assessment = assessment;
    }

    @Override
    public String toString() {
        return "Factor{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                ", type=" + type +
                '}';
    }
}
