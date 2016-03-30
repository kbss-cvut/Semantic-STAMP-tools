package cz.cvut.kbss.inbas.reporting.model_new;

import org.junit.Test;

import java.net.URI;

import static org.junit.Assert.assertTrue;

public class EventTest {

    /**
     * @see Vocabulary#p_hasEventType
     */
    @Test
    public void setTypeAddsEventTypeUriToInstanceTypesAsWell() {
        final EventType et = new EventType();
        et.setName("2200101 - Runway Incursion by an Aircraft");
        et.setUri(URI.create("http://onto.fel.cvut.cz/ontologies/eccairs-3.4.0.2/vl-a-390/v-2200101"));
        final Event evt = new Event();
        assertTrue(evt.getTypes() == null || evt.getTypes().isEmpty());
        evt.setType(et);
        assertTrue(evt.getTypes().contains(et.getUri().toString()));
    }
}