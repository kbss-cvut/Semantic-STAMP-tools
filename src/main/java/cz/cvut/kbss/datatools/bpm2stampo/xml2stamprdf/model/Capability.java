package cz.cvut.kbss.datatools.bpm2stampo.xml2stamprdf.model;

import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.onto.safety.stamp.Vocabulary;

@OWLClass(iri = Vocabulary.s_c_capability)
public class Capability extends StampObject {
    @OWLObjectProperty(iri = Vocabulary.s_p_is_manifested_by)
    protected String manifestation;

    public String getManifestation() {
        return manifestation;
    }

    public void setManifestation(String manifestation) {
        this.manifestation = manifestation;
    }
}
