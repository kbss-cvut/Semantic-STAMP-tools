package cz.cvut.kbss.reporting.model.location;

import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.reporting.model.AbstractEntity;
import cz.cvut.kbss.reporting.model.Vocabulary;

@OWLClass(iri = Vocabulary.s_c_Location)
public abstract class Location extends AbstractEntity {

    /**
     * Copies this instance, copying all attributes except URI.
     *
     * @return Copy of this instance
     */
    public abstract Location copy();
}
