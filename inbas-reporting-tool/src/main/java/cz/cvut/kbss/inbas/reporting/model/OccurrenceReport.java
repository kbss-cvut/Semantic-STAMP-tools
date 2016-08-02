package cz.cvut.kbss.inbas.reporting.model;

import cz.cvut.kbss.inbas.reporting.dto.reportlist.OccurrenceReportDto;
import cz.cvut.kbss.inbas.reporting.dto.reportlist.ReportDto;
import cz.cvut.kbss.jopa.model.annotations.*;

import java.io.Serializable;
import java.net.URI;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@OWLClass(iri = Vocabulary.s_c_occurrence_report)
public class OccurrenceReport extends AbstractReport implements LogicalDocument, Serializable {

    @OWLObjectProperty(iri = Vocabulary.s_p_has_reporting_phase)
    private URI phase;

    @ParticipationConstraints(nonEmpty = true)
    @OWLObjectProperty(iri = Vocabulary.s_p_documents, fetch = FetchType.EAGER)
    private Occurrence occurrence;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_severity_assessment)
    private URI severityAssessment;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_responsible_organization)
    private Set<URI> responsibleDepartments;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_corrective_measure, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<CorrectiveMeasureRequest> correctiveMeasures;

    // ARMS Attributes

    @OWLObjectProperty(iri = Vocabulary.s_p_has_most_probable_accident_outcome)
    private URI accidentOutcome;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_barrier_effectiveness_evaluation)
    private URI barrierEffectiveness;

    @Transient
    private Integer armsIndex;

    public OccurrenceReport() {
    }

    public OccurrenceReport(OccurrenceReport other) {
        Objects.requireNonNull(other);
        this.fileNumber = other.fileNumber;
        this.phase = other.phase;
        this.occurrence = Occurrence.copyOf(other.occurrence);
        this.severityAssessment = other.severityAssessment;
        if (other.responsibleDepartments != null) {
            this.responsibleDepartments = new HashSet<>(other.responsibleDepartments);
        }
        this.summary = other.summary;
        if (other.correctiveMeasures != null) {
            this.correctiveMeasures = other.correctiveMeasures.stream().map(CorrectiveMeasureRequest::new)
                                                              .collect(Collectors.toSet());
        }
        this.barrierEffectiveness = other.barrierEffectiveness;
        this.accidentOutcome = other.accidentOutcome;
        this.armsIndex = other.armsIndex;
    }

    public URI getPhase() {
        return phase;
    }

    public void setPhase(URI phase) {
        this.phase = phase;
    }

    public Occurrence getOccurrence() {
        return occurrence;
    }

    public void setOccurrence(Occurrence occurrence) {
        this.occurrence = occurrence;
    }

    public URI getSeverityAssessment() {
        return severityAssessment;
    }

    public void setSeverityAssessment(URI severityAssessment) {
        this.severityAssessment = severityAssessment;
    }

    public Set<URI> getResponsibleDepartments() {
        return responsibleDepartments;
    }

    public void setResponsibleDepartments(Set<URI> responsibleDepartments) {
        this.responsibleDepartments = responsibleDepartments;
    }

    public Set<CorrectiveMeasureRequest> getCorrectiveMeasures() {
        return correctiveMeasures;
    }

    public void setCorrectiveMeasures(Set<CorrectiveMeasureRequest> correctiveMeasures) {
        this.correctiveMeasures = correctiveMeasures;
    }

    public URI getAccidentOutcome() {
        return accidentOutcome;
    }

    public void setAccidentOutcome(URI accidentOutcome) {
        this.accidentOutcome = accidentOutcome;
    }

    public URI getBarrierEffectiveness() {
        return barrierEffectiveness;
    }

    public void setBarrierEffectiveness(URI barrierEffectiveness) {
        this.barrierEffectiveness = barrierEffectiveness;
    }

    public Integer getArmsIndex() {
        return armsIndex;
    }

    public void setArmsIndex(Integer armsIndex) {
        this.armsIndex = armsIndex;
    }

    @Override
    public String toString() {
        return "OccurrenceReport{" +
                "uri=" + uri +
                ", fileNumber=" + fileNumber +
                ", revision=" + revision +
                ", occurrence=" + occurrence +
                '}';
    }

    @Override
    public ReportDto toReportDto() {
        final OccurrenceReportDto res = new OccurrenceReportDto();
        copyAttributes(res);
        res.setPhase(phase);
        res.getTypes().add(Vocabulary.s_c_occurrence_report);
        assert occurrence != null;
        res.setIdentification(occurrence.getName());
        res.setDate(occurrence.getStartTime());
        res.setArmsIndex(armsIndex);
        res.setSummary(summary);
        res.setSeverityAssessment(severityAssessment);
        res.setOccurrenceCategory(occurrence.getEventType());
        return res;
    }
}
