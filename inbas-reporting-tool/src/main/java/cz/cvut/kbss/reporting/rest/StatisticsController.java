package cz.cvut.kbss.reporting.rest;

import cz.cvut.kbss.reporting.rest.dto.model.RawJson;
import cz.cvut.kbss.reporting.service.SPARQLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statistics")
public class StatisticsController extends BaseController {

    @Autowired
    private SPARQLService sparqlService;

    @RequestMapping(path="/{queryName}",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public RawJson getQueryResult(@PathVariable String queryName) {
        return sparqlService.getSPARQLSelectResult("statistics_"+queryName);
    }
}
