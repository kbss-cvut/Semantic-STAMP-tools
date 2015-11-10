package cz.cvut.kbss.inbas.audit.persistence.dao;

import cz.cvut.kbss.inbas.audit.model.Occurrence;
import cz.cvut.kbss.inbas.audit.model.reports.EventType;
import cz.cvut.kbss.inbas.audit.model.reports.Factor;
import cz.cvut.kbss.inbas.audit.model.reports.InvestigationReport;
import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import cz.cvut.kbss.jopa.model.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class InvestigationReportDao extends BaseDao<InvestigationReport> {

    @Autowired
    private EventTypeDao eventTypeDao;
    @Autowired
    private EventTypeAssessmentDao typeAssessmentDao;

    public InvestigationReportDao() {
        super(InvestigationReport.class);
    }

    /**
     * Gets all investigation reports for the specified occurrence.
     *
     * @param occurrence Occurrence to filter reports by
     * @return List of matching reports
     */
    public List<InvestigationReport> findByOccurrence(Occurrence occurrence) {
        Objects.requireNonNull(occurrence);

        final EntityManager em = entityManager();
        try {
            return em.createNativeQuery("SELECT ?r WHERE { ?r <" + Vocabulary.p_hasOccurrence + "> ?occurrence ;" +
                            "a <" + Vocabulary.InvestigationReport + "> . }",
                    InvestigationReport.class).setParameter("occurrence", occurrence.getUri()).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    protected void persist(InvestigationReport entity, EntityManager em) {
        if (entity.getRootFactor() != null) {
            persistFactors(entity.getRootFactor(), em);
        }
        super.persist(entity, em);
    }

    private void persistFactors(Factor factor, EntityManager em) {
        if (factor.getType().getId() == null || em.find(EventType.class, factor.getType().getId()) == null) {
            eventTypeDao.persist(factor.getType(), em);
        }
        if (factor.getAssessment() != null) {
            typeAssessmentDao.persist(factor.getAssessment());
        }
        em.persist(factor);
        if (factor.getChildren() != null) {
            for (Factor f : factor.getChildren()) {
                persistFactors(f, em);
            }
        }
    }
}
