package cz.cvut.kbss.reporting.persistence.dao;

import cz.cvut.kbss.jopa.exceptions.NoResultException;
import cz.cvut.kbss.jopa.model.EntityManager;
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
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class OccurrenceReportDao extends BaseReportDao<OccurrenceReport> {

    private final OccurrenceDao occurrenceDao;

    @Autowired
    public OccurrenceReportDao(OccurrenceDao occurrenceDao) {
        super(OccurrenceReport.class);
        this.occurrenceDao = occurrenceDao;
    }

    @Override
    protected List<OccurrenceReport> findAll(EntityManager em) {
        final Pageable pageSpec = PageRequest.of(0, Integer.MAX_VALUE);
        return findAll(pageSpec, em).getContent();
    }

    @Override
    public Page<OccurrenceReport> findAll(Pageable pageSpec) {
        final EntityManager em = entityManager();
        try {
            return findAll(pageSpec, em);
        } finally {
            em.close();
        }
    }

    private Page<OccurrenceReport> findAll(Pageable pageSpec, EntityManager em) {
        final List<cz.cvut.kbss.reporting.model.reportlist.OccurrenceReport> res = em
                .createNativeQuery("SELECT ?x WHERE { " +
                                "?x a ?type ; " +
                                "?hasFileNumber ?fileNo ;" +
                                "?hasRevision ?maxRev ;" +
                                "?hasOccurrence ?occurrence ." +
                                "?occurrence ?hasStartTime ?startTime ." +
                                "{ SELECT (MAX(?rev) AS ?maxRev) ?fileNo WHERE " +
                                "{ ?y a ?type; ?hasFileNumber ?fileNo ; ?hasRevision ?rev . } GROUP BY ?fileNo }" +
                                "} ORDER BY DESC(?startTime) DESC(?revision) LIMIT ?limit OFFSET ?offset",
                        cz.cvut.kbss.reporting.model.reportlist.OccurrenceReport.class)
                .setParameter("type", typeUri)
                .setParameter("hasRevision", URI.create(Vocabulary.s_p_has_revision))
                .setParameter("hasFileNumber", URI.create(Vocabulary.s_p_has_file_number))
                .setParameter("hasOccurrence", URI.create(Vocabulary.s_p_documents))
                .setParameter("hasStartTime", URI.create(Vocabulary.s_p_has_start_time))
                .setUntypedParameter("limit", pageSpec.getPageSize())
                .setUntypedParameter("offset", pageSpec.getPageSize() * pageSpec.getPageNumber())
                .getResultList();
        return new PageImpl<>(
                res.stream().map(cz.cvut.kbss.reporting.model.reportlist.OccurrenceReport::toOccurrenceReport)
                   .collect(Collectors.toList()), pageSpec, 0L);
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
        new OrphanRemover(em).removeOrphans(original.getCorrectiveMeasures(), merged.getCorrectiveMeasures());
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
                     .setParameter("type", typeIri)
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
