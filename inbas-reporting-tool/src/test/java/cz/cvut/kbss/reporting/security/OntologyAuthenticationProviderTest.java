package cz.cvut.kbss.reporting.security;

import cz.cvut.kbss.reporting.environment.config.TestSecurityConfig;
import cz.cvut.kbss.reporting.environment.generator.Generator;
import cz.cvut.kbss.reporting.model.Person;
import cz.cvut.kbss.reporting.security.model.UserDetails;
import cz.cvut.kbss.reporting.service.BaseServiceTestRunner;
import cz.cvut.kbss.reporting.service.security.LoginTracker;
import cz.cvut.kbss.reporting.util.Constants;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;

import javax.servlet.http.HttpServletRequest;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

@ContextConfiguration(classes = {TestSecurityConfig.class})
public class OntologyAuthenticationProviderTest extends BaseServiceTestRunner {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    @Qualifier("ontologyAuthenticationProvider")
    private AuthenticationProvider provider;

    @Autowired
    private LoginTracker loginTracker;

    @Autowired
    private HttpServletRequest request;

    private Person user;

    @Before
    public void setUp() {
        this.user = persistPerson();
        SecurityContextHolder.setContext(new SecurityContextImpl());
    }

    @After
    public void tearDown() {
        SecurityContextHolder.setContext(new SecurityContextImpl());
    }

    @Test
    public void successfulAuthenticationSetsSecurityContext() {
        final Authentication auth = authentication(Generator.USERNAME, Generator.PASSWORD);
        final SecurityContext context = SecurityContextHolder.getContext();
        assertNull(context.getAuthentication());
        final Authentication result = provider.authenticate(auth);
        assertNotNull(SecurityContextHolder.getContext());
        final UserDetails details = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
        assertEquals(Generator.USERNAME, details.getUsername());
        assertTrue(result.isAuthenticated());
    }

    private static Authentication authentication(String username, String password) {
        return new UsernamePasswordAuthenticationToken(username, password);
    }

    @Test
    public void authenticateThrowsUserNotFoundExceptionForUnknownUsername() {
        thrown.expect(UsernameNotFoundException.class);
        final Authentication auth = authentication("unknownUsername", Generator.PASSWORD);
        try {
            provider.authenticate(auth);
        } finally {
            final SecurityContext context = SecurityContextHolder.getContext();
            assertNull(context.getAuthentication());
        }
    }

    @Test
    public void authenticateThrowsBadCredentialsForInvalidPassword() {
        thrown.expect(BadCredentialsException.class);
        final Authentication auth = authentication(Generator.USERNAME, "unknownPassword");
        try {
            provider.authenticate(auth);
        } finally {
            final SecurityContext context = SecurityContextHolder.getContext();
            assertNull(context.getAuthentication());
        }
    }

    @Test
    public void supportsUsernameAndPasswordAuthentication() {
        assertTrue(provider.supports(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    public void authenticateThrowsLockedExceptionForLockedAccount() {
        thrown.expect(LockedException.class);
        thrown.expectMessage("Account is locked.");
        user.lock();
        personDao.update(user);
        final Authentication auth = authentication(user.getUsername(), Generator.PASSWORD);
        try {
            provider.authenticate(auth);
        } finally {
            final SecurityContext context = SecurityContextHolder.getContext();
            assertNull(context.getAuthentication());
        }
    }

    @Test
    public void failedLoginNotifiesLoginTracker() {
        thrown.expect(BadCredentialsException.class);
        final Authentication auth = authentication(Generator.USERNAME, "unknownPassword");
        try {
            provider.authenticate(auth);
        } finally {
            final ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);
            verify(loginTracker).unsuccessfulLoginAttempt(captor.capture());
            assertEquals(user.getUri(), captor.getValue().getUri());
        }
    }

    @Test
    public void successfulLoginNotifiesLoginTracker() {
        final Authentication auth = authentication(Generator.USERNAME, Generator.PASSWORD);
        final Authentication result = provider.authenticate(auth);
        assertTrue(result.isAuthenticated());
        final ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);
        verify(loginTracker).successfulLoginAttempt(captor.capture());
        assertEquals(user.getUri(), captor.getValue().getUri());
    }

    @Test
    public void authenticateThrowsDisabledExceptionForDisabledAccount() {
        thrown.expect(DisabledException.class);
        thrown.expectMessage("Account is disabled.");
        user.disable();
        personDao.update(user);
        final Authentication auth = authentication(user.getUsername(), Generator.PASSWORD);
        try {
            provider.authenticate(auth);
        } finally {
            final SecurityContext context = SecurityContextHolder.getContext();
            assertNull(context.getAuthentication());
        }
    }

    @Test
    public void authenticateThrowsAuthenticationExceptionForEmptyUsername() {
        thrown.expect(UsernameNotFoundException.class);
        thrown.expectMessage(containsString("Username cannot be empty."));
        final Authentication auth = authentication("", "");
        provider.authenticate(auth);
    }

    @Test
    public void authenticateSetsExtendedUserDetailsForMobileClient() {
        ((MockHttpServletRequest) request).addHeader(Constants.CLIENT_TYPE_HEADER, Constants.CLIENT_TYPE_MOBILE);
        final Authentication auth = authentication(Generator.USERNAME, Generator.PASSWORD);
        provider.authenticate(auth);
        final UserDetails details = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
        assertTrue(details.isExtended());
    }
}
