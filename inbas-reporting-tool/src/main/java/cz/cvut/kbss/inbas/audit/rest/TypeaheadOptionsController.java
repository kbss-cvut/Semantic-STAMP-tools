package cz.cvut.kbss.inbas.audit.rest;

import cz.cvut.kbss.inbas.audit.rest.dto.model.RawJson;
import cz.cvut.kbss.inbas.audit.service.options.TypeaheadOptionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ledvima1
 */
@RestController
@RequestMapping("/typeahead")
public class TypeaheadOptionsController extends BaseController {

    @Autowired
    private TypeaheadOptionsService optionsService;

    @PreAuthorize("permitAll()")
    @RequestMapping(value = "/options", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public RawJson getOptions(@RequestParam("type") String type) {
        return optionsService.getOptions(type);
    }
}
