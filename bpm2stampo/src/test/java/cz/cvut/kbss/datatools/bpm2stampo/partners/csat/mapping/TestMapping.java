package cz.cvut.kbss.datatools.bpm2stampo.partners.csat.mapping;

import cz.cvut.kbss.datatools.bpm2stampo.partners.csat.model.AbstractModelTest;
import org.junit.BeforeClass;

import java.io.IOException;

public class TestMapping extends AbstractModelTest {
    @BeforeClass
    public static void setUp() throws IOException {
        AbstractModelTest.setUp();
        bpmProcessor.injectObjectReferences(results);
    }


}
