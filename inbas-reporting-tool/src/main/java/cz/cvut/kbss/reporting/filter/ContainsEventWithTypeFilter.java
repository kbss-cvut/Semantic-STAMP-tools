package cz.cvut.kbss.reporting.filter;

import cz.cvut.kbss.reporting.model.Vocabulary;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents report filtering by a event type. The filter is graph pattern which looks for an event with a given eventType
 * in the report's occurrence.
 */
public class ContainsEventWithTypeFilter extends ReportFilter{
    public static final String KEY = "eventType";

    private final List<String> values;

    protected String pattern =
            "?occurrence <" + Vocabulary.s_p_has_part_A + ">+ ?et ." +
            "?et <" + Vocabulary.s_p_has_event_type + "> %s.";

    ContainsEventWithTypeFilter(List<String> values) {
        this.isGraphPatternFilter = true;
        assert values != null;
        this.values = new ArrayList<>(values);
        this.values.forEach(ReportFilter::validateUri);
    }



    @Override
    public String toQueryString() {
        // Note: This relies on variable naming in the corresponding query in OccurrenceReportDao
        if (values.size() == 1) {
            return String.format(pattern, "<" + values.get(0) + ">");
        }
        String queryFragment = String.format(pattern, "?eventType");
        final List<String> uris = values.stream().map(v -> "<" + v + ">").collect(Collectors.toList());

        return queryFragment + "?eventType IN (" + String.join(",", uris) + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContainsEventWithTypeFilter)) return false;

        ContainsEventWithTypeFilter that = (ContainsEventWithTypeFilter) o;

        return values.equals(that.values);
    }

    @Override
    public int hashCode() {
        return values.hashCode();
    }
}
