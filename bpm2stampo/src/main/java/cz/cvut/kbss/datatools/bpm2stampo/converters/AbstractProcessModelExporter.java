package cz.cvut.kbss.datatools.bpm2stampo.converters;

import cz.cvut.kbss.datatools.bpm2stampo.common.Utils;
import cz.cvut.kbss.datatools.bpm2stampo.xml2stamprdf.BPMProcessor;
import cz.cvut.kbss.jopa.vocabulary.RDF;
import cz.cvut.kbss.jopa.vocabulary.RDFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractProcessModelExporter<T> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractProcessModelExporter.class);

    protected BPMProcessor bpmProcessor;
    protected Class<T> mapperClass;
    protected String pkg;
    protected File outputFile;
    protected File outputDir;

    protected void initBMPMProcessor(){
        bpmProcessor = new BPMProcessor();
        bpmProcessor.setPrefixMapping(constructPrefixMapping());
//        bpmProcessor. // TODO
        bpmProcessor.resetRegistry();
    }


    public void processFile(String filePath, String outputFile){
        try(InputStream is = new FileInputStream(Utils.getFile(filePath))){
            processFile(filePath, is, outputFile);
        } catch (FileNotFoundException e) {
            LOG.warn("Could find file with at \"{}\"", filePath,e);
        } catch (IOException e) {
            LOG.warn("Error reading file \"{}\"", filePath, e);
        }
    }

    public void processFile(String inputName, InputStream is, String outputFile){
        File f = new File(inputName);
        if(outputFile != null) {
            this.outputFile = new File(outputFile);
        }

        if(this.outputFile == null)
            this.outputFile = Utils.getOutputFileSmart(f, new File("."));

        this.outputDir = this.outputFile.getParentFile();
        if(this.outputDir != null && !this.outputDir.exists()){
            this.outputDir.mkdirs();
        }

        config();
        initBMPMProcessor();
        LOG.info("converter [{}] - converting bpmn to rdf, from \"{}\" out \"{}\"", getProcessorName(), inputName, this.outputFile);
        bpmProcessor.resetRegistry();

        process(inputName, is);
    }

    public InputStream convertToStream(String inputName, InputStream is){
        config();
        initBMPMProcessor();
        LOG.info("converter [{}] - converting bpmn to rdf, from \"{}\"", getProcessorName(), inputName);
        bpmProcessor.resetRegistry();

        return convert(inputName, is);
    }

    public OutputStream processStream(String inputName, InputStream is){
        return null;
    }

    public abstract void config();
    public abstract void process(String fileName, InputStream stream);
    public abstract InputStream convert(String fileName, InputStream stream);


    public String getProcessorName(){
        return "unnamed";
    }

    public Map<String, String> constructPrefixMapping(){
        Map<String, String> map = new HashMap<>();
        map.put("stamp", "http://onto.fel.cvut.cz/ontologies/stamp/");
        map.put("ufo", "http://onto.fel.cvut.cz/ontologies/ufo/");
        map.put(RDFS.PREFIX, RDFS.NAMESPACE);
        map.put(RDF.PREFIX, RDF.NAMESPACE);
        return map;
    }

    public BPMProcessor getBpmProcessor() {
        return bpmProcessor;
    }

    public void setBpmProcessor(BPMProcessor bpmProcessor) {
        this.bpmProcessor = bpmProcessor;
    }

    public Class<T> getMapperClass() {
        return mapperClass;
    }

    public void setMapperClass(Class<T> mapperClass) {
        this.mapperClass = mapperClass;
    }

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public File getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

    public File getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(File outputDir) {
        this.outputDir = outputDir;
    }
}
