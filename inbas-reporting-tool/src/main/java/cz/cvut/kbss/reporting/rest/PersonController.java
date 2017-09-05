package cz.cvut.kbss.reporting.rest;

import cz.cvut.kbss.reporting.dto.PersonUpdateDto;
import cz.cvut.kbss.reporting.exception.NotFoundException;
import cz.cvut.kbss.reporting.model.Person;
import cz.cvut.kbss.reporting.rest.dto.mapper.DtoMapper;
import cz.cvut.kbss.reporting.rest.util.RestUtils;
import cz.cvut.kbss.reporting.security.SecurityConstants;
import cz.cvut.kbss.reporting.service.PersonService;
import cz.cvut.kbss.reporting.service.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;

@RestController
@RequestMapping("/persons")
public class PersonController extends BaseController {

    private final PersonService personService;

    private final SecurityUtils securityUtils;

    private final DtoMapper dtoMapper;

    @Autowired
    public PersonController(PersonService personService, SecurityUtils securityUtils, DtoMapper dtoMapper) {
        this.personService = personService;
        this.securityUtils = securityUtils;
        this.dtoMapper = dtoMapper;
    }

    @PreAuthorize("hasRole('" + SecurityConstants.ROLE_ADMIN + "')")
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<Person> getAll() {
        return personService.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Person getByUsername(@PathVariable("username") String username) {
        final Person p = personService.findByUsername(username);
        if (p == null) {
            throw NotFoundException.create("Person", username);
        }
        p.erasePassword();
        return p;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/current", produces = MediaType.APPLICATION_JSON_VALUE)
    public Person getCurrent(Principal principal) {
        final String username = principal.getName();
        return getByUsername(username);
    }

    @PreAuthorize("permitAll()")
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> create(@RequestBody Person person) {
        personService.persist(person);
        if (LOG.isTraceEnabled()) {
            LOG.trace("User {} successfully registered.", person);
        }
        final HttpHeaders headers = RestUtils
                .createLocationHeaderFromCurrentUri("/{username}", person.getUsername());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/current", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCurrentUser(@RequestBody PersonUpdateDto dto) {
        if (dto.getPassword() != null) {
            securityUtils.verifyCurrentUserPassword(dto.getPasswordOriginal());
        }
        final Person person = dtoMapper.personUpdateDtoToPerson(dto);
        personService.update(person);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Person {} successfully updated.", person);
        }
    }

    @RequestMapping(value = "/exists", method = RequestMethod.GET)
    @ResponseBody
    public String desUsernameExist(@RequestParam(name = "username") String username) {
        return Boolean.toString(personService.exists(username));
    }
}
