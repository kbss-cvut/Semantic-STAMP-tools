package cz.cvut.kbss.inbas.audit.persistence.dao;

import cz.cvut.kbss.inbas.audit.model.HasDerivableUri;
import cz.cvut.kbss.inbas.audit.model.HasOwlKey;
import cz.cvut.kbss.inbas.audit.persistence.PersistenceException;
import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import cz.cvut.kbss.jopa.exceptions.NoResultException;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Base implementation of the generic DAO.
 */
public abstract class BaseDao<T> implements GenericDao<T>, SupportsOwlKey<T> {

    protected static final Logger LOG = LoggerFactory.getLogger(BaseDao.class);

    private final Class<T> type;
    private final URI typeUri;

    protected BaseDao(Class<T> type) {
        this.type = type;
        final OWLClass owlClass = type.getDeclaredAnnotation(OWLClass.class);
        if (owlClass == null) {
            throw new IllegalArgumentException("Class " + type + " is not an entity.");
        }
        this.typeUri = URI.create(owlClass.iri());
    }

    @Autowired
    private EntityManagerFactory emf;

    @Override
    public T find(URI uri) {
        Objects.requireNonNull(uri);
        final EntityManager em = entityManager();
        try {
            return findByUri(uri, em);
        } finally {
            em.close();
        }
    }

    protected T findByUri(URI uri, EntityManager em) {
        return em.find(type, uri);
    }

    @Override
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

    @Override
    public List<T> findAll() {
        final EntityManager em = entityManager();
        try {
            return findAll(em);
        } finally {
            em.close();
        }
    }

    protected List<T> findAll(EntityManager em) {
        final String query = "SELECT ?x WHERE { ?x a ?type .}";
        final OWLClass owlClass = type.getDeclaredAnnotation(OWLClass.class);
        if (owlClass == null) {
            throw new IllegalArgumentException("Class " + type + " is not an entity.");
        }
        return em.createNativeQuery(query, type).setParameter("type", URI.create(owlClass.iri())).getResultList();
    }

    @Override
    public void persist(T entity) {
        Objects.requireNonNull(entity);
        final EntityManager em = entityManager();
        try {
            em.getTransaction().begin();
            persist(entity, em);
            em.getTransaction().commit();
        } catch (Exception e) {
            LOG.error("Error when persisting entity.", e);
            throw new PersistenceException(e);
        } finally {
            em.close();
        }
    }

    protected void persist(T entity, EntityManager em) {
        if (entity instanceof HasOwlKey) {
            ((HasOwlKey) entity).generateKey();
        }
        if (entity instanceof HasDerivableUri) {
            ((HasDerivableUri) entity).generateUri();
        }
        em.persist(entity);
    }

    @Override
    public void persist(Collection<T> entities) {
        Objects.requireNonNull(entities);
        if (entities.isEmpty()) {
            return;
        }
        final EntityManager em = entityManager();
        try {
            em.getTransaction().begin();
            entities.forEach(em::persist);
            em.getTransaction().commit();
        } catch (Exception e) {
            LOG.error("Error when persisting entities.", e);
            throw new PersistenceException(e);
        } finally {
            em.close();
        }
    }

    @Override
    public void update(T entity) {
        Objects.requireNonNull(entity);
        final EntityManager em = entityManager();
        try {
            em.getTransaction().begin();
            update(entity, em);
            em.getTransaction().commit();
        } catch (Exception e) {
            LOG.error("Error when updating entity.", e);
            throw new PersistenceException(e);
        } finally {
            em.close();
        }
    }

    protected void update(T entity, EntityManager em) {
        em.merge(entity);
    }

    @Override
    public void remove(T entity) {
        Objects.requireNonNull(entity);
        final EntityManager em = entityManager();
        try {
            em.getTransaction().begin();
            remove(entity, em);
            em.getTransaction().commit();
        } catch (Exception e) {
            LOG.error("Error when removing entity.", e);
            throw new PersistenceException(e);
        } finally {
            em.close();
        }
    }

    protected void remove(T entity, EntityManager em) {
        final T toRemove = em.merge(entity);
        assert toRemove != null;
        em.remove(toRemove);
    }

    @Override
    public void remove(Collection<T> entities) {
        Objects.requireNonNull(entities);
        if (entities.isEmpty()) {
            return;
        }
        final EntityManager em = entityManager();
        try {
            em.getTransaction().begin();
            entities.forEach(entity -> {
                final T toRemove = em.merge(entity);
                em.remove(toRemove);
            });
            em.getTransaction().commit();
        } catch (Exception e) {
            LOG.error("Error when removing entities.", e);
            throw new PersistenceException(e);
        } finally {
            em.close();
        }
    }

    @Override
    public boolean exists(URI uri) {
        if (uri == null) {
            return false;
        }
        final EntityManager em = entityManager();
        try {
            return exists(uri, em);
        } finally {
            em.close();
        }
    }

    protected boolean exists(URI uri, EntityManager em) {
        if (uri == null) {
            return false;
        }
        final String owlClass = type.getDeclaredAnnotation(OWLClass.class).iri();
        return em.createNativeQuery("ASK { ?individual a ?type . }", Boolean.class).setParameter("individual", uri)
                 .setParameter("type", URI.create(owlClass)).getSingleResult();
    }

    protected EntityManager entityManager() {
        return emf.createEntityManager();
    }
}
