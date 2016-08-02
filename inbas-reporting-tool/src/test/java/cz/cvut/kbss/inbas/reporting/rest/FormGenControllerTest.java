package cz.cvut.kbss.inbas.reporting.rest;

import cz.cvut.kbss.inbas.reporting.dto.OccurrenceReportDto;
import cz.cvut.kbss.inbas.reporting.environment.config.MockServiceConfig;
import cz.cvut.kbss.inbas.reporting.environment.config.MockSesamePersistence;
import cz.cvut.kbss.inbas.reporting.environment.generator.Generator;
import cz.cvut.kbss.inbas.reporting.environment.util.Environment;
import cz.cvut.kbss.inbas.reporting.model.OccurrenceReport;
import cz.cvut.kbss.inbas.reporting.model.Person;
import cz.cvut.kbss.inbas.reporting.rest.dto.model.FormGenData;
import cz.cvut.kbss.inbas.reporting.rest.dto.model.RawJson;
import cz.cvut.kbss.inbas.reporting.rest.handler.ErrorInfo;
import cz.cvut.kbss.inbas.reporting.service.formgen.FormGenService;
import cz.cvut.kbss.inbas.reporting.util.Constants;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MvcResult;

import java.net.URI;
import java.net.URLEncoder;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {MockServiceConfig.class, MockSesamePersistence.class})
public class FormGenControllerTest extends BaseControllerTestRunner {

    private static final String MOCK_FORM_STRUCTURE = "{\"form\": {\"sections\": [{\"id\": \"sectionOne\"}, {\"id\": \"sectionTwo\"}]}}";
    private static final String PATH = "/formGen";
    private static final String POSSIBLE_VALUES_PATH = PATH + "/possibleValues";

    @Autowired
    private FormGenService formGenService;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Mockito.reset(formGenService);
        final Person user = Generator.getPerson();
        Environment.setCurrentUser(user);
    }

    @Test
    public void generateFormPassesDataToFormGenService() throws Exception {
        final OccurrenceReportDto dto = Environment
                .loadData("data/occurrenceReportWithFactorGraph.json", OccurrenceReportDto.class);
        when(formGenService.generateForm(any(Object.class), anyMap())).thenReturn(new RawJson(MOCK_FORM_STRUCTURE));
        final MvcResult result = mockMvc
                .perform(post(PATH).content(toJson(dto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        final String json = result.getResponse().getContentAsString();
        assertEquals(MOCK_FORM_STRUCTURE, json);
        final ArgumentCaptor<OccurrenceReport> captor = ArgumentCaptor.forClass(OccurrenceReport.class);
        verify(formGenService).generateForm(captor.capture(), anyMap());
        assertNotNull(captor.getValue());
        assertTrue(captor.getValue() instanceof OccurrenceReport);
    }

    @Test
    public void generateFormReturnsConflictForUnsupportedData() throws Exception {
        final FormGenDataInvalid data = new FormGenDataInvalid();
        data.setUri(URI.create("http://testData"));
        final String errorMessage = "Unsupported data type.";
        when(formGenService.generateForm(any(FormGenData.class), anyMap()))
                .thenThrow(new IllegalArgumentException(errorMessage));
        final MvcResult result = mockMvc
                .perform(post(PATH).content(toJson(data)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict()).andReturn();
        final ErrorInfo error = readValue(result, ErrorInfo.class);
        assertNotNull(error);
        assertEquals(errorMessage, error.getMessage());
    }

    public static class FormGenDataInvalid implements FormGenData {
        private URI uri;

        public URI getUri() {
            return uri;
        }

        public void setUri(URI uri) {
            this.uri = uri;
        }
    }

    @Test
    public void getPossibleValuesPassesQueryToFormGenService() throws Exception {
        final String query = URLEncoder.encode("SELECT ?x ?y ?z WHERE {?x ?y ?z. }", Constants.UTF_8_ENCODING);
        when(formGenService.getPossibleValues(query)).thenReturn(new RawJson(MOCK_FORM_STRUCTURE));

        final MvcResult result = mockMvc.perform(get(POSSIBLE_VALUES_PATH).param("query", query))
                                        .andExpect(status().isOk()).andReturn();
        final String json = result.getResponse().getContentAsString();
        assertEquals(MOCK_FORM_STRUCTURE, json);
        verify(formGenService).getPossibleValues(query);
    }
}