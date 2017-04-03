package cz.cvut.kbss.reporting.service.factory;

import cz.cvut.kbss.reporting.environment.generator.OccurrenceReportGenerator;
import cz.cvut.kbss.reporting.model.InitialReport;
import cz.cvut.kbss.reporting.model.OccurrenceReport;
import cz.cvut.kbss.reporting.service.BaseServiceTestRunner;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class DefaultOccurrenceReportFactoryTest extends BaseServiceTestRunner {

    @Autowired
    private OccurrenceReportFactory reportFactory;

    @Test
    public void createFromInitialCreatesNewReportFromSpecifiedInitialReport() {
        final long startTime = System.currentTimeMillis();
        final InitialReport initialReport = OccurrenceReportGenerator.generateInitialReport();
        final OccurrenceReport result = reportFactory.createFromInitialReport(initialReport);
        assertNotNull(result);
        assertEquals(initialReport, result.getInitialReport());
        assertNotNull(result.getOccurrence());
        assertNotNull(result.getOccurrence().getStartTime());
        assertNotNull(result.getOccurrence().getEndTime());
        assertEquals(result.getOccurrence().getStartTime(), result.getOccurrence().getEndTime());
        assertTrue(startTime <= result.getOccurrence().getStartTime().getTime());
        assertTrue(result.getOccurrence().getStartTime().getTime() <= System.currentTimeMillis());
    }
}