package cz.cvut.kbss.reporting.persistence.dao;

import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import cz.cvut.kbss.reporting.environment.generator.Generator;
import cz.cvut.kbss.reporting.environment.generator.SafetyIssueReportGenerator;
import cz.cvut.kbss.reporting.environment.util.Environment;
import cz.cvut.kbss.reporting.model.CorrectiveMeasureRequest;
import cz.cvut.kbss.reporting.model.Person;
import cz.cvut.kbss.reporting.model.safetyissue.SafetyIssueReport;
import cz.cvut.kbss.reporting.persistence.BaseDaoTestRunner;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static org.junit.Assert.*;

public class SafetyIssueReportDaoTest extends BaseDaoTestRunner {

    @Autowired
    private SafetyIssueReportDao reportDao;

    @Autowired
    private SafetyIssueDao safetyIssueDao;

    @Autowired
    private EntityManagerFactory emf;

    @Test
    public void persistPersistsSafetyIssueAsWell() {
        final SafetyIssueReport report = SafetyIssueReportGenerator.generateSafetyIssueReport(true, false);
        persistPerson(report.getAuthor());
        reportDao.persist(report);
        assertNotNull(report.getUri());
        assertNotNull(report.getKey());
        assertNotNull(reportDao.find(report.getUri()));
        assertNotNull(safetyIssueDao.find(report.getSafetyIssue().getUri()));
    }

    @Test
    public void updateRemovesOrphanCorrectiveMeasures() {
        final SafetyIssueReport report = persistReportWithMeasures();
        final Set<CorrectiveMeasureRequest> toRemove = new HashSet<>();
        final Iterator<CorrectiveMeasureRequest> it = report.getCorrectiveMeasures().iterator();
        while (it.hasNext()) {
            final CorrectiveMeasureRequest m = it.next();
            // Just make sure that there is at least one to remove
            if (Generator.randomBoolean() || toRemove.isEmpty()) {
                it.remove();
                toRemove.add(m);
            }
        }
        reportDao.update(report);

        final SafetyIssueReport result = reportDao.findByKey(report.getKey());
        assertEquals(report.getCorrectiveMeasures().size(), result.getCorrectiveMeasures().size());
        final EntityManager em = emf.createEntityManager();
        try {
            for (CorrectiveMeasureRequest removed : toRemove) {
                assertNull(em.find(CorrectiveMeasureRequest.class, removed.getUri()));
            }
        } finally {
            em.close();
        }
    }

    private SafetyIssueReport persistReportWithMeasures() {
        final SafetyIssueReport report = SafetyIssueReportGenerator.generateSafetyIssueReport(true, true);
        persistPerson(report.getAuthor());
        report.getCorrectiveMeasures().stream()
              .filter(m -> m.getResponsibleOrganizations() != null && !m.getResponsibleOrganizations().isEmpty())
              .forEach(m -> m.getResponsibleOrganizations().forEach(this::persistOrganization));
        reportDao.persist(report);
        return report;
    }

    @Test
    public void findAllReturnsAllLatestRevisionsOfSafetyIssues() {
        final Person author = Generator.getPerson();
        persistPerson(author);
        final List<SafetyIssueReport> reports = generateReports(author);

        final List<SafetyIssueReport> result = reportDao.findAll();
        assertTrue(Environment.areEqual(reports, result));
    }

    private List<SafetyIssueReport> generateReports(Person author) {
        final List<SafetyIssueReport> reports = new ArrayList<>();
        for (int i = 0; i < Generator.randomInt(2, 10); i++) {
            final SafetyIssueReport r = SafetyIssueReportGenerator.generateSafetyIssueReport(true, false);
            r.setAuthor(author);
            reports.add(r);
        }
        reportDao.persist(reports);
        return reports;
    }
}
