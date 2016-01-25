package cz.cvut.kbss.inbas.audit.persistence.dao;

import cz.cvut.kbss.inbas.audit.model.reports.InitialReport;
import cz.cvut.kbss.inbas.audit.model.reports.PreliminaryReport;
import cz.cvut.kbss.inbas.audit.util.Constants;
import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import cz.cvut.kbss.jopa.model.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Set;

@Repository
public class PreliminaryReportDao extends BaseReportDao<PreliminaryReport> {

    @Autowired
    private OccurrenceDao occurrenceDao;

    @Autowired
    private EventTypeAssessmentDao typeAssessmentDao;

    @Autowired
    private InitialReportDao initialReportDao;

    public PreliminaryReportDao() {
        super(PreliminaryReport.class);
    }

    @Override
    protected void persist(PreliminaryReport entity, EntityManager em) {
        if (entity.getRevision() == null) {
            entity.setRevision(Constants.INITIAL_REVISION);
        }
        if (entity.getRevision().equals(Constants.INITIAL_REVISION) &&
                !occurrenceDao.exists(entity.getOccurrence().getUri(), em)) {
            occurrenceDao.persist(entity.getOccurrence(), em);
        }
        if (entity.getTypeAssessments() != null) {
            entity.getTypeAssessments().forEach(typeAssessmentDao::persist);
        }
        entity.addType(Vocabulary.Report);
        saveInitialReports(entity.getInitialReports(), em);
        entity.generateKey();
        em.persist(entity);
    }

    private void saveInitialReports(Set<InitialReport> initialReports, EntityManager em) {
        if (initialReports == null) {
            return;
        }
        initialReports.stream().filter(ir -> !initialReportDao.exists(ir.getUri(), em))
                      .forEach(ir -> initialReportDao.persist(ir, em));
    }

    @Override
    public void update(PreliminaryReport entity, EntityManager em) {
        Objects.requireNonNull(entity);

        if (entity.getTypeAssessments() != null) {
            entity.getTypeAssessments().forEach(eta -> {
                if (eta.getUri() == null) {
                    typeAssessmentDao.persist(eta, em);
                } else {
                    typeAssessmentDao.update(eta, em);
                }
            });
        }
        entity.addType(Vocabulary.Report);
        saveInitialReports(entity.getInitialReports(), em);
        em.merge(entity);
    }
}
