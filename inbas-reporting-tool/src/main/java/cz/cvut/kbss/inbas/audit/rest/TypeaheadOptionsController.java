package cz.cvut.kbss.inbas.audit.rest;

import cz.cvut.kbss.inbas.audit.rest.dto.model.RawJson;
import cz.cvut.kbss.inbas.audit.rest.exceptions.NotFoundException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ledvima1
 */
@RestController
@RequestMapping("/typeahead")
public class TypeaheadOptionsController extends BaseController {

    private final Map<String, String> options = new HashMap<>();

    @RequestMapping(value = "/options", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public RawJson getLocations(@RequestParam("type") String type) {
        String values;
        synchronized (options) {
            if (!options.containsKey(type)) {
                values = loadOptions(type);
                options.put(type, values);
            } else {
                values = options.get(type);
            }
        }
        return new RawJson(values);
    }

    private synchronized String loadOptions(String type) {
        final InputStream is = this.getClass().getClassLoader().getResourceAsStream(type + ".json");
        if (is == null) {
            throw new NotFoundException("Options for typeahead of type " + type + " not found.");
        }
        try (final BufferedReader reader = new BufferedReader(
                new InputStreamReader(is))) {
            final StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            LOG.error("Unable to load event types from file.", e);
            return "";
        }
    }
}
