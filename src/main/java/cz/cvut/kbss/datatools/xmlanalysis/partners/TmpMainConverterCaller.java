package cz.cvut.kbss.datatools.xmlanalysis.partners;

import org.eclipse.rdf4j.query.algebra.Str;

public class TmpMainConverterCaller {
    public static void main(String[] args) {
//        String in = "c:\\Users\\user\\Documents\\skola\\projects\\10-2017-ZETA\\code\\lkpr-process-model-extraction\\csat-process-models\\bizagi-process-models\\verze 25.10 BM Administration-001.bpm";
        String in = "c:\\Users\\user\\Documents\\skola\\projects\\10-2017-ZETA\\code\\lkpr-process-model-extraction\\csat-process-models\\bizagi-process-models\\bbb.bpm";
        MainConverter.main(new String[]{in});
    }
}
