package cz.cvut.kbss.reporting.model;

import cz.cvut.kbss.reporting.environment.generator.Generator;
import cz.cvut.kbss.reporting.environment.generator.OccurrenceReportGenerator;
import cz.cvut.kbss.reporting.environment.util.FactorGraphVerifier;
import cz.cvut.kbss.reporting.model.qam.Question;
import org.junit.Test;

import java.net.URI;
import java.util.Collections;

import static org.junit.Assert.*;

public class OccurrenceTest {

    @Test
    public void newInstanceHasEventInTypes() {
        final Occurrence o = new Occurrence();
        assertTrue(o.getTypes().contains(Vocabulary.s_c_Event));
    }

    @Test
    public void copyOfCopiesOccurrenceWithChildren() {
        final Occurrence original = OccurrenceReportGenerator.generateOccurrenceWithDescendantEvents();
        final Occurrence result = Occurrence.copyOf(original);
        assertNotNull(result);
        verifyFactorGraph(original, result);
    }

    private void verifyFactorGraph(Occurrence original, Occurrence copy) {
        assertEquals(original.getName(), copy.getName());
        new FactorGraphVerifier().verifyFactorGraph(original, copy);
    }

    @Test
    public void copyOfCopiesFactorGraph() {
        final Occurrence original = generateFactorGraph();
        final Occurrence result = Occurrence.copyOf(original);
        assertNotNull(result);
        verifyFactorGraph(original, result);
    }

    private Occurrence generateFactorGraph() {
        final Occurrence o = OccurrenceReportGenerator.generateOccurrenceWithDescendantEvents();
        OccurrenceReportGenerator.generateFactors(o);
        return o;
    }

    @Test
    public void setEventTypesAddsEventTypesToTypesToo() {
        final Occurrence occurrence = OccurrenceReportGenerator.generateOccurrence();
        final URI eventType = Generator.generateEventType();
        occurrence.setEventTypes(Collections.singleton(eventType));
        assertTrue(occurrence.getTypes().contains(eventType.toString()));
    }

    @Test
    public void copyConstructorCopiesQuestionInstance() {
        final Occurrence occurrence = OccurrenceReportGenerator.generateOccurrence();
        final Question question = new Question();
        question.setUri(URI.create(Vocabulary.s_c_question + "instance117"));
        occurrence.setQuestion(question);

        final Occurrence copy = new Occurrence(occurrence);
        assertNotNull(copy.getQuestion());
        assertNotSame(question, copy.getQuestion());
    }
}
