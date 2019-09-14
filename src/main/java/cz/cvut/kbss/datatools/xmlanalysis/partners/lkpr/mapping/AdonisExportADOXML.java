package cz.cvut.kbss.datatools.xmlanalysis.partners.lkpr.mapping;

import cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf.model.*;
import cz.cvut.kbss.datatools.xmlanalysis.mapstructext.annotations.*;
import cz.cvut.kbss.datatools.xmlanalysis.partners.IRIMapper;
import cz.cvut.kbss.datatools.xmlanalysis.partners.lkpr.model.*;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mapper
public interface AdonisExportADOXML {

    static final Logger LOG = LoggerFactory.getLogger(AdonisExportADOXML.class);

    String ADOXML = "http://onto.fel.cvut.cz/ontolgies/adoxml-bpmn/adoxml";
    String BUSINESS_PROCESS_MODEL = "http://onto.fel.cvut.cz/ontolgies/adoxml-bpmn/business-process-model";
    String ACTIVITY = "http://onto.fel.cvut.cz/ontolgies/adoxml-bpmn/activity";
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
    String IRI_EXP = "java(toIRI(in))";


    Map<String, Identifiable> registry = new HashMap<>();
    List<Connector> unresolvedConnectors = new ArrayList<>();

    // ADOXML mapping
    @RootMapping
    @Mapping(expression = IRI_EXP, target = "iri")
    @Mapping(source = "organizationalModels", target = "participants")
    @Mapping(expression = "java(toIRIs(in.getBusinessProcessModels(), in.getRiskPoolModels()))", target = "childProcessTypes")
    ProcessType xmlToProcessType(ADOXML in);

    // BusinessProcessModel mapping
    @RootMapping
//    @AttributeValueQualifier(field = "modeltype", acceptedValues = {"Business process model", "Company map"})
    @Mapping(expression = IRI_EXP, target = "iri")
    @AddTypes(types = {BUSINESS_PROCESS_MODEL})
    @Mapping(source = "name", target = "label")
    @Mapping(source = "activities", target = "childProcessTypes")
    @Mapping(source = "subsequentConnectors", target = "childConnections")
    ProcessType xmlToProcessType(BusinessProcessModel in);

    @AfterMapping
    default void xmlToProcessType(BusinessProcessModel in, @MappingTarget ProcessType out) {
        addUnregisteredPartOfConnectors(in.getEventPartOfConnectors());
    }

    // OrganizationalModel mapping
    @RootMapping
//    @AttributeValueQualifier(field = "modeltype", acceptedValues = {"Business process model", "Company map"})
    @Mapping(expression = IRI_EXP, target = "iri")
    @AddTypes(types = {ORGANIZATIONAL_UNIT_MODEL})
    @Mapping(source = "name", target = "label")
    @Mapping(source = "performers", target = "people")
    @Mapping(expression = "java(toIRIs(in.getRoles(), in.getAggregations()))", target = "controllerTypes")
    @Mapping(source = "organizationalUnits", target = "subGroups")
    Group xmlToGroup(OrganizationalModel in);

    @AfterMapping
    default void xmlToGroup(OrganizationalModel in, @MappingTarget Group out) {
        addUnregisteredPartOfConnectors(
                in.getOrganizationalUnitIsSubordinateConnectors(),
                in.getPerformerBelongsToConnectors(),
                in.getPerformerIsManagerConnectors(),
                in.getRoleIsInsideConnectors()
        );
    }

    // RiskPool mapping
    @RootMapping
    @Mapping(expression = IRI_EXP, target = "iri")
    @AddTypes(types = {RISK_POOL_MODEL})
    @Mapping(source = "name", target = "label")
    @Mapping(source = "risks", target = "childProcessTypes")
    ProcessType xmlToProcessType(RiskPool in);


    // Box elements
    @RootMapping
    @AttributeValueQualifier(field = "cls", acceptedValues = {
            "Activity", "Swimlane (vertical)", "Swimlane (horizontal)", "Process Start",
            "End", "Decision", "Merging", "Parallelity", "Subprocess", "Trigger", "Risk"})
    @Mapping(expression = IRI_EXP, target = "iri")
    @AddTypesFromVal(field = "cls", namespace = ADOXML_NS)
    @Mapping(source = "name", target = "label")
    ProcessType xmlToEventType(Instance in);

    @RootMapping
    @AttributeValueQualifier(field = "cls", acceptedValues = {"Aggregation", "Role"})
    @Mapping(expression = IRI_EXP, target = "iri")
    @AddTypesFromVal(field = "cls", namespace = ADOXML_NS)
    @Mapping(source = "name", target = "label")
    ControllerType xmlToRole(Instance in);

    @RootMapping
    @AttributeValueQualifier(field = "cls", acceptedValues = {"Performer"})
    @Mapping(expression = IRI_EXP, target = "iri")
    @AddTypesFromVal(field = "cls", namespace = ADOXML_NS)
    @Mapping(source = "name", target = "label")
    Person xmlToPerson(Instance in);

    @RootMapping
    @AttributeValueQualifier(field = "cls", acceptedValues = {"Organizational unit"})
    @AddTypesFromVal(field = "cls", namespace = ADOXML_NS)
    @Mapping(source = "name", target = "label")
    @Mapping(expression = IRI_EXP, target = "iri")
    Group xmlToGroup(Instance in);

    @AfterMapping
    default void register(BaseEntity in, @MappingTarget Identifiable out){
        if(out != null)
            registry.put(out.getIri(), out);
    }

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




    Set<String> toIRI(Collection<? extends BaseEntity> xmlObjects);

    default String toIRI(BaseEntity xmlObject){
        return IRIMapper.generateIRI(xmlObject);
    }

    default Set<String> toIRIs(Collection<? extends BaseEntity> ... collections){
        return Stream.of(collections)
                .filter(c -> c != null)
                .flatMap(c -> c.stream())
                .map(this::toIRI)
                .collect(Collectors.toCollection(HashSet::new));
    }


    // after all the mapping is done

    @AfterAllMappings
    default void resolvePartOfConnectors(){
        List<Connector> resolvedConnectors = new ArrayList<>();
        for(Connector c : unresolvedConnectors){
            String fromIRI = toIRI(c.getFrom());
            Identifiable to = registry.get(toIRI(c.getTo()));
            boolean processed = addValueToCollection(fromIRI, to, c.getCls());

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

    default <T> T get(BaseEntity in, Class<T> cls){
        Identifiable identifiable = registry.get(toIRI(in));
        if(identifiable != null && cls.isAssignableFrom(identifiable.getClass())){
            return (T)identifiable;
        }
        return null;
    }

    default boolean addValueToCollection(String partIri, Identifiable whole, String connectorType){
        if(whole == null || partIri == null)
            return false;
        Set<String> s = null;
        if(ProcessType.class.isAssignableFrom(whole.getClass())) {
            ProcessType p = ((ProcessType) whole);
            s = getAndInit(p::getChildProcessTypes, p::setChildProcessTypes);

            switch (connectorType){
                case "Is inside":
                    break;
                case "Grouped into (risk)":
                    break;
                default: LOG.warn("Unknown part of relation '{}' between part '{}' and whole '{}' of type {}", connectorType, partIri, whole.getIri(), whole.getClass().getCanonicalName());
            }
        }else if(Group.class.isAssignableFrom(whole.getClass())){
            Group g = ((Group)whole);
            switch (connectorType){
                case "Is subordinated": s = getAndInit(g::getSubGroups, g::setSubGroups); break;
                case "Belongs to": s = getAndInit(g::getPeople, g::setPeople); break;
                case "Is manager": s = getAndInit(g::getPeople, g::setPeople); break;
                case "Is inside": s = getAndInit(g::getControllerTypes, g::setControllerTypes); break;
                default: LOG.warn("Unknown part of relation '{}' between part '{}' and whole '{}' of type {}", connectorType, partIri, whole.getIri(), whole.getClass().getCanonicalName());
            }
        }else if(ControllerType.class.isAssignableFrom(whole.getClass()) && "Is inside".equals(connectorType)){
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

    default Set<String> getAndInit(Supplier<Set<String>> get, Consumer<Set<String>> set){
        Set<String> s = get.get();
        if(s == null){
            s = new HashSet<>();
            set.accept(s);
        }
        return s;
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
