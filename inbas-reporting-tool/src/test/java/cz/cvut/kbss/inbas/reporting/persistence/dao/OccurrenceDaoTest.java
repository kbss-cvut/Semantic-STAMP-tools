package cz.cvut.kbss.inbas.reporting.persistence.dao;

import cz.cvut.kbss.inbas.reporting.environment.util.Generator;
import cz.cvut.kbss.inbas.reporting.model.Event;
import cz.cvut.kbss.inbas.reporting.model.Factor;
import cz.cvut.kbss.inbas.reporting.model.Occurrence;
import cz.cvut.kbss.inbas.reporting.model.qam.Answer;
import cz.cvut.kbss.inbas.reporting.model.qam.Question;
import cz.cvut.kbss.inbas.reporting.persistence.BaseDaoTestRunner;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class OccurrenceDaoTest extends BaseDaoTestRunner {

    private static final int MAX_DEPTH = 5;

    @Autowired
    private OccurrenceDao dao;

    @Autowired
    private EntityManagerFactory emf;

    @Test
    public void persistPersistsAllEventsFromFactorGraph() {
        final Occurrence occurrence = Generator.generateOccurrence();
        final Set<Event> events = new HashSet<>();
        generateFactorGraph(occurrence, events, 0);
        dao.persist(occurrence);

        final Occurrence res = dao.find(occurrence.getUri());
        assertNotNull(res);
        assertEquals(occurrence.getKey(), res.getKey());
        final EntityManager em = emf.createEntityManager();
        try {
            for (Event e : events) {
                assertNotNull(em.find(Event.class, e.getUri()));
            }
        } finally {
            em.close();
        }
    }

    private void generateFactorGraph(Occurrence occurrence, Set<Event> events, int depth) {
        for (int i = 0; i < Generator.randomInt(5); i++) {
            final Event evt = event(events);
            occurrence.addChild(evt);
            generateFactorGraph(evt, events, depth + 1);
        }
        for (int i = 0; i < Generator.randomInt(5); i++) {
            final Factor f = factor(events);
            occurrence.addFactor(f);
            generateFactorGraph(f.getEvent(), events, depth + 1);
        }
    }

    private Event event(Set<Event> events) {
        final Event evt = new Event();
        evt.setStartTime(new Date());
        evt.setEndTime(new Date());
        evt.setEventType(Generator.generateEventType());
        events.add(evt);
        return evt;
    }

    private Factor factor(Set<Event> events) {
        final Factor f = new Factor();
        f.setEvent(event(events));
        f.setType(Generator.randomFactorType());
        return f;
    }

    private void generateFactorGraph(Event event, Set<Event> events, int depth) {
        if (depth == MAX_DEPTH) {
            return;
        }
        for (int i = 0; i < Generator.randomInt(5); i++) {
            final Event evt = event(events);
            event.addChild(evt);
            generateFactorGraph(evt, events, depth + 1);
        }
        for (int i = 0; i < Generator.randomInt(5); i++) {
            final Factor f = factor(events);
            event.addFactor(f);
            generateFactorGraph(f.getEvent(), events, depth + 1);
        }
    }

    @Test
    public void persistPersistsQuestionsAndAnswersOfEvents() {
        final Occurrence occurrence = Generator.generateOccurrence();
        generateQuestions(occurrence);
        dao.persist(occurrence);
        final EntityManager em = emf.createEntityManager();
        try {
            verifyQuestions(occurrence.getQuestion(), question -> {
                final Question res = em.find(Question.class, question.getUri());
                assertNotNull(res);
                assertEquals(question.getTypes().size(), res.getTypes().size());
                assertTrue(question.getTypes().containsAll(res.getTypes()));
                final Set<URI> childUris = question.getSubQuestions().stream().map(Question::getUri)
                                                   .collect(Collectors.toSet());
                assertEquals(question.getSubQuestions().size(), res.getSubQuestions().size());
                res.getSubQuestions().forEach(sq -> assertTrue(childUris.contains(sq.getUri())));
                assertEquals(question.getAnswers().size(), res.getAnswers().size());
                // Assuming only one answer, the string representation can be used for comparison
                assertEquals(question.getAnswers().iterator().next().toString(),
                        res.getAnswers().iterator().next().toString());
            });
        } finally {
            em.close();
        }
    }

    private void generateQuestions(Occurrence occurrence) {
        final int maxDepth = Generator.randomInt(10);
        final Question root = question();
        occurrence.setQuestion(root);
        root.setAnswers(Collections.singleton(answer()));
        generateQuestions(root, 0, maxDepth);
    }

    private Question question() {
        final Question q = new Question();
        q.getTypes().add(Generator.generateEventType().toString());
        return q;
    }

    private Answer answer() {
        final Answer a = new Answer();
        if (Generator.randomBoolean()) {
            a.setTextValue("RandomTextValue" + Generator.randomInt());
        } else {
            a.setCodeValue(Generator.generateEventType());
        }
        return a;
    }

    private void generateQuestions(Question parent, int depth, int maxDepth) {
        if (depth >= maxDepth) {
            return;
        }
        for (int i = 0; i < Generator.randomInt(5); i++) {
            final Question child = question();
            child.setAnswers(Collections.singleton(answer()));
            parent.getSubQuestions().add(child);
            generateQuestions(child, depth + 1, maxDepth);
        }
    }

    private void verifyQuestions(Question question, Consumer<Question> verification) {
        verification.accept(question);
        question.getSubQuestions().forEach(sq -> verifyQuestions(sq, verification));
    }

    @Test
    public void removeDeletesAllQuestionsAndAnswersAsWell() {
        final Occurrence occurrence = Generator.generateOccurrence();
        generateQuestions(occurrence);
        dao.persist(occurrence);

        dao.remove(occurrence);
        final EntityManager em = emf.createEntityManager();
        try {
            verifyQuestions(occurrence.getQuestion(), question -> {
                assertNull(em.find(Question.class, question.getUri()));
                question.getAnswers().forEach(a -> assertNull(em.find(Answer.class, a.getUri())));
            });
        } finally {
            em.close();
        }
    }
}