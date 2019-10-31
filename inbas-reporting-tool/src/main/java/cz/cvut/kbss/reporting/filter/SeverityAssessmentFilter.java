package cz.cvut.kbss.reporting.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents report filtering by severity assessment (occurrence class in 376).
 */
public class SeverityAssessmentFilter extends ReportFilter {

    public static final String KEY = "severityAssessment";

    private final List<String> values;

    SeverityAssessmentFilter(List<String> values) {
        assert values != null;
        this.values = new ArrayList<>(values);
        values.forEach(ReportFilter::validateUri);
    }

    @Override
    public String toQueryString() {
        // Note: This relies on variable naming in the corresponding query in OccurrenceReportDao
        if (values.size() == 1) {
            return "?severity = <" + values.get(0) + ">";
        }
        final List<String> uris = values.stream().map(v -> "<" + v + ">").collect(Collectors.toList());
        return "?severity IN (" + String.join(",", uris) + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SeverityAssessmentFilter)) return false;

        SeverityAssessmentFilter that = (SeverityAssessmentFilter) o;

        return values.equals(that.values);
    }

    @Override
    public int hashCode() {
        return values.hashCode();
    }
}
