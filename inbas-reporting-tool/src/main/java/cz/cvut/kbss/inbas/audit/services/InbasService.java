package cz.cvut.kbss.inbas.audit.services;

import java.net.URI;
import java.util.Collection;

/**
 * @author ledvima1
 */
public interface InbasService<T> {

    /**
     * Returns all instances of the class.
     *
     * @return Collection of instances
     */
    Collection<T> findAll();

    /**
     * Finds instance by its URI identifier.
     *
     * @param uri Identifier
     * @return Instance or {@code null} if none matches the identifier
     */
    T find(URI uri);

    /**
     * Persists the specified instance.
     *
     * @param instance Instance to persist
     */
    void persist(T instance);

    /**
     * Merges the state of the specified instance into the storage.
     *
     * @param instance The updated instance
     */
    void update(T instance);

    /**
     * Deletes the specified instance from the storage.
     *
     * @param instance Instance to remove
     */
    void remove(T instance);
}
