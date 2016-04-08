package cz.cvut.kbss.inbas.audit.security;

import cz.cvut.kbss.inbas.audit.config.RestConfig;
import cz.cvut.kbss.inbas.audit.environment.config.MockSesamePersistence;
import cz.cvut.kbss.inbas.audit.environment.config.TestSecurityConfig;
import cz.cvut.kbss.inbas.audit.security.portal.PortalEndpoint;
import cz.cvut.kbss.inbas.audit.security.portal.PortalEndpointType;
import cz.cvut.kbss.inbas.audit.service.BaseServiceTestRunner;
import cz.cvut.kbss.inbas.audit.util.ConfigParam;
import cz.cvut.kbss.inbas.audit.util.Constants;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withUnauthorizedRequest;

@ContextConfiguration(classes = {TestSecurityConfig.class, RestConfig.class, MockSesamePersistence.class})
public class PortalAuthenticationProviderTest extends BaseServiceTestRunner {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Environment environment;

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
        String expectedUrl = environment.getProperty(ConfigParam.PORTAL_URL.toString());
        final PortalEndpoint endpoint = PortalEndpoint.createEndpoint(PortalEndpointType.EMAIL_ADDRESS);
        final String username = "john117";
        final String companyId = "unsc";
        expectedUrl += "/" + endpoint.constructPath(username, companyId);
        mockServer.expect(requestTo(expectedUrl)).andExpect(method(HttpMethod.GET))
                  .andRespond(withUnauthorizedRequest());
        setCompanyIdInCurrentRequest(companyId);

        try {
            provider.authenticate(createAuthentication(username));
        } finally {
            mockServer.verify();
        }
    }

    private Authentication createAuthentication(String username) {
        return new UsernamePasswordAuthenticationToken(username, "");
    }

    private void setCompanyIdInCurrentRequest(String companyId) {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie(Constants.COMPANY_ID_COOKIE, companyId));
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }
}