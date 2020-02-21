package cz.cvut.kbss.datatools.xmlanalysis.partners.csat.mapping;

import cz.cvut.kbss.datatools.xmlanalysis.partners.csat.model.AbstractModelTest;
import org.junit.BeforeClass;

import java.io.IOException;

public class TestMapping extends AbstractModelTest {
    @BeforeClass
    public static void setUp() throws IOException {
        AbstractModelTest.setUp();
        bpmProcessor.injectObjectReferences(results);
    }


}
