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
        setupRemoteFormGenServiceMock();
        final OccurrenceReport report = Generator.generateOccurrenceReportWithFactorGraph();
        report.getAuthor().generateUri();   // This won't be necessary in code, the user is already persisted
        formGenService.generateForm(report);
        final EntityManager em = emf.createEntityManager();
        try {
            assertTrue(em.createNativeQuery("ASK { ?x a ?report .} ", Boolean.class).setParameter("report", URI.create(
                    Vocabulary.OccurrenceReport)).getSingleResult());
        } finally {
            em.close();
        }
    }

    @Test
    public void formGenServiceThrowsIllegalArgumentForUnsupportedDataType() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Unsupported data type for form generation.");
        formGenService.generateForm(Generator.getPerson());
    }

    @Test
    public void formGenPassesRepositoryUrlAndContextUrlToRemoteFormGenerator() throws Exception {
        setupRemoteFormGenServiceMock();

        final OccurrenceReport report = Generator.generateOccurrenceReportWithFactorGraph();
        report.getAuthor().generateUri();
        final RawJson result = formGenService.generateForm(report);
        assertNotNull(result);
        assertEquals(MOCK_FORM_STRUCTURE, result.getValue());
        mockServer.verify();
    }

    private void setupRemoteFormGenServiceMock() {
        final String serviceUrl = environment.getProperty(ConfigParam.FORM_GEN_SERVICE_URL.toString());
        final String repoUrl = environment.getProperty("test." + ConfigParam.FORM_GEN_REPOSITORY_URL.toString());
        ((MockEnvironment) environment).setProperty(ConfigParam.FORM_GEN_REPOSITORY_URL.toString(), repoUrl);

        mockServer.expect(requestTo(new UrlWithParamsMatcher(serviceUrl, repoUrl))).andExpect(method(HttpMethod.GET))
                  .andRespond(withSuccess(MOCK_FORM_STRUCTURE, MediaType.APPLICATION_JSON));
    }

    @Test
    public void generateFormReturnsEmptyJsonWhenRemoteServiceUrlIsMissing() {
        setupRemoteFormGenServiceMock();
        ((MockEnvironment) environment).setProperty(ConfigParam.FORM_GEN_SERVICE_URL.toString(), "");
        final OccurrenceReport report = Generator.generateOccurrenceReportWithFactorGraph();
        report.getAuthor().generateUri();

        assertEquals("", formGenService.generateForm(report).getValue());
    }

    private static final class UrlWithParamsMatcher extends BaseMatcher<String> {

        private final String url;
        private final String repoUrl;

        private UrlWithParamsMatcher(String url, String repoUrl) {
            this.url = url;
            this.repoUrl = repoUrl;
        }

        @Override
        public boolean matches(Object item) {
            final String actual = item.toString();
            return actual.startsWith(url) && actual.contains(FormGenServiceImpl.REPOSITORY_URL_PARAM + "=" + repoUrl) &&
                    actual.contains(FormGenServiceImpl.CONTEXT_URI_PARAM + "=");
        }

        @Override
        public void describeTo(Description description) {
            description.appendValue(url + "?" + FormGenServiceImpl.REPOSITORY_URL_PARAM + "=" + url + "&" +
                    FormGenServiceImpl.CONTEXT_URI_PARAM + "=$CONTEXT$");
        }
    }
}