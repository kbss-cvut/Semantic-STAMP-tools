package cz.cvut.kbss.inbas.reporting.service.repository;

import cz.cvut.kbss.inbas.reporting.environment.generator.AuditReportGenerator;
import cz.cvut.kbss.inbas.reporting.environment.generator.Generator;
import cz.cvut.kbss.inbas.reporting.environment.generator.OccurrenceReportGenerator;
import cz.cvut.kbss.inbas.reporting.environment.generator.SafetyIssueReportGenerator;
import cz.cvut.kbss.inbas.reporting.environment.util.Environment;
import cz.cvut.kbss.inbas.reporting.exception.NotFoundException;
import cz.cvut.kbss.inbas.reporting.model.AbstractEntity;
import cz.cvut.kbss.inbas.reporting.model.Occurrence;
import cz.cvut.kbss.inbas.reporting.model.Person;
import cz.cvut.kbss.inbas.reporting.model.audit.AuditFinding;
import cz.cvut.kbss.inbas.reporting.model.safetyissue.SafetyIssueReport;
import cz.cvut.kbss.inbas.reporting.model.safetyissue.SafetyIssueRiskAssessment;
import cz.cvut.kbss.inbas.reporting.persistence.dao.OccurrenceDao;
import cz.cvut.kbss.inbas.reporting.persistence.dao.SafetyIssueReportDao;
import cz.cvut.kbss.inbas.reporting.service.BaseServiceTestRunner;
import cz.cvut.kbss.inbas.reporting.service.SafetyIssueReportService;
import cz.cvut.kbss.inbas.reporting.service.arms.ArmsService;
import cz.cvut.kbss.inbas.reporting.util.Constants;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RepositorySafetyIssueReportServiceTest extends BaseServiceTestRunner {

    @Autowired
    private SafetyIssueReportService service;

    @Autowired
    private SafetyIssueReportDao dao;

    @Autowired
    private OccurrenceDao occurrenceDao;

    @Autowired
    private ArmsService armsServiceMock;

    @Autowired
    private EntityManagerFactory emf;

    private Person author;

    @Before
    public void setUp() {
        this.author = persistPerson();
        Environment.setCurrentUser(author);
    }

    @Test
    public void persistInitializesReportProvenanceData() {
        final SafetyIssueReport report = SafetyIssueReportGenerator.generateSafetyIssueReport(false, false);
        service.persist(report);
        final SafetyIssueReport result = service.findByKey(report.getKey());
        assertNotNull(result);
        assertEquals(author, result.getAuthor());
        assertNotNull(result.getDateCreated());
        assertNotNull(result.getFileNumber());
        assertEquals(Constants.INITIAL_REVISION, result.getRevision());
    }

    @Test
    public void updateSetsLastModifiedAndLastModifiedBy() {
        final SafetyIssueReport report = SafetyIssueReportGenerator.generateSafetyIssueReport(false, false);
        service.persist(report);
        report.setSummary("Updated report summary.");
        assertNull(report.getLastModified());
        assertNull(report.getLastModifiedBy());
        service.update(report);
        final SafetyIssueReport result = service.findByKey(report.getKey());
        assertNotNull(result);
        assertEquals(author, result.getLastModifiedBy());
        assertNotNull(result.getLastModified());
    }

    @Test
    public void getLatestRevisionLoadsLatestRevisionOfSafetyIssueReport() {
        final List<SafetyIssueReport> chain = SafetyIssueReportGenerator.generateSafetyIssueReportChain(author);
        dao.persist(chain);
        final SafetyIssueReport latest = chain.get(chain.size() - 1);
        final SafetyIssueReport result = service.findLatestRevision(latest.getFileNumber());
        assertEquals(latest.getUri(), result.getUri());
        assertEquals(latest.getRevision(), result.getRevision());
    }

    @Test
    public void getLatestRevisionReturnsNullForUnknownFileNumber() {
        assertNull(service.findLatestRevision(System.currentTimeMillis()));
    }

    @Test
    public void removeReportChainDeletesAllReportsInChain() {
        final List<SafetyIssueReport> chain = SafetyIssueReportGenerator.generateSafetyIssueReportChain(author);
        dao.persist(chain);
        chain.forEach(r -> assertNotNull(service.find(r.getUri())));
        service.removeReportChain(chain.get(0).getFileNumber());
        chain.forEach(r -> {
            assertNull(service.find(r.getUri()));
            assertFalse(service.exists(r.getUri()));
        });
    }

    @Test
    public void createNewRevisionCreatesReportWithIdenticalFileNumber() {
        final List<SafetyIssueReport> chain = SafetyIssueReportGenerator.generateSafetyIssueReportChain(author);
        dao.persist(chain);
        final SafetyIssueReport latest = chain.get(chain.size() - 1);
        final SafetyIssueReport newRevision = service.createNewRevision(latest.getFileNumber());
        assertEquals(latest.getFileNumber(), newRevision.getFileNumber());
        assertEquals(latest.getRevision() + 1, newRevision.getRevision().intValue());
        assertNotEquals(latest.getKey(), newRevision.getKey());
        assertNotNull(service.findByKey(newRevision.getKey()));
    }

    @Test(expected = NotFoundException.class)
    public void createNewRevisionThrowsNotFoundExceptionForUnknownFileNumber() {
        service.createNewRevision(System.currentTimeMillis());
    }

    @Test
    public void findRevisionReturnsReportWithMatchingFileNumberAndRevision() {
        final List<SafetyIssueReport> chain = SafetyIssueReportGenerator.generateSafetyIssueReportChain(author);
        dao.persist(chain);
        final SafetyIssueReport report = chain.get(Generator.randomIndex(chain));
        final SafetyIssueReport result = service.findRevision(report.getFileNumber(), report.getRevision());
        assertNotNull(result);
        assertEquals(report.getUri(), result.getUri());
    }

    @Test
    public void updateRemovesBaseOccurrence() {
        final SafetyIssueReport report = persistSafetyIssueWithBases();

        final Set<Occurrence> removed = new HashSet<>();
        final Iterator<Occurrence> it = report.getSafetyIssue().getBasedOnOccurrences().iterator();
        while (it.hasNext()) {
            final Occurrence o = it.next();
            if (Generator.randomBoolean() && report.getSafetyIssue().getBasedOnOccurrences().size() > 1) {
                it.remove();
                removed.add(o);
            }
        }
        assertFalse(removed.isEmpty());
        service.update(report);

        final SafetyIssueReport result = service.find(report.getUri());
        assertEquals(report.getSafetyIssue().getBasedOnOccurrences().size(),
                result.getSafetyIssue().getBasedOnOccurrences().size());
        final Set<URI> uris = result.getSafetyIssue().getBasedOnOccurrences().stream().map(AbstractEntity::getUri)
                                    .collect(Collectors.toSet());
        removed.forEach(o -> {
            assertFalse(uris.contains(o.getUri()));
            assertNotNull(occurrenceDao.find(o.getUri()));
        });
    }

    private SafetyIssueReport persistSafetyIssueWithBases() {
        final SafetyIssueReport report = SafetyIssueReportGenerator.generateSafetyIssueReport(false, false);
        final Set<Occurrence> bases = new HashSet<>();
        for (int i = 0; i < Generator.randomInt(10, 15); i++) {
            final Occurrence base = OccurrenceReportGenerator.generateOccurrenceReport(true).getOccurrence();
            bases.add(base);
        }
        occurrenceDao.persist(bases);
        report.getSafetyIssue().setBasedOnOccurrences(bases);
        final Set<AuditFinding> findings = AuditReportGenerator.generateFindings();
        final EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            findings.forEach(em::persist);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        report.getSafetyIssue().setBasedOnFindings(findings);
        service.persist(report);
        return report;
    }

    @Test
    public void updateRemovesBaseFindings() {
        final SafetyIssueReport report = persistSafetyIssueWithBases();

        final Set<AuditFinding> removed = new HashSet<>();
        final Iterator<AuditFinding> it = report.getSafetyIssue().getBasedOnFindings().iterator();
        while (it.hasNext()) {
            final AuditFinding finding = it.next();
            if (Generator.randomBoolean() && report.getSafetyIssue().getBasedOnFindings().size() > 1) {
                it.remove();
                removed.add(finding);
            }
        }
        assertFalse(removed.isEmpty());
        service.update(report);

        final SafetyIssueReport result = service.find(report.getUri());
        assertEquals(report.getSafetyIssue().getBasedOnFindings().size(),
                result.getSafetyIssue().getBasedOnFindings().size());
        final Set<URI> uris = result.getSafetyIssue().getBasedOnFindings().stream().map(AbstractEntity::getUri)
                                    .collect(Collectors.toSet());
        final EntityManager em = emf.createEntityManager();
        try {
            removed.forEach(o -> {
                assertFalse(uris.contains(o.getUri()));
                assertNotNull(em.find(AuditFinding.class, o.getUri()));
            });
        } finally {
            em.close();
        }
    }

    @Test
    public void findSetsReportSiraValueBeforeReturningIt() {
        final SafetyIssueReport report = SafetyIssueReportGenerator.generateSafetyIssueReport(false, false);
        service.persist(report);
        final URI siraValue = Generator.generateUri();
        when(armsServiceMock.calculateSafetyIssueRiskAssessment(any(SafetyIssueRiskAssessment.class)))
                .thenReturn(siraValue);

        final SafetyIssueReport result = service.findByKey(report.getKey());
        assertEquals(siraValue, result.getSira().getSiraValue());
        verify(armsServiceMock).calculateSafetyIssueRiskAssessment(result.getSira());
    }

    @Test
    public void findLatestRevisionSetsSiraValueBeforeReturningReport() {
        final SafetyIssueReport report = SafetyIssueReportGenerator.generateSafetyIssueReport(false, false);
        service.persist(report);
        final URI siraValue = Generator.generateUri();
        when(armsServiceMock.calculateSafetyIssueRiskAssessment(any(SafetyIssueRiskAssessment.class)))
                .thenReturn(siraValue);

        final SafetyIssueReport result = service.findLatestRevision(report.getFileNumber());
        assertEquals(siraValue, result.getSira().getSiraValue());
        verify(armsServiceMock).calculateSafetyIssueRiskAssessment(result.getSira());
    }

    @Test
    public void createNewRevisionCopiesSiraValueToTheNewInstance() {
        final SafetyIssueReport report = SafetyIssueReportGenerator.generateSafetyIssueReport(false, false);
        service.persist(report);
        final URI siraValue = Generator.generateUri();
        when(armsServiceMock.calculateSafetyIssueRiskAssessment(any(SafetyIssueRiskAssessment.class)))
                .thenReturn(siraValue);

        final SafetyIssueReport newRevision = service.createNewRevision(report.getFileNumber());
        assertEquals(siraValue, newRevision.getSira().getSiraValue());
        verify(armsServiceMock).calculateSafetyIssueRiskAssessment(newRevision.getSira());
    }

    @Test
    public void findRevisionSetsSiraValue() {
        final SafetyIssueReport report = SafetyIssueReportGenerator.generateSafetyIssueReport(false, false);
        service.persist(report);
        final URI siraValue = Generator.generateUri();
        when(armsServiceMock.calculateSafetyIssueRiskAssessment(any(SafetyIssueRiskAssessment.class)))
                .thenReturn(siraValue);

        final SafetyIssueReport result = service.findRevision(report.getFileNumber(), report.getRevision());
        assertEquals(siraValue, result.getSira().getSiraValue());
        verify(armsServiceMock).calculateSafetyIssueRiskAssessment(result.getSira());
    }

    @Test
    public void findErasesAuthorCredentials() {
        final SafetyIssueReport report = SafetyIssueReportGenerator.generateSafetyIssueReport(false, false);
        report.setAuthor(author);
        service.persist(report);

        final SafetyIssueReport result = service.find(report.getUri());
        assertNull(result.getAuthor().getPassword());
    }

    @Test
    public void findErasesCredentialsOfLastModifier() {
        final Person lastModifier = new Person();
        lastModifier.setFirstName("Last");
        lastModifier.setLastName("Modifier");
        lastModifier.setUsername("last.modifier@fel.cvut.cz");
        lastModifier.setPassword("P@ssw0rd01");
        lastModifier.encodePassword(passwordEncoder);
        personDao.persist(lastModifier);
        final SafetyIssueReport report = SafetyIssueReportGenerator.generateSafetyIssueReport(false, false);
        report.setAuthor(author);
        report.setLastModifiedBy(lastModifier);
        service.persist(report);

        final SafetyIssueReport result = service.find(report.getUri());
        assertNull(result.getAuthor().getPassword());
        assertNull(result.getLastModifiedBy().getPassword());
    }
}
