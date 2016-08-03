package cz.cvut.kbss.inbas.reporting.dto.reportlist;

import java.net.URI;

public class OccurrenceReportDto extends ReportDto {

    private URI severityAssessment;

    private URI occurrenceCategory;

    private Integer armsIndex;

    private String summary;

    public URI getSeverityAssessment() {
        return severityAssessment;
    }

    public void setSeverityAssessment(URI severityAssessment) {
        this.severityAssessment = severityAssessment;
    }

    public URI getOccurrenceCategory() {
        return occurrenceCategory;
    }

    public void setOccurrenceCategory(URI occurrenceCategory) {
        this.occurrenceCategory = occurrenceCategory;
    }

    public Integer getArmsIndex() {
        return armsIndex;
    }

    public void setArmsIndex(Integer armsIndex) {
        this.armsIndex = armsIndex;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OccurrenceReportDto reportDto = (OccurrenceReportDto) o;

        return getUri().equals(reportDto.getUri());

    }

    @Override
    public int hashCode() {
        return getUri().hashCode();
    }

    @Override
    public String toString() {
        return "OccurrenceReportDto{ " + super.toString() + '}';
    }

    @Override
    public ReportDto toReportDto() {
        return this;
    }
}
