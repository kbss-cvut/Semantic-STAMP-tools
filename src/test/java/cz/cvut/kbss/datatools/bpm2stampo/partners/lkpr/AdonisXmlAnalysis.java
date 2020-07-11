package cz.cvut.kbss.datatools.bpm2stampo.partners.lkpr;

import cz.cvut.kbss.datatools.bpm2stampo.XMLAnalyzer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AdonisXmlAnalysis extends XMLAnalyzer{
    private static final Logger LOG = LoggerFactory.getLogger(AdonisXmlAnalysis.class);

    public AdonisXmlAnalysis() {
    }

    public AdonisXmlAnalysis(String propertyPath) {
        super(propertyPath);
    }

    @Override
    public void analysisXMLDocument(Document doc, File xmlAnalysisReportFile) throws FileNotFoundException {
        super.analysisXMLDocument(doc, xmlAnalysisReportFile);
        elementsWithAttributeNameSummary(doc, "modeltype");
        elementsWithAttributeNameSummary(doc, "name");
        analyzeIREFElement(doc);
    }
    ///////////////////////////////////////////////////////////
    // analize usage of IREF element //////////////////////////
    ///////////////////////////////////////////////////////////
    public void analyzeIREFElement(Document doc){
        sectionTitle("Analyzing IREF element usage");
        Element r = doc.getDocumentElement();
        List<Node> nodes = getNodeList(r,"//IREF");

        nodes.stream().map(this::reportIrefNodeIntsanceAndModel).forEach(s -> out.println("\t" + s));
        out.println();

        sectionTitle("Analyzing IREF tclassname summary");
        nodes.stream().map(this::reportIrefNodeTclassNameIntsanceAndModel)
                .collect(Collectors.groupingBy(s -> s, Collectors.counting()))
                .entrySet().stream()
                .sorted(Comparator.comparing(e -> e.getKey()))
                .forEach(e -> out.println(toString(e)));
        out.println();
    }



    protected String reportIrefNodeIntsanceAndModel(Node n){
        Node interref = getNode(n, "ancestor::INTERREF");
        Node record = getNode(n, "ancestor::RECORD");
        Node instance = getNode(n, "ancestor::INSTANCE");
        Node model = getNode(n, "ancestor::MODEL");

        return String.format(
                "model[modeltype='%s'] - instance[class='%s'] -%s interref[name='%s'] - " +
                        "Iref[tmodeltype='%s', tclassname='%s', tmodelver='%s', tmodelname='%s', type='%s', tobjname='%s' ]",
                        getAttrValue(model, "modeltype"),
                        getAttrValue(instance, "class"),
                        record != null ? String.format(" RECORD[name=%s] - ",getAttrValue(record,"name")) : "",
                        getAttrValue(interref, "name"),
                        getAttrValue(n, "tmodeltype"),
                        getAttrValue(n, "tclassname"),
                        getAttrValue(n, "tmodelver"),
                        getAttrValue(n, "tmodelname"),
                        getAttrValue(n, "type"),
                        getAttrValue(n, "tobjname")
                );
    }

    protected String reportIrefNodeTclassNameIntsanceAndModel(Node n){
        Node interref = getNode(n, "ancestor::INTERREF");
        Node record = getNode(n, "ancestor::RECORD");
        Node instance = getNode(n, "ancestor::INSTANCE");
        Node model = getNode(n, "ancestor::MODEL");
        return String.format(
                "model[modeltype='%s'] - instance[class='%s'] -%s interref[name='%s'] - " +
                        "Iref[tmodeltype='%s', tclassname='%s']",
                getAttrValue(model, "modeltype"),
                getAttrValue(instance, "class"),
                record != null ? String.format(" RECORD[name=%s] - ",getAttrValue(record,"name")) : "",
                getAttrValue(interref, "name"),
                getAttrValue(n, "tmodeltype"),
                getAttrValue(n, "tclassname")
        );
    }

    ///////////////////////////////////////////////////////////
    // Analize usage of IREF element //////////////////////////
    ///////////////////////////////////////////////////////////


    public static void main(String[] args) {
        AdonisXmlAnalysis xmlAnalyzer = new AdonisXmlAnalysis("/lkpr-adonis-input.properties");
        xmlAnalyzer.processSources();
    }

}
