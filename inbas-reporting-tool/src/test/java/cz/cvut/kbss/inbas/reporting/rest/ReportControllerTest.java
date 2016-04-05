package cz.cvut.kbss.inbas.reporting.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import cz.cvut.kbss.inbas.reporting.dto.OccurrenceReportDto;
import cz.cvut.kbss.inbas.reporting.environment.config.MockServiceConfig;
import cz.cvut.kbss.inbas.reporting.environment.config.MockSesamePersistence;
import cz.cvut.kbss.inbas.reporting.environment.util.Environment;
import cz.cvut.kbss.inbas.reporting.environment.util.Generator;
import cz.cvut.kbss.inbas.reporting.model_new.OccurrenceReport;
import cz.cvut.kbss.inbas.reporting.model_new.Person;
import cz.cvut.kbss.inbas.reporting.model_new.Report;
import cz.cvut.kbss.inbas.reporting.model_new.Vocabulary;
import cz.cvut.kbss.inbas.reporting.service.ReportBusinessService;
import cz.cvut.kbss.inbas.reporting.util.IdentificationUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MvcResult;

import java.net.URI;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {MockServiceConfig.class, MockSesamePersistence.class})
public class ReportControllerTest extends BaseControllerTestRunner {

    @Autowired
    private ReportBusinessService reportServiceMock;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Mockito.reset(reportServiceMock);
        Person person = Generator.getPerson();
        Environment.setCurrentUser(person);
    }

    @Test
    public void getAllReportsReturnsEmptyCollectionWhenThereAreNoReports() throws Exception {
        when(reportServiceMock.findAll()).thenReturn(Collections.emptyList());
        final MvcResult result = mockMvc.perform(get("/reports").accept(MediaType.APPLICATION_JSON_VALUE))
                                        .andExpect(status().isOk()).andReturn();
        final List<Report> res = objectMapper
                .readValue(result.getResponse().getContentAsByteArray(), new TypeReference<List<Report>>() {
                });
        assertNotNull(res);
        assertTrue(res.isEmpty());
    }

    @Test
    public void getReportReturnsNotFoundForUnknownKey() throws Exception {
        final String key = "unknownKey";
        when(reportServiceMock.findByKey(key)).thenReturn(null);
        mockMvc.perform(get("/reports/" + key)).andExpect(status().isNotFound());
    }

    @Test
    public void testGetReportForOccurrenceReport() throws Exception {
        final OccurrenceReport report = Generator.generateOccurrenceReport(true);
        report.getOccurrence().setUri(URI.create(Vocabulary.Occurrence + "#32145"));
        report.setKey(IdentificationUtils.generateKey());
        report.setUri(URI.create(Vocabulary.OccurrenceReport + "#instance12345"));
        when(reportServiceMock.findByKey(report.getKey())).thenReturn(report);
        final MvcResult result = mockMvc.perform(get("/reports/" + report.getKey())).andExpect(status().isOk())
                                        .andReturn();
        final OccurrenceReportDto res = readValue(result, OccurrenceReportDto.class);
        assertNotNull(res);
        assertEquals(report.getUri(), res.getUri());
        assertEquals(report.getKey(), res.getKey());
        assertEquals(report.getOccurrence().getUri(), res.getOccurrence().getUri());
    }

    @Test
    public void testGetLatestRevisionForOccurrenceReport() throws Exception {
        final OccurrenceReport latestRevision = Generator.generateOccurrenceReport(true);
        latestRevision.setRevision(Generator.randomInt(10));
        when(reportServiceMock.findLatestRevision(latestRevision.getFileNumber())).thenReturn(latestRevision);
        final MvcResult result = mockMvc.perform(get("/reports/chain/" + latestRevision.getFileNumber()))
                                        .andExpect(status().isOk()).andReturn();
        final OccurrenceReportDto res = readValue(result, OccurrenceReportDto.class);
        assertNotNull(res);
        assertEquals(latestRevision.getUri(), res.getUri());
        assertEquals(latestRevision.getRevision(), res.getRevision());
    }

    @Test
    public void getLatestRevisionThrowsNotFoundWhenReportChainIsNotFound() throws Exception {
        final Long fileNumber = 12345L;
        when(reportServiceMock.findLatestRevision(fileNumber)).thenReturn(null);
        mockMvc.perform(get("/reports/chain/" + fileNumber)).andExpect(status().isNotFound());
    }

//    @Test
//    public void createPreliminaryReportReturnsLocationOfNewInstance() throws Exception {
//        final PreliminaryReport report = null;
//        final String key = "117";
//        doAnswer(call -> {
//            final PreliminaryReport pr = (PreliminaryReport) call.getArguments()[0];
//            pr.setKey(key);
//            return null;
//        }).when(reportServiceMock).persist(any(PreliminaryReport.class));
//
//        final MvcResult result = mockMvc.perform(
//                post("/reports").content(toJson(null))
//                                .contentType(
//                                        MediaType.APPLICATION_JSON_VALUE)).andReturn();
//        assertEquals(HttpStatus.CREATED, HttpStatus.valueOf(result.getResponse().getStatus()));
//        verifyLocationEquals("/reports/" + key, result);
//        verify(reportServiceMock).persist(any(PreliminaryReport.class));
//    }
//
//    private void verifyLocationEquals(String expectedPath, MvcResult result) {
//        final String locationHeader = result.getResponse().getHeader(HttpHeaders.LOCATION);
//        assertEquals("http://localhost" + expectedPath, locationHeader);
//    }
//
//    @Test
//    public void createNewRevisionReturnsLocationOfNewRevision() throws Exception {
//        final Long fileNumber = 12345L;
//        final InvestigationReport newRevision = new InvestigationReport();
//        newRevision.setFileNumber(fileNumber);
//        newRevision.setKey("117");
//        when(reportServiceMock.createNewRevision(fileNumber)).thenReturn(newRevision);
//
//        final MvcResult result = mockMvc.perform(post("/reports/chain/" + fileNumber + "/revisions")).andReturn();
//        assertEquals(HttpStatus.CREATED, HttpStatus.valueOf(result.getResponse().getStatus()));
//        verifyLocationEquals("/reports/" + newRevision.getKey(), result);
//        verify(reportServiceMock).createNewRevision(fileNumber);
//    }
//
//    @Test
//    public void getRevisionThrowsNotFoundWhenRevisionIsNotFound() throws Exception {
//        final Long fileNumber = 12345L;
//        final Integer revision = 3;
//        when(reportServiceMock.findRevision(fileNumber, revision)).thenReturn(null);
//
//        final MvcResult result = mockMvc.perform(get("/reports/chain/" + fileNumber + "/revisions/" + revision))
//                                        .andReturn();
//        assertEquals(HttpStatus.NOT_FOUND, HttpStatus.valueOf(result.getResponse().getStatus()));
//    }
//
//    @Test
//    public void updateReportWithDifferentKeyThrowsBadRequest() throws Exception {
//        final PreliminaryReport report = null;
//        report.setKey("711");
//        final String differentKey = "117";
//
//        final MvcResult result = mockMvc.perform(put("/reports/" + differentKey)
//                .content(toJson(null))
//                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
//        assertEquals(HttpStatus.BAD_REQUEST, HttpStatus.valueOf(result.getResponse().getStatus()));
//    }
//
//    @Test
//    public void updateReportPassesEntityMappedFromDtoToService() throws Exception {
//        final PreliminaryReport report = null;
//        final String key = "117";
//        report.setKey(key);
//        final MvcResult result = mockMvc.perform(put("/reports/" + key)
//                .content(toJson(null))
//                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
//        assertEquals(HttpStatus.NO_CONTENT, HttpStatus.valueOf(result.getResponse().getStatus()));
//        verify(reportServiceMock).update(any(Report.class));
//    }
//
//    @Test
//    public void createRevisionWithInvestigateParamStartsInvestigationFromLatestPreliminaryReport() throws Exception {
//        final Long fileNumber = 12345L;
//        final InvestigationReport investigation = null;
//        investigation.setFileNumber(fileNumber);
//        investigation.setKey("117");
//        when(reportServiceMock.startInvestigation(fileNumber)).thenReturn(investigation);
//
//        mockMvc.perform(
//                post("/reports/chain/" + fileNumber + "/revisions").param("investigate", Boolean.TRUE.toString()))
//               .andReturn();
//        verify(reportServiceMock).startInvestigation(fileNumber);
//    }
//
//    @Test
//    public void startingInvestigationReturnsLocationHeader() throws Exception {
//        final Long fileNumber = 12345L;
//        final String key = "117";
//        final InvestigationReport investigation = null;
//        investigation.setFileNumber(fileNumber);
//        investigation.setKey(key);
//        when(reportServiceMock.startInvestigation(fileNumber)).thenReturn(investigation);
//
//        final MvcResult result = mockMvc.perform(
//                post("/reports/chain/" + fileNumber + "/revisions").param("investigate", Boolean.TRUE.toString()))
//                                        .andReturn();
//        assertEquals(HttpStatus.CREATED, HttpStatus.valueOf(result.getResponse().getStatus()));
//        verifyLocationEquals("/reports/" + key, result);
//    }
}