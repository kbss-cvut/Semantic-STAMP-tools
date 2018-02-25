package cz.cvut.kbss.reporting.dto;

import cz.cvut.kbss.jopa.model.annotations.Id;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.reporting.dto.agent.AgentDto;
import cz.cvut.kbss.reporting.dto.event.EventDto;
import cz.cvut.kbss.reporting.model.Vocabulary;

import java.net.URI;
import java.util.Set;

@OWLClass(iri = Vocabulary.s_c_corrective_measure_request)
public class CorrectiveMeasureRequestDto {

    @Id
    private URI uri;

    @OWLDataProperty(iri = Vocabulary.s_p_description)
    private String description;

    private Set<AgentDto> responsibleAgents;

    @OWLObjectProperty(iri = Vocabulary.s_p_based_on_event)
    private EventDto basedOn;

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
}
