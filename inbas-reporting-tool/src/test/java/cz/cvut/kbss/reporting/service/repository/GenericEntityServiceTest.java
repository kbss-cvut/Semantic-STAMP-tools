package cz.cvut.kbss.reporting.service.repository;

import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import cz.cvut.kbss.reporting.environment.generator.AuditReportGenerator;
import cz.cvut.kbss.reporting.environment.generator.Generator;
import cz.cvut.kbss.reporting.model.Person;
import cz.cvut.kbss.reporting.model.audit.AuditFinding;
import cz.cvut.kbss.reporting.service.BaseServiceTestRunner;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GenericEntityServiceTest extends BaseServiceTestRunner {

    @Autowired
    private GenericEntityService service;

    @Autowired
    private EntityManagerFactory emf;

    @Test
    public void findReturnsMatchingResultInstances() {
        final Person p = Generator.getPerson();
        p.generateUri();
        final AuditFinding finding = AuditReportGenerator.generateFinding();
        final EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(p);
            em.persist(finding);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        final Person pResult = service.find(Person.class, p.getUri());
        assertNotNull(pResult);
        assertEquals(p.getUri(), pResult.getUri());
        final AuditFinding fResult = service.find(AuditFinding.class, finding.getUri());
        assertNotNull(fResult);
        assertEquals(finding.getUri(), fResult.getUri());
    }
}
