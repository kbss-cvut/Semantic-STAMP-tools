package cz.cvut.kbss.reporting.model.location;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.reporting.model.AbstractEntity;
import cz.cvut.kbss.reporting.model.Vocabulary;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "javaClass")
@OWLClass(iri = Vocabulary.s_c_Location)
public abstract class Location extends AbstractEntity {

    /**
     * Copies this instance, copying all attributes except URI.
     *
     * @return Copy of this instance
     */
    public abstract Location copy();
}
