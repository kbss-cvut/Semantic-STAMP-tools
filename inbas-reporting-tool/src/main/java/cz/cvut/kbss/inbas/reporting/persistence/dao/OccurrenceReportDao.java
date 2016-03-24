package cz.cvut.kbss.inbas.reporting.persistence.dao;

import cz.cvut.kbss.inbas.reporting.dto.ReportRevisionInfo;
import cz.cvut.kbss.inbas.reporting.model.Occurrence;
import cz.cvut.kbss.inbas.reporting.model.ReportingPhase;
import cz.cvut.kbss.inbas.reporting.model.reports.OccurrenceReport;
import cz.cvut.kbss.inbas.reporting.util.Vocabulary;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import org.springframework.stereotype.Repository;

import java.net.URI;
import java.util.*;

@Repository
public class OccurrenceReportDao extends BaseReportDao<OccurrenceReport>
        implements GenericDao<OccurrenceReport>, SupportsOwlKey<OccurrenceReport> {

    private final URI typeIri;

    public OccurrenceReportDao() {
        super(OccurrenceReport.class);
        final OWLClass owlClass = OccurrenceReport.class.getDeclaredAnnotation(OWLClass.class);
        assert owlClass != null;
        this.typeIri = URI.create(owlClass.iri());
    }

    @Override
    public void persist(OccurrenceReport entity) {
        throw new UnsupportedOperationException("Persist is not supported for OccurrenceReports.");
    }

    @Override
    public void persist(Collection<OccurrenceReport> entities) {
        throw new UnsupportedOperationException("Persist is not supported for OccurrenceReports.");
    }

    @Override
    public void update(OccurrenceReport entity) {
        throw new UnsupportedOperationException("Update is not supported for OccurrenceReports.");
    }

    @Override
    public void remove(OccurrenceReport entity) {
        throw new UnsupportedOperationException("Remove is not supported for OccurrenceReports.");
    }

    @Override
    public void remove(Collection<OccurrenceReport> entities) {
        throw new UnsupportedOperationException("Remove is not supported for OccurrenceReports.");
    }

    public List<OccurrenceReport> findAll(String type) {
        final EntityManager em = entityManager();
        try {
            return em.createNativeQuery(
                    "SELECT ?x WHERE { ?x a ?type ;" +
                            "a ?reportType ;" +
                            "?hasRevision ?revision ;" +
                            "?hasFileNumber ?fileNo ;" +
                            "?hasStartTime ?startTime ;" +
                            // Use only the max revision report for each report chain (file number)
                            "{ SELECT (MAX(?rev) AS ?maxRev) ?iFileNo WHERE " +
                            "{ ?y a ?type ; a ?reportType ; ?hasFileNumber ?iFileNo ; ?hasRevision ?rev . } " +
                            "GROUP BY ?iFileNo }" +
                            "FILTER (?revision = ?maxRev && ?fileNo = ?iFileNo)" +
                            "} ORDER BY DESC(?startTime) DESC(?revision)",
                    OccurrenceReport.class)
                     .setParameter("type", typeIri).setParameter("reportType", URI.create(type))
                     .setParameter("hasRevision", URI.create(
                             Vocabulary.p_revision))
                     .setParameter("hasStartTime", URI.create(Vocabulary.p_startTime))
                     .setParameter("hasFileNumber", URI.create(Vocabulary.p_fileNumber))
                     .getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Gets reports concerning the specified occurrence.
     * <p>
     * Only latest revisions of reports of every report chain are returned.
     *
     * @param occurrence The occurrence to filter reports by
     * @return List of reports
     */
    public List<OccurrenceReport> findByOccurrence(Occurrence occurrence) {
        final EntityManager em = entityManager();
        try {
            return em.createNativeQuery(
                    "SELECT ?x WHERE { ?x a ?type ;" +
                            "?hasRevision ?revision ;" +
                            "?hasStartTime ?startTime ;" +
                            "?hasFileNumber ?fileNo ;" +
                            "?hasOccurrence ?occurrence . " +
                            // Use only the max revision reports
                            "{ SELECT (MAX(?rev) AS ?maxRev) ?iFileNo WHERE " +
                            "{ ?y a ?type ; ?hasOccurrence ?occurrence ; ?hasFileNumber ?iFileNo ; ?hasRevision ?rev . }" +
                            " GROUP BY ?iFileNo }" +
                            "FILTER (?revision = ?maxRev && ?fileNo = ?iFileNo)" +
                            "} ORDER BY DESC(?startTime) DESC(?revision)",
                    OccurrenceReport.class)
                     .setParameter("type", typeIri)
                     .setParameter("occurrence", occurrence.getUri())
                     .setParameter("hasRevision", URI.create(Vocabulary.p_revision))
                     .setParameter("hasOccurrence", URI.create(Vocabulary.p_hasOccurrence))
                     .setParameter("hasStartTime", URI.create(Vocabulary.p_startTime))
                     .setParameter("hasFileNumber", URI.create(Vocabulary.p_fileNumber))
                     .getResultList();
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
                    "SELECT ?x ?revision ?key ?created ?phase WHERE { ?x a ?type ;" +
                            "a ?phase ;" +
                            "?hasRevision ?revision ; " +
                            "?wasCreated ?created ;" +
                            "?hasFileNumber ?fileNo ;" +
                            "?hasKey ?key ." +
                            "FILTER (?type != ?phase)" +
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
                if (!ReportingPhase.isSupported((URI) rowArr[4])) {
                    continue;
                }
                info.setPhase(ReportingPhase.fromType((URI) rowArr[4]));
                result.add(info);
            }
            return result;
        } finally {
            em.close();
        }
    }
}
