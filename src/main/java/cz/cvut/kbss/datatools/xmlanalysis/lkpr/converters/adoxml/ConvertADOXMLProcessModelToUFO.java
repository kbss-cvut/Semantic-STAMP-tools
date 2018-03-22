/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.kbss.datatools.xmlanalysis.lkpr.converters.adoxml;

import cz.cvut.kbss.datatools.xmlanalysis.common.XMLCollections;
import cz.cvut.kbss.datatools.xmlanalysis.common.jena.ExperimentGraphPattern;
import cz.cvut.kbss.datatools.xmlanalysis.common.jena.GraphPatternUtils;
import cz.cvut.kbss.datatools.xmlanalysis.voc.Vocabulary;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.shared.PrefixMapping;
import org.apache.jena.shared.impl.PrefixMappingImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
public class ConvertADOXMLProcessModelToUFO {
    
    private static final Logger LOG = LoggerFactory.getLogger(ConvertADOXMLProcessModelToUFO.class);
    public static String ENCODING = "UTF-8";
    
    protected Document doc;
    protected Node root;
    protected Model model;
    protected PrefixMapping pm;
    protected XPath xpath = XPathFactory.newInstance().newXPath();
    protected String ns = Vocabulary.lkprNS;
    
    
    public String createURIByName(String instanceName) {
        try {
            return ns + URLEncoder.encode(instanceName, ENCODING);
        } catch (UnsupportedEncodingException ex) {
            LOG.error("", ex);
            throw new RuntimeException(ex);
        }
    }
    
    
    /**
     * Convert the inputFile, an xml file with adoxml scheme, to a jena model containing UFO-STAMP.
     * The converter sports the following modeltypes (in adoxml the model type is represented as the modeltype attribute in the MODEL element):
     *   - Business process model
     *   - Working environment model
     *
     * @param inputFile
     * @return a jena model containing the UFO-STAMP
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException 
     */
    public Model convert(String inputFile) throws ParserConfigurationException, SAXException, IOException{
        // parse the xml file into Document and pass it to the convert(Document) method
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new File(inputFile));
        return convert(doc);
    }
    
    
    /**
     * Convert a adoxml Document to jena model con=taining UFO-STAMP.
     * @param doc
     * @see convert(String)
     * @return a jena model containing the UFO-STAMP
     */
    public Model convert(Document doc){
        
        // prepare converter fields
        this.doc = doc;
        root = doc.getDocumentElement();
        // initialize jena model, used to store the results of the conversion
        model = ModelFactory.createDefaultModel();
        // add prefixes to model
        pm = new PrefixMappingImpl();
        pm.setNsPrefixes(PrefixMapping.Standard);
        pm.setNsPrefixes(Vocabulary.getPrefixMapping());
        model.setNsPrefixes(pm);
        // begin conversion
        convert();
        
        return model;
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
     * Root method for converting entities, e.g. Activity, Role, Risk
     */
    public void processEntities(){
        processInstances();
        processSensorsAndActuators();
    }
    
    
    /**
     * Converts entities represented as an INSTANCE element
     */
    public void processInstances(){
        processInstances("//INSTANCE[@class='Activity']", Vocabulary.c_EventType);
        processInstances("//INSTANCE[@class='Process start']", Vocabulary.c_EventType);
        processInstances("//INSTANCE[@class='End']", Vocabulary.c_EventType);
        processInstances("//INSTANCE[@class='Decision']", Vocabulary.c_EventType);
        processInstances("//INSTANCE[@class='Merging']", Vocabulary.c_EventType);
        processInstances("//INSTANCE[@class='Parallelity']", Vocabulary.c_EventType);
        processInstances("//INSTANCE[@class='Organizational unit']", Vocabulary.c_Agent);
        processInstances("//INSTANCE[@class='Performer']", Vocabulary.c_Agent);
        processInstances("//INSTANCE[@class='Role']", Vocabulary.c_Role);
        processInstances("//INSTANCE[@class='Risk']", Vocabulary.c_Risk);
    }
    
    /**
     * Converts the our custome representation of sensors and actuators in adonis
     */
    public void processSensorsAndActuators(){
        // TODO - sensors and actuators are represented as text content (each line 
        // is a sensor/actuator) of the ATTRIBUTE elements for which the name attribute is:
        // actuator -> Particular responsibilities
        // sensors -> Particular recommendations
        
    }
    
    
    /**
     * Root method for converting relations between entities, e.g. Subsequent, has role
     */
    public void processRelations(){
        processConnectors();
        processINTERREF();
    }
    
    /**
     * This method converts relations represented using the INTERREF element
     */
    public void processINTERREF(){
        // TODO
        // see INTERREF, are there an
    }
    
    /**
     * This method converts relations represented using the CONNECTOR element
     */
    public void processConnectors(){
        // TODO 
        // CONNECTOR element
        // its children FROM and TO elements
    }
    
    /**
     * A parametrized conversion of INSTANCE elemnets. The method maps elements 
     * with a given xpath to a specific type.
     * @see processInstances()
     * @param xpath selects elements
     * @param type 
     */
    public void processInstances(String xpath, String type){
        // select the List of nodes matching the xpath
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
                    // construct variable to resource mapping to be substituted in the graph pattern
                    Map<String,RDFNode> varMap = new HashMap<>();
                    varMap.put("name", ResourceFactory.createStringLiteral(name));
                    varMap.put("id", ResourceFactory.createStringLiteral(id));
                    varMap.put("type", ResourceFactory.createResource(type));
                    varMap.put("instance", ResourceFactory.createResource(createURIByName(name)));
                    
                    // instantiate the pattern and add it to the result model
                    instantiatePatternAndAddToModel(graphPattern, varMap);
                }
            }
        }
    }
    
    /**
     * Utility method to instantiate a graph pattern (create a jena model based 
     * on the graphPattern and varMap) and add it to the global result model
     * @param graphPattern
     * @param varnameMapping 
     */
    public void instantiatePatternAndAddToModel(String graphPattern, Map<String,RDFNode> varnameMapping){
        Model activityModel = GraphPatternUtils.instantiatePattern(varnameMapping, graphPattern, pm);
        model.add(activityModel);
        LOG.info("constructed model for of sise {} for elements with xpath ({})", activityModel.size(), xpath);
    }
    
    
    /**
     * Utility method to select list of nodes from the converted document using 
     * xpath.
     * @param path
     * @return 
     */
    public List<Node> getNodeList(String path){
        try {
            return XMLCollections.asList((NodeList)xpath.compile(path).evaluate(root, XPathConstants.NODESET));
        } catch (XPathExpressionException ex) {
            LOG.error("",ex);
        }
        return Collections.EMPTY_LIST;
    }
    
    /**
     * This is an example how to call the onversion of adoxml xml file to UFO-STAMP
     * @throws Exception 
     */
    public static void convertExperiment()throws Exception {
        // path to file
        String input = "lkpr-process-models/TEST 24_2.xml";
        // create converter instance
        ConvertADOXMLProcessModelToUFO converter = new ConvertADOXMLProcessModelToUFO();
        // convert to UFO-STAMP
        Model m = converter.convert(input);
        // write resulting jena model to console
        m.write(System.out, "TTL");
    }
    
    public static void main(String[] args) throws Exception {
        convertExperiment();
    }

}
