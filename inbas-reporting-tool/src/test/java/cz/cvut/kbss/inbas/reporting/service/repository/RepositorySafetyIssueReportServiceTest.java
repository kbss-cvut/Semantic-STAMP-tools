package cz.cvut.kbss.inbas.reporting.service.repository;

import cz.cvut.kbss.inbas.reporting.environment.generator.Generator;
import cz.cvut.kbss.inbas.reporting.environment.generator.SafetyIssueReportGenerator;
import cz.cvut.kbss.inbas.reporting.environment.util.Environment;
import cz.cvut.kbss.inbas.reporting.exception.NotFoundException;
import cz.cvut.kbss.inbas.reporting.model.Person;
import cz.cvut.kbss.inbas.reporting.model.safetyissue.SafetyIssueReport;
import cz.cvut.kbss.inbas.reporting.persistence.dao.SafetyIssueReportDao;
import cz.cvut.kbss.inbas.reporting.service.BaseServiceTestRunner;
import cz.cvut.kbss.inbas.reporting.service.SafetyIssueReportService;
import cz.cvut.kbss.inbas.reporting.util.Constants;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class RepositorySafetyIssueReportServiceTest extends BaseServiceTestRunner {

    @Autowired
    private SafetyIssueReportService service;

    @Autowired
    private SafetyIssueReportDao dao;

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
}
