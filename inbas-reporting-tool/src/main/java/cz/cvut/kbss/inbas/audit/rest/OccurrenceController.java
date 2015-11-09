package cz.cvut.kbss.inbas.audit.rest;

import cz.cvut.kbss.inbas.audit.model.Occurrence;
import cz.cvut.kbss.inbas.audit.rest.exceptions.NotFoundException;
import cz.cvut.kbss.inbas.audit.service.OccurrenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping(value = "/occurrences")
public class OccurrenceController extends BaseController {

    @Autowired
    private OccurrenceService occurrenceService;

    public Collection<Occurrence> getOccurrences() {
        return occurrenceService.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{key}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Occurrence findByKey(@PathVariable("key") String key) {
        final Occurrence o = occurrenceService.findByKey(key);
        if (o == null) {
            throw NotFoundException.create("Occurrence", key);
        }
        return o;
    }
}
