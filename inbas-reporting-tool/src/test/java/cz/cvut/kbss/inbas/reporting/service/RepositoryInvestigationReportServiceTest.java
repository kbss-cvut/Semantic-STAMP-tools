package cz.cvut.kbss.inbas.reporting.service;

import cz.cvut.kbss.inbas.reporting.environment.util.Environment;
import cz.cvut.kbss.inbas.reporting.environment.util.Generator;
import cz.cvut.kbss.inbas.reporting.model.ReportingPhase;
import cz.cvut.kbss.inbas.reporting.model.reports.*;
import cz.cvut.kbss.inbas.reporting.model.reports.incursions.Intruder;
import cz.cvut.kbss.inbas.reporting.model.reports.incursions.RunwayIncursion;
import cz.cvut.kbss.inbas.reporting.persistence.dao.InvestigationReportDao;
import cz.cvut.kbss.inbas.reporting.persistence.dao.OccurrenceDao;
import cz.cvut.kbss.inbas.reporting.persistence.dao.PreliminaryReportDao;
import cz.cvut.kbss.inbas.reporting.service.repository.RepositoryInvestigationReportService;
import cz.cvut.kbss.inbas.reporting.util.Constants;
import cz.cvut.kbss.inbas.reporting.util.Vocabulary;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.util.*;
import java.util.logging.LogManager;

import static org.junit.Assert.*;

public class RepositoryInvestigationReportServiceTest extends BaseServiceTestRunner {

    @Autowired
    private OccurrenceDao occurrenceDao;
    @Autowired
    private PreliminaryReportDao preliminaryReportDao;
    @Autowired
    private InvestigationReportDao investigationDao;

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
        super.setUp();
        Environment.setCurrentUser(person);
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

    @Test
    public void createFromPreliminaryReportSetsFileNumber() {
        final PreliminaryReport report = Generator
                .generatePreliminaryReport(Generator.ReportType.WITHOUT_TYPE_ASSESSMENTS);
        preliminaryReportDao.persist(report);

        final InvestigationReport result = service.createFromPreliminaryReport(report);
        assertNotNull(result);
        assertEquals(report.getFileNumber(), result.getFileNumber());
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

    @Test
    public void newRevisionReusesOccurrenceInstance() {
        final InvestigationReport report = Generator.generateMinimalInvestigation();
        occurrenceDao.persist(report.getOccurrence());
        investigationDao.persist(report);

        final InvestigationReport ir = service.createNewRevision(report);
        assertNotNull(ir);
        assertEquals(report.getOccurrence().getUri(), ir.getOccurrence().getUri());
        assertNotNull(ir.getAuthor());
        assertNotNull(ir.getCreated());
        assertEquals(report.getRevision() + 1, ir.getRevision().intValue());
        assertNotNull(service.find(ir.getUri()));
    }

    @Test
    public void newRevisionOfNewRevisionCanBeCreated() {
        final InvestigationReport report = Generator.generateMinimalInvestigation();
        occurrenceDao.persist(report.getOccurrence());
        investigationDao.persist(report);

        final InvestigationReport revisionTwo = service.createNewRevision(report);
        assertNotNull(revisionTwo);
        assertEquals(report.getOccurrence().getUri(), revisionTwo.getOccurrence().getUri());
        assertNotNull(service.find(revisionTwo.getUri()));

        final InvestigationReport revisionThree = service.createNewRevision(revisionTwo);
        assertNotNull(revisionThree);
        assertEquals(report.getOccurrence().getUri(), revisionThree.getOccurrence().getUri());
        assertNotNull(service.find(revisionThree.getUri()));
    }

    @Test
    public void newRevisionIsIndependentOfPreviousRevision() throws Exception {
        final InvestigationReport report = Generator.generateInvestigationWithFactorHierarchy();
        occurrenceDao.persist(report.getOccurrence());
        investigationDao.persist(report);

        final InvestigationReport newRevision = service.createNewRevision(report);
        assertNotEquals(report.getUri(), newRevision.getUri());
        verifyFactorsIndependence(report.getRootFactor(), newRevision.getRootFactor());
    }

    private void verifyFactorsIndependence(Factor original, Factor newFactor) {
        assertNotEquals(original.getUri(), newFactor.getUri());
        if (original.getChildren() != null) {
            final SortedSet<Factor> sortedOriginalChildren = getSortedChildren(original.getChildren());
            final SortedSet<Factor> sortedRevisionChildren = getSortedChildren(newFactor.getChildren());
            final Iterator<Factor> itOriginal = sortedOriginalChildren.iterator();
            final Iterator<Factor> itNewFactor = sortedRevisionChildren.iterator();
            while (itOriginal.hasNext() && itNewFactor.hasNext()) {
                verifyFactorsIndependence(itOriginal.next(), itNewFactor.next());
            }
        }
    }

    private SortedSet<Factor> getSortedChildren(Set<Factor> children) {
        final SortedSet<Factor> sortedSet = new TreeSet<>(new FactorComparator());
        sortedSet.addAll(children);
        return sortedSet;
    }

    @Test
    public void createNewRevisionCorrectlyReproducesCauseMitigatesRelationships() throws Exception {
        final InvestigationReport report = Generator.generateInvestigationWithCausesAndMitigatingFactors();
        occurrenceDao.persist(report.getOccurrence());
        investigationDao.persist(report);

        final InvestigationReport newRevision = service.createNewRevision(report);
        final Map<URI, URI> factorMapping = mapOldFactorsToNewOnes(report.getRootFactor(), newRevision.getRootFactor());
        verifyFactorCausesAndMitigates(report.getRootFactor(), newRevision.getRootFactor(), factorMapping);
    }

    private Map<URI, URI> mapOldFactorsToNewOnes(Factor orig, Factor newRevision) {
        final Map<URI, URI> m = new HashMap<>();
        m.put(orig.getUri(), newRevision.getUri());
        m.put(newRevision.getUri(), orig.getUri());
        if (orig.getChildren() != null) {
            final SortedSet<Factor> sortedOriginalChildren = getSortedChildren(orig.getChildren());
            final SortedSet<Factor> sortedRevisionChildren = getSortedChildren(newRevision.getChildren());
            final Iterator<Factor> itOriginal = sortedOriginalChildren.iterator();
            final Iterator<Factor> itNewFactor = sortedRevisionChildren.iterator();
            while (itOriginal.hasNext() && itNewFactor.hasNext()) {
                m.putAll(mapOldFactorsToNewOnes(itOriginal.next(), itNewFactor.next()));
            }
        }
        return m;
    }

    private void verifyFactorCausesAndMitigates(Factor original, Factor newRevision, Map<URI, URI> factorMapping) {
        if (original.getCauses() != null) {
            verifySetContents(original.getCauses(), newRevision.getCauses(), factorMapping);
        }
        if (original.getMitigatingFactors() != null) {
            verifySetContents(original.getMitigatingFactors(), newRevision.getMitigatingFactors(), factorMapping);
        }
        if (original.getChildren() != null) {
            final SortedSet<Factor> sortedOriginalChildren = getSortedChildren(original.getChildren());
            final SortedSet<Factor> sortedRevisionChildren = getSortedChildren(newRevision.getChildren());
            final Iterator<Factor> itOriginal = sortedOriginalChildren.iterator();
            final Iterator<Factor> itNewFactor = sortedRevisionChildren.iterator();
            while (itOriginal.hasNext() && itNewFactor.hasNext()) {
                verifyFactorCausesAndMitigates(itOriginal.next(), itNewFactor.next(), factorMapping);
            }
        }
    }

    private void verifySetContents(Set<Factor> orig, Set<Factor> newRevision, Map<URI, URI> factorMapping) {
        assertEquals(orig.size(), newRevision.size());
        boolean found = false;
        for (Factor origRev : orig) {
            final URI newRevisionUri = factorMapping.get(origRev.getUri());
            for (Factor newRev : newRevision) {
                if (newRev.getUri().equals(newRevisionUri)) {
                    found = true;
                    break;
                }
            }
            assertTrue(found);
        }
    }

    private static class FactorComparator implements Comparator<Factor> {

        @Override
        public int compare(Factor o1, Factor o2) {
            return o1.getStartTime().compareTo(o2.getStartTime());
        }
    }

    private List<InvestigationReport> initRevisions(int count) {
        count = count < 0 ? Generator.randomInt(10) : count;
        assertTrue(count > 0);
        final List<InvestigationReport> revisions = new ArrayList<>(count);
        final Long fileNumber = System.currentTimeMillis();
        final InvestigationReport revisionOne = Generator.generateMinimalInvestigation();
        revisionOne.setFileNumber(fileNumber);
        occurrenceDao.persist(revisionOne.getOccurrence());
        revisions.add(revisionOne);
        for (int i = 0; i < count; i++) {
            final InvestigationReport revision = new InvestigationReport(revisionOne);
            revision.setAuthor(Environment.getCurrentUser());
            revision.setRevision(Constants.INITIAL_REVISION + i + 1);
            revision.setCreated(new Date());
            revision.setFileNumber(fileNumber);
            if (i % 2 == 0) {   // Set last edited only for every even index
                revision.setLastEdited(new Date(System.currentTimeMillis() + 100000));
            }
            revisions.add(revision);
        }
        investigationDao.persist(revisions);
        Collections.sort(revisions, (rOne, rTwo) -> rTwo.getRevision() - rOne.getRevision());
        return revisions;
    }

    @Test
    public void getInvestigationReportsReturnsLatestRevisionsOfInvestigations() {
        final PreliminaryReport pr = Generator.generatePreliminaryReport(Generator.ReportType.WITH_TYPE_ASSESSMENTS);
        pr.setRevision(Constants.INITIAL_REVISION + 1);
        occurrenceDao.persist(pr.getOccurrence());
        preliminaryReportDao.persist(pr);
        final InvestigationReport irOne = service.createFromPreliminaryReport(pr);
        final List<InvestigationReport> revisions = initRevisions(2);
        final InvestigationReport irTwo = revisions.get(0);

        final Collection<InvestigationReport> result = service.findAll();
        assertEquals(2, result.size());
        for (InvestigationReport report : result) {
            assertTrue(irOne.getUri().equals(report.getUri()) || irTwo.getUri().equals(report.getUri()));
        }

    }
}
