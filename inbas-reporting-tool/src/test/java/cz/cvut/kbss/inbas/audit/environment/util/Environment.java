package cz.cvut.kbss.inbas.audit.environment.util;

import cz.cvut.kbss.inbas.audit.model.Person;
import cz.cvut.kbss.inbas.audit.security.model.AuthenticationToken;
import cz.cvut.kbss.inbas.audit.security.model.UserDetails;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

public class Environment {

    private static Person currentUser;

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
}
