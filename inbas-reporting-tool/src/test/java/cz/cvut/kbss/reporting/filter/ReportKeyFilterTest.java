package cz.cvut.kbss.reporting.filter;

import cz.cvut.kbss.reporting.util.Constants;
import cz.cvut.kbss.reporting.util.IdentificationUtils;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

public class ReportKeyFilterTest {

    @Test
    public void toQueryStringProducesEqualityFilterForSingleValue() {
        final String key = IdentificationUtils.generateKey();
        final ReportFilter filter = new ReportKeyFilter(Collections.singletonList(key));

        final String queryString = filter.toQueryString();
        assertThat(queryString, containsString("?key = \"" + key + "\"@" + Constants.PU_LANGUAGE));
    }

    @Test
    public void toQueryStringProducesFilterInForMultipleValues() {
        final List<String> keys = IntStream.range(0, 5).mapToObj(i -> IdentificationUtils.generateKey())
                                           .collect(Collectors.toList());
        final ReportFilter filter = new ReportKeyFilter(keys);

        final String queryString = filter.toQueryString();
        assertThat(queryString, containsString("?key IN ("));
        keys.forEach(key -> assertThat(queryString, containsString("\"" + key + "\"@" + Constants.PU_LANGUAGE)));
    }
}