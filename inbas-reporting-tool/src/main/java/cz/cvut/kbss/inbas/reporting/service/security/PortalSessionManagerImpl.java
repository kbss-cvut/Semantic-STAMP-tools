package cz.cvut.kbss.inbas.reporting.service.security;

import cz.cvut.kbss.inbas.reporting.security.model.UserDetails;
import cz.cvut.kbss.inbas.reporting.security.portal.PortalUserDetails;
import cz.cvut.kbss.inbas.reporting.util.ConfigParam;
import cz.cvut.kbss.inbas.reporting.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class PortalSessionManagerImpl implements PortalSessionManager {

    private static final String SESSION_EXTEND_PATH = "c/portal/extend_session";

    private static final Logger LOG = LoggerFactory.getLogger(PortalSessionManagerImpl.class);

    @Autowired
    private Environment environment;

    @Autowired
    private SecurityUtils securityUtils;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void keepPortalSessionAlive() {
        final UserDetails userDetails = securityUtils.getCurrentUserDetails();
        if (!portalUserAuthenticated(userDetails)) {
            return;
        }
        final PortalUserDetails portalUser = (PortalUserDetails) userDetails;
        extendSession(portalUser);
    }

    private boolean portalUserAuthenticated(UserDetails userDetails) {
        return userDetails instanceof PortalUserDetails;
    }

    private void extendSession(PortalUserDetails portalUser) {
        if (LOG.isTraceEnabled()) {
            LOG.trace("Extending portal session of user {}.", portalUser.getUsername());
        }
        String url = environment.getProperty(ConfigParam.PORTAL_URL.toString());
        assert url != null && !url.isEmpty();
        if (!url.endsWith("/")) {
            url += "/";
        }
        url += SESSION_EXTEND_PATH;
        final HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Authorization", Constants.BASIC_AUTHORIZATION_PREFIX + portalUser.getBasicAuthentication());
        final HttpEntity<Object> entity = new HttpEntity<>(null, requestHeaders);
        final ResponseEntity res = restTemplate.postForObject(URI.create(url), entity, ResponseEntity.class);
        // For some reason, the response is null, event though the request gets through and according to testing in Postman,
        // the portal responds with 200 OK. To prevent NPX, check for res being null here. If it is, just silently ignore it.
        if (res != null && !res.getStatusCode().equals(HttpStatus.OK)) {
            LOG.error("Unable to extend portal session, got status {} and body {}.", res.getStatusCode(),
                    res.getBody());
        }
    }
}
