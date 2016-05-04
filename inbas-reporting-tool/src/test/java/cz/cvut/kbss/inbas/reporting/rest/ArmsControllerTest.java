package cz.cvut.kbss.inbas.reporting.rest;

import cz.cvut.kbss.inbas.reporting.environment.config.MockServiceConfig;
import cz.cvut.kbss.inbas.reporting.environment.config.MockSesamePersistence;
import cz.cvut.kbss.inbas.reporting.model.arms.AccidentOutcome;
import cz.cvut.kbss.inbas.reporting.model.arms.BarrierEffectiveness;
import cz.cvut.kbss.inbas.reporting.service.arms.ArmsService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ContextConfiguration(classes = {MockServiceConfig.class, MockSesamePersistence.class})
public class ArmsControllerTest extends BaseControllerTestRunner {

    @Autowired
    private ArmsService armsService;

    @Test
    public void testGetArmsIndex() throws Exception {
        final AccidentOutcome outcome = AccidentOutcome.CATASTROPHIC;
        final BarrierEffectiveness barrier = BarrierEffectiveness.NOT_EFFECTIVE;
        final short expected = armsService.calculateArmsIndex(outcome, barrier);
        final MvcResult result = mockMvc.perform(
                get("/arms/").param("accidentOutcome", outcome.toString())
                             .param("barrierEffectiveness", barrier.toString())).andReturn();
        assertEquals(HttpStatus.OK, HttpStatus.valueOf(result.getResponse().getStatus()));
        final short armsRes = readValue(result, Short.class);
        assertEquals(expected, armsRes);
    }

    @Test
    public void getArmsIndexReturnsBadRequestWhenUnsupportedValueIsPassed() throws Exception {
        final MvcResult result = mockMvc.perform(
                get("/arms/").param("accidentOutcome", AccidentOutcome.CATASTROPHIC.toString())
                             .param("barrierEffectiveness", "blabla")).andReturn();
        assertEquals(HttpStatus.BAD_REQUEST, HttpStatus.valueOf(result.getResponse().getStatus()));
    }
}