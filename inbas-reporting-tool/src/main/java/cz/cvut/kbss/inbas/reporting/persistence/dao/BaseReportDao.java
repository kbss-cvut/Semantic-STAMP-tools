package cz.cvut.kbss.inbas.reporting.persistence.dao;

import cz.cvut.kbss.inbas.reporting.model_new.Vocabulary;
import cz.cvut.kbss.inbas.reporting.model_new.util.HasOwlKey;
import cz.cvut.kbss.inbas.reporting.persistence.PersistenceException;
import cz.cvut.kbss.jopa.exceptions.NoResultException;
import cz.cvut.kbss.jopa.exceptions.NoUniqueResultException;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;

import java.net.URI;
import java.util.List;

abstract class BaseReportDao<T extends HasOwlKey> extends OwlKeySupportingDao<T> {

    final URI typeIri;

    BaseReportDao(Class<T> type) {
        super(type);
        final OWLClass owlClass = type.getDeclaredAnnotation(OWLClass.class);
        assert owlClass != null;
        this.typeIri = URI.create(owlClass.iri());
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

    /**
     * Finds report with the specified revision in report chain with the specified identifier.
     *
     * @param fileNumber Report chain identifier
     * @param revision   Report revision
     * @return Matching report or {@code null}, if no such report exists
     */
    public T findRevision(Long fileNumber, Integer revision) {
        final EntityManager em = entityManager();
        try {
            return em.createNativeQuery("SELECT ?x WHERE {" +
                    "?x a ?type ;" +
                    "?hasFileNumber ?fileNo ;" +
                    "?hasRevision ?revision . }", type)
                     .setParameter("type", typeIri)
                     .setParameter("hasFileNumber", URI.create(Vocabulary.p_fileNumber))
                     .setParameter("fileNo", fileNumber)
                     .setParameter("hasRevision", URI.create(Vocabulary.p_revision))
                     .setParameter("revision", revision)
                     .getSingleResult();
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
