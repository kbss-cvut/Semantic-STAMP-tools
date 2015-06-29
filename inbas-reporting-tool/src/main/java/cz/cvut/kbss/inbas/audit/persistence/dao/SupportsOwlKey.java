package cz.cvut.kbss.inbas.audit.persistence.dao;

/**
 * @author ledvima1
 */
public interface SupportsOwlKey<T> extends GenericDao<T> {

    /**
     * Finds entity instance by its unique key.
     *
     * @param type Return type class
     * @param key  Instance key
     * @return Entity instance or {@code null} if no such matching exists
     */
    T findByKey(Class<T> type, String key);
}
