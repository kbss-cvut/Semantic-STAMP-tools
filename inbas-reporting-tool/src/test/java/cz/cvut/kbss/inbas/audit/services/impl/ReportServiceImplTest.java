package cz.cvut.kbss.inbas.audit.services.impl;

import cz.cvut.kbss.inbas.audit.model.Aircraft;
import cz.cvut.kbss.inbas.audit.model.Person;
import cz.cvut.kbss.inbas.audit.model.reports.*;
import cz.cvut.kbss.inbas.audit.model.reports.incursions.Intruder;
import cz.cvut.kbss.inbas.audit.model.reports.incursions.PersonIntruder;
import cz.cvut.kbss.inbas.audit.model.reports.incursions.RunwayIncursion;
import cz.cvut.kbss.inbas.audit.services.BaseServiceTestRunner;
import cz.cvut.kbss.inbas.audit.services.PersonService;
import cz.cvut.kbss.inbas.audit.services.ReportService;
import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.util.*;

import static org.junit.Assert.*;

/**
 * @author ledvima1
 */
public class ReportServiceImplTest extends BaseServiceTestRunner {

    private static final String USERNAME = "halsey@unsc.org";

    private Person author;

    @Autowired
    private PersonService personService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private EntityManagerFactory emf;

    @Before
    public void setUp() {
        this.author = new Person();
        author.setFirstName("Catherine");
        author.setLastName("Halsey");
        author.setUsername("halsey@unsc.org");
        author.setPassword("john117");
        author.generateUri();
        if (personService.findByUsername(USERNAME) == null) {
            personService.persist(author);
        }
    }

    @Test
    public void eventTypeAssessmentsWithRunwayIncursionAreDeletedWhenRemovedFromReport() throws Exception {
        final OccurrenceReport reportWithIncursion = persistReportWithIncursion();
        assertNotNull(reportWithIncursion.getKey());
        final List<EventTypeAssessment> removed = new ArrayList<>();
        final Iterator<EventTypeAssessment> it = reportWithIncursion.getTypeAssessments().iterator();
        while (it.hasNext()) {
            final EventTypeAssessment a = it.next();
            if (a.getRunwayIncursion() != null) {
                removed.add(a);
                it.remove();
            }
        }
        reportService.update(reportWithIncursion);

        // Check there is no incursion and no leftovers
        final OccurrenceReport result = reportService.findByKey(reportWithIncursion.getKey());
        assertNotNull(result);
        assertEquals(reportWithIncursion.getTypeAssessments().size(), result.getTypeAssessments().size());
        verifyStatementsDeleted(removed, Vocabulary.EventTypeAssessment);
        verifyNoIncursionLeftoversArePresent();
    }

    private OccurrenceReport persistReportWithIncursion() {
        final OccurrenceReport report = new OccurrenceReport();
        report.setAuthor(author);
        report.setOccurrenceTime(new Date());
        report.setInitialReport("test report");
        final EventTypeAssessment one = new EventTypeAssessment();
        one.setEventType(new EventType(URI.create("http://krizik.felk.cvut.cz/ontologies/eventTypes#Incursion")));
        final RunwayIncursion incursion = new RunwayIncursion();
        final Aircraft cleared = new Aircraft();
        cleared.setCallSign("test");
        incursion.setConflictingAircraft(cleared);
        final Intruder intruder = new Intruder(new PersonIntruder());
        incursion.setIntruder(intruder);
        one.setRunwayIncursion(incursion);
        final EventTypeAssessment two = new EventTypeAssessment();
        two.setDescription("Event description");
        two.setEventType(new EventType(URI.create("http://krizik.felk.cvut.cz/ontologies/eventTypes#General")));
        report.setTypeAssessments(new HashSet<>(Arrays.asList(one, two)));
        reportService.persist(report);
        return report;
    }

    private <T extends ReportingStatement> void verifyStatementsDeleted(Collection<T> removed, String type) {
        final EntityManager em = emf.createEntityManager();
        try {
            for (T a : removed) {
                assertFalse(em.createNativeQuery(
                        "ASK { <" + a.getUri().toString() + "> a <" + type + "> . }", Boolean.class).getSingleResult());
            }
        } finally {
            em.close();
        }
    }

    private void verifyNoIncursionLeftoversArePresent() throws Exception {
        final EntityManager em = emf.createEntityManager();
        try {
            assertFalse(em.createNativeQuery("ASK { ?x a <" + Vocabulary.Aircraft + "> . }", Boolean.class)
                          .getSingleResult());
            assertFalse(em.createNativeQuery("ASK { ?x a <" + Vocabulary.PersonIntruder + "> . }", Boolean.class)
                          .getSingleResult());
            assertFalse(em.createNativeQuery("ASK { ?x a <" + Vocabulary.RunwayIncursion + "> .}", Boolean.class)
                          .getSingleResult());
            assertFalse(em.createNativeQuery("ASK { ?x a <" + Vocabulary.Location + "> .}", Boolean.class)
                          .getSingleResult());
        } finally {
            em.close();
        }
    }

    @Test
    public void generalEventTypeAssessmentIsRemovedWhenMissingInUpdatedReport() throws Exception {
        final OccurrenceReport report = persistReportWithIncursion();
        final List<EventTypeAssessment> removed = new ArrayList<>();
        final Iterator<EventTypeAssessment> it = report.getTypeAssessments().iterator();
        while (it.hasNext()) {
            final EventTypeAssessment a = it.next();
            if (a.getRunwayIncursion() == null) {
                removed.add(a);
                it.remove();
            }
        }
        reportService.update(report);

        final OccurrenceReport res = reportService.find(report.getUri());
        assertNotNull(res);
        assertEquals(report.getTypeAssessments().size(), res.getTypeAssessments().size());
        verifyStatementsDeleted(removed, Vocabulary.EventTypeAssessment);
    }

    @Test
    public void correctiveMeasureIsRemovedWhenMissingInUpdatedReport() throws Exception {
        final OccurrenceReport report = persistReportWithCorrectiveMeasure();
        boolean remove = false;
        final List<CorrectiveMeasure> removed = new ArrayList<>();
        final Iterator<CorrectiveMeasure> it = report.getCorrectiveMeasures().iterator();
        while (it.hasNext()) {
            final CorrectiveMeasure m = it.next();
            if (remove) {
                removed.add(m);
                it.remove();
            }
            remove = !remove;
        }
        reportService.update(report);

        final OccurrenceReport res = reportService.find(report.getUri());
        assertNotNull(res);
        assertEquals(report.getCorrectiveMeasures().size(), res.getCorrectiveMeasures().size());
        verifyStatementsDeleted(removed, Vocabulary.CorrectiveMeasure);
    }

    private OccurrenceReport persistReportWithCorrectiveMeasure() {
        final OccurrenceReport report = new OccurrenceReport();
        report.setAuthor(author);
        report.setOccurrenceTime(new Date());
        report.setInitialReport("test report");
        final CorrectiveMeasure mOne = new CorrectiveMeasure();
        mOne.setDescription("Corrective measure one");
        final CorrectiveMeasure mTwo = new CorrectiveMeasure();
        mTwo.setDescription("Corrective measure two");
        report.setCorrectiveMeasures(new HashSet<>(Arrays.asList(mOne, mTwo)));
        reportService.persist(report);
        return report;
    }
}