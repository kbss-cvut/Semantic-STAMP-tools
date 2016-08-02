package cz.cvut.kbss.inbas.reporting.service.repository;

import cz.cvut.kbss.inbas.reporting.environment.generator.SafetyIssueReportGenerator;
import cz.cvut.kbss.inbas.reporting.environment.util.Environment;
import cz.cvut.kbss.inbas.reporting.model.Person;
import cz.cvut.kbss.inbas.reporting.model.safetyissue.SafetyIssueReport;
import cz.cvut.kbss.inbas.reporting.service.BaseServiceTestRunner;
import cz.cvut.kbss.inbas.reporting.service.SafetyIssueReportService;
import cz.cvut.kbss.inbas.reporting.util.Constants;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class RepositorySafetyIssueReportServiceTest extends BaseServiceTestRunner {

    @Autowired
    private SafetyIssueReportService service;

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
}
