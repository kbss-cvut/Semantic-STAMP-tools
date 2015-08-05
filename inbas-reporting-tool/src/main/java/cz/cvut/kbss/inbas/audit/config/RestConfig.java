package cz.cvut.kbss.inbas.audit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author ledvima1
 */
@Configuration
@ComponentScan(basePackages = "cz.cvut.kbss.inbas.audit.rest")
public class RestConfig {

    @Bean
    public ObjectMapper objectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper;
    }
}
