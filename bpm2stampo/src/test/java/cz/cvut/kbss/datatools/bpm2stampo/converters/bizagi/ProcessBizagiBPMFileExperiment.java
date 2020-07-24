package cz.cvut.kbss.datatools.bpm2stampo.converters.bizagi;

import cz.cvut.kbss.datatools.bpm2stampo.common.Utils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.*;

public class ProcessBizagiBPMFileExperiment {
    @Test
    public void testProcessFileFile(){
//        new ProcessBizagiBPMFile().processFile("csat-process-models/bizagi-process-models/verze 25.10 BM Administration-001.bpm", "target/test-output/ProcessBizagiBPMFileExperiment/verze 25.10 BM Administration-001.ttl");
        new ProcessBizagiBPMFile().processFile("/bizagi/example-model-1.bpm", "target/test-output/ProcessBizagiBPMFileExperiment/verze 25.10 BM Administration-001.ttl");
    }

    @Ignore
    @Test
    public void testProcessFileInputStream() throws IOException {
        File file = Utils.getResourceAsFile("/bizagi/example-model-1.bpm");
        try(InputStream is = new FileInputStream(file)) {
            new ProcessBizagiBPMFile().processFile(file.getAbsolutePath(), "target/test-output/ProcessBizagiBPMFileExperiment/example-model-1.ttl");
        }
    }

    @Test
    public void test_convertInputStream() throws IOException {
        File file = Utils.getResourceAsFile("/bizagi/example-model-1.bpm");
        InputStream output;
        try(InputStream is = file.toURI().toURL().openStream()) {
            output = new ProcessBizagiBPMFile().convertToStream(file.getAbsolutePath(), is);
        }
        Assert.assertNotNull(output);
        Model model = ModelFactory.createDefaultModel();
        model.read(output, "http://example.org/");
        Assert.assertTrue(model.size() > 0);
    }

    @Ignore
    @Test
    public void experiment_test_convertInputStream() throws IOException {
        File file = Utils.getFile("c:\\Users\\user\\Documents\\skola\\projects\\10-2017-ZETA\\code\\bpmn2ontology\\csat-process-models\\bizagi-process-models\\verze 08.11 BM Administration.bpm");
        InputStream output;
        try(InputStream is = file.toURI().toURL().openStream()) {
            output = new ProcessBizagiBPMFile().convertToStream(file.getAbsolutePath(), is);
        }
        Assert.assertNotNull(output);
        Model model = ModelFactory.createDefaultModel();
        model.read(output, "http://example.org/");
        Assert.assertTrue(model.size() > 0);
    }
}