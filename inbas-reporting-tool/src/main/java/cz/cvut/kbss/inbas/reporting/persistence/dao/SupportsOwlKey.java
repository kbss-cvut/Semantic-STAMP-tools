package cz.cvut.kbss.inbas.reporting.persistence.dao;

/**
 * @author ledvima1
 */
public interface SupportsOwlKey<T> extends GenericDao<T> {

    /**
     * Finds entity instance by its unique key.
     *
     * @param key  Instance key
     * @return Entity instance or {@code null} if no such matching exists
     */
    T findByKey(String key);
}
