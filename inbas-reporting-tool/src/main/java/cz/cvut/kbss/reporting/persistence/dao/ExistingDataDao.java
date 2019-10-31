package cz.cvut.kbss.reporting.persistence.dao;

import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import cz.cvut.kbss.reporting.model.Vocabulary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

/**
 * Provides access to data used in existing reports, e.g. used occurrence categories.
 */
@Repository
public class ExistingDataDao {

    private final EntityManagerFactory emf;

    @Autowired
    public ExistingDataDao(EntityManagerFactory emf) {
        this.emf = emf;
    }

    /**
     * Retrieves occurrence categories used by occurrences known to the system (and being referenced by the latest
     * report revisions).
     *
     * @return Occurrence category URIs
     */
    public Set<URI> getUsedOccurrenceCategories() {
        final EntityManager em = emf.createEntityManager();
        try {
            return new HashSet<>(em.createNativeQuery("SELECT DISTINCT ?occurrenceCategory WHERE {" +
                    "?x a ?type ; " +
                    "   ?documents ?occurrence . " +
                    "?occurrence ?hasCategory ?occurrenceCategory . " +
                    "FILTER NOT EXISTS { " +
                    "?y a ?type . " +
                    "?x ?hasNext ?y . }" +
                    "}", URI.class).setParameter("type", URI.create(Vocabulary.s_c_occurrence_report))
                                   .setParameter("documents", URI.create(Vocabulary.s_p_documents))
                                   .setParameter("hasCategory", URI.create(Vocabulary.s_p_has_event_type))
                                   .setParameter("hasNext", URI.create(Vocabulary.s_p_has_next_revision))
                                   .getResultList());
        } finally {
            em.close();
        }
    }

    /**
     * Retrieves event types used by events known to the system (and being referenced by the latest report revisions).
     *
     * @return Event type URIs
     */
    public Set<URI> getUsedEventTypes() {
        final EntityManager em = emf.createEntityManager();
        try {
            return new HashSet<>(em.createNativeQuery("SELECT DISTINCT ?eventType WHERE {" +
                    "?x a ?type ; " +
                    "   ?documents ?occurrence . " +
                    "?occurrence ?hasPart+ ?et . " +
                    "?et ?hasEventType ?eventType . " +
                    "FILTER NOT EXISTS { " +
                    " ?y a ?type . " +
                    " ?x ?hasNext ?y . }" +
                    "}", URI.class).setParameter("type", URI.create(Vocabulary.s_c_occurrence_report))
                                   .setParameter("documents", URI.create(Vocabulary.s_p_documents))
                                   .setParameter("hasPart", URI.create(Vocabulary.s_p_has_part_A))
                                   .setParameter("hasEventType", URI.create(Vocabulary.s_p_has_event_type))
                                   .setParameter("hasNext", URI.create(Vocabulary.s_p_has_next_revision))
                                   .getResultList());
        } finally {
            em.close();
        }
    }
}
