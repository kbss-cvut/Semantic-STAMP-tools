package cz.cvut.kbss.inbas.audit.security;

import cz.cvut.kbss.inbas.audit.model.Person;
import cz.cvut.kbss.inbas.audit.rest.dto.model.portal.PortalUser;
import cz.cvut.kbss.inbas.audit.security.model.AuthenticationToken;
import cz.cvut.kbss.inbas.audit.security.model.UserDetails;
import cz.cvut.kbss.inbas.audit.services.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Collections;

@Service("portalAuthenticationProvider")
public class PortalAuthenticationProvider implements AuthenticationProvider {

    private static final String PORTAL_USER_PATH = "api/jsonws/user/get-user-by-email-address";
    private static final String USER_EMAIL_PARAM = "emailAddress";
    private static final String COMPANY_ID_PARAM = "companyId";
    private static final String COMPANY_ID_COOKIE = "COMPANY_ID";

    private static final String PORTAL_URL_CONFIG = "portalUrl";

    private static final Logger LOG = LoggerFactory.getLogger(PortalAuthenticationProvider.class);

    @Autowired
    private Environment environment;

    @Autowired
    private PersonService personService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String username = authentication.getPrincipal().toString();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Authenticating user {} against the portal.", username);
        }
        final Person authenticatedUser = authenticateAgainstPortal(username,
                authentication.getCredentials().toString());
        saveUser(authenticatedUser);
        final UserDetails userDetails = new UserDetails(authenticatedUser,
                Collections.singleton(new SimpleGrantedAuthority(PortalUser.PORTAL_USER_ROLE)));
        userDetails.eraseCredentials();
        final AuthenticationToken token = new AuthenticationToken(userDetails.getAuthorities(), userDetails);
        token.setAuthenticated(true);
        token.setDetails(userDetails);

        final SecurityContext context = new SecurityContextImpl();
        context.setAuthentication(token);
        SecurityContextHolder.setContext(context);
        return token;
    }

    private Person authenticateAgainstPortal(String username, String password) {
        String url = environment.getProperty(PORTAL_URL_CONFIG);
        if (url == null) {
            throw new AuthenticationServiceException("Portal is not available.");
        }
        if (!url.endsWith("/")) {
            url += "/";
        }
        url += PORTAL_USER_PATH + "?" + USER_EMAIL_PARAM + "=" + username;
        String companyId = null;
        final RestTemplate restTemplate = new RestTemplate();
        try {
            final HttpServletRequest request = getCurrentRequest();
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(COMPANY_ID_COOKIE)) {
                    companyId = cookie.getValue();
                }
            }
            assert companyId != null;
            url += "&" + COMPANY_ID_PARAM + "=" + companyId;
            final HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.add("Authorization", "Basic " + encodeBase64(username + ":" + password));
            final HttpEntity<Object> entity = new HttpEntity<>(null, requestHeaders);
            final PortalUser portalUser = restTemplate.exchange(url, HttpMethod.GET, entity, PortalUser.class)
                                                      .getBody();
            final Person person = portalUser.toPerson();
            person.setPassword(password);
            return person;
        } catch (RestClientException e) {
            LOG.error("Unable to get user info from portal at " + url, e);
            throw new AuthenticationServiceException("Unable to authenticate user on portal.", e);
        }
    }

    private HttpServletRequest getCurrentRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }

    private String encodeBase64(String value) {
        return Base64.getEncoder().encodeToString(value.getBytes());
    }

    /**
     * We store the user because it is associated with occurrence reports.
     */
    private void saveUser(Person user) {
        final Person existing = personService.findByUsername(user.getUsername());
        if (existing == null) {
            personService.persist(user);
            return;
        }
        user.encodePassword(passwordEncoder);
        if (!existing.valueEquals(user)) {
            personService.update(user);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
