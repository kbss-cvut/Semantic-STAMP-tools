package cz.cvut.kbss.inbas.reporting.dto.event;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import cz.cvut.kbss.inbas.reporting.dto.util.HasReferenceId;
import cz.cvut.kbss.inbas.reporting.model_new.EventType;
import cz.cvut.kbss.inbas.reporting.model_new.Factor;

import java.net.URI;
import java.util.Set;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "javaClass")
public class EventDto extends HasReferenceId {

    private URI uri;

    private Set<Factor> factors;

    private Set<EventDto> children;

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

    public Set<Factor> getFactors() {
        return factors;
    }

    public void setFactors(Set<Factor> factors) {
        this.factors = factors;
    }

    public Set<EventDto> getChildren() {
        return children;
    }

    public void setChildren(Set<EventDto> children) {
        this.children = children;
    }
}
