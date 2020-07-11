package cz.cvut.kbss.datatools.bpm2stampo.partners.csat.xmlanalysis;

import cz.cvut.kbss.datatools.bpm2stampo.XMLAnalyzer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BizagiXmlAnalysis extends XMLAnalyzer {

    private static final Logger LOG = LoggerFactory.getLogger(BizagiXmlAnalysis.class);

    public BizagiXmlAnalysis() {
    }

    public BizagiXmlAnalysis(String propertyPath) {
        super(propertyPath);
    }


    public PrintStream getOut(){
        return out;
    }

    public void setOut(PrintStream out){
        this.out = out;
    }
//
//    public void process(String name, Document doc) {
//        fileName = name;
//        process(doc);
//    }

    @Override
    public void analysisXMLDocument(Document doc, File xmlAnalysisReportFile) throws FileNotFoundException {
        resetDataStructures();
        super.analysisXMLDocument(doc, xmlAnalysisReportFile);
        analyzeEventElements(doc, xmlAnalysisReportFile);
    }


    protected void analyzeEventElements(Document doc, File xmlAnalysisReportFile){
        root = doc.getDocumentElement();

        List<Node> eventNodes = getNodeList("//Event");
//        sectionTitle("Event Element Analysis");
        // analyze attributes of event elements
        // analyze paths that lead to Event elements
        sectionTitle("Paths that stem from Events ");
        eventNodes.stream()
//                .filter(n -> n instanceof Element)
                .flatMap(n -> listPathsFrom(n).stream().map(this::toPathString))
                .collect(Collectors.groupingBy(s -> s, Collectors.counting()))
                .entrySet().stream()
                .sorted(Comparator.comparing(e -> e.getKey()))
                .map(e -> String.format("%2d - %s", e.getValue(), e.getKey()))
                .forEach(s -> out.println(s));

        ;
        // analyze paths that stem from an Event element
        sectionTitle("Paths that lead to Events");
        eventNodes.stream()
//                .filter(n -> n instanceof Element)
                .map(n -> getPathFromRoot(n))
                .map(this::toPathString)
                .collect(Collectors.groupingBy(s -> s, Collectors.counting()))
                .entrySet().stream()
                .sorted(Comparator.comparing(e -> e.getKey()))
                .map(e -> String.format("%2d - %s", e.getValue(), e.getKey()))
                .forEach(s -> out.println(s));

    }

    protected void analyzeExtendedAttributes(Document doc, File xmlAnalysisReportFile){
        File extendedAttributeFile = new File(this.inputFile.getParent(), "ExtendedAttributeValues.xml");

    }



    public static void main(String[] args) {
        XMLAnalyzer xmlAnalyzer = new BizagiXmlAnalysis("/csat-bizagi-input.properties");
        xmlAnalyzer.processSources();
    }
}
