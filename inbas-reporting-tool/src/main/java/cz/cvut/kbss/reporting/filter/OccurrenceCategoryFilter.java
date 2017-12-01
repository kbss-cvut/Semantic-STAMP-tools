package cz.cvut.kbss.reporting.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents report filtering by occurrence category.
 */
public class OccurrenceCategoryFilter extends ReportFilter {

    public static final String KEY = "occurrenceCategory";

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
            return "?occurrenceCategory = <" + values.get(0) + ">";
        }
        final List<String> uris = values.stream().map(v -> "<" + v + ">").collect(Collectors.toList());
        return "?occurrenceCategory IN (" + String.join(",", uris) + ")";
    }
}
