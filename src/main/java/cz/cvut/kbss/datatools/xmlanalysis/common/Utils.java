package cz.cvut.kbss.datatools.xmlanalysis.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Properties;

public class Utils {
    private static final Logger LOG = LoggerFactory.getLogger(Utils.class);

    public static String ENCODING = "UTF-8";

    public static  String urlEncode(String strToEncode){
        try {
            return URLEncoder.encode(strToEncode, ENCODING);
        } catch (UnsupportedEncodingException ex) {
            LOG.error("", ex);
            throw new RuntimeException(ex);
        }
    }


    public static Properties loadProperties(String path){
        File f = getFile(path);
        Properties config = new Properties();
        try {
            config.load(new FileReader(f));
            return config;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static File getFile(String path){
        // find as file path
        File f = new File(path);
        if(f.exists())
            return f;

        // find as resource
        try {
            f = new File(Utils.class.getResource(path).toURI());
        } catch (URISyntaxException e) {
            LOG.error("Could not load the properties file \"{}\"", path, e);
        }

        return f.exists() ? f : null;
    }
}
