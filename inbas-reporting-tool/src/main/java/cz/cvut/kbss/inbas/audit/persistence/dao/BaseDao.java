package cz.cvut.kbss.inbas.audit.persistence.dao;

import cz.cvut.kbss.inbas.audit.model.HasDerivableUri;
import cz.cvut.kbss.inbas.audit.model.HasOwlKey;
import cz.cvut.kbss.inbas.audit.persistence.PersistenceException;
import cz.cvut.kbss.inbas.audit.util.ErrorUtils;
import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import cz.cvut.kbss.jopa.exceptions.NoResultException;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.util.List;
import java.util.Objects;

/**
 * Base implementation of the generic DAO.
 */
public abstract class BaseDao<T> implements GenericDao<T>, SupportsOwlKey<T> {

    protected static final Logger LOG = LoggerFactory.getLogger(BaseDao.class);

    private final Class<T> type;

    protected BaseDao(Class<T> type) {
        this.type = type;
    }

    @Autowired
    private EntityManagerFactory emf;

    @Override
    public T findByUri(URI uri) {
        Objects.requireNonNull(uri, ErrorUtils.createNPXMessage("uri"));
        final EntityManager em = entityManager();
        try {
            return em.find(type, uri);
        } finally {
            em.close();
        }
    }

    @Override
    public T findByKey(String key) {
        Objects.requireNonNull(key, ErrorUtils.createNPXMessage("key"));
        final EntityManager em = entityManager();
        try {
            return em.createNativeQuery(
                    "SELECT ?x WHERE { ?x <" + Vocabulary.p_hasKey + "> \"" + key + "\"@en . }",
                    type).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<T> findAll() {
        final String query = "SELECT ?x WHERE { ?x a <$type$> .}";
        final OWLClass owlClass = type.getDeclaredAnnotation(OWLClass.class);
        if (owlClass == null) {
            throw new IllegalArgumentException("Class " + type + " is not an entity.");
        }
        final EntityManager em = entityManager();
        try {
            return em.createNativeQuery(query.replace("$type$", owlClass.iri()), type).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void persist(T entity) {
        Objects.requireNonNull(entity, ErrorUtils.createNPXMessage("entity"));
        final EntityManager em = entityManager();
        try {
            em.getTransaction().begin();
            if (entity instanceof HasOwlKey) {
                ((HasOwlKey) entity).generateKey();
            }
            if (entity instanceof HasDerivableUri) {
                ((HasDerivableUri) entity).generateUri();
            }
            em.persist(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            LOG.error("Error when persisting entity.", e);
            throw new PersistenceException(e);
        } finally {
            em.close();
        }
    }

    @Override
    public void update(T entity) {
        Objects.requireNonNull(entity, ErrorUtils.createNPXMessage("entity"));
        final EntityManager em = entityManager();
        try {
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            LOG.error("Error when updating entity.", e);
            throw new PersistenceException(e);
        } finally {
            em.close();
        }
    }

    @Override
    public void remove(T entity) {
        Objects.requireNonNull(entity, ErrorUtils.createNPXMessage("entity"));
        final EntityManager em = entityManager();
        try {
            em.getTransaction().begin();
            final T toRemove = em.merge(entity);
            assert toRemove != null;
            em.remove(toRemove);
            em.getTransaction().commit();
        } catch (Exception e) {
            LOG.error("Error when removing entity.", e);
            throw new PersistenceException(e);
        } finally {
            em.close();
        }
    }

    @Override
    public boolean exists(URI uri) {
        final String owlClass = type.getDeclaredAnnotation(OWLClass.class).iri();
        final EntityManager em = entityManager();
        try {
            // TODO Ask queries don't work
            final List<List<String>> result = em
                    .createNativeQuery("ASK { <" + uri.toString() + "> a <" + owlClass + "> . }").getResultList();
            final String exists = result.get(0).get(0);
            return exists.equals("yes");
        } finally {
            em.close();
        }
    }

    protected EntityManager entityManager() {
        return emf.createEntityManager();
    }
}
