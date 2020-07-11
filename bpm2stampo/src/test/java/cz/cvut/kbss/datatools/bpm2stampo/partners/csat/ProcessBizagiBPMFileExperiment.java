package cz.cvut.kbss.datatools.bpm2stampo.partners.csat;

import org.junit.Test;

public class ProcessBizagiBPMFileExperiment {
    @Test
    public void runMapping(){
//        new ProcessBisagiBPMFile().processDir("csat-process-models/bizagi-process-models/");
//        new ProcessBisagiBPMFile().processFile("csat-process-models/bizagi-process-models/verze 12.09 BM Administration .bpm");
//        new ProcessBisagiBPMFile().processFile("csat-process-models/bizagi-process-models/verze 08.11 BM Administration.bpm", null);
        new ProcessBizagiBPMFile().processFile("csat-process-models/bizagi-process-models/verze 25.10 BM Administration-001.bpm", "target/test-output/ProcessBizagiBPMFileExperiment/verze 25.10 BM Administration-001.ttl");
//        new ProcessBisagiBPMFile().processFile("csat-process-models/bizagi-process-models/example-model-1.bpm");
    }


}