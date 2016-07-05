package cz.cvut.kbss.inbas.reporting.environment.config;

import cz.cvut.kbss.inbas.reporting.persistence.TestFormGenPersistenceFactory;
import cz.cvut.kbss.inbas.reporting.persistence.TestPersistenceFactory;
import cz.cvut.kbss.inbas.reporting.persistence.sesame.SesamePersistenceProvider;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(basePackages = {"cz.cvut.kbss.inbas.reporting.persistence.dao",
        "cz.cvut.kbss.inbas.reporting.persistence.sesame"})
@Import({TestPersistenceFactory.class,
        TestFormGenPersistenceFactory.class,})
public class TestPersistenceConfig {

    @Bean
    @Primary
    public SesamePersistenceProvider persistenceProvider() {
        return null;
    }
}
