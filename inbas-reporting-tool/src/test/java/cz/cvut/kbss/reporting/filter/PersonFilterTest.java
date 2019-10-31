package cz.cvut.kbss.reporting.filter;

import cz.cvut.kbss.reporting.environment.generator.Generator;
import cz.cvut.kbss.reporting.environment.util.Environment;
import cz.cvut.kbss.reporting.model.Person;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

public class PersonFilterTest {

    @Test
    public void createReturnsFilterWhenValuesContainTrue() {
        final List<String> values = Arrays.asList(Boolean.TRUE.toString(), "yes", "1");
        final Optional<PersonFilter> result = PersonFilter.create(values);
        assertTrue(result.isPresent());
    }

    @Test
    public void createReturnsEmptyOptionalWhenValuesDoNotContainTrue() {
        final List<String> values = Arrays.asList(Boolean.FALSE.toString(), "no", "0");
        final Optional<PersonFilter> result = PersonFilter.create(values);
        assertFalse(result.isPresent());
    }

    @Test
    public void toQueryStringReturnsOrWithAuthorOrEditorEqualToCurrentUser() {
        final Person currentUser = Generator.getPerson();
        currentUser.generateUri();
        Environment.setCurrentUser(currentUser);
        final PersonFilter filter = new PersonFilter();
        final String queryString = filter.toQueryString();
        assertThat(queryString, containsString(
                String.format("?author = <%1$s> || ?lastEditor = <%1$s>", currentUser.getUri().toString())));
    }
}