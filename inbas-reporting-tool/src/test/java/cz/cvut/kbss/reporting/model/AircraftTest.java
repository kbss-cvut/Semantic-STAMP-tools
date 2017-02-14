package cz.cvut.kbss.reporting.model;

import cz.cvut.kbss.reporting.environment.generator.Generator;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.*;

public class AircraftTest {

    @Test
    public void copyConstructorCopiesAttributes() {
        final Aircraft original = new Aircraft();
        original.setUri(Generator.generateUri());
        original.setOperator(Generator.generateOrganization());
        original.setTypes(Collections.singleton(Vocabulary.s_c_Aircraft + "/Boeing-787"));

        final Aircraft copy = new Aircraft(original);
        assertNull(copy.getUri());
        assertEquals(original.getOperator(), copy.getOperator());
        assertEquals(original.getTypes(), copy.getTypes());
    }
}