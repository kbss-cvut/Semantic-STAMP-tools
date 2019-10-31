package cz.cvut.kbss.reporting.filter;

import cz.cvut.kbss.reporting.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Allows to filter reports by their key.
 */
public class ReportKeyFilter extends ReportFilter {

    public static final String KEY = "key";

    private final List<String> values;

    ReportKeyFilter(List<String> values) {
        assert values != null;
        this.values = new ArrayList<>(values);
    }

    @Override
    public String toQueryString() {
        if (values.size() == 1) {
            return "?key = \"" + values.get(0) + "\"@" + Constants.PU_LANGUAGE;
        }
        return "?key IN (" + String.join(",",
                values.stream().map(v -> "\"" + v + "\"@" + Constants.PU_LANGUAGE).collect(Collectors.toList())) + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReportKeyFilter)) return false;

        ReportKeyFilter that = (ReportKeyFilter) o;

        return values.equals(that.values);
    }

    @Override
    public int hashCode() {
        return values.hashCode();
    }
}
