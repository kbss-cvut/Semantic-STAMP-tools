package cz.cvut.kbss.reporting.rest;

import cz.cvut.kbss.reporting.exception.NotFoundException;
import cz.cvut.kbss.reporting.model.OccurrenceReport;
import cz.cvut.kbss.reporting.service.data.eccairs.EccairsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/eccairs")
public class EccairsController extends BaseController {

    @Autowired
    private EccairsService eccairsService;

    @RequestMapping(value = "/latest/{key}", method = RequestMethod.GET)
    public String getEccairsLatest(@PathVariable("key") String key) {
        final OccurrenceReport report = eccairsService.getEccairsLatestByKey(key);
        if (report == null) {
            throw new NotFoundException("Unable to find ECCAIRS report for report with key " + key + ".");
        }
        return report.getKey();
    }
}
