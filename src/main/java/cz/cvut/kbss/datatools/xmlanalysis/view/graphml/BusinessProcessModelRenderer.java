package cz.cvut.kbss.datatools.xmlanalysis.view.graphml;

import cz.cvut.kbss.datatools.xmlanalysis.common.graphml.ActivityGMLBuilder;
import cz.cvut.kbss.datatools.xmlanalysis.voc.Vocabulary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BusinessProcessModelRenderer extends BaseGMLRenderer<ActivityGMLBuilder>{

    private static final Logger LOG = LoggerFactory.getLogger(BusinessProcessModelRenderer.class);


    @Override
    protected ActivityGMLBuilder createGMLBuilder(){
        return new ActivityGMLBuilder();
    }

    @Override
    protected void buildRendererMaps(){
        // node resource to renderer mapping
        bindTypeToRenderer(Vocabulary.j_c_Activity, gmlBuilder::addActivity);
        bindTypeToRenderer(Vocabulary.j_c_ProcessStart, gmlBuilder::addCircle);
        bindTypeToRenderer(Vocabulary.j_c_ProcessEnd, gmlBuilder::addCircle);
        bindTypeToRenderer(Vocabulary.j_c_Decision, gmlBuilder::addBranch);
        bindTypeToRenderer(Vocabulary.j_c_Parallelity, gmlBuilder::addUpTriangle);
        bindTypeToRenderer(Vocabulary.j_c_Merging, gmlBuilder::addDownTriagle);
        bindTypeToRenderer(Vocabulary.j_c_Trigger, gmlBuilder::addTrigger);
        bindTypeToRenderer(Vocabulary.j_c_Swimlane, gmlBuilder::addSwimLane);
        bindTypeToRenderer(Vocabulary.j_c_Cross_reference, gmlBuilder::addCrossReference);
        bindTypeToRenderer(Vocabulary.j_c_Subprocess, gmlBuilder::addSubProcess);


        // edge resource to renderer mapping
        bindPropertyToRenderer(Vocabulary.j_c_Subsequent, defaultEdgeRenderer);
        bindPropertyToRenderer(Vocabulary.j_c_Is_inside, partOfGroupRenderer);
    }

}
