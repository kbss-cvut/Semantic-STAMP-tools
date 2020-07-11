/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.kbss.datatools.bpm2stampo.voc;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.shared.impl.PrefixMappingImpl;

/**
 * 
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
public class Vocabulary {
    public static PrefixMappingImpl prefixMapping = getPrefixMapping();

    public final static String ufoNS = "http://onto.fel.cvut.cz/ontologies/ufo/";
    public final static String ufoPrfix = "ufo";
    public final static String lkprNS = "http://onto.fel.cvut.cz/ontologies/aviation/airport/lkpr/pm/";
    public final static String lkprPrefix = "lkpr-pm";
    public final static String sptampNS = "http://onto.fel.cvut.cz/ontologies/stamp/";
    public final static String stampPrefix = "stamp";
    public final static String bpmnNS = "http://onto.fel.cvut.cz/ontologies/bpmn/";
    public final static String bpmnPrefix = "bpmn";


    public final static String c_EventType = "http://onto.fel.cvut.cz/ontologies/ufo/event-type";
    public final static Resource j_c_EventType = ResourceFactory.createResource(c_EventType);
    public final static String c_Agent = "http://onto.fel.cvut.cz/ontologies/ufo/Agent";
    public final static Resource j_c_Agent = ResourceFactory.createResource(c_Agent);
    public final static String c_Disposition = "http://onto.fel.cvut.cz/ontologies/ufo/disposition";
    public final static Resource j_c_Disposition = ResourceFactory.createResource(c_Disposition);
    public final static String c_Sensor = "http://onto.fel.cvut.cz/ontologies/aviation/airport/lkpr/pm/Sensor";
    public final static Resource j_c_Sensor = ResourceFactory.createResource(c_Sensor);
    public final static String c_Actuator = "http://onto.fel.cvut.cz/ontologies/aviation/airport/lkpr/pm/Actuator";
    public final static Resource j_c_Actuator = ResourceFactory.createResource(c_Actuator);

//    public final static String p_partOfCluster = "http://onto.fel.cvut.cz/ontologies/dataset-descriptor/part-of-cluster";
//    public final static Property j_p_partOfCluster = URIFactory.createProperty(p_partOfCluster);
    public final static String p_hasSensor = "http://onto.fel.cvut.cz/ontologies/stamp/has-sensor";
    public final static Property j_p_hasSensor = ResourceFactory.createProperty(p_hasSensor);
    public final static String p_hasController = "http://onto.fel.cvut.cz/ontologies/stamp/has-controller";
    public final static Property j_p_hasController = ResourceFactory.createProperty(p_hasController);
    public final static String p_hasActuator = "http://onto.fel.cvut.cz/ontologies/stamp/has-actuator";
    public final static Property j_p_hasActuator = ResourceFactory.createProperty(p_hasActuator);
    public final static String p_controlsFor = "http://onto.fel.cvut.cz/ontologies/stamp/controls-for";
    public final static Property j_p_controlsFor = ResourceFactory.createProperty(p_controlsFor);
    public final static String p_designedToPrevent = "http://onto.fel.cvut.cz/ontologies/stamp/designed-to-prevent";
    public final static Property j_p_designedToPrevent = ResourceFactory.createProperty(p_designedToPrevent);


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //// BPMN classes and properties ///////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
    public final static String c_flowControlEventType = "http://onto.fel.cvut.cz/ontologies/bpmn/flow-control-event-type";
    public final static Resource j_c_flowControlEventType = ResourceFactory.createResource(c_flowControlEventType);
    public final static String c_Swimlane = "http://onto.fel.cvut.cz/ontologies/bpmn/swimlane";
    public final static Resource j_c_Swimlane = ResourceFactory.createResource(c_Swimlane);
    public final static String c_Activity = "http://onto.fel.cvut.cz/ontologies/bpmn/activity";
    public final static Resource j_c_Activity = ResourceFactory.createResource(c_Activity);
    public final static String c_Subprocess = "http://onto.fel.cvut.cz/ontologies/bpmn/subprocess";
    public final static Resource j_c_Subprocess = ResourceFactory.createResource(c_Subprocess);
    public final static String c_ProcessStart = "http://onto.fel.cvut.cz/ontologies/bpmn/process-start";
    public final static Resource j_c_ProcessStart = ResourceFactory.createResource(c_ProcessStart);
    public final static String c_ProcessEnd = "http://onto.fel.cvut.cz/ontologies/bpmn/process-end";
    public final static Resource j_c_ProcessEnd = ResourceFactory.createResource(c_ProcessEnd);
    public final static String c_intermediateEvent = "http://onto.fel.cvut.cz/ontologies/bpmn/intermediate-event";
    public final static Resource j_c_intermediateEvent = ResourceFactory.createResource(c_intermediateEvent);
    public final static String c_Decision = "http://onto.fel.cvut.cz/ontologies/bpmn/decision";
    public final static Resource j_c_Decision = ResourceFactory.createResource(c_Decision);
    public final static String c_Parallelity = "http://onto.fel.cvut.cz/ontologies/bpmn/decision";
    public final static Resource j_c_Parallelity = ResourceFactory.createResource(c_Parallelity);
    public final static String c_Merging = "http://onto.fel.cvut.cz/ontologies/bpmn/merging";
    public final static Resource j_c_Merging = ResourceFactory.createResource(c_Merging);
    public final static String c_Trigger = "http://onto.fel.cvut.cz/ontologies/bpmn/trigger";
    public final static Resource j_c_Trigger = ResourceFactory.createResource(c_Trigger);
    public final static String c_Connector = "http://onto.fel.cvut.cz/ontologies/bpmn/Connector";
    public final static Resource j_c_Connector = ResourceFactory.createResource(c_Connector);

    public final static String c_Organizational_Unit = "http://onto.fel.cvut.cz/ontologies/bpmn/organizational-unit";
    public final static Resource j_c_Organizational_Unit = ResourceFactory.createResource(c_Organizational_Unit);
    public final static String c_Employee = "http://onto.fel.cvut.cz/ontologies/bpmn/employee";
    public final static Resource j_c_Employee = ResourceFactory.createResource(c_Employee);
    public final static String c_Performer = "http://onto.fel.cvut.cz/ontologies/bpmn/performer";
    public final static Resource j_c_Performer = ResourceFactory.createResource(c_Performer);
    public final static String c_Role = "http://onto.fel.cvut.cz/ontologies/bpmn/role";
    public final static Resource j_c_Role = ResourceFactory.createResource(c_Role);
    public final static String c_Aggregation = "http://onto.fel.cvut.cz/ontologies/bpmn/aggregation";
    public final static Resource j_c_Aggregation = ResourceFactory.createResource(c_Aggregation);
    public final static String c_Risk = "http://onto.fel.cvut.cz/ontologies/bpmn/risk";
    public final static Resource j_c_Risk = ResourceFactory.createResource(c_Risk);

    public final static String p_from = "http://onto.fel.cvut.cz/ontologies/bpmn/from";
    public final static Property j_p_from = ResourceFactory.createProperty(p_from);
    public final static String p_to = "http://onto.fel.cvut.cz/ontologies/bpmn/to";
    public final static Property j_p_to = ResourceFactory.createProperty(p_to);

    public final static String c_Cross_reference = "http://onto.fel.cvut.cz/ontologies/bpmn/cross-reference";
    public final static Resource j_c_Cross_reference = ResourceFactory.createResource(c_Cross_reference);

    // connectors
    public final static String c_Subsequent = "http://onto.fel.cvut.cz/ontologies/bpmn/subsequent";
    public final static Resource j_c_Subsequent = ResourceFactory.createResource(c_Subsequent);
    public final static String c_Is_subordinated = "http://onto.fel.cvut.cz/ontologies/bpmn/is-subordinated";
    public final static Resource j_c_Is_subordinated = ResourceFactory.createResource(c_Is_subordinated);
    public final static String c_BelongsTo = "http://onto.fel.cvut.cz/ontologies/bpmn/belongs-to";
    public final static Resource j_c_BelongsTo = ResourceFactory.createResource(c_BelongsTo);
    public final static String c_Has_Role = "http://onto.fel.cvut.cz/ontologies/bpmn/has-role";
    public final static Resource j_c_Has_Role = ResourceFactory.createResource(c_Has_Role);
    public final static String c_Grouped_Into_Risk = "http://onto.fel.cvut.cz/ontologies/bpmn/grouped-into-risk";
    public final static Resource j_c_Grouped_Into_Risk = ResourceFactory.createResource(c_Grouped_Into_Risk);
    public final static String c_Responsible_role = "http://onto.fel.cvut.cz/ontologies/bpmn/responsible-role";
    public final static Resource j_c_Responsible_role = ResourceFactory.createResource(c_Responsible_role);
    public final static String c_Responsible_for_execution = "http://onto.fel.cvut.cz/ontologies/bpmn/responsible-for-execution";
    public final static Resource j_c_Responsible_for_execution = ResourceFactory.createResource(c_Responsible_for_execution);
    public final static String c_Is_inside= "http://onto.fel.cvut.cz/ontologies/bpmn/is-inside";
    public final static Resource j_c_Is_inside = ResourceFactory.createResource(c_Is_inside);
    public final static String c_Is_manager = "http://onto.fel.cvut.cz/ontologies/bpmn/is-manager";
    public final static Resource j_c_Is_manager = ResourceFactory.createResource(c_Is_manager);

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //// Conceptual Modelling //////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public final static String c_unwantedEvent = "http://onto.fel.cvut.cz/ontologies/stamp/unwanted-event";
    public final static Resource j_c_unwantedEvent = ResourceFactory.createResource(c_unwantedEvent);


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //// RDF CONCEPTS AND PROPERTIES ///////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public final static String p_sub_class_of = "http://www.w3.org/2000/01/rdf-schema#subClassOf";

    /**
     * @param uri
     * @return the prefixed version of the uri
     */
    public static String shortVersion(String uri){
        return prefixMapping.shortForm(uri);
//        return Vocabulary.felddPrefix + ":" + lrdType.substring(Vocabulary.felddNS.length());
    }

    /**
     *
     * @param uri
     * @return the local name of the uri
     */
    public static String localName(String uri){
        String localName = prefixMapping.shortForm(uri);
        if(!localName.equals(uri)){
            localName = localName.substring(localName.indexOf(':') + 1);
        }
        return localName;
//        return Vocabulary.felddPrefix + ":" + lrdType.substring(Vocabulary.felddNS.length());
    }

    /**
     * @return the prefix mapping for namespaces used in this vocabulary class.
     */
    public static PrefixMappingImpl getPrefixMapping(){
        if(prefixMapping == null){
            prefixMapping = new PrefixMappingImpl();
            prefixMapping.setNsPrefix(ufoPrfix, ufoNS);
            prefixMapping.setNsPrefix(lkprPrefix, lkprNS);
            prefixMapping.setNsPrefix(stampPrefix, sptampNS);
            prefixMapping.setNsPrefix(bpmnPrefix, bpmnNS);
        }
        return prefixMapping;
    }
    
}
