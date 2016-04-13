package cz.cvut.kbss.inbas.reporting.model;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class PersonTest {

    @Test
    public void newInstanceHasAgentInTypes() {
        final Person p = new Person();
        assertTrue(p.getTypes().contains(Vocabulary.Agent));
    }
}