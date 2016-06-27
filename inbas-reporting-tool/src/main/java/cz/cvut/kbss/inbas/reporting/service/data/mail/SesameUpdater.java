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

    /**
     * This executes the SPARQL updates using a sesame repository. If something 
     * goes wrong during execution of the updates this method will catch the 
     * exception, rollback any dirty changes and rethrow the exception for further 
     * processing.
     * 
     * @param updates the sparql update queries to be executed
     * @throws RepositoryException
     * @throws RepositoryConfigException
     * @throws MalformedQueryException
     * @throws UpdateExecutionException 
     */
    public void executeUpdate(String... updates) throws RepositoryException, RepositoryConfigException, MalformedQueryException, UpdateExecutionException {
        Repository r = RepositoryProvider.getRepository(repository);
        r.initialize();
        RepositoryConnection c = r.getConnection();
        try {
            c.begin();
            for (String update : updates) {
                LOG.trace("Executing SPARQL Update {}", update);
                Update u = c.prepareUpdate(QueryLanguage.SPARQL, update);
                u.execute();
            }
            c.commit();
        // Do not close the repository, it may be used by the application persistence
        } catch (RepositoryException | MalformedQueryException | UpdateExecutionException ex) {
            if(c.isActive())
                c.rollback();
            throw ex;
//            LOG.error("Exception caught when executing Sesame update.", ex);
        }finally{
            c.close();
            r.shutDown();
        }
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }
}
