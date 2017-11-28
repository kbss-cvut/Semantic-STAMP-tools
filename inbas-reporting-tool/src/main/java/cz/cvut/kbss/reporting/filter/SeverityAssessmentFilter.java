package cz.cvut.kbss.reporting.filter;

import cz.cvut.kbss.reporting.exception.ValidationException;
import cz.cvut.kbss.reporting.model.Vocabulary;

import java.net.URI;

/**
 * Represents report filtering by severity assessment (occurrence class in 376).
 */
class SeverityAssessmentFilter extends ReportFilter {

    static final String KEY = "severityAssessment";

    private final URI value;

    SeverityAssessmentFilter(String value) {
        assert value != null;
        try {
            this.value = URI.create(value);
        } catch (IllegalArgumentException e) {
            throw new ValidationException("\"" + value + "\" is not a valid occurrence class URI.", e);
        }
    }

    @Override
    public String toQueryString() {
        return String.format("?x <%s> <%s> .", Vocabulary.s_p_has_severity_assessment, value);
    }
}
