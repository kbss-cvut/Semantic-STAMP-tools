package cz.cvut.kbss.reporting.persistence.sesame;

import org.openrdf.model.ValueFactory;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFHandler;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.rdfxml.util.RDFXMLPrettyWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.net.URI;

@Component
public class DataDao {

    private static final Logger LOG = LoggerFactory.getLogger(DataDao.class);

    @Autowired
    private SesamePersistenceProvider sesameRepository;

    /**
     * Exports repository data from the specified context and passes it to the handler.
     *
     * @param contextUri Context from which the data should be exported. Optional
     * @param handler    Handler for the exported data
     */
    public void getRepositoryData(URI contextUri, RDFHandler handler) {
        try {
            final RepositoryConnection connection = sesameRepository.getRepository().getConnection();
            try {
                final ValueFactory valueFactory = connection.getValueFactory();
                if (contextUri != null) {
                    connection.export(handler, valueFactory.createURI(contextUri.toString()));
                } else {
                    connection.export(handler);
                }
            } finally {
                connection.close();
            }
        } catch (RepositoryException | RDFHandlerException e) {
            LOG.error("Unable to read data from repository.", e);
        }
    }

    /**
     * Gets raw content of the repository.
     * <p>
     * The data are serialized using Sesame's {@link RDFXMLPrettyWriter}.
     *
     * @return Repository content serialized as String
     */
    public String getRepositoryData() {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final RDFHandler rdfHandler = new RDFXMLPrettyWriter(bos);
        getRepositoryData(null, rdfHandler);
        return new String(bos.toByteArray());
    }
}
