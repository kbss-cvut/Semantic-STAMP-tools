package cz.cvut.kbss.inbas.audit.rest;

import cz.cvut.kbss.inbas.audit.model.reports.InvestigationReport;
import cz.cvut.kbss.inbas.audit.model.reports.PreliminaryReport;
import cz.cvut.kbss.inbas.audit.rest.exceptions.NotFoundException;
import cz.cvut.kbss.inbas.audit.rest.util.RestUtils;
import cz.cvut.kbss.inbas.audit.service.InvestigationReportService;
import cz.cvut.kbss.inbas.audit.service.PreliminaryReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/investigations")
public class InvestigationController extends BaseController {

    @Autowired
    private PreliminaryReportService preliminaryReportService;

    @Autowired
    private InvestigationReportService investigationReportService;

    @RequestMapping(method = RequestMethod.GET, value = "/{key}", produces = MediaType.APPLICATION_JSON_VALUE)
    public InvestigationReport getInvestigationReport(@PathVariable("key") String key) {
        // TODO
        return null;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> createInvestigationFromPreliminaryReport(
            @RequestParam(value = "key", required = true) String reportKey) {
        final PreliminaryReport report = preliminaryReportService.findByKey(reportKey);
        if (report == null) {
            throw NotFoundException.create("Preliminary report", reportKey);
        }
        final InvestigationReport investigation = investigationReportService.createFromPreliminaryReport(report);
        final HttpHeaders headers = RestUtils.createLocationHeader("/{key}", investigation.getKey());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
}
