package cz.cvut.kbss.inbas.audit.service.options;

import cz.cvut.kbss.inbas.audit.model.reports.OccurrenceSeverity;
import cz.cvut.kbss.inbas.audit.model.reports.incursions.LowVisibilityProcedure;
import cz.cvut.kbss.inbas.audit.rest.dto.model.RawJson;
import cz.cvut.kbss.inbas.audit.service.ConfigReader;
import cz.cvut.kbss.inbas.audit.service.data.DataLoader;
import cz.cvut.kbss.inbas.audit.util.ConfigParam;
import cz.cvut.kbss.inbas.audit.util.Constants;
import cz.cvut.kbss.inbas.audit.util.EventTypeJsonLdTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collections;

@Service
public class OptionsService {

    @Autowired
    private ConfigReader configReader;

    @Autowired
    @Qualifier("localDataLoader")
    private DataLoader localLoader;

    @Autowired
    @Qualifier("remoteDataLoader")
    private DataLoader remoteLoader;

    private final EventTypeJsonLdTransformer eventTypeTransformer = new EventTypeJsonLdTransformer();

    public Object getOptions(String type) {
        switch (type) {
            case "lvp":
                return Arrays.asList(LowVisibilityProcedure.values());
            case "occurrenceSeverity":
                return Arrays.asList(OccurrenceSeverity.values());
            case "location":
                return new RawJson(localLoader.loadData("locations.json", Collections.emptyMap()));
            case "operator":
                return new RawJson(localLoader.loadData("operators.json", Collections.emptyMap()));
            case "eventType":
                return loadEventTypes();
            default:
                throw new IllegalArgumentException("Unsupported option type " + type);
        }
    }

    private RawJson loadEventTypes() {
        final String repositoryUrl = configReader.getConfig(ConfigParam.EVENT_TYPE_REPOSITORY_URL);
        if (repositoryUrl == null) {
            throw new IllegalStateException("Missing repository URL configuration.");
        }
        String query = localLoader.loadData(Constants.EVENT_TYPE_QUERY_FILE, Collections.emptyMap());
        try {
            query = URLEncoder.encode(query, Constants.UTF_8_ENCODING);
            final String data = remoteLoader.loadData(repositoryUrl, Collections.singletonMap("query", query));
            return new RawJson(eventTypeTransformer.transform(data));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Unable to find encoding " + Constants.UTF_8_ENCODING, e);
        }
    }
}
