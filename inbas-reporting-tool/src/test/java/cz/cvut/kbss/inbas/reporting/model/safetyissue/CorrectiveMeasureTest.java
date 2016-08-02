package cz.cvut.kbss.inbas.reporting.model.safetyissue;

import cz.cvut.kbss.inbas.reporting.environment.generator.Generator;
import org.junit.Test;

import java.util.Collections;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CorrectiveMeasureTest {

    @Test
    public void copyConstructorCopiesResponsibleAgents() {
        final Date deadline = new Date();
        final CorrectiveMeasure m = new CorrectiveMeasure();
        m.setDeadline(deadline);
        m.setDescription("Corrective measure" + Generator.randomInt());
        m.setResponsiblePersons(Collections.singleton(Generator.getPerson()));
        m.setResponsibleOrganizations(Collections.singleton(Generator.generateOrganization()));
        m.setPhase(Generator.generateUri());

        final CorrectiveMeasure result = new CorrectiveMeasure(m);
        assertNull(result.getUri());
        assertEquals(m.getDeadline(), result.getDeadline());
        assertEquals(m.getDescription(), result.getDescription());
        assertEquals(m.getPhase(), result.getPhase());
        assertEquals(m.getResponsiblePersons(), result.getResponsiblePersons());
        assertEquals(m.getResponsibleOrganizations(), result.getResponsibleOrganizations());
    }
}
