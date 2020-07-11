package cz.cvut.kbss.datatools.bpm2stampo.common;


import cz.cvut.kbss.commons.io.NamedStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

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

    public static String toString(InputStream is){
        try {
            return org.apache.commons.io.IOUtils.toString(is, StandardCharsets.UTF_8);
        }catch (IOException e){
            LOG.error("", e);
        }
        return null;
    }

    public static InputStream toInputStream(String in){
        return new ByteArrayInputStream(in.getBytes(StandardCharsets.UTF_8));
    }

    public static void copy(NamedStream ns, OutputStream os){
        InputStream is = ns.getContent();
        ByteArrayInputStream bis = null;
        if(!(is instanceof ByteArrayInputStream)){
            bis = toByteInputStream(is);
            ns.setContent(bis);
        }else{
            bis = (ByteArrayInputStream)is;
        }
        try {
            org.apache.commons.io.IOUtils.copy(bis, os);
        }catch (IOException e){
            LOG.error("", e);
        }
        bis.reset();
    }

}
