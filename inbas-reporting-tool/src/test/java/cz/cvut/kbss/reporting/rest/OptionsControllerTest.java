package cz.cvut.kbss.reporting.rest;

import cz.cvut.kbss.reporting.environment.generator.Generator;
import cz.cvut.kbss.reporting.environment.util.Environment;
import cz.cvut.kbss.reporting.rest.dto.model.RawJson;
import cz.cvut.kbss.reporting.service.options.OptionsService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OptionsControllerTest extends BaseControllerTestRunner {

    @Mock
    private OptionsService optionsServiceMock;

    @InjectMocks
    private OptionsController controller;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        super.setUp(controller);
        Environment.setCurrentUser(Generator.getPerson());
    }

    @Test
    public void getOptionsReturnsOptionsForSpecifiedType() throws Exception {
        final String optionsType = "test";
        final String options = "[{\n" +
                "\"IATA\":\"OK\",\n" +
                "\"ICAO\":\"CSA\",\n" +
                "\"operator\":\"Czech Airlines\",\n" +
                "\"country\":\"Czech Rep.\",\n" +
                "\"radioCallsign\":\"CSA Lines\"\n" +
                "  }]";
        when(optionsServiceMock.getOptions(optionsType)).thenReturn(new RawJson(options));
        final MvcResult result = mockMvc.perform(get("/options").param("type", optionsType))
                                        .andExpect(status().isOk()).andReturn();
        assertEquals(options, result.getResponse().getContentAsString());
    }

    @Test
    public void getOptionsWithUnknownTypeReturnsIllegalArgumentWrappedInBadRequestResponse() throws Exception {
        final String unknownType = "unknownType";
        final String message = "Unsupported option type " + unknownType;
        when(optionsServiceMock.getOptions(unknownType)).thenThrow(new IllegalArgumentException(message));
        final MvcResult result = mockMvc.perform(get("/options").param("type", unknownType))
                                        .andExpect(status().isBadRequest()).andReturn();
        final String msg = result.getResponse().getContentAsString();
        assertTrue(msg.contains(message));
    }
}