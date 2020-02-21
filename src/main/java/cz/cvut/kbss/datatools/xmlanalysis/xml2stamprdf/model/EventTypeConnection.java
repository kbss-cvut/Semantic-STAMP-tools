package cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf.model;

import cz.cvut.kbss.jopa.model.annotations.MappedSuperclass;
import cz.cvut.kbss.onto.safety.stamp.Vocabulary;

import java.util.HashSet;

@MappedSuperclass
public class EventTypeConnection extends StructureConnection{

    public EventTypeConnection() {
        types = new HashSet<>();
        types.add(Vocabulary.s_c_process_connection);
    }
}
