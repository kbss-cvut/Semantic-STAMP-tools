package cz.cvut.kbss.reporting.persistence.dao;

import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import cz.cvut.kbss.reporting.environment.generator.AuditReportGenerator;
import cz.cvut.kbss.reporting.environment.generator.Generator;
import cz.cvut.kbss.reporting.environment.util.TestUtils;
import cz.cvut.kbss.reporting.model.CorrectiveMeasureRequest;
import cz.cvut.kbss.reporting.model.Organization;
import cz.cvut.kbss.reporting.model.audit.Audit;
import cz.cvut.kbss.reporting.model.audit.AuditFinding;
import cz.cvut.kbss.reporting.model.qam.Answer;
import cz.cvut.kbss.reporting.model.qam.Question;
import cz.cvut.kbss.reporting.persistence.BaseDaoTestRunner;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static org.junit.Assert.*;

public class AuditDaoTest extends BaseDaoTestRunner {

    @Autowired
    private AuditDao dao;

    @Autowired
    private OrganizationDao organizationDao;

    @Autowired
    private EntityManagerFactory emf;

    @Test
    public void persistPersistsAuditeeWhenItDoesNotExistAlready() {
        final Audit audit = AuditReportGenerator.generateAudit();
        dao.persist(audit);
        assertNotNull(dao.find(audit.getUri()));
        verifyOrganizationExists(audit.getAuditee());
    }

    private void verifyOrganizationExists(Organization organization) {
        assertNotNull(organizationDao.find(organization.getUri()));
    }

    @Test
    public void persistPersistsAuditorWhenItDoesNotExist() {
        final Audit audit = AuditReportGenerator.generateAudit();
        audit.setAuditor(Generator.generateOrganization());
        dao.persist(audit);
        assertNotNull(dao.find(audit.getUri()));
        verifyOrganizationExists(audit.getAuditor());
    }

    @Test
    public void persistPersistsQuestionTree() {
        final Audit audit = AuditReportGenerator.generateAudit();
        audit.setQuestion(Generator.generateQuestions(2));
        dao.persist(audit);
        assertNotNull(audit.getQuestion().getUri());
        final EntityManager em = emf.createEntityManager();
        try {
            TestUtils.verifyQuestions(audit.getQuestion(), q -> assertNotNull(em.find(Question.class, q.getUri())));
        } finally {
            em.close();
        }
    }

    @Test
    public void persistPersistsCorrectiveMeasuresOfFindings() {
        final Audit audit = prepareAuditWithCorrectiveMeasures();
        dao.persist(audit);

        final EntityManager em = emf.createEntityManager();
        try {
            for (AuditFinding f : audit.getFindings()) {
                assertNotNull(em.find(AuditFinding.class, f.getUri()));
                if (f.getCorrectiveMeasures() != null) {
                    for (CorrectiveMeasureRequest cm : f.getCorrectiveMeasures()) {
                        assertNotNull(em.find(CorrectiveMeasureRequest.class, cm.getUri()));
                    }
                }
            }
        } finally {
            em.close();
        }
    }

    private Audit prepareAuditWithCorrectiveMeasures() {
        final Audit audit = AuditReportGenerator.generateAudit();
        audit.setFindings(AuditReportGenerator.generateFindings());
        audit.getFindings().forEach(f -> {
            f.setCorrectiveMeasures(Generator.generateCorrectiveMeasureRequests());
            f.getCorrectiveMeasures()
             .forEach(cm -> {
                 cm.setResponsibleOrganizations(Collections.singleton(audit.getAuditee()));
                 cm.setResponsiblePersons(null);
             });
        });
        return audit;
    }

    @Test
    public void persistHandlesCorrectiveMeasuresReferencedByMultipleFindings() {
        final Audit audit = AuditReportGenerator.generateAudit();
        audit.setFindings(AuditReportGenerator.generateFindings());
        final CorrectiveMeasureRequest cm = new CorrectiveMeasureRequest();
        cm.setDescription("Corrective measure");
        cm.setResponsibleOrganizations(Collections.singleton(audit.getAuditee()));
        audit.getFindings().forEach(f -> f.setCorrectiveMeasures(Collections.singleton(cm)));
        dao.persist(audit);

        final EntityManager em = emf.createEntityManager();
        try {
            assertNotNull(em.find(CorrectiveMeasureRequest.class, cm.getUri()));
        } finally {
            em.close();
        }
    }

    @Test
    public void updatePersistsAddedCorrectiveMeasures() {
        final Audit audit = prepareAuditWithCorrectiveMeasures();
        dao.persist(audit);
        final Set<CorrectiveMeasureRequest> toAdd = Generator.generateCorrectiveMeasureRequests();
        final List<AuditFinding> findingList = new ArrayList<>(audit.getFindings());
        for (CorrectiveMeasureRequest cm : toAdd) {
            cm.setResponsiblePersons(null);
            cm.setResponsibleOrganizations(Collections.singleton(audit.getAuditee()));
            findingList.get(Generator.randomIndex(findingList)).getCorrectiveMeasures().add(cm);
        }
        dao.update(audit);
        // No need for explicit persist, cascade.MERGE will merge the instances automatically

        final EntityManager em = emf.createEntityManager();
        try {
            for (CorrectiveMeasureRequest added : toAdd) {
                assertNotNull(em.find(CorrectiveMeasureRequest.class, added.getUri()));
            }
        } finally {
            em.close();
        }
    }

    @Test
    public void updateMergesUpdatedCorrectiveMeasures() {
        final Audit audit = prepareAuditWithCorrectiveMeasures();
        dao.persist(audit);
        for (AuditFinding f : audit.getFindings()) {
            for (CorrectiveMeasureRequest cm : f.getCorrectiveMeasures()) {
                cm.setDescription(cm.getDescription() + " Updated!");
            }
        }
        dao.update(audit);

        final EntityManager em = emf.createEntityManager();
        try {
            for (AuditFinding f : audit.getFindings()) {
                for (CorrectiveMeasureRequest cm : f.getCorrectiveMeasures()) {
                    final CorrectiveMeasureRequest result = em.find(CorrectiveMeasureRequest.class, cm.getUri());
                    assertEquals(cm.getDescription(), result.getDescription());
                }
            }
        } finally {
            em.close();
        }
    }

    @Test
    public void updateRemovesOrphanedCorrectiveMeasures() {
        final Audit audit = prepareAuditWithCorrectiveMeasures();
        dao.persist(audit);
        final List<CorrectiveMeasureRequest> removed = new ArrayList<>();
        for (AuditFinding f : audit.getFindings()) {
            final Iterator<CorrectiveMeasureRequest> it = f.getCorrectiveMeasures().iterator();
            removed.add(it.next());
            it.remove();
        }
        dao.update(audit);

        final EntityManager em = emf.createEntityManager();
        try {
            for (CorrectiveMeasureRequest cm : removed) {
                assertNull(em.find(CorrectiveMeasureRequest.class, cm.getUri()));
            }
        } finally {
            em.close();
        }
    }

    @Test
    public void updateRemovesCorrectiveMeasuresFromOneFindingButLeavesItInAnother() {
        final Audit audit = AuditReportGenerator.generateAudit();
        audit.setFindings(AuditReportGenerator.generateFindings());
        final CorrectiveMeasureRequest cm = new CorrectiveMeasureRequest();
        cm.setDescription("Corrective measure");
        cm.setResponsibleOrganizations(Collections.singleton(audit.getAuditee()));
        audit.getFindings().forEach(f -> f.setCorrectiveMeasures(new HashSet<>(Collections.singletonList(cm))));
        dao.persist(audit);

        final List<AuditFinding> emptyFindings = new ArrayList<>();
        audit.getFindings().stream().filter(af -> Generator.randomBoolean())
             .forEach(af -> {
                 af.getCorrectiveMeasures().clear();
                 emptyFindings.add(af);
             });
        // Make sure there is at least one finding where the corrective measure remains
        if (emptyFindings.size() == audit.getFindings().size()) {
            audit.getFindings().iterator().next().setCorrectiveMeasures(new HashSet<>(Collections.singletonList(cm)));
        }
        dao.update(audit);

        final EntityManager em = emf.createEntityManager();
        try {
            for (AuditFinding finding : emptyFindings) {
                final AuditFinding af = em.find(AuditFinding.class, finding.getUri());
                assertTrue(af.getCorrectiveMeasures().isEmpty());
            }
            assertNotNull(em.find(CorrectiveMeasureRequest.class, cm.getUri()));
        } finally {
            em.close();
        }
    }

    @Test
    public void updateRemovesOrphanAuditFindingWithCorrectiveMeasures() {
        final Audit audit = prepareAuditWithCorrectiveMeasures();
        dao.persist(audit);
        final AuditFinding toRemove = audit.getFindings().iterator().next();
        audit.getFindings().remove(toRemove);
        dao.update(audit);

        final EntityManager em = emf.createEntityManager();
        try {
            assertNull(em.find(AuditFinding.class, toRemove.getUri()));
            toRemove.getCorrectiveMeasures()
                    .forEach(m -> assertNull(em.find(CorrectiveMeasureRequest.class, m.getUri())));
        } finally {
            em.close();
        }
    }

    @Test
    public void updateSavesNewQuestionInQuestionTree() {
        final Audit audit = AuditReportGenerator.generateAudit();
        audit.setQuestion(Generator.generateQuestions(3));
        dao.persist(audit);
        final Question q = Generator.question();
        q.setAnswers(Collections.singleton(Generator.answer()));
        audit.getQuestion().getSubQuestions().add(q);
        dao.update(audit);

        final EntityManager em = emf.createEntityManager();
        try {
            assertNotNull(em.find(Question.class, q.getUri()));
            q.getAnswers().forEach(a -> assertNotNull(em.find(Answer.class, a.getUri())));
        } finally {
            em.close();
        }
    }

    @Test
    public void updatePersistsAuditeeWhenItDoesNotExist() {
        final Audit audit = AuditReportGenerator.generateAudit();
        dao.persist(audit);
        final Organization originalOrganization = audit.getAuditee();
        final Organization newOrganization = Generator.generateOrganization();
        audit.setAuditee(newOrganization);
        dao.update(audit);

        verifyOrganizationExists(originalOrganization);
        verifyOrganizationExists(newOrganization);
        final Audit result = dao.find(audit.getUri());
        assertEquals(newOrganization, result.getAuditee());
    }

    @Test
    public void updatePersistAuditorWhenItDoesNotExist() {
        final Audit audit = AuditReportGenerator.generateAudit();
        final Organization originalAuditor = Generator.generateOrganization();
        audit.setAuditor(originalAuditor);
        dao.persist(audit);
        final Organization newAuditor = Generator.generateOrganization();
        audit.setAuditor(newAuditor);
        dao.update(audit);

        verifyOrganizationExists(originalAuditor);
        verifyOrganizationExists(newAuditor);
        final Audit result = dao.find(audit.getUri());
        assertEquals(newAuditor, result.getAuditor());
    }

    @Test
    public void removeRemovesAlsoCorrectiveMeasures() {
        final Audit audit = prepareAuditWithCorrectiveMeasures();
        dao.persist(audit);

        dao.remove(audit);
        verifyAuditCleanup(audit);
    }

    private void verifyAuditCleanup(Audit audit) {
        final EntityManager em = emf.createEntityManager();
        try {
            assertNull(em.find(Audit.class, audit.getUri()));
            audit.getFindings().forEach(f -> {
                f.getCorrectiveMeasures().forEach(m -> assertNull(em.find(CorrectiveMeasureRequest.class, m.getUri())));
                assertNull(em.find(AuditFinding.class, f.getUri()));
            });
        } finally {
            em.close();
        }
    }

    @Test
    public void removeHandlesMultipleReferenceToOneCorrectiveMeasure() {
        final Audit audit = AuditReportGenerator.generateAudit();
        audit.setFindings(AuditReportGenerator.generateFindings());
        final CorrectiveMeasureRequest cm = new CorrectiveMeasureRequest();
        cm.setDescription("Corrective measure");
        cm.setResponsibleOrganizations(Collections.singleton(audit.getAuditee()));
        audit.getFindings().forEach(f -> f.setCorrectiveMeasures(Collections.singleton(cm)));
        dao.persist(audit);

        dao.remove(audit);
        verifyAuditCleanup(audit);
    }
}
