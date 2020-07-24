package cz.cvut.kbss.datatools.bpm2stampo.converters.adonis;

import cz.cvut.kbss.datatools.bpm2stampo.converters.AbstractProcessModelExporter;
import cz.cvut.kbss.datatools.bpm2stampo.converters.adonis.mapping.AdonisExportADOXML;
import cz.cvut.kbss.datatools.bpm2stampo.converters.adonis.model.ADOXML;
import cz.cvut.kbss.datatools.bpm2stampo.xml2stamprdf.InputXmlStream;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class  ProcessAdonisExportFile extends AbstractProcessModelExporter<AdonisExportADOXML> {

    private static final Logger LOG = LoggerFactory.getLogger(ProcessAdonisExportFile.class);

    protected File rootDir;
    protected File sourceDir;
    protected File outputDir;

    protected String inputFileExtension = ".xml";

    @Override
    public String getProcessorName() {
        return "ADONIS xml";
    }

    public void processDir(String dirPath){
        config(dirPath);
        LOG.debug("Processing ADONIS exports in dir '{}'.", dirPath);
        for(File f : listSourceFilesInDir(sourceDir)){
            process(f);
        }
    }

    public void processFile(String fileString){
        File file = new File( fileString);
        config(file.getParent());
        LOG.debug("Processing ADONIS export file '{}'.", fileString);
        process(file);
    }

    @Override
    public void config(){
        mapperClass = AdonisExportADOXML.class;
        pkg = "cz.cvut.kbss.datatools.bpm2stampo.xml2stamprdf.model";
        outputDir = null;
    }

    public void config(String dirPath){
        mapperClass = AdonisExportADOXML.class;
        pkg = "cz.cvut.kbss.datatools.bpm2stampo.xml2stamprdf.model";
        initFolders(dirPath);
        initBMPMProcessor();
    }

    public void process(File file){
        try(InputStream is = new FileInputStream(file)){
            process(file.getCanonicalPath(), is);
        }catch(IOException e){
            LOG.error("", e);
        }
    }

    @Override
    public InputStream convert(String fileName, InputStream stream) {
        LOG.debug("Processing ADONIS export file  '{}'.", fileName);
        if(!StringUtils.endsWithIgnoreCase(fileName,inputFileExtension))
            LOG.warn("The input file path points to a file with a unrecognized extension.");
        File outputFile = getOutputFile(fileName);

        return bpmProcessor.convert(Stream.of(Arrays.asList(new InputXmlStream(fileName, stream, ADOXML.class))), mapperClass, pkg);
    }

    @Override
    public void process(String fileName, InputStream is) {
        LOG.debug("Processing ADONIS export file  '{}'.", fileName);
        if(!StringUtils.endsWithIgnoreCase(fileName,inputFileExtension))
            LOG.warn("The input file path points to a file with a unrecognized extension.");
        File outputFile = getOutputFile(fileName);

        bpmProcessor.processFiles(Stream.of(new InputXmlStream(fileName, is, ADOXML.class)), mapperClass, pkg, outputFile.getAbsolutePath());
    }

    @Override
    public Map<String, String> constructPrefixMapping(){
        Map<String, String> map = super.constructPrefixMapping();
        map.put("lkpr.stamp", "http://onto.fel.cvut.cz/partners/lkpr/");
        map.put("adoxml", AdonisExportADOXML.ADOXML_NS);
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

    protected File getOutputFile(File file){
        return getOutputFile(file.getName());
    }
    protected File getOutputFile(String fileName){
        String name = fileName.replaceFirst("\\.[^\\.]+$", ".rdf");
        return new File(outputDir, name);
    }

}
