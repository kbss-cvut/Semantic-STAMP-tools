package cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf.model;

import cz.cvut.kbss.jopa.model.annotations.MappedSuperclass;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.onto.safety.stamp.Vocabulary;

@MappedSuperclass
public class StructureConnection extends Structure{
    @OWLObjectProperty(iri= Vocabulary.s_p_from_structure_component)
    protected String from;
    @OWLObjectProperty(iri= Vocabulary.s_p_to_structure_component)
    protected String to;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
