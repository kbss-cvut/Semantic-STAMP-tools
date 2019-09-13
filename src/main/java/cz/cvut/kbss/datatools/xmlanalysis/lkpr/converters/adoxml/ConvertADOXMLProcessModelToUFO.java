/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.kbss.datatools.xmlanalysis.lkpr.converters.adoxml;

import cz.cvut.kbss.adoxml.MODEL;
import cz.cvut.kbss.datatools.xmlanalysis.XMLProcessor;
import cz.cvut.kbss.datatools.xmlanalysis.common.Utils;
import cz.cvut.kbss.datatools.xmlanalysis.common.XMLCollections;
import cz.cvut.kbss.datatools.xmlanalysis.common.jena.GraphPatternUtils;
import cz.cvut.kbss.datatools.xmlanalysis.voc.Vocabulary;
import org.apache.jena.rdf.model.*;
import org.apache.jena.shared.PrefixMapping;
import org.apache.jena.shared.impl.PrefixMappingImpl;
import org.apache.jena.vocabulary.RDF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
public class ConvertADOXMLProcessModelToUFO extends XMLProcessor {
    
    private static final Logger LOG = LoggerFactory.getLogger(ConvertADOXMLProcessModelToUFO.class);
    public static final String[] EMPTY_STRING_ARRAY = new String[0];

    public static String ENCODING = "UTF-8";

//    protected Map<String, Node> instanceMap;

    protected Resource defaultController;
    protected Resource defaultSensor;
    protected Resource defaultActuator;
    protected Resource defaultRisk;


    protected Map<Node, Model> models;
    protected PrefixMapping pm;
    protected String ns = Vocabulary.lkprNS;
    protected URIFactory uf;

    public ConvertADOXMLProcessModelToUFO() {}

    public ConvertADOXMLProcessModelToUFO(String propertyPath) {
        this(Utils.loadProperties(propertyPath));
    }

    public ConvertADOXMLProcessModelToUFO(Properties config) {
        setConfig(config);
    }


    public List<Model> getModels() {
        return new ArrayList<>(models.values());
    }

//    protected void addToInstanceMap(String cls, String name, Node n){
//        String key = cls + "-" + name;
//        Node oldNode = instanceMap.put(key,n);
//        if(oldNode != null){
//            LOG.warn("Corrupted instance map. Found nodes with the same key \"{}\", " +
//                            "\nold node: \n{}, \n\nnew node:\n{}", key, oldNode, n);
//        }
//    }
//
//    protected Node getInstance(String cls, String name){
//        String key = cls + "-" + name;
//        return instanceMap.get(key);
//    }
    

//    public Function<String, ExtractNodes> nodeByClass(String xpathClassPattern){
//        return cls -> nodes(String.format(xpathClassPattern, cls));
//    }

    @Override
    public void postProcess() {
        for(Map.Entry<Node, Model> entry : models.entrySet()) {
            Model model = entry.getValue();
            if(model == null || model.isEmpty())
                continue;
            String modelName = Optional.ofNullable(entry.getKey())
                    .map(n -> getAttrValue(n,"modeltype") + "-" + getAttrValue(n,"name"))
                    .orElse("default");
            File output = new File(getOutputDir(), fileName + "-" + modelName + ".ttl");
            try (FileOutputStream fos = new FileOutputStream(output)) {

                model.write(fos, "TTL");
            } catch (Exception e) {
                LOG.error("Writing converted model for file \"{}\" failed!", inputFile.getAbsolutePath());
            }
        }
    }

    /**
     * Convert a adoxml Document to jena model containing UFO-STAMP. The converter process the following modeltypes
     * (in adoxml the model type is represented as the modeltype attribute in the MODEL element):
     *
     *   - Business process model
     *   - Working environment model
     *   - Risk Model TODO
     *
     * @param doc
     * @see ConvertADOXMLProcessModelToUFO#processSource(File)
     * @return a jena model containing the UFO-STAMP
     */
    @Override
    public void process(Document doc){

        // prepare converter fields
        this.doc = doc;
        root = doc.getDocumentElement();

        // prepare prefix map
        pm = new PrefixMappingImpl();
        pm.setNsPrefixes(PrefixMapping.Standard);
        pm.setNsPrefixes(Vocabulary.getPrefixMapping());
        // initialize jena model, used to store the results of the conversion
        models = new HashMap<>();
        models.put(null, null);

        for(Node modelNode : getNodeList("//MODEL")) {
            Model model = ModelFactory.createDefaultModel();
            models.put(modelNode, model);
            model.setNsPrefixes(pm);
        }

//        // init instance map
//        instanceMap = new HashMap<>();

        uf = new URIFactory(Vocabulary.lkprNS);

        // create new defaults
        defaultController = ResourceFactory.createResource(Vocabulary.lkprNS + "controller/default-" + System.currentTimeMillis());
        defaultSensor = ResourceFactory.createResource(Vocabulary.lkprNS + "sensor/default-" + System.currentTimeMillis());
        defaultActuator = ResourceFactory.createResource(Vocabulary.lkprNS + "actuator/default-" + System.currentTimeMillis());
        defaultRisk = ResourceFactory.createResource(Vocabulary.lkprNS + "risk/default-" + System.currentTimeMillis());
        // begin conversion
        convert();
    }
    
    /**
     * This method implements the actual conversion of xml nodes to UFO-STAMP rdf model.
     * 
     * See the lkpr-process-models/README.txt for documentation/examoples 
     * about the transofrmation.
     */
    protected void convert(){
        processEntities();
        processRelations();
    }

    /**
     * Root method for converting entities, e.g. Employee, Role, Risk
     */
    public void processEntities(){
        processInstances();
//        processSensorsAndActuators(); // implemented in processInstances
    }
    
    
    /**
     * Converts entities represented as an INSTANCE element
     */
    public void processInstances(){
//        List<String> classes = Arrays.asList(
//                "Swimlane (vertical)",
//                "Employee",
//                "Process start",
//                "End",
//                "Decision",
//                "Merging",
//                "Parallelity",
//                "Organizational unit",
//                "Performer",
//                "Role",
//                "Aggregation",
//                "Risk",
//                "Trigger",
//                "Subprocess",
//                "Cross-reference");
        processInstances("Swimlane (vertical)", Vocabulary.c_Swimlane, Vocabulary.c_EventType);
        processInstances("Employee", Vocabulary.c_Employee, Vocabulary.c_Agent);
        processInstances("Process start", Vocabulary.c_ProcessStart, Vocabulary.c_EventType);
        processInstances("End", Vocabulary.c_ProcessEnd, Vocabulary.c_EventType);
        processInstances("Decision", Vocabulary.c_Decision, Vocabulary.c_EventType);
        processInstances("Merging", Vocabulary.c_Merging, Vocabulary.c_EventType);
        processInstances("Parallelity", Vocabulary.c_Parallelity, Vocabulary.c_EventType);
        processInstances("Organizational unit", Vocabulary.c_Organizational_Unit, Vocabulary.c_Agent);
        processInstances("Performer", Vocabulary.c_Performer, Vocabulary.c_Agent);
        processInstances("Role", Vocabulary.c_Role);
        processInstances("Aggregation", Vocabulary.c_Aggregation);
        processInstances("Risk", Vocabulary.c_Risk);
        processInstances("Trigger", Vocabulary.c_Trigger, Vocabulary.c_EventType);
        processInstances("Subprocess", Vocabulary.c_Subprocess, Vocabulary.c_EventType);
        processInstances("Cross-reference", Vocabulary.c_Cross_reference);
    }

    /**
     * Converts the our custome representation of sensors and actuators in adonis
     */
    public void processSensorsAndActuators(){
        // TODO - sensors and actuators are represented as text content (each line 
        // is a sensor/actuator) of the ATTRIBUTE elements for which the name attribute is:
        //


    }
//
//    /**
//     *
//     * @param attributeType - "Particular responsibilities" -> actuator , "Particular recommendations" -> sensors
//     * @param type
//     */
//    protected void processSensorsAndActuators(String attributeType, String ... type){
//        String pathPattern = "//ATTRIBUTE[@name='%s' and @type='STRING']/text()";
//        String path = String.format(pathPattern, type);
//        String parent = "//parent::INSTANCE";
//        XPathExpression parentX = compile(parent);
//        try {
//            List<Node> nodes = XMLCollections.asList((NodeList) compile(path).evaluate(root, XPathConstants.NODESET));
//            for(Node node: nodes){
//                Node p = (Node)parentX.evaluate(node, XPathConstants.NODE);
//                Resource parentInstance = null;
//                if(p != null) {
//                    NamedNodeMap am = p.getAttributes();
//                    String name = Optional.ofNullable(am.getNamedItem("name")).map(n -> n.getTextContent()).orElse(null);
//                    String id = Optional.ofNullable(am.getNamedItem("id")).map(n -> n.getTextContent()).orElse(null);
//                    String cls = Optional.ofNullable(am.getNamedItem("class")).map(n -> n.getTextContent()).orElse(null);
//                    if(name != null && cls != null){// the converted node should have a value for its name and id attributes
//                        parentInstance = ResourceFactory.createResource(uf.createURIByClassAndName(cls, name));
//                        node.getTextContent();
//
//                    }
//                }else
//                    continue;
//
//
//            }
//        }catch(XPathExpressionException e){
//            LOG.error("",e);
//        }
//    }
//
//    protected void processSensorsAndActuators(Resource parentInstance, String elements, String ropoerty, String ... type){
//
//    }
    
    
    /**
     * Root method for converting relations between entities, e.g. Connector, has role
     */
    public void processRelations(){
        processConnectors();
        processINTERREF();
        processRecord();
    }
    
    /**
     * This method converts relations represented using the INTERREF element
     */
    public void processINTERREF(){
        // TODO processINTERREF
        // see INTERREF, are there an
    }

    public void processRecord(){

    }
    
    /**
     * This method converts relations represented using the CONNECTOR element
     */
    public void processConnectors(){
        //TODO - process also the domain and range of the connectors. The domain and range impose roles to the connected entities.
        processConnector("//CONNECTOR[@class='Connector']", Vocabulary.c_Subsequent, Vocabulary.c_Connector);
        processConnector("//CONNECTOR[@class='Is inside']", Vocabulary.c_Is_inside, Vocabulary.c_Connector);
        // working environment
        processConnector("//CONNECTOR[@class='Is subordinated']", Vocabulary.c_Is_subordinated, Vocabulary.c_Connector);
        processConnector("//CONNECTOR[@class='Belongs to']", Vocabulary.c_BelongsTo, Vocabulary.c_Connector);
        processConnector("//CONNECTOR[@class='Has role']", Vocabulary.c_Has_Role, Vocabulary.c_Connector);
        processConnector("//CONNECTOR[@class='Is manager']", Vocabulary.c_Is_manager, Vocabulary.c_Connector);

        // risk pool
        processConnector("//CONNECTOR[@class='Grouped into (risk)']", Vocabulary.c_Grouped_Into_Risk, Vocabulary.c_Connector);

        // process control loops
        processINTERREF_Resposible("Responsible role");
        processINTERREF_Resposible("Responsible for execution");
//        processConnector("/CONNECTOR/FROM [@class='Belongs to']", Vocabulary.c_EventType);
//        processConnector("//CONNECTOR [@class='Organizational unit']", Vocabulary.c_Agent);
//        processConnector("//CONNECTOR [@class='Performer']", Vocabulary.c_Agent);
//        processConnector("//CONNECTOR [@class='Role']", Vocabulary.c_Role);
//        processConnector("//CONNECTOR [@class='Risk']", Vocabulary.c_Risk);
    }
    
    /**
     * A parametrized conversion of INSTANCE elemnets. The method maps elements 
     * with a given xpath to a specific type.
     * @see ConvertADOXMLProcessModelToUFO#processInstances()
     * @param cls selects elements
     * @param type
     */
    public void processInstances(String cls, String ... type){
        // select the List of nodes matching the xpath
        String xpath = String.format("//INSTANCE[@class='%s']", cls);
        List<Node> nodes = getNodeList(xpath);
        LOG.info("process activities, there are {} activities to be processed", nodes.size());
        
        // a graph pattern to be instantiated 
        String graphPattern = 
                "?instance a ?type;\n"
                + "rdfs:label ?name;"
                + "lkpr-pm:id ?id.";
        
        // process all matching nodes
        for (Node node : nodes){
            if(node.getAttributes() != null){ // the converted node should have attributes
                NamedNodeMap am = node.getAttributes();
                String name = Optional.ofNullable(am.getNamedItem("name")).map(n -> n.getTextContent()).orElse(null);
                String id = Optional.ofNullable(am.getNamedItem("id")).map(n -> n.getTextContent()).orElse(null);
                if(name != null){// the converted node should have a value for its name and id attributes
                    Resource instance = ResourceFactory.createResource(uf.createURIByClassAndName(cls, name));
                    // construct variable to resource mapping to be substituted in the graph pattern
//                    addToInstanceMap(cls, name, node);
                    Map<String,RDFNode> varMap = new HashMap<>();
                    varMap.put("name", ResourceFactory.createStringLiteral(name));
                    varMap.put("id", ResourceFactory.createStringLiteral(id));
                    varMap.put("type", ResourceFactory.createResource(type[0]));
                    varMap.put("instance", instance);
                    
                    // instantiate the pattern and add it to the result model
                    Model model = getNodeModel(node);
                    instantiatePatternAndAddToModel(graphPattern, varMap, model);
                    // add types
                    for(String t : type){
                        model.add(instance, RDF.type, ResourceFactory.createResource(t));
                    }
                }
            }
        }
    }

    public void processConnector(String xpath, String ... type){
        // select the List of nodes matching the xpath
        List<Node> nodes1 = getNodeList(xpath);
        LOG.info("process connectors, there are {} connectors with xpath \"{}\" to be processed", nodes1.size(), xpath);

        // a graph pattern to be instantiated
        String graphPattern =
                "?instance a ?type;\n"
                        + "rdfs:label ?name;"
                        + "bpmn:from ?from;"
                        + "bpmn:to ?to;"
                        + "lkpr-pm:id ?id.";

//        String graphPattern =
//                "?instance rdf:predicate ?predicate;\n"
////                        + "rdfs:label ?name;"
//                        + "lkpr-pm:from ?from;"
//                        + "lkpr-pm:to ?to;"
//                        + "lkpr-pm:id ?id."
//                        + "?from ?predicate ?to.";
        
        // process all matching nodes
        for (Node node : nodes1){
            if(node.getAttributes() != null){ // the converted node should have attributes
                List<Node> nodes = XMLCollections.asList(node.getChildNodes());
                NamedNodeMap am = node.getAttributes();

                String name = getAttrValue(node, "class");//Optional.ofNullable(am.getNamedItem("class")).map(n -> n.getTextContent()).orElse(null);
                String id1 = getAttrValue(node, "id");//Optional.ofNullable(am.getNamedItem("id")).map(n -> n.getTextContent()).orElse(null);
                Node fromNode = nodes.stream().filter(nc -> "FROM".equals(nc.getNodeName())).findFirst().orElse(null);
                if(fromNode == null)
                    continue;
                Node toNode = nodes.stream().filter(nc -> "TO".equals(nc.getNodeName())).findFirst().orElse(null);
                if(toNode == null)
                    continue;

                String fromName = getAttrValue(fromNode, "instance");//.map(nc -> nc.getAttributes().getNamedItem("instance").getTextContent()).findAny().orElse(null).trim();
                String fromClass = getAttrValue(fromNode, "class");
                String toName = getAttrValue(toNode, "instance");//.map(nc -> nc.getAttributes().getNamedItem("instance").getTextContent()).findAny().orElse(null).trim();
                String toClass = getAttrValue(toNode, "class");

                if(id1 != null){// the converted node should have a value for its name and id attributes
                    Resource instance = ResourceFactory.createResource(uf.createURIByName(id1));
                    Resource fromResource = ResourceFactory.createResource(uf.createURIByClassAndName(fromClass, fromName));
                    Resource toResource = ResourceFactory.createResource(uf.createURIByClassAndName(toClass, toName));
                    // construct variable to resource mapping to be substituted in the graph pattern
                    Map<String,RDFNode> varMap = new HashMap<>();
                    varMap.put("name", ResourceFactory.createStringLiteral(name));
                    varMap.put("from", fromResource);
                    varMap.put("to", toResource);
                    varMap.put("id", ResourceFactory.createStringLiteral(id1));
                    varMap.put("type", ResourceFactory.createResource(type[0]));
                    varMap.put("instance", instance);

                    // instantiate the pattern and add it to the result model
                    Model model = getNodeModel(node);
                    instantiatePatternAndAddToModel1(graphPattern, varMap, model);

                    // add types
                    for(String t : type){
                        model.add(instance, RDF.type, ResourceFactory.createResource(t));
                    }
                }
            }
        }
    }

    /**
     * Specific for "Responsible Role" and "Responsible for execution"
     * @param roleType
     */
    public void processINTERREF_Resposible(String roleType){
        LOG.info("processing control loop with role type \"{}\"", roleType);
        String xpathPattern = "./INTERREF[@name='%s']/IREF[@tclassname='Role' and @type='objectreference']";
//        String xpathPattern = "./INTERREF[@name=\"%s\"]";
        // select the List of nodes matching the xpath
        String xpath = String.format(xpathPattern, roleType);
//        LOG.info("process INTERREF connectors, there are {} connectos with xpath \"{}\" to be processed", activities.size(), xpath);

        String activityXpath = "//INSTANCE[@class='Employee']";
        List<Node> activities = getNodeList(activityXpath);

//        // a graph pattern to be instantiated
//        String controlLoopPattern =
//                "?instance a ?type;\n"
//                        + "rdfs:label ?name;"
//                        + "lkpr-pm:from ?from;"
//                        + "lkpr-pm:to ?to;"
//                        + "lkpr-pm:id ?id.";

        LOG.info("ACTIVITIES FOUND {} ", activities.size());
        for(Node activityNode : activities) {
            Model model = getNodeModel(activityNode);
            // "Responsible Role" and "Responsible for execution"


            // 3. get Risks
            List<Node> risks = getRiskInActivity(activityNode);
            List<Resource> riskReosources = risks.stream()
                    .map(rn -> instanceReferenceToResource(rn))
                    .collect(Collectors.toList());

//                    risks.stream()
//                    .map(n -> getAttrValue(n, "tobjname"))
//                    .filter(s -> s != null)
//                    .map(s -> s.trim())
//                    .filter(s -> s.isEmpty())
//                    .map(riskName -> createURIByClassAndName("Risk", riskName))
//                    .map(uri -> URIFactory.createResource(uri))
//                    .collect(Collectors.toList());



            // 1. find role of activity
            List<Node> roleNodes = getNodeList(activityNode, xpath);
            if(roleNodes.isEmpty()) {
                roleNodes.add(null);
            }
            for(Node node : roleNodes) {
                 if(node == null){
                    continue;
                }
                String roleName = getAttrValue(node, "tobjname");
                if(roleName != null){
                    roleName = roleName.trim();
                }
                if(roleName.isEmpty()){
                    continue;
                }

                // create Control Loop Instance
                // id is constructed from activity and role
                String activityName = getAttrValue(activityNode, "name");
                String activityId = getAttrValue(activityNode, "id");
                String id = Stream.of(activityName, activityId, roleName)
                        .collect(Collectors.joining("-"));
                String instanceURI = uf.createURIByName(id);
                String name = id;
                Map<String,RDFNode> varMap = new HashMap<>();
                varMap.put("instance", ResourceFactory.createResource(instanceURI));
                varMap.put("name", ResourceFactory.createStringLiteral(name));
                varMap.put("id", ResourceFactory.createStringLiteral(id));

//            Model m = instantiatePatternAndAddToModel(controlLoopPattern, varMap);
                Model m = ModelFactory.createDefaultModel();
                Resource cl = m.getResource(instanceURI);
                // init properties
                Property controlsFor = ResourceFactory.createProperty(cz.cvut.kbss.onto.safety.stamp.Vocabulary.s_p_controls_for);
                Property designedToPrevent = ResourceFactory.createProperty(cz.cvut.kbss.onto.safety.stamp.Vocabulary.s_p_designed_to_prevent);
                Property hasActuator = ResourceFactory.createProperty(cz.cvut.kbss.onto.safety.stamp.Vocabulary.s_p_has_actuator);
                Property hasSensor = ResourceFactory.createProperty(cz.cvut.kbss.onto.safety.stamp.Vocabulary.s_p_has_sensor);
                Property hasController = ResourceFactory.createProperty(cz.cvut.kbss.onto.safety.stamp.Vocabulary.s_p_has_controller);
                model.add(cl, controlsFor, model.createResource(uf.createURIByName(activityId)));
                model.add(cl, controlsFor, model.createResource(uf.createURIByName(activityId)));


//            LOG.info("CONTROL LOOP : found {} role(s) for activity {}", roleNodes.size(), activityName);
//            String roleName = getAttrValue(roleNode, "tobjname");
//            if(roleName == null) continue;


                // 2. get sensors and actuators
                List<String> sensors = getSensors(roleName);
                List<String> actuators = getActuators(roleName);

                // add risks
                riskReosources.stream()
                        .filter(r -> r != null)
                        .forEach(r -> model.add(cl, designedToPrevent, r));


                // add Actuators
                actuators.stream()
                        .map(rn -> uf.createURIByClassAndName("actuator", rn))
                        .map(uri -> ResourceFactory.createResource(uri))
                        .filter(r -> r != null)
                        .forEach(r -> model.add(cl, hasActuator, r));

                // add Sensors
                sensors.stream()
                        .map(rn -> uf.createURIByClassAndName("sensor", rn))
                        .map(uri -> ResourceFactory.createResource(uri))
                        .filter(r -> r != null)
                        .forEach(r -> model.add(cl, hasSensor, r));

                // add Controller
                String controllerURI = uf.createURIByClassAndName("Role", roleName);
                cl.addProperty(hasController, ResourceFactory.createResource(controllerURI));
            }

        }
        // Cooperation/participation
    }


//    protected List<Node> getRolesInActivity(Node activity){
//        String xpathPattern = "INTERREF[@name=\"%s\"]/IREF[@tclassname=\"Role\" @tobjname=\"role_name\" @type=\"objectreference\"]";
//        List<Node> rolesInActivity = new ArrayList<>();
//        for(String roleType : Arrays.asList("Responsible role", "Responsible for execution")) {
//            String xpath = String.format(xpathPattern, roleType);
//            List<Node> nodes = getNodeList(activity, xpath);
//            rolesInActivity.addAll(nodes);
//        }
//        return rolesInActivity;
//    }

    protected List<String> getSensors(String roleName){
        return getSensorsOrActuators(roleName, "Particular responsibilities");
    }
    protected List<String> getActuators(String roleName){
        return getSensorsOrActuators(roleName, "Particular recommendations");
    }

    protected List<String> getSensorsOrActuators(String roleName, String attributeName){
        String contextXPath = String.format("//INSTANCE[@class=\"Role\" and @name=\"%s\"]", roleName);
        String list = getATTRIBUTEStringValue(contextXPath, attributeName);
        return parseActuatorsSensors(list);
    }


    protected Resource instanceReferenceToResource(Node instanceReference){
        String instanceName = getAttrValue(instanceReference, "tobjname");
        String instanceClass = getAttrValue(instanceReference, "tclassname");
        if(instanceName == null || instanceName.isEmpty() || instanceClass == null || instanceClass.isEmpty()){
            return null;
        }
//        Node instanceNode = getInstance(instanceClass, instanceName);
//        if(instanceNode == null){
//            LOG.warn("Corrupted Instance Map. Could not find risk instance with key with class \"{}-{}\"", instanceClass, instanceName);
//        }
        String ulr = uf.createURIByClassAndName(instanceClass, instanceName);
        return ResourceFactory.createResource(ulr);
    }

    protected String getATTRIBUTEStringValue(String contextXPath, String attributeName){
        String xpath = contextXPath + String.format("/ATTRIBUTE[@name=\"%s\" and @type=\"STRING\"]", attributeName);
        List<Node> nodes1 = getNodeList(xpath);
        if(!nodes1.isEmpty()){
            Node n = nodes1.get(0);
            return n.getTextContent();
        }
        return null;
    }

    protected List<String> parseActuatorsSensors(String str){
        String pattern = "\n\\s*\"";
        String[] strs = str.split(pattern);
        return Stream.of(strs).map(s -> "\"" + s).collect(Collectors.toList());
    }

    // old alternative to parseActuatorsSensors
//    protected List<String> parseListFromStrsing(String list){
//        String[] vals = Optional.ofNullable(list).map(v -> v.split("\n")).orElse(EMPTY_STRING_ARRAY);
//        return Stream.of(vals)
//                .map(v -> v.trim())
//                .filter(v -> !v.isEmpty())
//                .map(v -> v.replaceAll("&quot;", "")).collect(Collectors.toList());
//    }

    protected List<Node> getRiskInActivity(Node activity){
        String xpath = "RECORD[name=\"Risk/control allocation\"]/ROW/INTERREF/IREF[tclassname=\"Risk\" and type=\"objectreference\"]";
        List<Node> nodes = getNodeList(activity, xpath);
        return nodes;
    }

    protected Model getNodeModel(Node n){
        Node modelNode = getModelNode(n);
        if(modelNode != null){
            return models.get(modelNode);
        }
        return null;
    }

    protected Node getModelNode(Node childNode){
        while(childNode != null && !MODEL.class.getSimpleName().equals(childNode.getNodeName())){
            childNode = childNode.getParentNode();
        }
        return childNode;
    }

    /**
     * Utility method to instantiate a graph pattern (create a jena model based 
     * on the graphPattern and varMap) and add it to the global result model
     * @param graphPattern
     * @param varnameMapping 
     */
    public Model instantiatePatternAndAddToModel(String graphPattern, Map<String,RDFNode> varnameMapping, Model model){
        Model activityModel = GraphPatternUtils.instantiatePattern(varnameMapping, graphPattern, pm);
        model.add(activityModel);
        LOG.info("constructed model for of sise {} for elements with xpath ({})", activityModel.size(), xpath);
        return activityModel;
    }

    public Model instantiatePatternAndAddToModel1(String graphPattern, Map<String,RDFNode> varnameMapping, Model model){
        Model activityModel = GraphPatternUtils.instantiatePattern(varnameMapping, graphPattern, pm);
        model.add(activityModel);
        LOG.info("constructed model for of sise {} for elements with xpath ({})", activityModel.size(), xpath);
        return activityModel;
    }

    
    /**
     * This is an example how to call the onversion of adoxml xml file to UFO-STAMP
     * @throws Exception 
     */
    public static void convertExperiment()throws Exception {
        // path to file
//        String input = "lkpr-process-models/TEST 24_2.xml";
//        String input = "lkpr-process-models/Safety TEST onion structure.xml";
//        String input = "lkpr-process-models/AandS included.xml";
        String input = "lkpr-process-models/TEST 24_2.xml/";
        LOG.info("processing file \"{}\"", input);
        // create converter instance
        ConvertADOXMLProcessModelToUFO converter = new ConvertADOXMLProcessModelToUFO();
        // convert to UFO-STAMP
        converter.processSource(new File(input));
//        Model m = converter.getModels().;
//        // write resulting jena model to console
//        m.write(System.out, "TTL");
    }
    
    public static void main(String[] args) throws Exception {
        convertExperiment();
    }

}
