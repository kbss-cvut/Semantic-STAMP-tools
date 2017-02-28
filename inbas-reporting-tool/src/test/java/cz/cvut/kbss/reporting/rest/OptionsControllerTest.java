package cz.cvut.kbss.reporting.rest;

import cz.cvut.kbss.reporting.dto.StatisticsConfiguration;
import cz.cvut.kbss.reporting.environment.config.MockServiceConfig;
import cz.cvut.kbss.reporting.environment.config.MockSesamePersistence;
import cz.cvut.kbss.reporting.environment.generator.Generator;
import cz.cvut.kbss.reporting.environment.util.Environment;
import cz.cvut.kbss.reporting.rest.dto.model.RawJson;
import cz.cvut.kbss.reporting.rest.handler.ErrorInfo;
import cz.cvut.kbss.reporting.service.options.OptionsService;
import cz.cvut.kbss.reporting.util.ConfigParam;
import cz.cvut.kbss.reporting.util.Constants;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {MockServiceConfig.class, MockSesamePersistence.class})
public class OptionsControllerTest extends BaseControllerTestRunner {

    private static final String URL = "/options";

    @Autowired
    private OptionsService optionsServiceMock;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Mockito.reset(optionsServiceMock);
        Environment.setCurrentUser(user);
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
        when(optionsServiceMock.getOptions(eq(optionsType), anyMap())).thenReturn(new RawJson(options));
        final MvcResult result = mockMvc.perform(get("/options").param("type", optionsType))
                                        .andExpect(status().isOk()).andReturn();
        assertEquals(options, result.getResponse().getContentAsString());
    }

    @Test
    public void getOptionsWithUnknownTypeReturnsIllegalArgumentWrappedInBadRequestResponse() throws Exception {
        final String unknownType = "unknownType";
        final String message = "Unsupported option type " + unknownType;
        when(optionsServiceMock.getOptions(eq(unknownType), anyMap())).thenThrow(new IllegalArgumentException(message));
        final MvcResult result = mockMvc.perform(get(URL).param("type", unknownType))
                                        .andExpect(status().isBadRequest()).andReturn();
        final ErrorInfo errorInfo = readValue(result, ErrorInfo.class);
        assertTrue(errorInfo.getMessage().contains(message));
    }

    @Test
    public void getOptionsWithoutOptionsTypeReturnsBadRequest() throws Exception {
        final MvcResult result = mockMvc.perform(get(URL)).andExpect(status().isBadRequest()).andReturn();
        final ErrorInfo errorInfo = readValue(result, ErrorInfo.class);
        assertEquals("Missing options type parameter - \'type\'.", errorInfo.getMessage());
        verify(optionsServiceMock, never()).getOptions(anyString(), anyMap());
    }

    @Test
    public void getOptionsPassesAdditionalParametersToOptionsService() throws Exception {
        final String type = "eventType";
        final Map<String, String> params = new HashMap<>();
        for (int i = 0; i < Generator.randomInt(2, 5); i++) {
            params.put("param" + i, Integer.toString(i));
        }
        final MockHttpServletRequestBuilder b = get(URL);
        b.param(Constants.OPTIONS_TYPE_QUERY_PARAM, type);
        for (Map.Entry<String, String> e : params.entrySet()) {
            b.param(e.getKey(), e.getValue());
        }
        mockMvc.perform(b).andExpect(status().isOk());
        verify(optionsServiceMock).getOptions(type, params);
    }

    @Test
    public void getStatisticsConfigLoadsStatisticsConfigurationFromOptionsService() throws Exception {
        final StatisticsConfiguration expected = new StatisticsConfiguration();
        expected.add(ConfigParam.STATISTICS_DASHBOARD, "http://kbss.felk.cvut.cz/statistics");
        when(optionsServiceMock.getStatisticsConfiguration()).thenReturn(expected);
        final MvcResult result = mockMvc.perform(get(URL + "/statistics/config")).andExpect(status().isOk())
                                        .andReturn();
        final StatisticsConfiguration config = readValue(result, StatisticsConfiguration.class);
        assertEquals(expected, config);
    }
}
