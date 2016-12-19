package cz.cvut.kbss.inbas.reporting.service.options;

import cz.cvut.kbss.inbas.reporting.dto.StatisticsConfiguration;
import cz.cvut.kbss.inbas.reporting.rest.dto.model.RawJson;
import cz.cvut.kbss.inbas.reporting.service.ConfigReader;
import cz.cvut.kbss.inbas.reporting.service.data.DataLoader;
import cz.cvut.kbss.inbas.reporting.util.ConfigParam;
import cz.cvut.kbss.inbas.reporting.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@PropertySource("classpath:statistics.properties")
@Service
public class OptionsServiceImpl implements OptionsService {

    private static final Logger LOG = LoggerFactory.getLogger(OptionsServiceImpl.class);

    /**
     * Statistics configuration parameters.
     */
    private static final ConfigParam[] STATISTICS_CONFIGS = {
            ConfigParam.STATISTICS_DASHBOARD,
            ConfigParam.STATISTICS_GENERAL,
            ConfigParam.STATISTICS_EVENT_TYPE,
            ConfigParam.STATISTICS_AUDIT,
            ConfigParam.STATISTICS_SAFETY_ISSUE,
            ConfigParam.STATISTICS_SAG
    };

    @Autowired
    private ConfigReader configReader;

    @Autowired
    @Qualifier("localDataLoader")
    private DataLoader localLoader;

    @Autowired
    @Qualifier("remoteDataLoader")
    private DataLoader remoteLoader;

    @Autowired
    private Environment environment;

    private final Map<String, String> remoteOptions = new HashMap<>();
    private final Map<String, String> localOptions = new HashMap<>();

    @PostConstruct
    private void setupOptions() {
        discoverRemoteQueryFiles();
        discoverLocalOptionsFiles();
    }

    private void discoverRemoteQueryFiles() {
        final URL folderUrl = this.getClass().getClassLoader().getResource(Constants.QUERY_FILES_DIRECTORY);
        if (folderUrl == null) {
            LOG.error("Query directory {} not found.", Constants.QUERY_FILES_DIRECTORY);
            return;
        }
        discoverOptionsFiles(folderUrl, remoteOptions);
    }

    private void discoverOptionsFiles(URL folderUrl, Map<String, String> options) {
        try {
            final File dir = new File(folderUrl.toURI());
            final File[] files = dir.listFiles();
            if (files == null || files.length == 0) {
                LOG.warn("No query files found in directory {}.", dir);
                return;
            }
            for (File f : files) {
                if (f.isDirectory()) {
                    continue;
                }
                String category = f.getName();
                category = category.substring(0, category.lastIndexOf('.'));
                options.put(category, dir.getName() + File.separator + f.getName());
            }
        } catch (URISyntaxException e) {
            LOG.error("Unable to get query files from directory {}.", folderUrl, e);
        }
    }

    private void discoverLocalOptionsFiles() {
        final URL folderUrl = this.getClass().getClassLoader().getResource(Constants.OPTION_FILES_DIRECTORY);
        if (folderUrl == null) {
            LOG.error("Options directory {} not found.", Constants.OPTION_FILES_DIRECTORY);
            return;
        }
        discoverOptionsFiles(folderUrl, localOptions);
    }

    @Override
    public Object getOptions(String type, Map<String, String> parameters) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(parameters);
        if (remoteOptions.containsKey(type)) {
            return loadRemoteData(remoteOptions.get(type), parameters);
        } else if (localOptions.containsKey(type)) {
            return loadLocalData(localOptions.get(type));
        }
        throw new IllegalArgumentException("Unsupported option type " + type);
    }

    private RawJson loadRemoteData(String queryFile, Map<String, String> parameters) {
        final String repositoryUrl = configReader.getConfig(ConfigParam.EVENT_TYPE_REPOSITORY_URL);
        if (repositoryUrl.isEmpty()) {
            throw new IllegalStateException("Missing repository URL configuration.");
        }
        String query = localLoader.loadData(queryFile, Collections.emptyMap());
        query = enhanceQueryWithParameters(query, parameters);
        try {
            query = URLEncoder.encode(query, Constants.UTF_8_ENCODING);
            final String data = remoteLoader.loadData(repositoryUrl, Collections.singletonMap("query", query));
            return new RawJson(data);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Unable to find encoding " + Constants.UTF_8_ENCODING, e);
        }
    }

    private String enhanceQueryWithParameters(String query, Map<String, String> parameters) {
        for (Map.Entry<String, String> e : parameters.entrySet()) {
            query = query.replaceAll("\\?" + e.getKey(), "<" + e.getValue() + ">");
        }
        return query;
    }

    private RawJson loadLocalData(String optionsFile) {
        return new RawJson(localLoader.loadData(optionsFile, Collections.emptyMap()));
    }

    @Override
    public StatisticsConfiguration getStatisticsConfiguration() {
        final StatisticsConfiguration config = new StatisticsConfiguration();
        for (ConfigParam param : STATISTICS_CONFIGS) {
            config.add(param, environment.getProperty(param.toString(), ""));
        }
        return config;
    }
}
