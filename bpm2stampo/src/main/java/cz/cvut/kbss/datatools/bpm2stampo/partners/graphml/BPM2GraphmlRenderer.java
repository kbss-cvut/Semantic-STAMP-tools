package cz.cvut.kbss.datatools.bpm2stampo.partners.graphml;

import com.tinkerpop.blueprints.Vertex;
import cz.cvut.kbss.datatools.bpm2stampo.common.graphml.ActivityGMLBuilder;
import cz.cvut.kbss.datatools.bpm2stampo.common.graphml.BaseGMLRenderer;
import cz.cvut.kbss.datatools.bpm2stampo.common.graphml.GraphMLBuilder;
import cz.cvut.kbss.onto.safety.stamp.Vocabulary;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class BPM2GraphmlRenderer extends BaseGMLRenderer<ActivityGMLBuilder> {
    @Override
    protected ActivityGMLBuilder createGMLBuilder() {
        return new ActivityGMLBuilder();
    }

    @Override
    protected void buildRendererMaps() {
        bindTypeToRenderer(Vocabulary.s_c_event_type, gmlBuilder::addActivity);
//        bindTypeToRenderer(Vocabulary.j_c_ProcessStart, gmlBuilder::addCircle);
//        bindTypeToRenderer(Vocabulary.j_c_ProcessEnd, gmlBuilder::addCircle);
//        bindTypeToRenderer(Vocabulary.j_c_Decision, gmlBuilder::addBranch);
//        bindTypeToRenderer(Vocabulary.j_c_Parallelity, gmlBuilder::addUpTriangle);
//        bindTypeToRenderer(Vocabulary.j_c_Merging, gmlBuilder::addDownTriagle);
//        bindTypeToRenderer(Vocabulary.j_c_Trigger, gmlBuilder::addTrigger);
//        bindTypeToRenderer(Vocabulary.j_c_Swimlane, gmlBuilder::addSwimLane);
//        bindTypeToRenderer(Vocabulary.j_c_Cross_reference, gmlBuilder::addCrossReference);
//        bindTypeToRenderer(Vocabulary.j_c_Subprocess, gmlBuilder::addSubProcess);
//
//
//        // edge resource to renderer mapping
        bindPropertyToRenderer(Vocabulary.s_c_next, defaultEdgeRenderer);
//        bindPropertyToRenderer(Vocabulary.s_p_has_control_structure_element_part, partOfGroupRenderer);
    }

    static class FlowGraph extends BaseGMLRenderer<ActivityGMLBuilder>{
        @Override
        protected ActivityGMLBuilder createGMLBuilder() {
            return new ActivityGMLBuilder();
        }

        @Override
        protected void buildRendererMaps() {
            bindTypeToRenderer(Vocabulary.s_c_event_type, gmlBuilder::addActivity);
            bindPropertyToRenderer(Vocabulary.s_c_next, defaultEdgeRenderer);
        }
    }

    static class StructureGraph extends BaseGMLRenderer<ActivityGMLBuilder>{
        @Override
        protected ActivityGMLBuilder createGMLBuilder() {
            return new ActivityGMLBuilder();
        }

        @Override
        protected void buildRendererMaps() {
            bindTypeToRenderer(Vocabulary.s_c_unsafe_event, nd -> addActivity(nd, "#FF0000"));
            bindTypeToRenderer(Vocabulary.s_c_event_type, gmlBuilder::addActivity);
            bindTypeToRenderer(Vocabulary.s_c_people_group, nd -> addActivity(nd, "#00FF00"));
            bindTypeToRenderer(Vocabulary.s_c_controller_type, nd -> addActivity(nd, "#0000FF"));


            bindPropertyToRenderer(Vocabulary.s_p_has_control_structure_element_part, partOfGroupRenderer);
            bindPropertyToRenderer(Vocabulary.s_p_has_group_member, partOfGroupRenderer);
            bindPropertyToRenderer(Vocabulary.s_p_has_person_member, partOfGroupRenderer);
        }

        protected Vertex addActivity(GraphMLBuilder.NodeData nodeData, String colorHexa){
            super.gmlBuilder.addWithCustomShape(nodeData, "com.yworks.flowchart.process", "fill", colorHexa);
            return null;
        }
    }


    public static void writeGraph(Model m, BaseGMLRenderer<ActivityGMLBuilder> gr, String outputFile){
        gr.init();
        GraphMLBuilder b = gr.render(m);
        try(FileOutputStream fos = new FileOutputStream(outputFile)) {
            b.write(fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void experimentGenerateFlowAndStructure() {
//        String f = "file:../lkpr-process-model-extraction/csat-process-models/bizagi-process-models/verze 08.11 BM Administration.rdf";
//        String o1 = "../lkpr-process-model-extraction/csat-process-models/bizagi-process-models/verze 08.11 BM Administration-flow.gml";
//        String o2 = "../lkpr-process-model-extraction/csat-process-models/bizagi-process-models/verze 08.11 BM Administration-structure.gml";

        String f = "file:../lkpr-process-model-extraction/lkpr-process-models/output/CDP.rdf";
        String o1 = "../lkpr-process-model-extraction/lkpr-process-models/output/CDP-flows.gml";
        String o2 = "../lkpr-process-model-extraction/lkpr-process-models/output/CDP-structure.gml";


        File ff = new File(".");
        System.out.println(ff.getAbsolutePath());
        Model m = ModelFactory.createDefaultModel();
        m.read(f);
        System.out.println(m.size());
        writeGraph(m, new FlowGraph(), o1);
        writeGraph(m, new StructureGraph(), o2);
    }

    public static void utilityRenderFlowAndStructrue(String in){
        File fin = new File(in);
        File dir = fin.getParentFile();
        String fileName = fin.getName();
        fileName = fileName.substring(0, fileName.lastIndexOf('.'));
        String outFlow = new File(dir, fileName + "-flow.gml").getAbsolutePath();
        String outStructure = new File(dir, fileName + "-structure.gml").getAbsolutePath();
        Model m = ModelFactory.createDefaultModel();
        m.read(fin.toURI().toString());
        System.out.println(m.size());
        writeGraph(m, new FlowGraph(), outFlow);
        writeGraph(m, new StructureGraph(), outStructure);
    }

    public static void main(String[] args) {
        String in = "csat-process-models/bizagi-process-models/verze 25.10 BM Administration-001.rdf";
        utilityRenderFlowAndStructrue(in);
    }
}
