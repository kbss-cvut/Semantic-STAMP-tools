package cz.cvut.kbss.inbas.reporting.service.options;

import cz.cvut.kbss.inbas.reporting.model.arms.AccidentOutcome;
import cz.cvut.kbss.inbas.reporting.model.arms.BarrierEffectiveness;
import cz.cvut.kbss.inbas.reporting.rest.dto.model.RawJson;
import cz.cvut.kbss.inbas.reporting.service.ConfigReader;
import cz.cvut.kbss.inbas.reporting.service.data.DataLoader;
import cz.cvut.kbss.inbas.reporting.util.ConfigParam;
import cz.cvut.kbss.inbas.reporting.util.Constants;
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

@Service
public class OptionsServiceImpl implements OptionsService {

    private static final Logger LOG = LoggerFactory.getLogger(OptionsServiceImpl.class);

    @Autowired
    private ConfigReader configReader;

    @Autowired
    @Qualifier("localDataLoader")
    private DataLoader localLoader;

    @Autowired
    @Qualifier("remoteDataLoader")
    private DataLoader remoteLoader;

    private final Map<String, String> optionsCategories = new HashMap<>();

    @PostConstruct
    private void discoverQueryFiles() {
        final URL folderUrl = this.getClass().getClassLoader().getResource(Constants.QUERY_FILES_DIRECTORY);
        if (folderUrl == null) {
            LOG.error("Query directory {} not found.", Constants.QUERY_FILES_DIRECTORY);
            return;
        }
        try {
            final File dir = new File(folderUrl.toURI());
            final File[] queryFiles = dir.listFiles();
            if (queryFiles == null || queryFiles.length == 0) {
                LOG.warn("No query files found in directory {}.", dir);
                return;
            }
            for (File f : queryFiles) {
                String category = f.getName();
                category = category.substring(0, category.lastIndexOf('.'));
                optionsCategories.put(category, Constants.QUERY_FILES_DIRECTORY + File.separator + f.getName());
            }
        } catch (URISyntaxException e) {
            LOG.error("Unable to get query files from directory {}.", Constants.QUERY_FILES_DIRECTORY, e);
        }
    }

    @Override
    public Object getOptions(String type) {
        Objects.requireNonNull(type);
        if (optionsCategories.containsKey(type)) {
            return loadRemoteData(optionsCategories.get(type));
        } else {
            // This is temporary. Once we get rid of the json files, we won't need this else branch
            switch (type) {
                case "location":
                    return new RawJson(localLoader.loadData("locations.json", Collections.emptyMap()));
                case "operator":
                    return new RawJson(localLoader.loadData("operators.json", Collections.emptyMap()));
                case "accidentOutcome":
                    return AccidentOutcome.toPairs();
                case "barrierEffectiveness":
                    return BarrierEffectiveness.values();
                default:
                    break;
            }
        }
        throw new IllegalArgumentException("Unsupported option type " + type);
    }

    private RawJson loadRemoteData(String queryFile) {
        final String repositoryUrl = configReader.getConfig(ConfigParam.EVENT_TYPE_REPOSITORY_URL);
        if (repositoryUrl == null) {
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
}
