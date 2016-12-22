package cz.cvut.kbss.reporting.service.data.eccairs;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.sun.org.apache.xml.internal.dtm.ref.DTMNodeList;
import com.sun.org.apache.xpath.internal.jaxp.XPathFactoryImpl;
import net.minidev.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class EccairsQueryUtils {

    public String setFileNumberValue(final String query, final String value) {
        Document document = parse(query);

        if ( document == null ) {
            return null;
        }

        final Node nValues = xpathSingle(document,
                "/QUERY/RESTRICTIONS/CRITERION[./ATTRIBUTE[@ID=452 and @ENTITY_PATH=24]]/VALUES"
        );

        final Element newValue = document.createElement("VALUE");
        newValue.setAttribute("VALUETYPE", "0");
        newValue.setAttribute("DATA", value);
        nValues.appendChild(newValue);
        return toString(document);
    }

    private static String toString(final Document document) {
        DOMSource domSource = new DOMSource(document);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = tf.newTransformer();
            transformer.transform(domSource, result);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return writer.toString();
    }

    private static Document parse(final String input) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            return documentBuilder.parse(new ByteArrayInputStream(input.getBytes()));
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String> xpathTextContent(final String input, final String xpath) {
        Document document = parse(input);
        if ( document == null ) {
            return null;
        }
        try {
            NodeList result = ((DTMNodeList) XPathFactoryImpl.newXPathFactoryNoServiceLoader().newXPath().evaluate(
                    xpath,
                    document,
                    XPathConstants.NODESET));
            Stream<String> nodes = IntStream.range(0, result.getLength())
                    .mapToObj(result::item).map((item) -> item.getTextContent());
            return nodes.collect(Collectors.toList());
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }

    private static List<Node> xpath(final Node e, final String xpath) {
        try {
            NodeList result = ((DTMNodeList) XPathFactoryImpl.newXPathFactoryNoServiceLoader().newXPath().evaluate(
                    xpath,
                    e,
                    XPathConstants.NODESET));

            Stream<Node> nodes = IntStream.range(0, result.getLength())
                    .mapToObj(result::item);
            return nodes.collect(Collectors.toList());
        } catch (Exception ex) {
            ex.printStackTrace();
            return Collections.emptyList();
        }
    }

    private static Node xpathSingle(final Node e, final String xpath) {
        try {
            Node result = ((Node) XPathFactoryImpl.newXPathFactoryNoServiceLoader().newXPath().evaluate(
                    xpath,
                    e,
                    XPathConstants.NODE));
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String setAttributeValuesForQueryObjectJSON(final String json, final Map<String, String> eccairsAttributeValueMap) {
        final DocumentContext document = JsonPath.parse(json);

        for (final Map.Entry e : eccairsAttributeValueMap.entrySet()) {
            final JSONObject o = new JSONObject();
            o.put("Type", 0);
            o.put("TypeName", "Value");
            o.put("Value", e.getValue());
            o.put("Name", "None");
            document.add("$.Restrictions[*][?(@.Attribute.Id == " + e.getKey() + ")].Values", o);
        }

        return document.jsonString();
    }
}
