package cz.cvut.kbss.inbas.audit.persistence.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.kbss.inbas.audit.environment.util.Environment;
import cz.cvut.kbss.inbas.audit.environment.util.Generator;
import cz.cvut.kbss.inbas.audit.model.reports.EventTypeAssessment;
import cz.cvut.kbss.inbas.audit.model.reports.Factor;
import cz.cvut.kbss.inbas.audit.model.reports.InvestigationReport;
import cz.cvut.kbss.inbas.audit.model.reports.incursions.LowVisibilityProcedure;
import cz.cvut.kbss.inbas.audit.model.reports.incursions.RunwayIncursion;
import cz.cvut.kbss.inbas.audit.persistence.BaseDaoTestRunner;
import cz.cvut.kbss.inbas.audit.util.FileDataLoader;
import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static org.junit.Assert.*;

public class InvestigationReportDaoTest extends BaseDaoTestRunner {

    private final ObjectMapper objectMapper = Environment.getObjectMapper();

    @Autowired
    private PersonDao personDao;
    @Autowired
    private OccurrenceDao occurrenceDao;
    @Autowired
    private InvestigationReportDao investigationDao;

    @Autowired
    private EntityManagerFactory emf;

    @Test
    public void persistPersistsFactorHierarchyInReport() throws Exception {
        final InvestigationReport report = loadReport("test_data/reportWithFactorHierarchy.json");

        persistReport(report);
        verifyPersistedReport(report);
    }

    private void verifyPersistedReport(InvestigationReport report) {

        assertNotNull(report.getRootFactor().getUri());
        assertNotNull(investigationDao.findByKey(report.getKey()));
        final EntityManager em = emf.createEntityManager();
        try {
            final Factor root = em.find(Factor.class, report.getRootFactor().getUri());
            assertNotNull(root);
            assertFalse(root.getChildren().isEmpty());
            assertEquals(report.getRootFactor().getChildren().size(), root.getChildren().size());
            verifyFactorHierarchy(report.getRootFactor(), root);
        } finally {
            em.close();
        }
    }

    private void persistReport(InvestigationReport report) {
        occurrenceDao.persist(report.getOccurrence());
        personDao.persist(report.getAuthor());
        investigationDao.persist(report);
    }

    private InvestigationReport loadReport(String fileName) throws Exception {
        final String json = new FileDataLoader().load(fileName);
        final InvestigationReport report = objectMapper
                .readValue(json, InvestigationReport.class);
        report.setFileNumber(System.currentTimeMillis());
        return report;
    }

    private void verifyFactorHierarchy(Factor expectedRoot, Factor actualRoot) {
        assertEquals(expectedRoot.getUri(), actualRoot.getUri());
        if (expectedRoot.getAssessment() != null) {
            assertEquals(expectedRoot.getAssessment().getUri(), actualRoot.getAssessment().getUri());
            if (expectedRoot.getAssessment().getRunwayIncursion() == null) {
                assertEquals(expectedRoot.getAssessment().getDescription(),
                        actualRoot.getAssessment().getDescription());
            } else {
                assertEquals(expectedRoot.getAssessment().getRunwayIncursion().getUri(),
                        actualRoot.getAssessment().getRunwayIncursion().getUri());
            }
        }
        if (expectedRoot.getCauses() != null) {
            verifyLinks(expectedRoot.getCauses(), actualRoot.getCauses());
        }
        if (expectedRoot.getMitigatingFactors() != null) {
            verifyLinks(expectedRoot.getMitigatingFactors(), actualRoot.getMitigatingFactors());
        }
        if (expectedRoot.getChildren() != null) {
            assertEquals(expectedRoot.getChildren().size(), actualRoot.getChildren().size());
            for (Factor expChild : expectedRoot.getChildren()) {
                boolean found = false;
                for (Factor actChild : actualRoot.getChildren()) {
                    if (expChild.getUri().equals(actChild.getUri())) {
                        verifyFactorHierarchy(expChild, actChild);
                        found = true;
                        break;
                    }
                }
                assertTrue(found);
            }
        }
    }

    private void verifyLinks(Set<Factor> expectedLinks, Set<Factor> actualLinks) {
        assertEquals(expectedLinks.size(), actualLinks.size());
        for (Factor cause : expectedLinks) {
            Factor actualCause = actualLinks.stream().filter(ac -> cause.getUri().equals(ac.getUri())).findFirst()
                                            .get();
            assertNotNull(actualCause);
        }
    }

    @Test
    public void persistReportWithFactorsWithCausesPreservesCausalityRelations() throws Exception {
        final InvestigationReport report = loadReport("test_data/reportWithFactorsWithCauses.json");

        persistReport(report);
        verifyPersistedReport(report);
    }

    @Test
    public void persistReportWithFactorsWithMitigatingFactorsPreservesMitigationRelations() throws Exception {
        final InvestigationReport report = loadReport("test_data/reportWithFactorsWithMitigates.json");

        persistReport(report);
        verifyPersistedReport(report);
    }

    @Test
    public void testPersistReportWithFactorWithBothMitigatesAndCauses() throws Exception {
        final InvestigationReport report = loadReport("test_data/reportWithFactorsAndCausesAndMitigates.json");

        persistReport(report);
        verifyPersistedReport(report);
    }

    @Test
    public void reportUpdateCorrectlyCascadesChangesToFactors() throws Exception {
        final InvestigationReport report = loadReport("test_data/reportWithFactorHierarchy.json");
        persistReport(report);

        final InvestigationReport update = investigationDao.find(report.getUri());
        final Factor added = new Factor();
        added.setStartTime(new Date());
        added.setEndTime(new Date());
        final EventTypeAssessment assessment = new EventTypeAssessment();
        assessment.setRunwayIncursion(new RunwayIncursion());
        assessment.getRunwayIncursion().setLowVisibilityProcedure(LowVisibilityProcedure.CAT_III);
        added.setCauses(Collections.singleton(update.getRootFactor().getChildren().iterator().next()));
        update.getRootFactor().getChildren().add(added);

        investigationDao.update(update);
        verifyPersistedReport(update);
    }

    @Test
    public void reportUpdateCascadesChangesToCauseAndMitigationLinks() throws Exception {
        final InvestigationReport report = loadReport("test_data/reportWithFactorHierarchy.json");
        persistReport(report);

        final InvestigationReport update = investigationDao.find(report.getUri());
        final List<Factor> children = new ArrayList<>(update.getRootFactor().getChildren());
        children.get(1).setCauses(Collections.singleton(children.get(0)));

        investigationDao.update(update);
        verifyPersistedReport(update);
    }

    @Test
    public void addsReportTypeToInvestigationsTypes() throws Exception {
        final InvestigationReport report = loadReport("test_data/reportWithFactorHierarchy.json");
        persistReport(report);
        assertTrue(report.getTypes().contains(Vocabulary.Report));

        final InvestigationReport result = investigationDao.find(report.getUri());
        assertTrue(result.getTypes().contains(Vocabulary.Report));
    }

    @Test
    public void ensuresReportIsPresentInInvestigationsTypesOnUpdate() throws Exception {
        final InvestigationReport report = loadReport("test_data/reportWithFactorHierarchy.json");
        persistReport(report);
        assertTrue(report.getTypes().contains(Vocabulary.Report));

        final InvestigationReport toUpdate = investigationDao.find(report.getUri());
        toUpdate.setTypes(new HashSet<>());
        investigationDao.update(toUpdate);

        final InvestigationReport result = investigationDao.find(report.getUri());
        assertTrue(result.getTypes().contains(Vocabulary.Report));
    }

    @Test
    public void findAllReturnsLatestRevisionsForEachReportChain() throws Exception {
        final InvestigationReport rOne = Generator.generateMinimalInvestigation();
        rOne.setFileNumber(System.currentTimeMillis());
        persistReport(rOne);
        final InvestigationReport rOneRevTwo = new InvestigationReport(rOne);
        rOneRevTwo.setRevision(rOne.getRevision() + 1);
        rOneRevTwo.setAuthor(Generator.getPerson());
        investigationDao.persist(rOneRevTwo);

        final InvestigationReport rTwo = Generator.generateMinimalInvestigation();
        rTwo.setFileNumber(System.currentTimeMillis() + 10000);
        rTwo.setOccurrence(rOne.getOccurrence());   // The same occurrence, different report chain
        rTwo.setAuthor(Generator.getPerson());
        investigationDao.persist(rTwo);
        final InvestigationReport rTwoRevTwo = new InvestigationReport(rTwo);
        rTwoRevTwo.setRevision(rTwo.getRevision() + 5);
        rTwoRevTwo.setAuthor(Generator.getPerson());
        investigationDao.persist(rTwoRevTwo);

        final List<InvestigationReport> result = investigationDao.findAll();
        assertEquals(2, result.size());
        assertTrue(result.get(0).getUri().equals(rOneRevTwo.getUri()) ||
                result.get(0).getUri().equals(rTwoRevTwo.getUri()));
        assertTrue(result.get(1).getUri().equals(rOneRevTwo.getUri()) ||
                result.get(1).getUri().equals(rTwoRevTwo.getUri()));
    }
}
