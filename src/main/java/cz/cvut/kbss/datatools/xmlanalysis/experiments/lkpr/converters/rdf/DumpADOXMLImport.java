package cz.cvut.kbss.datatools.xmlanalysis.experiments.lkpr.converters.rdf;

import cz.cvut.kbss.datatools.xmlanalysis.SourceProcessor;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.OWL2;
import org.apache.jena.vocabulary.RDF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DumpADOXMLImport extends SourceProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(DumpADOXMLImport.class);

    public static final String ADOXML_IMPORT_DUMP_DIR = "output.dir.adoxml-import-dump";

    protected String defaultOutputDirName = "adoxml-import-dump";

    protected File adoxmlImportDumpDir;
    protected File currentFile;
    protected File outputFile;
    protected Model currentModel;

    protected PrintStream output;
    protected int level;



    @Override
    public void setConfig(Properties config) {
        super.setConfig(config);
        adoxmlImportDumpDir = ensureDir(config, outputDir, ADOXML_IMPORT_DUMP_DIR);
    }

    @Override
    public void processSource(File inputFile) {
        currentFile = inputFile;
        outputFile = new File(adoxmlImportDumpDir, String.format("%s-%s.%s", inputFile.getName(),"dump","txt"));
        currentModel = RDFDataMgr.loadModel(inputFile.toURI().toASCIIString());
        try(PrintStream ps = new PrintStream(outputFile)) {
            printModel();
        }catch(IOException e){
            LOG.error("", e);
        }
    }

    protected void printModel(){

        List<Resource> ontologyList = currentModel.listSubjectsWithProperty(RDF.type, OWL2.Ontology).toList();
        Resource ontology;
        if(ontologyList != null && !ontologyList.isEmpty()){
            ontology = ontologyList.get(0);
            if(ontologyList.size() > 1){
                LOG.warn("There are multiple defined ontologies {}");
            }
        }
        printTitle(
                String.format("-- Processing file", currentFile.getName()),
                String.format("-- Processing file", currentFile.getName()),
                ontologyList.stream()
                        .map(o -> currentModel.shortForm(o.getURI()))
                        .collect(Collectors.joining("\n"))
                );
        printEntities();

    }


    protected void printEntities(){
        // TODO - CONTINUE HERE
//        currentModel.listResourcesWithProperty()

        printRelations();
    }



    protected void printRelations(){

    }


    protected void printTitle(String ... titles){
        String.format("%s\n%s\n%s",
                StringUtils.repeat('-', 20),
                Stream.of(titles).map(s -> "-- " + s).collect(Collectors.joining("\n")),
                StringUtils.repeat('-', 20)
        );
    }

    protected void printSection(String ... titles){
        String.format("\n\n%s\n%s\n%s",
                Stream.of(titles).map(s -> "-- " + s).collect(Collectors.joining("\n")),
                StringUtils.repeat('-', 20)
        );
    }

    protected void printMessage(String str){
        output.println(str);
    }


}
