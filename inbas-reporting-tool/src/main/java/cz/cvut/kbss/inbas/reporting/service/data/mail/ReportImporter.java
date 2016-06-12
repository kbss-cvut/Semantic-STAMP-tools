package cz.cvut.kbss.inbas.reporting.service.data.mail;

import cz.cvut.kbss.eccairs.report.e5xml.commons.NamedStream;
import java.util.stream.Stream;
import org.apache.jena.rdf.model.Model;

public interface ReportImporter {

    Stream<String> processDelegate(Object o) throws Exception;

    Stream<String> process(NamedStream ns) throws Exception;

    Stream<String> process(Model m) throws Exception;
}
