package cz.cvut.kbss.inbas.audit.rest;

import cz.cvut.kbss.inbas.audit.model.Person;
import cz.cvut.kbss.inbas.audit.rest.exceptions.BadRequestException;
import cz.cvut.kbss.inbas.audit.rest.exceptions.NotFoundException;
import cz.cvut.kbss.inbas.audit.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.security.Principal;
import java.util.Collection;

/**
 * @author ledvima1
 */
@RestController
@RequestMapping("/persons")
public class PersonController extends BaseController {

    @Autowired
    private PersonService personService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<Person> getAll() {
        final Collection<Person> people = personService.findAll();
        if (people.isEmpty()) {
            throw new NotFoundException("No people found.");
        }
        return people;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(method = RequestMethod.GET, value = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Person getByUsername(@PathVariable("username") String username) {
        final Person p = personService.findByUsername(username);
        if (p == null) {
            throw NotFoundException.create("Person", username);
        }
        return p;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(method = RequestMethod.GET, value = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
    public Person getCurrent(Principal principal) {
        final String username = principal.getName();
        return getByUsername(username);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Person person) {
        try {
            personService.persist(person);
        } catch (IllegalStateException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @PostConstruct
    public void init() {
        final Person p = new Person();
        p.setFirstName("Catherine");
        p.setLastName("Halsey");
        p.setUsername("halsey@unsc.org");
        p.setPassword("john117");
        if (personService.findByUsername(p.getUsername()) == null) {
            personService.persist(p);
        }
    }
}
