package cz.cvut.kbss.datatools.xmlanalysis.partners.csat;

import cz.cvut.kbss.commons.io.NamedStream;
import cz.cvut.kbss.commons.io.zip.ZipSource;
import cz.cvut.kbss.commons.io.zip.ZipSourceEntry;
import cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf.BPMProcessor;
import cz.cvut.kbss.datatools.xmlanalysis.partners.csat.mapping.BizagiDiagPackage;
import cz.cvut.kbss.datatools.xmlanalysis.partners.IRIMapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class ProcessBisagiBPMFile {

    private static final Logger LOG = LoggerFactory.getLogger(ProcessBisagiBPMFile.class);

    protected BPMProcessor bpmProcessor = new BPMProcessor();
    protected Class<BizagiDiagPackage> mapperClass = BizagiDiagPackage.class;
    protected String pkg = "cz.cvut.kbss.datatools.xmlanalysis.bpmn.model";
    protected String rootIRI = "http://onto.fel.cvut.cz/partners/csat/";
    protected String outputFile = "c:\\Users\\user\\Documents\\skola\\projects\\10-2017-ZETA\\code\\lkpr-process-model-extraction\\tmp-export.rdf";

    public void processDir(String dirPath){
        bpmProcessor.setPrefixMapping(constructPrefixMapping());
        IRIMapper.initIriMapper(rootIRI);
        File dir = new File(dirPath);
        LOG.debug("Processing BPM files in directory '{}'.", dirPath);
        for(File f : dir.listFiles(f ->
                StringUtils.endsWithIgnoreCase(f.getName(),".bpm") &&
                StringUtils.startsWithIgnoreCase(f.getName(),"BM Administration verze 26.08.bpm")
                )){

            process(f);
        }
    }

    public Map<String, String> constructPrefixMapping(){
        Map<String, String> map = new HashMap<>();
        map.put("csat.stamp", "http://onto.fel.cvut.cz/partners/csat/");
        return map;
    }

    public void process(File file){
        LOG.info("Processing file '{}'", file.getAbsolutePath());
        try(SourceFileStreamer sfs = new SourceFileStreamer(file.getAbsolutePath())) {
            bpmProcessor.process(sfs.streamSourceFiles(), mapperClass, pkg, outputFile);
        } catch (IOException e) {
            LOG.error("", e);
        }
    }



    public static class SourceFileStreamer implements Closeable {
        protected ZipFile zipFile;

        public SourceFileStreamer(ZipFile zipFile) {
            this.zipFile = zipFile;
        }

        public SourceFileStreamer(String fileName) throws IOException {
            zipFile = new ZipFile(fileName);
        }

        @Override
        public void close() throws IOException {
            zipFile.close();
        }

        public Stream<NamedStream> streamSourceFiles() {
            ZipSource<ZipFile> zip = ZipSource.wrap(zipFile);
            return zip.streamEntries()
                    .filter(e -> {
                        System.out.println(e.getName());
                        return e.getName().endsWith(".diag");
                    })
                    .flatMap(e -> listDiagFilesSafe(e));
        }

        protected Stream<NamedStream> listDiagFilesSafe(ZipSourceEntry e) {
            try {
                return listDiagFiles(e.getName(), e.getInputStream());
            } catch (IOException ex) {
                LOG.error("", ex);
            }
            return Stream.of();
        }

        protected NamedStream asNamedStream(ZipSourceEntry e) {
            try {
                return new NamedStream(e.getName(), e.getInputStream());
            } catch (IOException ex) {
                LOG.error("", ex);
            }
            return null;
        }

        protected Stream<NamedStream> listDiagFiles(String filePath, InputStream stream) throws IOException {
//            LOG.debug("Stream class = '{}'", stream.getClass().getCanonicalName());
//            ByteArrayInputStream bis = new ByteArrayInputStream(IOUtils.toByteArray(stream));
//            ZipSource<ZipInputStream> zip = ZipSource.wrap(bis);
            ZipSource<ZipInputStream> zip = ZipSource.wrap(stream);

            return zip.streamEntries()
                    .filter(e -> e.getName().equals("Diagram.xml"))
                    .map(this::asNamedStream)
                    .filter(ns -> ns != null);

        }
    }


    public static void main(String[] args) {
        new ProcessBisagiBPMFile().processDir("c:\\Users\\user\\Documents\\skola\\projects\\10-2017-ZETA\\partners\\csat\\");
    }

}
