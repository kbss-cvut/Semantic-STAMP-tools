package cz.cvut.kbss.inbas.audit.rest;

import cz.cvut.kbss.inbas.audit.dto.InvestigationReportDto;
import cz.cvut.kbss.inbas.audit.exception.NotFoundException;
import cz.cvut.kbss.inbas.audit.exception.ValidationException;
import cz.cvut.kbss.inbas.audit.model.reports.InvestigationReport;
import cz.cvut.kbss.inbas.audit.model.reports.OccurrenceReport;
import cz.cvut.kbss.inbas.audit.model.reports.PreliminaryReport;
import cz.cvut.kbss.inbas.audit.rest.dto.mapper.ReportMapper;
import cz.cvut.kbss.inbas.audit.rest.util.RestUtils;
import cz.cvut.kbss.inbas.audit.service.InvestigationReportService;
import cz.cvut.kbss.inbas.audit.service.PreliminaryReportService;
import cz.cvut.kbss.inbas.audit.service.ReportService;
import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/investigations")
public class InvestigationController extends BaseController {

    @Autowired
    private PreliminaryReportService preliminaryReportService;
    @Autowired
    private InvestigationReportService investigationReportService;
    @Autowired
    private ReportService reportService;

    @Autowired
    private ReportMapper reportMapper;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<OccurrenceReport> getInvestigationReports() {
        return reportService.findAll(Vocabulary.InvestigationReport);
    }


    @RequestMapping(method = RequestMethod.GET, value = "/{key}", produces = MediaType.APPLICATION_JSON_VALUE)
    public InvestigationReportDto getInvestigationReport(@PathVariable("key") String key) {
        final InvestigationReport report = getReport(key);
        return reportMapper.investigationReportToInvestigationReportDto(report);
    }

    private InvestigationReport getReport(String key) {
        final InvestigationReport report = investigationReportService.findByKey(key);
        if (report == null) {
            throw NotFoundException.create("Investigation report", key);
        }
        return report;
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{key}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateInvestigationReport(@PathVariable("key") String key, @RequestBody InvestigationReportDto data) {
        final InvestigationReport orig = getReport(key);
        validateReportForUpdate(orig, data);
        final InvestigationReport update = reportMapper.investigationReportDtoToInvestigationReport(data);
        investigationReportService.update(update);
        if (LOG.isTraceEnabled()) {
            LOG.trace("Updated investigation report {}", update);
        }
    }

    private void validateReportForUpdate(InvestigationReport original, InvestigationReportDto update) {
        if (!original.getUri().equals(update.getUri())) {
            throw new ValidationException(
                    "Updated report URI does not match the original. Expected " + original.getUri() + ", but got " +
                            update.getUri());
        }
        if (!original.getKey().equals(update.getKey())) {
            throw new ValidationException(
                    "Updated report key does not match the original. Expected " + original.getKey() + ", but got " +
                            update.getKey());
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> createInvestigationFromPreliminaryReport(
            @RequestParam(value = "key", required = true) String reportKey) {
        final PreliminaryReport report = preliminaryReportService.findByKey(reportKey);
        if (report == null) {
            throw NotFoundException.create("Preliminary report", reportKey);
        }
        if (LOG.isTraceEnabled()) {
            LOG.trace("Creating investigation report from preliminary report {}", report.getKey());
        }
        final InvestigationReport investigation = investigationReportService.createFromPreliminaryReport(report);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("/{key}", investigation.getKey());
        LOG.debug("Investigation successfully created. URI: {}", investigation.getUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{key}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeInvestigation(@PathVariable("key") String key) {
        final InvestigationReport toRemove = getReport(key);
        investigationReportService.remove(toRemove);
        LOG.debug("Investigation removed: {}", toRemove);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{key}/revisions")
    public ResponseEntity<Void> createNewRevision(@PathVariable("key") String key) {
        final InvestigationReport report = getReport(key);
        final InvestigationReport newRevision = investigationReportService.createNewRevision(report);
        final HttpHeaders headers = RestUtils.createLocationHeaderFromCurrentUri("{key}", newRevision.getKey());
        final String location = headers.getLocation().toString();
        headers.set(HttpHeaders.LOCATION, location.replace(key + "/revisions", ""));
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
}
