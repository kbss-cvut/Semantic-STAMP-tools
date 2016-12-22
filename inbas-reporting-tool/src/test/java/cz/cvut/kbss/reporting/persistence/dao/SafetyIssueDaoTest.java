package cz.cvut.kbss.reporting.persistence.dao;

import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import cz.cvut.kbss.reporting.environment.generator.Generator;
import cz.cvut.kbss.reporting.environment.generator.SafetyIssueReportGenerator;
import cz.cvut.kbss.reporting.model.Event;
import cz.cvut.kbss.reporting.model.safetyissue.SafetyIssue;
import cz.cvut.kbss.reporting.persistence.BaseDaoTestRunner;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static org.junit.Assert.*;

public class SafetyIssueDaoTest extends BaseDaoTestRunner {

    @Autowired
    private SafetyIssueDao dao;

    @Autowired
    private EntityManagerFactory emf;

    @Test
    public void testPersistSafetyIssueWithFactorGraph() {
        final SafetyIssue issue = SafetyIssueReportGenerator.generateSafetyIssueWithDescendantEvents();
        dao.persist(issue);

        final SafetyIssue result = dao.find(issue.getUri());
        assertNotNull(result);
        verifyChildren(issue.getChildren(), result.getChildren());
    }

    private void verifyChildren(Set<Event> original, Set<Event> actual) {
        assertEquals(original.size(), actual.size());
        final List<Event> orig = new ArrayList<>(original);
        final List<Event> act = new ArrayList<>(actual);
        Collections.sort(orig);
        Collections.sort(act);
        for (int i = 0; i < orig.size(); i++) {
            assertEquals(orig.get(i).getUri(), act.get(i).getUri());
            if (orig.get(i).getChildren() != null) {
                verifyChildren(orig.get(i).getChildren(), act.get(i).getChildren());
            }
        }
    }

    @Test
    public void updateRemovesOrphansAndAddsNewEvents() {
        final SafetyIssue issue = SafetyIssueReportGenerator.generateSafetyIssueWithDescendantEvents();
        dao.persist(issue);

        final List<Event> removed = new ArrayList<>();
        final Iterator<Event> it = issue.getChildren().iterator();
        int highestIndex = 0;
        while (it.hasNext()) {
            final Event e = it.next();
            highestIndex = e.getIndex() > highestIndex ? e.getIndex() : highestIndex;
            if (Generator.randomBoolean()) {
                it.remove();
                removed.add(e);
            }
        }
        for (int i = 0; i < Generator.randomInt(2, 5); i++) {
            final Event e = new Event();
            e.setIndex(highestIndex + i);
            e.setEventTypes(Collections.singleton(Generator.generateEventType()));
            issue.addChild(e);
        }
        dao.update(issue);

        final SafetyIssue result = dao.find(issue.getUri());
        assertNotNull(result);
        verifyChildren(issue.getChildren(), result.getChildren());
        final EntityManager em = emf.createEntityManager();
        try {
            removed.forEach(e -> assertNull(em.find(Event.class, e.getUri())));
        } finally {
            em.close();
        }
    }
}
