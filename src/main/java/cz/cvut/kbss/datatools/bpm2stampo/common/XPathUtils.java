package cz.cvut.kbss.datatools.bpm2stampo.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpression;
import java.util.*;
import java.util.function.Function;

public class XPathUtils {

    private static final Logger LOG = LoggerFactory.getLogger(XPathUtils.class);

    protected static Map<Class, QName> JAVA_XML_TYPE_MAP;
    protected static Map<QName, Class> XML_JAVA_TYPE_MAP;



    static {
        JAVA_XML_TYPE_MAP = new HashMap<>();
        JAVA_XML_TYPE_MAP.put(Double.class, XPathConstants.NUMBER);
        JAVA_XML_TYPE_MAP.put(String.class, XPathConstants.STRING);
        JAVA_XML_TYPE_MAP.put(Boolean.class, XPathConstants.BOOLEAN);
        JAVA_XML_TYPE_MAP.put(Node.class, XPathConstants.NODE);
        JAVA_XML_TYPE_MAP.put(NodeList.class, XPathConstants.NODESET);

        XML_JAVA_TYPE_MAP = new HashMap<>();
        JAVA_XML_TYPE_MAP.entrySet().forEach(e -> XML_JAVA_TYPE_MAP.put(e.getValue(), e.getKey()));
    }

    public static interface ExtractFunction<O> extends Function<Node, O>{}
    public static interface ExtractStr extends ExtractFunction<Optional<String>>{};
    public static interface ExtractNum extends ExtractFunction<Optional<Double>>{};
    public static interface ExtractBool extends ExtractFunction<Optional<Boolean>>{};
    public static interface ExtractNodes extends ExtractFunction<List<Node>>{};

    public static ExtractStr extractStr(XPathExpression xp) {
        return from -> extractStr(xp, from);
    }

    public static ExtractBool extractBool(XPathExpression xp) {
        return from -> extractBoolean(xp, from);
    }

    public static ExtractNum extractNum(XPathExpression xp) {
        return from -> extractNumber(xp, from);
    }

    public static ExtractNodes extractNodes(XPathExpression xp){
       return from -> extractListOfNodes(xp, from);
    }


    public static Optional<String> extractStr(XPathExpression xp, Node from){
        return extractOpt(xp, from, String.class);
    }

    public static Optional<Double> extractNumber(XPathExpression xp, Node from){
        return extractOpt(xp, from, Double.class);
    }

    public static Optional<Boolean> extractBoolean(XPathExpression xp, Node from){
        return extractOpt(xp, from, Boolean.class);
    }

    public static List<Node> extractListOfNodes(XPathExpression xp, Node from){
        NodeList nl = extract(xp, null, from, NodeList.class);
        return XMLCollections.asList(nl);
    }

    public static <T> Function<Node,T> extract(XPathExpression xp, T defaultValue, Class<T> cls) {
        return from -> extract(xp, defaultValue, from, cls);
    }

    public static <T> Function<Node,Optional<T>> extractOpt(XPathExpression xp, Class<T> cls) {
        return from -> extractOpt(xp, from, cls);
    }


    public static <T> T extract(XPathExpression xp, T defaultValue, Node from, Class<T> cls){
        return extractOpt(xp, from, cls).orElse(defaultValue);
    }

    public static <T> Optional<T> extractOpt(XPathExpression xp, Node from, Class<T> cls){
        Objects.requireNonNull(xp);
        Objects.requireNonNull(cls);

        Class<T> outputType = cls;

        Class key = cls;
        if(Number.class.isAssignableFrom(cls) && !cls.equals(Double.class)){
            key = Double.class;
        }


        try {
            Optional opt = Optional.ofNullable(xp.evaluate(from, JAVA_XML_TYPE_MAP.get(key)));// evaluate will throw NullPointerException if cls has no value in the map
            if(Number.class.isAssignableFrom(cls)) {
                opt = opt.map(n -> castNumber((Double) n, outputType));
            }
            return opt;
        } catch (XPathException e) {
            LOG.error("", e);
        }
        return Optional.ofNullable(null);
    }

    public static <T> T castNumber(Number n, Class<T> to){
        switch (to.getSimpleName()){
            case "Double":  return (T)(Number)n.doubleValue();
            case "Integer":  return (T)(Number)n.intValue();
            case "Short":  return (T)(Number)n.shortValue();
            case "Long":  return (T)(Number)n.longValue();
            case "Float":  return (T)(Number)n.floatValue();
            case "Byte":  return (T)(Number)n.byteValue();
        }
        return null;
    }
}
