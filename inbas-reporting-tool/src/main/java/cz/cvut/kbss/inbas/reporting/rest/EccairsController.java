package cz.cvut.kbss.inbas.reporting.rest;

import cz.cvut.kbss.inbas.reporting.model.OccurrenceReport;
import cz.cvut.kbss.inbas.reporting.service.data.eccairs.EccairsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/eccairs")
public class EccairsController extends BaseController {

    @Autowired
    private EccairsService eccairsService;

    @RequestMapping(value = "/latest/{key}", method = RequestMethod.GET)
    public OccurrenceReport getEccairsLatest(@PathVariable("key") String key) {
        return eccairsService.getEccairsLatestByKey(key);
    }
}
