package cz.cvut.kbss.inbas.reporting.model;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class OccurrenceTest {

    @Test
    public void newInstanceHasEventInTypes() {
        final Occurrence o = new Occurrence();
        assertTrue(o.getTypes().contains(Vocabulary.Event));
    }
}