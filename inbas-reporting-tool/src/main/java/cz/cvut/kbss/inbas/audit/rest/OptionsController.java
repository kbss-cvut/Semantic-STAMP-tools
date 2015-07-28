package cz.cvut.kbss.inbas.audit.rest;

import cz.cvut.kbss.inbas.audit.model.EventSeverity;
import cz.cvut.kbss.inbas.audit.model.LowVisibilityProcedure;
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

    @RequestMapping(value = "/eventSeverity", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EventSeverity> getEventSeverityOptions() {
        return Arrays.asList(EventSeverity.values());
    }
}
