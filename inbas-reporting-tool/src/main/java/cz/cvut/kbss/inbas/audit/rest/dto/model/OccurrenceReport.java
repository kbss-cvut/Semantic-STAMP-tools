package cz.cvut.kbss.inbas.audit.rest.dto.model;

import cz.cvut.kbss.inbas.audit.model.*;
import cz.cvut.kbss.inbas.audit.model.reports.CorrectiveMeasure;
import cz.cvut.kbss.inbas.audit.model.reports.incursions.Intruder;
import cz.cvut.kbss.inbas.audit.model.reports.SeverityAssessment;
import cz.cvut.kbss.inbas.audit.rest.dto.model.incursion.AircraftIntruder;
import cz.cvut.kbss.inbas.audit.rest.dto.model.incursion.RunwayIncursion;
import cz.cvut.kbss.inbas.audit.rest.dto.model.incursion.RunwayIntruder;
import cz.cvut.kbss.inbas.audit.rest.dto.model.incursion.VehicleIntruder;

import java.net.URI;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ledvima1
 */
public class OccurrenceReport {

    private URI uri;

    private String key;

    private Date occurrenceTime;

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

    private Set<EventTypeAssessment> typeAssessments;

    public OccurrenceReport() {
    }

    public OccurrenceReport(cz.cvut.kbss.inbas.audit.model.reports.OccurrenceReport report) {
        this.uri = report.getUri();
        this.key = report.getKey();
        this.occurrenceTime = report.getOccurrenceTime();
        this.created = report.getCreated();
        this.lastEdited = report.getLastEdited();
        this.name = report.getName();
        this.description = report.getDescription();
        this.factors = report.getFactors();
        this.author = report.getAuthor();
        this.lastEditedBy = report.getLastEditedBy();
        this.resource = report.getResource();
        this.severityAssessment = report.getSeverityAssessment();
        this.correctiveMeasures = report.getCorrectiveMeasures();
        this.typeAssessments = new HashSet<>();
        if (report.getTypeAssessments() != null) {
            this.typeAssessments = report.getTypeAssessments().stream().map(type -> {
                if (type.getRunwayIncursion() != null) {
                    final RunwayIncursion incursion = new RunwayIncursion(type.getRunwayIncursion());
                    incursion.setEventType(type.getEventType());
                    incursion.setIntruder(getIntruder(type.getRunwayIncursion().getIntruder()));
                    return incursion;
                } else {
                    return new GeneralEvent(type);
                }
            }).collect(Collectors.toSet());
        }
    }

    private RunwayIntruder getIntruder(Intruder intruder) {
        if (intruder.getAircraft() != null) {
            return new AircraftIntruder(intruder.getAircraft());
        } else if (intruder.getVehicle() != null) {
            return new VehicleIntruder(intruder.getVehicle());
        } else if (intruder.getPerson() != null) {
            return new cz.cvut.kbss.inbas.audit.rest.dto.model.incursion.PersonIntruder(intruder.getPerson());
        }
        return null;
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

    public Date getOccurrenceTime() {
        return occurrenceTime;
    }

    public void setOccurrenceTime(Date occurrenceTime) {
        this.occurrenceTime = occurrenceTime;
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

    public Set<EventTypeAssessment> getTypeAssessments() {
        return typeAssessments;
    }

    public void setTypeAssessments(Set<EventTypeAssessment> typeAssessments) {
        this.typeAssessments = typeAssessments;
    }

    @Override
    public String toString() {
        return "OccurrenceReport{" +
                "uri=" + uri +
                ", key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
