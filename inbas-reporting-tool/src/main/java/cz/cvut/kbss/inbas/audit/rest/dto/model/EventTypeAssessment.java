package cz.cvut.kbss.inbas.audit.rest.dto.model;

import cz.cvut.kbss.inbas.audit.model.EventType;

import java.net.URI;

/**
 * @author ledvima1
 */
public class EventTypeAssessment {

    private URI uri;

    private EventType eventType;

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

    @Override
    public String toString() {
        return "EventTypeAssessmentDto{" +
                "uri=" + uri +
                ", eventType='" + eventType + '\'' +
                '}';
    }
}
