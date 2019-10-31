package cz.cvut.kbss.reporting.model.util;

import cz.cvut.kbss.reporting.environment.generator.Generator;
import cz.cvut.kbss.reporting.environment.generator.OccurrenceReportGenerator;
import cz.cvut.kbss.reporting.model.Event;
import cz.cvut.kbss.reporting.model.qam.Question;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;

import static org.junit.Assert.*;

public class QuestionUnifierTest {

    @Test
    public void unifyQuestionsDoesNothingForNullRootQuestion() {
        final Event event = OccurrenceReportGenerator.generateEvent();
        assertNull(event.getQuestion());
        new QuestionUnifier().unifyQuestions(event);
        assertNull(event.getQuestion());
    }

    @Test
    public void unifyQuestionsUnifiesTwoSiblingQuestionOccurrences() {
        final Event event = OccurrenceReportGenerator.generateEvent();
        final Question qOne = Generator.question();
        final Question qTwo = Generator.question();
        qTwo.setUri(qOne.getUri());
        final Question root = Generator.question();
        root.setSubQuestions(new LinkedHashSet<>(Arrays.asList(qOne, qTwo)));
        event.setQuestion(root);
        new QuestionUnifier().unifyQuestions(event);
        assertEquals(1, event.getQuestion().getSubQuestions().size());
        assertSame(qOne, event.getQuestion().getSubQuestions().iterator().next());
    }

    @Test
    public void unifyQuestionsReusesQuestionFromOneSubtreeInAnother() {
        final Event event = OccurrenceReportGenerator.generateEvent();
        final Question root = Generator.question();
        event.setQuestion(root);
        final Question qOne = Generator.question();
        final Question qTwo = Generator.question();
        root.setSubQuestions(new LinkedHashSet<>(Arrays.asList(qOne, qTwo)));
        final Question reused = Generator.question();
        qOne.setSubQuestions(Collections.singleton(reused));
        final Question qFour = Generator.question();
        final Question qFive = Generator.question();
        qFive.setUri(reused.getUri());
        qTwo.setSubQuestions(new LinkedHashSet<>(Arrays.asList(qFour, qFive)));

        new QuestionUnifier().unifyQuestions(event);
        assertTrue(qTwo.getSubQuestions().contains(reused));
        assertFalse(qTwo.getSubQuestions().contains(qFive));
    }

    @Test
    public void unifyQuestionsBreaksQuestionSubQuestionCycle() {
        final Event event = OccurrenceReportGenerator.generateEvent();
        final Question root = Generator.question();
        event.setQuestion(root);
        final Question selfRef = new Question(root);
        selfRef.setUri(root.getUri());
        root.setSubQuestions(new HashSet<>(Collections.singleton(selfRef)));

        new QuestionUnifier().unifyQuestions(event);
        assertFalse(root.getSubQuestions().contains(root));
    }
}