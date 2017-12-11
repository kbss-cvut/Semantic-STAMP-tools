package cz.cvut.kbss.reporting.security;

import cz.cvut.kbss.reporting.security.model.UserDetails;
import cz.cvut.kbss.reporting.service.security.LoginTracker;
import cz.cvut.kbss.reporting.service.security.SecurityUtils;
import cz.cvut.kbss.reporting.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service("ontologyAuthenticationProvider")
public class OntologyAuthenticationProvider extends AbstractAuthenticationProvider {

    private static final Logger LOG = LoggerFactory.getLogger(OntologyAuthenticationProvider.class);

    private final UserDetailsService userDetailsService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public OntologyAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder,
                                          SecurityUtils securityUtils, LoginTracker loginTracker) {
        super(passwordEncoder, securityUtils, loginTracker);
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String username = authentication.getPrincipal().toString();
        verifyUsernameNotEmpty(username);
        LOG.debug("Authenticating user {}", username);

        final UserDetails userDetails = (UserDetails) userDetailsService.loadUserByUsername(username);
        verifyAccountStatus(userDetails.getUser());
        final String password = (String) authentication.getCredentials();
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            loginFailure(userDetails.getUser());
            throw new BadCredentialsException("Provided credentials don't match.");
        }
        loginSuccess(userDetails.getUser());
        setupExtendedSessionForMobileUser(userDetails);
        return securityUtils.setCurrentUser(userDetails);
    }

    private void setupExtendedSessionForMobileUser(UserDetails userDetails) {
        final String client = request.getHeader(Constants.CLIENT_TYPE_HEADER);
        if (client != null && client.equals(Constants.CLIENT_TYPE_MOBILE)) {
            userDetails.setExtended();
        }
    }
}
