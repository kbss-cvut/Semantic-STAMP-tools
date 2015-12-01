package cz.cvut.kbss.inbas.audit.rest;

import cz.cvut.kbss.inbas.audit.model.reports.OccurrenceReport;
import cz.cvut.kbss.inbas.audit.rest.exceptions.BadRequestException;
import cz.cvut.kbss.inbas.audit.rest.exceptions.NotFoundException;
import cz.cvut.kbss.inbas.audit.service.OccurrenceReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/reports")
public class OccurrenceReportController extends BaseController {

    @Autowired
    private OccurrenceReportService reportService;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<OccurrenceReport> getAllReports(@RequestParam(value = "type", required = false) String type) {
        try {
            return reportService.findAll(type);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(e);
        }
    }

    @RequestMapping(value = "/{key}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public OccurrenceReport getReport(@PathVariable("key") String key) {
        final OccurrenceReport report = reportService.findByKey(key);
        if (report == null) {
            throw NotFoundException.create("Occurrence report", key);
        }
        return report;
    }

    @RequestMapping(value = "/{key}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReport(@PathVariable("key") String key) {
        final OccurrenceReport toRemove = getReport(key);
        reportService.remove(toRemove);
    }
}
