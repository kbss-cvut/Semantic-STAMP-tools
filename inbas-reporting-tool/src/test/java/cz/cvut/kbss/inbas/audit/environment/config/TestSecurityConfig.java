package cz.cvut.kbss.inbas.audit.environment.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "cz.cvut.kbss.inbas.audit.security")
public class TestSecurityConfig {
}
