package cz.cvut.kbss.inbas.audit.model;

import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.*;

import java.net.URI;
import java.util.Set;

/**
 * @author ledvima1
 */
@OWLClass(iri = Vocabulary.EventTypeAssessment)
public class EventTypeAssessment implements ReportingStatement {

    @Id(generated = true)
    private URI uri;

    @OWLObjectProperty(iri = Vocabulary.p_hasEventType, fetch = FetchType.EAGER)
    private EventType eventType;

    @OWLObjectProperty(iri = Vocabulary.p_hasEvent, fetch = FetchType.EAGER)
    private EventReport eventReport;

    @OWLDataProperty(iri = Vocabulary.p_description)
    private String description;

    @OWLObjectProperty(iri = Vocabulary.p_hasIncursion, fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST,
            CascadeType.REFRESH})
    private RunwayIncursion runwayIncursion;

    @OWLObjectProperty(iri = Vocabulary.p_hasResource, cascade = CascadeType.ALL)
    private Set<Resource> resources;

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

    public EventReport getEventReport() {
        return eventReport;
    }

    public void setEventReport(EventReport eventReport) {
        this.eventReport = eventReport;
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
    public Set<Resource> getResources() {
        return resources;
    }

    public void setResources(Set<Resource> resources) {
        this.resources = resources;
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
