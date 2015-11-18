package cz.cvut.kbss.inbas.audit.rest.dto.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import cz.cvut.kbss.inbas.audit.model.reports.EventType;

import java.net.URI;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "dtoClass", defaultImpl = GeneralEventDto.class)
public class EventTypeAssessmentDto {

    protected URI uri;

    protected EventType eventType;

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public EventTypeAssessmentDto() {
    }

    protected EventTypeAssessmentDto(URI uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "EventTypeAssessmentDto{" +
                "uri=" + uri +
                ", eventType='" + eventType + '\'' +
                '}';
    }
}
