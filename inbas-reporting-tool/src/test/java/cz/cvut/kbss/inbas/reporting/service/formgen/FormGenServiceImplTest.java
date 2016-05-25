package cz.cvut.kbss.inbas.reporting.service.formgen;

import cz.cvut.kbss.inbas.reporting.environment.config.PropertyMockingApplicationContextInitializer;
import cz.cvut.kbss.inbas.reporting.environment.util.Generator;
import cz.cvut.kbss.inbas.reporting.model.OccurrenceReport;
import cz.cvut.kbss.inbas.reporting.model.Vocabulary;
import cz.cvut.kbss.inbas.reporting.rest.dto.model.RawJson;
import cz.cvut.kbss.inbas.reporting.service.BaseServiceTestRunner;
import cz.cvut.kbss.inbas.reporting.util.ConfigParam;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@PropertySource("classpath:config.properties")
@ContextConfiguration(initializers = PropertyMockingApplicationContextInitializer.class)
public class FormGenServiceImplTest extends BaseServiceTestRunner {

    private static final String MOCK_FORM_STRUCTURE = "{\"form\": {\"sections\": [{\"id\": \"sectionOne\"}, {\"id\": \"sectionTwo\"}]}}";

    @Autowired
    private Environment environment;

    @Autowired
    private FormGenService formGenService;

    @Autowired
    @Qualifier("formGen")
    private EntityManagerFactory emf;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        this.mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void formGenServicePersistsSpecifiedOccurrenceReportWhenFormIsRequested() {
        setupRemoteFormGenServiceMock(Collections.emptyMap());
        final OccurrenceReport report = getOccurrenceReport();
        formGenService.generateForm(report, Collections.emptyMap());
        final EntityManager em = emf.createEntityManager();
        try {
            assertTrue(em.createNativeQuery("ASK { ?x a ?report .} ", Boolean.class).setParameter("report", URI.create(
                    Vocabulary.OccurrenceReport)).getSingleResult());
        } finally {
            em.close();
        }
    }

    private OccurrenceReport getOccurrenceReport() {
        final OccurrenceReport report = Generator.generateOccurrenceReportWithFactorGraph();
        report.getAuthor().generateUri();   // This won't be necessary in code, the user is already persisted
        return report;
    }

    @Test
    public void formGenServiceThrowsIllegalArgumentForUnsupportedDataType() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Unsupported data type for form generation.");
        formGenService.generateForm(Generator.getPerson(), Collections.emptyMap());
    }

    @Test
    public void formGenPassesRepositoryUrlAndContextUrlToRemoteFormGenerator() throws Exception {
        setupRemoteFormGenServiceMock(Collections.emptyMap());

        final OccurrenceReport report = getOccurrenceReport();
        final RawJson result = formGenService.generateForm(report, Collections.emptyMap());
        assertNotNull(result);
        assertEquals(MOCK_FORM_STRUCTURE, result.getValue());
        mockServer.verify();
    }

    @Test
    public void formGenPassesParametersToRemoteFormGenerator() throws Exception {
        final OccurrenceReport report = Generator.generateOccurrenceReportWithFactorGraph();
        final Map<String, String> params = Collections
                .singletonMap("eventType", report.getOccurrence().getEventType().toString());
        setupRemoteFormGenServiceMock(params);

        report.getAuthor().generateUri();
        final RawJson result = formGenService.generateForm(report, params);
        assertNotNull(result);
    }

    private void setupRemoteFormGenServiceMock(Map<String, String> params) {
        final String serviceUrl = environment.getProperty(ConfigParam.FORM_GEN_SERVICE_URL.toString());
        final String repoUrl = environment.getProperty("test." + ConfigParam.FORM_GEN_REPOSITORY_URL.toString());
        ((MockEnvironment) environment).setProperty(ConfigParam.FORM_GEN_REPOSITORY_URL.toString(), repoUrl);
        final Map<String, String> expectedParams = new HashMap<>(params);
        expectedParams.put(FormGenServiceImpl.REPOSITORY_URL_PARAM, repoUrl);
        expectedParams.put(FormGenServiceImpl.CONTEXT_URI_PARAM, "");   // We don't know the context, it is random

        mockServer.expect(requestTo(new UrlWithParamsMatcher(serviceUrl, expectedParams)))
                  .andExpect(method(HttpMethod.GET))
                  .andRespond(withSuccess(MOCK_FORM_STRUCTURE, MediaType.APPLICATION_JSON));
    }

    @Test
    public void generateFormReturnsEmptyJsonWhenRemoteServiceUrlIsMissing() {
        setupRemoteFormGenServiceMock(Collections.emptyMap());
        ((MockEnvironment) environment).setProperty(ConfigParam.FORM_GEN_SERVICE_URL.toString(), "");
        final OccurrenceReport report = getOccurrenceReport();

        assertEquals("", formGenService.generateForm(report, Collections.emptyMap()).getValue());
    }

    private static final class UrlWithParamsMatcher extends BaseMatcher<String> {

        private final String url;
        private final Map<String, String> params;

        private UrlWithParamsMatcher(String url, Map<String, String> params) {
            this.url = url;
            this.params = params;
        }

        @Override
        public boolean matches(Object item) {
            final String actual = item.toString();
            if (!actual.startsWith(url)) {
                return false;
            }
            for (Map.Entry<String, String> e : params.entrySet()) {
                if (!actual.contains(e.getKey() + "=" + e.getValue())) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public void describeTo(Description description) {
            description.appendValue(url);
            boolean first = true;
            for (Map.Entry<String, String> e : params.entrySet()) {
                description.appendValue((first ? "?" : "&") + e.getKey() + "=" + e.getValue());
                first = false;
            }
        }
    }
}