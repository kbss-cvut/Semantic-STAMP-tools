package cz.cvut.kbss.datatools.xmlanalysis.partners;

import cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf.BPMProcessor;
import cz.cvut.kbss.jopa.vocabulary.RDF;
import cz.cvut.kbss.jopa.vocabulary.RDFS;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AbstractProcessModelExporter<T> {

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
