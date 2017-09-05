package cz.cvut.kbss.reporting.security;

import cz.cvut.kbss.reporting.model.Vocabulary;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class RoleTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void existsReturnsTrueForTypeWithExistingRole() {
        assertTrue(SecurityConstants.Role.exists(Vocabulary.s_c_admin));
    }

    @Test
    public void existsReturnsFalseForUnknownType() {
        assertFalse(SecurityConstants.Role.exists(Vocabulary.s_c_damage));
    }

    @Test
    public void fromTypeReturnsRoleMatchingSpecifiedOntologicalType() {
        final SecurityConstants.Role result = SecurityConstants.Role.fromType(Vocabulary.s_c_admin);
        assertEquals(SecurityConstants.Role.ADMIN, result);
    }

    @Test
    public void fromTypeThrowsIllegalArgumentForUnknownType() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("No role found for type " + Vocabulary.s_c_Agent + ".");
        SecurityConstants.Role.fromType(Vocabulary.s_c_Agent);
    }
}