package cz.cvut.kbss.inbas.reporting.service;

import cz.cvut.kbss.inbas.reporting.util.ConfigParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ConfigReader {

    @Autowired
    private Environment environment;

    public String getConfig(ConfigParam param) {
        return environment.getProperty(param.toString());
    }

    public String getConfig(ConfigParam param, String defaultValue) {
        return environment.getProperty(param.toString(), defaultValue);
    }
}
