package cz.cvut.kbss.reporting.security;

import cz.cvut.kbss.reporting.config.RestConfig;
import cz.cvut.kbss.reporting.environment.config.MockSesamePersistence;
import cz.cvut.kbss.reporting.environment.config.TestSecurityConfig;
import cz.cvut.kbss.reporting.environment.generator.Generator;
import cz.cvut.kbss.reporting.model.Person;
import cz.cvut.kbss.reporting.model.Vocabulary;
import cz.cvut.kbss.reporting.security.model.UserDetails;
import cz.cvut.kbss.reporting.service.BaseServiceTestRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.*;

@ContextConfiguration(classes = {TestSecurityConfig.class, RestConfig.class, MockSesamePersistence.class})
public class OntologyAuthenticationProviderTest extends BaseServiceTestRunner {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    @Qualifier("ontologyAuthenticationProvider")
    private AuthenticationProvider provider;

    private Person user;

    @Before
    public void setUp() throws Exception {
        this.user = persistPerson();
        SecurityContextHolder.setContext(new SecurityContextImpl());
    }

    @After
    public void tearDown() throws Exception {
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
        user.addType(Vocabulary.s_c_locked);
        personDao.update(user);
        final Authentication auth = authentication(user.getUsername(), user.getPassword());
        try {
            provider.authenticate(auth);
        } finally {
            final SecurityContext context = SecurityContextHolder.getContext();
            assertNull(context.getAuthentication());
        }
    }
}
