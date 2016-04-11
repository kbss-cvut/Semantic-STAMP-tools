package cz.cvut.kbss.inbas.reporting.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.kbss.inbas.audit.config.RestConfig;
import cz.cvut.kbss.inbas.audit.environment.config.MockSesamePersistence;
import cz.cvut.kbss.inbas.reporting.environment.config.PropertyMockingApplicationContextInitializer;
import cz.cvut.kbss.inbas.audit.environment.config.TestSecurityConfig;
import cz.cvut.kbss.inbas.audit.model.Person;
import cz.cvut.kbss.inbas.audit.rest.dto.model.PortalUser;
import cz.cvut.kbss.inbas.audit.security.portal.PortalEndpoint;
import cz.cvut.kbss.inbas.audit.security.portal.PortalEndpointType;
import cz.cvut.kbss.inbas.reporting.security.portal.PortalUserDetails;
import cz.cvut.kbss.inbas.audit.service.BaseServiceTestRunner;
import cz.cvut.kbss.inbas.audit.util.ConfigParam;
import cz.cvut.kbss.inbas.audit.util.Constants;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import java.net.URI;

import static org.junit.Assert.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withUnauthorizedRequest;

@ContextConfiguration(classes = {TestSecurityConfig.class,
        RestConfig.class,
        MockSesamePersistence.class}, initializers = PropertyMockingApplicationContextInitializer.class)
public class PortalAuthenticationProviderTest extends BaseServiceTestRunner {

    private static final String USERNAME = "masterchief";
    private static final String COMPANY_ID = "117";
    private static final String PASSWORD = "cortanaisthebest";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Environment environment;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    @Qualifier("portalAuthenticationProvider")
    AuthenticationProvider provider;

    private MockRestServiceServer mockServer;

    @Before
    public void setUp() throws Exception {
        this.mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test(expected = AuthenticationServiceException.class)
    public void unsuccessfulLoginThrowsAuthenticationServiceException() {
        String expectedUrl = getExpectedUrl();
        mockServer.expect(requestTo(expectedUrl)).andExpect(method(HttpMethod.GET))
                  .andRespond(withUnauthorizedRequest());
        setCompanyIdInCurrentRequest(COMPANY_ID);

        try {
            provider.authenticate(createAuthentication(USERNAME));
        } finally {
            mockServer.verify();
        }
    }

    private String getExpectedUrl() {
        String expectedUrl = environment.getProperty(ConfigParam.PORTAL_URL.toString());
        final PortalEndpoint endpoint = PortalEndpoint.createEndpoint(PortalEndpointType.EMAIL_ADDRESS);
        expectedUrl += "/" + endpoint.constructPath(USERNAME, COMPANY_ID);
        return expectedUrl;
    }

    private Authentication createAuthentication(String username) {
        return new UsernamePasswordAuthenticationToken(username, PASSWORD);
    }

    private void setCompanyIdInCurrentRequest(String companyId) {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie(Constants.COMPANY_ID_COOKIE, companyId));
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    public void successfulLoginCreatesAuthenticationObjectWithUserData() throws Exception {
        final PortalUser userData = getPortalUser();
        mockServer.expect(requestTo(getExpectedUrl())).andExpect(method(HttpMethod.GET))
                  .andRespond(withSuccess(objectMapper.writeValueAsBytes(userData),
                          MediaType.APPLICATION_JSON));
        setCompanyIdInCurrentRequest(COMPANY_ID);

        final Authentication auth = provider.authenticate(createAuthentication(USERNAME));
        assertTrue(auth.isAuthenticated());
        final PortalUserDetails userDetails = (PortalUserDetails) auth.getDetails();
        assertEquals(userData.getEmailAddress(), userDetails.getUsername());
    }

    private PortalUser getPortalUser() {
        final PortalUser userData = new PortalUser();
        userData.setFirstName("John");
        userData.setLastName("Spartan");
        userData.setEmailAddress("masterchief@unsc.org");
        return userData;
    }

    @Test
    public void successfulLoginPersistsUserWhenHeDoesNotExistYet() throws Exception {
        final PortalUser userData = getPortalUser();
        assertNull(personDao.findByUsername(userData.getEmailAddress()));
        mockServer.expect(requestTo(getExpectedUrl())).andExpect(method(HttpMethod.GET))
                  .andRespond(withSuccess(objectMapper.writeValueAsBytes(userData),
                          MediaType.APPLICATION_JSON));
        setCompanyIdInCurrentRequest(COMPANY_ID);

        final Authentication auth = provider.authenticate(createAuthentication(USERNAME));
        assertTrue(auth.isAuthenticated());
        final Person res = personDao.findByUsername(userData.getEmailAddress());
        assertNotNull(res);
        assertEquals(userData.getFirstName(), res.getFirstName());
        assertEquals(userData.getLastName(), res.getLastName());
        assertEquals(userData.getEmailAddress(), res.getUsername());
        assertTrue(encoder.matches(PASSWORD, res.getPassword()));
    }

    @Test
    public void successfulLoginReusesExistingUserInstance() throws Exception {
        final PortalUser userData = getPortalUser();
        final Person p = new Person();
        p.setFirstName(userData.getFirstName());
        p.setLastName(userData.getLastName());
        p.setUsername(userData.getEmailAddress());
        p.setPassword(PASSWORD);
        p.encodePassword(encoder);
        p.setUri(URI.create("http://krizik.felk.cvut.cz/differentUri"));
        personDao.persist(p);
        mockServer.expect(requestTo(getExpectedUrl())).andExpect(method(HttpMethod.GET))
                  .andRespond(withSuccess(objectMapper.writeValueAsBytes(userData),
                          MediaType.APPLICATION_JSON));
        setCompanyIdInCurrentRequest(COMPANY_ID);

        provider.authenticate(createAuthentication(USERNAME));
        final Person res = personDao.findByUsername(userData.getEmailAddress());
        // This means it didn't persist the new person got from portal, because
        // it would have a different, generated, uri
        assertEquals(p.getUri(), res.getUri());
    }

    @Test(expected = AuthenticationServiceException.class)
    public void throwsAuthenticationExceptionWhenPortalUrlIsNotAvailable() {
        final MockEnvironment me = (MockEnvironment) environment;
        me.setProperty(ConfigParam.PORTAL_URL.toString(), "");
        setCompanyIdInCurrentRequest(COMPANY_ID);

        provider.authenticate(createAuthentication(USERNAME));
    }

    @Test(expected = AuthenticationServiceException.class)
    public void throwsAuthenticationExceptionWhenApplicationIsNotRunningOnPortal() {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        // No company id -> not running on portal
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        provider.authenticate(createAuthentication(USERNAME));
    }
}