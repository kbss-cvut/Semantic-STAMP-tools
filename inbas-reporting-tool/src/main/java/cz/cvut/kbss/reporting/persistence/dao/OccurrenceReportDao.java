package cz.cvut.kbss.reporting.persistence.dao;

import cz.cvut.kbss.jopa.exceptions.NoResultException;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.reporting.filter.ReportFilter;
import cz.cvut.kbss.reporting.model.Occurrence;
import cz.cvut.kbss.reporting.model.OccurrenceReport;
import cz.cvut.kbss.reporting.model.StampVocabulary;
import cz.cvut.kbss.reporting.model.Vocabulary;
import cz.cvut.kbss.reporting.persistence.util.OrphanRemover;
import cz.cvut.kbss.reporting.service.event.InvalidateCacheEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
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
public class OccurrenceReportDao extends BaseReportDao<OccurrenceReport>
        implements ApplicationListener<InvalidateCacheEvent> {

    private static final String SELECT = "SELECT DISTINCT ?x WHERE { ";
    private static final String WHERE_CONDITION = "?x a ?type ; " +
            "?hasKey ?key ;" +
            "?hasAuthor ?author ;" +
            "?hasCreatedDate ?created ;" +
            "?hasOccurrence ?occurrence ." +
            "OPTIONAL { ?x ?hasSeverity ?severity . }" +
            "OPTIONAL { ?x ?hasLastEditor ?lastEditor . }" +
            "OPTIONAL { ?x ?hasLastEditedDate ?lastModified . }" +
            "OPTIONAL { ?occurrence ?hasEventType ?occurrenceCategory }." +
            "OPTIONAL { ?occurrence ?hasLossEventType ?lossEventType }." +
            "FILTER NOT EXISTS { " +
            "?y a ?type . " +
            "?x ?hasNext ?y . }" +
            "BIND (IF (BOUND(?lastModified), ?lastModified, ?created) AS ?edited)";
    private static final String QUERY_TAIL = "} ORDER BY DESC(?edited) LIMIT ?limit OFFSET ?offset";

    // Current count of latest revisions of reports
    private volatile long reportCount = -1;

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
                .setParameter("hasSeverity", URI.create(Vocabulary.s_p_has_severity_assessment))
                .setParameter("hasLastEditor", URI.create(Vocabulary.s_p_has_last_editor))
                .setParameter("hasAuthor", URI.create(Vocabulary.s_p_has_author))
                .setParameter("hasOccurrence", URI.create(Vocabulary.s_p_documents))
                .setParameter("hasEventType", URI.create(Vocabulary.s_p_has_event_type))
                .setParameter("hasNext", URI.create(Vocabulary.s_p_has_next_revision))
                .setParameter("hasCreatedDate", URI.create(Vocabulary.s_p_created))
                .setParameter("hasLastEditedDate", URI.create(Vocabulary.s_p_modified))
                .setParameter("hasLossEventType", URI.create(StampVocabulary.s_p_contains_loss_event_of_type))
                .setUntypedParameter("limit", pageSpec.getPageSize())
                .setUntypedParameter("offset", pageSpec.getPageSize() * pageSpec.getPageNumber())
                .getResultList();
        return new PageImpl<>(
                res.stream().map(cz.cvut.kbss.reporting.model.reportlist.OccurrenceReport::toOccurrenceReport)
                   .collect(Collectors.toList()), pageSpec, getReportCount(em));
    }

    private String buildQuery(Collection<ReportFilter> filters) {
        final StringBuilder sb = new StringBuilder(SELECT);
        List<ReportFilter> graphPatternFilters = filters.stream().filter(f -> f.isGraphPatternFilter()).collect(Collectors.toList());
        List<ReportFilter> filterConditions = filters.stream().filter(f -> !f.isGraphPatternFilter()).collect(Collectors.toList());

        sb.append(WHERE_CONDITION);
        if (!graphPatternFilters.isEmpty()) {
            sb.append(String.join("\n",
                    graphPatternFilters.stream().map(ReportFilter::toQueryString).collect(Collectors.toList())));
        }
        if(!filterConditions.isEmpty()){
            sb.append(" FILTER (");
            sb.append(String.join(" && ",
                    filterConditions.stream().map(ReportFilter::toQueryString).collect(Collectors.toList())));
            sb.append(')');
        }
        sb.append(QUERY_TAIL);
        return sb.toString();
    }

    /**
     * Gets total count of latest revisions of all reports in the repository.
     *
     * @param em EntityManager
     * @return Report count
     */
    private long getReportCount(EntityManager em) {
        if (reportCount == -1) {
            this.reportCount = em.createNativeQuery("SELECT (count(?x) as ?cnt) WHERE {" +
                    "?x a ?type ;" +
                    "FILTER NOT EXISTS {\n" +
                    "?y a ?type ." +
                    "?x ?hasNext ?y . }" +
                    "}", Integer.class) // Count returns an xsd:int (as per SPARQL specification)
                                 .setParameter("type", typeUri)
                                 .setParameter("hasNext", URI.create(Vocabulary.s_p_has_next_revision))
                                 .getSingleResult();
        }
        return reportCount;
    }

    private void resetReportCount() {
        this.reportCount = -1;
    }

    @Override
    protected void persist(OccurrenceReport entity, EntityManager em) {
        assert entity != null;
        if (entity.getOccurrence() != null && entity.getOccurrence().getUri() == null) {
            occurrenceDao.persist(entity.getOccurrence(), em);
        }
        super.persist(entity, em);
        resetReportCount();
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
        resetReportCount();
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

    @Override
    public void onApplicationEvent(InvalidateCacheEvent invalidateCacheEvent) {
        resetReportCount();
    }
}
