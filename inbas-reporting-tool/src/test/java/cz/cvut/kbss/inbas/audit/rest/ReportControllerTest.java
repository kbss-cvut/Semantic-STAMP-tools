package cz.cvut.kbss.inbas.audit.rest;

import cz.cvut.kbss.inbas.audit.environment.config.MockServiceConfig;
import cz.cvut.kbss.inbas.audit.environment.util.Environment;
import cz.cvut.kbss.inbas.audit.environment.util.Generator;
import cz.cvut.kbss.inbas.audit.model.Person;
import cz.cvut.kbss.inbas.audit.model.reports.InvestigationReport;
import cz.cvut.kbss.inbas.audit.service.ReportService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ContextConfiguration(classes = {MockServiceConfig.class})
public class ReportControllerTest extends BaseControllerTestRunner {

    @Autowired
    private ReportService reportServiceMock;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Mockito.reset(reportServiceMock);
        Person person = Generator.getPerson();
        Environment.setCurrentUser(person);
    }

    @Test
    public void createNewRevisionReturnsLocationOfNewRevision() throws Exception {
        final Long fileNumber = 12345L;
        final InvestigationReport newRevision = new InvestigationReport();
        newRevision.setFileNumber(fileNumber);
        newRevision.setKey("117");
        when(reportServiceMock.createNewRevision(fileNumber)).thenReturn(newRevision);

        final MvcResult result = mockMvc.perform(post("/reports/chain/" + fileNumber + "/revisions")).andReturn();
        assertEquals(HttpStatus.CREATED, HttpStatus.valueOf(result.getResponse().getStatus()));
        final String locationHeader = result.getResponse().getHeader(HttpHeaders.LOCATION);
        assertEquals("http://localhost/reports/" + newRevision.getKey(), locationHeader);
        verify(reportServiceMock).createNewRevision(fileNumber);
    }

    @Test
    public void getLatestRevisionThrowsNotFoundWhenReportChainIsNotFound() throws Exception {
        final Long fileNumber = 12345L;

        final MvcResult result = mockMvc.perform(get("/reports/chain/" + fileNumber)).andReturn();
        assertEquals(HttpStatus.NOT_FOUND, HttpStatus.valueOf(result.getResponse().getStatus()));
    }
}