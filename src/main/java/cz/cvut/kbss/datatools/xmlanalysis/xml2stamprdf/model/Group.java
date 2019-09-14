package cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf.model;

import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.onto.safety.stamp.Vocabulary;

import java.util.Set;

@OWLClass(iri = Vocabulary.s_c_people_group)
public class Group extends Identifiable {

    @OWLObjectProperty(iri = Vocabulary.s_p_has_participant)
    protected Set<String> controllerTypes;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_control_structure_element_part)
    protected Set<String> subGroups;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_participant)
    protected Set<String> people;

    @OWLDataProperty(iri = Vocabulary.s_p_label)
    protected String label;


    public Set<String> getPeople() {
        return people;
    }

    public void setPeople(Set<String> people) {
        this.people = people;
    }

    public Set<String> getSubGroups() {
        return subGroups;
    }

    public void setSubGroups(Set<String> subGroups) {
        this.subGroups = subGroups;
    }

    public Set<String> getControllerTypes() {
        return controllerTypes;
    }

    public void setControllerTypes(Set<String> controllerTypes) {
        this.controllerTypes = controllerTypes;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
