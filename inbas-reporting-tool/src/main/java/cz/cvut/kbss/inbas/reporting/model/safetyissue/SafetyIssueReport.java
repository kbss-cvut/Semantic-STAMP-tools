package cz.cvut.kbss.inbas.reporting.model.safetyissue;

import cz.cvut.kbss.inbas.reporting.dto.reportlist.ReportDto;
import cz.cvut.kbss.inbas.reporting.dto.reportlist.SafetyIssueReportDto;
import cz.cvut.kbss.inbas.reporting.model.AbstractReport;
import cz.cvut.kbss.inbas.reporting.model.CorrectiveMeasureRequest;
import cz.cvut.kbss.inbas.reporting.model.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@OWLClass(iri = Vocabulary.s_c_safety_issue_report)
public class SafetyIssueReport extends AbstractReport implements Serializable {

    @ParticipationConstraints(nonEmpty = true)
    @OWLObjectProperty(iri = Vocabulary.s_p_documents, fetch = FetchType.EAGER)
    private SafetyIssue safetyIssue;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_corrective_measure, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<CorrectiveMeasureRequest> correctiveMeasures;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_safety_issue_risk_assessment, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private SafetyIssueRiskAssessment sira;

    public SafetyIssueReport() {
    }

    public SafetyIssueReport(SafetyIssueReport other) {
        super(other);
        this.safetyIssue = SafetyIssue.copyOf(other.safetyIssue);
        this.sira = other.sira != null ? new SafetyIssueRiskAssessment(other.sira) : null;
        if (other.getCorrectiveMeasures() != null) {
            this.correctiveMeasures = other.getCorrectiveMeasures().stream().map(CorrectiveMeasureRequest::new).collect(
                    Collectors.toSet());
        }
    }

    public SafetyIssue getSafetyIssue() {
        return safetyIssue;
    }

    public void setSafetyIssue(SafetyIssue safetyIssue) {
        this.safetyIssue = safetyIssue;
    }

    public Set<CorrectiveMeasureRequest> getCorrectiveMeasures() {
        if (correctiveMeasures == null) {
            this.correctiveMeasures = new HashSet<>();
        }
        return correctiveMeasures;
    }

    public void setCorrectiveMeasures(Set<CorrectiveMeasureRequest> correctiveMeasures) {
        this.correctiveMeasures = correctiveMeasures;
    }

    public SafetyIssueRiskAssessment getSira() {
        return sira;
    }

    public void setSira(SafetyIssueRiskAssessment sira) {
        this.sira = sira;
    }

    @Override
    public ReportDto toReportDto() {
        final SafetyIssueReportDto dto = new SafetyIssueReportDto();
        copyAttributes(dto);
        dto.setSummary(summary);
        dto.setIdentification(safetyIssue.getName());
        if (sira != null) {
            dto.setSira(sira.getSiraValue());
        }
        dto.getTypes().add(SafetyIssueReport.class.getDeclaredAnnotation(OWLClass.class).iri());
        return dto;
    }
}
