package cz.cvut.kbss.datatools.bpm2stampo.converters.csat.mapping;

import cz.cvut.kbss.datatools.bpm2stampo.common.Utils;
import cz.cvut.kbss.datatools.bpm2stampo.mapstructext.DefaultMapper;
import cz.cvut.kbss.datatools.bpm2stampo.mapstructext.annotations.*;
import cz.cvut.kbss.datatools.bpm2stampo.converters.csat.ValueProcessing;
import cz.cvut.kbss.datatools.bpm2stampo.converters.csat.model.Package;
import cz.cvut.kbss.datatools.bpm2stampo.converters.csat.model.*;
import cz.cvut.kbss.datatools.bpm2stampo.xml2stamprdf.model.*;
import cz.cvut.kbss.onto.safety.stamp.Vocabulary;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

@Mapper
@SetIRI(prefix = "http://onto.fel.cvut.cz/partners/csat/", sourceField = "id")
@SetLabel(sourceField = "name")
public interface BizagiDiagPackage extends DefaultMapper<BaseEntity> {

    Logger LOG = LoggerFactory.getLogger(BizagiDiagPackage.class);

//    public static final String DEVIATIONS = "deviations";
    public static final String DEVIATIONS = "list of deviations";

    String baseIri = "http://onto.fel.cvut.cz/partners/csat/";
    String baseIriPrefix = "csat.stamp";

    String BIZAGI_PACKAGE_NAMESPACE = "http://bizagi.com/package/";
    String BIZAGI_PACKAGE_PREFIX = "bizagi";

    String PACKAGE_IRI = "http://bizagi.com/package/Package";
    String PARTICIPANT_IRI = "http://bizagi.com/package/Participant";
    String POOL_IRI = "http://bizagi.com/package/Pool";
    String WORKFLOWPROCESS_IRI = "http://bizagi.com/package/WorkflowProcess";
    String ACTIVITY_IRI = "http://bizagi.com/package/Activity";
    String TRANSITION_IRI = "http://bizagi.com/package/Transition";

    String IRI_EXP = "java(toIRI(in))";
    
    String GROUP = "G";
    String PARTICIPANT = "Participant";
    String CAPABILITY = "C";
    String PROCESS = "[P]";
    String ACTIVITY = "[A]";
    String DECISION = "[D]";
    String HAZARD = "[H]";
    String CONTROL_STRUCTURE = "Control structure";

    Map<String, Identifiable> registry = new HashMap<>();
    List<Activity> unresolvedSubProcess = new ArrayList<>();

    // do not transform Package to eventtype if it is an OrgChart package
    @RootMapping
    default GroupController xmlToGroupControllerRoot(Package in){
        GroupController out = transform("xmlToGroupController", in, GroupController.class);
        if(in.getName().contains("OrgChart")){
            out.getTypes().add(cz.cvut.kbss.datatools.bpm2stampo.voc.Vocabulary.c_Organizational_Unit);
        }
        return out;
    }

    @SetIRI(beforeId = GROUP)
    @AddTypes(types = {PACKAGE_IRI})
    @Mapping(source = "pools ", target = "subGroups")
    GroupController xmlToGroupController(Package in);

    @RootMapping
    default EventType xmlToEventTypeRoot(Package in){
        if(!in.getName().contains("OrgChart")){
            return transform("xmlToEventType", in, EventType.class);
        }
        return null;
    }

    // TODO: toIRIs must include "group" in the iri. How to access the IRIDecorator created for the SetIRI annotation from
    //  this mapper
    @AddTypes(types = {PACKAGE_IRI})
    @Mapping(source = "processes", target = "components")
    @Mapping(source = "pools", target = "participants")
    EventType xmlToEventType(Package in);

    @AfterMapping
    default void xmlToEventType(Package in, @MappingTarget EventType out){
        String l = out.getLabel();
        if(l != null && !l.isEmpty()) {
            out.setLabel("Main - " + l);
        }
    }

    @RootMapping
    default GroupController xmlToGroupControllerRoot(Pool in) {
        GroupController out = transform("xmlToGroupController", in, GroupController.class);
        Package p = in.getFirstAncestor(Package.class);
        if (p.getName().startsWith("OrgChart")) {
            out.getTypes().add(cz.cvut.kbss.datatools.bpm2stampo.voc.Vocabulary.c_Organizational_Unit);
        }
        return out;
    }

    @AddTypes(types = POOL_IRI)
    GroupController xmlToGroupController(Pool in);

    @AfterMapping
    default void xmlToGroupController(Pool in, @MappingTarget GroupController out){

        // add team if not in label. Because some pool labels might be used to refer to a process
        String l = out.getLabel();
        if(l == null || l.isEmpty()){// add default label for unlabeled groups
            l = "NO LABEL";
            out.setLabel(l);
        }
//        if(!StringUtils.containsIgnoreCase(l, "team")){
//            out.setLabel("Team - " + l);
//        }

        // add controller parts from activities of the process associated with the pool
        WorkflowProcess workflowProcess = in.getProcess();
        Set<String> subControllers = getAndInit(out::getSubGroups, out::setSubGroups);
        if(workflowProcess != null && workflowProcess.getActivities() != null) {
//        Package p = in.getFirstAncestor(Package.class);
//        List<WorkflowProcess> processes = p.getProcesses();
//        Set<String> subControllers = getAndInit(out::getSubGroups, out::setSubGroups);
//        if(processes != null) {
//            WorkflowProcess workflowProcess = processes.stream()
//                    .filter(wp -> wp.getId().equals(in.getProcessId()))
//                    .findFirst()
//                    .orElse(null);
            for(Activity a : workflowProcess.getActivities()) {
                subControllers.addAll(iris(a.getPerformers()));
                WorkflowProcess subProcess = a.getSubProcess();
                if(subProcess != null){
                    Package subProcessPackage = subProcess.getFirstAncestor(Package.class);
                    subControllers.add(iri(subProcessPackage, new String[]{GROUP}, emptyStringArray));
//                    List<Pool> pools = subProcessPackage.getPools();
//                    if(pools != null)
//                        subControllers.addAll(iris(pools));
                }
            }
        }
    }

//    @AfterMapping
//    default void xmlToEventType(Package in, @MappingTarget GroupController out){
//        if(in.getName() != null && in.)
//    }

    @RootMapping
    @AddTypes(types = {PARTICIPANT_IRI})
    ControllerType xmlToPersonRole(Participant in);

    // Do not transform OrgChart WorkflowProcesses to event types
    @RootMapping
    default EventType xmlToProcessTypeRoot(WorkflowProcess in){
        Package p = (Package)in.getFirstAncestor(Package.class);
        if(p != null && p.getName().contains("OrgChart")){
            return null;
        }
        return transform("xmlToProcessType", in, EventType.class);
    }

    @AddTypes(types = {WORKFLOWPROCESS_IRI, Vocabulary.s_c_controlled_process_type})
    @Mapping(source = "activities", target = "components")
    @Mapping(source = "transitions", target = "connections")
    EventType xmlToProcessType(WorkflowProcess in);

    @AfterMapping
    default void xmlToProcessType(WorkflowProcess in, @MappingTarget EventType out){
        Set<String> participants = getAndInit(out::getParticipants, out::setParticipants);
        Package pck = in.getFirstAncestor(Package.class);
        // fix label if it starts with team and it is the only process
//        if(out.getLabel().startsWith("Team") ) {
        if(!in.getName().equals("Main Process")) {// TODO inject pool in process, use pool in the condition
            if (pck.getProcesses().size() < 3) {
                out.setLabel(composeLabel(PROCESS, pck.getName()));
            } else {
                out.setLabel(composeLabel(PROCESS, out.getLabel() + " - " + pck.getName()));
            }
        }
//        }
        // add the pool as a participant to to the process
        Pool pool = pck.getPools().stream().filter(p -> p.getProcessId().equals(in.getId())).findFirst().orElse(null);
        if(pool != null)
            participants.add(iri(pool));
//        set.add(
//                iri(in, new String[]{GROUP}, emptyStringArray)
//        );
    }

    @RootMapping
    @SetIRI(beforeId = CAPABILITY)
    @AddTypes(types = {Vocabulary.s_c_capability})
    @SetLabel(beforeLabel = CAPABILITY)
    @Mapping(source = "in", target="manifestation" )
    Capability xmlToWorkflowProcessCapability(WorkflowProcess in);

    @AfterMapping
     default void xmlToWorkflowProcessCapability(WorkflowProcess in, @MappingTarget Capability out){
        out.setManifestation(iri(in));
//        out.setManifestation(
//                iri(in, new String[]{GROUP}, emptyStringArray)
//        );
    }

//    @RootMapping
//    @SetIRI(beforeId = GROUP)
//    @AddTypes(types = {Vocabulary.s_c_people_group})
//    @SetLabel(beforeLabel = GROUP)
//    GroupController xmlToWorkflowProcessGroup(WorkflowProcess in);

//    @AfterMapping
//    default void xmlToWorkflowProcessGroup(WorkflowProcess in, @MappingTarget GroupController out){
//        getAndInit(out::getCapabilities, out::setCapabilities).add(
//                iri(in, new String[]{CAPABILITY}, emptyStringArray)
//        );
//    }

    // Do not transform OrgChart Activities
    @RootMapping
    default EventType xmlToProcessTypeRoot(Activity in){
        Package p = (Package)in.getFirstAncestor(Package.class);
        if(p != null && p.getName().contains("OrgChart")){
            return null;
        }
        return transform("xmlToProcessType", in, EventType.class);
    }

    @AddTypes(types = {ACTIVITY_IRI})
    @Mapping(source = "performers", target = "participants" )
//    @Mapping(source = "elementAttributeValues.deviations", target = "components")
    EventType xmlToProcessType(Activity in);


    @RootMapping
    @SetIRI(beforeId = {CAPABILITY})
    @AddTypes(types = {ACTIVITY_IRI})
    @SetLabel(beforeLabel = CAPABILITY)
    @Mapping(source = "in", target = "manifestation")
    Capability xmlToCapability(Activity in);

    @AfterMapping
    default void xmlToCapability(Activity in, Capability out){
        out.setManifestation(iri(in));
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //// Build activity control structure model adding control and feedback connections ///////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @AfterMapping
    default void xmlToProcessType(Activity in, @MappingTarget EventType out){

        Set<String> components = getAndInit(out::getComponents, out::setComponents);
        // handle event types and event parts
        handleEventTypeParts(in, out);
        WorkflowProcess tmp = null;
        if(in.getSubProcess() != null){
             tmp = in.getSubProcess();
            if(tmp == null && in.getImplementationSubFlow() != null) {
                tmp = new WorkflowProcess();
                tmp.setId(in.getImplementationSubFlow().getReferencedProcessId());
            }
            if(tmp != null) {
                Package p = tmp.getFirstAncestor(Package.class);
                components.add(iri(p));
//                if("Main Process".equals(tmp.getName())){
//                    Package p = tmp.getFirstAncestor(Package.class);
//                    components.add(iri(p));
//                }else {
//                    components.add(iri(tmp));
//                }
            }
        }

        ElementAttributeValues attrs = in.getElementAttributeValues();
        if(attrs == null || attrs.getAttributeValueList() == null)
            return;
        List<AttributeValue> attributeValues = attrs.getAttributeValueList().stream()
                .filter(av -> Optional.ofNullable(av)
                        .map(v -> v.getExtendedAttribute())
                        .map(a -> a.getName())
                        .map(n -> StringUtils.equalsIgnoreCase(DEVIATIONS, n))
                        .orElse(false)
                )
                .collect(Collectors.toList());
        for(AttributeValue devAttributeValue : attributeValues){
            List<EventType> deviations = xmlDeviationsStringToEventTypes(devAttributeValue);
            deviations.forEach(d -> getRegistry().put(d.getIri(), d));
            components.addAll(deviations.stream().map(e -> e.getIri()).collect(Collectors.toSet()));
        }
    }

    default List<EventType> xmlDeviationsStringToEventTypes(AttributeValue av){
        List<String> vals = ValueProcessing.decodeStringList(av.getAttributeValue());
        return new ArrayList<>(vals.stream().map(v -> asDeviationEventType(av.getAttributeId(), v)).collect(Collectors.toList()));
    }

    default EventType asDeviationEventType(String id, String label){
        id = concat("-",id, Utils.urlEncode(label));
        label = composeLabel(HAZARD, label);
        return as(id, label, EventType.class, Vocabulary.s_c_unsafe_event);
    }

    default void handleEventTypeParts(Activity in, EventType out){
        if(in.getStartEvent() != null){
            out.getTypes().add(cz.cvut.kbss.datatools.bpm2stampo.voc.Vocabulary.c_ProcessStart);
            out.setLabel("start");
        }else if(in.getEndEvent() != null){
            out.getTypes().add(cz.cvut.kbss.datatools.bpm2stampo.voc.Vocabulary.c_ProcessEnd);
            out.setLabel("end");
        }else if(in.getRoute() != null){
            out.getTypes().add(cz.cvut.kbss.datatools.bpm2stampo.voc.Vocabulary.c_flowControlEventType);
        }else if(in.getIntermediateEvent() != null){
//            out.setLabel(composeLabel(ACTIVITY, out.getLabel()));
            out.getTypes().add(cz.cvut.kbss.datatools.bpm2stampo.voc.Vocabulary.c_intermediateEvent);
            if(in.getImplementationTask() != null) {
                Set<String> components = getAndInit(out::getComponents, out::setComponents);
                String eventPart = iri(in.getIntermediateEvent());
                components.add(eventPart);
            }
        }else if(in.getImplementationSubFlow() != null){
            out.setLabel(composeLabel(ACTIVITY, out.getLabel()));
            if(in.getSubProcess() != null){
                Set<String> superClasses = getAndInit(out::getSuperClasses, out::setSuperClasses);
                String superClass = iri(in.getSubProcess());
                superClasses.add(superClass);
            }
        }else{
            out.setLabel(composeLabel(ACTIVITY, out.getLabel()));
        }
    }

    @AfterAllMappings
    default void handleActivityEvents(){
        // extract organizational structure from org unit operational model defined in diagrams named orgChart

    }

    default void constructAndComposeActuatorStructure(ActionControlConnection ac, AttributeValue av){
        List<String> vals = ValueProcessing.decodeStringList(av.getAttributeValue());
        String id = av.getAttributeId() + "-actuator-";

        Set<String> components = getAndInit(ac::getComponents, ac::setComponents);
        Set<String> connections = getAndInit(ac::getConnections, ac::setConnections);

        vals.stream().forEach(v -> {
            String aid = id + Utils.urlEncode(v);
            StampObject a = as(aid, v, StampObject.class, Vocabulary.s_c_sensor);
            ActionControlConnection ac1 = as(id + "-conn-to-actuator", String.format("to actuator \"%s\"", v) , ActionControlConnection.class);
            ActionControlConnection ac2 = as(id + "-conn-from-actuator", String.format("from actuator \"%s\"", v), ActionControlConnection.class);

            ac1.setFrom(ac.getFrom());
            ac1.setTo(a.getIri());
            ac2.setFrom(a.getIri());
            ac1.setTo(ac.getTo());

            components.add(a.getIri());
            connections.add(ac1.getIri());
            connections.add(ac2.getIri());
        });
    }

    default void constructAndComposeSensorStructure(FeedbackControlConnection fb, AttributeValue av){
        List<String> vals = ValueProcessing.decodeStringList(av.getAttributeValue());
        String id = av.getAttributeId() + "-sensor-";

        Set<String> components = getAndInit(fb::getComponents, fb::setComponents);
        Set<String> connections = getAndInit(fb::getConnections, fb::setConnections);
        vals.forEach(v -> {
            String sid = id + Utils.urlEncode(v);
            StampObject s = as(sid, v, StampObject.class, Vocabulary.s_c_sensor);
            FeedbackControlConnection fb1 = as(id + "-conn-to-sensor", String.format("to sensor \"%s\"", v) , FeedbackControlConnection.class);
            ActionControlConnection fb2 = as(id + "-conn-from-sensor", String.format("from sensor \"%s\"", v), ActionControlConnection.class);

            fb1.setFrom(fb.getFrom());
            fb1.setTo(s.getIri());
            fb2.setFrom(s.getIri());
            fb1.setTo(fb.getTo());

            components.add(s.getIri());
            connections.add(fb1.getIri());
            connections.add(fb2.getIri());
        });
    }

//    default void constructAndComposeDeviations(Activity inActivity, EventType outActivity, ControlStructure cs){
//        AttributeValue devAttr = inActivity.getElementAttributeValues().getDeviations();
//        if(devAttr == null)
//            return;
//
//        String devStrings = devAttr.getAttributeValue();
//        if(devStrings == null || devStrings.trim().isEmpty())
//            return;
//        String id = devAttr.getAttributeId();
//        Set<String>  activityComponents = getAndInit(outActivity::getComponents, outActivity::setComponents);
//        Set<String>  csCapabilities = getAndInit(cs::getCapabilities, cs::setCapabilities);
//
//        for(String label : ValueProcessing.decodeStringList(devAttr.getAttributeValue())){
//            EventType deviation = asDeviationEventType(id, label);
//            Capability deviationCapability = asDeviationCapability(id, label);
//            deviationCapability.setManifestation(deviation.getIri());
//            activityComponents.add(deviation.getIri());
//            csCapabilities.add(deviationCapability.getIri());
//        }
//    }

//    @RootMapping
//    @AttributeValueQualifier(field = "attributeId", acceptedValues = "5ad36dfb-ed0b-4519-b1cc-dce6cbcee08e")
//    default List<Capability> xmlDeviationToCapability(AttributeValue av){
//        List<String> vals = ValueProcessing.decodeStringList(av.getAttributeValue());
//        return new ArrayList<>(vals.stream().map(v -> asDeviationCapability(av.getAttributeId(), v)).collect(Collectors.toList()));
//    }

    default Capability asDeviationCapability(String id, String label){
        id = concat("-", CAPABILITY, id, Utils.urlEncode(label));
        label = composeLabel(CAPABILITY, label);
        return as(id, label, Capability.class, Vocabulary.s_c_unsafe_control_capability);
    }

//    @RootMapping
//    @AttributeValueQualifier(field = "attributeId", acceptedValues = "469c5dc2-d945-4f85-ba98-d0c42869d886") // Control loop mapping
//    default ControlStructure xmlControlLoopToControlStructure(AttributeValue av){
//        if(av.getAttributeId() == null)
//            return null;
//        String attributeValue = av.getAttributeValue();
//        String label = null;
//        if(attributeValue != null){
//            List<String> vals = ValueProcessing.decodeStringList(attributeValue);
//            if(vals != null && !vals.isEmpty()) {
//                label = vals.get(0);
//            }
//        }
//
//        if(label == null)
//            label = "CS - " + System.currentTimeMillis();
//
//        return asControlStructure(av.getAttributeId(), label);
//    }

// TODO - is it Ok to remove this method?
//
//    default EventType asEventType(String id, String label){
//        id = concat("-",id, Utils.urlEncode(label));
//
//        return as(id, label, EventType.class, Vocabulary.s_c_risk_event);
//    }

    default ControlStructure asControlStructure(String id, String label){
        return as("control-structure-" + id + "-" + Utils.urlEncode(label), label, ControlStructure.class);
    }

    default <T extends Identifiable> T as(String id, String label, Class<T> cls, String ... types){
        String iri = baseIri + id;
        T t = (T) get(iri);
        if(t == null) {
            try {
                t = cls.newInstance();
            } catch (IllegalAccessException e) {
                LOG.error("",e);
            } catch (InstantiationException e) {
                LOG.error("", e);
            }
            t.setIri(iri);
            if(label != null && !label.isEmpty())
                t.setLabel(label);

            for(String type : types)
                t.getTypes().add(type);
            getRegistry().put(t.getIri(), (Identifiable) t);
        }
        return t;
    }


//    @AfterMapping
//    default void xmlToCapability(Activity in, Capability out){
//        if(in.getElementAttributeValues() == null)
//            return;
//
//        if()
//    }




    @RootMapping
    @AddTypes(types = {TRANSITION_IRI})
    @Mapping(source="from", target = "from")
    @Mapping(source="to", target = "to")
    NextConnection xmlToNextConnection(Transition in);

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    //// IRI generator definitions ////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////

    @Named(GROUP)
    default Set<String> toGroupIRIs(Collection<? extends BaseEntity> xmlObjects){
        return iris(xmlObjects, new String[]{GROUP}, emptyStringArray);
    }

    @Named(CAPABILITY)
    default Set<String> toCapabilityIRIs(Collection<? extends BaseEntity> xmlObjects){
        return iris(xmlObjects, new String[]{CAPABILITY}, emptyStringArray);
    }


    default List<String> extendedValueAttributeToList(String value){
        return ValueProcessing.decodeStringList(value);
    }

//    default String label(String value, String postfix){
//        return String.format("%s - %s", value, postfix);
//    }
}
