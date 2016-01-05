package cz.cvut.kbss.inbas.audit.persistence.dao;

import cz.cvut.kbss.inbas.audit.model.reports.OccurrenceReport;
import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import org.springframework.stereotype.Repository;

import java.net.URI;
import java.util.Collection;
import java.util.List;

@Repository
public class OccurrenceReportDao extends BaseDao<OccurrenceReport>
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

    @Override
    protected List<OccurrenceReport> findAll(EntityManager em) {
        return em.createNativeQuery(
                "SELECT ?x WHERE { ?x a ?type ;" +
                        "?hasRevision ?revision ;" +
                        "?hasStartTime ?startTime ;" +
                        "?hasOccurrence ?occurrence . " +
                        // Use only the max revision report for each occurrence
                        "{ SELECT (MAX(?rev) AS ?maxRev) WHERE { ?y ?hasOccurrence ?occurrence ; ?hasRevision ?rev . } }" +
                        "FILTER (?revision = ?maxRev)" +
                        "} ORDER BY DESC(?startTime) DESC(?revision)",
                OccurrenceReport.class)
                 .setParameter("type", typeIri)
                 .setParameter("hasRevision", URI.create(
                         Vocabulary.p_revision)).setParameter("hasOccurrence", URI.create(Vocabulary.p_hasOccurrence))
                 .setParameter("hasStartTime", URI.create(Vocabulary.p_startTime))
                 .getResultList();
    }

    public List<OccurrenceReport> findAll(String type) {
        final EntityManager em = entityManager();
        try {
            return em.createNativeQuery(
                    "SELECT ?x WHERE { ?x a ?type ;" +
                            "a ?reportType ;" +
                            "?hasRevision ?revision ;" +
                            "?hasStartTime ?startTime ;" +
                            "?hasOccurrence ?occurrence . " +
                            // Use only the max revision report for each occurrence
                            "{ SELECT (MAX(?rev) AS ?maxRev) WHERE { ?y ?hasOccurrence ?occurrence ; ?hasRevision ?rev . } }" +
                            "FILTER (?revision = ?maxRev)" +
                            "} ORDER BY DESC(?startTime) DESC(?revision)",
                    OccurrenceReport.class)
                     .setParameter("type", typeIri).setParameter("reportType", URI.create(type))
                     .setParameter("hasRevision", URI.create(
                             Vocabulary.p_revision))
                     .setParameter("hasOccurrence", URI.create(Vocabulary.p_hasOccurrence))
                     .setParameter("hasStartTime", URI.create(Vocabulary.p_startTime))
                     .getResultList();
        } finally {
            em.close();
        }
    }
}
