package cz.cvut.kbss.inbas.audit.service.security;

import cz.cvut.kbss.inbas.audit.security.model.UserDetails;
import cz.cvut.kbss.inbas.audit.security.portal.PortalUserDetails;
import cz.cvut.kbss.inbas.audit.util.ConfigParam;
import cz.cvut.kbss.inbas.audit.util.Constants;
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
        LOG.debug("Extending portal session of user {}.", portalUser.getUsername());
        String url = environment.getProperty(ConfigParam.PORTAL_URL.toString());
        assert url != null && !url.isEmpty();
        if (!url.endsWith("/")) {
            url += "/";
        }
        url += SESSION_EXTEND_PATH;
        final HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Authorization", Constants.BASIC_AUTHORIZATION_PREFIX + portalUser.getBasicAuthentication());
        final HttpEntity<Object> entity = new HttpEntity<>(null, requestHeaders);
        LOG.debug("Sending extend request {} to url {}.", entity, url);
        final RestTemplate restTemplate = new RestTemplate();
        final ResponseEntity res = restTemplate.postForObject(URI.create(url), entity, ResponseEntity.class);
        if (res != null && !res.getStatusCode().equals(HttpStatus.OK)) {
            LOG.error("Unable to extend portal session, got status {} and body {}.", res.getStatusCode(),
                    res.getBody());
        }
    }
}
