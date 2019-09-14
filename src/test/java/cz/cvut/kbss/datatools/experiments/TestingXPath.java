package cz.cvut.kbss.datatools.experiments;

import cz.cvut.kbss.datatools.xmlanalysis.XMLProcessor;
import cz.cvut.kbss.datatools.xmlanalysis.common.Utils;
import cz.cvut.kbss.datatools.xmlanalysis.common.XMLCollections;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;

import static cz.cvut.kbss.datatools.xmlanalysis.common.XPathUtils.ExtractStr;

public class TestingXPath {

    private static final Logger LOG = LoggerFactory.getLogger(TestingXPath.class);

    @Test
    public void experiment2(){
        IntToIntFunc f = i -> i + 1;
        Function<Integer,Integer> fa = i -> i + 1;
        IntToIntFunc fb = f;
        Function<Integer,Integer> fc = f;


        int i = 1;
        System.out.println(String.format("IntToIntFunc(%d) = %d", i, f.apply(i)));
    }

    public static interface IntToIntFunc extends Function<Integer, Integer>{};

    @Test
    public void experiment1(){
        Properties props = Utils.loadProperties("/lkpr-adonis-input.properties");
        TMPXMLProcessor p = new TMPXMLProcessor();
        p.setConfig(props);
        p.processSources();
    }

    @Test
    public void experiment3(){
        Properties props = Utils.loadProperties("/lkpr-adonis-input.properties");
        XMLProcessor p = new XMLProcessor(){

            @Override
            public void process(Document doc) {
                this.doc = doc;
                root = doc.getDocumentElement();

                String pathPattern = "//ATTRIBUTE[@name='%s' and @type='STRING']/text()";

                String path = String.format(pathPattern, "Particular responsibilities");
                String parent = "//parent::INSTANCE";
                XPathExpression parentX = compile(parent);
                try {
                    List<Node> nodes = XMLCollections.asList((NodeList) compile(path).evaluate(root, XPathConstants.NODESET));
                    for(Node n: nodes){
                        Node p = (Node)parentX.evaluate(n, XPathConstants.NODE);
                        if(p != null) {
                            System.out.println(String.format("Parent node \"%s\":%s (%s)",
                                        Optional.ofNullable(p.getAttributes())
                                                .map( am -> am.getNamedItem("name"))
                                                .map(a -> a.getTextContent()).orElse("?"),
                                    Optional.ofNullable(p.getAttributes())
                                            .map( am -> am.getNamedItem("class"))
                                            .map(a -> a.getTextContent()).orElse("?"),
                                        Optional.ofNullable(p.getAttributes())
                                                .map( am -> am.getNamedItem("id"))
                                                .map(a -> a.getTextContent()).orElse("?")
                                    )
                            );
                        }

                        System.out.println(n.getLocalName());
                        System.out.println(n.getBaseURI());
                        System.out.println(n.getNamespaceURI());
                        System.out.println(n.getParentNode());
                        System.out.println(n.getTextContent());

                    }
                }catch(XPathExpressionException e){
                    LOG.error("",e);
                }

            }

            @Override
            public void postProcess() {

            }
        };
        p.setConfig(props);
        p.processSources();

    }





    public static class TMPXMLProcessor extends XMLProcessor{

        // This declaration compiles the xpaths, the document schema is not considered see XMLProcessor.xpath

        protected final String instancePattern = "//INSTANCE[@class='%s']";
        protected String instancePath(String cls){return String.format(instancePattern, cls);}



        protected ExtractStr
            extName = extractString("@name"),
            extId = extractString("@id");

        @Override
        public void process(Document doc) {
            LOG.info("Processing a new file...");
            LOG.info("                        ");
            this.doc = doc;
            this.root = doc.getDocumentElement();

            String cls = "Employee";
//            String xp = String.format(, cls);
            List<Node> nodes = null;

            String attrXpath = "@id";
            for (Node n : nodes ){
                try {
                    String name = extName.apply(n).orElse("no name");
                    String id = extId.apply(n).orElse("no Id");
                    System.out.println(String.format("Employee \"%s\" (%s)", name , id) );
                }catch (Exception e){
                    LOG.error("", e);
                }
            }
        }

        @Override
        public void postProcess() {

        }


    }
}
