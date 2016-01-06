package cz.cvut.kbss.inbas.audit.dto;

import java.net.URI;
import java.util.Date;

public class ReportRevisionInfo {

    // Report URI
    private URI uri;

    // Report key
    private String key;

    private Date lastEdited;

    private Integer revision;

    public ReportRevisionInfo() {
    }

    public ReportRevisionInfo(URI uri, String key, Date lastEdited, Integer revision) {
        this.uri = uri;
        this.key = key;
        this.lastEdited = lastEdited;
        this.revision = revision;
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

    public Date getLastEdited() {
        return lastEdited;
    }

    public void setLastEdited(Date lastEdited) {
        this.lastEdited = lastEdited;
    }

    public Integer getRevision() {
        return revision;
    }

    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    @Override
    public String toString() {
        return "ReportRevisionInfo{" +
                "uri=" + uri +
                ", revision=" + revision +
                '}';
    }
}
