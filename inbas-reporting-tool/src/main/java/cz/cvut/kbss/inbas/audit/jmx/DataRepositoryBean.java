package cz.cvut.kbss.inbas.audit.jmx;

import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFHandler;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.rdfxml.util.RDFXMLPrettyWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;

@Component
@ManagedResource(objectName = "bean:name=DataRepositoryMBean", description = "MBean for accessing raw repository data.")
public class DataRepositoryBean {

    private static final Logger LOG = LoggerFactory.getLogger(DataRepositoryBean.class);

    @Autowired
    private Repository repository;

    @ManagedOperation(description = "Returns raw repository data.")
    public String getRepositoryData() {
        try {
            final RepositoryConnection connection = repository.getConnection();
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            final RDFHandler rdfHandler = new RDFXMLPrettyWriter(bos);
            connection.export(rdfHandler);
            connection.close();
            return new String(bos.toByteArray());
        } catch (RepositoryException | RDFHandlerException e) {
            LOG.error("Unable to read data from repository.", e);
            return "";
        }
    }
}
