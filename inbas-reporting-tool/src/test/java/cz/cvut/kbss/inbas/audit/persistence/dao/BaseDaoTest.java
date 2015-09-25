package cz.cvut.kbss.inbas.audit.persistence.dao;

import cz.cvut.kbss.inbas.audit.model.reports.OccurrenceReport;
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
    private OccurrenceReportDao dao;  // We're using one of the DAO implementations for the basic tests

    @Test
    public void existsForExistingInstanceReturnsTrue() throws Exception {
        final OccurrenceReport report = new OccurrenceReport();
        dao.persist(report);
        assertNotNull(report.getUri());
        assertTrue(dao.exists(report.getUri()));
    }
}
