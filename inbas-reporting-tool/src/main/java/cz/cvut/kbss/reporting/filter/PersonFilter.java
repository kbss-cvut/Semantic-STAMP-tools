package cz.cvut.kbss.reporting.filter;

import cz.cvut.kbss.reporting.service.security.SecurityUtils;

import java.util.List;
import java.util.Optional;

/**
 * Represents filtering by author and last editor.
 * <p>
 * Matching reports must have been created or last edited by the currently logged in user.
 */
public class PersonFilter extends ReportFilter {

    /**
     * This filter is boolean, so the value is expected to be true/false.
     */
    public static final String KEY = "mine";

    PersonFilter() {
    }

    @Override
    public String toQueryString() {
        return String.format("?author = <%1$s> || ?lastEditor = <%1$s>", SecurityUtils.currentUser().getUri());
    }

    /**
     * Creates the filter if the specified values contain {@code true}.
     *
     * @param values The values to check
     * @return Filter wrapped in an {@code Optional} or an empty {@code Optional}
     */
    static Optional<PersonFilter> create(List<String> values) {
        Optional<String> contains = values.stream().filter(Boolean::parseBoolean).findAny();
        return contains.map(v -> new PersonFilter());
    }
}
