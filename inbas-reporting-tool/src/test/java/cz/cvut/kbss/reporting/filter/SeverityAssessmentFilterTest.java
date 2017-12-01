package cz.cvut.kbss.reporting.filter;

import cz.cvut.kbss.reporting.environment.generator.Generator;
import cz.cvut.kbss.reporting.exception.ValidationException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

public class SeverityAssessmentFilterTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructorRefusesNonUriValueWithValidationException() {
        final String value = "DELETE WHERE { ?x ?y ?z. }";
        thrown.expect(ValidationException.class);
        thrown.expectMessage(containsString("is not a valid URI."));
        new SeverityAssessmentFilter(Collections.singletonList(value));
    }

    @Test
    public void toQueryStringWithSingleValueProducesCorrectSparqlTriplePattern() {
        final String value = Generator.generateUri().toString();
        final ReportFilter filter = new SeverityAssessmentFilter(Collections.singletonList(value));
        assertThat(filter.toQueryString(), containsString("?severity = <" + value + ">"));
    }

    @Test
    public void toQueryStringWithMultipleValuesProducesUnion() {
        final List<String> values = IntStream.range(0, 5).mapToObj(i -> Generator.generateUri().toString()).collect(
                Collectors.toList());
        final ReportFilter filter = new SeverityAssessmentFilter(values);
        final String queryString = filter.toQueryString();
        assertThat(queryString, containsString("?severity IN ("));
        values.forEach(v -> assertThat(queryString, containsString("<" + v + ">")));
    }
}