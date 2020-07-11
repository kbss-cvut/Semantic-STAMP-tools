package cz.cvut.kbss.datatools.xmlanalysis.partners;

import cz.cvut.kbss.datatools.xmlanalysis.common.Utils;
import junit.framework.TestCase;
import org.junit.Ignore;
import org.junit.Test;
import sun.nio.cs.ext.MacArabic;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ServiceLoader;

public class MainConverterTest extends TestCase {

    @Ignore
    public void testMainAdonisXML() {
    }

    @Test
    public void testMainBizagiBPM() throws URISyntaxException {
        File f = Utils.getResourceAsFile("/bizagi/example-model-1.bpm");
        MainConverter.main(new String[]{f.getAbsolutePath(), "./target/test-output/MainConverterTest/example-model-1.rdf"});
    }
}