package cz.cvut.kbss.datatools.xmlanalysis.partners.lkpr;

import cz.cvut.kbss.datatools.xmlanalysis.partners.AbstractProcessModelExporter;
import cz.cvut.kbss.datatools.xmlanalysis.partners.lkpr.mapping.AdonisExportADOXML;
import cz.cvut.kbss.datatools.xmlanalysis.partners.lkpr.model.ADOXML;
import cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf.InputXmlStream;
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
        pkg = "cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf.model";
        outputDir = null;
    }

    public void config(String dirPath){
        mapperClass = AdonisExportADOXML.class;
        pkg = "cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf.model";
        initFolders(dirPath);
        initBMPMProcessor();
    }

    public void process(File file){
        LOG.debug("Processing ADONIS export file  '{}'.", file.getAbsolutePath());
        if(!StringUtils.endsWithIgnoreCase(file.getName(),inputFileExtension))
            LOG.warn("The input file path points to a file with a unrecognized extension.");
        try(InputStream is = new FileInputStream(file)){
            File outputFile = getOutputFile(file);

            bpmProcessor.processFiles(Stream.of(new InputXmlStream(file.getCanonicalPath(), is, ADOXML.class)), mapperClass, pkg, outputFile.getAbsolutePath());
        }catch(IOException e){
            LOG.error("", e);
        }
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

    protected File getOutputFile(File inputFile){
        String name = inputFile.getName().replaceFirst("\\.[^\\.]+$", ".rdf");
        return new File(outputDir, name);
    }

}
