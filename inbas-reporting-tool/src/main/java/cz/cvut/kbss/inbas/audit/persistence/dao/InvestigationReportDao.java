package cz.cvut.kbss.inbas.audit.persistence.dao;

import cz.cvut.kbss.inbas.audit.model.Occurrence;
import cz.cvut.kbss.inbas.audit.model.reports.Factor;
import cz.cvut.kbss.inbas.audit.model.reports.InitialReport;
import cz.cvut.kbss.inbas.audit.model.reports.InvestigationReport;
import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import cz.cvut.kbss.jopa.model.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Repository
public class InvestigationReportDao extends BaseDao<InvestigationReport> {

    @Autowired
    private FactorDao factorDao;
    @Autowired
    private InitialReportDao initialReportDao;

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
        persistInitialReports(entity.getInitialReports(), em);
        super.persist(entity, em);
    }

    private void persistFactors(Factor factor, EntityManager em) {
        factorDao.persist(factor, em);
        if (factor.getChildren() != null) {
            for (Factor f : factor.getChildren()) {
                persistFactors(f, em);
            }
        }
    }

    private void persistInitialReports(Set<InitialReport> reports, EntityManager em) {
        if (reports == null || reports.isEmpty()) {
            return;
        }
        reports.forEach(initialReport -> initialReportDao.persist(initialReport, em));
    }

    @Override
    protected void update(InvestigationReport entity, EntityManager em) {
        if (entity.getRootFactor() != null) {
            updateFactors(entity.getRootFactor(), em);
        }
        super.update(entity, em);
    }

    private void updateFactors(Factor factor, EntityManager em) {
        factorDao.update(factor, em);
        if (factor.getChildren() != null) {
            for (Factor f : factor.getChildren()) {
                updateFactors(f, em);
            }
        }
    }
}
