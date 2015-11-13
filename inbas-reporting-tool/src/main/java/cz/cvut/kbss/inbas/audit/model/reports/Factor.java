package cz.cvut.kbss.inbas.audit.model.reports;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.*;

import java.net.URI;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

// Identity generator is used to identify factors in the causes/mitigates relationships
// Note that every reference has to be created before it is used in an causes/mitigatingFactors array in JSON
// See reportWithFactorsWithCauses.json in test resources
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
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
    private Set<Factor> mitigatingFactors;

    @OWLObjectProperty(iri = Vocabulary.p_hasEventTypeAssessment, cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private EventTypeAssessment assessment;

    public Factor() {
    }

    public Factor(EventTypeAssessment typeAssessment) {
        this.assessment = new EventTypeAssessment(typeAssessment);
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
        return children;
    }

    public void addChild(Factor child) {
        if (children == null) {
            this.children = new HashSet<>();
        }
        children.add(child);
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

    public Set<Factor> getMitigatingFactors() {
        return mitigatingFactors;
    }

    public void setMitigatingFactors(Set<Factor> mitigatingFactors) {
        this.mitigatingFactors = mitigatingFactors;
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
                ", assessment=" + assessment +
                '}';
    }
}
