package cz.cvut.kbss.inbas.reporting.dto.reportlist;

import java.net.URI;

public class OccurrenceReportDto extends ReportDto {

    private URI severityAssessment;

    private String summary;

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
