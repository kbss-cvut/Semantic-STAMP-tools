package cz.cvut.kbss.reporting.security.model;

import cz.cvut.kbss.reporting.environment.generator.Generator;
import cz.cvut.kbss.reporting.model.Person;
import cz.cvut.kbss.reporting.model.Vocabulary;
import cz.cvut.kbss.reporting.security.SecurityConstants;
import org.junit.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static org.junit.Assert.assertEquals;
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
        p.addType(Vocabulary.s_c_admin);
        final UserDetails details = new UserDetails(p);
        assertTrue(
                details.getAuthorities().contains(new SimpleGrantedAuthority(SecurityConstants.Role.USER.getName())));
        assertTrue(
                details.getAuthorities().contains(new SimpleGrantedAuthority(SecurityConstants.Role.ADMIN.getName())));
    }

    @Test
    public void detailsOfSamePersonAreEqual() {
        final Person pOne = Generator.getPerson();
        final Person pTwo = Generator.getPerson();
        assertEquals(pOne.getUri(), pTwo.getUri());
        assertTrue(pOne.nameEquals(pTwo));
        final UserDetails detailsOne = new UserDetails(pOne);
        final UserDetails detailsTwo = new UserDetails(pTwo);
        assertEquals(detailsOne, detailsTwo);
    }
}