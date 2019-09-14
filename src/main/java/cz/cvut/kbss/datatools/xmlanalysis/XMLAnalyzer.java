package cz.cvut.kbss.datatools.xmlanalysis;

import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;
import cz.cvut.kbss.datatools.xmlanalysis.common.AttributesPerElement;
import cz.cvut.kbss.datatools.xmlanalysis.common.Utils;
import cz.cvut.kbss.datatools.xmlanalysis.common.XMLCollections;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class XMLAnalyzer extends XMLProcessor{

    private static final Logger LOG = LoggerFactory.getLogger(XMLAnalyzer.class);
    protected AttributesPerElement apel = new AttributesPerElement();
    protected SetMultimap<String, Node> nodesByParentPath = Multimaps.newSetMultimap(new HashMap<>(), () -> {return new HashSet<>();} );

    protected PrintStream out;

    public XMLAnalyzer() {}

    public XMLAnalyzer(String propertyPath) {
        this(Utils.loadProperties(propertyPath));
    }

    public XMLAnalyzer(Properties config) {
        setConfig(config);
    }

    public Properties getConfig() {
        return config;
    }


    public void elementAttributeSummary(Document doc){
        sectionTitle("Element Attribute Summary");
        Element el = doc.getDocumentElement();

//        String elementName = "INSTANCE";
        Map<String, List<Node>> elementsByName = apel.elementsByName(el);
        for(String name : elementsByName.keySet() ){
            Map<String, List<Node>> attributeMap = apel.getElementAttributes(el, name);
            attributeMap.entrySet().forEach(e -> out.println(String.format("%s - %s - %d", name, e.getKey(), e.getValue().size())));
        }
        out.println();
    }


    ///////////////////////////////////////////////////////////////////////////////////////
    // Child parent tree Summary Tree//////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////
    public void childParentSummaryTree(Document doc){
        sectionTitle("Child Parent Summary Tree");
        Element el = doc.getDocumentElement();
        String rootPath = ".";
        nodesByParentPath.put(rootPath, el);
        childParentSummaryTree(el, rootPath);
        Map<String,Collection<Node>> nodesPerPath = nodesByParentPath.asMap();
        nodesPerPath.entrySet().stream()
                .sorted((e1, e2) -> e1.getKey().compareTo(e2.getKey()))
                .forEach(e -> out.println(toStringWithAdjustedSize(e)) );
        out.println();
    }

    protected String toStringWithAdjustedSize(Map.Entry<String,Collection<Node>> e){
        int size = e.getValue().size();
        if(size == 1 && e.getValue().iterator().next() == null){
            size = 0;
        }
        return String.format("[%7d] - %s", size, e.getKey());
    }

    protected String toString(Map.Entry<? extends Object,? extends Number> e){
        return String.format("[%7d] - %s", e.getValue(), e.getKey());
    }

    public void childParentSummaryTree(Node n, String currentPath){
//        nodesByParrentPath.put(currentPath, Arrays.asList(n));
        String newCurrentPath = String.format("%s/%s", currentPath, n.getNodeName());

        List<Node> childNodes = XMLCollections.asStream(n.getChildNodes())
                .filter(node -> (node instanceof Element)).collect(Collectors.toList());
        if(childNodes.isEmpty()){
            nodesByParentPath.put(newCurrentPath, null);
        }else{
            nodesByParentPath.putAll(newCurrentPath, childNodes);
        }
        childNodes.forEach(cn -> childParentSummaryTree(cn, newCurrentPath));
//        childParentSummaryTree(
//
//                , currentPath
//        );
    }


    ///////////////////////////////////////////////////////////////////////////////////////
    // Three paths summary ////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////
    public void pathSummary(Document doc){
        sectionTitle("Three paths summary");
        Element el = doc.getDocumentElement();
        List<List<Node>> paths = listPathsFrom(el);
        paths.stream().map(this::toPathString)
                .collect(Collectors.groupingBy(s -> s, Collectors.counting()))
                .entrySet().stream()
                .sorted(Comparator.comparing(e -> e.getKey()))
                .forEach(e -> out.println(toString(e)));

        out.println();
    }

    protected Stream<Element> streamDescendants(Element el){
        if(el == null)
            return Stream.of();
        if(el.getChildNodes().getLength() == 0)
            return Stream.of(el);
        Stream<Element> s1 = Stream.of(el);
        List<Element> chs = new ArrayList<>();
        for(int i = 0; i < el.getChildNodes().getLength(); i++){
            Node n = el.getChildNodes().item(i);
            if(n instanceof Element)
                chs.add((Element)n);
        }

        return Stream.concat(s1, chs.stream().flatMap(this::streamDescendants));
    }

//    protected List<Element> getChildNodes()


    protected List<Node> getPathFromRoot(Node node){
        Node n = node;
        List<Node> path = new ArrayList<>();

        while(n != null){
            path.add(n);
            n = n.getParentNode();
        }

        Collections.reverse(path);
        return path;
    }

    protected List<List<Node>> listPathsFrom(Element r){
        Stack<Pair<Node, Integer>> stack = new Stack<>();
        stack.push(Pair.of(r, -1));
        List<List<Node>> paths = new ArrayList<>();
        while(!stack.isEmpty()){
            paths.add(stack.stream().map(p -> p.getLeft()).collect(Collectors.toList()));
            Pair<Node, Integer> n = stack.peek();
            if(n.getLeft().hasChildNodes()){ // go further along the path
                stack.push(Pair.of(n.getLeft().getChildNodes().item(0), 0));
            }else{ // switch path
                while(true) {
                    n = stack.pop();
                    if(stack.isEmpty())
                        break;
                    Pair<Node, Integer> p = stack.peek();
                    int i = n.getRight() + 1;
                    if(i < p.getLeft().getChildNodes().getLength()){
                        stack.push(Pair.of(p.getLeft().getChildNodes().item(i), i));
                        break;
                    }
                }
            }

        }
        return paths;
    }

    protected String toPathString(List<? extends Node> path){
        return path.stream().map(n -> ((Node) n).getNodeName()).collect(Collectors.joining("/"));
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    // Elements with path summary /////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////
    public void elementsWithAttributeNameSummary(Document doc, String attributeName){
        sectionTitle(String.format("Elements With \"%s\"Attribute Summary", attributeName));
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


    public void generateGraphML(Document d, File outputFileName) throws IOException {
//        ActivityGMLBuilder b = new ActivityGMLBuilder();
//        // create - nodes
//        Element el = d.getDocumentElement();
//
//        int[] i = new int[]{0};
//        Set<String> set = new HashSet<>();
//        Consumer<Node> activity = n -> {
//            String activityName = n.getAttributes().getNamedItem("name").getTextContent().trim();
//            set.add(activityName);
//            b.addActivity(activityName);
//        };
//        Consumer<Node> circle = n -> {
//            String activityName = n.getAttributes().getNamedItem("name").getTextContent().trim();
//            set.add(activityName);
//            b.addCircle(activityName);
//        };
//
//        Consumer<Node> upTriangle = n -> {
//            String activityName = n.getAttributes().getNamedItem("name").getTextContent().trim();
//            set.add(activityName);
//            b.addUpTriangle(activityName);
//        };
//        Consumer<Node> downTriangle = n -> {
//            String activityName = n.getAttributes().getNamedItem("name").getTextContent().trim();
//            set.add(activityName);
//            b.addDownTriagle(activityName);
//        };
//        Consumer<Node> branch = n -> {
//            String activityName = n.getAttributes().getNamedItem("name").getTextContent().trim();
//            set.add(activityName);
//            b.addBranch(activityName);
//        };
//        XMLCollections.asStream(apel.nodesWithPrefixPathName(el, "//INSTANCE[@class='Employee']", "")).forEach(activity);
//        XMLCollections.asStream(apel.nodesWithPrefixPathName(el, "//INSTANCE[@class='End']", "")).forEach(circle);
//        XMLCollections.asStream(apel.nodesWithPrefixPathName(el, "//INSTANCE[@class='Process start']", "")).forEach(circle);
//        XMLCollections.asStream(apel.nodesWithPrefixPathName(el, "//INSTANCE[@class='Decision']", "")).forEach(branch);
//        XMLCollections.asStream(apel.nodesWithPrefixPathName(el, "//INSTANCE[@class='Parallelity']", "")).forEach(upTriangle);
//        XMLCollections.asStream(apel.nodesWithPrefixPathName(el, "//INSTANCE[@class='Merging']", "")).forEach(downTriangle);
//
//        out.println("Generating GML graph ...");
//        out.println("Nodes inserted into GML graph:");
//        out.println("------------------------ ----------------------------------------------------------------");
//        set.forEach(s -> out.println("\t" + s));
//        out.println();
//        out.println("Report Edge processing into GML graph:");
//        out.println("(+ = edge inserted, - = edge not inserted) (bool, bool) indicates wheather source/target nodes are processed");
//        out.println("----------------------------------------------------------------------------------------");
//        XMLCollections.asStream(apel.nodesWithPrefixPathName(el, "//CONNECTOR[@class='Connector']", "")).forEach(
//                n -> {
//                    List<Node> nodes = XMLCollections.asList(n.getChildNodes());
//                    String from = nodes.stream().filter(nc -> "FROM".equals(nc.getNodeName()))
//                            .map(nc -> nc.getAttributes().getNamedItem("instance").getTextContent()).findAny().orElse(null).trim();
//                    String to = nodes.stream().filter(nc -> "TO".equals(nc.getNodeName()))
//                            .map(nc -> nc.getAttributes().getNamedItem("instance").getTextContent()).findAny().orElse(null).trim();
//                    String edgeReport = String.format("(from, to) - (%s,%s) - (%s,%s) ",
//                            set.contains(from), set.contains(to),
//                            from, to);
////                    System.out.println("from = " + from);
////                    System.out.println("to = " + to);
////                    System.out.println(set.contains(from));
////                    System.out.println(set.contains(to));
//                    if(b.getG().getVertex(from) != null && b.getG().getVertex(to) != null  ){
//                        edgeReport = " - " + edgeReport;
////                        System.out.println("adding an edge");
//                        b.addEdgeSafe(""+i[0]++, from, to, "");
//
//                    }else{
//                        edgeReport = " + " + edgeReport;
//                    }
//
//                    out.println(edgeReport);
//
//                }
//        );
//
////        Vertex v = weBuilder.addBranch("haha");
////        weBuilder.addActivity("hahaha");
////        weBuilder.addEdgeSafe("1", "haha"," hahaha", "");
//        PrintStream os = new PrintStream(new FileOutputStream(outputFileName), true, "ASCII");
//        b.write(os);
    }



    /**
     * This method is designed to work with XML documents with adoxml31.dtd.
     * @param doc a xml Document object (adoxml31.dtd compliant) containing a BPMN diagram description.
     */
    @Override
    public void process(Document doc) {
        File gmlOuputFile = new File(outputDirAnalysis, fileName + ".gml");
        File xmlAnalysisReportFile = new File(outputDirAnalysis, fileName + "-xml-analysis.txt");
        try {
            out = new PrintStream(xmlAnalysisReportFile);
            // xml analysis
            analysisXMLDocument(doc, xmlAnalysisReportFile);
            // generate
            generateGraphML(doc, gmlOuputFile);
            out.flush();
            out.close();
            out = null;

        } catch (IOException ex) {
            LOG.error("",ex);
        }
    }

    @Override
    public void postProcess() {
        // do nothing
    }

    public void analysisXMLDocument(Document doc, File xmlAnalysisReportFile) throws FileNotFoundException {

        // xml analysis
        pathSummary(doc);
        elementAttributeSummary(doc);
        childParentSummaryTree(doc);
        elementsWithAttributeNameSummary(doc, "class");
        elementsWithAttributeNameSummary(doc, "type");

    }

    //////////////////////////////////////////////////////////////////////////////////////
    // Printing methods //////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////

    protected void sectionTitle(String txt){
        out.println();
        out.println("----------------------------------------------------------------------------------------");
        out.println(txt);
        out.println("----------------------------------------------------------------------------------------");
        out.println();
    }
}
