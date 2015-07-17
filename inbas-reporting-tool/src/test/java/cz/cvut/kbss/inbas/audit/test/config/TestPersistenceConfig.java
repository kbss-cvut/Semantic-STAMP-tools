package cz.cvut.kbss.inbas.audit.test.config;

import cz.cvut.kbss.inbas.audit.persistence.TestPersistenceFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author ledvima1
 */
@Configuration
@ComponentScan(basePackages = "cz.cvut.kbss.inbas.audit.persistence.dao")
@Import({TestPersistenceFactory.class})
public class TestPersistenceConfig {

}
