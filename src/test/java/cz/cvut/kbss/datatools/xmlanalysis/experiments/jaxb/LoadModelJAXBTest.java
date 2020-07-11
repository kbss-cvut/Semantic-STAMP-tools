package cz.cvut.kbss.datatools.xmlanalysis.experiments.jaxb;

import junit.framework.TestCase;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

public class LoadModelJAXBTest extends TestCase {
    @Ignore
    @Test
    public void testProcess(){
        File file = new File("/adonis/Safety TEST onion structure.xml");
        LoadModelJAXB processor = new LoadModelJAXB();
        processor.process(file);
    }

}