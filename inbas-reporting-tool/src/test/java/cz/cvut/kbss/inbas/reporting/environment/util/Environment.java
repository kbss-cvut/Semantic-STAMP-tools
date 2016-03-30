package cz.cvut.kbss.inbas.reporting.environment.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.kbss.inbas.reporting.model_new.Person;
import cz.cvut.kbss.inbas.reporting.model_new.util.HasUri;
import cz.cvut.kbss.inbas.reporting.security.model.AuthenticationToken;
import cz.cvut.kbss.inbas.reporting.security.model.UserDetails;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import java.util.Collection;

public class Environment {

    private static Person currentUser;

    private static ObjectMapper objectMapper;

    private Environment() {
        throw new AssertionError();
    }

    /**
     * Initializes security context with the specified user.
     *
     * @param user User to set as currently authenticated
     */
    public static void setCurrentUser(Person user) {
        currentUser = user;
        final UserDetails userDetails = new UserDetails(user);
        SecurityContext context = new SecurityContextImpl();
        context.setAuthentication(new AuthenticationToken(userDetails.getAuthorities(), userDetails));
        SecurityContextHolder.setContext(context);
    }

    public static Person getCurrentUser() {
        return currentUser;
    }

    /**
     * Gets a Jackson object mapper for mapping JSON to Java and vice versa.
     *
     * @return Object mapper
     */
    public static ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
            objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
        }
        return objectMapper;
    }

    /**
     * Returns true if the two collections contain elements with the same URIs.
     *
     * @param one First collection
     * @param two Second collection
     * @return True if the collections match, false otherwise
     */
    public static boolean areEqual(Collection<? extends HasUri> one, Collection<? extends HasUri> two) {
        assert one != null;
        assert two != null;
        if (one.size() != two.size()) {
            return false;
        }
        boolean found;
        for (HasUri a : one) {
            found = false;
            for (HasUri b : two) {
                if (a.getUri().equals(b.getUri())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }
}
