package cz.cvut.kbss.inbas.audit.rest;

import cz.cvut.kbss.inbas.audit.environment.config.MockServiceConfig;
import cz.cvut.kbss.inbas.audit.environment.util.Environment;
import cz.cvut.kbss.inbas.audit.environment.util.Generator;
import cz.cvut.kbss.inbas.audit.model.Person;
import cz.cvut.kbss.inbas.audit.model.reports.InvestigationReport;
import cz.cvut.kbss.inbas.audit.model.reports.PreliminaryReport;
import cz.cvut.kbss.inbas.audit.service.InvestigationReportService;
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

@ContextConfiguration(classes = {MockServiceConfig.class})
public class InvestigationControllerTest extends BaseControllerTest {

    @Autowired
    private PreliminaryReportService preliminaryReportServiceMock;
    @Autowired
    private InvestigationReportService investigationReportServiceMock;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Mockito.reset(preliminaryReportServiceMock, investigationReportServiceMock);
        Person person = Generator.getPerson();
        Environment.setCurrentUser(person);
    }

    @Test
    public void createInvestigationFromPreliminaryReportReturnsLocationHeaderUrlToCreatedInvestigation()
            throws Exception {
        final String key = "12345";
        final String investigationKey = "54321";
        final PreliminaryReport pr = new PreliminaryReport();
        pr.setKey(key);
        final InvestigationReport ir = new InvestigationReport();
        ir.setKey(investigationKey);
        when(preliminaryReportServiceMock.findByKey(key)).thenReturn(pr);
        when(investigationReportServiceMock.createFromPreliminaryReport(pr)).thenReturn(ir);

        final MvcResult result = mockMvc.perform(post("/investigations?key=" + key)).andReturn();
        assertEquals(HttpStatus.CREATED, HttpStatus.valueOf(result.getResponse().getStatus()));
        final String locationHeader = result.getResponse().getHeader(HttpHeaders.LOCATION);
        assertEquals("http://localhost/investigations/" + investigationKey, locationHeader);
        verify(investigationReportServiceMock).createFromPreliminaryReport(pr);
    }
}