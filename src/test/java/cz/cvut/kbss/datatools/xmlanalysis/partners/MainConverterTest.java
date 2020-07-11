package cz.cvut.kbss.datatools.xmlanalysis.partners;

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
        File f = new File(MainConverterTest.class.getResource("/bizagi/example-model-1.bpm").toURI());
        MainConverter.main(new String[]{f.getAbsolutePath(), "./target"});
    }
}