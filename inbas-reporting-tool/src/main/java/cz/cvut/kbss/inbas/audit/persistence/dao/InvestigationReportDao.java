package cz.cvut.kbss.inbas.audit.persistence.dao;

import cz.cvut.kbss.inbas.audit.model.Occurrence;
import cz.cvut.kbss.inbas.audit.model.reports.InvestigationReport;
import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import cz.cvut.kbss.jopa.model.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class InvestigationReportDao extends BaseDao<InvestigationReport> {

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
}
