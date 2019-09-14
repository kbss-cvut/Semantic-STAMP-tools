package cz.cvut.kbss.datatools.xmlanalysis.partners.csat.xmlanalysis;

import cz.cvut.kbss.datatools.xmlanalysis.XMLAnalyzer;

public class XmlSchemaAnalysis {
    public static void main(String[] args) {
        XMLAnalyzer xmlAnalyzer = new XMLAnalyzer("csat-bizagi-input.properties");
        xmlAnalyzer.processSources();
    }
}
