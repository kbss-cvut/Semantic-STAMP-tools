package cz.cvut.kbss.inbas.audit.rest;

import cz.cvut.kbss.inbas.audit.exceptions.InvalidReportException;
import cz.cvut.kbss.inbas.audit.rest.dto.factory.DtoFactory;
import cz.cvut.kbss.inbas.audit.rest.dto.model.OccurrenceReport;
import cz.cvut.kbss.inbas.audit.rest.exceptions.NotFoundException;
import cz.cvut.kbss.inbas.audit.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    private ReportService reportService;

    @Autowired
    private DtoFactory dtoFactory;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<OccurrenceReport> getAllReports() {
        final Collection<cz.cvut.kbss.inbas.audit.model.reports.OccurrenceReport> reports = reportService.findAll();
        if (reports.isEmpty()) {
            throw new NotFoundException("No reports found.");
        }
        return reports.stream().map(dtoFactory::toDto).collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{key}", produces = MediaType.APPLICATION_JSON_VALUE)
    public OccurrenceReport getReport(@PathVariable("key") String key) {
        return dtoFactory.toDto(getOccurrenceReport(key));
    }

    private cz.cvut.kbss.inbas.audit.model.reports.OccurrenceReport getOccurrenceReport(String key) {
        final cz.cvut.kbss.inbas.audit.model.reports.OccurrenceReport original = reportService.findByKey(key);
        if (original == null) {
            throw NotFoundException.create("Report", key);
        }
        return original;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void createReport(@RequestBody OccurrenceReport report) {
        final cz.cvut.kbss.inbas.audit.model.reports.OccurrenceReport occurrenceReport = dtoFactory.toDomainModel(report);
        assert occurrenceReport != null;
        reportService.persist(occurrenceReport);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Created report from data {}", report);
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{key}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateReport(@PathVariable("key") String key, @RequestBody OccurrenceReport report) {
        final cz.cvut.kbss.inbas.audit.model.reports.OccurrenceReport original = getOccurrenceReport(key);
        final cz.cvut.kbss.inbas.audit.model.reports.OccurrenceReport update = dtoFactory.toDomainModel(report);
        validateReportForUpdate(original, update);
        reportService.update(update);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Updated report {}", report);
        }
    }

    private void validateReportForUpdate(cz.cvut.kbss.inbas.audit.model.reports.OccurrenceReport original, cz.cvut.kbss.inbas.audit.model.reports.OccurrenceReport update) {
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

    @RequestMapping(method = RequestMethod.DELETE, value = "/{key}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReport(@PathVariable("key") String key) {
        final cz.cvut.kbss.inbas.audit.model.reports.OccurrenceReport report = getOccurrenceReport(key);
        reportService.remove(report);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Deleted report {}.", report.getUri());
        }
    }
}
