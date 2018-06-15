package cz.cvut.kbss.reporting.rest;

import cz.cvut.kbss.jsonld.JsonLd;
import cz.cvut.kbss.reporting.service.options.OptionsService;
import cz.cvut.kbss.reporting.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Retrieves options for populating selectors and typeaheads in the UI.
 * <p>
 * This endpoint now requires authorization like the rest of the API since some of the options rely on data present in
 * repository.
 */
@RestController
@RequestMapping("/options")
public class OptionsController extends BaseController {

    @Autowired
    private OptionsService optionsService;

    @RequestMapping(method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE, JsonLd.MEDIA_TYPE})
    public Object getOptions(@RequestParam(value = Constants.OPTIONS_TYPE_QUERY_PARAM) String type) {
        try {
            return optionsService.getOptions(type);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
