package cz.cvut.kbss.inbas.reporting.persistence.dao;

import cz.cvut.kbss.inbas.reporting.environment.generator.AuditReportGenerator;
import cz.cvut.kbss.inbas.reporting.environment.generator.Generator;
import cz.cvut.kbss.inbas.reporting.environment.util.Environment;
import cz.cvut.kbss.inbas.reporting.model.Person;
import cz.cvut.kbss.inbas.reporting.model.audit.Audit;
import cz.cvut.kbss.inbas.reporting.model.audit.AuditReport;
import cz.cvut.kbss.inbas.reporting.persistence.BaseDaoTestRunner;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class AuditReportDaoTest extends BaseDaoTestRunner {

    @Autowired
    private AuditReportDao dao;

    @Autowired
    private AuditDao auditDao;

    @Autowired
    private EntityManagerFactory emf;

    @Test
    public void persistPersistsAudit() {
        final AuditReport report = AuditReportGenerator.generateAuditReport(true);
        persistPerson(report.getAuthor());
        dao.persist(report);
        assertTrue(dao.exists(report.getUri()));
        assertNotNull(auditDao.find(report.getAudit().getUri()));
    }

    @Test
    public void updateUpdatesAudit() {
        final AuditReport report = AuditReportGenerator.generateAuditReport(true);
        persistPerson(report.getAuthor());
        dao.persist(report);
        final String newName = "New audit name";
        report.getAudit().setName(newName);
        dao.update(report);

        final EntityManager em = emf.createEntityManager();
        try {
            em.getEntityManagerFactory().getCache().evictAll();
            final Audit result = em.find(Audit.class, report.getAudit().getUri());
            assertEquals(newName, result.getName());
        } finally {
            em.close();
        }
    }

    @Test
    public void removeRemovesAudit() {
        final AuditReport report = AuditReportGenerator.generateAuditReport(true);
        persistPerson(report.getAuthor());
        dao.persist(report);

        dao.remove(report);

        final EntityManager em = emf.createEntityManager();
        try {
            em.getEntityManagerFactory().getCache().evictAll();
            assertNull(em.find(Audit.class, report.getAudit().getUri()));
        } finally {
            em.close();
        }
    }

    @Test
    public void findAllReturnsLatestRevisionsOfAllAuditReports() {
        final List<AuditReport> latestRevisions = generateReportChains();

        final List<AuditReport> result = dao.findAll();
        assertTrue(Environment.areEqual(latestRevisions, result));
    }

    private List<AuditReport> generateReportChains() {
        final List<AuditReport> result = new ArrayList<>();
        final Person author = Generator.getPerson();
        persistPerson(author);
        for (int i = 0; i < Generator.randomInt(5, 10); i++) {
            final List<AuditReport> chain = AuditReportGenerator.generateAuditReportChain(author);
            chain.forEach(r -> r.setAuthor(author));
            dao.persist(chain);
            result.add(chain.get(chain.size() - 1));    // Last one is the latest revision
        }
        return result;
    }
}
