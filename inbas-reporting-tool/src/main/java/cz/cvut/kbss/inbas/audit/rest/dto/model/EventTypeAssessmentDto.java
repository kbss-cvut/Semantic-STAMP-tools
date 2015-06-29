package cz.cvut.kbss.inbas.audit.rest.dto.model;

import java.net.URI;

/**
 * @author ledvima1
 */
public class EventTypeAssessmentDto {

    private URI uri;

    private String eventType;

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
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
