package cz.cvut.kbss.inbas.reporting.model;

import cz.cvut.kbss.inbas.reporting.environment.util.Generator;
import org.junit.Test;

import java.net.URI;

import static org.junit.Assert.assertTrue;

public class EventTest {

    /**
     * @see Vocabulary#p_hasEventType
     */
    @Test
    public void setTypeAddsEventTypeUriToInstanceTypesAsWell() {
        final URI et = Generator.generateEventType();
        final Event evt = new Event();
        assertTrue(evt.getTypes() == null || evt.getTypes().isEmpty());
        evt.setEventType(et);
        assertTrue(evt.getTypes().contains(et.toString()));
    }
}