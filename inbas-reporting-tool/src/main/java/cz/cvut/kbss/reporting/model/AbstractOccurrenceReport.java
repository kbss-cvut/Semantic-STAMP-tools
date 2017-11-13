package cz.cvut.kbss.reporting.model;

import cz.cvut.kbss.jopa.model.annotations.MappedSuperclass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;

import java.net.URI;

@MappedSuperclass
public abstract class AbstractOccurrenceReport extends AbstractReport {

    @OWLObjectProperty(iri = Vocabulary.s_p_has_reporting_phase)
    protected URI phase;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_severity_assessment)
    protected URI severityAssessment;

    @OWLDataProperty(iri = Vocabulary.s_p_description)
    protected String summary;

    public URI getPhase() {
        return phase;
    }

    public void setPhase(URI phase) {
        this.phase = phase;
    }

    public URI getSeverityAssessment() {
        return severityAssessment;
    }

    public void setSeverityAssessment(URI severityAssessment) {
        this.severityAssessment = severityAssessment;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
