package cz.cvut.kbss.inbas.reporting.persistence.dao;

import cz.cvut.kbss.inbas.reporting.model.safetyissue.SafetyIssueReport;
import cz.cvut.kbss.inbas.reporting.persistence.dao.util.OrphanRemover;
import cz.cvut.kbss.jopa.model.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class SafetyIssueReportDao extends BaseReportDao<SafetyIssueReport> {

    @Autowired
    private SafetyIssueDao safetyIssueDao;

    public SafetyIssueReportDao() {
        super(SafetyIssueReport.class);
    }

    @Override
    protected void persist(SafetyIssueReport entity, EntityManager em) {
        assert entity != null;
        if (entity.getSafetyIssue() != null && entity.getSafetyIssue().getUri() == null) {
            safetyIssueDao.persist(entity.getSafetyIssue(), em);
        }
        super.persist(entity, em);
    }

    @Override
    protected void update(SafetyIssueReport entity, EntityManager em) {
        final SafetyIssueReport original = em.find(SafetyIssueReport.class, entity.getUri());
        assert original != null;
        em.merge(entity);
        new OrphanRemover(em).removeOrphans(original.getCorrectiveMeasures(), entity.getCorrectiveMeasures());
        safetyIssueDao.update(entity.getSafetyIssue(), em);
    }
}
