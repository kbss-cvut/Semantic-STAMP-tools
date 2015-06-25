package cz.cvut.kbss.inbas.audit.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author ledvima1
 */
@Component
public class StartupConfig {

    @PostConstruct
    public void configureApplication() {
        ignoreUnknownJsonFields();
    }

    private void ignoreUnknownJsonFields() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
}
