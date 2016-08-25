package cz.cvut.kbss.inbas.reporting.persistence.dao;

import cz.cvut.kbss.inbas.reporting.model.Vocabulary;
import cz.cvut.kbss.inbas.reporting.model.audit.AuditReport;
import cz.cvut.kbss.jopa.model.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.net.URI;
import java.util.List;

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
        auditDao.remove(entity.getAudit(), em);
        super.remove(entity, em);
    }
}
