package cz.cvut.kbss.inbas.reporting.service.data.mail;

import cz.cvut.kbss.commons.io.NamedStream;
import cz.cvut.kbss.datatools.mail.model.Message;
import org.apache.jena.rdf.model.Model;

import java.net.URI;
import java.util.List;

public interface ReportImporter {

    List<URI> processDelegate(Object o) throws Exception;

    /**
     * Processes the specified stream, importing all reports it contains.
     *
     * @param ns Named stream containing the report to import
     * @return list of URIs of the imported reports.
     * @throws Exception
     */
    List<URI> process(NamedStream ns) throws Exception;

    List<URI> process(Model m) throws Exception;

    List<URI> process(Message m);

}
