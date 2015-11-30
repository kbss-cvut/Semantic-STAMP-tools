package cz.cvut.kbss.inbas.audit.persistence.dao;

import cz.cvut.kbss.inbas.audit.model.reports.InitialReport;
import cz.cvut.kbss.inbas.audit.persistence.BaseDaoTestRunner;
import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;

import static org.junit.Assert.assertTrue;

public class InitialReportDaoTest extends BaseDaoTestRunner {

    @Autowired
    private InitialReportDao dao;

    @Test
    public void addsReportTypeToInitialReportsTypesOnPersist() throws Exception {
        final InitialReport ir = new InitialReport("Very short initial report.");
        dao.persist(ir);
        assertTrue(ir.getTypes().contains(Vocabulary.Report));

        final InitialReport result = dao.find(ir.getUri());
        assertTrue(result.getTypes().contains(Vocabulary.Report));
    }

    @Test
    public void ensuresReportTypeIsPresentInInitialReportTypesOnUpdate() throws Exception {
        final InitialReport ir = new InitialReport("Very short initial report.");
        dao.persist(ir);
        assertTrue(ir.getTypes().contains(Vocabulary.Report));

        final InitialReport toUpdate = dao.find(ir.getUri());
        toUpdate.setTypes(new HashSet<>());
        dao.update(toUpdate);

        final InitialReport result = dao.find(ir.getUri());
        assertTrue(result.getTypes().contains(Vocabulary.Report));
    }
}
