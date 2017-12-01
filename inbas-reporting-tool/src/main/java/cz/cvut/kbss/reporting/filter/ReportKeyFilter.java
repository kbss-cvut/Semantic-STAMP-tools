package cz.cvut.kbss.reporting.filter;

import java.util.List;

/**
 * Allows to filter reports by their key.
 */
public class ReportKeyFilter extends ReportFilter {

    public static final String KEY = "key";

    public ReportKeyFilter(List<String> values) {
    }

    @Override
    public String toQueryString() {
        // TODO
        return null;
    }
}
