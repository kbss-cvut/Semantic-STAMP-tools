package cz.cvut.kbss.reporting.rest;

import cz.cvut.kbss.jsonld.JsonLd;
import cz.cvut.kbss.reporting.dto.OccurrenceReportDto;
import cz.cvut.kbss.reporting.dto.ReportRevisionInfo;
import cz.cvut.kbss.reporting.dto.reportlist.ReportList;
import cz.cvut.kbss.reporting.exception.AttachmentException;
import cz.cvut.kbss.reporting.exception.NotFoundException;
import cz.cvut.kbss.reporting.filter.ReportFilter;
import cz.cvut.kbss.reporting.model.AbstractReport;
import cz.cvut.kbss.reporting.model.InitialReport;
import cz.cvut.kbss.reporting.model.LogicalDocument;
import cz.cvut.kbss.reporting.rest.dto.mapper.DtoMapper;
import cz.cvut.kbss.reporting.rest.exception.BadRequestException;
import cz.cvut.kbss.reporting.rest.util.RestUtils;
import cz.cvut.kbss.reporting.service.ReportBusinessService;
import cz.cvut.kbss.reporting.service.data.AttachmentService;
import cz.cvut.kbss.reporting.service.data.export.ReportExporter;
import cz.cvut.kbss.reporting.service.factory.OccurrenceReportFactory;
import cz.cvut.kbss.reporting.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static cz.cvut.kbss.reporting.util.Constants.DEFAULT_PAGE_SPEC;

@RestController
@RequestMapping("/reports")
public class ReportController extends BaseController {

    private final ReportBusinessService reportService;

    private final DtoMapper dtoMapper;

    private final OccurrenceReportFactory reportFactory;

    private final ReportExporter reportExporter;

    private final AttachmentService attachmentService;

    @Autowired
    public ReportController(@Qualifier("cachingReportBusinessService") ReportBusinessService reportService,
                            DtoMapper dtoMapper, OccurrenceReportFactory reportFactory, ReportExporter reportExporter,
                            AttachmentService attachmentService) {
        this.reportService = reportService;
        this.dtoMapper = dtoMapper;
        this.reportFactory = reportFactory;
        this.reportExporter = reportExporter;
        this.attachmentService = attachmentService;
    }

    @RequestMapping(method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE, JsonLd.MEDIA_TYPE})
    public ReportList getAllReports(@RequestParam(name = Constants.PAGE, required = false) Integer page,
                                    @RequestParam(name = Constants.PAGE_SIZE, required = false) Integer pageSize,
                                    @RequestParam MultiValueMap<String, String> params) {
        return new ReportList(
                reportService.findAll(buildPageRequest(page, pageSize), buildFilters(params)).getContent());
    }

    private Pageable buildPageRequest(Integer page, Integer pageSize) {
        return PageRequest.of(page != null ? page : DEFAULT_PAGE_SPEC.getPageNumber(),
                pageSize != null ? pageSize : DEFAULT_PAGE_SPEC.getPageSize());
    }

    private Collection<ReportFilter> buildFilters(MultiValueMap<String, String> reqParams) {
        final List<ReportFilter> filters = new ArrayList<>(reqParams.size());
        for (Map.Entry<String, List<String>> param : reqParams.entrySet()) {
            ReportFilter.create(param.getKey(), param.getValue()).ifPresent(filters::add);
        }
        return filters;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, JsonLd.MEDIA_TYPE})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> createReport(@RequestBody LogicalDocument reportDto) {
        final LogicalDocument report = dtoMapper.reportDtoToReport(reportDto);
        reportService.persist(report);

        LOG.trace("Report {} successfully persisted.", report);
        final String key = report.getKey();
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{key}", key);
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{key}", method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE, JsonLd.MEDIA_TYPE})
    public LogicalDocument getReport(@PathVariable("key") String key) {
        return dtoMapper.reportToReportDto(getReportInternal(key));
    }

    private AbstractReport getReportInternal(String key) {
        final AbstractReport report = reportService.findByKey(key);
        if (report == null) {
            throw NotFoundException.create("Occurrence report", key);
        }
        return report;
    }

    @RequestMapping(value = "/{key}", method = RequestMethod.PUT,
            consumes = {MediaType.APPLICATION_JSON_VALUE, JsonLd.MEDIA_TYPE})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateReport(@PathVariable("key") String key, @RequestBody LogicalDocument reportUpdate) {
        if (!key.equals(reportUpdate.getKey())) {
            throw new BadRequestException("The passed report's key is different from the specified one.");
        }
        final LogicalDocument report = dtoMapper.reportDtoToReport(reportUpdate);
        if (reportService.findByKey(key) == null) {
            throw NotFoundException.create("Report", key);
        }
        reportService.update(report);
        LOG.trace("Updated report {}.", report);
    }

    @RequestMapping(value = "/{key}/phase", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void transitionToNextPhase(@PathVariable("key") String key) {
        final LogicalDocument report = getReportInternal(key);
        reportService.transitionToNextPhase(report);
    }

    @RequestMapping(value = "/{key}/attachments", method = RequestMethod.POST)
    public ResponseEntity<Void> addAttachment(@PathVariable("key") String key,
                                              @RequestParam("file") MultipartFile attachment,
                                              @RequestParam(value = "description", required = false) String description) {
        final AbstractReport report = getReportInternal(key);
        try {
            reportService
                    .addAttachment(report, attachment.getOriginalFilename(), description, attachment.getInputStream());
        } catch (IOException e) {
            throw new AttachmentException("Unable to read file content from request.", e);
        }
        final HttpHeaders location = RestUtils
                .createLocationHeaderFromCurrentUri("/{name}", attachment.getOriginalFilename());
        return new ResponseEntity<>(location, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{key}/attachments/{name}", method = RequestMethod.GET)
    public ResponseEntity<FileSystemResource> getAttachment(@PathVariable("key") String key,
                                                            @PathVariable("name") String name,
                                                            HttpServletResponse response) {
        final AbstractReport report = reportService.findByKey(key);
        if (report == null) {
            // Can't use the regular NotFoundException mechanism, because Spring expects to return an image file, so
            // it is unable to return a JSON with error info
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + name);
        final File attachment = attachmentService.getAttachment(report, name);
        return new ResponseEntity<>(new FileSystemResource(attachment), HttpStatus.OK);
    }

    @RequestMapping(value = "/chain/{fileNumber}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeChain(@PathVariable("fileNumber") Long fileNumber) {
        reportService.removeReportChain(fileNumber);
    }

    @RequestMapping(value = "/chain/{fileNumber}", method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE, JsonLd.MEDIA_TYPE})
    public LogicalDocument findLatestRevision(@PathVariable("fileNumber") Long fileNumber) {
        final LogicalDocument report = reportService.findLatestRevision(fileNumber);
        if (report == null) {
            throw NotFoundException.create("Report chain", fileNumber);
        }
        return dtoMapper.reportToReportDto(report);
    }

    @RequestMapping(value = "/chain/{fileNumber}/revisions", method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE, JsonLd.MEDIA_TYPE})
    public List<ReportRevisionInfo> getReportChainRevisions(@PathVariable("fileNumber") Long fileNumber) {
        final List<ReportRevisionInfo> revisions = reportService.getReportChainRevisions(fileNumber);
        if (revisions.isEmpty()) {
            throw NotFoundException.create("Report chain", fileNumber);
        }
        return revisions;
    }

    /**
     * Creates new revision in the report chain (of the same type as the latest revision) or starts investigation (from
     * latest preliminary report).
     *
     * @param fileNumber Report chain identifier
     * @return Response with location header pointing to the new report
     */
    @RequestMapping(value = "/chain/{fileNumber}/revisions", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> createNewRevision(@PathVariable("fileNumber") Long fileNumber) {
        final LogicalDocument newRevision = reportService.createNewRevision(fileNumber);
        final HttpHeaders headers = RestUtils
                .createLocationHeaderFromContextPath("/reports/{key}", newRevision.getKey());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/chain/{fileNumber}/revisions/{revision}", method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE, JsonLd.MEDIA_TYPE})
    public LogicalDocument getRevision(@PathVariable("fileNumber") Long fileNumber,
                                       @PathVariable("revision") Integer revision) {
        final LogicalDocument report = reportService.findRevision(fileNumber, revision);
        if (report == null) {
            throw new NotFoundException(
                    "Report with revision " + revision + " not found in report chain with file number " + fileNumber +
                            " or the report chain does not exist.");
        }
        return dtoMapper.reportToReportDto(report);
    }

    /**
     * Creates a new occurrence report instance based on the specified {@link InitialReport}.
     *
     * @param initialReport The report to start from
     * @return New occurrence report (not persisted)
     */
    @RequestMapping(value = "/initial", method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_VALUE, JsonLd.MEDIA_TYPE},
            produces = {MediaType.APPLICATION_JSON_VALUE, JsonLd.MEDIA_TYPE})
    public OccurrenceReportDto createFromInitial(@RequestBody InitialReport initialReport) {
        return dtoMapper.occurrenceReportToOccurrenceReportDto(reportFactory.createFromInitialReport(initialReport));
    }

    /**
     * Export report with key to e5x xml
     *
     * @param key      Report key
     * @param response HTTP response object into which the exported XML will be written
     */
    @RequestMapping(value = "/{key}/export/e5xxml", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_XML_VALUE)
    public void exportReportToE5XXml(@PathVariable("key") String key, HttpServletResponse response) {
        exportReportToE5XImpl(key, response, false);
    }

    /**
     * Export report with key to e5x (zipped e5x xml)
     *
     * @param key      Report key
     * @param response HTTP response object into which the exported zipped XML will be written
     */
    @RequestMapping(value = "/{key}/export/e5x", method = RequestMethod.GET, produces = {"application/zip"})
    public void exportReportToE5X(@PathVariable("key") String key, HttpServletResponse response) {
        exportReportToE5XImpl(key, response, true);
    }

    /**
     * export the report with key as E5X.
     *
     * @param key      Report key
     * @param response Target HTTP response object
     * @param zip      true for e5x (zipped e5x xml), false for e5x xml
     */
    protected void exportReportToE5XImpl(String key, HttpServletResponse response, boolean zip) {
        String fileType = (zip ? "e5x" : "e5x xml");
        // transform report
        byte[] reportE5X = reportExporter.exportReportToE5X(key, zip);
        if (reportE5X == null) {
            LOG.trace("Cannot export {}. No such Occurrence Report with key {}.", fileType, key);
            throw NotFoundException.create("Occurrence Report", key);
        }
        if (zip) { // make response downloadable
            final LogicalDocument report = reportService.findByKey(key);
            response.setHeader("Content-Disposition", "attachment; filename=\"" + report.getFileNumber() + ".e5x\"");
        }
        try { // send the transformed to the response's output stream
            response.getOutputStream().write(reportE5X);
            response.flushBuffer();
            response.getOutputStream().close();
        } catch (IOException e) {
            String message = String
                    .format("IO error writing %s export of report with key %s to output stream", fileType, key);
            LOG.warn(message, e);
            throw new RuntimeException(message, e);
        }
    }
}
