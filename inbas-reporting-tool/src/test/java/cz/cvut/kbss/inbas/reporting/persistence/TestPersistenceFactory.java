package cz.cvut.kbss.inbas.reporting.persistence;

import cz.cvut.kbss.jopa.Persistence;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import cz.cvut.kbss.jopa.model.JOPAPersistenceProperties;
import cz.cvut.kbss.jopa.model.JOPAPersistenceProvider;
import cz.cvut.kbss.ontodriver.config.OntoDriverProperties;
import cz.cvut.kbss.ontodriver.sesame.config.SesameOntoDriverProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ledvima1
 */
@Configuration
@PropertySource("classpath:config.properties")
public class TestPersistenceFactory {

    private static final String URL_PROPERTY = "test.repositoryUrl";
    private static final String DRIVER_PROPERTY = "test.driver";
    private static final String USERNAME_PROPERTY = "test.username";
    private static final String PASSWORD_PROPERTY = "test.password";

    @Autowired
    private Environment environment;

    private EntityManagerFactory emf;

    @Bean
    public EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

    @PostConstruct
    private void init() {
        final Map<String, String> properties = new HashMap<>();
        properties.put(OntoDriverProperties.ONTOLOGY_LANGUAGE, "en");
        properties.put(JOPAPersistenceProperties.SCAN_PACKAGE, "cz.cvut.kbss.inbas.audit.model");
        properties.put(SesameOntoDriverProperties.SESAME_USE_VOLATILE_STORAGE, Boolean.TRUE.toString());
        properties.put(SesameOntoDriverProperties.SESAME_USE_INFERENCE, Boolean.FALSE.toString());
        properties.put(JOPAPersistenceProperties.JPA_PERSISTENCE_PROVIDER, JOPAPersistenceProvider.class.getName());
        properties.put(JOPAPersistenceProperties.ONTOLOGY_PHYSICAL_URI_KEY, environment.getProperty(URL_PROPERTY));
        properties.put(JOPAPersistenceProperties.DATA_SOURCE_CLASS, environment.getProperty(DRIVER_PROPERTY));
        if (environment.getProperty(USERNAME_PROPERTY) != null) {
            properties.put(JOPAPersistenceProperties.DATA_SOURCE_USERNAME, environment.getProperty(USERNAME_PROPERTY));
            properties.put(JOPAPersistenceProperties.DATA_SOURCE_PASSWORD, environment.getProperty(PASSWORD_PROPERTY));
        }
        this.emf = Persistence.createEntityManagerFactory("inbasTestPU", properties);
    }

    @PreDestroy
    private void close() {
        emf.close();
    }
}
