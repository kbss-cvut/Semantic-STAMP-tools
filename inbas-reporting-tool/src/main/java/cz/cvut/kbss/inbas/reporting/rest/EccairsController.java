package cz.cvut.kbss.inbas.reporting.rest;

import cz.cvut.kbss.inbas.reporting.model.OccurrenceReport;
import cz.cvut.kbss.inbas.reporting.rest.dto.model.RawJson;
import cz.cvut.kbss.inbas.reporting.service.OccurrenceReportService;
import cz.cvut.kbss.inbas.reporting.service.data.eccairs.EccairsService;
import cz.cvut.kbss.inbas.reporting.service.formgen.FormGenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/eccairs")
public class EccairsController extends BaseController {

    @Autowired
    private OccurrenceReportService occurrenceReportService;

    @Autowired
    private FormGenService formGenService;

    @Autowired
    private EccairsService eccairsService;

    @RequestMapping(value = "/latest/{key}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public OccurrenceReport getEccairsLatest(@PathVariable("key") String key) {
        final OccurrenceReport report = occurrenceReportService.findByKey(key);
        final RawJson s = formGenService.generateForm(report, Collections.emptyMap());

        return eccairsService.getEccairsLatest(s.toString());

    }
}
