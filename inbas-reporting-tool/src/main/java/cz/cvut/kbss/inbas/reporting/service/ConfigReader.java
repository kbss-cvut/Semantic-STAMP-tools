package cz.cvut.kbss.inbas.reporting.service;

import cz.cvut.kbss.inbas.reporting.util.ConfigParam;
import cz.cvut.kbss.inbas.reporting.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Properties;

@Component
public class ConfigReader {

    @Autowired
    private Environment environment;

    private final Properties defaultConfig = new Properties();

    public String getConfig(ConfigParam param) {
        return getConfig(param, null);
    }

    public String getConfig(ConfigParam param, String defaultValue) {
        if (environment.containsProperty(param.toString())) {
            return environment.getProperty(param.toString());
        }
        return defaultConfig.getProperty(param.toString(), defaultValue);
    }

    @PostConstruct
    public void initDefaultConfiguration() {
        defaultConfig.setProperty(ConfigParam.INDEX_FILE.toString(), Constants.INDEX_FILE_LOCATION);
    }
}
