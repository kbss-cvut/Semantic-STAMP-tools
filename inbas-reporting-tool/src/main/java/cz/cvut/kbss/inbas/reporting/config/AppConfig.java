package cz.cvut.kbss.inbas.reporting.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({WebAppConfig.class, PersistenceConfig.class, ServiceConfig.class})
public class AppConfig {
}
