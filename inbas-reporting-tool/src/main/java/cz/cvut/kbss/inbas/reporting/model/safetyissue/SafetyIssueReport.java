package cz.cvut.kbss.inbas.reporting.model.safetyissue;

import cz.cvut.kbss.inbas.reporting.dto.reportlist.ReportDto;
import cz.cvut.kbss.inbas.reporting.dto.reportlist.SafetyIssueReportDto;
import cz.cvut.kbss.inbas.reporting.model.AbstractReport;
import cz.cvut.kbss.inbas.reporting.model.LogicalDocument;
import cz.cvut.kbss.inbas.reporting.model.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.*;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

@OWLClass(iri = Vocabulary.s_c_safety_issue_report)
public class SafetyIssueReport extends AbstractReport implements LogicalDocument, Serializable {

    @ParticipationConstraints(nonEmpty = true)
    @OWLObjectProperty(iri = Vocabulary.s_p_documents, fetch = FetchType.EAGER)
    private SafetyIssue safetyIssue;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_corrective_measure, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<CorrectiveMeasure> correctiveMeasures;

    public SafetyIssueReport() {
    }

    public SafetyIssueReport(SafetyIssueReport other) {
        this.fileNumber = other.fileNumber;
        this.summary = other.summary;
        types.addAll(other.getTypes());
        this.safetyIssue = SafetyIssue.copyOf(other.safetyIssue);
        if (other.getCorrectiveMeasures() != null) {
            this.correctiveMeasures = other.getCorrectiveMeasures().stream().map(CorrectiveMeasure::new).collect(
                    Collectors.toSet());
        }
    }

    public SafetyIssue getSafetyIssue() {
        return safetyIssue;
    }

    public void setSafetyIssue(SafetyIssue safetyIssue) {
        this.safetyIssue = safetyIssue;
    }

    public Set<CorrectiveMeasure> getCorrectiveMeasures() {
        return correctiveMeasures;
    }

    public void setCorrectiveMeasures(
            Set<CorrectiveMeasure> correctiveMeasures) {
        this.correctiveMeasures = correctiveMeasures;
    }

    @Override
    public ReportDto toReportDto() {
        final SafetyIssueReportDto dto = new SafetyIssueReportDto();
        copyAttributes(dto);
        dto.setSummary(summary);
        dto.setIdentification(safetyIssue.getName());
        dto.getTypes().add(SafetyIssueReport.class.getDeclaredAnnotation(OWLClass.class).iri());
        return dto;
    }
}
