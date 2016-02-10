package cz.cvut.kbss.inbas.audit.service.data;

import cz.cvut.kbss.inbas.audit.exception.WebServiceIntegrationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Map;

@Service("remoteDataLoader")
public class RemoteDataLoader implements DataLoader {

    private static final Logger LOG = LoggerFactory.getLogger(RemoteDataLoader.class);

    /**
     * {@inheritDoc}
     *
     * @param remoteUrl   Remote data source (URL)
     * @param queryParams Query parameters
     * @return Loaded data
     */
    public String loadData(String remoteUrl, Map<String, String> queryParams) {
        final URI urlWithQuery = prepareUri(remoteUrl, queryParams);
        final HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/ld+json");
        final HttpEntity<Object> entity = new HttpEntity<>(null, headers);
        final RestTemplate restTemplate = new RestTemplate();
        if (LOG.isTraceEnabled()) {
            LOG.trace("Getting remote data using {}", urlWithQuery.toString());
        }
        try {
            final ResponseEntity<String> result = restTemplate.exchange(urlWithQuery, HttpMethod.GET, entity,
                    String.class);
            return result.getBody();
        } catch (Exception e) {
            LOG.error("Error when requesting remote data, url: " + urlWithQuery.toString(), e);
            throw new WebServiceIntegrationException("Unable to fetch remote data.", e);
        }
    }

    private URI prepareUri(String remoteUrl, Map<String, String> queryParams) {
        final StringBuilder sb = new StringBuilder(remoteUrl);
        int index = 0;
        for (Map.Entry<String, String> e : queryParams.entrySet()) {
            sb.append(index == 0 ? '?' : '&');
            sb.append(e.getKey()).append('=').append(e.getValue());
            index++;
        }
        return URI.create(sb.toString());
    }
}
