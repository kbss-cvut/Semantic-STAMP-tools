package cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf.model;

import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.onto.safety.stamp.Vocabulary;

import java.util.Set;

@OWLClass(iri = Vocabulary.s_c_controller_type)
public class ControllerType extends StampObject{

    @OWLDataProperty(iri = Vocabulary.s_p_label)
    protected String label;

    @OWLObjectProperty(iri = cz.cvut.kbss.datatools.xmlanalysis.voc.Vocabulary.p_sub_class_of)
    protected Set<String> subControllerTypes;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Set<String> getSubControllerTypes() {
        return subControllerTypes;
    }

    public void setSubControllerTypes(Set<String> subControllerTypes) {
        this.subControllerTypes = subControllerTypes;
    }
}
