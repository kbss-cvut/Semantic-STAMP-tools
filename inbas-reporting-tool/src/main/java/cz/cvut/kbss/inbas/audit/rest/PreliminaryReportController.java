package cz.cvut.kbss.inbas.audit.rest;

import cz.cvut.kbss.inbas.audit.dto.PreliminaryReportDto;
import cz.cvut.kbss.inbas.audit.dto.ReportRevisionInfo;
import cz.cvut.kbss.inbas.audit.exception.ValidationException;
import cz.cvut.kbss.inbas.audit.model.Occurrence;
import cz.cvut.kbss.inbas.audit.model.reports.OccurrenceReport;
import cz.cvut.kbss.inbas.audit.model.reports.PreliminaryReport;
import cz.cvut.kbss.inbas.audit.rest.dto.mapper.ReportMapper;
import cz.cvut.kbss.inbas.audit.rest.exceptions.NotFoundException;
import cz.cvut.kbss.inbas.audit.rest.util.RestUtils;
import cz.cvut.kbss.inbas.audit.service.OccurrenceReportService;
import cz.cvut.kbss.inbas.audit.service.OccurrenceService;
import cz.cvut.kbss.inbas.audit.service.PreliminaryReportService;
import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/preliminaryReports")
public class PreliminaryReportController extends BaseController {

    @Autowired
    private PreliminaryReportService preliminaryReportService;

    @Autowired
    private OccurrenceReportService occurrenceReportService;

    @Autowired
    private OccurrenceService occurrenceService;

    @Autowired
    private ReportMapper reportMapper;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<OccurrenceReport> getAllReports() {
        return occurrenceReportService.findAll(Vocabulary.PreliminaryReport);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{key}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PreliminaryReportDto getReport(@PathVariable("key") String key) {
        return reportMapper.preliminaryReportToPreliminaryReportDto(getOccurrenceReport(key));
    }

    private PreliminaryReport getOccurrenceReport(String key) {
        final PreliminaryReport original = preliminaryReportService.findByKey(key);
        if (original == null) {
            throw NotFoundException.create("Report", key);
        }
        return original;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void createReport(@RequestBody PreliminaryReportDto report) {
        final PreliminaryReport preliminaryReport = reportMapper.preliminaryReportDtoToPreliminaryReport(report);
        assert preliminaryReport != null;
        preliminaryReportService.persist(preliminaryReport);
        if (LOG.isTraceEnabled()) {
            LOG.trace("Created report from data {}", report);
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{key}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateReport(@PathVariable("key") String key, @RequestBody PreliminaryReportDto report) {
        final PreliminaryReport original = getOccurrenceReport(key);
        final PreliminaryReport update = reportMapper.preliminaryReportDtoToPreliminaryReport(report);
        validateReportForUpdate(original, update);
        preliminaryReportService.update(update);
        if (LOG.isTraceEnabled()) {
            LOG.trace("Updated report {}", report);
        }
    }

    private void validateReportForUpdate(PreliminaryReport original, PreliminaryReport update) {
        if (!original.getUri().equals(update.getUri())) {
            throw new ValidationException(
                    "The updated report URI " + update.getUri() + " is different from the original URI " + original
                            .getUri() + "!");
        }
        if (!original.getKey().equals(update.getKey())) {
            throw new ValidationException(
                    "The updated report key " + update.getKey() + " is different from the original key " + original
                            .getKey());
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{key}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReport(@PathVariable("key") String key) {
        final PreliminaryReport report = getOccurrenceReport(key);
        preliminaryReportService.remove(report);
        if (LOG.isTraceEnabled()) {
            LOG.trace("Deleted report {}.", report.getUri());
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{key}/revisions")
    public ResponseEntity<Void> createNewRevision(@PathVariable("key") String key) {
        final PreliminaryReport report = getOccurrenceReport(key);
        if (LOG.isTraceEnabled()) {
            LOG.trace("Creating new revision of report {}", report);
        }
        final PreliminaryReport newRevision = preliminaryReportService.createNewRevision(report);
        final HttpHeaders headers = RestUtils.createLocationHeader("{key}", newRevision.getKey());
        final String location = headers.getLocation().toString();
        headers.set(HttpHeaders.LOCATION, location.replace(key + "/revisions", ""));
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    /**
     * Gets occurrence report revision list.
     *
     * @param occurrenceKey Occurrence key
     * @return List of occurrence reports
     */
    @RequestMapping(method = RequestMethod.GET, value = "/revisions/{key}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ReportRevisionInfo> getOccurrenceReportRevisions(@PathVariable("key") String occurrenceKey) {
        final Occurrence occurrence = occurrenceService.findByKey(occurrenceKey);
        if (occurrence == null) {
            throw NotFoundException.create("Occurrence", occurrenceKey);
        }
        return preliminaryReportService.getRevisionsForOccurrence(occurrence);
    }
}
