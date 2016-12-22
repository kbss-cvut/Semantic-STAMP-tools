package cz.cvut.kbss.reporting.dto.reportlist;

import java.net.URI;

public class SafetyIssueReportDto extends ReportDto {

    private String summary;

    private URI sira;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public URI getSira() {
        return sira;
    }

    public void setSira(URI sira) {
        this.sira = sira;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SafetyIssueReportDto that = (SafetyIssueReportDto) o;

        return getUri() != null ? getUri().equals(that.getUri()) : that.getUri() == null;

    }

    @Override
    public int hashCode() {
        return getUri().hashCode();
    }

    @Override
    public String toString() {
        return "SafetyIssueReportDto{ " + super.toString() + '}';
    }

    @Override
    public ReportDto toReportDto() {
        return this;
    }
}
