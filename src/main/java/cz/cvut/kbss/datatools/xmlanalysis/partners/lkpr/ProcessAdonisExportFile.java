package cz.cvut.kbss.datatools.xmlanalysis.partners.lkpr;

import cz.cvut.kbss.commons.io.NamedStream;
import cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf.BPMProcessor;
import cz.cvut.kbss.datatools.xmlanalysis.partners.IRIMapper;
import cz.cvut.kbss.datatools.xmlanalysis.partners.lkpr.mapping.AdonisExportADOXML;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Stream;

public class  ProcessAdonisExportFile {

    private static final Logger LOG = LoggerFactory.getLogger(ProcessAdonisExportFile.class);

    protected BPMProcessor bpmProcessor = new BPMProcessor();
    protected Class<AdonisExportADOXML> mapperClass = AdonisExportADOXML.class;
    protected String pkg = "cz.cvut.kbss.datatools.xmlanalysis.bpmn.model";
    protected String rootIRI = "http://onto.fel.cvut.cz/partners/lkpr/";
//    protected String outputFile = "c:\\Users\\user\\Documents\\skola\\projects\\10-2017-ZETA\\code\\lkpr-process-model-extraction\\tmp-export.rdf";

    protected File rootDir;
    protected File sourceDir;
    protected File outputDir;

    protected String inputFileExtension = ".xml";

    public void processDir(String dirPath){
        init(dirPath);
        LOG.debug("Processing ADONIS exports in dir '{}'.", dirPath);
        for(File f : listSourceFilesInDir(sourceDir)){
            process(f);
        }
    }

    public void processFile(String fileString){
        File file = new File( fileString);
        init(file.getParent());
        LOG.debug("Processing ADONIS export file '{}'.", fileString);
        process(file);
    }

    public void init(String dirPath){
        bpmProcessor.setPrefixMapping(constructPrefixMapping());
        IRIMapper.initIriMapper(rootIRI);
        initFolders(dirPath);

    }

    public void process(File file){
        LOG.debug("Processing ADONIS export file  '{}'.", file.getAbsolutePath());
        if(!StringUtils.endsWithIgnoreCase(file.getName(),inputFileExtension))
            LOG.warn("The input file path points to a file with a unrecognized extension.");
        try(InputStream is = new FileInputStream(file)){
            File outputFile = getOutputFile(file);
            bpmProcessor.process(Stream.of(new NamedStream(file.getCanonicalPath(), is)), mapperClass, pkg, outputFile.getAbsolutePath());
        }catch(IOException e){
            LOG.error("", e);
        }
    }

    public Map<String, String> constructPrefixMapping(){
        Map<String, String> map = new HashMap<>();
        map.put("lkpr.stamp", "http://onto.fel.cvut.cz/partners/lkpr/");
        return map;
    }

    protected List<File> listSourceFilesInDir(File dir){
        return Arrays.asList(dir.listFiles(f -> StringUtils.endsWithIgnoreCase(f.getName(), inputFileExtension)));
    }

    protected void initFolders(String sourceFolder){
        Objects.requireNonNull(sourceFolder);
        this.rootDir = new File(sourceFolder);
        assert rootDir.exists();

        this.sourceDir = rootDir;
        this.outputDir = new File(rootDir, "output");
        if(!outputDir.exists()){
            outputDir.mkdirs();
        }
    }

    protected File getOutputFile(File inputFile){
        String name = inputFile.getName().replaceFirst("\\.[^\\.]+$", ".rdf");
        return new File(outputDir, name);
    }

}
