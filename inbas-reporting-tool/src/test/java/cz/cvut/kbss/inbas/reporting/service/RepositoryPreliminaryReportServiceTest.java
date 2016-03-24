package cz.cvut.kbss.inbas.reporting.service;

import cz.cvut.kbss.inbas.reporting.environment.util.Environment;
import cz.cvut.kbss.inbas.reporting.environment.util.Generator;
import cz.cvut.kbss.inbas.reporting.model.Aircraft;
import cz.cvut.kbss.inbas.reporting.model.Occurrence;
import cz.cvut.kbss.inbas.reporting.model.Person;
import cz.cvut.kbss.inbas.reporting.model.ReportingPhase;
import cz.cvut.kbss.inbas.reporting.model.reports.*;
import cz.cvut.kbss.inbas.reporting.model.reports.incursions.Intruder;
import cz.cvut.kbss.inbas.reporting.model.reports.incursions.PersonIntruder;
import cz.cvut.kbss.inbas.reporting.model.reports.incursions.RunwayIncursion;
import cz.cvut.kbss.inbas.reporting.persistence.dao.OccurrenceDao;
import cz.cvut.kbss.inbas.reporting.util.Constants;
import cz.cvut.kbss.inbas.reporting.util.Vocabulary;
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
    public void persistSetsFileNumber() throws Exception {
        final PreliminaryReport report = Generator
                .generatePreliminaryReport(Generator.ReportType.WITH_TYPE_ASSESSMENTS);
        report.setFileNumber(null);

        reportService.persist(report);
        assertNotNull(report.getFileNumber());
        assertNotNull(reportService.find(report.getUri()));
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
        one.setEventType(
                new EventType(URI.create("http://krizik.felk.cvut.cz/ontologies/eventTypes#Incursion"), "Incursion"));
        final RunwayIncursion incursion = new RunwayIncursion();
        final Aircraft cleared = new Aircraft();
        cleared.setCallSign("test");
        incursion.setConflictingAircraft(cleared);
        final Intruder intruder = new Intruder(new PersonIntruder());
        incursion.setIntruder(intruder);
        one.setRunwayIncursion(incursion);
        final EventTypeAssessment two = new EventTypeAssessment();
        two.setDescription("Event description");
        two.setEventType(
                new EventType(URI.create("http://krizik.felk.cvut.cz/ontologies/eventTypes#General"), "General"));
        report.setTypeAssessments(new HashSet<>(Arrays.asList(one, two)));
        reportService.persist(report);
        return report;
    }

    private PreliminaryReport initReportWithOccurrence() {
        final PreliminaryReport report = new PreliminaryReport();
        report.setOccurrenceStart(new Date(System.currentTimeMillis() - 10000));
        report.setOccurrenceEnd(new Date());
        report.setOccurrence(Generator.generateOccurrence());
        report.setOccurrenceCategory(Generator.getEventType());
        report.setSeverityAssessment(OccurrenceSeverity.OCCURRENCE_WITHOUT_SAFETY_EFFECT);
        report.setSummary("Narrative");
        report.setFileNumber(System.currentTimeMillis());
        final EventTypeAssessment typeAssessment = new EventTypeAssessment();
        typeAssessment.setDescription("Event type assessment.");
        typeAssessment.setEventType(new EventType(URI.create(
                "http://onto.fel.cvut.cz/ontologies/eccairs-1.3.0.8/V-24-1-31-31-14-390-2000000-2200000-2200100"),
                "2200100 - Runway incursions"));
        report.setTypeAssessments(Collections.singleton(typeAssessment));
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

    @Test
    public void createNewRevisionSetsRevisionNumberAuthorAndDateCreated() throws Exception {
        final PreliminaryReport report = initReportWithOccurrence();
        reportService.persist(report);
        assertNotNull(report.getCreated());

        final Person revisionAuthor = initRevisionAuthor();
        final PreliminaryReport newRevision = reportService.createNewRevision(report);
        assertNotNull(newRevision);
        assertTrue(newRevision.getCreated().compareTo(report.getCreated()) > 0);
        assertEquals(report.getRevision() + 1, newRevision.getRevision().intValue());
        assertTrue(revisionAuthor.valueEquals(newRevision.getAuthor()));
        assertNotNull(reportService.find(newRevision.getUri()));
    }

    private Person initRevisionAuthor() {
        final Person hitGirl = new Person();
        hitGirl.setFirstName("Mindy");
        hitGirl.setLastName("McCready");
        hitGirl.setUsername("hitgirl");
        hitGirl.setPassword("hitgirl");
        hitGirl.generateUri();
        personService.persist(hitGirl);
        Environment.setCurrentUser(hitGirl);
        return hitGirl;
    }

    @Test
    public void newRevisionReusesOccurrenceInstance() throws Exception {
        final PreliminaryReport report = initReportWithOccurrence();
        reportService.persist(report);
        assertNotNull(report.getCreated());

        initRevisionAuthor();
        final PreliminaryReport newRevision = reportService.createNewRevision(report);
        assertNotNull(newRevision);
        assertEquals(report.getOccurrence().getUri(), newRevision.getOccurrence().getUri());
    }

    @Test
    public void newRevisionIsIndependentOfOriginalReport() throws Exception {
        final PreliminaryReport report =
                Generator.generatePreliminaryReport(Generator.ReportType.WITH_TYPE_ASSESSMENTS);
        reportService.persist(report);
        assertNotNull(report.getCreated());

        initRevisionAuthor();
        final PreliminaryReport newRevision = reportService.createNewRevision(report);
        verifyRevisionIndependence(report, newRevision);
    }

    private void verifyRevisionIndependence(PreliminaryReport report, PreliminaryReport newRevision) {
        boolean found;
        for (InitialReport newIr : newRevision.getInitialReports()) {
            found = false;
            for (InitialReport ir : report.getInitialReports()) {
                if (newIr.getText().equals(ir.getText())) {
                    found = true;
                    assertNotEquals(ir.getUri(), newIr.getUri());
                    break;
                }
            }
            assertTrue(found);
        }
        for (EventTypeAssessment newEta : newRevision.getTypeAssessments()) {
            found = false;
            for (EventTypeAssessment eta : report.getTypeAssessments()) {
                if (newEta.getEventType().getId().equals(eta.getEventType().getId())) {
                    found = true;
                    verifyTypeAssessmentsAreIndependent(eta, newEta);
                    break;
                }
            }
            assertTrue(found);
        }
    }

    private void verifyTypeAssessmentsAreIndependent(EventTypeAssessment eta, EventTypeAssessment newEta) {
        assertNotEquals(eta.getUri(), newEta.getUri());
        if (eta.getRunwayIncursion() == null) {
            assertEquals(eta.getDescription(), newEta.getDescription());
        } else {
            assertNotEquals(eta.getRunwayIncursion().getUri(), newEta.getRunwayIncursion().getUri());
            final Intruder i = eta.getRunwayIncursion().getIntruder();
            final Intruder newI = newEta.getRunwayIncursion().getIntruder();
            assertNotEquals(i.getUri(), newI.getUri());
            if (i.getAircraft() != null) {
                assertNotEquals(i.getAircraft().getUri(), newI.getAircraft().getUri());
            } else if (i.getVehicle() != null) {
                assertNotEquals(i.getVehicle().getUri(), newI.getVehicle().getUri());
            } else {
                assertNotEquals(i.getPerson().getUri(), newI.getPerson().getUri());
            }
        }
    }

    @Test
    public void newRevisionOfNewRevisionCanBeCreated() throws Exception {
        final PreliminaryReport report =
                Generator.generatePreliminaryReport(Generator.ReportType.WITH_TYPE_ASSESSMENTS);
        reportService.persist(report);
        assertNotNull(report.getCreated());

        initRevisionAuthor();
        final PreliminaryReport newRevision = reportService.createNewRevision(report);
        final PreliminaryReport anotherRevision = reportService.createNewRevision(newRevision);
        assertTrue(report.getRevision() < newRevision.getRevision());
        assertTrue(newRevision.getRevision() < anotherRevision.getRevision());
        verifyRevisionIndependence(report, newRevision);
        verifyRevisionIndependence(newRevision, anotherRevision);
    }

    private List<PreliminaryReport> initReportRevisions() {
        final int count = Generator.randomInt(10);
        assertTrue(count > 0);
        final List<PreliminaryReport> revisions = new ArrayList<>(count);
        final PreliminaryReport revisionOne = Generator
                .generatePreliminaryReport(Generator.ReportType.WITH_TYPE_ASSESSMENTS);
        revisions.add(revisionOne);
        for (int i = 0; i < count; i++) {
            final PreliminaryReport revision = new PreliminaryReport(revisionOne);
            revision.setAuthor(author);
            revision.setRevision(Constants.INITIAL_REVISION + i + 1);
            if (i % 2 == 0) {   // Set last edited only for every even index
                revision.setLastEdited(new Date(System.currentTimeMillis() + 100000));
            }
            revisions.add(revision);
        }
        reportService.persist(revisions);
        Collections.sort(revisions, (rOne, rTwo) -> rTwo.getRevision() - rOne.getRevision());
        return revisions;
    }
}