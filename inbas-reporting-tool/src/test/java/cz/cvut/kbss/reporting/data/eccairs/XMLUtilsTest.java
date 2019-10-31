package cz.cvut.kbss.reporting.data.eccairs;

import cz.cvut.kbss.reporting.exception.NotFoundException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.xml.validation.Schema;

import static org.junit.Assert.assertNotNull;

public class XMLUtilsTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void loadsSchemaFromRemoteUrl() throws Exception {
        // Had to replace the original ECCAIRS XSD, because it is often offline
        final String location = "https://www.w3.org/2002/08/xhtml/xhtml1-strict.xsd";
        final Schema s = XMLUtils.loadSchema(location);
        assertNotNull(s);
    }

    @Test
    public void loadsSchemaFromFileWhenLocationIsNotValidUrl() throws Exception {
        final String location = "src/test/resources/data/ECCAIRS5_dataTypes.xsd";
        final Schema s = XMLUtils.loadSchema(location);
        assertNotNull(s);
    }

    @Test
    public void loadsSchemaFromClasspathWhenLocationIsNotValidUrlAndFileIsNotFound() throws Exception {
        final String location = "data/ECCAIRS5_dataTypes.xsd";
        final Schema s = XMLUtils.loadSchema(location);
        assertNotNull(s);
    }

    @Test
    public void loadSchemaThrowsNotFoundExceptionWhenSchemaLocationIsNotValid() throws Exception {
        final String location = "data/blabla.xsd";
        thrown.expect(NotFoundException.class);
        thrown.expectMessage("No schema was found at " + location);
        XMLUtils.loadSchema(location);
    }
}