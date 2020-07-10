package cz.cvut.kbss.reporting.filter;

import cz.cvut.kbss.reporting.exception.ValidationException;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * Report filters represent a uniform way of filtering reports.
 * <p>
 * Once initialized, they can be used on DAO level and inserted into the query string used to retrieve reports.
 */
public abstract class ReportFilter {

    protected boolean isGraphPatternFilter = false;

    /**
     * If true, the filter is rendered as graph pattern instead as a condition in a filter clause.
     * @return
     */
    public boolean isGraphPatternFilter() {
        return isGraphPatternFilter;
    }

    /**
     * Translates this filter condition into a String insertable into a SPARQL query.
     *
     * @return SPARQL-compatible representation of this filter
     */
    public abstract String toQueryString();

    /**
     * Creates a {@code ReportFilter} instance for the specified key.
     * <p>
     * If there are multiple values specified, they are interpreted as a logical OR, i.e. report with any of the values
     * matches the search criteria.
     *
     * @param key    Key supplied in request, used to determine type of filter
     * @param values Values to filter by
     * @return filter instance or an empty optional if no filter exists for the specified key
     */
    public static Optional<ReportFilter> create(String key, List<String> values) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(values);
        switch (key) {
            case OccurrenceCategoryFilter.KEY:
                return Optional.of(new OccurrenceCategoryFilter(values));
            case SeverityAssessmentFilter.KEY:
                return Optional.of(new SeverityAssessmentFilter(values));
            case ReportKeyFilter.KEY:
                return Optional.of(new ReportKeyFilter(values));
            case PersonFilter.KEY:
                return PersonFilter.create(values).map(Function.identity());
            case LossEventTypeFilter.KEY:
                return Optional.of(new LossEventTypeFilter(values));
            case ContainsEventWithTypeFilter.KEY:
                return Optional.of(new ContainsEventWithTypeFilter(values));
            default:
                return Optional.empty();
        }
    }

    /**
     * Verifies that the specified value is a valid URI.
     *
     * @param value The value to validate
     */
    static void validateUri(String value) {
        try {
            URI.create(value);
        } catch (IllegalArgumentException e) {
            throw new ValidationException("\"" + value + "\" is not a valid URI.", e);
        }
    }
}
