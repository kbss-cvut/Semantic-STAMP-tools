package cz.cvut.kbss.inbas.audit.rest.dto.model;

import cz.cvut.kbss.inbas.audit.model.Occurrence;

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

    @Override
    public String toString() {
        return "OccurrenceReportInfo{" +
                "occurrence=" + occurrence +
                ", lastEdited=" + lastEdited +
                '}';
    }
}
