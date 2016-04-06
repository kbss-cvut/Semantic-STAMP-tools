package cz.cvut.kbss.inbas.reporting.rest;

import cz.cvut.kbss.inbas.reporting.exception.NotFoundException;
import cz.cvut.kbss.inbas.reporting.model_new.Occurrence;
import cz.cvut.kbss.inbas.reporting.model_new.OccurrenceReport;
import cz.cvut.kbss.inbas.reporting.rest.dto.mapper.ReportMapper;
import cz.cvut.kbss.inbas.reporting.rest.dto.model.OccurrenceReportDtoList;
import cz.cvut.kbss.inbas.reporting.service.OccurrenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping(value = "/occurrences")
public class OccurrenceController extends BaseController {

    @Autowired
    private OccurrenceService occurrenceService;

    @Autowired
    private ReportMapper mapper;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<Occurrence> getOccurrences() {
        return occurrenceService.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{key}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Occurrence findByKey(@PathVariable("key") String key) {
        final Occurrence o = occurrenceService.findByKey(key);
        if (o == null) {
            throw NotFoundException.create("Occurrence", key);
        }
        return o;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{key}/reports", produces = MediaType.APPLICATION_JSON_VALUE)
    public OccurrenceReportDtoList getOccurrenceReports(@PathVariable("key") String key) {
        final Occurrence occurrence = findByKey(key);
        final Collection<OccurrenceReport> reports = occurrenceService.getReports(occurrence);
        final OccurrenceReportDtoList list = new OccurrenceReportDtoList(reports.size());
        reports.forEach(r -> list.add(mapper.occurrenceReportToOccurrenceReportDto(r)));
        return list;
    }
}
