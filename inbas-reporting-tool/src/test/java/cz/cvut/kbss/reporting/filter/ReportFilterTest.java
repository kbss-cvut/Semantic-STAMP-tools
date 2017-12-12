package cz.cvut.kbss.reporting.filter;

import cz.cvut.kbss.reporting.environment.generator.Generator;
import org.junit.Test;

import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ReportFilterTest {

    @Test
    public void createCreatesOccurrenceCategoryFilterForOccurrenceCategoryKey() {
        final String value = Generator.generateUri().toString();
        final Optional<ReportFilter> filter = ReportFilter
                .create(OccurrenceCategoryFilter.KEY, Collections.singletonList(value));
        assertTrue(filter.isPresent());
        assertTrue(filter.get() instanceof OccurrenceCategoryFilter);
    }

    @Test
    public void createReturnsEmptyOptionalForUnknownKey() {
        assertFalse(ReportFilter.create("unknown", Collections.singletonList("anything")).isPresent());
    }

    @Test
    public void createReturnsSeverityAssessmentFilterForSeverityAssessmentKey() {
        final String value = Generator.generateUri().toString();
        final Optional<ReportFilter> filter = ReportFilter
                .create(SeverityAssessmentFilter.KEY, Collections.singletonList(value));
        assertTrue(filter.isPresent());
        assertTrue(filter.get() instanceof SeverityAssessmentFilter);
    }

    @Test
    public void createReturnsReportKeyFilterForKey() {
        final String value = Generator.generateUri().toString();
        final Optional<ReportFilter> filter = ReportFilter
                .create(ReportKeyFilter.KEY, Collections.singletonList(value));
        assertTrue(filter.isPresent());
        assertTrue(filter.get() instanceof ReportKeyFilter);
    }

    @Test
    public void createReturnsPersonFilterForMineKey() {
        final Optional<ReportFilter> filter = ReportFilter
                .create(PersonFilter.KEY, Collections.singletonList(Boolean.TRUE.toString()));
        assertTrue(filter.isPresent());
        assertTrue(filter.get() instanceof PersonFilter);
    }
}