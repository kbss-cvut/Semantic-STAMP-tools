/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.kbss.datatools.xmlanalysis.lkpr;

import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;
import cz.cvut.kbss.datatools.xmlanalysis.common.AttributesPerElement;
import cz.cvut.kbss.datatools.xmlanalysis.common.XMLCollections;
import cz.cvut.kbss.datatools.xmlanalysis.common.graphml.ActivityGMLBuilder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 *
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
public class LKPRProcessModelAnalysis {
    
    private static final Logger LOG = LoggerFactory.getLogger(LKPRProcessModelAnalysis.class);
    protected static AttributesPerElement apel = new AttributesPerElement();
    protected static SetMultimap<String, Node> nodesByParrentPath = Multimaps.newSetMultimap(new HashMap<>(), () -> {return new HashSet<>();} );
    
    protected static PrintStream out;
    
    public static void elementAttributeSummary(Document doc){
        out.println("Element Attribute Summary");
        out.println("----------------------------------------------------------------------------------------");
        Element el = doc.getDocumentElement();
        
//        String elementName = "INSTANCE";
        Map<String, List<Node>> elementsByName = apel.elementsByName(el);
        for(String name : elementsByName.keySet() ){
            Map<String, List<Node>> attributeMap = apel.getElementAttributes(el, name);
            attributeMap.entrySet().forEach(e -> out.println(String.format("%s - %s - %d", name, e.getKey(), e.getValue().size())));
        }
        out.println();
    }
    
    public static void childParentSummaryTree(Document doc){
        out.println("Child Parent Summary Tree");
        out.println("----------------------------------------------------------------------------------------");
        Element el = doc.getDocumentElement();
        String rootPath = ".";
        nodesByParrentPath.put(rootPath, el);
        childParentSummaryTree(el, rootPath);
        Map<String,Collection<Node>> nodesPerPath = nodesByParrentPath.asMap();
        nodesPerPath.entrySet().stream()
                .sorted((e1, e2) -> e1.getKey().compareTo(e2.getKey()))
                .forEach(e -> out.println(toString(e)) );
        out.println();
    }
    
    protected static String toString(Map.Entry<String,Collection<Node>> e){
        int size = e.getValue().size();
        if(size == 1 && e.getValue().iterator().next() == null){
            size = 0;
        }
        return String.format("[%7d] - %s", size, e.getKey());
    }
    
    
    public static void childParentSummaryTree(Node n, String currentPath){
//        nodesByParrentPath.put(currentPath, Arrays.asList(n));
        String newCurrentPath = String.format("%s/%s", currentPath, n.getNodeName());
        
        List<Node> childNodes = XMLCollections.asStream(n.getChildNodes())
        .filter(node -> (node instanceof Element)).collect(Collectors.toList());
        if(childNodes.isEmpty()){
            nodesByParrentPath.put(newCurrentPath, null);
        }else{
            nodesByParrentPath.putAll(newCurrentPath, childNodes);
        }
        childNodes.forEach(cn -> childParentSummaryTree(cn, newCurrentPath));
//        childParentSummaryTree(
//                
//                , currentPath
//        );
    }
    
    public static void elementsWithAttributeNameSummary(Document doc, String attributeName){
        out.println(String.format("Elements With \"%s\"Attribute Summary", attributeName));
        out.println("----------------------------------------------------------------------------------------");
        Element el = doc.getDocumentElement();
//        String path = "//*[@class]";
        String path = String.format("//*[@%s]", attributeName);
        
        Function<Node,String> toStr = n -> 
                n.getNodeName() + " - " + 
                n.getAttributes().getNamedItem(attributeName) + " - " + 
                n.getAttributes().getNamedItem("name");
        Map<String, List<Node>> nodesPerElementClass = XMLCollections.asStream(apel.nodesWithPrefixPathName(el, path, "")) 
                .sorted((n1, n2) -> toStr.apply(n1).compareTo(toStr.apply(n2)))
                .collect(Collectors.groupingBy(n -> toStr.apply(n)));
        nodesPerElementClass.entrySet().stream()
                .sorted((e1, e2) -> e1.getKey().compareTo(e2.getKey()))
                .forEach(e -> out.println(String.format("[%7d] - %s", e.getValue().size(), e.getKey())));
        out.println("");
    }
    
    
    public static void generateGraphML(Document d, String outputFileName) throws IOException{
        ActivityGMLBuilder b = new ActivityGMLBuilder();
        // create - nodes
        Element el = d.getDocumentElement();
        
        int[] i = new int[]{0}; 
        Set<String> set = new HashSet<>();
        Consumer<Node> activity = n -> {
            String activityName = n.getAttributes().getNamedItem("name").getTextContent().trim();
            set.add(activityName);
            b.addActivity(activityName);
        };
        Consumer<Node> circle = n -> {
            String activityName = n.getAttributes().getNamedItem("name").getTextContent().trim();
            set.add(activityName);
            b.addCircle(activityName);
        };
        
        Consumer<Node> upTriangle = n -> {
            String activityName = n.getAttributes().getNamedItem("name").getTextContent().trim();
            set.add(activityName);
            b.addUpTriangle(activityName);
        };
        Consumer<Node> downTriangle = n -> {
            String activityName = n.getAttributes().getNamedItem("name").getTextContent().trim();
            set.add(activityName);
            b.addDownTriagle(activityName);
        };
        Consumer<Node> branch = n -> {
            String activityName = n.getAttributes().getNamedItem("name").getTextContent().trim();
            set.add(activityName);
            b.addBranch(activityName);
        };
        XMLCollections.asStream(apel.nodesWithPrefixPathName(el, "//INSTANCE[@class='Activity']", "")).forEach(activity);
        XMLCollections.asStream(apel.nodesWithPrefixPathName(el, "//INSTANCE[@class='End']", "")).forEach(circle);
        XMLCollections.asStream(apel.nodesWithPrefixPathName(el, "//INSTANCE[@class='Process start']", "")).forEach(circle);
        XMLCollections.asStream(apel.nodesWithPrefixPathName(el, "//INSTANCE[@class='Decision']", "")).forEach(branch);
        XMLCollections.asStream(apel.nodesWithPrefixPathName(el, "//INSTANCE[@class='Parallelity']", "")).forEach(upTriangle);
        XMLCollections.asStream(apel.nodesWithPrefixPathName(el, "//INSTANCE[@class='Merging']", "")).forEach(downTriangle);
        
        out.println("Generating GML graph ...");
        out.println("Nodes inserted into GML graph:");
        out.println("------------------------ ----------------------------------------------------------------");
        set.forEach(s -> out.println("\t" + s));
        out.println();
        out.println("Report Edge processing into GML graph:");
        out.println("(+ = edge inserted, - = edge not inserted) (bool, bool) indicates wheather source/target nodes are processed");
        out.println("----------------------------------------------------------------------------------------");
        XMLCollections.asStream(apel.nodesWithPrefixPathName(el, "//CONNECTOR[@class='Subsequent']", "")).forEach(
                n -> {
                    List<Node> nodes = XMLCollections.asList(n.getChildNodes());
                    String from = nodes.stream().filter(nc -> "FROM".equals(nc.getNodeName()))
                            .map(nc -> nc.getAttributes().getNamedItem("instance").getTextContent()).findAny().orElse(null).trim();
                    String to = nodes.stream().filter(nc -> "TO".equals(nc.getNodeName()))
                            .map(nc -> nc.getAttributes().getNamedItem("instance").getTextContent()).findAny().orElse(null).trim();
                    String edgeReport = String.format("(from, to) - (%s,%s) - (%s,%s) ", 
                            set.contains(from), set.contains(to),
                            from, to);
//                    System.out.println("from = " + from);
//                    System.out.println("to = " + to);
//                    System.out.println(set.contains(from));
//                    System.out.println(set.contains(to));
                    if(b.getG().getVertex(from) != null && b.getG().getVertex(to) != null  ){
                        edgeReport = " - " + edgeReport;
//                        System.out.println("adding an edge");
                        b.addEdgeSafe(""+i[0]++, from, to, "");
                        
                    }else{
                        edgeReport = " + " + edgeReport;
                    }
                    
                    out.println(edgeReport);
                    
                }
        );
        
//        Vertex v = b.addBranch("haha");
//        b.addActivity("hahaha");
//        b.addEdgeSafe("1", "haha"," hahaha", "");
        PrintStream os = new PrintStream(new FileOutputStream(outputFileName), true, "ASCII");
        b.write(os);
    }
    
    
    /**
     * This method is designed to work with XML documents with adoxml31.dtd.
     * @param inputProcessModel a xml file containing a process description 
     * @param outputDir a the name of a gml file with the process model 
     */
    public static void analyzeLKPRProcessModel(String inputProcessModel, String outputDir){
        try {
            
            // configuration
            File inputFile = new File(inputProcessModel);
            String fileName = inputFile.getName();
            fileName = fileName.substring(0,fileName.lastIndexOf('.'));
            File odir = new File(outputDir);
            if(!odir.exists()){
                odir.mkdir();
            }
            String gmlOuputFile = outputDir + "/" + fileName + ".gml";
            String xmlAnalysisReportFile = outputDir + "/" + fileName + "-xml-analysis.txt";

            
            // load xml file
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(inputProcessModel));
            
            out = new PrintStream(xmlAnalysisReportFile);
            // xml analysis
            xmlAnalysis(doc, xmlAnalysisReportFile);
            // generate 
            generateGraphML(doc, gmlOuputFile);
            out.close();
            out = null;
            
        } catch (IOException | ParserConfigurationException | SAXException ex) {
            LOG.error("",ex);
        }
    }
    
    
    public static void xmlAnalysis(Document doc, String xmlAnalysisReportFile) throws FileNotFoundException {
        
        // xml analysis
        elementAttributeSummary(doc);
        childParentSummaryTree(doc);
        elementsWithAttributeNameSummary(doc, "class");
        elementsWithAttributeNameSummary(doc, "type");
        
    }
    
    /**
     * This method analyzeLKPRProcessModel to generate report for the lkpr-process-models\aerobridge docking.xml
     */
    public static void lkprAeroBridgeDockingProcessModelAnalysis(){
        analyzeLKPRProcessModel("lkpr-process-models/aerobridge docking.xml", "lkpr-process-models/output");
    }
    
    /**
     * This method analyzeLKPRProcessModel to generate report for the lkpr-process-models\aerobridge go home.xml
     */
    public static void lkprAeroBridgeGoHomeProcessModelAnalysis(){
        analyzeLKPRProcessModel("lkpr-process-models/aerobridge go home.xml", "lkpr-process-models/output");
    }
    
    /**
     * This calls analyzeLKPRProcessModel to generate report for the lkpr-process-models\TEST 24_2.xml
     */
    public static void lkprTEST_24_2ProcessModelAnalysis(){
        analyzeLKPRProcessModel("lkpr-process-models/TEST 24_2.xml", "lkpr-process-models/output");
    }
    
    /**
     * This calss analyzeLKPRProcessModel to generate report for the lkpr-process-models\AandS included.xml
     */
    public static void lkprAandS_IncludedProcessModelAnalysis(){
        analyzeLKPRProcessModel("lkpr-process-models/AandS included.xml", "lkpr-process-models/output");
    }
    
    
    
    
    public static void main(String[] args){
        lkprTEST_24_2ProcessModelAnalysis();
        lkprAandS_IncludedProcessModelAnalysis();
        
        lkprAeroBridgeDockingProcessModelAnalysis();
        lkprAeroBridgeGoHomeProcessModelAnalysis();
    }
}
