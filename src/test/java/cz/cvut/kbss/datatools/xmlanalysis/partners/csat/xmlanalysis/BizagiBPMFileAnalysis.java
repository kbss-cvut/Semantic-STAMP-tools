package cz.cvut.kbss.datatools.xmlanalysis.partners.csat.xmlanalysis;

import cz.cvut.kbss.datatools.xmlanalysis.common.Utils;
import cz.cvut.kbss.datatools.xmlanalysis.partners.csat.ProcessBisagiBPMFile;
import cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf.InputXmlStream;
import cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf.JAXBUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.w3c.dom.Document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

public class BizagiBPMFileAnalysis {

    @Test
    public void analyzeBPMFile(){
        BizagiXmlAnalysis analizer = new BizagiXmlAnalysis("/csat-bizagi-input.properties");
//        String fileName = "csat-process-models/bizagi-process-models/verze 12.09 BM Administration .bpm";
        String fileName = "csat-process-models/bizagi-process-models/example-model-1.bpm";
        try(ProcessBisagiBPMFile.BizagiBPMPackageXMLStreamer xmlStreamer = new ProcessBisagiBPMFile.BizagiBPMPackageXMLStreamer(fileName); ){
            xmlStreamer.streamSourceFiles()
                    .flatMap(l -> l.stream())
                    .filter(i -> StringUtils.startsWithIgnoreCase(i.getName(), "Diagram"))
                    .forEach(i -> process(fileName, i, analizer));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    protected void process(String bpmFileName, InputXmlStream in, BizagiXmlAnalysis analizer){
        File bpmFile = new File(bpmFileName);
        String bpmDir = Utils.getNameWithoutExtension(bpmFile);
        File outputDir = Utils.ensureDir(analizer.getOutputDirAnalysis(), bpmDir);

        String fileName = in.getParentDir() + "#" + in.getName();
        File f = new File(outputDir, fileName);
        try(PrintStream ps = new PrintStream(f)) {
            analizer.setOut(ps);
            Document d = JAXBUtils.parseDocument(in.getName(), in.getContent(), null);
            analizer.analysisXMLDocument(d, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        analizer.setOut(null);
    }
}
