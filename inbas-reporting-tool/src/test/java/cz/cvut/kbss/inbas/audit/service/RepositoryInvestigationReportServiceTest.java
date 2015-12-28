package cz.cvut.kbss.inbas.audit.service;

import cz.cvut.kbss.inbas.audit.environment.util.Environment;
import cz.cvut.kbss.inbas.audit.environment.util.Generator;
import cz.cvut.kbss.inbas.audit.model.Person;
import cz.cvut.kbss.inbas.audit.model.ReportingPhase;
import cz.cvut.kbss.inbas.audit.model.reports.*;
import cz.cvut.kbss.inbas.audit.model.reports.incursions.Intruder;
import cz.cvut.kbss.inbas.audit.model.reports.incursions.RunwayIncursion;
import cz.cvut.kbss.inbas.audit.persistence.dao.PersonDao;
import cz.cvut.kbss.inbas.audit.persistence.dao.PreliminaryReportDao;
import cz.cvut.kbss.inbas.audit.service.repository.RepositoryInvestigationReportService;
import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.util.Iterator;
import java.util.logging.LogManager;

import static org.junit.Assert.*;

public class RepositoryInvestigationReportServiceTest extends BaseServiceTestRunner {

    @Autowired
    private PersonDao personDao;
    @Autowired
    private PreliminaryReportDao preliminaryReportDao;

    @Autowired
    private RepositoryInvestigationReportService service;

    @Autowired
    private EntityManagerFactory emf;

    public static void setUpBeforeClass() throws Exception {
        LogManager.getLogManager().readConfiguration(
                RepositoryInvestigationReportServiceTest.class.getResourceAsStream("/logging.properties"));
    }

    @Before
    public void setUp() throws Exception {
        final Person user = Generator.getPerson();
        if (personDao.findByUsername(user.getUsername()) == null) {
            personDao.persist(user);
        }
        Environment.setCurrentUser(user);
    }

    @Test
    public void testCreateInvestigationFromPreliminaryReportWithoutEventTypeAssessments() throws Exception {
        final PreliminaryReport report = Generator
                .generatePreliminaryReport(Generator.ReportType.WITHOUT_TYPE_ASSESSMENTS);
        preliminaryReportDao.persist(report);
        final InvestigationReport result = service.createFromPreliminaryReport(report);
        assertNotNull(result);
        assertEquals(report.getOccurrenceStart(), result.getOccurrenceStart());
        assertEquals(report.getOccurrenceEnd(), result.getOccurrenceEnd());
        assertEquals(report.getSeverityAssessment(), result.getSeverityAssessment());
        assertEquals(report.getCorrectiveMeasures().size(), result.getCorrectiveMeasures().size());
        boolean found;
        for (CorrectiveMeasure cm : report.getCorrectiveMeasures()) {
            found = false;
            for (CorrectiveMeasure cmActual : result.getCorrectiveMeasures()) {
                if (cm.getDescription().equals(cmActual.getDescription())) {
                    found = true;
                    break;
                }
            }
            assertTrue(found);
        }
        assertEquals(report.getInitialReports().size(), result.getInitialReports().size());
        for (InitialReport ir : report.getInitialReports()) {
            found = false;
            for (InitialReport irActual : result.getInitialReports()) {
                if (ir.getText().equals(irActual.getText())) {
                    found = true;
                    break;
                }
            }
            assertTrue(found);
        }
    }

    @Test
    public void testCreateInvestigationFromPreliminaryReportWithEventTypeAssessments() throws Exception {
        final PreliminaryReport report = Generator
                .generatePreliminaryReport(Generator.ReportType.WITH_TYPE_ASSESSMENTS);
        preliminaryReportDao.persist(report);

        final InvestigationReport result = service.createFromPreliminaryReport(report);
        assertNotNull(result);
        assertNotNull(result.getRootFactor());
        assertEquals(report.getTypeAssessments().size(), result.getRootFactor().getChildren().size());
        for (EventTypeAssessment eta : report.getTypeAssessments()) {
            boolean found = false;
            for (Factor f : result.getRootFactor().getChildren()) {
                assertNotNull(f.getAssessment());
                final EventTypeAssessment fAssessment = f.getAssessment();
                assertNotEquals(eta.getUri(), fAssessment.getUri());
                if (!eta.getEventType().getId().equals(fAssessment.getEventType().getId())) {
                    continue;
                }
                found = true;
                if (eta.getDescription() != null) {
                    assertEquals(eta.getDescription(), fAssessment.getDescription());
                } else {
                    verifyIncursion(eta.getRunwayIncursion(), fAssessment.getRunwayIncursion());
                }
            }
            assertTrue(found);
        }
    }

    private void verifyIncursion(RunwayIncursion expected, RunwayIncursion actual) {
        assertEquals(expected.getLocation().getUri(), actual.getLocation().getUri());
        assertEquals(expected.getLowVisibilityProcedure(), actual.getLowVisibilityProcedure());
        assertNotEquals(expected.getUri(), actual.getUri());
        assertNotEquals(expected.getIntruder().getUri(), actual.getIntruder().getUri());
        final Intruder expIntruder = expected.getIntruder();
        final Intruder actIntruder = actual.getIntruder();
        // The intruder instances should be newly created
        if (expIntruder.getAircraft() != null) {
            assertNotNull(actIntruder.getAircraft());
            assertNotEquals(expIntruder.getAircraft().getUri(), actIntruder.getAircraft().getUri());
        } else if (expIntruder.getVehicle() != null) {
            assertNotNull(actIntruder.getVehicle());
            assertNotEquals(expIntruder.getVehicle().getUri(), actIntruder.getVehicle().getUri());
        } else {
            assertNotNull(actIntruder.getPerson());
            assertNotEquals(expIntruder.getPerson().getUri(), actIntruder.getPerson().getUri());
        }
    }

    @Test
    public void createInvestigationFromPreliminaryReportWithoutTypeAssessmentsCreatesRootFactor() throws Exception {
        final PreliminaryReport report = Generator
                .generatePreliminaryReport(Generator.ReportType.WITHOUT_TYPE_ASSESSMENTS);
        preliminaryReportDao.persist(report);
        final InvestigationReport result = service.createFromPreliminaryReport(report);
        assertNotNull(result);
        assertNotNull(result.getRootFactor());
        assertEquals(report.getOccurrenceStart(), result.getRootFactor().getStartTime());
        assertEquals(report.getOccurrenceEnd(), result.getRootFactor().getEndTime());
    }

    @Test
    public void updateSetsLastEditedAndLastEditedByFields() throws Exception {
        final InvestigationReport toUpdate = createInvestigation(Generator.ReportType.WITHOUT_TYPE_ASSESSMENTS);
        assertNull(toUpdate.getLastEditedBy());
        assertNull(toUpdate.getLastEdited());
        toUpdate.getOccurrence().setName("Updated name");

        service.update(toUpdate);
        final InvestigationReport result = service.find(toUpdate.getUri());
        assertNotNull(result);
        assertNotNull(result.getLastEdited());
        assertNotNull(result.getLastEditedBy());
        assertTrue(Environment.getCurrentUser().valueEquals(result.getLastEditedBy()));
        assertEquals(toUpdate.getOccurrence().getName(), result.getOccurrence().getName());
    }

    private InvestigationReport createInvestigation(Generator.ReportType type) {
        final PreliminaryReport report = Generator
                .generatePreliminaryReport(type);
        preliminaryReportDao.persist(report);
        return service.createFromPreliminaryReport(report);
    }

    @Test
    public void removesObsoleteFactorsOnUpdate() throws Exception {
        final InvestigationReport toUpdate = createInvestigation(Generator.ReportType.WITH_TYPE_ASSESSMENTS);
        final Iterator<Factor> it = toUpdate.getRootFactor().getChildren().iterator();
        removeEveryOther(it);
        service.update(toUpdate);
        final InvestigationReport result = service.find(toUpdate.getUri());
        assertEquals(toUpdate.getRootFactor().getChildren().size(), result.getRootFactor().getChildren().size());
        final int factorCount = countFactorsInHierarchy(toUpdate.getRootFactor());
        final int factorsInRepo = countInstancesInRepo(URI.create(Vocabulary.Factor));
        assertEquals(factorCount, factorsInRepo);
    }

    private void removeEveryOther(Iterator<?> it) {
        boolean remove = true;
        while (it.hasNext()) {
            it.next();
            if (remove) {
                it.remove();
                remove = false;
            } else {
                remove = true;
            }
        }
    }

    private int countFactorsInHierarchy(Factor root) {
        int count = 1;
        if (root.getChildren() != null) {
            for (Factor f : root.getChildren()) {
                count += (countFactorsInHierarchy(f));
            }
        }
        return count;
    }

    private int countInstancesInRepo(URI type) {
        final EntityManager em = emf.createEntityManager();
        try {
            return em.createNativeQuery("SELECT ?x WHERE { ?x a ?type . }").setParameter("type", type).getResultList()
                     .size();
        } finally {
            em.close();
        }
    }

    @Test
    public void updateRemovesObsoleteCorrectiveMeasures() throws Exception {
        final InvestigationReport toUpdate = createInvestigation(Generator.ReportType.WITHOUT_TYPE_ASSESSMENTS);
        final Iterator<CorrectiveMeasure> it = toUpdate.getCorrectiveMeasures().iterator();
        removeEveryOther(it);
        service.update(toUpdate);

        final InvestigationReport result = service.find(toUpdate.getUri());
        assertEquals(toUpdate.getCorrectiveMeasures().size(), result.getCorrectiveMeasures().size());
        assertEquals(toUpdate.getCorrectiveMeasures().size(), countCorrectiveMeasures(result));
    }

    private int countCorrectiveMeasures(InvestigationReport owner) {
        final EntityManager em = emf.createEntityManager();
        try {
            return em.createNativeQuery("SELECT ?x WHERE { ?owner <" + Vocabulary.p_hasCorrectiveMeasure + "> ?x . }")
                     .setParameter("owner", owner.getUri()).getResultList()
                     .size();
        } finally {
            em.close();
        }
    }

    @Test
    public void updatePersistsNewlyAddedCorrectiveMeasures() throws Exception {
        final InvestigationReport toUpdate = createInvestigation(Generator.ReportType.WITHOUT_TYPE_ASSESSMENTS);
        toUpdate.getCorrectiveMeasures().add(new CorrectiveMeasure("Added corrective measure number 1."));
        toUpdate.getCorrectiveMeasures().add(new CorrectiveMeasure("Added corrective measure number 2."));

        service.update(toUpdate);
        final InvestigationReport result = service.find(toUpdate.getUri());
        assertEquals(toUpdate.getCorrectiveMeasures().size(), result.getCorrectiveMeasures().size());
    }

    @Test
    public void removeDeletesAllFactorsAndCleansUpAfterInvestigation() throws Exception {
        final InvestigationReport toRemove = createInvestigation(Generator.ReportType.WITH_TYPE_ASSESSMENTS);
        assertNotNull(emf.createEntityManager().find(Factor.class, toRemove.getRootFactor().getUri()));
        service.remove(toRemove);
        final EntityManager em = emf.createEntityManager();
        try {
            final boolean res = em.createNativeQuery("ASK WHERE { ?instance ?x ?y . }", Boolean.class)
                                  .setParameter("instance", toRemove.getUri()).getSingleResult();
            assertFalse(res);
        } finally {
            em.close();
        }
    }

    @Test
    public void investigationCreatedFromPreliminaryReportReusesOccurrenceInstance() {
        final PreliminaryReport report = Generator
                .generatePreliminaryReport(Generator.ReportType.WITHOUT_TYPE_ASSESSMENTS);
        preliminaryReportDao.persist(report);
        final InvestigationReport created = service.createFromPreliminaryReport(report);
        assertNotNull(created);
        assertEquals(report.getOccurrence().getUri(), created.getOccurrence().getUri());
    }

    @Test
    public void creatingInvestigationFromPreliminarySetsOccurrencePhaseToInvestigation() {
        final PreliminaryReport report = Generator
                .generatePreliminaryReport(Generator.ReportType.WITHOUT_TYPE_ASSESSMENTS);
        preliminaryReportDao.persist(report);
        final InvestigationReport created = service.createFromPreliminaryReport(report);
        assertNotNull(created);
        emf.getCache().evictAll();

        final InvestigationReport result = service.find(created.getUri());
        assertEquals(ReportingPhase.INVESTIGATION, result.getOccurrence().getReportingPhase());
    }
}
