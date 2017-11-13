package cz.cvut.kbss.reporting.model;

import cz.cvut.kbss.jopa.model.annotations.*;
import cz.cvut.kbss.reporting.dto.reportlist.OccurrenceReportDto;
import cz.cvut.kbss.reporting.dto.reportlist.ReportDto;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@OWLClass(iri = Vocabulary.s_c_occurrence_report)
public class OccurrenceReport extends AbstractOccurrenceReport {

    @ParticipationConstraints(nonEmpty = true)
    @OWLObjectProperty(iri = Vocabulary.s_p_documents, fetch = FetchType.EAGER)
    private Occurrence occurrence;

    @OWLObjectProperty(iri = Vocabulary.s_p_based_on, cascade = CascadeType.REMOVE, fetch = FetchType.EAGER, readOnly = true)
    private InitialReport initialReport;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_corrective_measure, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<CorrectiveMeasureRequest> correctiveMeasures;

    public OccurrenceReport() {
        getTypes().add(Vocabulary.s_c_report);
    }

    public OccurrenceReport(OccurrenceReport other) {
        this();
        Objects.requireNonNull(other);
        this.fileNumber = other.fileNumber;
        this.phase = other.phase;
        this.occurrence = Occurrence.copyOf(other.occurrence);
        this.initialReport = other.initialReport;
        this.severityAssessment = other.severityAssessment;
        this.summary = other.summary;
        if (other.correctiveMeasures != null) {
            this.correctiveMeasures = other.correctiveMeasures.stream().map(CorrectiveMeasureRequest::new)
                                                              .collect(Collectors.toSet());
        }
        if (other.references != null) {
            this.references = other.references.stream().map(Resource::new).collect(Collectors.toSet());
        }
    }

    public Occurrence getOccurrence() {
        return occurrence;
    }

    public void setOccurrence(Occurrence occurrence) {
        this.occurrence = occurrence;
    }

    public InitialReport getInitialReport() {
        return initialReport;
    }

    public void setInitialReport(InitialReport initialReport) {
        this.initialReport = initialReport;
    }

    public Set<CorrectiveMeasureRequest> getCorrectiveMeasures() {
        return correctiveMeasures;
    }

    public void setCorrectiveMeasures(Set<CorrectiveMeasureRequest> correctiveMeasures) {
        this.correctiveMeasures = correctiveMeasures;
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
        final OccurrenceReportDto dto = new OccurrenceReportDto();
        copyBasicAttributesToDto(dto);
        dto.setPhase(phase);
        dto.getTypes().add(Vocabulary.s_c_occurrence_report);
        assert occurrence != null;
        dto.setIdentification(occurrence.getName());
        dto.setDate(occurrence.getStartTime());
        dto.setSummary(summary);
        dto.setSeverityAssessment(severityAssessment);
        dto.setOccurrenceCategory(occurrence.getEventType());
        return dto;
    }
}
