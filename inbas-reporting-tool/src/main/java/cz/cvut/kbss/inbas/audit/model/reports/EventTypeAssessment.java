package cz.cvut.kbss.inbas.audit.model.reports;

import cz.cvut.kbss.inbas.audit.model.reports.incursions.RunwayIncursion;
import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.*;

import java.io.Serializable;
import java.net.URI;

@OWLClass(iri = Vocabulary.EventTypeAssessment)
public class EventTypeAssessment implements ReportingStatement, Serializable {

    @Id(generated = true)
    private URI uri;

    @ParticipationConstraints(nonEmpty = true)
    @OWLObjectProperty(iri = Vocabulary.p_hasEventType, fetch = FetchType.EAGER)
    private EventType eventType;

    @OWLDataProperty(iri = Vocabulary.p_description)
    private String description;

    @OWLObjectProperty(iri = Vocabulary.p_hasIncursion, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private RunwayIncursion runwayIncursion;

    public EventTypeAssessment() {
    }

    /**
     * Copy constructor.
     */
    public EventTypeAssessment(EventTypeAssessment other) {
        this.eventType = other.eventType;
        this.description = other.description;
        if (other.runwayIncursion != null) {
            this.runwayIncursion = new RunwayIncursion(other.runwayIncursion);
        }
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RunwayIncursion getRunwayIncursion() {
        return runwayIncursion;
    }

    public void setRunwayIncursion(RunwayIncursion runwayIncursion) {
        this.runwayIncursion = runwayIncursion;
    }

    @Override
    public String toString() {
        return "EventTypeAssessment{" +
                "uri=" + uri +
                ", eventType=" + eventType +
                (runwayIncursion != null ? runwayIncursion : description) +
                '}';
    }
}
