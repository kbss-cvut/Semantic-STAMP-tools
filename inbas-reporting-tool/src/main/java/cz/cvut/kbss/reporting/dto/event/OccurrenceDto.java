package cz.cvut.kbss.reporting.dto.event;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import cz.cvut.kbss.jopa.model.annotations.OWLAnnotationProperty;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.reporting.model.StampVocabulary;
import cz.cvut.kbss.reporting.model.Vocabulary;
import cz.cvut.kbss.reporting.model.location.Location;

import java.net.URI;

@JsonIdentityInfo(property = "referenceId", generator = ObjectIdGenerators.PropertyGenerator.class)
@OWLClass(iri = Vocabulary.s_c_Occurrence)
public class OccurrenceDto extends EventDto {

    @OWLDataProperty(iri = Vocabulary.s_p_has_key)
    private String key;

    @OWLAnnotationProperty(iri = Vocabulary.s_p_label)
    private String name;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_location)
    private Location location;

    @OWLObjectProperty(iri = StampVocabulary.s_p_contains_loss_event_of_type)
    private URI lossEventType;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public URI getLossEventType() {
        return lossEventType;
    }

    public void setLossEventType(URI lossEventType) {
        this.lossEventType = lossEventType;
    }
}
