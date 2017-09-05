package cz.cvut.kbss.reporting.security.model;

import cz.cvut.kbss.reporting.environment.generator.Generator;
import cz.cvut.kbss.reporting.model.Person;
import cz.cvut.kbss.reporting.model.Vocabulary;
import cz.cvut.kbss.reporting.security.SecurityConstants;
import org.junit.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static org.junit.Assert.assertTrue;

public class UserDetailsTest {

    @Test
    public void constructorAddsDefaultRole() {
        final Person p = Generator.getPerson();
        final UserDetails details = new UserDetails(p);
        assertTrue(
                details.getAuthorities().contains(new SimpleGrantedAuthority(SecurityConstants.Role.USER.getName())));
    }

    @Test
    public void constructorAddsRolesMatchingPersonTypes() {
        final Person p = Generator.getPerson();
        p.getTypes().add(Vocabulary.s_c_admin);
        final UserDetails details = new UserDetails(p);
        assertTrue(
                details.getAuthorities().contains(new SimpleGrantedAuthority(SecurityConstants.Role.USER.getName())));
        assertTrue(
                details.getAuthorities().contains(new SimpleGrantedAuthority(SecurityConstants.Role.ADMIN.getName())));
    }
}