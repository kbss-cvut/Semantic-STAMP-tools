package cz.cvut.kbss.inbas.audit.rest.dto.model;

import cz.cvut.kbss.inbas.audit.model.CorrectiveMeasure;
import cz.cvut.kbss.inbas.audit.model.Person;
import cz.cvut.kbss.inbas.audit.model.Resource;
import cz.cvut.kbss.inbas.audit.model.SeverityAssessment;

import java.net.URI;
import java.util.Date;
import java.util.Set;

/**
 * @author ledvima1
 */
public class EventReportDto {

    private URI uri;

    private String key;

    private Date eventTime;

    private Date created;

    private Date lastEdited;

    private String name;

    private String description;

    private String factors;

    private Person author;

    private Person lastEditedBy;

    private Resource resource;

    private SeverityAssessment severityAssessment;

    private Set<CorrectiveMeasure> correctiveMeasures;

    private Set<RunwayIncursionDto> typeAssessments;

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

    public Date getEventTime() {
        return eventTime;
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFactors() {
        return factors;
    }

    public void setFactors(String factors) {
        this.factors = factors;
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

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public SeverityAssessment getSeverityAssessment() {
        return severityAssessment;
    }

    public void setSeverityAssessment(SeverityAssessment severityAssessment) {
        this.severityAssessment = severityAssessment;
    }

    public Set<CorrectiveMeasure> getCorrectiveMeasures() {
        return correctiveMeasures;
    }

    public void setCorrectiveMeasures(Set<CorrectiveMeasure> correctiveMeasures) {
        this.correctiveMeasures = correctiveMeasures;
    }

    public Set<RunwayIncursionDto> getTypeAssessments() {
        return typeAssessments;
    }

    public void setTypeAssessments(Set<RunwayIncursionDto> typeAssessments) {
        this.typeAssessments = typeAssessments;
    }
}
