package cz.cvut.kbss.reporting.security.model;

import cz.cvut.kbss.reporting.environment.generator.Generator;
import cz.cvut.kbss.reporting.model.Person;
import cz.cvut.kbss.reporting.model.Vocabulary;
import cz.cvut.kbss.reporting.security.SecurityConstants.Role;
import org.junit.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static org.junit.Assert.assertTrue;

public class UserDetailsTest {

    @Test
    public void constructorAddsAuthoritiesBasedOnUserTypes() {
        final Person p = Generator.getPerson();
        p.addType(Vocabulary.s_c_regular_user);
        p.addType(Vocabulary.s_c_guest);

        final UserDetails details = new UserDetails(p);
        assertTrue(details.getAuthorities().contains(new SimpleGrantedAuthority(Role.GUEST.getRoleName())));
        assertTrue(details.getAuthorities().contains(new SimpleGrantedAuthority(Role.USER.getRoleName())));
    }
}
