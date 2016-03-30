package cz.cvut.kbss.inbas.reporting.persistence.dao;

import cz.cvut.kbss.inbas.reporting.dto.ReportRevisionInfo;
import cz.cvut.kbss.inbas.reporting.model_new.Vocabulary;
import cz.cvut.kbss.inbas.reporting.model_new.util.HasOwlKey;
import cz.cvut.kbss.inbas.reporting.persistence.PersistenceException;
import cz.cvut.kbss.jopa.exceptions.NoResultException;
import cz.cvut.kbss.jopa.exceptions.NoUniqueResultException;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
     * Gets a list of revision info instances for a report chain identified by the specified file number.
     *
     * @param fileNumber Report chain identifier
     * @return List of revision infos, ordered by revision number (descending)
     */
    public List<ReportRevisionInfo> getReportChainRevisions(Long fileNumber) {
        Objects.requireNonNull(fileNumber);
        final EntityManager em = entityManager();
        try {
            final List rows = em.createNativeQuery(
                    "SELECT ?x ?revision ?key ?created WHERE { ?x a ?type ;" +
                            "?hasRevision ?revision ; " +
                            "?wasCreated ?created ;" +
                            "?hasFileNumber ?fileNo ;" +
                            "?hasKey ?key ." +
                            "} ORDER BY DESC(?revision)")
                                .setParameter("type", typeIri)
                                .setParameter("hasRevision", URI.create(Vocabulary.p_revision))
                                .setParameter("wasCreated", URI.create(Vocabulary.p_dateCreated))
                                .setParameter("hasKey", URI.create(Vocabulary.p_hasKey))
                                .setParameter("hasFileNumber", URI.create(Vocabulary.p_fileNumber))
                                .setParameter("fileNo", fileNumber)
                                .getResultList();
            final List<ReportRevisionInfo> result = new ArrayList<>(rows.size());
            for (Object row : rows) {
                final Object[] rowArr = (Object[]) row;
                final ReportRevisionInfo info = new ReportRevisionInfo();
                info.setUri((URI) rowArr[0]);
                info.setRevision((Integer) rowArr[1]);
                info.setKey((String) rowArr[2]);
                info.setCreated((Date) rowArr[3]);
                result.add(info);
            }
            return result;
        } finally {
            em.close();
        }
    }
}
