package cz.cvut.kbss.datatools.xmlanalysis.partners.csat.mapping;

import cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf.model.ControllerType;
import cz.cvut.kbss.datatools.xmlanalysis.mapstructext.annotations.AddTypes;
import cz.cvut.kbss.datatools.xmlanalysis.mapstructext.annotations.RootMapping;
import cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf.model.NextConnection;
import cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf.model.ProcessType;
import cz.cvut.kbss.datatools.xmlanalysis.partners.IRIMapper;
import cz.cvut.kbss.datatools.xmlanalysis.partners.csat.model.Package;
import cz.cvut.kbss.datatools.xmlanalysis.partners.csat.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.Set;

@Mapper
public interface BizagiDiagPackage {
    public static final String PACKAGE = "http://bizagi.com/package/Package";
    public static final String PARTICIPANT = "http://bizagi.com/package/Participant";
    public static final String WORKFLOWPROCESS = "http://bizagi.com/package/WorkflowProcess";
    public static final String ACTIVITY = "http://bizagi.com/package/Activity";
    public static final String TRANSITION = "http://bizagi.com/package/Transition";


    @RootMapping
    @AddTypes(types = {PACKAGE})
    @Mapping(source="name", target = "label")
    @Mapping(target = "iri", expression = "java(toIRI(pkg))")
    @Mapping(source = "processes", target = "childProcessTypes")
    @Mapping(source = "participants", target = "childConnections")
    ProcessType xmlToProcessType(Package pkg);


    @RootMapping
    @AddTypes(types = {PARTICIPANT})
    @Mapping(source = "name", target = "label")
    @Mapping(target = "iri", expression = "java(toIRI(participant))")
    ControllerType xmlToPersonRole(Participant participant);

    @RootMapping
    @AddTypes(types = {WORKFLOWPROCESS})
    @Mapping(source="name", target = "label")
    @Mapping(target = "iri", expression = "java(toIRI(workflowProcess))")
    @Mapping(source = "activities", target = "childProcessTypes")
    @Mapping(source = "transitions", target = "childConnections")
    ProcessType xmlToProcessType(WorkflowProcess workflowProcess);

    @RootMapping
    @AddTypes(types = {ACTIVITY})
    @Mapping(source="name", target = "label")
    @Mapping(target = "iri", expression = "java(toIRI(activity))")
    ProcessType xmlToProcessType(Activity activity);

    @RootMapping
    @AddTypes(types = {TRANSITION})
    @Mapping(target = "iri", expression = "java(toIRI(transition))")
    @Mapping(target = "from", expression = "java(toIRI(transition.getFrom()))")
    @Mapping(target = "to", expression = "java(toIRI(transition.getTo()))")
    NextConnection xmlToNextConnection(Transition transition);

    Set<String> toIRIs(Collection<? extends BaseEntity> xmlObjects);

    // generating IRIs from xmlObjects

    default String toIRI(BaseEntity xmlObject){
        return IRIMapper.generateIRI(xmlObject);
    }
}
