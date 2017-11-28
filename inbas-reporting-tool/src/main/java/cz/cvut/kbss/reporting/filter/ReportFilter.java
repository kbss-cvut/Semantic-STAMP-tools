package cz.cvut.kbss.reporting.filter;

import java.util.Objects;
import java.util.Optional;

/**
 * Report filters represent a uniform way of filtering reports.
 * <p>
 * Once initialized, they can be used on DAO level and inserted into the query string used to retrieve reports.
 */
public abstract class ReportFilter {

    /**
     * Translates this filter condition into a String insertable into a SPARQL query.
     *
     * @return SPARQL-compatible representation of this filter
     */
    public abstract String toQueryString();

    /**
     * Creates a {@code ReportFilter} instance for the specified key.
     *
     * @param key   Key supplied in request, used to determine type of filter
     * @param value Value to filter by
     * @return filter instance or an empty optional if no filter exists for the specified key
     */
    public static Optional<ReportFilter> create(String key, String value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
        switch (key) {
            case OccurrenceCategoryFilter.KEY:
                return Optional.of(new OccurrenceCategoryFilter(value));
            case SeverityAssessmentFilter.KEY:
                return Optional.of(new SeverityAssessmentFilter(value));
            default:
                return Optional.empty();
        }
    }
}
