package cz.cvut.kbss.reporting.filter;

import cz.cvut.kbss.reporting.environment.generator.Generator;
import cz.cvut.kbss.reporting.exception.ValidationException;
import cz.cvut.kbss.reporting.model.Vocabulary;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

public class SeverityAssessmentFilterTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructorRefusesNonUriValueWithValidationException() {
        final String value = "DELETE WHERE { ?x ?y ?z. }";
        thrown.expect(ValidationException.class);
        thrown.expectMessage(containsString("is not a valid occurrence class URI."));
        new SeverityAssessmentFilter(value);
    }

    @Test
    public void toQueryStringProducesCorrectSparqlTriplePattern() {
        final String value = Generator.generateUri().toString();
        final ReportFilter filter = new SeverityAssessmentFilter(value);
        assertThat(filter.toQueryString(),
                containsString("?x <" + Vocabulary.s_p_has_severity_assessment + "> <" + value + "> ."));
    }
}