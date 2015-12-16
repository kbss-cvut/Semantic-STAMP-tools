package cz.cvut.kbss.inbas.audit.service.options;

import cz.cvut.kbss.inbas.audit.exception.ApplicationInitializationException;
import cz.cvut.kbss.inbas.audit.exception.WebServiceIntegrationException;
import cz.cvut.kbss.inbas.audit.rest.dto.model.RawJson;
import cz.cvut.kbss.inbas.audit.service.ConfigReader;
import cz.cvut.kbss.inbas.audit.util.ConfigParam;
import cz.cvut.kbss.inbas.audit.util.Constants;
import cz.cvut.kbss.inbas.audit.util.EventTypeJsonLdTransformer;
import cz.cvut.kbss.inbas.audit.util.FileDataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;

@Service("eventType")
class EventTypeOptionsService implements OptionsService {

    private static final Logger LOG = LoggerFactory.getLogger(EventTypeOptionsService.class);

    @Autowired
    private ConfigReader configReader;

    private final EventTypeJsonLdTransformer eventTypeTransformer = new EventTypeJsonLdTransformer();

    private String encodedQuery;

    @Override
    public RawJson getOptions() {
        final String repositoryUrl = configReader.getConfig(ConfigParam.EVENT_TYPE_REPOSITORY_URL);
        if (repositoryUrl == null) {
            throw new IllegalStateException("Missing event type repository URL configuration.");
        }
        final URI urlWithQuery = URI.create(repositoryUrl + "?query=" + encodedQuery);
        final HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/ld+json");
        final HttpEntity<Object> entity = new HttpEntity<>(null, headers);
        final RestTemplate restTemplate = new RestTemplate();
        if (LOG.isTraceEnabled()) {
            LOG.trace("Getting event types using {}", urlWithQuery.toString());
        }
        try {
            final ResponseEntity<String> result = restTemplate.exchange(urlWithQuery, HttpMethod.GET, entity,
                    String.class);
            return new RawJson(eventTypeTransformer.transform(result.getBody()));
        } catch (Exception e) {
            LOG.error("Error when requesting event types, url: " + urlWithQuery.toString(), e);
            throw new WebServiceIntegrationException("Unable to fetch event types.", e);
        }
    }

    @PostConstruct
    public void setUp() {
        final String queryString = new FileDataLoader().load(Constants.EVENT_TYPE_QUERY_FILE);
        try {
            this.encodedQuery = URLEncoder.encode(queryString, Constants.UTF_8_ENCODING);
        } catch (UnsupportedEncodingException e) {
            LOG.error("Unable to find encoding " + Constants.UTF_8_ENCODING, e);
            throw new ApplicationInitializationException(e);
        }
    }
}
