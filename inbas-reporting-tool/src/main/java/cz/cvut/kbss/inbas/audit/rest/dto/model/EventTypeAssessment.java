package cz.cvut.kbss.inbas.audit.rest.dto.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import cz.cvut.kbss.inbas.audit.model.EventType;

import java.net.URI;

/**
 * @author ledvima1
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "dtoClass", defaultImpl = GeneralEvent.class)
public class EventTypeAssessment {

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

    public EventTypeAssessment() {
    }

    protected EventTypeAssessment(URI uri) {
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
