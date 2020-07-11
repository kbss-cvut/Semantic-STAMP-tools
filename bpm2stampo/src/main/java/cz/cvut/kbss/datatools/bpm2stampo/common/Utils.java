package cz.cvut.kbss.datatools.bpm2stampo.common;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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


    public static Map<String, String> loadQueriesAsMap(){
        return Stream.of(
                "fix-parents-of-next-relations",
                "add-capababilities-to-processes",
                "remove-redundant-has-part")
                .map(n -> Pair.of(n, getResourceAsString("/query/" + n + ".sparql")))
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
    }
    public static Map<String, String> loadQueriesAsMapNotWorking(){
        File queryDir = getFile("/query");
        return Stream.of(queryDir.listFiles(f -> f.getName().endsWith(".sparql")))
                .map(f -> Pair.of(fileName(f.getName()), getResourceAsString("/query/" + f.getName())))
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
    }

    public static List<String> loadQueriesAsList(){
        File queryDir = getFile("/query");

        return Stream.of(queryDir.listFiles(f -> f.getName().endsWith(".sparql")))
                .map(f -> getResourceAsString("/query/" + f.getName()))
                .collect(Collectors.toList());
    }

    public static String fileName(File f){
        return fileName(f.getName());
    }

    public static String fileName(String fn){
        return fn.substring(0, fn.lastIndexOf('.'));
    }

    public static String getDefinedQuery(String name){
        return getResourceAsString(getDefinedQueryResourcePath(name));
    }

    public static String getResourceAsString(String resourcePath){
        try {
            LOG.debug("opening resource at \"{}\"", resourcePath);
            return IOUtils.toString(Utils.class.getResourceAsStream(resourcePath), Utils.ENCODING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected static String getDefinedQueryResourcePath(String queryName){
        return "/query/" + queryName + ".sparql";
    }

    /**
     * This method is intended to be used in tests where the
     * classes and resources are not yet packaged in a jar.
     * @param resourcePath path of the resource
     * @return the file pointing to the resource file pointed by the resourcePath
     */
    public static File getResourceAsFile(String resourcePath){
        URL url = Utils.class.getResource(resourcePath);
        if(url == null){
            return null;
        }
        String p = url.getPath();
        LOG.debug("opening resource with uri \"{}\"", p);
        return new File(p);
    }

    public static File getFile(String path){
        // find as file path
        File f = new File(path);
        if(f.exists())
            return f;

        // find as resource
        f = getResourceAsFile(path);

        return f != null && f.exists() ? f : null;
    }

    public static File ensureDir(String dirName){
        return ensureDir(null, dirName);
    }
    public static File ensureDir(File parentDir, String dirName){
        File dir = new File(parentDir, dirName);
        if(!dir.exists()){
            dir.mkdirs();
        }
        return dir;
    }

    public static String getNameWithoutExtension(File f){
        String name = f.getName();
        if(f.isDirectory()){
            return name;
        }
        return name.substring(0,name.lastIndexOf('.'));
    }

    public static File newFileWithExtension(File in, String extension){
        String name = in.getName().replaceFirst("\\.[^\\.]+$", ".rdf");
        return new File(in.getParent(), name);
    }

    public static File getOutputFileSmart(File inputFile, File outputDir){
        if(outputDir == null)
            outputDir = new File(".");
        return getOutputFile(inputFile, outputDir);
    }
    public static File getOutputFile(File inputFile, File outputDir){
        String name = inputFile.getName().replaceFirst("\\.[^\\.]+$", ".rdf");
        return new File(outputDir, name);
    }

    public static void main(String[] args) {
        loadQueriesAsList().stream().forEach(
                q -> System.out.println("\n" + q)
        );
    }
}
