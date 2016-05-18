/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.kbss.inbas.reporting.service.data.eccairs;

import cz.cvut.kbss.inbas.reporting.model.qam.Question;
import cz.cvut.kbss.jopa.Persistence;
import cz.cvut.kbss.jopa.model.EntityManagerFactoryImpl;
import cz.cvut.kbss.jopa.model.EntityManagerImpl;
import cz.cvut.kbss.jopa.model.JOPAPersistenceProperties;
import cz.cvut.kbss.jopa.model.JOPAPersistenceProvider;
import cz.cvut.kbss.ontodriver.config.OntoDriverProperties;
import cz.cvut.kbss.ontodriver.sesame.SesameDataSource;
import info.aduna.iteration.Iterations;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openrdf.model.Model;
import org.openrdf.model.Statement;
import org.openrdf.model.impl.LinkedHashModel;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.repository.config.RepositoryConfigException;
import org.openrdf.repository.manager.RepositoryManager;
import org.openrdf.repository.manager.RepositoryProvider;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.Rio;

/**
 *
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
public class LoadDataFromE5X {
    public static void main(String[] args) {
        
    }
    
    public static void processQuestions(Question q){
        try {
            // persist Question
            String tmpRepoUri = "file:///d:/downloads/inbas/tmp/sesame/repositories/e5xml-inport-tmp";
            String repoId = "e5xml-inport-tmp";
            EntityManagerFactoryImpl emf = (EntityManagerFactoryImpl)Persistence.createEntityManagerFactory("tmp-eccairs-import", initTempRepo(tmpRepoUri));
            EntityManagerImpl em = (EntityManagerImpl)emf.createEntityManager();
            em.persist(q);
            RepositoryManager rm = RepositoryProvider.getRepositoryManagerOfRepository(tmpRepoUri);
            Repository r = rm.getRepository(repoId);
            RepositoryConnection c = r.getConnection();
            RepositoryResult<Statement> res = c.getStatements(null, null, null, true);
            Model m = Iterations.addAll(res, new LinkedHashModel());
            Rio.write(m, System.out, RDFFormat.TURTLE);
            c.close();
            r.shutDown();
                    
        } catch (RepositoryConfigException ex) {
            Logger.getLogger(LoadDataFromE5X.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RepositoryException ex) {
            Logger.getLogger(LoadDataFromE5X.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RDFHandlerException ex) {
            Logger.getLogger(LoadDataFromE5X.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }  
    
    public static void loadData1(String fileName){
        EntityManagerFactoryImpl emf = (EntityManagerFactoryImpl)Persistence.createEntityManagerFactory("aaa", initParams());
        EntityManagerImpl em = (EntityManagerImpl)emf.createEntityManager();
        
    }
    
    public static void loadDataFromEMail(){
        
    }
    
    public static void loadData1(InputStream fileName){
        EntityManagerFactoryImpl emf = (EntityManagerFactoryImpl)Persistence.createEntityManagerFactory("aaa", initParams());
        EntityManagerImpl em = (EntityManagerImpl)emf.createEntityManager();
        
        
    }
    
    
    
    
    
    private static Map<String, String> initParams() {
        // to enable the access to the in-memory repository created internaly by 
        // JOPA because of this configuration we need to do the following:
        // implement a new SesameDataSource as follows
        // - initialize the SesameDriver with a custom one
        // implement a custom SesameDriver which initializes with a custom connector factory
        // 
        
        
        final Map<String, String> map = new HashMap<>();
//        properties.put(JOPAPersistenceProperties.ONTOLOGY_PHYSICAL_URI_KEY, );
        map.put(OntoDriverProperties.ONTOLOGY_LANGUAGE, "en");
        map.put(JOPAPersistenceProperties.SCAN_PACKAGE, "cz.cvut.kbss.inbas.reporting.model");
        map.put(JOPAPersistenceProperties.JPA_PERSISTENCE_PROVIDER, JOPAPersistenceProvider.class.getName());
        map.put(JOPAPersistenceProperties.DATA_SOURCE_CLASS, SesameDataSource.class.getName());
//        map.put(JOPAPersistenceProperties.ONTOLOGY_PHYSICAL_URI_KEY, "myrepo");
        map.put(JOPAPersistenceProperties.ONTOLOGY_PHYSICAL_URI_KEY, "http://localhost:18080/openrdf-sesame/repositories/UCL-05-04-2016-working-repo");
        map.put(JOPAPersistenceProperties.SCAN_PACKAGE, "cz.cvut.kbss.experiments.jopa");
//        map.put(SesameOntoDriverProperties.SESAME_USE_VOLATILE_STORAGE, "true");
//        map.put(JOPAPersistenceProperties., SesameDataSource.class.getName());
        
        return map;
    }
    private static Map<String, String> initTempRepo(String repoUri) {
        // to enable the access to the in-memory repository created internaly by 
        // JOPA because of this configuration we need to do the following:
        // implement a new SesameDataSource as follows
        // - initialize the SesameDriver with a custom one
        // implement a custom SesameDriver which initializes with a custom connector factory
        // 
        
        
        final Map<String, String> map = new HashMap<>();
//        properties.put(JOPAPersistenceProperties.ONTOLOGY_PHYSICAL_URI_KEY, );
        map.put(OntoDriverProperties.ONTOLOGY_LANGUAGE, "en");
        map.put(JOPAPersistenceProperties.SCAN_PACKAGE, "cz.cvut.kbss.inbas.reporting.model");
        map.put(JOPAPersistenceProperties.JPA_PERSISTENCE_PROVIDER, JOPAPersistenceProvider.class.getName());
        map.put(JOPAPersistenceProperties.DATA_SOURCE_CLASS, SesameDataSource.class.getName());
//        map.put(JOPAPersistenceProperties.ONTOLOGY_PHYSICAL_URI_KEY, "myrepo");
        map.put(JOPAPersistenceProperties.ONTOLOGY_PHYSICAL_URI_KEY, repoUri);
        
        map.put(JOPAPersistenceProperties.SCAN_PACKAGE, "cz.cvut.kbss.experiments.jopa");
//        map.put(SesameOntoDriverProperties.SESAME_USE_VOLATILE_STORAGE, "true");
//        map.put(JOPAPersistenceProperties., SesameDataSource.class.getName());
        
        return map;
    }
}
