package cz.cvut.kbss.datatools.xmlanalysis.partners.lkpr;

import org.junit.Test;

public class ProcessAdonisExportFileExperiment {

    @Test
    public void process() {
//        String f = new File(".").getAbsolutePath();
//        System.out.println(f);
//        new ProcessAdonisExportFile().processDir("lkpr-process-models");
        new ProcessAdonisExportFile().processFile("lkpr-process-models/AandS included.xml");
    }
}