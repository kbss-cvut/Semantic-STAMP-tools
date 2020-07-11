package cz.cvut.kbss.datatools.xmlanalysis;

import junit.framework.TestCase;
import org.junit.Ignore;
import org.junit.Test;

public class XMLAnalyzerTest extends TestCase {

    @Test
    public void testXMLAnalyzer_processSources(){
        XMLAnalyzer analyzer = new XMLAnalyzer("/lkpr-adonis-input.properties");

        analyzer.processSources();
        assertTrue(true);
    }
}