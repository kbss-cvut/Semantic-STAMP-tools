package cz.cvut.kbss.inbas.audit.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import cz.cvut.kbss.inbas.audit.model.Occurrence;
import cz.cvut.kbss.inbas.audit.model.Person;
import cz.cvut.kbss.inbas.audit.model.reports.CorrectiveMeasure;
import cz.cvut.kbss.inbas.audit.model.reports.InitialReport;
import cz.cvut.kbss.inbas.audit.model.reports.OccurrenceSeverity;

import java.net.URI;
import java.util.Date;
import java.util.Set;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "dtoClass")
public abstract class AbstractReportDto {

    private URI uri;

    private String key;

    private Long fileNumber;

    private Date occurrenceStart;

    private Date occurrenceEnd;

    private Date created;

    private Date lastEdited;

    private Integer revision;

    private OccurrenceSeverity severityAssessment;

    private String summary;

    private Person author;

    private Person lastEditedBy;

    private Occurrence occurrence;

    private Set<InitialReport> initialReports;

    private Set<CorrectiveMeasure> correctiveMeasures;

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

    public Long getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(Long fileNumber) {
        this.fileNumber = fileNumber;
    }

    public Date getOccurrenceStart() {
        return occurrenceStart;
    }

    public void setOccurrenceStart(Date occurrenceStart) {
        this.occurrenceStart = occurrenceStart;
    }

    public Date getOccurrenceEnd() {
        return occurrenceEnd;
    }

    public void setOccurrenceEnd(Date occurrenceEnd) {
        this.occurrenceEnd = occurrenceEnd;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
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

    public OccurrenceSeverity getSeverityAssessment() {
        return severityAssessment;
    }

    public void setSeverityAssessment(OccurrenceSeverity severityAssessment) {
        this.severityAssessment = severityAssessment;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Person getAuthor() {
        return author;
    }

    public void setAuthor(Person author) {
        this.author = author;
    }

    public Person getLastEditedBy() {
        return lastEditedBy;
    }

    public void setLastEditedBy(Person lastEditedBy) {
        this.lastEditedBy = lastEditedBy;
    }

    public Occurrence getOccurrence() {
        return occurrence;
    }

    public void setOccurrence(Occurrence occurrence) {
        this.occurrence = occurrence;
    }

    public Set<InitialReport> getInitialReports() {
        return initialReports;
    }

    public void setInitialReports(Set<InitialReport> initialReports) {
        this.initialReports = initialReports;
    }

    public Set<CorrectiveMeasure> getCorrectiveMeasures() {
        return correctiveMeasures;
    }

    public void setCorrectiveMeasures(Set<CorrectiveMeasure> correctiveMeasures) {
        this.correctiveMeasures = correctiveMeasures;
    }
}
