package cz.cvut.kbss.datatools.bpm2stampo.partners.lkpr;

import cz.cvut.kbss.datatools.bpm2stampo.partners.lkpr.model.ADOXML;
import cz.cvut.kbss.datatools.bpm2stampo.partners.lkpr.model.CompanyMapModel;
import cz.cvut.kbss.datatools.bpm2stampo.xml2stamprdf.BPMProcessor;
import cz.cvut.kbss.datatools.bpm2stampo.xml2stamprdf.InputXmlStream;
import cz.cvut.kbss.datatools.bpm2stampo.xml2stamprdf.JAXBUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ProcessAdonisExportFileExperiment {

    @Test
    public void process() {
//        String f = new File(".").getAbsolutePath();
//        System.out.println(f);
//        new ProcessAdonisExportFile().processDir("lkpr-process-models");

//        new ProcessAdonisExportFile().processFile("lkpr-process-models/adonis-usage-specification/AandS included.xml");
//        new ProcessAdonisExportFile().processFile("lkpr-process-models/adonis-usage-specification/TEST 24_2.xml");
//        new ProcessAdonisExportFile().processFile("lkpr-process-models/adonis-usage-specification/aerobridge docking.xml");
//        new ProcessAdonisExportFile().processFile("lkpr-process-models/adonis-usage-specification/aerobridge go home.xml");
//        new ProcessAdonisExportFile().processFile("lkpr-process-models/adonis-usage-specification/AandS included.xml");
//        new ProcessAdonisExportFile().processFile("lkpr-process-models/adonis-usage-specification/AandS included.xml");
        new ProcessAdonisExportFile().processFile("lkpr-process-models/CDP.xml");
    }

    @Test
    public void testSafetyTESTOnionStructure(){
        File file = new File("lkpr-process-models/adonis-usage-specification/Safety TEST onion structure.xml");
        try(InputStream is = new FileInputStream(file)) {
            InputXmlStream input = new InputXmlStream(file.getName(), is, ADOXML.class);
            BPMProcessor p = new BPMProcessor();
            List<JAXBUtils.UnmarshledResult> results = p.unmarshalXMLFiles(Arrays.asList(input));
            Assert.assertEquals(results.size(), 1);
            JAXBUtils.UnmarshledResult res = results.get(0);
            Set<Object> os = res.getObjects();
            Assert.assertNotNull(os);

            // TEST Company map
            List<CompanyMapModel> companyMapModels = os
                    .stream()
                    .filter(o -> o instanceof CompanyMapModel)
                    .map(o -> (CompanyMapModel)o).collect(Collectors.toList());

            Assert.assertEquals(2, companyMapModels.size());


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}