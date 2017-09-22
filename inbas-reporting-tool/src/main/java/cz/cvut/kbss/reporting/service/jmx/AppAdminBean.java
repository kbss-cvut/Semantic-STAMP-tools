package cz.cvut.kbss.reporting.service.jmx;

import ch.qos.logback.classic.LoggerContext;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import cz.cvut.kbss.reporting.model.Person;
import cz.cvut.kbss.reporting.service.ConfigReader;
import cz.cvut.kbss.reporting.service.PersonService;
import cz.cvut.kbss.reporting.service.event.InvalidateCacheEvent;
import cz.cvut.kbss.reporting.util.ConfigParam;
import cz.cvut.kbss.reporting.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.Random;

@Component
@ManagedResource(objectName = "bean:name=FD-AdminBean", description = "Application administration bean.")
public class AppAdminBean implements ApplicationEventPublisherAware {

    private static final Logger LOG = LoggerFactory.getLogger(AppAdminBean.class);

    private static final String LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final int PASSWORD_LENGTH = 8;

    private ApplicationEventPublisher eventPublisher;

    private final EntityManagerFactory emf;

    private final ConfigReader configReader;
    private final PersonService personService;

    @Autowired
    public AppAdminBean(EntityManagerFactory emf, ConfigReader configReader, PersonService personService) {
        this.emf = emf;
        this.configReader = configReader;
        this.personService = personService;
    }

    @ManagedOperation(description = "Invalidates the application's caches.")
    public void invalidateCaches() {
        eventPublisher.publishEvent(new InvalidateCacheEvent(this));
        emf.getCache().evictAll();
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;
    }

    @PostConstruct
    void initSystemAdmin() {    // Package-private for testing purposes
        final Person admin = Constants.SYSTEM_ADMIN;
        if (personService.exists(admin.getUsername())) {
            LOG.info("Admin already exists.");
            return;
        }
        final String passwordPlain = generatePassword();
        admin.setPassword(passwordPlain);
        personService.persist(admin);
        LOG.info("----------------------------------------------");
        LOG.info("Admin credentials are: {}/{}", admin.getUsername(), passwordPlain);
        LOG.info("----------------------------------------------");
        final File directory = new File(configReader.getConfig(ConfigParam.ADMIN_CREDENTIALS_LOCATION));
        if (!directory.exists()) {
            directory.mkdirs();
        }
        final File credentialsFile = new File(
                configReader.getConfig(ConfigParam.ADMIN_CREDENTIALS_LOCATION) + File.separator +
                        Constants.ADMIN_CREDENTIALS_FILE);
        try {
            Files.write(credentialsFile.toPath(),
                    Collections.singletonList(admin.getUsername() + "/" + passwordPlain),
                    StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            LOG.error("Unable to create admin credentials file.", e);
        }
    }

    private String generatePassword() {
        final StringBuilder sb = new StringBuilder(PASSWORD_LENGTH);
        final Random random = new Random();
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            if (random.nextBoolean()) {
                sb.append(random.nextInt(10));
            } else {
                char c = LETTERS.charAt(random.nextInt(LETTERS.length()));
                sb.append(random.nextBoolean() ? c : Character.toUpperCase(c));
            }
        }
        return sb.toString();
    }

    @PreDestroy
    void tearDown() {
        // Shuts down logback context, which unregisters logback JMX bean. Prevents memory leaks on undeploy
        // See https://logback.qos.ch/manual/jmxConfig.html
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        lc.stop();
    }
}
