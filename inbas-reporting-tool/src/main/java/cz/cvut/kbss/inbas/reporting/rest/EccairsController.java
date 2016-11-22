package cz.cvut.kbss.inbas.reporting.rest;

import cz.cvut.kbss.inbas.reporting.exception.NotFoundException;
import cz.cvut.kbss.inbas.reporting.model.OccurrenceReport;
import cz.cvut.kbss.inbas.reporting.service.OccurrenceReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/eccairs")
public class EccairsController extends BaseController {

    @Autowired
    private OccurrenceReportService occurrenceReportService;
//
//    @Autowired
//    private FormGenService formGenService;
//
//    @Autowired
//    private EccairsService eccairsService;
    
    
    
    @RequestMapping(value = "/latest/{key}", method = RequestMethod.GET)
    public OccurrenceReport getEccairsLatest(@PathVariable("key") String key) {
        Set<String> keys = new HashSet<>();
        keys.add("48344622112312892577");
        keys.add("48255409727258881179");
        keys.add("48304190915314602370");
        if(!keys.contains(key)){
            throw new NotFoundException(String.format("Latest eccairs report for report with key %s not found!", key));
        }
        final OccurrenceReport report = occurrenceReportService.findByKey("48344622112312892577");
        return report;
    }
}
