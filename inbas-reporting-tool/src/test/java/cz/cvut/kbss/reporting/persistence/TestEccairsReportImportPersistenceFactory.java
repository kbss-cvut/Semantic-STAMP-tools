/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.kbss.reporting.persistence;

import cz.cvut.kbss.jopa.Persistence;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import cz.cvut.kbss.jopa.model.JOPAPersistenceProperties;
import cz.cvut.kbss.reporting.util.ConfigParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;

/**
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
@Configuration
@PropertySource("classpath:config.properties")
public class TestEccairsReportImportPersistenceFactory {

    private static final String URL_PROPERTY = "test." + ConfigParam.REPOSITORY_URL;
    private static final String DRIVER_PROPERTY = "test." + ConfigParam.DRIVER.toString();
    private static final String USERNAME_PROPERTY = "test.username";
    private static final String PASSWORD_PROPERTY = "test.password";

    @Autowired
    private Environment environment;

    private EntityManagerFactory emf;

    @Bean(name = "eccairsPU")
    public EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

    @PostConstruct
    private void init() {
        final Map<String, String> properties = TestPersistenceFactory.getDefaultProperties();
        properties.put(JOPAPersistenceProperties.SCAN_PACKAGE, "cz.cvut.kbss.eccairs.report.model");
        properties.put(JOPAPersistenceProperties.ONTOLOGY_PHYSICAL_URI_KEY, environment.getProperty(URL_PROPERTY));
        properties.put(JOPAPersistenceProperties.DATA_SOURCE_CLASS, environment.getProperty(DRIVER_PROPERTY));
        if (environment.getProperty(USERNAME_PROPERTY) != null) {
            properties.put(JOPAPersistenceProperties.DATA_SOURCE_USERNAME, environment.getProperty(USERNAME_PROPERTY));
            properties.put(JOPAPersistenceProperties.DATA_SOURCE_PASSWORD, environment.getProperty(PASSWORD_PROPERTY));
        }
        this.emf = Persistence.createEntityManagerFactory("eccairsPU", properties);
    }

    @PreDestroy
    private void close() {
        emf.close();
    }
}
