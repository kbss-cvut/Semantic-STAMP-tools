package cz.cvut.kbss.inbas.audit.rest;

import cz.cvut.kbss.inbas.audit.model.EventReport;
import cz.cvut.kbss.inbas.audit.rest.exceptions.NotFoundException;
import cz.cvut.kbss.inbas.audit.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * @author ledvima1
 */
@RestController
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<EventReport> getAllReports() {
        final Collection<EventReport> reports = reportService.findAll();
        if (reports.isEmpty()) {
            throw new NotFoundException("No reports found.");
        }
        return reports;
    }
}
