package cz.cvut.kbss.inbas.reporting.service.data.mail;

import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.Update;
import org.openrdf.query.UpdateExecutionException;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.config.RepositoryConfigException;
import org.openrdf.repository.manager.RepositoryProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
public class SesameUpdater {

    private static final Logger LOG = LoggerFactory.getLogger(SesameUpdater.class);

    protected String repository;

    public void executeUpdate(String... updates) {
        try {
            Repository r = RepositoryProvider.getRepository(repository);
            r.initialize();

            RepositoryConnection c = r.getConnection();
            for (String update : updates) {
                c.begin();
                LOG.trace("Executing SPARQL Update {}", update);
                Update u = c.prepareUpdate(QueryLanguage.SPARQL, update);
                u.execute();
                c.commit();
            }
            c.close();
            // Do not close the repository, it may be used by the application persistence
        } catch (RepositoryException | RepositoryConfigException | MalformedQueryException | UpdateExecutionException ex) {
            LOG.error("Exception caught when executing Sesame update.", ex);
        }
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }
}
