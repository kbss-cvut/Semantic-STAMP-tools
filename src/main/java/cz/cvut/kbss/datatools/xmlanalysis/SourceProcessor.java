package cz.cvut.kbss.datatools.xmlanalysis;

import cz.cvut.kbss.datatools.xmlanalysis.common.Utils;

import java.io.File;
import java.util.Properties;

/**
 * A processor that takes as an input a directory, it processes the files in that directory, and stores the output in
 * the output directory.
 */
public abstract class SourceProcessor {

    protected Properties config;
    protected File sourceDir;
    protected File[] sources;
    protected File outputDir;
    protected File outputDirAnalysis;

    public SourceProcessor() {}

    public SourceProcessor(String propertyPath) {
        this(Utils.loadProperties(propertyPath));
    }

    public SourceProcessor(Properties config) {
        setConfig(config);
    }

    public Properties getConfig() {
        return config;
    }

    public File getSourceDir() {
        return sourceDir;
    }

    public File[] getSources() {
        return sources;
    }

    public File getOutputDir() {
        return outputDir;
    }

    public File getOutputDirAnalysis() {
        return outputDirAnalysis;
    }

    public void setSourceDir(File sourceDir) {
        this.sourceDir = sourceDir;
    }

    public void setSources(File[] sources) {
        this.sources = sources;
    }

    public void setOutputDir(File outputDir) {
        this.outputDir = outputDir;
    }

    public void setOutputDirAnalysis(File outputDirAnalysis) {
        this.outputDirAnalysis = outputDirAnalysis;
    }

    public void setConfig(Properties config) {
        this.config = config;
        String sourceDirPath = config.getProperty(PropertyConstants.SOURCE_DIR);
        sourceDir = new File(sourceDirPath);
        sources = listSources();

        outputDir = ensureDir(config.getProperty(PropertyConstants.OUTPUT_DIR));
        outputDirAnalysis = ensureDir(outputDir, config.getProperty(PropertyConstants.OUTPUT_DIR_ANALYSIS));

    }

    public File ensureDir(String dirName){
        return Utils.ensureDir(null, dirName);
    }
    public File ensureDir(File parentDir, String dirName){
        return Utils.ensureDir(parentDir, dirName);
    }

    public File[] listSources(){
        return sourceDir.listFiles();
    }


    public void processSources(){
        processSources(sources);
    }
    public void processSources(File[] files){
        for(File f : files){
            processSource(f);
        }
    }

    public abstract void processSource(File inputFile);
}
