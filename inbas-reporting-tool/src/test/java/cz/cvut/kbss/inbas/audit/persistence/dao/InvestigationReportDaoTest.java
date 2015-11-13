package cz.cvut.kbss.inbas.audit.persistence.dao;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.kbss.inbas.audit.model.reports.EventTypeAssessment;
import cz.cvut.kbss.inbas.audit.model.reports.Factor;
import cz.cvut.kbss.inbas.audit.model.reports.InvestigationReport;
import cz.cvut.kbss.inbas.audit.model.reports.incursions.LowVisibilityProcedure;
import cz.cvut.kbss.inbas.audit.model.reports.incursions.RunwayIncursion;
import cz.cvut.kbss.inbas.audit.persistence.BaseDaoTestRunner;
import cz.cvut.kbss.inbas.audit.service.options.FileOptionsLoader;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import static org.junit.Assert.*;

public class InvestigationReportDaoTest extends BaseDaoTestRunner {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private PersonDao personDao;
    @Autowired
    private OccurrenceDao occurrenceDao;
    @Autowired
    private InvestigationReportDao investigationDao;

    @Autowired
    private EntityManagerFactory emf;

    @Before
    public void setUp() throws Exception {
        objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
    }

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
        final String json = new FileOptionsLoader().load(fileName);
        return objectMapper
                .readValue(json, InvestigationReport.class);
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
}
