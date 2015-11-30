package cz.cvut.kbss.inbas.audit.persistence.dao;

import cz.cvut.kbss.inbas.audit.model.Occurrence;
import cz.cvut.kbss.inbas.audit.model.reports.InitialReport;
import cz.cvut.kbss.inbas.audit.model.reports.PreliminaryReport;
import cz.cvut.kbss.inbas.audit.util.Constants;
import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import cz.cvut.kbss.jopa.model.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Repository
public class PreliminaryReportDao extends BaseDao<PreliminaryReport> {

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
        if (entity.getRevision().equals(Constants.INITIAL_REVISION)) {
            occurrenceDao.persist(entity.getOccurrence(), em);
        }
        if (entity.getTypeAssessments() != null) {
            entity.getTypeAssessments().forEach(typeAssessmentDao::persist);
        }
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
        saveInitialReports(entity.getInitialReports(), em);
        em.merge(entity);
    }

    /**
     * Gets all preliminary reports for the specified occurrence.
     *
     * @param occurrence Occurrence to filter reports by
     * @return List of matching reports
     */
    public List<PreliminaryReport> findByOccurrence(Occurrence occurrence) {
        Objects.requireNonNull(occurrence);

        final EntityManager em = entityManager();
        try {
            return em.createNativeQuery("SELECT ?r WHERE { ?r <" + Vocabulary.p_hasOccurrence + "> ?occurrence ;" +
                            "rdf:type <" + Vocabulary.PreliminaryReport + "> . }",
                    PreliminaryReport.class).setParameter("occurrence", occurrence.getUri()).getResultList();
        } finally {
            em.close();
        }
    }
}
