package cz.cvut.kbss.inbas.audit.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author ledvima1
 */
@Configuration
@Import({WebAppConfig.class})
public class AppConfig {
}
