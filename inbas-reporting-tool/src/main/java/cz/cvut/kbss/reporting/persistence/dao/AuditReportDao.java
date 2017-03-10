package cz.cvut.kbss.reporting.persistence.dao;

import cz.cvut.kbss.jopa.exceptions.NoResultException;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.reporting.model.Vocabulary;
import cz.cvut.kbss.reporting.model.audit.AuditFinding;
import cz.cvut.kbss.reporting.model.audit.AuditReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@Repository
public class AuditReportDao extends BaseReportDao<AuditReport> {

    @Autowired
    private AuditDao auditDao;

    public AuditReportDao() {
        super(AuditReport.class);
    }

    @Override
    protected List<AuditReport> findAll(EntityManager em) {
        return em.createNativeQuery("SELECT ?x WHERE { " +
                "?x a ?type ; " +
                "?hasFileNumber ?fileNo ;" +
                "?hasRevision ?revision ;" +
                "?documents ?audit ." +
                "?audit ?auditDate ?startDate ." +
                "{ SELECT (MAX(?rev) AS ?maxRev) ?iFileNo WHERE " +
                "{ ?y a ?type; ?hasFileNumber ?iFileNo ; ?hasRevision ?rev . } GROUP BY ?iFileNo }" +
                "FILTER (?revision = ?maxRev && ?fileNo = ?iFileNo)" +
                "} ORDER BY DESC(?startDate) DESC(?revision)", type)
                 .setParameter("type", typeUri)
                 .setParameter("hasRevision", URI.create(Vocabulary.s_p_has_revision))
                 .setParameter("hasFileNumber", URI.create(Vocabulary.s_p_has_file_number))
                 .setParameter("documents", URI.create(Vocabulary.s_p_documents))
                 .setParameter("auditDate", URI.create(Vocabulary.s_p_has_start_time))
                 .getResultList();
    }

    /**
     * Finds audit report, which documents an audit which contains the specified finding.
     *
     * @param finding Finding contained in audit
     * @return Matching audit report or {@code null}, if there is no matching report
     */
    public AuditReport findByAuditFinding(AuditFinding finding) {
        Objects.requireNonNull(finding);
        final EntityManager em = entityManager();
        try {
            return em.createNativeQuery("SELECT ?x WHERE {" +
                    "?x a ?type ;" +
                    "?documents ?audit ." +
                    "?audit ?hasFinding ?finding . }", type)
                     .setParameter("type", typeUri).setParameter("documents", URI.create(Vocabulary.s_p_documents))
                     .setParameter("hasFinding", URI.create(Vocabulary.s_p_has_part))
                     .setParameter("finding", finding.getUri()).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    protected void persist(AuditReport entity, EntityManager em) {
        auditDao.persist(entity.getAudit(), em);
        super.persist(entity, em);
    }

    @Override
    protected void update(AuditReport entity, EntityManager em) {
        auditDao.update(entity.getAudit(), em);
        super.update(entity, em);
    }

    @Override
    protected void remove(AuditReport entity, EntityManager em) {
        super.remove(entity, em);
        auditDao.remove(entity.getAudit(), em);
    }
}
