package cz.cvut.kbss.inbas.audit.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ledvima1 on 20.3.15.
 */
@Configuration
@ComponentScan(basePackages = "cz.cvut.kbss.inbas.audit.persistence")
public class PersistenceConfig {
}
