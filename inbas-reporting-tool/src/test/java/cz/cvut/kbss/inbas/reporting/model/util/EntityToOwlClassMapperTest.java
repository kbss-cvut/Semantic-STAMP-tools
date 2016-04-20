package cz.cvut.kbss.inbas.reporting.model.util;

import cz.cvut.kbss.inbas.reporting.model.Occurrence;
import cz.cvut.kbss.inbas.reporting.model.Vocabulary;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EntityToOwlClassMapperTest {

    @Test
    public void getOWlClassForEntityExtractsOwlClassIriFromEntityClass() {
        assertEquals(Vocabulary.Occurrence, EntityToOwlClassMapper.getOwlClassForEntity(Occurrence.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getOwlClassForEntityThrowsIllegalArgumentForNonEntity() {
        EntityToOwlClassMapper.getOwlClassForEntity(Object.class);
    }
}