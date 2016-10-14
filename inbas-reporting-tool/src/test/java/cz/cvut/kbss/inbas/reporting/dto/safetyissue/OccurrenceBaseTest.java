package cz.cvut.kbss.inbas.reporting.dto.safetyissue;

import cz.cvut.kbss.inbas.reporting.environment.generator.Generator;
import cz.cvut.kbss.inbas.reporting.environment.generator.OccurrenceReportGenerator;
import cz.cvut.kbss.inbas.reporting.model.Occurrence;
import cz.cvut.kbss.inbas.reporting.model.Vocabulary;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OccurrenceBaseTest {

    @Test
    public void constructorFromOccurrenceAddsOccurrenceToTypes() {
        final Occurrence occurrence = OccurrenceReportGenerator.generateOccurrence();
        occurrence.setUri(Generator.generateUri());

        final OccurrenceBase base = new OccurrenceBase(occurrence);
        assertEquals(occurrence.getUri(), base.getUri());
        assertEquals(occurrence.getName(), base.getName());
        assertTrue(base.getTypes().containsAll(occurrence.getTypes()));
        assertTrue(base.getTypes().contains(Vocabulary.s_c_Occurrence));
    }
}
