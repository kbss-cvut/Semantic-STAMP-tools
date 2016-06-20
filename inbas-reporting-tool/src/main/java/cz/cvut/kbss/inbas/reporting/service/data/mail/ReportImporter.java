package cz.cvut.kbss.inbas.reporting.service.data.mail;

import cz.cvut.kbss.datatools.mail.model.Message;
import cz.cvut.kbss.commons.io.NamedStream;
import java.net.URI;
import java.util.List;
import org.apache.jena.rdf.model.Model;

public interface ReportImporter {

    List<URI> processDelegate(Object o) throws Exception;
    
    /**
     * @param ns
     * @return list of URIs of the imported reports.
     * @throws Exception 
     */
    List<URI> process(NamedStream ns) throws Exception;

    List<URI> process(Model m) throws Exception;
    
    List<URI> process(Message m);
    
}
