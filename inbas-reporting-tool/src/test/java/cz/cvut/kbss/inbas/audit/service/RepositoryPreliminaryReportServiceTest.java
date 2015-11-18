package cz.cvut.kbss.inbas.audit.service;

import cz.cvut.kbss.inbas.audit.environment.util.Environment;
import cz.cvut.kbss.inbas.audit.environment.util.Generator;
import cz.cvut.kbss.inbas.audit.model.Aircraft;
import cz.cvut.kbss.inbas.audit.model.Occurrence;
import cz.cvut.kbss.inbas.audit.model.Person;
import cz.cvut.kbss.inbas.audit.model.ReportingPhase;
import cz.cvut.kbss.inbas.audit.model.reports.*;
import cz.cvut.kbss.inbas.audit.model.reports.incursions.Intruder;
import cz.cvut.kbss.inbas.audit.model.reports.incursions.PersonIntruder;
import cz.cvut.kbss.inbas.audit.model.reports.incursions.RunwayIncursion;
import cz.cvut.kbss.inbas.audit.persistence.dao.OccurrenceDao;
import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.util.*;

import static org.junit.Assert.*;

public class RepositoryPreliminaryReportServiceTest extends BaseServiceTestRunner {

    private Person author;

    @Autowired
    private PersonService personService;
    @Autowired
    private OccurrenceDao occurrenceDao;

    @Autowired
    private PreliminaryReportService reportService;

    @Autowired
    private EntityManagerFactory emf;

    @Before
    public void setUp() {
        this.author = Generator.getPerson();
        if (personService.findByUsername(Generator.USERNAME) == null) {
            personService.persist(author);
        }
        Environment.setCurrentUser(author);
    }

    @Test
    public void eventTypeAssessmentsWithRunwayIncursionAreDeletedWhenRemovedFromReport() throws Exception {
        final PreliminaryReport reportWithIncursion = persistReportWithIncursion();
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
        final PreliminaryReport result = reportService.findByKey(reportWithIncursion.getKey());
        assertNotNull(result);
        assertEquals(reportWithIncursion.getTypeAssessments().size(), result.getTypeAssessments().size());
        verifyStatementsDeleted(removed, Vocabulary.EventTypeAssessment);
        verifyNoIncursionLeftoversArePresent();
    }

    private PreliminaryReport persistReportWithIncursion() {
        final PreliminaryReport report = initReportWithOccurrence();
        report.setSummary("test report");
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

    private PreliminaryReport initReportWithOccurrence() {
        final PreliminaryReport report = new PreliminaryReport();
        report.setOccurrence(Generator.generateOccurrence());
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
        final PreliminaryReport report = persistReportWithIncursion();
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

        final PreliminaryReport res = reportService.find(report.getUri());
        assertNotNull(res);
        assertEquals(report.getTypeAssessments().size(), res.getTypeAssessments().size());
        verifyStatementsDeleted(removed, Vocabulary.EventTypeAssessment);
    }

    @Test
    public void correctiveMeasureIsRemovedWhenMissingInUpdatedReport() throws Exception {
        final PreliminaryReport report = persistReportWithCorrectiveMeasure();
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

        final PreliminaryReport res = reportService.find(report.getUri());
        assertNotNull(res);
        assertEquals(report.getCorrectiveMeasures().size(), res.getCorrectiveMeasures().size());
        verifyStatementsDeleted(removed, Vocabulary.CorrectiveMeasure);
    }

    private PreliminaryReport persistReportWithCorrectiveMeasure() {
        final PreliminaryReport report = initReportWithOccurrence();
        report.setSummary("test report");
        final CorrectiveMeasure mOne = new CorrectiveMeasure();
        mOne.setDescription("Corrective measure one");
        final CorrectiveMeasure mTwo = new CorrectiveMeasure();
        mTwo.setDescription("Corrective measure two");
        report.setCorrectiveMeasures(new HashSet<>(Arrays.asList(mOne, mTwo)));
        reportService.persist(report);
        return report;
    }

    @Test
    public void persistSetsAuthorAndCreatedDateOfReport() throws Exception {
        final PreliminaryReport report = initReportWithOccurrence();
        assertNull(report.getAuthor());
        assertNull(report.getCreated());
        reportService.persist(report);

        final PreliminaryReport result = reportService.find(report.getUri());
        assertNotNull(result);
        assertNotNull(result.getAuthor());
        assertEquals(author.getUri(), result.getAuthor().getUri());
        assertNotNull(report.getCreated());
    }

    @Test
    public void updateSetsLastEditedAndLastEditedByOfReport() throws Exception {
        final PreliminaryReport report = initReportWithOccurrence();
        reportService.persist(report);
        final PreliminaryReport toUpdate = reportService.find(report.getUri());
        assertNull(toUpdate.getLastEdited());
        assertNull(toUpdate.getLastEditedBy());
        toUpdate.setSeverityAssessment(OccurrenceSeverity.INCIDENT);
        reportService.update(toUpdate);

        final PreliminaryReport result = reportService.find(report.getUri());
        assertNotNull(result.getLastEdited());
        assertNotNull(result.getLastEditedBy());
        assertEquals(author.getUri(), result.getLastEditedBy().getUri());
    }

    @Test
    public void persistUpdatesOccurrencePhaseFromInitialToPreliminary() throws Exception {
        final PreliminaryReport report = initReportWithOccurrence();
        report.getOccurrence().setReportingPhase(ReportingPhase.INITIAL);
        reportService.persist(report);

        final Occurrence occurrence = occurrenceDao.find(report.getOccurrence().getUri());
        assertNotNull(occurrence);
        assertEquals(ReportingPhase.PRELIMINARY, occurrence.getReportingPhase());
    }
}