package cz.cvut.kbss.inbas.audit.persistence.dao;

import java.net.URI;
import java.util.List;

/**
 * Base interface for data access objects.
 */
public interface GenericDao<T> {

    /**
     * Finds entity instance with the specified identifier.
     *
     * @param type Return type class
     * @param uri  Identifier
     * @return Entity instance or {@code null} if no such instance exists
     */
    public T findByUri(Class<T> type, URI uri);

    /**
     * Finds all instances of the specified class.
     *
     * @param cls Type to search
     * @return List of instances, possibly empty
     */
    public List<T> findAll(Class<T> cls);

    /**
     * Persists the specified entity.
     *
     * @param entity Entity to persist
     */
    public void persist(T entity);

    /**
     * Updates the specified entity.
     *
     * @param entity Entity to update
     */
    public void update(T entity);

    /**
     * Removes the specified entity.
     *
     * @param entity Entity to remove
     */
    public void remove(T entity);
}
