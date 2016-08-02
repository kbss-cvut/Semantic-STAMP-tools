package cz.cvut.kbss.inbas.reporting.rest;

import cz.cvut.kbss.inbas.reporting.environment.config.MockServiceConfig;
import cz.cvut.kbss.inbas.reporting.environment.config.MockSesamePersistence;
import cz.cvut.kbss.inbas.reporting.environment.generator.OccurrenceReportGenerator;
import cz.cvut.kbss.inbas.reporting.service.arms.ArmsService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MvcResult;

import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ContextConfiguration(classes = {MockServiceConfig.class, MockSesamePersistence.class})
public class ArmsControllerTest extends BaseControllerTestRunner {

    @Autowired
    private ArmsService armsService;

    @Test
    public void testGetArmsIndex() throws Exception {
        final URI outcome = OccurrenceReportGenerator.ACCIDENT_CATASTROPHIC;
        final URI barrier = OccurrenceReportGenerator.BARRIER_NOT_EFFECTIVE;
        final int expected = 117;
        when(armsService.calculateArmsIndex(outcome, barrier)).thenReturn(expected);
        final MvcResult result = mockMvc.perform(
                get("/arms/").param("accidentOutcome", outcome.toString())
                             .param("barrierEffectiveness", barrier.toString())).andReturn();
        assertEquals(HttpStatus.OK, HttpStatus.valueOf(result.getResponse().getStatus()));
        final int armsRes = readValue(result, Integer.class);
        assertEquals(expected, armsRes);
    }

    @Test
    public void getArmsIndexReturnsBadRequestWhenInvalidUriIsPassed() throws Exception {
        final MvcResult result = mockMvc.perform(
                get("/arms/").param("accidentOutcome", OccurrenceReportGenerator.ACCIDENT_CATASTROPHIC.toString())
                             .param("barrierEffectiveness", "_+++f//fds/dskfj;     j1213")).andReturn();
        assertEquals(HttpStatus.BAD_REQUEST, HttpStatus.valueOf(result.getResponse().getStatus()));
    }
}