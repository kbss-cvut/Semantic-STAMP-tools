package cz.cvut.kbss.reporting.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Adds support for paging to services.
 *
 * @param <T> Type managed by this service
 */
public interface PagingService<T> extends BaseService<T> {

    /**
     * Gets all instances of the type managed by this service corresponding to the specified page.
     *
     * @param pageSpec Page specification (number, size)
     * @return Page of matching instances
     */
    Page<T> findAll(Pageable pageSpec);
}
