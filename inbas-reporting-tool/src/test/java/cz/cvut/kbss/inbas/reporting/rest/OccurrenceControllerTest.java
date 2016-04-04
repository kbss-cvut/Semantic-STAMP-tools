package cz.cvut.kbss.inbas.reporting.rest;

import cz.cvut.kbss.inbas.reporting.environment.config.MockServiceConfig;
import cz.cvut.kbss.inbas.reporting.environment.config.MockSesamePersistence;
import cz.cvut.kbss.inbas.reporting.environment.util.Environment;
import cz.cvut.kbss.inbas.reporting.environment.util.Generator;
import cz.cvut.kbss.inbas.reporting.model_new.Occurrence;
import cz.cvut.kbss.inbas.reporting.service.OccurrenceService;
import cz.cvut.kbss.inbas.reporting.util.IdentificationUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MvcResult;

import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ContextConfiguration(classes = {MockServiceConfig.class, MockSesamePersistence.class})
public class OccurrenceControllerTest extends BaseControllerTestRunner {

    @Autowired
    private OccurrenceService occurrenceService;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Mockito.reset(occurrenceService);
        Environment.setCurrentUser(Generator.getPerson());
    }

    @Test
    public void findByKeyReturnsMatchingOccurrence() throws Exception {
        final Occurrence occurrence = Generator.generateOccurrence();
        occurrence.setKey(IdentificationUtils.generateKey());
        occurrence.setUri(URI.create("http://onto.fel.cvut.cz/ontologies/documentation/occurrence#instance12345"));
        when(occurrenceService.findByKey(occurrence.getKey())).thenReturn(occurrence);
        final MvcResult result = mockMvc.perform(get("/occurrences/" + occurrence.getKey())).andReturn();
        assertEquals(HttpStatus.OK, HttpStatus.valueOf(result.getResponse().getStatus()));
        final Occurrence res = objectMapper.readValue(result.getResponse().getContentAsByteArray(), Occurrence.class);
        assertNotNull(res);
        assertEquals(occurrence.getUri(), res.getUri());
        assertEquals(occurrence.getName(), res.getName());
    }

    @Test
    public void findByKeyReturnsNotFoundForUnknownKey() throws Exception {
        final String unknownKey = "11223344";
        when(occurrenceService.findByKey(unknownKey)).thenReturn(null);
        final MvcResult result = mockMvc.perform(get("/occurrences/" + unknownKey)).andReturn();
        assertEquals(HttpStatus.NOT_FOUND, HttpStatus.valueOf(result.getResponse().getStatus()));
        verify(occurrenceService).findByKey(unknownKey);
    }
}