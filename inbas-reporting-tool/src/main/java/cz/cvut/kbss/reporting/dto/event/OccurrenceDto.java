package cz.cvut.kbss.reporting.dto.event;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import cz.cvut.kbss.jopa.model.annotations.OWLAnnotationProperty;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.reporting.model.Vocabulary;

@JsonIdentityInfo(property = "referenceId", generator = ObjectIdGenerators.PropertyGenerator.class)
@OWLClass(iri = Vocabulary.s_c_Occurrence)
public class OccurrenceDto extends EventDto {

    @OWLDataProperty(iri = Vocabulary.s_p_has_key)
    private String key;

    @OWLAnnotationProperty(iri = Vocabulary.s_p_label)
    private String name;

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
}
