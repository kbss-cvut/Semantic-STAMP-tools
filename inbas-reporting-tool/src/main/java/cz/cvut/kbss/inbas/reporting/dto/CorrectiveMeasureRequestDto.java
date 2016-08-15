package cz.cvut.kbss.inbas.reporting.dto;

import cz.cvut.kbss.inbas.reporting.dto.agent.AgentDto;
import cz.cvut.kbss.inbas.reporting.dto.event.EventDto;
import cz.cvut.kbss.inbas.reporting.model.CorrectiveMeasureImplementationEvaluation;

import java.net.URI;
import java.util.Set;

public class CorrectiveMeasureRequestDto {

    private URI uri;

    private String description;

    private Set<AgentDto> responsibleAgents;

    private EventDto basedOn;

    private CorrectiveMeasureImplementationEvaluation evaluation;

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<AgentDto> getResponsibleAgents() {
        return responsibleAgents;
    }

    public void setResponsibleAgents(Set<AgentDto> responsibleAgents) {
        this.responsibleAgents = responsibleAgents;
    }

    public EventDto getBasedOn() {
        return basedOn;
    }

    public void setBasedOn(EventDto basedOn) {
        this.basedOn = basedOn;
    }

    public CorrectiveMeasureImplementationEvaluation getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(CorrectiveMeasureImplementationEvaluation evaluation) {
        this.evaluation = evaluation;
    }
}
