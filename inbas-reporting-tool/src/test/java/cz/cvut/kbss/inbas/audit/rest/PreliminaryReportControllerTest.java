package cz.cvut.kbss.inbas.audit.rest;

import cz.cvut.kbss.inbas.audit.environment.config.MockServiceConfig;
import cz.cvut.kbss.inbas.audit.environment.util.Environment;
import cz.cvut.kbss.inbas.audit.environment.util.Generator;
import cz.cvut.kbss.inbas.audit.model.Person;
import cz.cvut.kbss.inbas.audit.model.reports.PreliminaryReport;
import cz.cvut.kbss.inbas.audit.service.PreliminaryReportService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ContextConfiguration(classes = MockServiceConfig.class)
public class PreliminaryReportControllerTest extends BaseControllerTestRunner {

    @Autowired
    private PreliminaryReportService preliminaryReportServiceMock;


    @Before
    public void setUp() throws Exception {
        super.setUp();
        Mockito.reset(preliminaryReportServiceMock);
        Person person = Generator.getPerson();
        Environment.setCurrentUser(person);
    }

    @Test
    public void createNewRevisionReturnsLocationUrlWithLocationOfNewRevision() throws Exception {
        final String rOneKey = "12345";
        final String rTwoKey = "23456";
        final PreliminaryReport revisionOne = new PreliminaryReport();
        revisionOne.setKey(rOneKey);
        final PreliminaryReport revisionTwo = new PreliminaryReport();
        revisionTwo.setKey(rTwoKey);
        when(preliminaryReportServiceMock.findByKey(rOneKey)).thenReturn(revisionOne);
        when(preliminaryReportServiceMock.createNewRevision(revisionOne)).thenReturn(revisionTwo);

        final MvcResult result = mockMvc.perform(post("/preliminaryReports/" + rOneKey + "/revision")).andReturn();
        assertEquals(HttpStatus.CREATED, HttpStatus.valueOf(result.getResponse().getStatus()));
        final String locationHeader = result.getResponse().getHeader(HttpHeaders.LOCATION);
        assertEquals("http://localhost/preliminaryReports/" + rTwoKey, locationHeader);
        verify(preliminaryReportServiceMock).createNewRevision(revisionOne);
    }
}
