package cz.cvut.kbss.datatools.xmlanalysis.partners.csat.model;

import cz.cvut.kbss.datatools.xmlanalysis.common.Utils;
import cz.cvut.kbss.datatools.xmlanalysis.partners.csat.ProcessBizagiBPMFile;
import cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf.BPMProcessor;
import cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf.InputXmlStream;
import cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf.JAXBUtils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class AbstractModelTest {

    protected static boolean initialized = false;
    protected static BPMProcessor bpmProcessor;
    protected static List<JAXBUtils.UnmarshledResult> results;
    protected static List<Object> objects;

//    @BeforeClass
    public static void setUp() throws IOException {
        if(!initialized) {
            bpmProcessor = new BPMProcessor();
            results = unmarshalXMLFiles(bpmProcessor);
            objects = flatten(results);
            initialized = true;
        }
    }


    public static List<JAXBUtils.UnmarshledResult> unmarshalXMLFiles(BPMProcessor bpmProcessor) throws IOException{
        List<InputXmlStream> inputXmls = new ProcessBizagiBPMFile
                .BizagiBPMPackageXMLStreamer(Utils.getResourceAsFile("/bizagi/example-model-1.bpm"))
                .streamSourceFiles().findFirst().get();


        return bpmProcessor.unmarshalXMLFiles(inputXmls);

//        for(JAXBUtils.UnmarshledResult ur : result){
//            for(Object o : ur.getObjects()){
//                if(o instanceof Activity){
//                    Activity a = (Activity)o;
//                    a.getImplementationSubFlow();
//                }
//            }
//        }
    }

    public static List<Object> flatten(List<JAXBUtils.UnmarshledResult> results){
        return (List<Object>)results.stream()
                .flatMap(r -> r.getObjects().stream()).collect(Collectors.toList());
    }

}
