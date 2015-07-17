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
     * @param uri Identifier
     * @return Entity instance or {@code null} if no such instance exists
     */
    T findByUri(URI uri);

    /**
     * Finds all instances of the specified class.
     *
     * @return List of instances, possibly empty
     */
    List<T> findAll();

    /**
     * Persists the specified entity.
     *
     * @param entity Entity to persist
     */
    void persist(T entity);

    /**
     * Updates the specified entity.
     *
     * @param entity Entity to update
     */
    void update(T entity);

    /**
     * Removes the specified entity.
     *
     * @param entity Entity to remove
     */
    void remove(T entity);

    /**
     * Checks whether an entity with the specified URI exists (and has the type managed by this DAO).
     *
     * @param uri Entity identifier
     * @return {@literal true} if entity exists, {@literal false} otherwise
     */
    boolean exists(URI uri);
}
