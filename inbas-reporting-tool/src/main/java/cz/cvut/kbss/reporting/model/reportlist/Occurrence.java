package cz.cvut.kbss.reporting.model.reportlist;

import cz.cvut.kbss.jopa.model.annotations.*;
import cz.cvut.kbss.reporting.model.AbstractEntity;
import cz.cvut.kbss.reporting.model.Vocabulary;
import cz.cvut.kbss.reporting.model.util.HasOwlKey;

import java.io.Serializable;
import java.net.URI;
import java.util.Date;
import java.util.Set;

@OWLClass(iri = Vocabulary.s_c_Occurrence)
public class Occurrence extends AbstractEntity implements HasOwlKey, Serializable {

    @ParticipationConstraints(nonEmpty = true)
    @OWLDataProperty(iri = Vocabulary.s_p_has_key)
    private String key;

    @ParticipationConstraints(nonEmpty = true)
    @OWLAnnotationProperty(iri = Vocabulary.s_p_label)
    private String name;

    @ParticipationConstraints(nonEmpty = true)
    @OWLDataProperty(iri = Vocabulary.s_p_has_start_time)
    protected Date startTime;

    @ParticipationConstraints(nonEmpty = true)
    @OWLDataProperty(iri = Vocabulary.s_p_has_end_time)
    protected Date endTime;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_event_type)
    protected URI eventType;

    @Types
    protected Set<String> types;

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public URI getEventType() {
        return eventType;
    }

    public void setEventType(URI eventType) {
        this.eventType = eventType;
    }

    public Set<String> getTypes() {
        return types;
    }

    public void setTypes(Set<String> types) {
        this.types = types;
    }

    @Override
    public String toString() {
        return "Occurrence{" +
                "name='" + name + '\'' +
                ", uri=" + uri +
                "}";
    }

    public cz.cvut.kbss.reporting.model.Occurrence toOccurrence() {
        final cz.cvut.kbss.reporting.model.Occurrence occurrence = new cz.cvut.kbss.reporting.model.Occurrence();
        occurrence.setUri(uri);
        occurrence.setKey(key);
        occurrence.setName(name);
        occurrence.setStartTime(startTime);
        occurrence.setEndTime(endTime);
        occurrence.setEventType(eventType);
        occurrence.setTypes(types);
        return occurrence;
    }
}
