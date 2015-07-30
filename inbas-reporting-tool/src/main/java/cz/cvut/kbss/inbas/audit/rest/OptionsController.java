package cz.cvut.kbss.inbas.audit.rest;

import cz.cvut.kbss.inbas.audit.model.reports.OccurrenceSeverity;
import cz.cvut.kbss.inbas.audit.model.reports.incursions.LowVisibilityProcedure;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * @author ledvima1
 */
@RestController
@RequestMapping("/options")
public class OptionsController {

    @RequestMapping(value = "/lvp", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<LowVisibilityProcedure> getLowVisibilityProcedureOptions() {
        return Arrays.asList(LowVisibilityProcedure.values());
    }

    @RequestMapping(value = "/occurrenceSeverity", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<OccurrenceSeverity> getOccurrenceSeverityOptions() {
        return Arrays.asList(OccurrenceSeverity.values());
    }
}
