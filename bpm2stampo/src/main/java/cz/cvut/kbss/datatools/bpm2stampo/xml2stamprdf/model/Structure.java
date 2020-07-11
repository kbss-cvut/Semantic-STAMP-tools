package cz.cvut.kbss.datatools.bpm2stampo.xml2stamprdf.model;

import cz.cvut.kbss.jopa.model.annotations.MappedSuperclass;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.onto.safety.stamp.Vocabulary;

import java.util.Set;

@MappedSuperclass
public class Structure extends StampObject{

    public Structure() {
        types.add(Vocabulary.s_c_structure);
    }

    @OWLObjectProperty(iri = Vocabulary.s_p_has_control_structure_element_part)
    protected Set<String> components;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_control_structure_element_part)
    protected Set<String> connections;

    @OWLObjectProperty(iri = Vocabulary.s_p_has_same_structure_as)
    protected String sameStructureAs;


    public Set<String> getComponents() {
        return components;
    }

    public void setComponents(Set<String> components) {
        this.components = components;
    }

    public Set<String> getConnections() {
        return connections;
    }

    public void setConnections(Set<String> connections) {
        this.connections = connections;
    }

    public String getSameStructureAs() {
        return sameStructureAs;
    }

    public void setSameStructureAs(String sameStructureAs) {
        this.sameStructureAs = sameStructureAs;
    }
}
