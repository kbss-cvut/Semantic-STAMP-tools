package cz.cvut.kbss.inbas.reporting.service.security;

import cz.cvut.kbss.inbas.reporting.model.Person;
import cz.cvut.kbss.inbas.reporting.security.model.UserDetails;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityUtils {

    /**
     * Gets the currently authenticated user.
     *
     * @return Current user
     */
    public Person getCurrentUser() {
        final SecurityContext context = SecurityContextHolder.getContext();
        final UserDetails userDetails = (UserDetails) context.getAuthentication().getDetails();
        return userDetails.getUser();
    }
}
