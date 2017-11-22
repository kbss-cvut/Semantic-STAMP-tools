package cz.cvut.kbss.reporting.persistence.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Adds support for paging to data access objects.
 *
 * @param <T> Type managed by this DAO
 */
public interface PagingDao<T> extends GenericDao<T> {

    /**
     * Finds all instances of the specified class on the specified page.
     * <p>
     * The page specification is expected to contain page number and size.
     *
     * @param pageSpec Specification of the page to return
     * @return List of instances, possibly empty
     */
    Page<T> findAll(Pageable pageSpec);
}
