package cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf.model;

import cz.cvut.kbss.jopa.model.annotations.MappedSuperclass;
import cz.cvut.kbss.onto.safety.stamp.Vocabulary;

@Deprecated
@MappedSuperclass
public class DirectedEventTypeConnection extends EventTypeConnection {

    public DirectedEventTypeConnection() {
        super();
        types.add(Vocabulary.s_c_structure_connection);
    }
}