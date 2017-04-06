package cz.cvut.kbss.reporting.service;

import cz.cvut.kbss.reporting.rest.dto.model.RawJson;
import cz.cvut.kbss.reporting.service.data.DataLoader;
import cz.cvut.kbss.reporting.util.ConfigParam;
import cz.cvut.kbss.reporting.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class SPARQLService {

    @Autowired
    @Qualifier("remoteDataLoader")
    private DataLoader remoteLoader;

    @Autowired
    @Qualifier("localDataLoader")
    private DataLoader localLoader;

    @Autowired
    private ConfigReader configReader;

    /**
     * Executes given named SPARQL query
     *
     * @param queryName of the SPARQL query
     * @return a {@link RawJson} object containing JSON-formatted SPARQL Select result.
     * @throws IllegalArgumentException When the specified queryName is not known
     */
    public RawJson getSPARQLSelectResult(String queryName) {
        final String repositoryUrl = configReader.getConfig(ConfigParam.REPOSITORY_URL);
        if (repositoryUrl.isEmpty()) {
            throw new IllegalStateException("Missing repository URL configuration.");
        }
        String query = localLoader.loadData("query/"+queryName+".sparql", Collections.emptyMap());
        try {
            query = URLEncoder.encode(query, Constants.UTF_8_ENCODING);
            final Map<String, String> params = new HashMap<>();
            params.put("query", query);
            params.put(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
            final String data = remoteLoader.loadData(repositoryUrl, params);
            return new RawJson(data);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Unable to find encoding " + Constants.UTF_8_ENCODING, e);
        }
    }
}
