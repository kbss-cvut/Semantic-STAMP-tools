package cz.cvut.kbss.datatools.xmlanalysis.partners;

import cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf.BasicIRIGenerator;

public class IRIMapper{
    public static BasicIRIGenerator iriMapper;

    public static void initIriMapper(String rootIri){
        iriMapper = new BasicIRIGenerator();
        iriMapper.setRootIRI(rootIri);
    }

    public static String generateIRI(Object o){
        return iriMapper.generateIRI(o);
    }
}