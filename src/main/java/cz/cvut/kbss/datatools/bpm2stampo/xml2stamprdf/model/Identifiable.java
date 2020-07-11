package cz.cvut.kbss.datatools.bpm2stampo.xml2stamprdf.model;

import cz.cvut.kbss.jopa.model.annotations.Id;
import cz.cvut.kbss.jopa.model.annotations.MappedSuperclass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.jopa.model.annotations.Types;
import cz.cvut.kbss.onto.safety.stamp.Vocabulary;

import java.util.HashSet;
import java.util.Set;

@MappedSuperclass
public class Identifiable {
    @Id
    protected String iri;

    @Types
    protected Set<String> types = new HashSet<>();

    @OWLDataProperty(iri = Vocabulary.s_p_label)
    protected String label;

    public String getIri() {
        return iri;
    }

    public void setIri(String iri) {
        this.iri = iri;
    }

    public Set<String> getTypes() {
        return types;
    }

    public void setTypes(Set<String> types) {
        this.types = types;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
