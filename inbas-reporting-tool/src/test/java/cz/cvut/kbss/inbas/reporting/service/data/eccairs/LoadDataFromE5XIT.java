/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.kbss.inbas.reporting.service.data.eccairs;

import cz.cvut.kbss.eccairs.report.e5xml.e5x.E5XXMLParser;
import cz.cvut.kbss.eccairs.report.model.EccairsReport;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.fail;

/**
 *
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
public class LoadDataFromE5XIT {
    
    public LoadDataFromE5XIT() {
    }

    /**
     * Test of loadData1 method, of class LoadDataFromE5X.
     */
//    @Test
    public void testLoadData1_String() {
        System.out.println("processing test file /data/eccairs/16FEDEF0BC91E511B897002655546824-anon.xml");
        URL doc = getClass().getResource("/data/eccairs/16FEDEF0BC91E511B897002655546824-anon.xml");
        EccairsReport r = loadReport();
        if(r == null){
            fail("can't cnfigure test.");
        }
        
//        Question q = LoadDataFromE5X.transformToQuestion(r);
//        LoadDataFromE5X.processQuestions(q);
        System.out.println("Done");
        
//        Entity oe = r.getOccurrence();
//        OccurrenceReport or = new OccurrenceReport();
//        Occurrence o = new Occurrence();
//        Repository repo = new SailRepository(new MemoryStore());
//        
//        LoadDataFromE5X 
//        
//        repo.getConnection(); 
//        .prepareGraphQuery(QueryLanguage.SPARQL, "");
//        LinkedHashModel m = new LinkedHashModel();
//        m.
        
        
//        System.out.println("loadData1");
//        String fileName = "";
//        LoadDataFromE5X.loadData1(fileName);
    }
    
    
    protected EccairsReport loadReport(){
        try {
//            String cfg = getClass().getResource("/config-1.properties").getFile();
//            System.setProperty("eccairs-tools-config-file", cfg);
//        System.out.println(System.getProperty("eccairs-tools-config-file", "hahahahah"));
            E5XXMLParser c = new E5XXMLParser();
            String doc = getClass().getResource("/data/eccairs/16FEDEF0BC91E511B897002655546824-anon.xml").getFile();
            System.out.println(doc);
            c.parseDocument(doc);
            return c.getReport();// run in debug to check whether the reading was successful
        } catch (IOException ex) {
            Logger.getLogger(LoadDataFromE5XIT.class.getName()).log(Level.SEVERE, "creating the report from extract e5x file failed dew to an IO excecption.", ex);
        }
        return null;
    }
}
