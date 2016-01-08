package cz.cvut.kbss.inbas.audit.persistence.dao;

import cz.cvut.kbss.inbas.audit.environment.util.Generator;
import cz.cvut.kbss.inbas.audit.model.reports.OccurrenceSeverity;
import cz.cvut.kbss.inbas.audit.model.reports.PreliminaryReport;
import cz.cvut.kbss.inbas.audit.persistence.BaseDaoTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author ledvima1
 */
public class BaseDaoTest extends BaseDaoTestRunner {

    @Autowired
    private PersonDao personDao;

    @Autowired
    private PreliminaryReportDao dao;  // We're using one of the DAO implementations for the basic tests

    @Before
    public void setUp() throws Exception {
        personDao.persist(Generator.getPerson());
    }

    @Test
    public void existsForExistingInstanceReturnsTrue() throws Exception {
        final PreliminaryReport report =
                Generator.generatePreliminaryReport(Generator.ReportType.WITHOUT_TYPE_ASSESSMENTS);
        dao.persist(report);
        assertNotNull(report.getUri());
        assertTrue(dao.exists(report.getUri()));
    }

    @Test
    public void findAllReturnsAllExistingInstances() {
        final List<PreliminaryReport> reports = new ArrayList<>();
        final PreliminaryReport r1 = Generator.generatePreliminaryReport(Generator.ReportType.WITHOUT_TYPE_ASSESSMENTS);
        reports.add(r1);
        final PreliminaryReport r2 = Generator.generatePreliminaryReport(Generator.ReportType.WITHOUT_TYPE_ASSESSMENTS);
        r2.setSeverityAssessment(OccurrenceSeverity.INCIDENT);
        reports.add(r2);
        dao.persist(reports);

        final List<PreliminaryReport> result = dao.findAll();
        assertEquals(reports.size(), result.size());
        for (PreliminaryReport r : reports) {
            final PreliminaryReport matching = result.stream().filter(pr -> r.getUri().equals(pr.getUri())).findFirst()
                                                     .get();
            assertNotNull(matching);
        }
    }
}
