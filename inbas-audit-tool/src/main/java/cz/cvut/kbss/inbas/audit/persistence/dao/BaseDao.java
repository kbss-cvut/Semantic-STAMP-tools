package cz.cvut.kbss.inbas.audit.persistence.dao;

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
import org.springframework.stereotype.Repository;

import java.net.URI;
import java.util.List;
import java.util.Objects;

/**
 * Base implementation of the generic DAO.
 */
@Repository
public class BaseDao<T> implements GenericDao<T>, SupportsOwlKey<T> {

    protected static final Logger LOG = LoggerFactory.getLogger(BaseDao.class);

    public BaseDao() {
    }

    @Autowired
    private EntityManagerFactory emf;

    @Override
    public T findByUri(Class<T> type, URI uri) {
        Objects.requireNonNull(type, ErrorUtils.createNPXMessage("type"));
        Objects.requireNonNull(uri, ErrorUtils.createNPXMessage("uri"));
        final EntityManager em = entityManager();
        try {
            return em.find(type, uri);
        } finally {
            em.close();
        }
    }

    @Override
    public T findByKey(Class<T> type, String key) {
        Objects.requireNonNull(type, ErrorUtils.createNPXMessage("type"));
        Objects.requireNonNull(key, ErrorUtils.createNPXMessage("key"));
        final EntityManager em = entityManager();
        try {
            return em.createNativeQuery("SELECT ?x WHERE { ?x <" + Vocabulary.p_hasKey + "> \"" + key + "\". }",
                    type).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<T> findAll(Class<T> cls) {
        Objects.requireNonNull(cls, ErrorUtils.createNPXMessage("cls"));
        final String query = "SELECT ?x WHERE { ?x a <$type$> .}";
        final OWLClass owlClass = cls.getDeclaredAnnotation(OWLClass.class);
        if (owlClass == null) {
            throw new IllegalArgumentException("Class " + cls + " is not an entity.");
        }
        final EntityManager em = entityManager();
        try {
            return em.createNativeQuery(query.replace("$type$", owlClass.iri()), cls).getResultList();
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

    }

    protected EntityManager entityManager() {
        return emf.createEntityManager();
    }
}
