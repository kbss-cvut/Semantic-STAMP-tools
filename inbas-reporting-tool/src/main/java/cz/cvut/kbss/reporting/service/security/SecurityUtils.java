package cz.cvut.kbss.reporting.service.security;

import cz.cvut.kbss.reporting.model.Person;
import cz.cvut.kbss.reporting.security.model.AuthenticationToken;
import cz.cvut.kbss.reporting.security.model.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SecurityUtils {

    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityUtils(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Gets the currently authenticated user.
     *
     * @return Current user
     */
    public Person getCurrentUser() {
        final SecurityContext context = SecurityContextHolder.getContext();
        assert context != null;
        final UserDetails userDetails = (UserDetails) context.getAuthentication().getDetails();
        return userDetails.getUser();
    }

    /**
     * Gets details of the currently authenticated user.
     *
     * @return Currently authenticated user details or null, if no one is currently authenticated
     */
    public UserDetails getCurrentUserDetails() {
        final SecurityContext context = SecurityContextHolder.getContext();
        if (context.getAuthentication() != null && context.getAuthentication().getDetails() instanceof UserDetails) {
            return (UserDetails) context.getAuthentication().getDetails();
        } else {
            return null;
        }
    }

    /**
     * Creates an authentication token based on the specified user details and sets it to the current thread's security
     * context.
     *
     * @param userDetails Details of the user to set as current
     * @return The generated authentication token
     */
    public AuthenticationToken setCurrentUser(UserDetails userDetails) {
        final AuthenticationToken token = new AuthenticationToken(userDetails.getAuthorities(), userDetails);
        token.setAuthenticated(true);

        final SecurityContext context = new SecurityContextImpl();
        context.setAuthentication(token);
        SecurityContextHolder.setContext(context);
        return token;
    }

    /**
     * Reloads the current user's data from the database.
     */
    public void updateCurrentUser() {
        final UserDetails updateDetails =
                (UserDetails) userDetailsService.loadUserByUsername(getCurrentUser().getUsername());
        setCurrentUser(updateDetails);
    }

    /**
     * Checks that the specified password corresponds to the current user's password.
     *
     * @param password The password to verify
     * @throws IllegalArgumentException When the password's do not match
     */
    public void verifyCurrentUserPassword(String password) {
        final Person currentUser = getCurrentUser();
        if (!passwordEncoder.matches(password, currentUser.getPassword())) {
            throw new IllegalArgumentException("The specified password does not match the original one.");
        }
    }
}
