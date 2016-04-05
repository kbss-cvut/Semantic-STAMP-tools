package cz.cvut.kbss.inbas.reporting.dto.event;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import cz.cvut.kbss.inbas.reporting.model_new.EventType;

import java.net.URI;
import java.util.Set;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "class")
public class EventDto {

    private URI uri;

    // TODO Factors and sub-events will need references
//    private Set<Factor> factors;
//
//    private Set<Event> children;

    private EventType type;

    private Set<String> types;

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public Set<String> getTypes() {
        return types;
    }

    public void setTypes(Set<String> types) {
        this.types = types;
    }
}
