package cz.cvut.kbss.reporting.service.options;

import cz.cvut.kbss.reporting.persistence.dao.ExistingDataDao;
import cz.cvut.kbss.reporting.rest.dto.model.RawJson;
import cz.cvut.kbss.reporting.service.ConfigReader;
import cz.cvut.kbss.reporting.service.data.DataLoader;
import cz.cvut.kbss.reporting.util.ConfigParam;
import cz.cvut.kbss.reporting.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import java.util.function.Supplier;

@Service
public class OptionsServiceImpl implements OptionsService {

    private static final Logger LOG = LoggerFactory.getLogger(OptionsServiceImpl.class);

    private ConfigReader configReader;

    private DataLoader localLoader;

    private DataLoader remoteLoader;

    private ExistingDataDao existingDataDao;

    private final Map<String, String> remoteOptions = new HashMap<>();
    private final Map<String, String> localOptions = new HashMap<>();
    private final Map<String, Supplier> knownOptions = new HashMap<>();

    @Autowired
    public OptionsServiceImpl(ConfigReader configReader, @Qualifier("localDataLoader") DataLoader localLoader,
                              @Qualifier("remoteDataLoader") DataLoader remoteLoader, ExistingDataDao dataDao) {
        this.configReader = configReader;
        this.localLoader = localLoader;
        this.remoteLoader = remoteLoader;
        this.existingDataDao = dataDao;
    }

    @PostConstruct
    private void setupOptions() {
        discoverRemoteQueryFiles();
        discoverLocalOptionsFiles();
        registerKnownOptionTypes();
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

    private void registerKnownOptionTypes() {
        knownOptions
                .put(OptionType.EXISTING_OCCURRENCE_CATEGORIES.getName(), existingDataDao::getUsedOccurrenceCategories);
        knownOptions.put(OptionType.EXISTING_EVENT_TYPES.getName(), existingDataDao::getUsedEventTypes);
    }

    @Override
    public Object getOptions(String type) {
        Objects.requireNonNull(type);
        if (remoteOptions.containsKey(type)) {
            return loadRemoteData(remoteOptions.get(type));
        } else if (localOptions.containsKey(type)) {
            return loadLocalData(localOptions.get(type));
        } else if (knownOptions.containsKey(type)) {
            return knownOptions.get(type).get();
        }
        throw new IllegalArgumentException("Unsupported option type " + type);
    }

    private RawJson loadRemoteData(String queryFile) {
        final String repositoryUrl = configReader.getConfig(ConfigParam.EVENT_TYPE_REPOSITORY_URL);
        if (repositoryUrl.isEmpty()) {
            throw new IllegalStateException("Missing repository URL configuration.");
        }
        String query = localLoader.loadData(queryFile, Collections.emptyMap());
        try {
            query = URLEncoder.encode(query, Constants.UTF_8_ENCODING);
            final String data = remoteLoader.loadData(repositoryUrl, Collections.singletonMap("query", query));
            return new RawJson(data);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Unable to find encoding " + Constants.UTF_8_ENCODING, e);
        }
    }

    private RawJson loadLocalData(String optionsFile) {
        return new RawJson(localLoader.loadData(optionsFile, Collections.emptyMap()));
    }
}
