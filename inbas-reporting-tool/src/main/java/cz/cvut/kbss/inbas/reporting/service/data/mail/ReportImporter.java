package cz.cvut.kbss.inbas.reporting.service.data.mail;

import cz.cvut.kbss.datatools.mail.model.Message;
import cz.cvut.kbss.eccairs.report.e5xml.commons.NamedStream;
import java.util.List;
import org.apache.jena.rdf.model.Model;

public interface ReportImporter {

    List<String> processDelegate(Object o) throws Exception;

    List<String> process(NamedStream ns) throws Exception;

    List<String> process(Model m) throws Exception;
    
    List<String> process(Message m);
    
}
