package cz.cvut.kbss.datatools.xmlanalysis.experiments.lkpr.converters.rdf;

import cz.cvut.kbss.datatools.xmlanalysis.common.graphml.BaseGMLRenderer;
import cz.cvut.kbss.datatools.xmlanalysis.common.graphml.GraphMLBuilder;
import cz.cvut.kbss.datatools.xmlanalysis.common.graphml.IGraphMLRenderer;
import cz.cvut.kbss.datatools.xmlanalysis.experiments.lkpr.converters.rdf.graphml.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RDFDataMgr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConvertRDFLKPRToGML<T extends GraphMLBuilder> {

    private static final Logger LOG = LoggerFactory.getLogger(ConvertRDFLKPRToGML.class);

    protected BaseGMLRenderer<T> gmlRenderer;

    public ConvertRDFLKPRToGML() {
    }

    public ConvertRDFLKPRToGML(BaseGMLRenderer<T> gmlRenderer) {
        this.gmlRenderer = gmlRenderer;
    }

    public BaseGMLRenderer<T> getGmlRenderer() {
        return gmlRenderer;
    }

    public void setGmlRenderer(BaseGMLRenderer<T> gmlRenderer) {
        this.gmlRenderer = gmlRenderer;
    }

    public void convertFolder(File dir){

    }

    public void convertFile(File file){
        Model m = RDFDataMgr.loadModel(file.toURI().toString());
        gmlRenderer.init();
        String name = file.getName();
        name = name.substring(0,name.lastIndexOf('.')) + ".gml";
        convert(m, gmlRenderer, new File(file.getParent(), name));
    }

    public void convert(Model m, IGraphMLRenderer renderer, File outputFile){
        GraphMLBuilder builder = renderer.render(m);
        try(FileOutputStream ps = new FileOutputStream(outputFile)){
            builder.write(ps);
        } catch (IOException e) {
            LOG.error("",e);
        }
    }

    public static <T extends GraphMLBuilder> void processMap(Map<BaseGMLRenderer, List<String>> rendererToModelMap, File rootDir){
        for(Map.Entry<BaseGMLRenderer, List<String>> entry : rendererToModelMap.entrySet()){
            processFiles(entry.getKey(), entry.getValue(), rootDir);
        }
    }

    public static <T extends GraphMLBuilder> void processFiles(BaseGMLRenderer<T> renderer, List<String> fileNames, File rootDir){
        for(String fileName : fileNames) {
            try {
                File inputFile = new File(rootDir, fileName);
                LOG.info("Processing file \"{}\"", inputFile.getAbsolutePath());
                ConvertRDFLKPRToGML gmlConverter = new ConvertRDFLKPRToGML(renderer);
                gmlConverter.convertFile(inputFile);
            }catch(Exception e){
                LOG.warn("Failed rendering GML for file \"{}\"", fileName, e);
            }
        }
    }


    public static void renderLKPRModels(){

        // set root folder
        File rootDir = new File("c:\\Users\\user\\Documents\\skola\\projects\\10-2017-ZETA\\code\\lkpr-process-model-extraction\\lkpr-process-models\\example-models-01\\out\\");

        // configure renderers to files
        Map<BaseGMLRenderer, List<String>> rendererToModelMap = new HashMap<>();

        // business process models
        rendererToModelMap.put(
                new BusinessProcessModelRenderer(),
                Arrays.asList(
                        "Aerobridge-Business process model-Aerobridge.ttl",
                        "bus-Business process model-BUS.ttl",
                        "model-001-Business process model-KSP_1_Safety risk management.ttl",
                        "model-001-Business process model-KSP_1A_Safety hazard.ttl",
                        "model-001-Business process model-KSP_1B_Hodnoceni rizik.ttl",
                        "model-001-Business process model-KSP_1C_Rizeni rizik.ttl",
                        "model-001-Business process model-KSP_2_Audit SMS.ttl",
                        "fuelling-Business process model-Fuelling.ttl"
                )
        );

        // working environment models
        rendererToModelMap.put(
                new WorkingEnvironmentRenderer(),
                Arrays.asList(
                        "Aerobridge-Working environment model-general roles.ttl",
                        "Aerobridge-Working environment model-HDG.ttl",
                        "Aerobridge-Working environment model-OJ CDP.ttl",
                        "bus_role-Working environment model-CDP_2.ttl",
                        "model-001-Working environment model-KSP_role.ttl"
                )
        );

        // risk pool models
        rendererToModelMap.put(
                new RiskPoolModelRenderer(),
                Arrays.asList(
                        "model-001-Risk pool-KSP_1A_Safety hazard.ttl",
                        "model-001-Risk pool-KSP_1B_Hodnocení rizik.ttl",
                        "model-001-Risk pool-KSP_1C_Rizeni rizik.ttl",
                        "model-001-Risk pool-KSP_2_Audit SMS.ttl"
                )
        );

        processMap(rendererToModelMap, rootDir);
    }

    public static void renderLKPRModels_0_2(){
        File rootDir = new File("c:\\Users\\user\\Documents\\skola\\projects\\10-2017-ZETA\\code\\lkpr-process-model-extraction\\lkpr-process-models\\example-models-01\\model-0.2\\out\\");
        // configure renderers to files
        Map<BaseGMLRenderer, List<String>> rendererToModelMap = new HashMap<>();

        // business process models
        rendererToModelMap.put(
                new BusinessProcessModelRenderer(),
                Arrays.asList(
                        "CDP_1_Aerobridge-Business process model-Aerobridge.ttl"
                )
        );

        // working environment models
        rendererToModelMap.put(
                new WorkingEnvironmentRenderer(),
                Arrays.asList(
                        "OJ CDP-Working environment model-OJ CDP.ttl"
                )
        );
        processMap(rendererToModelMap, rootDir);
    }

    public static void renderCSAT(){
        String in = "csat-process-models\\bizagi-process-models\\verze 25.10 BM Administration-001.rdf";
        File fin = new File(in);
        String fileName = fin.getName();
        File dir = fin.getParentFile();
        Map<BaseGMLRenderer, List<String>> rendererToModelMap = new HashMap<>();
        // business process models
        rendererToModelMap.put(
                new BusinessProcessModelRenderer(),
                Arrays.asList(fileName)
        );

        // working environment models
        rendererToModelMap.put(
                new WorkingEnvironmentRenderer(),
                Arrays.asList(fileName)
        );
        processMap(rendererToModelMap, dir);
    }

    public static void convert(String fileName){

    }

    public static void main(String[] args) {
//        renderLKPRModels();


//        renderLKPRModels_0_2();
        renderCSAT();
    }

}