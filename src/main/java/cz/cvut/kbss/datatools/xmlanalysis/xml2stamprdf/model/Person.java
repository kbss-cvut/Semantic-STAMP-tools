package cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf.model;

import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.onto.safety.stamp.Vocabulary;

@OWLClass(iri = Vocabulary.s_c_person_controller)
public class Person extends Identifiable{
    @OWLDataProperty(iri = Vocabulary.s_p_label)
    protected String label;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
