package cz.cvut.kbss.inbas.reporting.dto;

import cz.cvut.kbss.inbas.reporting.dto.event.EventGraph;
import cz.cvut.kbss.inbas.reporting.model_new.LogicalDocument;
import cz.cvut.kbss.inbas.reporting.model_new.Person;
import cz.cvut.kbss.inbas.reporting.model_new.SeverityLevel;
import cz.cvut.kbss.inbas.reporting.model_new.arms.AccidentOutcome;
import cz.cvut.kbss.inbas.reporting.model_new.arms.BarrierEffectiveness;

import java.net.URI;
import java.util.Date;
import java.util.Set;

public class OccurrenceReportDto implements LogicalDocument {

    private URI uri;

    private String key;

    private Long fileNumber;

    private EventGraph occurrence;

    private Date occurrenceStart;

    private Date occurrenceEnd;

    private Person author;

    private Date dateCreated;

    private Date lastModified;

    private Person lastModifiedBy;

    private Integer revision;

    private SeverityLevel severityAssessment;

    private Set<CorrectiveMeasureRequestDto> correctiveMeasureRequests;

    private String summary;

    // ARMS Attributes

    private AccidentOutcome accidentOutcome;

    private BarrierEffectiveness barrierEffectiveness;

    private Short armsIndex;

    private Set<String> types;

    @Override
    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public Long getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(Long fileNumber) {
        this.fileNumber = fileNumber;
    }

    public EventGraph getOccurrence() {
        return occurrence;
    }

    public void setOccurrence(EventGraph occurrence) {
        this.occurrence = occurrence;
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

    @Override
    public Person getAuthor() {
        return author;
    }

    public void setAuthor(Person author) {
        this.author = author;
    }

    @Override
    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public Person getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(Person lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @Override
    public Integer getRevision() {
        return revision;
    }

    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    public SeverityLevel getSeverityAssessment() {
        return severityAssessment;
    }

    public void setSeverityAssessment(SeverityLevel severityAssessment) {
        this.severityAssessment = severityAssessment;
    }

    public Set<CorrectiveMeasureRequestDto> getCorrectiveMeasureRequests() {
        return correctiveMeasureRequests;
    }

    public void setCorrectiveMeasureRequests(Set<CorrectiveMeasureRequestDto> correctiveMeasureRequests) {
        this.correctiveMeasureRequests = correctiveMeasureRequests;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public AccidentOutcome getAccidentOutcome() {
        return accidentOutcome;
    }

    public void setAccidentOutcome(AccidentOutcome accidentOutcome) {
        this.accidentOutcome = accidentOutcome;
    }

    public BarrierEffectiveness getBarrierEffectiveness() {
        return barrierEffectiveness;
    }

    public void setBarrierEffectiveness(BarrierEffectiveness barrierEffectiveness) {
        this.barrierEffectiveness = barrierEffectiveness;
    }

    public Short getArmsIndex() {
        return armsIndex;
    }

    public void setArmsIndex(Short armsIndex) {
        this.armsIndex = armsIndex;
    }

    public Set<String> getTypes() {
        return types;
    }

    public void setTypes(Set<String> types) {
        this.types = types;
    }
}
