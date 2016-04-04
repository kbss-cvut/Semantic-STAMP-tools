package cz.cvut.kbss.inbas.reporting.rest;

import cz.cvut.kbss.inbas.reporting.environment.config.MockServiceConfig;
import cz.cvut.kbss.inbas.reporting.environment.config.MockSesamePersistence;
import cz.cvut.kbss.inbas.reporting.environment.util.Environment;
import cz.cvut.kbss.inbas.reporting.environment.util.Generator;
import cz.cvut.kbss.inbas.reporting.model_new.Person;
import cz.cvut.kbss.inbas.reporting.service.PersonService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ContextConfiguration(classes = {MockServiceConfig.class, MockSesamePersistence.class})
public class PersonControllerTest extends BaseControllerTestRunner {

    @Autowired
    private PersonService personService;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Mockito.reset(personService);
    }

    @Test
    public void findByUsernameThrowsNotFoundForUnknownUsername() throws Exception {
        Environment.setCurrentUser(Generator.getPerson());
        final String unknownUsername = "unknownUsername";
        when(personService.findByUsername(unknownUsername)).thenReturn(null);
        MvcResult result = mockMvc.perform(get("/persons/" + unknownUsername)).andReturn();
        assertEquals(HttpStatus.NOT_FOUND, HttpStatus.valueOf(result.getResponse().getStatus()));
        verify(personService).findByUsername(unknownUsername);
    }

    @Test
    public void getCurrentUserReturnsTheCurrentlyLoggedInUser() throws Exception {
        final Person p = Generator.getPerson();
        p.generateUri();
        Environment.setCurrentUser(p);
        when(personService.findByUsername(p.getUsername())).thenReturn(p);
        MvcResult result = mockMvc.perform(get("/persons/current").principal(Environment.getCurrentUserPrincipal()))
                                  .andReturn();
        final Person res = objectMapper.readValue(result.getResponse().getContentAsString(), Person.class);
        assertEquals(p.getUri(), res.getUri());
        assertTrue(p.valueEquals(res));
    }
}