package cz.cvut.kbss.reporting.rest;

import cz.cvut.kbss.reporting.environment.config.MockServiceConfig;
import cz.cvut.kbss.reporting.environment.config.MockSesamePersistence;
import cz.cvut.kbss.reporting.environment.generator.Generator;
import cz.cvut.kbss.reporting.environment.generator.OccurrenceReportGenerator;
import cz.cvut.kbss.reporting.environment.generator.SafetyIssueReportGenerator;
import cz.cvut.kbss.reporting.model.safetyissue.SafetyIssueRiskAssessment;
import cz.cvut.kbss.reporting.rest.handler.ErrorInfo;
import cz.cvut.kbss.reporting.service.arms.ArmsService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MvcResult;

import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {MockServiceConfig.class, MockSesamePersistence.class})
public class ArmsControllerTest extends BaseControllerTestRunner {

    private static final String PATH = "/arms";

    @Autowired
    private ArmsService armsService;

    @Test
    public void testGetArmsIndex() throws Exception {
        final URI outcome = OccurrenceReportGenerator.ACCIDENT_CATASTROPHIC;
        final URI barrier = OccurrenceReportGenerator.BARRIER_NOT_EFFECTIVE;
        final int expected = 117;
        when(armsService.calculateArmsIndex(outcome, barrier)).thenReturn(expected);
        final MvcResult result = mockMvc.perform(
                get(PATH).param("accidentOutcome", outcome.toString())
                         .param("barrierEffectiveness", barrier.toString())).andReturn();
        assertEquals(HttpStatus.OK, HttpStatus.valueOf(result.getResponse().getStatus()));
        final int armsRes = readValue(result, Integer.class);
        assertEquals(expected, armsRes);
    }

    @Test
    public void getArmsIndexReturnsBadRequestWhenInvalidUriIsPassed() throws Exception {
        final MvcResult result = mockMvc.perform(
                get(PATH).param("accidentOutcome", OccurrenceReportGenerator.ACCIDENT_CATASTROPHIC.toString())
                         .param("barrierEffectiveness", "_+++f//fds/dskfj;     j1213")).andReturn();
        assertEquals(HttpStatus.BAD_REQUEST, HttpStatus.valueOf(result.getResponse().getStatus()));
    }

    @Test
    public void getSiraValueReturnsCalculatedSiraValue() throws Exception {
        final SafetyIssueRiskAssessment sira = SafetyIssueReportGenerator.generateSira();
        final URI siraValue = Generator.generateUri();
        when(armsService.calculateSafetyIssueRiskAssessment(sira)).thenReturn(siraValue);
        final MvcResult mvcResult = mockMvc
                .perform(post(PATH + "/sira").content(objectMapper.writeValueAsBytes(sira)).contentType(
                        MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk()).andReturn();
        final URI result = readValue(mvcResult, URI.class);
        assertEquals(siraValue, result);
    }

    @Test
    public void getSiraValueReturnsBadRequestWhenInvalidValuesArePassedIn() throws Exception {
        final SafetyIssueRiskAssessment sira = SafetyIssueReportGenerator.generateSira();
        sira.setInitialEventFrequency(Generator.generateUri());
        final String errorMessage = "Invalid SIRA value(s).";
        when(armsService.calculateSafetyIssueRiskAssessment(sira))
                .thenThrow(new IllegalArgumentException(errorMessage));
        final MvcResult mvcResult = mockMvc
                .perform(post(PATH + "/sira").content(objectMapper.writeValueAsBytes(sira)).contentType(
                        MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isBadRequest()).andReturn();
        final ErrorInfo errorInfo = readValue(mvcResult, ErrorInfo.class);
        assertEquals(errorMessage, errorInfo.getMessage());
    }

    @Test
    public void getSiraValueReturnsConflictWhenAnyOfValuesIsMissing() throws Exception {
        final SafetyIssueRiskAssessment sira = SafetyIssueReportGenerator.generateSira();
        sira.setInitialEventFrequency(null);
        when(armsService.calculateSafetyIssueRiskAssessment(sira)).thenReturn(null);
        mockMvc.perform(post(PATH + "/sira").content(objectMapper.writeValueAsBytes(sira)).contentType(
                MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isConflict());
    }
}
