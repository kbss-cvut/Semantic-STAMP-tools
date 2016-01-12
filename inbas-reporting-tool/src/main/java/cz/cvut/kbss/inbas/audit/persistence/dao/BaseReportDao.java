package cz.cvut.kbss.inbas.audit.persistence.dao;

import cz.cvut.kbss.inbas.audit.model.Occurrence;
import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import cz.cvut.kbss.jopa.model.EntityManager;

import java.net.URI;
import java.util.List;
import java.util.Objects;

public abstract class BaseReportDao<T> extends BaseDao<T> {

    protected BaseReportDao(Class<T> type) {
        super(type);
    }

    @Override
    protected List<T> findAll(EntityManager em) {
        return em.createNativeQuery("SELECT ?x WHERE { " +
                "?x a ?type ; " +
                "?hasOccurrence ?occurrence ; " +
                "?hasRevision ?revision . " +
                "{ SELECT (MAX(?rev) AS ?maxRev) ?yOccurrence WHERE " +
                    "{ ?y a ?type; ?hasOccurrence ?yOccurrence ; ?hasRevision ?rev . } GROUP BY ?yOccurrence }" +
                "FILTER (?revision = ?maxRev && ?occurrence = ?yOccurrence)" +
                "}", type)
                 .setParameter("type", typeUri).setParameter("hasOccurrence", URI.create(Vocabulary.p_hasOccurrence))
                 .setParameter("hasRevision", URI.create(Vocabulary.p_revision)).getResultList();
    }

    /**
     * Gets all preliminary reports for the specified occurrence.
     * <p>
     * The reports are ordered by their revision (ascending).
     *
     * @param occurrence Occurrence to filter reports by
     * @return List of matching reports
     */
    public List<T> findByOccurrence(Occurrence occurrence) {
        Objects.requireNonNull(occurrence);

        final EntityManager em = entityManager();
        try {
            return em.createNativeQuery("SELECT ?r WHERE { ?r a ?type ;" +
                    "?hasOccurrence ?occurrence ; " +
                    "?hasRevision ?revision . } ORDER BY ?revision", type)
                     .setParameter("type", typeUri)
                     .setParameter("hasOccurrence", URI.create(Vocabulary.p_hasOccurrence))
                     .setParameter("hasRevision", URI.create(Vocabulary.p_revision))
                     .setParameter("occurrence", occurrence.getUri()).getResultList();
        } finally {
            em.close();
        }
    }
}
