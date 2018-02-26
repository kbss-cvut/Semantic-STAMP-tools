package cz.cvut.kbss.reporting.dto.event;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import cz.cvut.kbss.jopa.model.annotations.*;
import cz.cvut.kbss.reporting.model.Vocabulary;
import cz.cvut.kbss.reporting.model.qam.Question;
import cz.cvut.kbss.reporting.model.util.HasUri;

import java.net.URI;
import java.util.Date;
import java.util.Set;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "javaClass")
@JsonIdentityInfo(property = "referenceId", generator = ObjectIdGenerators.PropertyGenerator.class)
@OWLClass(iri = Vocabulary.s_c_event)
public class EventDto implements HasUri {

    @Id
    private URI uri;

    @OWLDataProperty(iri = Vocabulary.s_p_has_start_time)
    private Date startTime;

    @OWLDataProperty(iri = Vocabulary.s_p_has_end_time)
    private Date endTime;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_event_type)
    private URI eventType;

    @Types
    private Set<String> types;

    private Integer referenceId;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_question)
    private Question question;

    @OWLDataProperty(iri = Vocabulary.s_p_child_index)
    private Integer index;

    @Override
    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public URI getEventType() {
        return eventType;
    }

    public void setEventType(URI eventType) {
        this.eventType = eventType;
    }

    public Set<String> getTypes() {
        return types;
    }

    public void setTypes(Set<String> types) {
        this.types = types;
    }

    public Integer getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Integer referenceId) {
        this.referenceId = referenceId;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
