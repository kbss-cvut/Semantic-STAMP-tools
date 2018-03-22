/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.kbss.datatools.xmlanalysis.voc;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.shared.impl.PrefixMappingImpl;

/**
 * 
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
public class Vocabulary {
    public static PrefixMappingImpl prefixMapping = getPrefixMapping();
    
    public final static String ufoNS = "http://onto.fel.cvut.cz/ontologies/ufo/";
    public final static String ufoPrfix = "ufo";
    public final static String lkprNS = "http://onto.fel.cvut.cz/ontologies/aviation/airport/lkpr/pm/";
    public final static String lkprPrefix = "lkpr-pm";

    
    public final static String c_EventType = "http://onto.fel.cvut.cz/ontologies/ufo/event-type"; 
    public final static Resource j_c_EventType = ResourceFactory.createResource(c_EventType);
    public final static String c_Agent = "http://onto.fel.cvut.cz/ontologies/ufo/Agent"; 
    public final static Resource j_c_Agent = ResourceFactory.createResource(c_Agent);
    public final static String c_Disposition = "http://onto.fel.cvut.cz/ontologies/ufo/disposition"; 
    public final static Resource j_c_Disposition = ResourceFactory.createResource(c_Disposition);
    public final static String c_Risk = "http://onto.fel.cvut.cz/ontologies/aviation/airport/lkpr/pm/risk"; 
    public final static Resource j_c_Risk = ResourceFactory.createResource(c_Risk);
    public final static String c_Performer = "http://onto.fel.cvut.cz/ontologies/aviation/airport/lkpr/pm/performer"; 
    public final static Resource j_c_Performer = ResourceFactory.createResource(c_Performer);
    public final static String c_Role = "http://onto.fel.cvut.cz/ontologies/aviation/airport/lkpr/pm/role"; 
    public final static Resource j_c_Role = ResourceFactory.createResource(c_Role);
    public final static String c_Organizational_Unit = "http://onto.fel.cvut.cz/ontologies/aviation/airport/lkpr/pm/organizational-unit"; 
    public final static Resource j_c_Organizational_Unit = ResourceFactory.createResource(c_Organizational_Unit);
//    public final static String p_partOfCluster = "http://onto.fel.cvut.cz/ontologies/dataset-descriptor/part-of-cluster"; 
//    public final static Property j_p_partOfCluster = ResourceFactory.createProperty(p_partOfCluster);

    /**
     * @param uri
     * @return the prefixed version of the uri
     */
    public static String shortVersion(String uri){
        return prefixMapping.shortForm(uri);
//        return Vocabulary.felddPrefix + ":" + lrdType.substring(Vocabulary.felddNS.length());
    }

    /**
     * 
     * @param uri
     * @return the local name of the uri
     */
    public static String localName(String uri){
        String localName = prefixMapping.shortForm(uri);
        if(!localName.equals(uri)){
            localName = localName.substring(localName.indexOf(':') + 1);
        }
        return localName;
//        return Vocabulary.felddPrefix + ":" + lrdType.substring(Vocabulary.felddNS.length());
    }
    
    /**
     * @return the prefix mapping for namespaces used in this vocabulary class.
     */
    public static PrefixMappingImpl getPrefixMapping(){
        if(prefixMapping == null){
            prefixMapping = new PrefixMappingImpl();
            prefixMapping.setNsPrefix(ufoPrfix, ufoNS);
            prefixMapping.setNsPrefix(lkprPrefix, lkprNS);
        }
        return prefixMapping;
    }
    
}
