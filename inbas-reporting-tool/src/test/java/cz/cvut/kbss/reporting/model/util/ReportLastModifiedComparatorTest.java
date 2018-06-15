package cz.cvut.kbss.reporting.model.util;

import cz.cvut.kbss.reporting.model.LogicalDocument;
import cz.cvut.kbss.reporting.model.OccurrenceReport;
import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;
import java.util.Date;

import static org.hamcrest.number.OrderingComparison.lessThan;
import static org.junit.Assert.assertThat;

public class ReportLastModifiedComparatorTest {

    private final Comparator<LogicalDocument> comparator = new ReportLastModifiedComparator();

    private OccurrenceReport rOne;
    private OccurrenceReport rTwo;

    @Before
    public void setUp() {
        this.rOne = new OccurrenceReport();
        this.rTwo = new OccurrenceReport();
    }

    @Test
    public void compareWorksForInstancesWithNullLastModifiedDate() {
        rOne.setDateCreated(new Date());
        rTwo.setDateCreated(new Date(System.currentTimeMillis() - 10000));
        assertThat(comparator.compare(rOne, rTwo), lessThan(0));
    }

    @Test
    public void compareComparesDateCreatedWithLastModifiedWhenLastModifiedIsNullForFirstArgument() {
        rOne.setDateCreated(new Date());
        rTwo.setDateCreated(new Date(System.currentTimeMillis() - 10000));
        rTwo.setLastModified(new Date(System.currentTimeMillis() - 5000));
        assertThat(comparator.compare(rOne, rTwo), lessThan(0));
    }

    @Test
    public void compareComparesDateCreatedWithLastModifiedWhenLastModifiedIsNullForSecondArgument() {
        rOne.setDateCreated(new Date());
        rTwo.setDateCreated(new Date(System.currentTimeMillis() - 10000));
        rOne.setLastModified(new Date(System.currentTimeMillis() - 5000));
        assertThat(comparator.compare(rOne, rTwo), lessThan(0));
    }

    @Test
    public void compareComparesLastModifiedDatesForBothInstancesByDefault() {
        rOne.setDateCreated(new Date());
        rTwo.setDateCreated(new Date(System.currentTimeMillis() - 10000));
        rOne.setLastModified(new Date(System.currentTimeMillis() - 5000));
        rTwo.setLastModified(new Date(System.currentTimeMillis() - 8000));
        assertThat(comparator.compare(rOne, rTwo), lessThan(0));
    }
}