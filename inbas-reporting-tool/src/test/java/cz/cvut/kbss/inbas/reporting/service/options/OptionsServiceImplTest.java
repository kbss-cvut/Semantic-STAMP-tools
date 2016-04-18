package cz.cvut.kbss.inbas.reporting.service.options;

import cz.cvut.kbss.inbas.reporting.environment.config.PropertyMockingApplicationContextInitializer;
import cz.cvut.kbss.inbas.reporting.rest.dto.model.RawJson;
import cz.cvut.kbss.inbas.reporting.service.BaseServiceTestRunner;
import cz.cvut.kbss.inbas.reporting.util.ConfigParam;
import cz.cvut.kbss.inbas.reporting.util.Constants;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@ContextConfiguration(initializers = PropertyMockingApplicationContextInitializer.class)
public class OptionsServiceImplTest extends BaseServiceTestRunner {

    private static final String DATA = "[{\"a\": 1}, {\"a\": 2}]";
    private static final String URL = "http://localhost/openrdf-sesame";

    @Autowired
    private OptionsService optionsService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Environment environment;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private MockRestServiceServer mockServer;

    @Before
    public void setUp() {
        this.mockServer = MockRestServiceServer.createServer(restTemplate);
        final MockEnvironment mockEnv = (MockEnvironment) environment;
        mockEnv.setProperty(ConfigParam.EVENT_TYPE_REPOSITORY_URL.toString(), URL);
    }

    @Test
    public void getEventTypesLoadsEventsTypesFromRemoteLocation() throws Exception {
        mockServer.expect(requestTo(expectedUrl())).andExpect(method(HttpMethod.GET))
                  .andRespond(withSuccess(DATA,
                          MediaType.APPLICATION_JSON));
        final Object res = optionsService.getOptions("eventType");
        assertTrue(res instanceof RawJson);
        assertEquals(DATA, ((RawJson) res).getValue());
    }

    private String expectedUrl() throws Exception {
        final String sparql = cz.cvut.kbss.inbas.reporting.environment.util.Environment
                .loadData("query/eventType.sparql", String.class);
        return URL + "?query=" + URLEncoder.encode(sparql, Constants.UTF_8_ENCODING);
    }

    @Test
    public void unsupportedOptionThrowsIllegalArgument() {
        final String unknownOptionType = "unknownOptionType";
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Unsupported option type " + unknownOptionType);
        optionsService.getOptions(unknownOptionType);
    }
}