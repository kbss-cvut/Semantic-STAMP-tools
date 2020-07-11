/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.kbss.datatools.bpm2stampo.common;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
public class AttributesPerElement {
    
    private static final Logger LOG = LoggerFactory.getLogger(AttributesPerElement.class);
    
    protected XPath xpath = XPathFactory.newInstance().newXPath();

    /**
     * This method constructs and evaluates an xpath from the given elFrom element.
     * The evaluated xpath is constructed by concatenating the inputs pathPrefix and name.
     * @param elFrom
     * @param pathPrefix
     * @param name
     * @return 
     */
    public NodeList nodesWithPrefixPathName(Element elFrom, String pathPrefix, String name){
        try {
            return (NodeList)xpath.compile(pathPrefix + name).evaluate(elFrom, XPathConstants.NODESET);
        } catch (XPathExpressionException ex) {
            LOG.error("", ex);
        }
        return null;
    }
    
    public NodeList allNodesWithName(Element elFrom, String name){
        return nodesWithPrefixPathName(elFrom, "//", name);
    }
    
    public NodeList allDirectNodesWithName(Element elFrom, String name){
        return nodesWithPrefixPathName(elFrom, "/", name);
    }
    
    public Map<String, List<Node>> elementsByName(Element elFrom){
        NodeList nl = allNodesWithName(elFrom, "*");
        return Optional.ofNullable(nl).map(l -> elementsByName(l)).orElse(null);
    }
    
    public Map<String, List<Node>> directElementsByName(Element elFrom){
        NodeList nl = allDirectNodesWithName(elFrom, "*");
        return Optional.ofNullable(nl).map(l -> elementsByName(l)).orElse(null);
    }
    
    protected Map<String, List<Node>> elementsByName(NodeList nl){
        return elementsByName(XMLCollections.asStream(nl));
    }
    
    protected Map<String, List<Node>> elementsByName(Stream<Node> nodeStream){
        return nodeStream.sorted((n1,n2) -> n1.getNodeName().compareTo(n2.getNodeName()))
                .collect(Collectors.groupingBy(a -> a.getNodeName()));
    }
    
    public  Map<String, List<Node>> getElementAttributes(Element elFrom, String elementName){
        NodeList nodeList = allNodesWithName(elFrom, elementName);
        return Optional.ofNullable(nodeList).map(nl -> attributeSummaryByName(nl)).orElse(null);
    }
    
    protected Map<String, List<Node>> attributeSummaryByName(NodeList nl){
        return elementsByName(XMLCollections.asStream(nl)
                .flatMap(n -> XMLCollections.asStream(n.getAttributes()))
        );
    }
    
    
//    public void getElementAttributes(Node n){
//        XMLCollections.asIterator(n.getAttributes()).forEachRemaining(
//                
//        );
//    }
    
    public static void main(String[] args) {
    }
}
