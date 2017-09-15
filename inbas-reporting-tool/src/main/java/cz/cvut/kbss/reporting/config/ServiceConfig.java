package cz.cvut.kbss.reporting.config;

import cz.cvut.kbss.reporting.service.security.DisabledLoginTracker;
import cz.cvut.kbss.reporting.service.security.LoginTracker;
import cz.cvut.kbss.reporting.service.security.RuntimeBasedLoginTracker;
import cz.cvut.kbss.reporting.util.ConfigParam;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan(basePackages = "cz.cvut.kbss.reporting.service")
public class ServiceConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * Creates login tracker based on configuration - disabled or enabled.
     */
    @Bean
    public LoginTracker loginTracker(Environment environment) {
        final String track = environment
                .getProperty(ConfigParam.RESTRICT_LOGIN_ATTEMPTS.toString(), Boolean.FALSE.toString());
        if (Boolean.valueOf(track)) {
            return new RuntimeBasedLoginTracker();
        } else {
            return new DisabledLoginTracker();
        }
    }
}
