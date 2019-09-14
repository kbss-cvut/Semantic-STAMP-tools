package cz.cvut.kbss.datatools.lkpr.adonis;


import cz.cvut.kbss.datatools.xmlanalysis.XMLAnalyzer;
import cz.cvut.kbss.datatools.xmlanalysis.experiments.lkpr.converters.adoxml.ConvertADOXMLProcessModelToUFO;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LKPRBuildOntologies {
    private static final Logger LOG = LoggerFactory.getLogger(LKPRBuildOntologies.class);

//    @Test
    public void createOntoligies(){
        LOG.info("Creating Ontologies");
        ConvertADOXMLProcessModelToUFO converter = new ConvertADOXMLProcessModelToUFO("/lkpr-adonis-input.properties"){
//            @Override
//            public File[] listSources() {
//                return new File[]{new File(sourceDir,"Aerobridge.xml")};
//            }
        };
        converter.processSources();
//        File[] files = converter.getSources();
//        for(File f: files){
//            converter.processSource(f);
//        }
    }



    @Test
    public void analyzeSources(){
        XMLAnalyzer analyzer = new XMLAnalyzer("/lkpr-adonis-input.properties");
        analyzer.processSources();
        LOG.info("Creating Ontologies");
    }
}
