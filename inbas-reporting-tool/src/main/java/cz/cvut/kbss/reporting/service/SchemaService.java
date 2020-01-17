package cz.cvut.kbss.reporting.service;

import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import cz.cvut.kbss.reporting.exception.FileFormatNotSupportedException;
import cz.cvut.kbss.reporting.util.ConfigParam;
import org.eclipse.rdf4j.common.lang.FileFormat;
import org.eclipse.rdf4j.common.lang.service.FileFormatServiceRegistry;
import org.eclipse.rdf4j.query.Update;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.eclipse.rdf4j.repository.manager.RemoteRepositoryManager;
import org.eclipse.rdf4j.repository.manager.RepositoryManager;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Service
public class SchemaService {

    private static final Logger LOG = LoggerFactory.getLogger(SchemaService.class);

    private static final List<RDFFormat> formats =  Arrays.asList(
            RDFFormat.BINARY, RDFFormat.JSONLD, RDFFormat.N3,
            RDFFormat.NQUADS, RDFFormat.NTRIPLES, RDFFormat.RDFXML,
            RDFFormat.TURTLE, RDFFormat.TRIG, RDFFormat.TRIX
    );

    @Autowired
    private ConfigReader configReader;

    public void replaceSchema(String fileName, InputStream content){
        String schemaServerUrl = configReader.getConfig(ConfigParam.SCHEMA_SERVER_URL);
        String repositoryUrl = configReader.getConfig(ConfigParam.EVENT_TYPE_REPOSITORY_URL);
        // TODO - this is a hack
        HTTPRepository r = new HTTPRepository(repositoryUrl);
        r.initialize();
        RepositoryConnection c = r.getConnection();
        // clear repo contents
        c.begin();
        if(!c.isEmpty()){
            Update u1 = c.prepareUpdate("DELETE{?s ?p ?o}WHERE{?s ?p ?o}");
            u1.execute();
        }
        c.commit();

        // guess file format from file name
        RDFFormat ff = RDFFormat.matchFileName(fileName, formats).orElse(null);
        if(ff == null){
            throw new FileFormatNotSupportedException(
                    String.format("The uploaded file's format is not supported. File name \"%s\"", fileName)
            );
        }

        // load new schema into repo
        c.begin();
        try {
            c.add(content, null, ff);
        } catch (IOException e) {
            LOG.warn("could not save file \"{}\" with guessed format \"{}\" to repo \"{}\".", fileName, ff, repositoryUrl);
        }
        c.commit();
    }
}
