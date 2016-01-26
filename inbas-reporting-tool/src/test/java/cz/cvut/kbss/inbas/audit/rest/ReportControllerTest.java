package cz.cvut.kbss.inbas.audit.rest;

import cz.cvut.kbss.inbas.audit.environment.config.MockServiceConfig;
import cz.cvut.kbss.inbas.audit.environment.util.Environment;
import cz.cvut.kbss.inbas.audit.environment.util.Generator;
import cz.cvut.kbss.inbas.audit.model.Person;
import cz.cvut.kbss.inbas.audit.model.reports.InvestigationReport;
import cz.cvut.kbss.inbas.audit.model.reports.PreliminaryReport;
import cz.cvut.kbss.inbas.audit.model.reports.Report;
import cz.cvut.kbss.inbas.audit.rest.dto.mapper.ReportMapper;
import cz.cvut.kbss.inbas.audit.service.ReportService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ContextConfiguration(classes = {MockServiceConfig.class})
public class ReportControllerTest extends BaseControllerTestRunner {

    @Autowired
    private ReportService reportServiceMock;

    @Autowired
    private ReportMapper reportMapper;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Mockito.reset(reportServiceMock);
        Person person = Generator.getPerson();
        Environment.setCurrentUser(person);
    }

    @Test
    public void createPreliminaryReportReturnsLocationOfNewInstance() throws Exception {
        final PreliminaryReport report = Generator
                .generatePreliminaryReport(Generator.ReportType.WITH_TYPE_ASSESSMENTS);
        final String key = "117";
        doAnswer(call -> {
            final PreliminaryReport pr = (PreliminaryReport) call.getArguments()[0];
            pr.setKey(key);
            return null;
        }).when(reportServiceMock).persist(any(PreliminaryReport.class));

        final MvcResult result = mockMvc.perform(
                post("/reports").content(toJson(reportMapper.preliminaryReportToPreliminaryReportDto(report)))
                                .contentType(
                                        MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertEquals(HttpStatus.CREATED, HttpStatus.valueOf(result.getResponse().getStatus()));
        verifyLocationEquals("/reports/" + key, result);
        verify(reportServiceMock).persist(any(PreliminaryReport.class));
    }

    private void verifyLocationEquals(String expectedPath, MvcResult result) {
        final String locationHeader = result.getResponse().getHeader(HttpHeaders.LOCATION);
        assertEquals("http://localhost" + expectedPath, locationHeader);
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
        verifyLocationEquals("/reports/" + newRevision.getKey(), result);
        verify(reportServiceMock).createNewRevision(fileNumber);
    }

    @Test
    public void getLatestRevisionThrowsNotFoundWhenReportChainIsNotFound() throws Exception {
        final Long fileNumber = 12345L;
        when(reportServiceMock.findLatestRevision(fileNumber)).thenReturn(null);

        final MvcResult result = mockMvc.perform(get("/reports/chain/" + fileNumber)).andReturn();
        assertEquals(HttpStatus.NOT_FOUND, HttpStatus.valueOf(result.getResponse().getStatus()));
    }

    @Test
    public void getReportThrowsNotFoundWhenReportIsNotFound() throws Exception {
        final String key = "12345";
        when(reportServiceMock.findByKey(key)).thenReturn(null);

        final MvcResult result = mockMvc.perform(get("/reports/" + key)).andReturn();
        assertEquals(HttpStatus.NOT_FOUND, HttpStatus.valueOf(result.getResponse().getStatus()));
    }

    @Test
    public void getRevisionThrowsNotFoundWhenRevisionIsNotFound() throws Exception {
        final Long fileNumber = 12345L;
        final Integer revision = 3;
        when(reportServiceMock.findRevision(fileNumber, revision)).thenReturn(null);

        final MvcResult result = mockMvc.perform(get("/reports/chain/" + fileNumber + "/revisions/" + revision))
                                        .andReturn();
        assertEquals(HttpStatus.NOT_FOUND, HttpStatus.valueOf(result.getResponse().getStatus()));
    }

    @Test
    public void updateReportWithDifferentKeyThrowsBadRequest() throws Exception {
        final PreliminaryReport report = Generator
                .generatePreliminaryReport(Generator.ReportType.WITH_TYPE_ASSESSMENTS);
        report.setKey("711");
        final String differentKey = "117";

        final MvcResult result = mockMvc.perform(put("/reports/" + differentKey)
                .content(toJson(reportMapper.preliminaryReportToPreliminaryReportDto(report)))
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertEquals(HttpStatus.BAD_REQUEST, HttpStatus.valueOf(result.getResponse().getStatus()));
    }

    @Test
    public void updateReportPassesEntityMappedFromDtoToService() throws Exception {
        final PreliminaryReport report = Generator
                .generatePreliminaryReport(Generator.ReportType.WITH_TYPE_ASSESSMENTS);
        final String key = "117";
        report.setKey(key);
        final MvcResult result = mockMvc.perform(put("/reports/" + key)
                .content(toJson(reportMapper.preliminaryReportToPreliminaryReportDto(report)))
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertEquals(HttpStatus.NO_CONTENT, HttpStatus.valueOf(result.getResponse().getStatus()));
        verify(reportServiceMock).update(any(Report.class));
    }
}