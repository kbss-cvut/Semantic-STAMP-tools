package cz.cvut.kbss.reporting.filter;

import cz.cvut.kbss.reporting.exception.ValidationException;

import java.net.URI;

/**
 * Represents report filtering by occurrence category.
 */
class OccurrenceCategoryFilter extends ReportFilter {

    static final String KEY = "occurrenceCategory";

    private final URI value;

    OccurrenceCategoryFilter(String value) {
        assert value != null;
        try {
            this.value = URI.create(value);
        } catch (IllegalArgumentException e) {
            throw new ValidationException("\"" + value + "\" is not a valid occurrence category URI.", e);
        }
    }

    @Override
    public String toQueryString() {
        // Note: This relies on variable naming in the corresponding query in OccurrenceReportDao
        return String.format("?occurrence a <%s> .", value);
    }
}
