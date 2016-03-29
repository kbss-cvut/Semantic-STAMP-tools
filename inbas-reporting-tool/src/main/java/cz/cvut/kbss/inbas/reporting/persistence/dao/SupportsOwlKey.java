package cz.cvut.kbss.inbas.reporting.persistence.dao;

import cz.cvut.kbss.inbas.reporting.model_new.util.HasOwlKey;
import cz.cvut.kbss.inbas.reporting.util.Vocabulary;
import cz.cvut.kbss.jopa.exceptions.NoResultException;
import cz.cvut.kbss.jopa.model.EntityManager;

import java.util.Objects;

/**
 * DAO for entity classes which have an OWL key.
 *
 * @param <T> Entity type
 */
public abstract class SupportsOwlKey<T extends HasOwlKey> extends BaseDao<T> {

    protected SupportsOwlKey(Class<T> type) {
        super(type);
    }

    /**
     * Finds entity instance by its unique key.
     *
     * @param key Instance key
     * @return Entity instance or {@code null} if no such matching exists
     */
    public T findByKey(String key) {
        Objects.requireNonNull(key);
        final EntityManager em = entityManager();
        try {
            return findByKey(key, em);
        } finally {
            em.close();
        }
    }

    protected T findByKey(String key, EntityManager em) {
        try {
            return em.createNativeQuery("SELECT ?x WHERE { ?x <" + Vocabulary.p_hasKey + "> ?key ;" +
                    "a ?type }", type)
                     .setParameter("key", key, "en").setParameter("type", typeUri).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
