package cz.cvut.kbss.reporting.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents report filtering by occurrence category.
 */
class OccurrenceCategoryFilter extends ReportFilter {

    static final String KEY = "occurrenceCategory";

    private final List<String> values;

    OccurrenceCategoryFilter(List<String> values) {
        assert values != null;
        this.values = new ArrayList<>(values);
        this.values.forEach(ReportFilter::validateUri);
    }

    @Override
    public String toQueryString() {
        // Note: This relies on variable naming in the corresponding query in OccurrenceReportDao
        if (values.size() == 1) {
            return "(?occurrenceType = <" + values.get(0) + ">)";
        }
        final List<String> uris = values.stream().map(v -> "<" + v + ">").collect(Collectors.toList());
        return "(?occurrenceType IN (" + String.join(",", uris) + ")";
    }
}
