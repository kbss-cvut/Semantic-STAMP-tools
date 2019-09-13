package cz.cvut.kbss.datatools.xmlanalysis.view.graphml;

import cz.cvut.kbss.datatools.xmlanalysis.common.graphml.RiskPoolGMLBuilder;
import cz.cvut.kbss.datatools.xmlanalysis.voc.Vocabulary;

public class RiskPoolModelRenderer extends BaseGMLRenderer<RiskPoolGMLBuilder>{
    @Override
    protected RiskPoolGMLBuilder createGMLBuilder() {
        return new RiskPoolGMLBuilder();
    }

    @Override
    protected void buildRendererMaps() {
        bindTypeToRenderer(Vocabulary.j_c_Risk, gmlBuilder::addRisk);
        bindTypeToRenderer(Vocabulary.j_c_Aggregation, gmlBuilder::addAggregation);
        bindTypeToRenderer(Vocabulary.j_c_Cross_reference, gmlBuilder::addCrossReference);

        bindPropertyToRenderer(Vocabulary.j_c_Grouped_Into_Risk, gmlBuilder::addGroupedIntoRisk);
    }
}
