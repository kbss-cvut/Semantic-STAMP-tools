package cz.cvut.kbss.inbas.audit.persistence.dao;

import cz.cvut.kbss.inbas.audit.environment.util.Generator;
import cz.cvut.kbss.inbas.audit.model.reports.PreliminaryReport;
import cz.cvut.kbss.inbas.audit.persistence.BaseDaoTestRunner;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author ledvima1
 */
public class BaseDaoTest extends BaseDaoTestRunner {

    @Autowired
    private PreliminaryReportDao dao;  // We're using one of the DAO implementations for the basic tests

    @Test
    public void existsForExistingInstanceReturnsTrue() throws Exception {
        final PreliminaryReport report = new PreliminaryReport();
        report.setOccurrence(Generator.generateOccurrence());
        dao.persist(report);
        assertNotNull(report.getUri());
        assertTrue(dao.exists(report.getUri()));
    }
}
