package cz.cvut.kbss.datatools.xmlanalysis.view.graphml;

import cz.cvut.kbss.datatools.xmlanalysis.common.graphml.WorkingEnvironmentBuilder;
import cz.cvut.kbss.datatools.xmlanalysis.voc.Vocabulary;

public class WorkingEnvironmentRenderer extends BaseGMLRenderer<WorkingEnvironmentBuilder>{

    @Override
    protected WorkingEnvironmentBuilder createGMLBuilder() {
        return new WorkingEnvironmentBuilder();
    }

    @Override
    protected void buildRendererMaps(){
        bindTypeToRenderer(Vocabulary.j_c_Organizational_Unit, gmlBuilder::addOrganizationalUnit);
        bindTypeToRenderer(Vocabulary.j_c_Performer, gmlBuilder::addPerformer);
        bindTypeToRenderer(Vocabulary.j_c_Role, gmlBuilder::addRole);
        bindTypeToRenderer(Vocabulary.j_c_Aggregation, gmlBuilder::addAggregation);
        bindTypeToRenderer(Vocabulary.j_c_Cross_reference, gmlBuilder::addCrossReference);


        bindPropertyToRenderer(Vocabulary.j_c_BelongsTo, gmlBuilder::addBelongsTo);
        bindPropertyToRenderer(Vocabulary.j_c_Is_subordinated, gmlBuilder::addIsSubordinated);
        bindPropertyToRenderer(Vocabulary.j_c_Is_manager, gmlBuilder::addIsManager);
        bindPropertyToRenderer(Vocabulary.j_c_Has_Role, gmlBuilder::addHasRole);
        bindPropertyToRenderer(Vocabulary.j_c_Is_inside, partOfGroupRenderer);
    }



}
