/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.kbss.inbas.reporting.persistence;

import static cz.cvut.kbss.inbas.reporting.util.ConfigParam.DRIVER;
import static cz.cvut.kbss.inbas.reporting.util.ConfigParam.REPOSITORY_URL;
import cz.cvut.kbss.jopa.Persistence;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import static cz.cvut.kbss.jopa.model.JOPAPersistenceProperties.DATA_SOURCE_CLASS;
import static cz.cvut.kbss.jopa.model.JOPAPersistenceProperties.DATA_SOURCE_PASSWORD;
import static cz.cvut.kbss.jopa.model.JOPAPersistenceProperties.DATA_SOURCE_USERNAME;
import static cz.cvut.kbss.jopa.model.JOPAPersistenceProperties.ONTOLOGY_PHYSICAL_URI_KEY;
import static cz.cvut.kbss.jopa.model.JOPAPersistenceProperties.SCAN_PACKAGE;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 *
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
@Configuration
@PropertySource("classpath:config.properties")
public class EccairsReportImportPersistenceFactory {
    
    private static final String USERNAME_PROPERTY = "username";
    private static final String PASSWORD_PROPERTY = "password";
    
    @Autowired
    private Environment environment;

    private EntityManagerFactory emf;

    @Bean(name = "eccairsPU")
    public EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

    @PostConstruct
    private void init() {
        final Map<String, String> properties = new HashMap<>(PersistenceFactory.getDefaultParams());
        properties.put(ONTOLOGY_PHYSICAL_URI_KEY, environment.getProperty(REPOSITORY_URL.toString()));
        properties.put(DATA_SOURCE_CLASS, environment.getProperty(DRIVER.toString()));
        if (environment.getProperty(USERNAME_PROPERTY) != null) {
            properties.put(DATA_SOURCE_USERNAME, environment.getProperty(USERNAME_PROPERTY));
            properties.put(DATA_SOURCE_PASSWORD, environment.getProperty(PASSWORD_PROPERTY));
        }
        // override the existing setting
        properties.put(SCAN_PACKAGE, "cz.cvut.kbss.inbas.reporting.model");
        this.emf = Persistence.createEntityManagerFactory("eccairsPU", properties);
    }

    @PreDestroy
    private void close() {
        emf.close();
    }
}
