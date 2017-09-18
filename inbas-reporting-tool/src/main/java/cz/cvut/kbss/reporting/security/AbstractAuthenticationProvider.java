package cz.cvut.kbss.reporting.security;

import cz.cvut.kbss.reporting.model.Person;
import cz.cvut.kbss.reporting.security.model.AuthenticationToken;
import cz.cvut.kbss.reporting.service.security.LoginTracker;
import cz.cvut.kbss.reporting.service.security.SecurityUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

abstract class AbstractAuthenticationProvider implements AuthenticationProvider {

    final PasswordEncoder passwordEncoder;

    final SecurityUtils securityUtils;

    private final LoginTracker loginTracker;

    protected AbstractAuthenticationProvider(PasswordEncoder passwordEncoder, SecurityUtils securityUtils,
                                             LoginTracker loginTracker) {
        this.passwordEncoder = passwordEncoder;
        this.securityUtils = securityUtils;
        this.loginTracker = loginTracker;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(aClass) ||
                AuthenticationToken.class.isAssignableFrom(aClass);
    }

    void loginSuccess(Person user) {
        loginTracker.successfulLoginAttempt(user);
    }

    void loginFailure(Person user) {
        loginTracker.unsuccessfulLoginAttempt(user);
    }
}
