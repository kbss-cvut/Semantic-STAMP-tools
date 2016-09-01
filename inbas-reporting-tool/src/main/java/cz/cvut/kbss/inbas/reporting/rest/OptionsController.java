package cz.cvut.kbss.inbas.reporting.rest;

import cz.cvut.kbss.inbas.reporting.rest.exception.BadRequestException;
import cz.cvut.kbss.inbas.reporting.service.options.OptionsService;
import cz.cvut.kbss.inbas.reporting.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@PreAuthorize("permitAll()")
@RestController
@RequestMapping("/options")
public class OptionsController extends BaseController {

    @Autowired
    private OptionsService optionsService;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getOptions(@RequestParam MultiValueMap<String, String> params) {
        if (!params.containsKey(Constants.OPTIONS_TYPE_QUERY_PARAM)) {
            throw new BadRequestException("Missing options type parameter - \'type\'.");
        }
        try {
            final Map<String, String> parameters = new HashMap<>(params.size());
            params.entrySet().stream().filter(e -> !e.getKey().equals(Constants.OPTIONS_TYPE_QUERY_PARAM))
                  .forEach(e -> parameters.put(e.getKey(), e.getValue().get(0)));
            return optionsService
                    .getOptions(params.getFirst(Constants.OPTIONS_TYPE_QUERY_PARAM), parameters);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
