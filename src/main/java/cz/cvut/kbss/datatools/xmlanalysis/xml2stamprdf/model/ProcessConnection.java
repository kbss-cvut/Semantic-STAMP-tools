package cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf.model;

import cz.cvut.kbss.jopa.model.annotations.MappedSuperclass;
import cz.cvut.kbss.jopa.model.annotations.Types;
import cz.cvut.kbss.onto.safety.stamp.Vocabulary;

import java.util.HashSet;
import java.util.Set;

@MappedSuperclass
public class ProcessConnection extends Identifiable{

    @Types
    protected Set<String> types;

    public ProcessConnection() {
        types = new HashSet<>();
        types.add(Vocabulary.s_c_process_connection);
    }

    public Set<String> getTypes() {
        return types;
    }

    public void setTypes(Set<String> types) {
        this.types = types;
    }
}
