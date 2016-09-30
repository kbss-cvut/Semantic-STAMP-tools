package cz.cvut.kbss.inbas.reporting.service.repository;

import cz.cvut.kbss.inbas.reporting.environment.generator.AuditReportGenerator;
import cz.cvut.kbss.inbas.reporting.environment.generator.Generator;
import cz.cvut.kbss.inbas.reporting.environment.util.Environment;
import cz.cvut.kbss.inbas.reporting.exception.NotFoundException;
import cz.cvut.kbss.inbas.reporting.exception.ValidationException;
import cz.cvut.kbss.inbas.reporting.model.CorrectiveMeasureRequest;
import cz.cvut.kbss.inbas.reporting.model.Organization;
import cz.cvut.kbss.inbas.reporting.model.Person;
import cz.cvut.kbss.inbas.reporting.model.Vocabulary;
import cz.cvut.kbss.inbas.reporting.model.audit.AuditFinding;
import cz.cvut.kbss.inbas.reporting.model.audit.AuditReport;
import cz.cvut.kbss.inbas.reporting.persistence.dao.AuditReportDao;
import cz.cvut.kbss.inbas.reporting.service.AuditReportService;
import cz.cvut.kbss.inbas.reporting.service.BaseServiceTestRunner;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class RepositoryAuditReportServiceTest extends BaseServiceTestRunner {

    @Autowired
    private AuditReportService service;

    @Autowired
    private AuditReportDao reportDao;

    @Autowired
    private EntityManagerFactory emf;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Person author;

    @Before
    public void setUp() {
        this.author = persistPerson();
        Environment.setCurrentUser(author);
    }

    @Test
    public void persistSetsReportProvenanceAndIdentificationData() {
        final AuditReport report = AuditReportGenerator.generateAuditReport(false);
        service.persist(report);

        final AuditReport result = reportDao.find(report.getUri());
        assertEquals(author, result.getAuthor());
        assertNotNull(result.getDateCreated());
        assertNotNull(result.getFileNumber());
        assertNotNull(result.getKey());
    }

    @Test
    public void persistSetsResponsibleOrganizationOfAllCorrectiveMeasuresToAuditee() {
        final AuditReport report = initAuditReportWithCorrectiveMeasures();
        service.persist(report);

        final AuditReport result = service.findByKey(report.getKey());
        assertNotNull(result.getAudit().getAuditee());
        for (AuditFinding f : result.getAudit().getFindings()) {
            f.getCorrectiveMeasures().forEach(m -> {
                assertEquals(1, m.getResponsibleOrganizations().size());
                assertTrue(m.getResponsibleOrganizations().contains(result.getAudit().getAuditee()));
            });
        }
    }

    private AuditReport initAuditReportWithCorrectiveMeasures() {
        final AuditReport report = AuditReportGenerator.generateAuditReport(false);
        report.getAudit().setFindings(AuditReportGenerator.generateFindings());
        for (AuditFinding f : report.getAudit().getFindings()) {
            final Set<CorrectiveMeasureRequest> measures = Generator.generateCorrectiveMeasureRequests();
            measures.forEach(m -> {
                m.setResponsibleOrganizations(null);
                m.setResponsiblePersons(null);
                m.setBasedOnEvent(null);
                m.setBasedOnOccurrence(null);
            });
            f.setCorrectiveMeasures(measures);
        }
        return report;
    }

    @Test
    public void persistingInvalidReportThrowsValidationException() {
        thrown.expect(ValidationException.class);
        final AuditReport report = AuditReportGenerator.generateAuditReport(false);
        // Audit end will be before its beginning
        report.getAudit().setEndDate(new Date(System.currentTimeMillis() - 10000));
        report.getAudit().setStartDate(new Date());
        service.persist(report);
    }

    @Test
    public void updatingInvalidReportThrowsValidationException() {
        final AuditReport report = AuditReportGenerator.generateAuditReport(false);
        service.persist(report);
        thrown.expect(ValidationException.class);
        report.getAudit().setAuditee(null);
        service.update(report);
    }

    @Test
    public void updateSetsLastModifierAndLastModifiedDate() {
        final AuditReport report = AuditReportGenerator.generateAuditReport(false);
        service.persist(report);
        report.setSummary("Updated summary.");
        service.update(report);

        final AuditReport result = service.find(report.getUri());
        assertEquals(author, report.getLastModifiedBy());
        assertNotNull(result.getLastModified());
    }

    @Test
    public void updateThrowsNotFoundWhenNonExistingReportIsPassedAsArgument() {
        thrown.expect(NotFoundException.class);
        final AuditReport report = AuditReportGenerator.generateAuditReport(true);
        report.setUri(Generator.generateUri());
        service.update(report);
    }

    @Test
    public void updateSetsResponsibleOrganizationToAuditeeOnAddedCorrectiveMeasures() {
        final AuditReport report = initAuditReportWithCorrectiveMeasures();
        service.persist(report);
        final CorrectiveMeasureRequest newOne = new CorrectiveMeasureRequest();
        newOne.setDescription("New one.");
        report.getAudit().getFindings().iterator().next().getCorrectiveMeasures().add(newOne);
        service.update(report);

        final EntityManager em = emf.createEntityManager();
        try {
            final CorrectiveMeasureRequest result = em.find(CorrectiveMeasureRequest.class, newOne.getUri());
            assertNotNull(result);
            assertEquals(1, result.getResponsibleOrganizations().size());
            assertTrue(result.getResponsibleOrganizations().contains(report.getAudit().getAuditee()));
        } finally {
            em.close();
        }
    }

    @Test
    public void updateReplacesResponsibleOrganizationOfCorrectiveMeasuresWhenAuditeeChanges() {
        final AuditReport report = initAuditReportWithCorrectiveMeasures();
        service.persist(report);
        final Organization newAuditee = Generator.generateOrganization();
        report.getAudit().setAuditee(newAuditee);
        service.update(report);

        final EntityManager em = emf.createEntityManager();
        try {
            final List<CorrectiveMeasureRequest> result = em
                    .createNativeQuery("SELECT ?x WHERE { ?x a ?type . }", CorrectiveMeasureRequest.class)
                    .setParameter("type",
                            URI.create(Vocabulary.s_c_corrective_measure_request)).getResultList();
            assertFalse(result.isEmpty());
            for (CorrectiveMeasureRequest m : result) {
                assertEquals(1, m.getResponsibleOrganizations().size());
                assertTrue(m.getResponsibleOrganizations().contains(newAuditee));
            }
        } finally {
            em.close();
        }
    }

    @Test
    public void findLatestRevisionLoadsLatestRevisionOfAReportChain() {
        final List<AuditReport> chain = AuditReportGenerator.generateAuditReportChain(author);
        reportDao.persist(chain);

        final AuditReport latest = chain.get(chain.size() - 1);
        final AuditReport result = service.findLatestRevision(latest.getFileNumber());
        assertNotNull(result);
        assertEquals(latest.getUri(), result.getUri());
    }

    @Test
    public void deleteReportChainRemovesChainWithSpecifiedFileNumber() {
        final List<AuditReport> chain = AuditReportGenerator.generateAuditReportChain(author);
        reportDao.persist(chain);

        service.removeReportChain(chain.get(0).getFileNumber());
        chain.forEach(r -> assertNull(reportDao.find(r.getUri())));
    }

    @Test
    public void createNewRevisionsPersistNewRevisionOfAuditReport() {
        final List<AuditReport> chain = AuditReportGenerator.generateAuditReportChain(author);
        reportDao.persist(chain);

        final AuditReport latest = chain.get(chain.size() - 1);
        final AuditReport newRevision = service.createNewRevision(latest.getFileNumber());
        assertNotNull(newRevision);
        assertEquals(latest.getFileNumber(), newRevision.getFileNumber());
        assertEquals(latest.getRevision() + 1, newRevision.getRevision().intValue());
        assertNotNull(service.findByKey(newRevision.getKey()));
    }

    @Test
    public void createNewRevisionThrowsNotFoundForUnknownFileNumber() {
        final List<AuditReport> chain = AuditReportGenerator.generateAuditReportChain(author);
        reportDao.persist(chain);
        final Long unknownFileNumber = System.currentTimeMillis();
        thrown.expect(NotFoundException.class);
        thrown.expectMessage("Audit report chain identified by " + unknownFileNumber + " not found.");
        service.createNewRevision(unknownFileNumber);
    }

    @Test
    public void findRevisionReturnsMatchingReportRevision() {
        final List<AuditReport> chain = AuditReportGenerator.generateAuditReportChain(author);
        reportDao.persist(chain);

        final AuditReport report = chain.get(Generator.randomIndex(chain));
        final AuditReport result = service.findRevision(report.getFileNumber(), report.getRevision());
        assertNotNull(result);
        assertEquals(report.getUri(), result.getUri());
    }

    @Test
    public void transitionToNextPhaseDoesNothing() {
        final List<AuditReport> chain = AuditReportGenerator.generateAuditReportChain(author);
        reportDao.persist(chain);

        final AuditReport latest = chain.get(chain.size() - 1);
        service.transitionToNextPhase(latest);
        final AuditReport result = service.find(latest.getUri());
        assertEquals(latest.getRevision(), result.getRevision());
        assertEquals(latest.getLastModified(), result.getLastModified());
        assertEquals(latest.getLastModifiedBy(), result.getLastModifiedBy());
    }
}
