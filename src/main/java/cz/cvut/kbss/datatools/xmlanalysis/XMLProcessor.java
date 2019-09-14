package cz.cvut.kbss.datatools.xmlanalysis;

import cz.cvut.kbss.datatools.xmlanalysis.common.XMLCollections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;
import java.io.File;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cz.cvut.kbss.datatools.xmlanalysis.common.XPathUtils.*;


/**
 * A XML file processor. Two new abstract methods are introduced.
 * <p/>
 * 1. {@link #process(Document)} - where document is the parsed XML file
 * and postProcess. Called in {@link #processSource(File)} implementation
 * <br/>
 * 2. {@link #postProcess()} - called after the {@link #process(Document)}
 */
public abstract class XMLProcessor extends SourceProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(XMLProcessor.class);

    protected Document doc;
    protected Node root;
    protected XPath xpath = XPathFactory.newInstance().newXPath();
    protected File inputFile;
    protected String fileName;


    public Document getDoc() {
        return doc;
    }

    public Node getRoot() {
        return root;
    }

    public File getInputFile() {
        return inputFile;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public File[] listSources(){
        return sourceDir.listFiles(c -> c.getName().toLowerCase().endsWith(".xml"));
    }

    /**
     * Convert the inputFile, into java xml Document object and pass it to the process(Document) method, postProcess()
     *      * method is called after the process(Document) has finished.
     * @param inputFile
     */
    public void processSource(File inputFile) {
        // parse the xml file into Document and pass it to the convert(Document) method
        try{
            this.inputFile = inputFile;
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(inputFile);
            fileName = inputFile.getName();
            fileName = fileName .substring(0, fileName .lastIndexOf("."));
            process(doc);
            postProcess();
        }catch (Exception e) {
            LOG.error("Conversion failed!", e);
        }
    }


    public abstract void process(Document doc);
    public abstract void postProcess();

    public XPathExpression compile(String expression) {
        try {
            return xpath.compile(expression);
        }catch (XPathExpressionException ex){
            LOG.error("Cannot parse xpath, {}", expression, ex);
        }
        return null;
    }

    protected Node getNode(Node root, String path){
        try {
            return (Node) xpath.compile(path).evaluate(root, XPathConstants.NODE);
        } catch (XPathExpressionException e) {
            LOG.error("", e);
        }
        return null;
    }

    /**
     * Utility method to select list of nodes from the converted document using
     * xpath.
     * @param path
     * @return
     */
    public List<Node> getNodeList(String path){
        return getNodeList(root, path);
    }

    public List<Node> getNodeList(Node root, String path){
        List<Node> ret = Collections.EMPTY_LIST;
        try {
            LOG.info("finding node list in root = {} and xpath = {}", root, path);
            ret = XMLCollections.asList((NodeList)xpath.compile(path).evaluate(root, XPathConstants.NODESET));

        } catch (XPathExpressionException ex) {
            LOG.error("xpath failed, \"{}\"", path, ex);
        }
        return new ArrayList(ret);
    }

    public String getAttrValue(Node n, String attributeName){
        return Optional.ofNullable(n.getAttributes())
                .map(as -> as.getNamedItem(attributeName))
                .map(a -> a.getTextContent())
                .map(a -> a.trim()).orElse(null);
    }

    public ExtractStr extractString(String xpath){
        return extractStr(compile(xpath));
    }

    public ExtractNum extractNumber(String xpath){
        return extractNum(compile(xpath));
    }

    public ExtractBool extractBoolean(String xpath){
        return extractBool(compile(xpath));
    }

    public ExtractNodes nodes(String xpath){
        return extractNodes(compile(xpath));
    }




    protected class TripleExtractor{
        protected String xpathString;
        protected XPathExpression xp;

        public TripleExtractor(String xpathString) {
            this.xpathString = xpathString;
            this.xp = compile(xpathString);
        }

        public void process(){
            for(Field f : getFields()){

            }
        }

        protected List<Field> getFields(){
            Set<Field> fieldSet = new HashSet<>();
            List<Class> classes = getSuperClasses();

            for(Class cls : classes){
                Set<Field> clsFields = Stream.of(cls.getDeclaredFields())
                        .filter(f -> Function.class.isAssignableFrom(f.getType()))
                        .collect(Collectors.toCollection(HashSet::new));
                clsFields.removeAll(fieldSet);
                fieldSet.addAll(clsFields);
            }
            return new ArrayList<>(fieldSet);
        }

        protected List<Class> getSuperClasses(){
            Class cls = this.getClass();
            List<Class> ret = new ArrayList<>();
            while(cls != TripleExtractor.class) {
                ret.add(cls);
                cls = this.getClass().getSuperclass();
            }
            return ret;
        }
    }

}
