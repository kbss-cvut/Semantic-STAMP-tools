package cz.cvut.kbss.inbas.audit.rest;

import cz.cvut.kbss.inbas.audit.model.Person;
import cz.cvut.kbss.inbas.audit.rest.dto.model.portal.PortalUser;
import cz.cvut.kbss.inbas.audit.rest.exceptions.PortalAuthenticationException;
import cz.cvut.kbss.inbas.audit.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

@RestController
@RequestMapping("/portal")
public class PortalController extends BaseController {

    private static final String PORTAL_USER_PATH = "api/jsonws/user/get-user-by-email-address";
    // Params: emailAddress, companyId

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private PersonService personService;

    @PreAuthorize("permitAll()")
    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.TEXT_HTML_VALUE)
    public String portalLogin(
            @RequestParam(value = "username", required = true) String username,
            @RequestParam(value = "password", required = true) String password, HttpServletRequest request) {
        if (personService.findByUsername(username) == null) {
            doSyntheticRegistration(username, password, request);
        }
        final Authentication result = authenticationProvider
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        if (result.isAuthenticated()) {
            // TODO Make this nicer
            String uri = request.getRequestURI();
            uri = uri.substring(0, uri.indexOf("/rest"));
            return "<html>" +
                    "<script>window.location='" + uri + "/index.html'</script>" +
                    "</html>";
        } else {
            throw new PortalAuthenticationException("Cannot authenticate user " + username + " through portal.");
        }
    }

    private void doSyntheticRegistration(String username, String password, HttpServletRequest request) {
        final Person person = new Person();
        person.setUsername(username);
        person.setPassword(password);
        loadUserInfoFormPortal(person, request);
        personService.persist(person);
    }

    private void loadUserInfoFormPortal(Person person, HttpServletRequest request) {
        final RestTemplate rest = new RestTemplate();
        // TODO construct the portal path
        String url = request.getRequestURI() + PORTAL_USER_PATH;
        final PortalUser portalUser = rest.getForObject(URI.create(url), PortalUser.class);
        person.setFirstName(portalUser.getFirstName());
        person.setLastName(portalUser.getLastName());
    }
}
