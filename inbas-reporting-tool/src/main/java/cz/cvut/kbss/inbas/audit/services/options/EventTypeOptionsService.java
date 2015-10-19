package cz.cvut.kbss.inbas.audit.services.options;

import cz.cvut.kbss.inbas.audit.rest.dto.model.RawJson;
import cz.cvut.kbss.inbas.audit.services.ConfigReader;
import cz.cvut.kbss.inbas.audit.util.ConfigParam;
import cz.cvut.kbss.inbas.audit.util.EventTypeJsonLdTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;

@Service("eventType")
class EventTypeOptionsService implements OptionsService {

    private static final Logger LOG = LoggerFactory.getLogger(EventTypeOptionsService.class);

    /**
     * Query used to get event types + additional information from repository.
     */
    private static final String ENCODED_QUERY = encodeQuery();

    private static final String ENCODING = "UTF-8";

    private static String encodeQuery() {
        try {
            return URLEncoder.encode("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                    "PREFIX e: <http://onto.fel.cvut.cz/ontologies/eccairs-1.3.0.8/>\n" +
                    "PREFIX m: <http://onto.fel.cvut.cz/ontologies/eccairs/model/>\n" +
                    "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX j.0: <http://onto.fel.cvut.cz/ontologies/eccairs-1.3.0.8/>\n" +
                    "\n" +
                    "CONSTRUCT {\n" +
                    "  ?term rdfs:label ?label ;\n" +
                    "     a ?type ;\n" +
                    "     rdfs:comment ?comment ;\n" +
                    "} WHERE {\n" +
                    "  {\n" +
                    "      # all events\n" +
                    "      # j.0:A-390 j.0:hasChild+ ?c.\n" +
                    "      # ANS events\n" +
                    "      e:A-390 e:hasChild+ ?term.\n" +
                    "    BIND(m:event-type as ?type)\n" +
                    "  } UNION {\n" +
                    "      # all events\n" +
                    "      # j.0:A-390 j.0:hasChild+ ?c.\n" +
                    "      # Aircraft operation DF\n" +
                    "      e:V-24-1-31-26-27-488-10000000 e:hasChild+ ?term.\n" +
                    "    BIND(m:descriptive-factor as ?type)\n" +
                    "  } UNION {\n" +
                    "      # all events\n" +
                    "      # j.0:A-390 j.0:hasChild+ ?c.\n" +
                    "      # ATM DF\n" +
                    "      e:V-24-1-31-26-27-488-20000000 e:hasChild+ ?term.\n" +
                    "    BIND(m:descriptive-factor as ?type)\n" +
                    "  } UNION {\n" +
                    "      # all events\n" +
                    "      # j.0:A-390 j.0:hasChild+ ?c.\n" +
                    "      # METEO DF\n" +
                    "      e:V-24-1-31-26-27-488-50000000 e:hasChild+ ?term.\n" +
                    "    BIND(m:descriptive-factor as ?type)\n" +
                    "  }\n" +
                    "  ?term rdfs:label ?label.\n" +
                    "  ?term j.0:hasExplanation ?comment.\n" +
                    "}", ENCODING);
        } catch (UnsupportedEncodingException e) {
            LOG.error("Unable to find encoding " + ENCODING, e);
        }
        return null;
    }

    @Autowired
    private ConfigReader configReader;

    private final EventTypeJsonLdTransformer eventTypeTransformer = new EventTypeJsonLdTransformer();

    @Override
    public RawJson getOptions() {
        final String repositoryUrl = configReader.getConfig(ConfigParam.EVENT_TYPE_REPOSITORY_URL);
        if (repositoryUrl == null) {
            throw new IllegalStateException("Missing event type repository URL configuration.");
        }
        final URI urlWithQuery = URI.create(repositoryUrl + "?query=" + ENCODED_QUERY);
        final HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/ld+json");
        final HttpEntity<Object> entity = new HttpEntity<>(null, headers);
        final RestTemplate restTemplate = new RestTemplate();
        try {
            final ResponseEntity<String> result = restTemplate.exchange(urlWithQuery, HttpMethod.GET, entity,
                    String.class);
            return new RawJson(eventTypeTransformer.transform(result.getBody()));
        } catch (Exception e) {
            LOG.error("Error when requesting event types.", e);
            throw e;
        }
    }
}
