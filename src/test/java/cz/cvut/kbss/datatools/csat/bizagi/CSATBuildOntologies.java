package cz.cvut.kbss.datatools.csat.bizagi;


import cz.cvut.kbss.datatools.bpm2stampo.XMLAnalyzer;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CSATBuildOntologies {
    private static final Logger LOG = LoggerFactory.getLogger(CSATBuildOntologies.class);

//    @Test
    public void createOntoligies(){
        // TODO
//        LOG.info("Creating Ontologies");
//        ConvertADOXMLProcessModelToUFO converter = new ConvertADOXMLProcessModelToUFO("/lkpr-adonis-input.properties"){
////            @Override
////            public File[] listSources() {
////                return new File[]{new File(sourceDir,"Aerobridge.xml")};
////            }
//        };
//        converter.processSources();
////        File[] files = converter.getSources();
////        for(File f: files){
////            converter.processSource(f);
////        }
    }

    @Test
    public void analyzeSources(){
        XMLAnalyzer analyzer = new XMLAnalyzer("/csat-bizagi-input.properties");
        LOG.info("Analyzing XML input");
        analyzer.processSources();
    }
}
