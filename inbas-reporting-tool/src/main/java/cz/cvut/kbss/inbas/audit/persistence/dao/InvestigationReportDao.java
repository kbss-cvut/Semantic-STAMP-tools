package cz.cvut.kbss.inbas.audit.persistence.dao;

import cz.cvut.kbss.inbas.audit.model.reports.Factor;
import cz.cvut.kbss.inbas.audit.model.reports.InitialReport;
import cz.cvut.kbss.inbas.audit.model.reports.InvestigationReport;
import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import cz.cvut.kbss.jopa.model.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public class InvestigationReportDao extends BaseReportDao<InvestigationReport> {

    @Autowired
    private FactorDao factorDao;
    @Autowired
    private InitialReportDao initialReportDao;

    public InvestigationReportDao() {
        super(InvestigationReport.class);
    }

    @Override
    protected void persist(InvestigationReport entity, EntityManager em) {
        if (entity.getRootFactor() != null) {
            persistFactors(entity.getRootFactor(), em);
        }
        persistOccurrenceCategoryIfNecessary(entity.getOccurrenceCategory(), em);
        persistInitialReports(entity.getInitialReports(), em);
        entity.addType(Vocabulary.Report);
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
        persistOccurrenceCategoryIfNecessary(entity.getOccurrenceCategory(), em);
        entity.addType(Vocabulary.Report);
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

    @Override
    protected void remove(InvestigationReport entity, EntityManager em) {
        final InvestigationReport ir = em.merge(entity);
        em.remove(ir);
    }
}
