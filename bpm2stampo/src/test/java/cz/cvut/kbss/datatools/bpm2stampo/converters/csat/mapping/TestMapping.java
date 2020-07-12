package cz.cvut.kbss.datatools.bpm2stampo.converters.csat.mapping;

import cz.cvut.kbss.datatools.bpm2stampo.converters.csat.model.AbstractModelTest;
import org.junit.BeforeClass;

import java.io.IOException;

public class TestMapping extends AbstractModelTest {
    @BeforeClass
    public static void setUp() throws IOException {
        AbstractModelTest.setUp();
        bpmProcessor.injectObjectReferences(results);
    }


}
