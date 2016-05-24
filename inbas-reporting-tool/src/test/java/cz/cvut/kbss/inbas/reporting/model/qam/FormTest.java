package cz.cvut.kbss.inbas.reporting.model.qam;

import cz.cvut.kbss.inbas.reporting.environment.util.Generator;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.*;

public class FormTest {

    @Test
    public void copyConstructorCopiesQuestions() {
        final Form f = new Form();
        f.setUri(Generator.generateEventType());
        for (int i = 0; i < Generator.randomInt(10); i++) {
            final Question q = new Question();
            q.setTypes(Collections.singleton(Generator.generateEventType()));
            f.getSubQuestions().add(q);
        }

        final Form copy = new Form(f);
        assertNull(copy.getUri());
        assertEquals(f.getSubQuestions().size(), copy.getSubQuestions().size());
        for (Question q : f.getSubQuestions()) {
            for (Question qq : copy.getSubQuestions()) {
                assertNotSame(q, qq);
            }
        }
    }
}