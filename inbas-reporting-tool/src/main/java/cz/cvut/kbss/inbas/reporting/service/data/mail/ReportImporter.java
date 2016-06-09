package cz.cvut.kbss.inbas.reporting.service.data.mail;

import cz.cvut.kbss.ucl.NamedStream;
import org.apache.jena.rdf.model.Model;

public interface ReportImporter {

    void processDelegate(Object o);

    void process(NamedStream ns);

    void process(Model m);
}
