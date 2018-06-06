package cz.cvut.kbss.reporting.persistence.dao;

import cz.cvut.kbss.jopa.exceptions.NoResultException;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.reporting.filter.ReportFilter;
import cz.cvut.kbss.reporting.model.Occurrence;
import cz.cvut.kbss.reporting.model.OccurrenceReport;
import cz.cvut.kbss.reporting.model.Vocabulary;
import cz.cvut.kbss.reporting.persistence.util.OrphanRemover;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class OccurrenceReportDao extends BaseReportDao<OccurrenceReport> {

    private static final String SELECT = "SELECT DISTINCT ?x WHERE { ";
    private static final String WHERE_CONDITION = "?x a ?type ; " +
            "?hasKey ?key ;" +
            "?hasRevision ?revision ;" +
            "?hasAuthor ?author ;" +
            "?hasOccurrence ?occurrence ." +
            "OPTIONAL { ?x ?hasSeverity ?severity . }" +
            "OPTIONAL { ?x ?hasLastEditor ?lastEditor . }" +
            "?occurrence ?hasStartTime ?startTime ;" +
            "?hasEventType ?occurrenceCategory ." +
            "FILTER NOT EXISTS { " +
            "?y a ?type . " +
            "?x ?hasNext ?y . }";
    private static final String QUERY_TAIL = "} ORDER BY DESC(?startTime) DESC(?revision) LIMIT ?limit OFFSET ?offset";

    private final OccurrenceDao occurrenceDao;

    @Autowired
    public OccurrenceReportDao(OccurrenceDao occurrenceDao) {
        super(OccurrenceReport.class);
        this.occurrenceDao = occurrenceDao;
    }

    @Override
    protected List<OccurrenceReport> findAll(EntityManager em) {
        final Pageable pageSpec = PageRequest.of(0, Integer.MAX_VALUE);
        return findAll(pageSpec, Collections.emptyList(), em).getContent();
    }

    @Override
    public Page<OccurrenceReport> findAll(Pageable pageSpec, Collection<ReportFilter> filters) {
        Objects.requireNonNull(pageSpec);
        Objects.requireNonNull(filters);
        final EntityManager em = entityManager();
        try {
            return findAll(pageSpec, filters, em);
        } finally {
            em.close();
        }
    }

    private Page<OccurrenceReport> findAll(Pageable pageSpec, Collection<ReportFilter> filters, EntityManager em) {
        final List<cz.cvut.kbss.reporting.model.reportlist.OccurrenceReport> res = em
                .createNativeQuery(buildQuery(filters),
                        cz.cvut.kbss.reporting.model.reportlist.OccurrenceReport.class)
                .setParameter("type", typeUri)
                .setParameter("hasKey", URI.create(Vocabulary.s_p_has_key))
                .setParameter("hasRevision", URI.create(Vocabulary.s_p_has_revision))
                .setParameter("hasSeverity", URI.create(Vocabulary.s_p_has_severity_assessment))
                .setParameter("hasLastEditor", URI.create(Vocabulary.s_p_has_last_editor))
                .setParameter("hasAuthor", URI.create(Vocabulary.s_p_has_author))
                .setParameter("hasOccurrence", URI.create(Vocabulary.s_p_documents))
                .setParameter("hasStartTime", URI.create(Vocabulary.s_p_has_start_time))
                .setParameter("hasEventType", URI.create(Vocabulary.s_p_has_event_type))
                .setParameter("hasNext", URI.create(Vocabulary.s_p_has_next_revision))
                .setUntypedParameter("limit", pageSpec.getPageSize())
                .setUntypedParameter("offset", pageSpec.getPageSize() * pageSpec.getPageNumber())
                .getResultList();
        return new PageImpl<>(
                res.stream().map(cz.cvut.kbss.reporting.model.reportlist.OccurrenceReport::toOccurrenceReport)
                   .collect(Collectors.toList()), pageSpec, 0L);
    }

    private String buildQuery(Collection<ReportFilter> filters) {
        final StringBuilder sb = new StringBuilder(SELECT);
        sb.append(WHERE_CONDITION);
        if (!filters.isEmpty()) {
            sb.append(" FILTER (");
            sb.append(String.join(" && ",
                    filters.stream().map(ReportFilter::toQueryString).collect(Collectors.toList())));
            sb.append(')');
        }
        sb.append(QUERY_TAIL);
        return sb.toString();
    }

    private long getReportCount(EntityManager em) {
        return em.createNativeQuery("SELECT (count(?x) as ?cnt) WHERE {" +
                "?x a ?type ;" +
                "FILTER NOT EXISTS {\n" +
                "?y a ?type ." +
                "?x ?hasNext ?y . }" +
                "}", Long.class)
                 .setParameter("type", typeUri)
                 .setParameter("hasNext", URI.create(Vocabulary.s_p_has_next_revision)).getSingleResult();
    }

    @Override
    protected void persist(OccurrenceReport entity, EntityManager em) {
        assert entity != null;
        if (entity.getOccurrence() != null && entity.getOccurrence().getUri() == null) {
            occurrenceDao.persist(entity.getOccurrence(), em);
        }
        super.persist(entity, em);
    }

    @Override
    protected void update(OccurrenceReport entity, EntityManager em) {
        final OccurrenceReport original = em.find(OccurrenceReport.class, entity.getUri());
        assert original != null;
        em.detach(original);
        occurrenceDao.update(entity.getOccurrence(), em);
        final OccurrenceReport merged = em.merge(entity);
        final OrphanRemover orphanRemover = new OrphanRemover(em);
        orphanRemover.removeOrphans(original.getCorrectiveMeasures(), merged.getCorrectiveMeasures());
        orphanRemover.removeOrphans(original.getReferences(), merged.getReferences());
        em.getEntityManagerFactory().getCache()
          .evict(cz.cvut.kbss.reporting.model.reportlist.OccurrenceReport.class, entity.getUri(), null);
    }

    @Override
    protected void remove(OccurrenceReport entity, EntityManager em) {
        occurrenceDao.remove(entity.getOccurrence(), em);
        super.remove(entity, em);
    }

    /**
     * Gets reports concerning the specified occurrence.
     * <p>
     * Only latest revisions of reports of every report chain are returned.
     *
     * @param occurrence The occurrence to filter reports by
     * @return List of reports
     */
    public OccurrenceReport findByOccurrence(Occurrence occurrence) {
        final EntityManager em = entityManager();
        try {
            return em.createNativeQuery(
                    "SELECT ?x WHERE { " +
                            "?x a ?type ;" +
                            "?documents ?occurrence . }",
                    OccurrenceReport.class)
                     .setParameter("type", typeUri)
                     .setParameter("documents", URI.create(Vocabulary.s_p_documents))
                     .setParameter("occurrence", occurrence.getUri())
                     .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}
