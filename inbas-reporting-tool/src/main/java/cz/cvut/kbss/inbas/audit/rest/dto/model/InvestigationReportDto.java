package cz.cvut.kbss.inbas.audit.rest.dto.model;

import cz.cvut.kbss.inbas.audit.model.Occurrence;
import cz.cvut.kbss.inbas.audit.model.Person;
import cz.cvut.kbss.inbas.audit.model.reports.CorrectiveMeasure;
import cz.cvut.kbss.inbas.audit.model.reports.InitialReport;
import cz.cvut.kbss.inbas.audit.model.reports.OccurrenceSeverity;

import java.net.URI;
import java.util.Date;
import java.util.Set;

public class InvestigationReportDto {

    private URI uri;

    private String key;

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

    private FactorDto rootFactor;

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

    public void setCorrectiveMeasures(
            Set<CorrectiveMeasure> correctiveMeasures) {
        this.correctiveMeasures = correctiveMeasures;
    }

    public FactorDto getRootFactor() {
        return rootFactor;
    }

    public void setRootFactor(FactorDto rootFactor) {
        this.rootFactor = rootFactor;
    }

    @Override
    public String toString() {
        return "InvestigationReportDto{" +
                "uri=" + uri +
                ", key='" + key + '\'' +
                ", summary='" + summary + '\'' +
                ", occurrence=" + occurrence +
                '}';
    }
}
