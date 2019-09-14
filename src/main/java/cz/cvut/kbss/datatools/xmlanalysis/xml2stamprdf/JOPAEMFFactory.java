package cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf;

import cz.cvut.kbss.jopa.Persistence;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import cz.cvut.kbss.jopa.model.JOPAPersistenceProperties;
import cz.cvut.kbss.jopa.model.JOPAPersistenceProvider;
import cz.cvut.kbss.ontodriver.config.OntoDriverProperties;
import cz.cvut.kbss.ontodriver.sesame.config.SesameOntoDriverProperties;

import java.util.HashMap;
import java.util.Map;

public class JOPAEMFFactory {

    public EntityManagerFactory createEntityManagerFactoryVolatileStorage(String puName, String packageToScan){
        Map<String, String> props = getDefaultProperties();

        props.put(JOPAPersistenceProperties.SCAN_PACKAGE, packageToScan);
        props.put(JOPAPersistenceProperties.ONTOLOGY_PHYSICAL_URI_KEY, "myscheme:"+puName);
        props.put(SesameOntoDriverProperties.SESAME_USE_VOLATILE_STORAGE, "true");

        return Persistence.createEntityManagerFactory(puName, props);
    }

    public Map<String, String> getDefaultProperties(){
        Map<String,String> props = new HashMap<>();
        props.put(OntoDriverProperties.ONTOLOGY_LANGUAGE, "en");
        props.put(JOPAPersistenceProperties.JPA_PERSISTENCE_PROVIDER, JOPAPersistenceProvider.class.getName());
        props.put(JOPAPersistenceProperties.DATA_SOURCE_CLASS, "cz.cvut.kbss.ontodriver.sesame.SesameDataSource");

        return props;
    }
}
