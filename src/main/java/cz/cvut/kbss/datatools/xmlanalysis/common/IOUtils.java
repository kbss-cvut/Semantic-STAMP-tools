package cz.cvut.kbss.datatools.xmlanalysis.common;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class IOUtils {
    private static final Logger LOG = LoggerFactory.getLogger(IOUtils.class);
    public static ByteArrayInputStream  toByteInputStream(InputStream is){
        try {
            String xmlString = org.apache.commons.io.IOUtils.toString(is, Charset.defaultCharset());
            return new ByteArrayInputStream(xmlString.getBytes(Charset.defaultCharset()));
        } catch (IOException e) {
            LOG.error("",e);
        }
        return null;
    }
}
