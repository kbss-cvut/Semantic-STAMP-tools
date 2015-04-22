package cz.cvut.kbss.inbas.audit.persistence;

import cz.cvut.kbss.jopa.Persistence;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import cz.cvut.kbss.jopa.owlapi.OWLAPIPersistenceProperties;
import cz.cvut.kbss.jopa.owlapi.OWLAPIPersistenceProvider;
import cz.cvut.kbss.ontodriver.OntologyStorageProperties;
import cz.cvut.kbss.ontodriver_new.OntoDriverProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides entity manager factory bean.
 *
 * @author ledvima1
 */
@Configuration
@PropertySource("classpath:config.properties")
public class PersistenceFactory {

    private static final Map<String, String> PARAMS = initParams();

    @Autowired
    private Environment environment;

    private EntityManagerFactory emf;

    @Bean
    public EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

    @PostConstruct
    private void init() {
        final OntologyStorageProperties.OntologyStoragePropertiesBuilder builder = OntologyStorageProperties.physicalUri(
                URI.create(environment.getProperty("repositoryUrl"))).driver(environment.getProperty("driver"));
        if (environment.getProperty("username") != null) {
            builder.username(environment.getProperty("username"));
            builder.password(environment.getProperty("password"));
        }
        final OntologyStorageProperties storageProperties = builder.build();
        this.emf = Persistence.createEntityManagerFactory("inbasPU", storageProperties, PARAMS);
    }

    @PreDestroy
    private void close() {
        emf.close();
    }

    private static Map<String, String> initParams() {
        final Map<String, String> map = new HashMap<>();
        map.put(OntoDriverProperties.ONTOLOGY_LANGUAGE, "en");
        map.put(OWLAPIPersistenceProperties.SCAN_PACKAGE, "cz.cvut.kbss.inbas.audit.model");
        map.put("storage", "new");  // Will be removed in the future
        map.put(OWLAPIPersistenceProperties.JPA_PERSISTENCE_PROVIDER,
                OWLAPIPersistenceProvider.class.getName());
        return map;
    }
}
