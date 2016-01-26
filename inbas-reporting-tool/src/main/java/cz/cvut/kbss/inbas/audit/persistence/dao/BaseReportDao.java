package cz.cvut.kbss.inbas.audit.persistence.dao;

import cz.cvut.kbss.inbas.audit.persistence.PersistenceException;
import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import cz.cvut.kbss.jopa.exceptions.NoResultException;
import cz.cvut.kbss.jopa.exceptions.NoUniqueResultException;
import cz.cvut.kbss.jopa.model.EntityManager;

import java.net.URI;
import java.util.List;

public abstract class BaseReportDao<T> extends BaseDao<T> {

    protected BaseReportDao(Class<T> type) {
        super(type);
    }

    @Override
    protected List<T> findAll(EntityManager em) {
        return em.createNativeQuery("SELECT ?x WHERE { " +
                "?x a ?type ; " +
                "?hasFileNumber ?fileNo ;" +
                "?hasStartTime ?startTime ;" +
                "?hasRevision ?revision . " +
                "{ SELECT (MAX(?rev) AS ?maxRev) ?iFileNo WHERE " +
                "{ ?y a ?type; ?hasFileNumber ?iFileNo ; ?hasRevision ?rev . } GROUP BY ?iFileNo }" +
                "FILTER (?revision = ?maxRev && ?fileNo = ?iFileNo)" +
                "} ORDER BY DESC(?startTime) DESC(?revision)", type)
                 .setParameter("type", typeUri)
                 .setParameter("hasRevision", URI.create(Vocabulary.p_revision))
                 .setParameter("hasFileNumber", URI.create(Vocabulary.p_fileNumber))
                 .setParameter("hasStartTime", URI.create(Vocabulary.p_startTime))
                 .getResultList();
    }

    /**
     * Gets latest revision in report chain with the specified file number.
     * <p>
     * The returned report is latest revision of type managed by this DAO. It does not mean that there cannot be a newer
     * revision of different type.
     *
     * @param fileNumber Report chain identifier
     * @return Report with highest revision number or {@code null} if there is no such with the specified file number
     */
    public T findLatestRevision(Long fileNumber) {
        final EntityManager em = entityManager();
        try {
            return em.createNativeQuery("SELECT ?x WHERE {" +
                    "?x a ?type ;" +
                    "?hasFileNumber ?fileNo ;" +
                    "?hasRevision ?revision ." +
                    "{ SELECT (MAX(?rev) AS ?maxRev) WHERE " +
                    "{ ?y ?hasFileNumber ?fileNo ; ?hasRevision ?rev . } } FILTER (?revision = ?maxRev) }", type)
                     .setParameter("type", typeUri)
                     .setParameter("hasRevision", URI.create(Vocabulary.p_revision))
                     .setParameter("hasFileNumber", URI.create(Vocabulary.p_fileNumber))
                     .setParameter("fileNo", fileNumber).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NoUniqueResultException e) {
            LOG.error("Expected single report with highest revision number, but got multiple!");
            throw new PersistenceException(e);
        } finally {
            em.close();
        }
    }
}
