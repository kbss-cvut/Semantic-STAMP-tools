package cz.cvut.kbss.inbas.reporting.model.util;

import cz.cvut.kbss.inbas.reporting.model.Report;
import cz.cvut.kbss.inbas.reporting.model.Vocabulary;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EntityToOwlClassMapperTest {

    @Test
    public void getOWlClassForEntityExtractsOwlClassIriFromEntityClass() {
        assertEquals(Vocabulary.Report, EntityToOwlClassMapper.getOwlClassForEntity(Report.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getOwlClassForEntityThrowsIllegalArgumentForNonEntity() {
        EntityToOwlClassMapper.getOwlClassForEntity(Object.class);
    }
}