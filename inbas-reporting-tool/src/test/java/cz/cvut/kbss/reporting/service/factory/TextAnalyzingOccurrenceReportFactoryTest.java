package cz.cvut.kbss.reporting.service.factory;

import cz.cvut.kbss.reporting.environment.config.PropertyMockingApplicationContextInitializer;
import cz.cvut.kbss.reporting.exception.WebServiceIntegrationException;
import cz.cvut.kbss.reporting.model.InitialReport;
import cz.cvut.kbss.reporting.model.OccurrenceReport;
import cz.cvut.kbss.reporting.service.BaseServiceTestRunner;
import cz.cvut.kbss.reporting.util.ConfigParam;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@ContextConfiguration(initializers = PropertyMockingApplicationContextInitializer.class)
public class TextAnalyzingOccurrenceReportFactoryTest extends BaseServiceTestRunner {

    private static final String SERVICE_URL = "http://localhost/analyze";
    private static final String TEXT = "Initial report text content.";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private TextAnalyzingOccurrenceReportFactory reportFactory;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Environment environment;

    private MockRestServiceServer mockServer;

    @Before
    public void setUp() {
        this.mockServer = MockRestServiceServer.createServer(restTemplate);
        final MockEnvironment mockEnvironment = (MockEnvironment) environment;
        mockEnvironment.setProperty(ConfigParam.TEXT_ANALYSIS_SERVICE_URL.toString(), SERVICE_URL);
    }

    @Test
    public void createFromInitialReportPassesInitialReportContentToTextAnalyzingService() throws Exception {
        final InitialReport initialReport = initialReport();
        mockServer.expect(requestTo(SERVICE_URL)).andExpect(method(HttpMethod.POST)).andExpect(content().string(
                CoreMatchers.containsString(TEXT))).andRespond(withSuccess());
        final OccurrenceReport result = reportFactory.createFromInitialReport(initialReport);
        assertNotNull(result);
        assertNotNull(result.getInitialReport());
        assertEquals(TEXT, result.getInitialReport().getDescription());
        mockServer.verify();
    }

    private InitialReport initialReport() {
        final InitialReport initialReport = new InitialReport();
        initialReport.setDescription(TEXT);
        return initialReport;
    }

    @Test
    public void createFromInitialReportThrowsWebServiceIntegrationExceptionWhenErrorOccurs() {
        final InitialReport initialReport = initialReport();
        mockServer.expect(requestTo(SERVICE_URL)).andExpect(method(HttpMethod.POST)).andExpect(content().string(
                CoreMatchers.containsString(TEXT))).andRespond(withServerError());
        thrown.expect(WebServiceIntegrationException.class);
        reportFactory.createFromInitialReport(initialReport);
    }

    @Test
    public void createFromInitialReportSkipsTextAnalysisWhenServiceUrlIsMissing() {
        final MockEnvironment mockEnvironment = (MockEnvironment) environment;
        mockEnvironment.setProperty(ConfigParam.TEXT_ANALYSIS_SERVICE_URL.toString(), "");
        final OccurrenceReport result = reportFactory.createFromInitialReport(initialReport());
        assertNotNull(result);
        assertNotNull(result.getInitialReport());
        assertEquals(TEXT, result.getInitialReport().getDescription());
    }
}
