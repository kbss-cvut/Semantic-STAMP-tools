package cz.cvut.kbss.reporting.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.jsonldjava.utils.JsonUtils;
import cz.cvut.kbss.jopa.exceptions.RollbackException;
import cz.cvut.kbss.jsonld.JsonLd;
import cz.cvut.kbss.reporting.dto.OccurrenceReportDto;
import cz.cvut.kbss.reporting.dto.ReportRevisionInfo;
import cz.cvut.kbss.reporting.dto.reportlist.ReportDto;
import cz.cvut.kbss.reporting.environment.generator.Generator;
import cz.cvut.kbss.reporting.environment.generator.OccurrenceReportGenerator;
import cz.cvut.kbss.reporting.environment.util.Environment;
import cz.cvut.kbss.reporting.environment.util.ReportRevisionComparator;
import cz.cvut.kbss.reporting.exception.NotFoundException;
import cz.cvut.kbss.reporting.exception.ValidationException;
import cz.cvut.kbss.reporting.filter.OccurrenceCategoryFilter;
import cz.cvut.kbss.reporting.filter.ReportFilter;
import cz.cvut.kbss.reporting.filter.ReportKeyFilter;
import cz.cvut.kbss.reporting.model.*;
import cz.cvut.kbss.reporting.persistence.PersistenceException;
import cz.cvut.kbss.reporting.rest.handler.ErrorInfo;
import cz.cvut.kbss.reporting.service.ReportBusinessService;
import cz.cvut.kbss.reporting.service.data.AttachmentService;
import cz.cvut.kbss.reporting.service.factory.OccurrenceReportFactory;
import cz.cvut.kbss.reporting.util.Constants;
import cz.cvut.kbss.reporting.util.IdentificationUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MvcResult;

import java.io.File;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReportControllerTest extends BaseControllerTestRunner {

    private static final String REPORTS_PATH = "/reports/";

    @Mock
    private ReportBusinessService reportServiceMock;

    @Mock
    private OccurrenceReportFactory reportFactoryMock;

    @Mock
    private AttachmentService attachmentServiceMock;

    @InjectMocks
    private ReportController controller;

    private Person author;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        super.setUp(controller);
        this.author = Generator.getPerson();
        Environment.setCurrentUser(author);
    }

    @Test
    public void getAllReportsReturnsEmptyCollectionWhenThereAreNoReports() throws Exception {
        when(reportServiceMock.findAll(any(Pageable.class), anyCollection())).thenReturn(Page.empty());
        final MvcResult result = mockMvc.perform(get("/reports").accept(MediaType.APPLICATION_JSON_VALUE))
                                        .andExpect(status().isOk()).andReturn();
        final List<ReportDto> res = objectMapper
                .readValue(result.getResponse().getContentAsByteArray(), new TypeReference<List<ReportDto>>() {
                });
        assertNotNull(res);
        assertTrue(res.isEmpty());
    }

    @Test
    public void getAllReportsPassesReportKeysToServiceWhenTheyAreSpecifiedInRequest() throws Exception {
        final List<String> keys = IntStream.range(0, Generator.randomInt(5, 10))
                                           .mapToObj(i -> IdentificationUtils.generateKey())
                                           .collect(Collectors.toList());
        final List<ReportDto> reports = keys.stream().map(k -> {
            final OccurrenceReport r = OccurrenceReportGenerator.generateOccurrenceReport(true);
            r.setUri(Generator.generateUri());
            r.setKey(k);
            return r.toReportDto();
        }).collect(Collectors.toList());
        when(reportServiceMock.findAll(any(Pageable.class), anyCollection())).thenReturn(new PageImpl<>(reports));

        final MvcResult result = mockMvc.perform(get("/reports").param("key", keys.toArray(new String[keys.size()])))
                                        .andExpect(status().isOk()).andReturn();
        final List<ReportDto> res = readValue(result, new TypeReference<List<ReportDto>>() {
        });
        assertNotNull(res);
        assertTrue(Environment.areEqual(reports, res));
        final ArgumentCaptor<Collection<ReportFilter>> captor = ArgumentCaptor.forClass(Collection.class);
        verify(reportServiceMock).findAll(any(Pageable.class), captor.capture());
        assertEquals(1, captor.getValue().size());
        assertEquals(ReportFilter.create(ReportKeyFilter.KEY, keys).get(), captor.getValue().iterator().next());
    }

    @Test
    public void getReportReturnsNotFoundForUnknownKey() throws Exception {
        final String key = "unknownKey";
        when(reportServiceMock.findByKey(key)).thenReturn(null);
        mockMvc.perform(get(REPORTS_PATH + key)).andExpect(status().isNotFound());
    }

    @Test
    public void testGetReportForOccurrenceReport() throws Exception {
        final OccurrenceReport report = OccurrenceReportGenerator.generateOccurrenceReport(true);
        report.getOccurrence().setUri(URI.create(Vocabulary.s_c_Occurrence + "#32145"));
        report.setKey(IdentificationUtils.generateKey());
        report.setUri(URI.create(Vocabulary.s_c_occurrence_report + "#instance12345"));
        when(reportServiceMock.findByKey(report.getKey())).thenReturn(report);
        final MvcResult result = mockMvc.perform(get(REPORTS_PATH + report.getKey())).andExpect(status().isOk())
                                        .andReturn();
        final OccurrenceReportDto res = readValue(result, OccurrenceReportDto.class);
        assertNotNull(res);
        assertEquals(report.getUri(), res.getUri());
        assertEquals(report.getKey(), res.getKey());
    }

    @Test
    public void testGetLatestRevisionForOccurrenceReport() throws Exception {
        final OccurrenceReport latestRevision = OccurrenceReportGenerator.generateOccurrenceReport(true);
        latestRevision.setRevision(Generator.randomInt(10));
        when(reportServiceMock.findLatestRevision(latestRevision.getFileNumber())).thenReturn(latestRevision);
        final MvcResult result = mockMvc.perform(get(REPORTS_PATH + "chain/" + latestRevision.getFileNumber()))
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
        mockMvc.perform(get(REPORTS_PATH + "chain/" + fileNumber)).andExpect(status().isNotFound());
    }

    @Test
    public void testGetReportChainRevisions() throws Exception {
        final List<OccurrenceReport> chain = OccurrenceReportGenerator.generateOccurrenceReportChain(author);
        chain.sort(new ReportRevisionComparator<>());  // sort by revision descending
        final Long fileNumber = chain.get(0).getFileNumber();
        final List<ReportRevisionInfo> revisions = new ArrayList<>(chain.size());
        for (int i = 0; i < chain.size(); i++) {
            final OccurrenceReport r = chain.get(i);
            r.setUri(URI.create(Vocabulary.s_c_occurrence_report + "#instance-" + i));
            r.setKey(IdentificationUtils.generateKey());
            final ReportRevisionInfo revision = new ReportRevisionInfo();
            revision.setUri(r.getUri());
            revision.setRevision(r.getRevision());
            revision.setKey(r.getKey());
            revision.setCreated(r.getDateCreated());
            revisions.add(revision);
        }
        when(reportServiceMock.getReportChainRevisions(fileNumber)).thenReturn(revisions);
        final MvcResult result = mockMvc.perform(get(REPORTS_PATH + "chain/" + fileNumber + "/revisions"))
                                        .andExpect(status().isOk()).andReturn();
        final List<ReportRevisionInfo> res = readValue(result, new TypeReference<List<ReportRevisionInfo>>() {
        });
        assertNotNull(res);
        assertEquals(revisions, res);
    }

    @Test
    public void getReportChainRevisionsThrowsNotFoundForUnknownReportChainIdentifier() throws Exception {
        final Long fileNumber = Long.MAX_VALUE;
        when(reportServiceMock.getReportChainRevisions(fileNumber)).thenReturn(Collections.emptyList());
        mockMvc.perform(get(REPORTS_PATH + "chain/" + fileNumber + "/revisions")).andExpect(status().isNotFound());
        verify(reportServiceMock).getReportChainRevisions(fileNumber);
    }

    @Test
    public void testGetOccurrenceReportRevisionByChainIdentifierAndRevisionNumber() throws Exception {
        final List<OccurrenceReport> chain = OccurrenceReportGenerator.generateOccurrenceReportChain(author);
        chain.forEach(r -> {
            r.setUri(URI.create(Vocabulary.s_c_occurrence_report + "#instance-" + Generator.randomInt()));
            r.setKey(IdentificationUtils.generateKey());
        });
        final OccurrenceReport report = chain.get(Generator.randomInt(chain.size()) - 1);
        when(reportServiceMock.findRevision(report.getFileNumber(), report.getRevision())).thenReturn(report);
        final MvcResult result = mockMvc
                .perform(get(REPORTS_PATH + "chain/" + report.getFileNumber() + "/revisions/" + report.getRevision()))
                .andExpect(status().isOk()).andReturn();
        final OccurrenceReportDto res = readValue(result, OccurrenceReportDto.class);
        assertNotNull(res);
        assertEquals(report.getFileNumber(), res.getFileNumber());
        assertEquals(report.getRevision(), res.getRevision());
        assertEquals(report.getUri(), res.getUri());
    }

    @Test
    public void getRevisionThrowsNotFoundWhenRevisionIsNotFound() throws Exception {
        final Long fileNumber = 12345L;
        final Integer revision = 3;
        when(reportServiceMock.findRevision(fileNumber, revision)).thenReturn(null);

        mockMvc.perform(get(REPORTS_PATH + "chain/" + fileNumber + "/revisions/" + revision))
               .andExpect(status().isNotFound());
    }

    @Test
    public void createReportReturnsLocationOfNewInstance() throws Exception {
        final OccurrenceReport report = OccurrenceReportGenerator.generateOccurrenceReport(false);
        final String key = "117";
        doAnswer(call -> {
            final OccurrenceReport r = (OccurrenceReport) call.getArguments()[0];
            r.setKey(key);
            return null;
        }).when(reportServiceMock).persist(any(OccurrenceReport.class));

        final MvcResult result = mockMvc
                .perform(post("/reports").content(toJson(dtoMapper.occurrenceReportToOccurrenceReportDto(report)))
                                         .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        verifyLocationEquals(REPORTS_PATH + key, result);
        verify(reportServiceMock).persist(any(OccurrenceReport.class));
    }

    @Test
    public void createReportReturnsValidationExceptionThrownByServiceAsResponse() throws Exception {
        final OccurrenceReport report = OccurrenceReportGenerator.generateOccurrenceReport(false);
        Mockito.doThrow(new ValidationException("Invalid report.")).when(reportServiceMock)
               .persist(any(OccurrenceReport.class));
        mockMvc.perform(post("/reports").content(toJson(dtoMapper.occurrenceReportToOccurrenceReportDto(report)))
                                        .contentType(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isConflict());
        verify(reportServiceMock).persist(any(OccurrenceReport.class));
    }

    @Test
    public void createNewRevisionReturnsLocationOfNewRevision() throws Exception {
        final List<OccurrenceReport> chain = OccurrenceReportGenerator.generateOccurrenceReportChain(author);
        final Long fileNumber = chain.get(0).getFileNumber();
        chain.sort(new ReportRevisionComparator<>());  // Sort descending
        final OccurrenceReport newRevision = new OccurrenceReport();
        newRevision.setFileNumber(fileNumber);
        newRevision.setKey(IdentificationUtils.generateKey());
        newRevision.setRevision(chain.get(0).getRevision() + 1);
        when(reportServiceMock.createNewRevision(fileNumber)).thenReturn(newRevision);

        final MvcResult result = mockMvc.perform(post(REPORTS_PATH + "chain/" + fileNumber + "/revisions"))
                                        .andExpect(status().isCreated()).andReturn();
        verifyLocationEquals(REPORTS_PATH + newRevision.getKey(), result);
    }

    @Test
    public void createNewRevisionReturnsNotFoundThrownByServiceAsResponse() throws Exception {
        final Long fileNumber = Long.MAX_VALUE;
        when(reportServiceMock.createNewRevision(fileNumber))
                .thenThrow(NotFoundException.create("Report chain", fileNumber));
        mockMvc.perform(post(REPORTS_PATH + "chain/" + fileNumber + "/revisions")).andExpect(status().isNotFound());
        verify(reportServiceMock).createNewRevision(fileNumber);
    }

    @Test
    public void updateReportPassesReportToServiceForUpdate() throws Exception {
        final OccurrenceReport report = prepareReport();
        mockMvc.perform(
                put(REPORTS_PATH + report.getKey())
                        .content(toJson(dtoMapper.occurrenceReportToOccurrenceReportDto(report)))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isNoContent());
        final ArgumentCaptor<OccurrenceReport> captor = ArgumentCaptor.forClass(OccurrenceReport.class);
        verify(reportServiceMock).update(captor.capture());
        final OccurrenceReport argument = captor.getValue();
        assertNotNull(argument);
        assertEquals(report.getUri(), argument.getUri());
        assertEquals(report.getKey(), argument.getKey());
    }

    private OccurrenceReport prepareReport() {
        final OccurrenceReport report = OccurrenceReportGenerator.generateOccurrenceReport(false);
        report.setUri(URI.create(Vocabulary.s_c_occurrence_report + "#instance"));
        report.setKey(IdentificationUtils.generateKey());
        when(reportServiceMock.findByKey(report.getKey())).thenReturn(report);
        return report;
    }

    @Test
    public void updateReportThrowsBadRequestWhenKeyInPathDoesNotMatchReportKey() throws Exception {
        final OccurrenceReport report = OccurrenceReportGenerator.generateOccurrenceReport(false);
        report.setUri(URI.create(Vocabulary.s_c_occurrence_report + "#instance"));
        report.setKey(IdentificationUtils.generateKey());
        final String otherKey = IdentificationUtils.generateKey();
        mockMvc.perform(
                put(REPORTS_PATH + otherKey).content(toJson(dtoMapper.occurrenceReportToOccurrenceReportDto(report)))
                                            .contentType(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isBadRequest());
    }

    @Test
    public void updateReportThrowsNotFoundForUnknownReportKey() throws Exception {
        final OccurrenceReport report = OccurrenceReportGenerator.generateOccurrenceReport(false);
        report.setUri(URI.create(Vocabulary.s_c_occurrence_report + "#instance"));
        report.setKey(IdentificationUtils.generateKey());
        when(reportServiceMock.findByKey(report.getKey())).thenReturn(null);
        mockMvc.perform(
                put(REPORTS_PATH + report.getKey())
                        .content(toJson(dtoMapper.occurrenceReportToOccurrenceReportDto(report)))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isNotFound());
        verify(reportServiceMock).findByKey(report.getKey());
        verify(reportServiceMock, never()).update(any(OccurrenceReport.class));
    }

    @Test
    public void updateReportReturnsValidationExceptionAsResponseWhenUpdateValidationFails() throws Exception {
        final OccurrenceReport report = prepareReport();
        doThrow(new ValidationException("Invalid report.")).when(reportServiceMock).update(any(OccurrenceReport.class));
        mockMvc.perform(
                put(REPORTS_PATH + report.getKey())
                        .content(toJson(dtoMapper.occurrenceReportToOccurrenceReportDto(report)))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isConflict());
    }

    @Test
    public void transitionToNextPhaseCallsService() throws Exception {
        final OccurrenceReport report = prepareReport();
        mockMvc.perform(put(REPORTS_PATH + report.getKey() + "/phase")).andExpect(status().isNoContent());
        verify(reportServiceMock).transitionToNextPhase(report);
    }

    @Test
    public void removeChainCallsService() throws Exception {
        final Long fileNumber = IdentificationUtils.generateFileNumber();
        mockMvc.perform(delete(REPORTS_PATH + "chain/" + fileNumber)).andExpect(status().isNoContent());
        verify(reportServiceMock).removeReportChain(fileNumber);
    }

    @Test
    public void persistenceExceptionIsWrappedInJsonObjectWithReadableMessage() throws Exception {
        final OccurrenceReport report = prepareReport();
        final String message = "Expected some value in attribute blabla, but found none.";
        Mockito.doThrow(new PersistenceException(new RollbackException(message))).when(reportServiceMock)
               .persist(any(OccurrenceReport.class));
        final MvcResult result = mockMvc.perform(
                post(REPORTS_PATH).content(toJson(dtoMapper.occurrenceReportToOccurrenceReportDto(report)))
                                  .contentType(MediaType.APPLICATION_JSON_VALUE))
                                        .andExpect(status().isInternalServerError()).andReturn();
        final ErrorInfo errorInfo = readValue(result, ErrorInfo.class);
        assertEquals(message, errorInfo.getMessage());
    }

    @Test
    public void createFromInitialReturnsNewOccurrenceReportInstance() throws Exception {
        final InitialReport initialReport = OccurrenceReportGenerator.generateInitialReport();
        final OccurrenceReport newReport = new OccurrenceReport();
        newReport.setInitialReport(initialReport);
        newReport.setOccurrence(new Occurrence());
        when(reportFactoryMock.createFromInitialReport(any())).thenReturn(newReport);
        final MvcResult result = mockMvc.perform(post(REPORTS_PATH + "initial").content(toJson(initialReport))
                                                                               .contentType(
                                                                                       MediaType.APPLICATION_JSON_VALUE))
                                        .andExpect(status().isOk()).andReturn();
        final OccurrenceReportDto report = readValue(result, OccurrenceReportDto.class);
        assertNotNull(report);
        assertNotNull(report.getInitialReport());
        assertEquals(initialReport.getDescription(), report.getInitialReport().getDescription());
        verify(reportFactoryMock).createFromInitialReport(any());
    }

    @Test
    public void createFromInitialReturnsBadRequestWhenInitialReportIsMissing() throws Exception {
        mockMvc.perform(post(REPORTS_PATH + "initial").contentType(MediaType.APPLICATION_JSON_VALUE))
               .andExpect(status().isBadRequest());
    }

    @Test
    public void findAllExtractsFiltersFromQueryParameters() throws Exception {
        final List<String> cats = IntStream.range(0, 5).mapToObj(i -> Generator.generateEventType().toString()).collect(
                Collectors.toList());
        when(reportServiceMock.findAll(any(Pageable.class), anyCollection()))
                .thenReturn(new PageImpl<>(Collections.emptyList()));
        mockMvc.perform(get(REPORTS_PATH).param(OccurrenceCategoryFilter.KEY, cats.toArray(new String[cats.size()])))
               .andExpect(status().isOk());
        final ReportFilter expectedFilter = ReportFilter.create(OccurrenceCategoryFilter.KEY, cats).get();
        verify(reportServiceMock).findAll(any(Pageable.class), eq(Collections.singletonList(expectedFilter)));
    }

    @Test
    public void findAllBuildsPageRequestFromQueryParameters() throws Exception {
        final int pageNo = 0;
        final int pageSize = 10;
        when(reportServiceMock.findAll(any(Pageable.class), anyCollection()))
                .thenReturn(new PageImpl<>(Collections.emptyList()));
        mockMvc.perform(get(REPORTS_PATH).param(Constants.PAGE, Integer.toString(pageNo))
                                         .param(Constants.PAGE_SIZE, Integer.toString(pageSize)))
               .andExpect(status().isOk());
        final ArgumentCaptor<Collection<ReportFilter>> captor = ArgumentCaptor.forClass(Collection.class);
        verify(reportServiceMock).findAll(eq(PageRequest.of(pageNo, pageSize)), captor.capture());
        assertTrue(captor.getValue().isEmpty());
    }

    @Test
    public void getReportSupportsJsonLdMediaType() throws Exception {
        final OccurrenceReport report = OccurrenceReportGenerator.generateOccurrenceReport(true);
        report.getOccurrence().setUri(URI.create(Vocabulary.s_c_Occurrence + "#32145"));
        report.setKey(IdentificationUtils.generateKey());
        report.setUri(URI.create(Vocabulary.s_c_occurrence_report + "#instance12345"));
        when(reportServiceMock.findByKey(report.getKey())).thenReturn(report);
        final MvcResult mvcResult = mockMvc.perform(get(REPORTS_PATH + report.getKey()).accept(JsonLd.MEDIA_TYPE))
                                           .andExpect(status().isOk())
                                           .andReturn();
        final Object result = JsonUtils.fromString(mvcResult.getResponse().getContentAsString());
        assertTrue(result instanceof Map);
        final Map<?, ?> mResult = (Map<?, ?>) result;
        assertEquals(report.getUri().toString(), mResult.get(JsonLd.ID));
        assertTrue(mResult.containsKey(Vocabulary.s_p_documents));
        assertTrue(mResult.containsKey(Vocabulary.s_p_has_author));
    }

    @Ignore
    @Test
    public void createReportSupportsJsonLdMediaType() throws Exception {
        final String key = "117";
        doAnswer(call -> {
            final OccurrenceReport r = (OccurrenceReport) call.getArguments()[0];
            r.setKey(key);
            return null;
        }).when(reportServiceMock).persist(any(OccurrenceReport.class));

        final MvcResult result = mockMvc.perform(
                post("/reports").content(Environment.loadData("data/occurrenceReportJsonLd.json", String.class))
                                .contentType(JsonLd.MEDIA_TYPE)).andReturn();
        verifyLocationEquals(REPORTS_PATH + key, result);
        verify(reportServiceMock).persist(any(OccurrenceReport.class));
    }

    @Test
    public void getAttachmentRetrievesAttachmentFile() throws Exception {
        final OccurrenceReport report = OccurrenceReportGenerator.generateOccurrenceReport(true);
        report.setKey(IdentificationUtils.generateKey());
        when(reportServiceMock.findByKey(report.getKey())).thenReturn(report);
        final File attachment = generateBinaryFile();
        when(attachmentServiceMock.getAttachment(report, attachment.getName())).thenReturn(attachment);

        final MvcResult mvcResult = mockMvc
                .perform(get(REPORTS_PATH + report.getKey() + "/attachments/" + attachment.getName()))
                .andExpect(status().isOk())
                .andReturn();
        final String disposition = mvcResult.getResponse().getHeader(HttpHeaders.CONTENT_DISPOSITION);
        assertThat(disposition, containsString(attachment.getName()));
        final byte[] result = mvcResult.getResponse().getContentAsByteArray();
        assertTrue(Arrays.equals(Environment.DATA.getBytes(), result));
    }

    private File generateBinaryFile() throws Exception {
        final File file = File.createTempFile("attachment", ".jpg");
        file.deleteOnExit();
        Files.write(file.toPath(), Environment.DATA.getBytes(), StandardOpenOption.APPEND);
        return file;
    }

    @Test
    public void getAttachmentThrowsNotFoundForUnknownReportKey() throws Exception {
        final String unknownKey = "12345";
        final String fileName = "image.jpg";
        when(reportServiceMock.findByKey(unknownKey)).thenReturn(null);
        mockMvc.perform(get(REPORTS_PATH + unknownKey + "/attachments/" + fileName))
               .andExpect(status().isNotFound());
        verify(attachmentServiceMock, never()).getAttachment(any(), anyString());
    }

    @Test
    public void addAttachmentAddsAttachmentToReport() throws Exception {
        final OccurrenceReport report = OccurrenceReportGenerator.generateOccurrenceReport(true);
        report.setKey(IdentificationUtils.generateKey());
        when(reportServiceMock.findByKey(report.getKey())).thenReturn(report);
        final File attachment = generateBinaryFile();

        final MockMultipartFile upload = new MockMultipartFile("file", attachment.getName(), MediaType.IMAGE_JPEG_VALUE,
                Environment.DATA.getBytes());
        mockMvc.perform(fileUpload(REPORTS_PATH + report.getKey() + "/attachments").file(upload))
               .andExpect(status().isCreated());
        verify(attachmentServiceMock).addAttachment(eq(report), eq(attachment.getName()), notNull());
    }

    @Test
    public void addAttachmentReturnsAttachmentLocationInHeader() throws Exception {
        final OccurrenceReport report = OccurrenceReportGenerator.generateOccurrenceReport(true);
        report.setKey(IdentificationUtils.generateKey());
        when(reportServiceMock.findByKey(report.getKey())).thenReturn(report);
        final File attachment = generateBinaryFile();

        final MockMultipartFile upload = new MockMultipartFile("file", attachment.getName(), MediaType.IMAGE_JPEG_VALUE,
                Environment.DATA.getBytes());
        final MvcResult mvcResult = mockMvc
                .perform(fileUpload(REPORTS_PATH + report.getKey() + "/attachments").file(upload))
                .andExpect(status().isCreated()).andReturn();
        verifyLocationEquals(REPORTS_PATH + report.getKey() + "/attachments/" + attachment.getName(), mvcResult);
    }

    @Test
    public void addAttachmentThrowsNotFoundExceptionForUnknownReportKey() throws Exception {
        final String reportKey = "12345";
        when(reportServiceMock.findByKey(reportKey)).thenReturn(null);
        final File attachment = generateBinaryFile();

        final MockMultipartFile upload = new MockMultipartFile("file", attachment.getName(), MediaType.IMAGE_JPEG_VALUE,
                Environment.DATA.getBytes());
        mockMvc.perform(fileUpload(REPORTS_PATH + reportKey + "/attachments").file(upload))
               .andExpect(status().isNotFound());
        verify(attachmentServiceMock, never()).addAttachment(any(), eq(attachment.getName()), any());
    }
}