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
     * @param queryFile of the SPARQL query
     * @param bindings variable bindings to be converted to VALUES block
     * @param repositoryUrl the repo for query execution
     * @return a {@link RawJson} object containing JSON-formatted SPARQL Select result.
     * @throws IllegalArgumentException When the specified queryName is not known
     */
    public RawJson getSPARQLSelectResult(final String queryFile, final Map<String,String> bindings, final String repositoryUrl) {
        return getSPARQLResult(queryFile, bindings, repositoryUrl, MediaType.APPLICATION_JSON_VALUE);
    }

    public RawJson getSPARQLSelectResult(final String queryFile, final Map<String,String> bindings, final String repositoryUrl, final String mediaType ) {
        return getSPARQLResult(queryFile, bindings, repositoryUrl, mediaType);
    }

    private RawJson getSPARQLResult(final String queryFile, final Map<String,String> bindings, final String repositoryUrl, final String mediaType) {
        if (repositoryUrl.isEmpty()) {
            throw new IllegalStateException("Missing repository URL configuration.");
        }
        String query = localLoader.loadData(queryFile, Collections.emptyMap());

        query = replaceParameterVariables(query);
        if (!bindings.isEmpty()) {
            query = query + " VALUES (";
            for (final String key : bindings.keySet()) {
                query = query + " ?" + key;
            }
            query = query + " )";

            query = query + " { (";
            for (final String key : bindings.keySet()) {
                query = query + " <" + bindings.get(key) + ">";
            }
            query = query + ") }";
        }

        try {
            query = URLEncoder.encode(query, Constants.UTF_8_ENCODING);
            final Map<String, String> params = new HashMap<>();
            params.put("query", query);
            params.put(HttpHeaders.ACCEPT, mediaType);
            final String data = remoteLoader.loadData(repositoryUrl, params);
            return new RawJson(data);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Unable to find encoding " + Constants.UTF_8_ENCODING, e);
        }
    }

    protected String replaceParameterVariables(String query){
        String var =  "\\?" + ConfigParam.EVENT_TYPE_REPOSITORY_URL.toString();
        String val = "<" + configReader.getConfig(ConfigParam.EVENT_TYPE_REPOSITORY_URL) + ">";
        return query.replaceAll(var, val);
    }
}
