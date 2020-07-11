package cz.cvut.kbss.datatools.bpm2stampo.xml2stamprdf.model;

import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.onto.safety.stamp.Vocabulary;

import java.util.Set;

@OWLClass(iri= Vocabulary.s_c_event_type)
public class EventType extends Structure{

    @OWLObjectProperty(iri = "http://www.w3.org/2000/01/rdf-schema#subClassOf")
    protected Set<String> superClasses;

//    @OWLObjectProperty(iri = Vocabulary.s_p_has_control_structure_element_part)
//    protected Set<String> childEventTypes;
//
//    @OWLObjectProperty(iri = Vocabulary.s_p_has_control_structure_element_part)
//    protected Set<String> childConnections;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_participant)
    protected Set<String> participants;

//    public Set<String> getChildEventTypes() {
//        return childEventTypes;
//    }
//
//    public void setChildEventTypes(Set<String> childEventTypes) {
//        this.childEventTypes = childEventTypes;
//    }
//
//    public Set<String> getChildConnections() {
//        return childConnections;
//    }
//
//    public void setChildConnections(Set<String> childConnections) {
//        this.childConnections = childConnections;
//    }


    public Set<String> getSuperClasses() {
        return superClasses;
    }

    public void setSuperClasses(Set<String> superClasses) {
        this.superClasses = superClasses;
    }

    public Set<String> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<String> participants) {
        this.participants = participants;
    }
}
