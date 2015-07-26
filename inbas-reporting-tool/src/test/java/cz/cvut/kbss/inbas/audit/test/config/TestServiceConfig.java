package cz.cvut.kbss.inbas.audit.test.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author ledvima1
 */
@Configuration
@Import(TestPersistenceConfig.class)
@ComponentScan(basePackages = "cz.cvut.kbss.inbas.audit.services")
public class TestServiceConfig {
}
