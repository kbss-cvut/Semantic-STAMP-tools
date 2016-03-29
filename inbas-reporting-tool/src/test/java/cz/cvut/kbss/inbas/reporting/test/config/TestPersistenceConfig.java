package cz.cvut.kbss.inbas.reporting.test.config;

import cz.cvut.kbss.inbas.reporting.persistence.TestPersistenceFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = "cz.cvut.kbss.inbas.reporting.persistence.dao")
@Import({TestPersistenceFactory.class})
public class TestPersistenceConfig {
}
