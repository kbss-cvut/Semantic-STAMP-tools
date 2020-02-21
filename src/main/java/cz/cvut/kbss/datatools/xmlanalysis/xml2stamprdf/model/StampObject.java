package cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf.model;

import cz.cvut.kbss.jopa.model.annotations.MappedSuperclass;
import cz.cvut.kbss.jopa.model.annotations.OWLObjectProperty;
import cz.cvut.kbss.onto.safety.stamp.Vocabulary;

import java.util.Set;

@MappedSuperclass
public class StampObject extends Identifiable {

    @OWLObjectProperty(iri = Vocabulary.s_p_has_capability)
    protected Set<String> capabilities;

    public Set<String> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(Set<String> capabilities) {
        this.capabilities = capabilities;
    }
}
