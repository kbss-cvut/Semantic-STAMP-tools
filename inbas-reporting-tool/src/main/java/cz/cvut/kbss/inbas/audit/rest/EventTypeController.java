package cz.cvut.kbss.inbas.audit.rest;

import cz.cvut.kbss.inbas.audit.rest.dto.model.RawJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author ledvima1
 */
@RestController
@RequestMapping("/eventTypes")

public class EventTypeController {

    private static final Logger LOG = LoggerFactory.getLogger(EventTypeController.class);

    private String eventTypes;

    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public RawJson getEventTypes() {
        if (eventTypes == null) {
            loadEventTypes();
        }
        return new RawJson(eventTypes);
    }

    private void loadEventTypes() {
        try (final BufferedReader reader = new BufferedReader(
                new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("evtypes.json")))) {
            final StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            this.eventTypes = sb.toString();
        } catch (IOException e) {
            LOG.error("Unable to load event types from file.", e);
        }
    }
}
