package cz.cvut.kbss.inbas.reporting.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import cz.cvut.kbss.inbas.reporting.dto.event.FactorGraph;
import cz.cvut.kbss.inbas.reporting.dto.event.SafetyIssueDto;
import cz.cvut.kbss.inbas.reporting.dto.reportlist.ReportDto;
import cz.cvut.kbss.inbas.reporting.model.safetyissue.SafetyIssueRiskAssessment;

import java.util.Set;

// Safety issue must come before the factor graph
@JsonPropertyOrder(value = {"uri, key, safetyIssue, factorGraph"})
public class SafetyIssueReportDto extends AbstractReportDto {

    private SafetyIssueDto safetyIssue;

    private FactorGraph factorGraph;

    private Set<CorrectiveMeasureRequestDto> correctiveMeasures;

    private SafetyIssueRiskAssessment sira;

    public SafetyIssueDto getSafetyIssue() {
        return safetyIssue;
    }

    public void setSafetyIssue(SafetyIssueDto safetyIssue) {
        this.safetyIssue = safetyIssue;
    }

    public FactorGraph getFactorGraph() {
        return factorGraph;
    }

    public void setFactorGraph(FactorGraph factorGraph) {
        this.factorGraph = factorGraph;
    }

    public Set<CorrectiveMeasureRequestDto> getCorrectiveMeasures() {
        return correctiveMeasures;
    }

    public void setCorrectiveMeasures(
            Set<CorrectiveMeasureRequestDto> correctiveMeasures) {
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
        final cz.cvut.kbss.inbas.reporting.dto.reportlist.SafetyIssueReportDto dto = new cz.cvut.kbss.inbas.reporting.dto.reportlist.SafetyIssueReportDto();
        copyAttributes(dto);
        dto.setSummary(getSummary());
        dto.setIdentification(safetyIssue.getName());
        if (sira != null) {
            dto.setSira(sira.getSiraValue());
        }
        return dto;
    }
}
