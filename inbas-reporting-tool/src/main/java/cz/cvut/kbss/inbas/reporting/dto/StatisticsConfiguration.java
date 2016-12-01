package cz.cvut.kbss.inbas.reporting.dto;

import cz.cvut.kbss.inbas.reporting.util.ConfigParam;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents configuration of the statistics UI module.
 */
public class StatisticsConfiguration {

    private Map<String, String> configuration = new HashMap<>(8);

    public Map<String, String> getConfiguration() {
        return configuration;
    }

    public void add(ConfigParam key, String value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
        configuration.put(key.toString(), value);
    }

    public void setConfiguration(Map<String, String> configuration) {
        this.configuration = configuration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StatisticsConfiguration)) return false;

        StatisticsConfiguration that = (StatisticsConfiguration) o;

        return configuration.equals(that.configuration);
    }

    @Override
    public int hashCode() {
        return configuration.hashCode();
    }
}
