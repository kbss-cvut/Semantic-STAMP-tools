package cz.cvut.kbss.datatools.xmlanalysis.partners.csat;

import cz.cvut.kbss.commons.io.zip.ZipSource;
import cz.cvut.kbss.commons.io.zip.ZipSourceEntry;
import cz.cvut.kbss.datatools.xmlanalysis.common.IOUtils;
import cz.cvut.kbss.datatools.xmlanalysis.common.Utils;
import cz.cvut.kbss.datatools.xmlanalysis.mapstructext.MapstructProcessor;
import cz.cvut.kbss.datatools.xmlanalysis.partners.AbstractProcessModelExporter;
import cz.cvut.kbss.datatools.xmlanalysis.partners.csat.mapping.BizagiDiagPackage;
import cz.cvut.kbss.datatools.xmlanalysis.partners.csat.model.*;
import cz.cvut.kbss.datatools.xmlanalysis.partners.csat.model.Package;
import cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf.BPMProcessor;
import cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf.InputXmlStream;
import cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf.JAXBUtils;
import cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf.model.EventType;
import cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf.model.GroupController;
import cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf.model.Identifiable;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class ProcessBisagiBPMFile extends AbstractProcessModelExporter<BizagiDiagPackage> {

    private static final Logger LOG = LoggerFactory.getLogger(ProcessBisagiBPMFile.class);

    public void config(){
        mapperClass = BizagiDiagPackage.class;
        pkg = "cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf.model";
        outputDir = null;
    }

    @Override
    protected void initBMPMProcessor() {
        bpmProcessor = new BPMProcessor(){
            @Override
            public List<Object> mapstructTransform(Collection<Object> in) {
                return  transform(in, mapstructProcessor);
            }
        };
        bpmProcessor.setPrefixMapping(constructPrefixMapping());
        bpmProcessor.resetRegistry();

    }

    protected List<Object> transform(Collection<Object> in, MapstructProcessor mapstructProcessor){

        List<Object> mainPools = in.stream()
                .filter(i -> i instanceof Pool)
                .filter(p -> "Main Process".equals(((Pool) p).getName()))
                .collect(Collectors.toList());

        List<Object> mainProcesses = in.stream()
                        .filter(i -> i instanceof WorkflowProcess)
                        .filter(p -> "Main Process".equals(((WorkflowProcess) p).getName()))
                        .collect(Collectors.toList());

//        for(Object obj : in){
//            if(obj instanceof Package){
//                Package pckg = (Package)obj;
//                Optional.ofNullable(pckg.getPools()).ifPresent(c -> c.removeAll(mainPools));
//                Optional.ofNullable(pckg.getProcesses()).ifPresent(p -> p.removeAll(mainProcesses));
//            }
//        }

        List<Object> toTransform = new ArrayList<>(in);

//        toTransform.removeAll(mainPools);
//        toTransform.removeAll(mainProcesses);
        // remove references to main pools and processes

        mapstructProcessor.transformAll(toTransform);
        Collection<Identifiable> ret = bpmProcessor.getRegistry().values();

        // Remove "Main process" EventTypes
        removeMainProcesses(ret);

        // Remove "Team - Main Process" controllers
        removeMainControllers(ret);

        // Remove Hazards with no parents
        removeHazardsWithNoParents(ret);

        // TODO
//        String s = mapstructProcessor.
        return new ArrayList<>(ret);
    }

    protected void removeMainProcesses(Collection<Identifiable> toFilter){
        List<EventType> toRemove = new ArrayList<>();
        for(Identifiable o : toFilter){
            if(o instanceof EventType && "Main Process".equals(o.getLabel())){
                toRemove.add((EventType) o);
            }
        }
        toFilter.removeAll(toRemove);
        Set<String> urisToRemove = toRemove.stream().map(o -> o.getIri()).collect(Collectors.toSet());
        for(Object o : toFilter){
            if(o instanceof EventType){
                EventType et = (EventType)o;
                Optional.ofNullable(et.getComponents()).ifPresent(c -> c.removeAll(urisToRemove));
            }
        }
    }

    protected void removeMainControllers(Collection<Identifiable> toFilter){
        List<GroupController> toRemove = new ArrayList<>();
        for(Identifiable o : toFilter){
            if(o instanceof GroupController && (
                    "Team - Main Process".equals(o.getLabel()) ||
                    "Main Process".equals(o.getLabel())
            )){
                toRemove.add((GroupController) o);
            }
        }
        toFilter.removeAll(toRemove);
        Set<String> urisToRemove = toRemove.stream().map(o -> o.getIri()).collect(Collectors.toSet());
        for(Object o : toFilter){
            if(o instanceof GroupController){
                GroupController gc = (GroupController)o;
                Optional.ofNullable(gc.getSubGroups()).ifPresent(c -> c.removeAll(urisToRemove));
            }
        }
    }

    protected void removeHazardsWithNoParents(Collection<Identifiable> toFilter){
        List<String> hazardIris = new ArrayList<>();
        for(Identifiable o : toFilter){
            if(o instanceof EventType && o.getLabel() != null && o.getLabel().startsWith("[H] - ")){
                hazardIris.add(o.getIri());
            }
        }
        toFilter.stream()
                .filter(i -> i instanceof EventType)
                .map(i -> ((EventType)i).getComponents())
                .filter(c -> c != null && !c.isEmpty())
                .forEach(c -> hazardIris.removeAll(c));

        List<Identifiable> toRemove = toFilter.stream()
                .filter(i -> hazardIris.contains(i.getIri()))
                .map(i -> (EventType)i)
                .collect(Collectors.toList());

        toFilter.removeAll(toRemove);
    }

    protected void resolveReferencesAcrossDiagrams(){

    }

    public void processFile(String filePath, String outputDir){
        config();
        File f = new File(filePath);
        if(outputDir != null){
            this.outputDir = new File(outputDir);
        }
        outputFile = Utils.getOutputFileSmart(f, this.outputDir);
        initBMPMProcessor();
        LOG.info("converting bpmn to rdf, from \"{}\" out \"{}\"", filePath, outputFile);
        bpmProcessor.resetRegistry();
        File file = new File(filePath);
        process(file);
    }

    public void processDir(String dirPath){
        config();
        initBMPMProcessor();
//        bpmProcessor.resetRegistry();
        File dir = new File(dirPath);
        LOG.debug("Processing BPM files in directory '{}'.", dirPath);
        for(File f : dir.listFiles(f ->
                StringUtils.endsWithIgnoreCase(f.getName(),".bpm")
                )){
            outputFile = Utils.getOutputFile(f, outputDir);
            process(f);
        }
    }

    @Override
    public Map<String, String> constructPrefixMapping(){
        Map<String, String> map = super.constructPrefixMapping();
        map.put("csat.stamp", "http://onto.fel.cvut.cz/partners/csat/");
        map.put(BizagiDiagPackage.BIZAGI_PACKAGE_PREFIX, BizagiDiagPackage.BIZAGI_PACKAGE_NAMESPACE);
        return map;
    }

    public void process(File file){
        LOG.info("Processing file '{}'", file.getAbsolutePath());
        try(BizagiBPMPackageXMLStreamer sfs = new BizagiBPMPackageXMLStreamer(file.getAbsolutePath())) {
            bpmProcessor.process(sfs.streamSourceFiles(), mapperClass, pkg, outputFile.getAbsolutePath());
        } catch (IOException e) {
            LOG.error("", e);
        }
    }

    public static class BizagiBPMPackageXMLStreamer implements Closeable {
        protected ZipFile zipFile;

        public BizagiBPMPackageXMLStreamer(ZipFile zipFile) {
            this.zipFile = zipFile;
        }

        public BizagiBPMPackageXMLStreamer(String fileName) throws IOException {
            zipFile = new ZipFile(fileName);
        }

        @Override
        public void close() throws IOException {
            zipFile.close();
        }

        public Stream<List<InputXmlStream>> streamSourceFiles() {
            ZipSource<ZipFile> zip = ZipSource.wrap(zipFile);
            return Stream.of(zip.streamEntries()
//                    .filter(e -> {
//                        System.out.println(e.getName());
//                        return e.getName().endsWith(".diag");
//                    })
                    .flatMap(e -> {
                        if(e.getName().endsWith(".diag")) {
                            return listDiagFileContentsSafe(e).stream();
                        }else if(e.getName().startsWith("Documentation") && e.getName().endsWith(".xml")){
                            return Stream.of(asNamedStream(e.getName(), e, ExtendedAttribute.class));
                        }
                        return Stream.of();
                    }).collect(Collectors.toList()));
        }

        protected List<InputXmlStream> listDiagFileContentsSafe(ZipSourceEntry e) {
            try {
                return listDiagFileContents(e.getName(), e.getInputStream());
            } catch (IOException ex) {
                LOG.error("", ex);
            }
            return Collections.emptyList();
        }

        protected InputXmlStream asNamedStream(String dirName, ZipSourceEntry e, Class rootClass) {
            try {
                return new InputXmlStream(e.getName(), dirName, e.getInputStream(), rootClass);
            } catch (IOException ex) {
                LOG.error("", ex);
            }
            return null;
        }

        protected List<InputXmlStream> listDiagFileContents(String filePath, InputStream stream) throws IOException {
//            LOG.debug("Stream class = '{}'", stream.getClass().getCanonicalName());
//            ByteArrayInputStream bis = new ByteArrayInputStream(IOUtils.toByteArray(stream));
//            ZipSource<ZipInputStream> zip = ZipSource.wrap(bis);
            ZipSource<ZipInputStream> zip = ZipSource.wrap(stream);

            return zip.streamEntries()
                    .filter(e -> e.getName().equals("Diagram.xml") || e.getName().equals("ExtendedAttributeValues.xml"))
                    .map(e -> asNamedStream(filePath, e, getXmlRoot(e.getName())))
                    .map(ns -> toByteStream(ns))
                    .filter(ns -> ns != null)
                    .collect(Collectors.toList());
        }
    }


    public static Class getXmlRoot(String fileName){
        switch (fileName){
            case "Diagram.xml": return Package.class;
            case "ExtendedAttributeValues.xml": return DiagramAttributeValues.class;
        }
        return null;
    }

    protected static InputXmlStream toByteStream(InputXmlStream ns){
        ByteArrayInputStream bais = IOUtils.toByteInputStream(ns.getContent());
        ns.setContent(bais);
        return ns;
//        BufferedReader br = new BufferedReader(new InputStreamReader(bais));
//
//        String firstLine = br.lines().findFirst().orElse("");
//        bais.reset();
//        if("<?xml version=\"1.0\"?>".equals(firstLine)) {
//            bais.skip(firstLine.length());
//        }
//        return new NamedStream(ns.getName(), bais);
    }
}
