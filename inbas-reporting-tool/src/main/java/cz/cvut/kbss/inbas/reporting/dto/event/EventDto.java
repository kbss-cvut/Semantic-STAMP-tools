package cz.cvut.kbss.inbas.reporting.dto.event;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import cz.cvut.kbss.inbas.reporting.model.EventType;
import cz.cvut.kbss.inbas.reporting.model.util.HasUri;

import java.net.URI;
import java.util.Set;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "javaClass")
@JsonIdentityInfo(property = "referenceId", generator = ObjectIdGenerators.PropertyGenerator.class)
public class EventDto implements HasUri {

    private URI uri;

    private EventType type;

    private Set<String> types;

    private Integer referenceId;

    @Override
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

    public Integer getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Integer referenceId) {
        this.referenceId = referenceId;
    }
}
