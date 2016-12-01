package cz.cvut.kbss.inbas.reporting.service.options;

import cz.cvut.kbss.inbas.reporting.dto.StatisticsConfiguration;
import cz.cvut.kbss.inbas.reporting.rest.dto.model.RawJson;

import java.util.Map;

public interface OptionsService {

    /**
     * Gets options of the specified type.
     *
     * @param type       Options type
     * @param parameters Parameters for the option retrieval. For example, when options are loaded using a query from a
     *                   remote repository, the parameters are used to parameterize the query
     * @return Object representing the options. This will most often be a {@link RawJson} containing options retrieved
     * from a remote repository. Otherwise, it can be an array/list of options.
     * @throws IllegalArgumentException When the specified option type is not supported
     */
    Object getOptions(String type, Map<String, String> parameters);

    /**
     * Gets configuration of the statistics UI module.
     *
     * @return Configuration object
     */
    StatisticsConfiguration getStatisticsConfiguration();
}
