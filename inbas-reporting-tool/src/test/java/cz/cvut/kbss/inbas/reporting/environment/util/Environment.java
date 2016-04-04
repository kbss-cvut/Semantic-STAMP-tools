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

import java.security.Principal;
import java.util.Collection;
import java.util.Iterator;

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

    /**
     * Gets current user as security principal.
     *
     * @return Current user authentication as principal or {@code null} if there is no current user
     */
    public static Principal getCurrentUserPrincipal() {
        return SecurityContextHolder.getContext() != null ? SecurityContextHolder.getContext().getAuthentication() :
               null;
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

    /**
     * Gets random element from the specified collection.
     *
     * @param col The collection
     * @param <T> Element type
     * @return Randomly selected element from the collection
     */
    public static <T> T randomElement(Collection<T> col) {
        final int index = Generator.randomInt(col.size() + 1) - 1;  // Some arithmetic to get the bounds right
        final Iterator<T> it = col.iterator();
        int i = 0;
        T item = null;
        while (it.hasNext() && i <= index) {
            item = it.next();
            i++;
        }
        return item;
    }
}
