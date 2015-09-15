package cz.cvut.kbss.inbas.audit.rest.dto.model;

import java.net.URI;
import java.util.Date;

/**
 * Contains only basic info about an occurrence report, without any event classification and other references.
 */
public class OccurrenceReportInfo {

    private URI uri;

    private String key;

    private String name;

    private Date occurrenceTime;

    private Date lastEdited;

    private String initialReport;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getOccurrenceTime() {
        return occurrenceTime;
    }

    public void setOccurrenceTime(Date occurrenceTime) {
        this.occurrenceTime = occurrenceTime;
    }

    public Date getLastEdited() {
        return lastEdited;
    }

    public void setLastEdited(Date lastEdited) {
        this.lastEdited = lastEdited;
    }

    public String getInitialReport() {
        return initialReport;
    }

    public void setInitialReport(String initialReport) {
        this.initialReport = initialReport;
    }

    @Override
    public String toString() {
        return "OccurrenceReportInfo{" +
                "name='" + name + '\'' +
                ", occurrenceTime=" + occurrenceTime +
                ", lastEdited=" + lastEdited +
                '}';
    }
}
