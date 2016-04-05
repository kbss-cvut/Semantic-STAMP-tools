package cz.cvut.kbss.inbas.reporting.model_new;

import cz.cvut.kbss.inbas.reporting.environment.util.Generator;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class OccurrenceTest {

    @Test
    public void newInstanceHasEventInTypes() {
        final Occurrence o = new Occurrence();
        assertTrue(o.getTypes().contains(Vocabulary.Event));
    }

    /**
     * @see Vocabulary#p_hasEventType
     */
    @Test
    public void setTypeAddsEventTypeUriToInstanceTypesAsWell() {
        final EventType et = Generator.generateEventType();
        final Occurrence occurrence = new Occurrence();
        occurrence.setType(et);
        assertTrue(occurrence.getTypes().contains(et.getUri().toString()));
    }
}