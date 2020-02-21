package cz.cvut.kbss.datatools.xmlanalysis.common.graphml;

import org.apache.jena.shared.impl.PrefixMappingImpl;

public class LabelRenderer {

    final static String ufoNS = "http://onto.fel.cvut.cz/ontologies/ufo/";
    final static String ufoPrfix = "ufo";
    final static String lkprNS = "http://onto.fel.cvut.cz/ontologies/aviation/airport/lkpr/pm/";
    final static String lkprPrefix = "lkpr-pm";
    final static String sptampNS = "http://onto.fel.cvut.cz/ontologies/stamp/";
    final static String stampPrefix = "stamp";
    final static String bpmnNS = "http://onto.fel.cvut.cz/ontologies/bpmn/";
    final static String bpmnPrefix = "bpmn";

    public static PrefixMappingImpl getPrefixMapping(){
        PrefixMappingImpl prefixMapping = new PrefixMappingImpl();
        prefixMapping.setNsPrefix(ufoPrfix, ufoNS);
        prefixMapping.setNsPrefix(lkprPrefix, lkprNS);
        prefixMapping.setNsPrefix(stampPrefix, sptampNS);
        prefixMapping.setNsPrefix(bpmnPrefix, bpmnNS);
        return prefixMapping;
    }

    public PrefixMappingImpl prefixMapping = getPrefixMapping();

    /**
     *
     * @param uri
     * @return the local name of the uri
     */
    public String localName(String uri){
        String localName = prefixMapping.shortForm(uri);
        if(!localName.equals(uri)){
            localName = localName.substring(localName.indexOf(':') + 1);
        }
        return localName;
//        return Vocabulary.felddPrefix + ":" + lrdType.substring(Vocabulary.felddNS.length());
    }

}
