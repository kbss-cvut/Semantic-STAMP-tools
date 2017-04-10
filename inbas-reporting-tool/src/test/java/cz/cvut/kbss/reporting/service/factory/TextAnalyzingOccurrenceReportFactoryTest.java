package cz.cvut.kbss.reporting.service.factory;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.kbss.reporting.environment.config.PropertyMockingApplicationContextInitializer;
import cz.cvut.kbss.reporting.environment.generator.Generator;
import cz.cvut.kbss.reporting.exception.WebServiceIntegrationException;
import cz.cvut.kbss.reporting.model.InitialReport;
import cz.cvut.kbss.reporting.model.OccurrenceReport;
import cz.cvut.kbss.reporting.model.textanalysis.ExtractedItem;
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
import org.springframework.http.MediaType;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@ContextConfiguration(initializers = PropertyMockingApplicationContextInitializer.class)
public class TextAnalyzingOccurrenceReportFactoryTest extends BaseServiceTestRunner {

    private static final String SERVICE_URL = "http://localhost/analyze";
    private static final String TEXT = "Initial report text content.";
    private static final double DEFAULT_CONFIDENCE = 0.5;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private TextAnalyzingOccurrenceReportFactory reportFactory;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Environment environment;

    private ObjectMapper objectMapper = new ObjectMapper();

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
                CoreMatchers.containsString(TEXT))).andRespond(
                withSuccess(objectMapper.writeValueAsString(textAnalysisResult()), MediaType.APPLICATION_JSON));
        final OccurrenceReport result = reportFactory.createFromInitialReport(initialReport);
        assertNotNull(result);
        assertNotNull(result.getInitialReport());
        assertEquals(TEXT, result.getInitialReport().getDescription());
        mockServer.verify();
    }

    private TextAnalyzingOccurrenceReportFactory.TextAnalysisResultWrapper textAnalysisResult() {
        final TextAnalyzingOccurrenceReportFactory.TextAnalysisResultWrapper analysisResult = new TextAnalyzingOccurrenceReportFactory.TextAnalysisResultWrapper();
        final double confidence = 0.5;
        analysisResult.setConfidence(Double.toString(confidence));
        analysisResult.setResults(Collections.emptyList());
        return analysisResult;
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

    @Test
    public void createFromInitialAttachesTextAnalysisResultsToInitialReport() throws Exception {
        final TextAnalyzingOccurrenceReportFactory.TextAnalysisResultWrapper analysisResult = textAnalysisResult();
        final List<TextAnalyzingOccurrenceReportFactory.TextAnalysisResult> items = IntStream.range(5, 10)
                                                                                             .mapToObj(i -> {
                                                                                                 final TextAnalyzingOccurrenceReportFactory.TextAnalysisResult item = new TextAnalyzingOccurrenceReportFactory.TextAnalysisResult();
                                                                                                 item.setEntityLabel(
                                                                                                         "EntityType" +
                                                                                                                 i);
                                                                                                 item.setEntityResource(
                                                                                                         Generator
                                                                                                                 .generateEventType());
                                                                                                 return item;
                                                                                             }).collect(
                        Collectors.toList());
        analysisResult.setResults(items);
        mockServer.expect(requestTo(SERVICE_URL)).andExpect(method(HttpMethod.POST)).andExpect(content().string(
                CoreMatchers.containsString(TEXT)))
                  .andRespond(withSuccess(objectMapper.writeValueAsString(analysisResult), MediaType.APPLICATION_JSON));

        final OccurrenceReport result = reportFactory.createFromInitialReport(initialReport());
        final InitialReport initialReport = result.getInitialReport();
        assertEquals(items.size(), initialReport.getExtractedItems().size());
        for (TextAnalyzingOccurrenceReportFactory.TextAnalysisResult r : items) {
            final Optional<ExtractedItem> matching = initialReport.getExtractedItems().stream().filter(item ->
                    item.getLabel().equals(r.getEntityLabel()) &&
                            item.getResource().equals(r.getEntityResource())).findAny();
            assertTrue(matching.isPresent());
            assertEquals(DEFAULT_CONFIDENCE, matching.get().getConfidence(), 0.001);
        }
    }
}
