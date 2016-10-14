package cz.cvut.kbss.inbas.reporting.service.repository;

import java.net.URI;

/**
 * Simple service for entities which do not have their own full-blown services.
 */
public interface GenericEntityService {

    /**
     * Loads instance of the target class with the specified identifier.
     *
     * @param resultClass Target class
     * @param uri         Instance identifier
     * @return Matching instance or {@code null}
     */
    <T> T find(Class<T> resultClass, URI uri);
}
