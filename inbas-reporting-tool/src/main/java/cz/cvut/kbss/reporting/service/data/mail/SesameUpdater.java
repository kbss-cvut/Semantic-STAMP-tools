package cz.cvut.kbss.reporting.service.data.mail;

import cz.cvut.kbss.reporting.persistence.sesame.SesamePersistenceProvider;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.Update;
import org.openrdf.query.UpdateExecutionException;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.config.RepositoryConfigException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
@Deprecated
public class SesameUpdater {

    private static final Logger LOG = LoggerFactory.getLogger(SesameUpdater.class);

    @Autowired
    private SesamePersistenceProvider sesamePersistence;

    /**
     * This executes the SPARQL updates using a sesame repository. If something goes wrong during execution of the
     * updates this method will catch the exception, rollback any dirty changes and rethrow the exception for further
     * processing.
     *
     * @param updates the sparql update queries to be executed
     * @throws RepositoryException
     * @throws RepositoryConfigException
     * @throws MalformedQueryException
     * @throws UpdateExecutionException
     */
    public void executeUpdate(String... updates)
            throws RepositoryException, RepositoryConfigException, MalformedQueryException, UpdateExecutionException {
        final Repository repository = sesamePersistence.getRepository();
        RepositoryConnection c = repository.getConnection();
        try {
            c.begin();
            for (String update : updates) {
                LOG.trace("Executing SPARQL Update {}", update);
                Update u = c.prepareUpdate(QueryLanguage.SPARQL, update);
                u.execute();
            }
            c.commit();
            // Do not close the repository, it may be used by the application persistence
        } finally {
            c.close();
        }
    }
}
