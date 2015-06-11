package cz.cvut.kbss.inbas.audit.model;

import cz.cvut.kbss.inbas.audit.util.IdentificationUtils;
import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.*;

import java.net.URI;
import java.util.Set;

/**
 * @author ledvima1
 */
@OWLClass(iri = Vocabulary.EventTypeAssessment)
public class EventTypeAssessment implements ReportingStatement, HasOwlKey {

    @Id(generated = true)
    private URI uri;

    @OWLDataProperty(iri = Vocabulary.p_hasKey)
    private String key;

    @OWLObjectProperty(iri = Vocabulary.p_hasEventType, fetch = FetchType.EAGER)
    private EventType eventType;

    @OWLObjectProperty(iri = Vocabulary.p_hasEvent, fetch = FetchType.EAGER)
    private EventReport eventReport;

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

    @Override
    public Set<Resource> getResources() {
        return resources;
    }

    public void setResources(Set<Resource> resources) {
        this.resources = resources;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void generateKey() {
        if (key != null) {
            return;
        }
        this.key = IdentificationUtils.generateKey();
    }

    @Override
    public String toString() {
        return "EventTypeAssessment{" +
                "uri=" + uri +
                ", eventType=" + eventType +
                ", eventReport=" + eventReport +
                '}';
    }
}
