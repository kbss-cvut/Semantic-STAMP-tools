package cz.cvut.kbss.datatools.xmlanalysis.experiments.lkpr.converters.xslt;

import cz.cvut.kbss.datatools.xmlanalysis.common.Utils;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RDFTransform {

    private static final Logger LOG = LoggerFactory.getLogger(RDFTransform.class);

    protected static Resource s;
    protected static Property p;
    protected static RDFNode o;
    protected static Model m;

    public static void reset(){
        m = ModelFactory.createDefaultModel();
    }

    public static String uri(String s){
        return ResourceFactory.createResource(Utils.urlEncode(s)).toString();
    }

    public static String  propuri(String s){
        if("a".equals(s)){
            return RDF.type.toString();
        }
        return ResourceFactory.createProperty(Utils.urlEncode(s)).toString();
    }


    public static void ru(String str){
        o = ResourceFactory.createPlainLiteral(str);
    }
    public static void ra(){
        o = ResourceFactory.createResource();
    }

    public static void p(String prop){
        p = ResourceFactory.createProperty(prop);
    }

    public static void ol(String str){
        o = ResourceFactory.createPlainLiteral(str);
    }
    public static void ou(String str){
        o = ResourceFactory.createPlainLiteral(str);
    }
//    public static void ou(String pref, String str){
//
//        o = URIFactory.createPlainLiteral(str);
//    }
    public static void oa(){
        o = ResourceFactory.createResource();
    }

    public static void reuseAnon(){
        s = (Resource)o;
    }


//    public static void add(String s, String p, String o){
//        add(uri(s), propuri(p), );
//    }


    public static void add(Resource s, Property p, RDFNode o){
        m.add(s, p, o);
        LOG.info("model size : {}", m.size());
    }

    public static void add(Resource s, RDFNode o){

    }

    public static String trickyString(){
        return "Trickyyyyy!\n\"hahaha\"!!!";
    }
}
