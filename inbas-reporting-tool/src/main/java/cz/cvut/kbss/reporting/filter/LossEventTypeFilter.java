package cz.cvut.kbss.reporting.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents report filtering by loss event.
 */
public class LossEventTypeFilter extends ReportFilter {


    public static final String KEY = "lossEventType";

    private final List<String> values;

    LossEventTypeFilter(List<String> values) {
        assert values != null;
        this.values = new ArrayList<>(values);
        this.values.forEach(ReportFilter::validateUri);
    }

    @Override
    public String toQueryString() {
        // Note: This relies on variable naming in the corresponding query in OccurrenceReportDao
        if (values.size() == 1) {
            return "?lossEventType = <" + values.get(0) + ">";
        }
        final List<String> uris = values.stream().map(v -> "<" + v + ">").collect(Collectors.toList());
        return "?lossEventType IN (" + String.join(",", uris) + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LossEventTypeFilter)) return false;

        LossEventTypeFilter that = (LossEventTypeFilter) o;

        return values.equals(that.values);
    }

    @Override
    public int hashCode() {
        return values.hashCode();
    }
}
