package cz.cvut.kbss.datatools.bpm2stampo.converters.lkpr.mapping;

import cz.cvut.kbss.datatools.bpm2stampo.mapstructext.DefaultMapper;
import cz.cvut.kbss.datatools.bpm2stampo.mapstructext.annotations.*;
import cz.cvut.kbss.datatools.bpm2stampo.converters.lkpr.model.*;
import cz.cvut.kbss.datatools.bpm2stampo.converters.lkpr.model.Process;
import cz.cvut.kbss.datatools.bpm2stampo.xml2stamprdf.jaxbmodel.IBaseXMLEntity;
import cz.cvut.kbss.datatools.bpm2stampo.xml2stamprdf.model.*;
import cz.cvut.kbss.onto.safety.stamp.Vocabulary;
import org.apache.commons.lang.StringUtils;
import org.mapstruct.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Stream;

@Mapper
@SetIRI(prefix = "http://onto.fel.cvut.cz/partners/lkpr/", sourceField = "id")
@SetLabel(sourceField = "name")
public interface AdonisExportADOXML extends DefaultMapper<BaseEntity> {

    static final Logger LOG = LoggerFactory.getLogger(AdonisExportADOXML.class);

    static final String ADOXML = "http://onto.fel.cvut.cz/ontolgies/adoxml-bpmn/adoxml";
    String BUSINESS_PROCESS_MODEL = "http://onto.fel.cvut.cz/ontolgies/adoxml-bpmn/business-process-model";
    String COMPANY_MAP_MODEL = "http://onto.fel.cvut.cz/ontolgies/adoxml-bpmn/company-map-model";
    String WORKING_ENVIRONMENT_MODEL = "http://onto.fel.cvut.cz/ontolgies/adoxml-bpmn/working-environment-model";
    String ACTIVITY = "http://onto.fel.cvut.cz/ontolgies/adoxml-bpmn/activity";
    String SWIMLANE = "http://onto.fel.cvut.cz/ontolgies/adoxml-bpmn/swimlane";
    String SWIMLANE_VERTICAL = "http://onto.fel.cvut.cz/ontolgies/adoxml-bpmn/swimlane-vertical";
    String SWIMLANE_HORIZONTAL = "http://onto.fel.cvut.cz/ontolgies/adoxml-bpmn/swimlane-horizontal";
    String PROCESS_START = "http://onto.fel.cvut.cz/ontolgies/adoxml-bpmn/process-start";
    String END = "http://onto.fel.cvut.cz/ontolgies/adoxml-bpmn/end";
    String DECISION = "http://onto.fel.cvut.cz/ontolgies/adoxml-bpmn/decision";
    String MERGING = "http://onto.fel.cvut.cz/ontolgies/adoxml-bpmn/merging";
    String PARALLELITY = "http://onto.fel.cvut.cz/ontolgies/adoxml-bpmn/parallelity";
    String SUBPROCESS = "http://onto.fel.cvut.cz/ontolgies/adoxml-bpmn/subprocess";
    String TRIGGER = "http://onto.fel.cvut.cz/ontolgies/adoxml-bpmn/trigger";


    String ORGANIZATIONAL_UNIT_MODEL = "http://onto.fel.cvut.cz/ontolgies/adoxml-bpmn/organizational-unit-model";
    String RISK_POOL_MODEL = "http://onto.fel.cvut.cz/ontolgies/adoxml-bpmn/risk-pool-model";
    String PERFORMER = "http://onto.fel.cvut.cz/ontolgies/adoxml-bpmn/performer";
    String ROLE = "http://onto.fel.cvut.cz/ontolgies/adoxml-bpmn/role";
    String AGGREGATION = "http://onto.fel.cvut.cz/ontolgies/adoxml-bpmn/aggregation";

//    public static final String[] EVENT_TYPES = {
//            "Business process model", "Company map", // processes
//            "Activity", "Swimlane (vertical)", "Swimlane (horizontal)", "Process Start", // sub process events types
//            "End", "Decision", "Merging", "Parallelity", "Subprocess", "Trigger"
//    };
//
//    public static final String[] ORDERING_CONNECTOR_TYPES = {"Subsequent"};
//    public static final String[] EVENT_PART_OF_CONNECTOR_TYPES = {""};
//
//
//    public static final String[] SINGLE_AGENT_TYPES = {"Performer", "Role"};
//    public static final String[] GROUP_AGENT_TYPES = {"Organizational unit"};
//    public static final String[] ALL_AGENT_TYPES = ArrayUtils.addAll(SINGLE_AGENT_TYPES, GROUP_AGENT_TYPES);
//
//
//
//    public static final String CONTROLLER_HAS_ROLE_CONNECTOR_TYPES = "Has role"; // connector - Performer "has role" role
//
//
//
//    // connector - role "Is inside" Aggregation (Something like complex role)
//    // connector - Performer "belongs to" Organizational Unit
//    // connector - Organizational Unit "Is subordinated" Organizational Unit
//    public static final String[] ROLE_PART_OF_CONNECTOR_TYPES = {"Is inside", "Belongs to", "Is subordinated"};



    String ADOXML_NS = "http://onto.fel.cvut.cz/ontolgies/adoxml-bpmn/";


    List<Connector> unresolvedConnectors = new ArrayList<>();

    // ADOXML mapping
    @RootMapping
    @AddTypes(types = {ADOXML})
    @Mapping(source = "workingEnvironmentModels", target = "participants")
    @Mapping(expression = "java(iris(in.getBusinessProcessModels(), in.getRiskPoolModels(), in.getCompanyMapModels()))", target = "components")
    @Mapping(expression = "java(\"LKPR Operations\")", target = "label")
    EventType xmlToProcessType(ADOXML in);

    @RootMapping
    @SetIRI(beforeId = "controller")
    @AddTypes(types = {ADOXML})
    @Mapping(source = "workingEnvironmentModels", target = "subGroups")
    @Mapping(expression = "java(\"LKPR Organization\")", target = "label")
    GroupController xmlToGroup(ADOXML in);

    // BusinessProcessModel mapping
    @RootMapping
//    @AttributeValueQualifier(field = "modeltype", acceptedValues = {"Business process model", "Company map"})
    @AddTypes(types = {BUSINESS_PROCESS_MODEL})
    @Mapping(expression = "java(iris(in.getActivities(), in.getOtherActivities()))", target = "components")
    @Mapping(source = "subsequentConnectors", target = "connections")
    EventType xmlToProcessType(BusinessProcessModel in);

    @AfterMapping
    default void xmlToProcessType(BusinessProcessModel in, @MappingTarget EventType out) {
        addUnregisteredPartOfConnectors(in.getEventPartOfConnectors());
    }

    @RootMapping
    @AddTypes(types = COMPANY_MAP_MODEL)
//    @Mapping(source = "processes", target = "components")
//    @Mapping(expression = "java(iris(in.getProcesses(), in.getSwimlanesH(), in.getSwimlanesV(), in.getAggregations()))", target = "components")
    @Mapping(expression = "java(iris(in.getActors(), in.getExternalPartners()))", target = "participants")
    EventType xmlToProcessType(CompanyMapModel in);

    @AfterMapping
    default void xmlToProcessType(CompanyMapModel in, @MappingTarget EventType out) {
        addUnregisteredPartOfConnectors(in.getHasChildProcess());
        addUnregisteredPartOfConnectors(in.getIsInside());

        Set<String> eventParts = getAndInit(out::getComponents, out::setComponents);
        for(Instance inst : in.getProcesses()){
            if(inst instanceof Process){
                Process refProcess = (Process)inst;
                BusinessProcessModel process = refProcess.getReferencedProcess();
                String partIri = null;
                if(process != null){
                    partIri = iri(process);

                }else{
                    partIri = iri(inst);
                }
                if(partIri != null) {
                    eventParts.add(partIri);
                }
            }
        }
    }





    // OrganizationalModel mapping
    @RootMapping
//    @AttributeValueQualifier(field = "modeltype", acceptedValues = {"Business process model", "Company map"})
    @AddTypes(types = {ORGANIZATIONAL_UNIT_MODEL})
//    @Mapping(source = "performers", target = "people")
    @Mapping(expression = "java(iris(in.getPerformers(), in.getRoles(), in.getAggregations()))", target = "controllerTypes")
    @Mapping(source = "organizationalUnits", target = "subGroups")
    GroupController xmlToGroup(WorkingEnvironmentModel in);

    @AfterMapping
    default void xmlToGroup(WorkingEnvironmentModel in, @MappingTarget GroupController out) {
        addUnregisteredPartOfConnectors(
                in.getOrganizationalUnitIsSubordinateConnectors(),
                in.getPerformerBelongsToConnectors(),
                in.getPerformerIsManagerConnectors(),
                in.getRoleIsInsideConnectors(),
                in.getPerformerHasRoleConnectors()
        );
    }

    // RiskPool mapping
//    @RootMapping
//    @AddTypes(types = {RISK_POOL_MODEL, Vocabulary.s_c_unsafe_event})
//    @Mapping(source = "risks", target = "components")
//    EventType xmlToProcessType(RiskPool in);


    // Box elements
    @RootMapping
    @AttributeValueQualifier(field = "cls", acceptedValues = {
            "Swimlane (vertical)", "Swimlane (horizontal)", "Process start",
            "End", "Decision", "Merging", "Parallelity", "Subprocess", "Trigger"})
    @AddTypesFromVal(field = "cls", namespace = ADOXML_NS)
    EventType xmlToEventType(Instance in);


    @AfterMapping
    default EventType xmlToEventType(Instance in, @MappingTarget EventType eventType){
        // set swimlane label
        if(StringUtils.startsWith(in.getCls(), "Swimlane")){
            String l = eventType.getLabel();
            if(l != null){
                l = l.replaceAll("\\s*\\(Role\\)\\s*", " ");
                l = l.replaceAll("\\s*\\(Working environment model\\)\\s*", " ");
                l  = l.trim();
            }
            IBaseXMLEntity p = in.getParent();
            if(p != null){
                if(p instanceof Model){
                    Model m = (Model)p;
                    l = "'" + l + "' in '" + m.getName() + "'";
                }
            }
            eventType.setLabel(l);
        }

        // add control flow event type
        Set<String> classes = new HashSet<>(Arrays.asList("Process start",
                "End", "Decision", "Merging", "Parallelity", "Subprocess", "Trigger"));
        if(classes.contains(in.getCls())){
            eventType.getTypes().add(cz.cvut.kbss.datatools.bpm2stampo.voc.Vocabulary.c_flowControlEventType);
        }

        // handle Processes which have a structure references
        if(in instanceof Process){
            Process refProcess = (Process)in;
            BusinessProcessModel referencedProcess = refProcess.getReferencedProcess();
            if(referencedProcess != null){
                String referencedProcessIri = iri(referencedProcess);
                if(referencedProcessIri != null){
                    eventType.setSameStructureAs(referencedProcessIri);
                }
            }
        }

        return eventType;
    }

    @RootMapping
    @AttributeValueQualifier(field = "cls", acceptedValues = {"Activity"})
    // TODO add the risks from the input Activity as unsafe event parts for the output
    @AddTypesFromVal(field = "cls", namespace = ADOXML_NS)
    @Mapping(source = "risks", target = "components")
    @Mapping(expression = "java(iris(in.getResponsibleRole()))", target = "participants")
    EventType xmlToEventType(Activity in);


    @RootMapping
    @AddTypes(types = {Vocabulary.s_c_unsafe_event})
    @AttributeValueQualifier(field = "cls", acceptedValues = {"Risk"})
    @AddTypesFromVal(field = "cls", namespace = ADOXML_NS)
    EventType xmlToUnsafeEvent(Instance in);


    @RootMapping
    @AttributeValueQualifier(field = "cls", acceptedValues = {"Aggregation", "Role", "Performer"})
    @AddTypesFromVal(field = "cls", namespace = ADOXML_NS)
    ControllerType xmlToRole(Instance in);

//    @RootMapping
//    @AttributeValueQualifier(field = "cls", acceptedValues = {"Performer"})
//    @SetIRI(beforeId = "controller-", sourceField = "id")
//    @AddTypesFromVal(field = "cls", namespace = ADOXML_NS)
//    PersonController xmlToPerson(Instance in);

    @RootMapping
    @AttributeValueQualifier(field = "cls", acceptedValues = {"Organizational unit"})
//    @SetIRI(beforeId = "group-controller", sourceField="id")
    @AddTypesFromVal(field = "cls", namespace = ADOXML_NS)
    GroupController xmlToGroup(Instance in);

    @RootMapping
    @AttributeValueQualifier(field = "cls", acceptedValues = "Subsequent")
    @Mapping(source = "from", target = "from")
    @Mapping(source = "to", target = "to")
    NextConnection xmlToNextConnection(Connector connector);

//    @RootMapping
//    @AttributeValueQualifier(field = "cls", acceptedValues = "Is subordinated")
//    @Mapping(source = "from", target = "from")
//    @Mapping(source = "to", target = "to")
//    NextConnection xmlToNextConnection(Connector connector);


    default void addUnregisteredPartOfConnectors(Collection<? extends Connector> ... collections){
        Stream.of(collections)
                .filter(c -> c != null)
                .forEach(unresolvedConnectors::addAll);
    }


//    @AfterMapping
//    default void addChildProcessTypes(ADOXML in, @MappingTarget ProcessType out){
//        Set<String> processTypes = out.getChildProcessTypes() != null ?
//                out.getChildProcessTypes() :
//                new HashSet<>();
//        if(out.getChildProcessTypes() == null){
//            out.setChildProcessTypes(processTypes);
//        }
//        Optional.of(in.getBusinessProcessModels()).ifPresent(l -> l.forEach(
//                x -> processTypes.add(toIRI(x))
//        ));
//        Optional.of(in.getRiskPoolModels()).ifPresent(l -> l.forEach(
//                x -> processTypes.add(toIRI(x))
//        ));
//    }

    // Connection relations

    // Part of relations

    // after all the mapping is done

    @AfterAllMappings
    default void resolvePartOfConnectors(){
        List<Connector> resolvedConnectors = new ArrayList<>();
        for(Connector c : unresolvedConnectors){
            String part = null;
            Identifiable whole = null;
            boolean processed = false;
            if(StringUtils.equalsIgnoreCase(c.getCls(), "Has role")){
                part = iri(c.getTo());
                whole = getRegistry().get(iri(c.getFrom()));
            }else {
                part = iri(c.getFrom());
                whole = getRegistry().get(iri(c.getTo()));
            }
            processed = addValueToCollection(part, whole, c.getCls());

            if(processed)
                resolvedConnectors.add(c);
//            if(from != null && toIRI != null){
//                Set<String> l = from.getChildProcessTypes();
//                if(l == null){
//                    l = new HashSet<>();
//                    from.setChildProcessTypes(l);
//                }
//                .add(toIRI);
//                resolvedConnectors.add(c);
//            }
        }
        unresolvedConnectors.removeAll(resolvedConnectors);
    }


    default boolean addValueToCollection(String partIri, Identifiable whole, String connectorType){
        if(whole == null || partIri == null)
            return false;
        Set<String> s = null;
        if(EventType.class.isAssignableFrom(whole.getClass())) {
            EventType p = ((EventType) whole);
            s = getAndInit(p::getComponents, p::setComponents);

            switch (connectorType){
                case "Is inside":
                    break;
                case "Grouped into (risk)":
                    break;
                default: LOG.warn("Unknown part of relation '{}' between part '{}' and whole '{}' of type {}", connectorType, partIri, whole.getIri(), whole.getClass().getCanonicalName());
            }
        }else if(GroupController.class.isAssignableFrom(whole.getClass())){
            GroupController g = ((GroupController)whole);
            switch (connectorType){
                case "Is subordinated": s = getAndInit(g::getSubGroups, g::setSubGroups); break;
                case "Belongs to": s = getAndInit(g::getControllerTypes, g::setControllerTypes); break;
                case "Is manager": s = getAndInit(g::getControllerTypes, g::setControllerTypes); break;
                case "Is inside": s = getAndInit(g::getControllerTypes, g::setControllerTypes); break;

                default: LOG.warn("Unknown part of relation '{}' between part '{}' and whole '{}' of type {}", connectorType, partIri, whole.getIri(), whole.getClass().getCanonicalName());
            }
        }else if(ControllerType.class.isAssignableFrom(whole.getClass()) && (
                "Is inside".equals(connectorType) ||
                "Has role".equals(connectorType))){
            ControllerType ct = ((ControllerType)whole);
            s = getAndInit(ct::getSubControllerTypes, ct::setSubControllerTypes);
        }else{
            LOG.warn("Unknown part of relation '{}' between part '{}' and whole '{}' of type {}", connectorType, partIri, whole.getIri(), whole.getClass().getCanonicalName());
        }

        if(s != null) {
            s.add(partIri);
            return true;
        }
        return false;
    }




//    default List<Connector> selectOrderingConnectors(Collection<Connector> connectors){
//        return selectElementsWithClass(connectors, c -> c.getCls(), EVENT_TYPES); //TODO continue
//    }
//
//
//
//
//    default List<Instance> selectAllAgentTypeInstances(Collection<Instance> instances){
//        return selectElementsWithClass(instances, i -> i.getCls(), ALL_AGENT_TYPES);
//    }
//
////    default List<Instance> selectGroupTypeInstances(Collection<Instance> instances){
////        return selectInstancesWithClass(instances, GROUP_AGENT_TYPES);
////    }
////
////    default List<Instance> selectAgentTypeInstances(Collection<Instance> instances){
////        return selectInstancesWithClass(instances, SINGLE_AGENT_TYPES);
////    }
//
//    default List<Instance> selectEventTypeInstances(Collection<Instance> instances){
//        return selectElementsWithClass(instances, i -> i.getCls(), EVENT_TYPES);
//    }
//
//
//    default <T> List<T> selectElementsWithClass(Collection<T> instances, Function<T, String> f, String[] classes){
//        return Optional.ofNullable(instances)
//                .map(l -> l.stream()
//                        .filter(i -> Stream.of(classes)
//                                .filter(cls -> StringUtils.equalsIgnoreCase(f.apply(i), cls))
//                                .findAny().isPresent()
//                        )
//                        .collect(Collectors.toList())
//                )
//                .orElse(Collections.EMPTY_LIST);
//    }

}
