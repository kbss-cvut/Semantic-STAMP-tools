package cz.cvut.kbss.inbas.reporting.service.repository;

import cz.cvut.kbss.inbas.reporting.environment.generator.OccurrenceReportGenerator;
import cz.cvut.kbss.inbas.reporting.model.Occurrence;
import cz.cvut.kbss.inbas.reporting.service.BaseServiceTestRunner;
import cz.cvut.kbss.inbas.reporting.service.OccurrenceService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class KeySupportingRepositoryServiceTest extends BaseServiceTestRunner {

    @Autowired
    private OccurrenceService occurrenceService;    // This one supports keys

    @Test
    public void testFindByKey() {
        final Occurrence occurrence = OccurrenceReportGenerator.generateOccurrence();
        occurrenceService.persist(occurrence);

        final Occurrence result = occurrenceService.findByKey(occurrence.getKey());
        assertNotNull(result);
        assertEquals(occurrence.getUri(), result.getUri());
    }
}