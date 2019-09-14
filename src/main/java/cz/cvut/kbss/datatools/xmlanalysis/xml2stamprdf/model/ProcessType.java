package cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf.model;

import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.onto.safety.stamp.Vocabulary;

import java.util.Set;

@OWLClass(iri= Vocabulary.s_c_controlled_process)
public class ProcessType extends Identifiable{

    @OWLDataProperty(iri = Vocabulary.s_p_label)
    protected String label;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_control_structure_element_part)
    protected Set<String> childProcessTypes;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_control_structure_element_part)
    protected Set<String> childConnections;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_participant)
    protected Set<String> participants;

    public Set<String> getChildProcessTypes() {
        return childProcessTypes;
    }

    public void setChildProcessTypes(Set<String> childProcessTypes) {
        this.childProcessTypes = childProcessTypes;
    }

    public Set<String> getChildConnections() {
        return childConnections;
    }

    public void setChildConnections(Set<String> childConnections) {
        this.childConnections = childConnections;
    }

    public Set<String> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<String> participants) {
        this.participants = participants;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
