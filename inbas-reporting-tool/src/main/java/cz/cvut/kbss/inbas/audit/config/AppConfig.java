package cz.cvut.kbss.inbas.audit.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by ledvima1 on 8.4.15.
 */
@Configuration
@ComponentScan(basePackages = "cz.cvut.kbss.inbas.audit")
@Import({PersistenceConfig.class, ServiceConfig.class, WebAppConfig.class})
public class AppConfig {
}
