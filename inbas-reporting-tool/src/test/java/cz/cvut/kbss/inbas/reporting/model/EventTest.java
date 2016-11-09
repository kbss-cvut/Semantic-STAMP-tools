package cz.cvut.kbss.inbas.reporting.model;

import cz.cvut.kbss.inbas.reporting.environment.generator.Generator;
import cz.cvut.kbss.inbas.reporting.model.qam.Question;
import org.junit.Test;

import java.net.URI;
import java.util.Collections;

import static org.junit.Assert.*;

public class EventTest {

    @Test
    public void setTypeAddsEventTypeUriToInstanceTypesAsWell() {
        final URI et = Generator.generateEventType();
        final Event evt = new Event();
        assertTrue(evt.getTypes() == null || evt.getTypes().isEmpty());
        evt.setEventTypes(Collections.singleton(et));
        assertTrue(evt.getTypes().contains(et.toString()));
    }

    @Test
    public void copyConstructorCreatesNewFormInstance() {
        final URI et = Generator.generateEventType();
        final Event evt = new Event();
        evt.setEventTypes(Collections.singleton(et));
        final Question q = new Question();
        q.setUri(Generator.generateEventType());
        evt.setQuestion(q);

        final Event copy = new Event(evt);
        assertNotNull(copy.getQuestion());
        assertNotSame(q, copy.getQuestion());
    }
}