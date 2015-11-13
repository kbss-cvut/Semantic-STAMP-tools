package cz.cvut.kbss.inbas.audit.rest.dto.model;

import cz.cvut.kbss.inbas.audit.model.Occurrence;
import cz.cvut.kbss.inbas.audit.model.reports.PreliminaryReport;

import java.net.URI;
import java.util.Date;

/**
 * Contains only basic info about an occurrence report, without any event classification and other references.
 */
public class OccurrenceReportInfo {

    private URI uri;

    private String key;

    private Occurrence occurrence;

    private Date lastEdited;

    private String summary;

    public OccurrenceReportInfo() {
    }

    public OccurrenceReportInfo(PreliminaryReport report) {
        this.uri = report.getUri();
        this.key = report.getKey();
        this.occurrence = report.getOccurrence();
        this.lastEdited = report.getLastEdited() != null ? report.getLastEdited() : report.getCreated();
        this.summary = report.getSummary();
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Occurrence getOccurrence() {
        return occurrence;
    }

    public void setOccurrence(Occurrence occurrence) {
        this.occurrence = occurrence;
    }

    public Date getLastEdited() {
        return lastEdited;
    }

    public void setLastEdited(Date lastEdited) {
        this.lastEdited = lastEdited;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public String toString() {
        return "OccurrenceReportInfo{" +
                "occurrence=" + occurrence +
                ", lastEdited=" + lastEdited +
                '}';
    }
}
