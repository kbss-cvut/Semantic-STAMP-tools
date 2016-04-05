package cz.cvut.kbss.inbas.reporting.dto.agent;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import cz.cvut.kbss.inbas.reporting.model_new.util.HasUri;

import java.net.URI;
import java.util.Objects;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "class")
public abstract class AgentDto {

    private URI uri;

    public AgentDto() {
    }

    AgentDto(HasUri instance) {
        Objects.requireNonNull(instance);
        this.uri = instance.getUri();
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }
}
