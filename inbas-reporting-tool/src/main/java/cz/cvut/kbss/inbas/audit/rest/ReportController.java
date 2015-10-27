package cz.cvut.kbss.inbas.audit.rest;

import cz.cvut.kbss.inbas.audit.exception.InvalidReportException;
import cz.cvut.kbss.inbas.audit.model.reports.OccurrenceReport;
import cz.cvut.kbss.inbas.audit.rest.dto.mapper.ReportMapper;
import cz.cvut.kbss.inbas.audit.rest.dto.model.OccurrenceReportDto;
import cz.cvut.kbss.inbas.audit.rest.dto.model.OccurrenceReportInfo;
import cz.cvut.kbss.inbas.audit.rest.exceptions.NotFoundException;
import cz.cvut.kbss.inbas.audit.service.OccurrenceReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author ledvima1
 */
@RestController
@RequestMapping("/reports")
public class ReportController extends BaseController {

    @Autowired
    private OccurrenceReportService occurrenceReportService;

    @Autowired
    private ReportMapper reportMapper;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<OccurrenceReportInfo> getAllReports() {
        final Collection<OccurrenceReport> reports = occurrenceReportService.findAll();
        return reports.stream().map(reportMapper::occurrenceReportToOccurrenceReportInfo).collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{key}", produces = MediaType.APPLICATION_JSON_VALUE)
    public OccurrenceReportDto getReport(@PathVariable("key") String key) {
        return reportMapper.occurrenceReportToOccurrenceReportDto(getOccurrenceReport(key));
    }

    private OccurrenceReport getOccurrenceReport(String key) {
        final OccurrenceReport original = occurrenceReportService.findByKey(key);
        if (original == null) {
            throw NotFoundException.create("Report", key);
        }
        return original;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void createReport(@RequestBody OccurrenceReportDto report) {
        final OccurrenceReport occurrenceReport = reportMapper.occurrenceReportDtoToOccurrenceReport(report);
        assert occurrenceReport != null;
        occurrenceReportService.persist(occurrenceReport);
        if (LOG.isTraceEnabled()) {
            LOG.trace("Created report from data {}", report);
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(method = RequestMethod.PUT, value = "/{key}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateReport(@PathVariable("key") String key, @RequestBody OccurrenceReportDto report) {
        final OccurrenceReport original = getOccurrenceReport(key);
        final OccurrenceReport update = reportMapper.occurrenceReportDtoToOccurrenceReport(report);
        validateReportForUpdate(original, update);
        occurrenceReportService.update(update);
        if (LOG.isTraceEnabled()) {
            LOG.trace("Updated report {}", report);
        }
    }

    private void validateReportForUpdate(OccurrenceReport original, OccurrenceReport update) {
        if (!original.getUri().equals(update.getUri())) {
            throw new InvalidReportException(
                    "The updated report URI " + update.getUri() + " is different from the original URI " + original
                            .getUri() + "!");
        }
        if (!original.getKey().equals(update.getKey())) {
            throw new InvalidReportException(
                    "The updated report key " + update.getKey() + " is different from the original key " + original
                            .getKey());
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(method = RequestMethod.DELETE, value = "/{key}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReport(@PathVariable("key") String key) {
        final OccurrenceReport report = getOccurrenceReport(key);
        occurrenceReportService.remove(report);
        if (LOG.isTraceEnabled()) {
            LOG.trace("Deleted report {}.", report.getUri());
        }
    }
}
