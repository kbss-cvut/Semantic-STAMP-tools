/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
public class SesameUpdater {
    protected String repository;
    public void executeUpdate(String... updates) {
        try {
//            RemoteRepositoryManager rrm = RemoteRepositoryManager.getInstance("https://dev.inbas.cz/openrdf-sesame");
//            rrm.getRepository(null).
//            Repository r = RepositoryProvider.getRepository("http://martin.inbas.cz/openrdf-sesame/repositories/reports-refactoring");
            Repository r = RepositoryProvider.getRepository(repository);
            r.initialize();

            for (String update : updates) {
                RepositoryConnection c = r.getConnection();
                c.begin();
                System.out.println(update);
                Update u = c.prepareUpdate(QueryLanguage.SPARQL, update);
                u.execute();
                c.commit();
            }
        } catch (RepositoryException ex) {
            Logger.getLogger(cz.cvut.kbss.ucl.ScriptE5XDataFromEmail2RDFForMiro.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RepositoryConfigException ex) {
            Logger.getLogger(cz.cvut.kbss.ucl.ScriptE5XDataFromEmail2RDFForMiro.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedQueryException ex) {
            Logger.getLogger(cz.cvut.kbss.ucl.ScriptE5XDataFromEmail2RDFForMiro.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UpdateExecutionException ex) {
            Logger.getLogger(cz.cvut.kbss.ucl.ScriptE5XDataFromEmail2RDFForMiro.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }
    
    
    
}
