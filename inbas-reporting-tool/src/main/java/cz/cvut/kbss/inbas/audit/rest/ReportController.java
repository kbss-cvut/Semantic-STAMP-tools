package cz.cvut.kbss.inbas.audit.rest;

import cz.cvut.kbss.inbas.audit.exceptions.InvalidReportException;
import cz.cvut.kbss.inbas.audit.rest.dto.factory.DtoFactory;
import cz.cvut.kbss.inbas.audit.rest.dto.model.EventReport;
import cz.cvut.kbss.inbas.audit.rest.exceptions.NotFoundException;
import cz.cvut.kbss.inbas.audit.services.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class ReportController {

    private static final Logger LOG = LoggerFactory.getLogger(ReportController.class);

    @Autowired
    private ReportService reportService;

    @Autowired
    private DtoFactory dtoFactory;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<EventReport> getAllReports() {
        final Collection<cz.cvut.kbss.inbas.audit.model.EventReport> reports = reportService.findAll();
        if (reports.isEmpty()) {
            throw new NotFoundException("No reports found.");
        }
        return reports.stream().map(dtoFactory::toDto).collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{key}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EventReport getReport(@PathVariable("key") String key) {
        return dtoFactory.toDto(getEventReport(key));
    }

    private cz.cvut.kbss.inbas.audit.model.EventReport getEventReport(String key) {
        final cz.cvut.kbss.inbas.audit.model.EventReport original = reportService.findByKey(key);
        if (original == null) {
            throw NotFoundException.create("Report", key);
        }
        return original;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void createReport(@RequestBody EventReport report) {
        final cz.cvut.kbss.inbas.audit.model.EventReport eventReport = dtoFactory.toDomainModel(report);
        assert eventReport != null;
        reportService.persist(eventReport);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Created report from data {}", report);
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{key}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateReport(@PathVariable("key") String key, @RequestBody EventReport report) {
        final cz.cvut.kbss.inbas.audit.model.EventReport original = getEventReport(key);
        final cz.cvut.kbss.inbas.audit.model.EventReport update = dtoFactory.toDomainModel(report);
        validateReportForUpdate(original, update);
        reportService.update(update);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Updated event report {}", report);
        }
    }

    private void validateReportForUpdate(cz.cvut.kbss.inbas.audit.model.EventReport original, cz.cvut.kbss.inbas.audit.model.EventReport update) {
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
        final cz.cvut.kbss.inbas.audit.model.EventReport report = getEventReport(key);
        reportService.remove(report);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Deleted event report {}.", report.getUri());
        }
    }
}
