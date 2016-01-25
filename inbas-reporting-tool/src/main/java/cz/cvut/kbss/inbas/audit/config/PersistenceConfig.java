package cz.cvut.kbss.inbas.audit.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "cz.cvut.kbss.inbas.audit.persistence")
public class PersistenceConfig {
}
