package cz.cvut.kbss.datatools.bpm2stampo.xml2stamprdf;

import cz.cvut.kbss.datatools.bpm2stampo.DefaultQueries;
import cz.cvut.kbss.datatools.bpm2stampo.common.Utils;
import cz.cvut.kbss.datatools.bpm2stampo.common.refs.IDRuntime;
import cz.cvut.kbss.datatools.bpm2stampo.mapstructext.DefaultMapper;
import cz.cvut.kbss.datatools.bpm2stampo.mapstructext.MapstructProcessor;
import cz.cvut.kbss.datatools.bpm2stampo.mapstructext.modules.IRIDecorator;
import cz.cvut.kbss.datatools.bpm2stampo.xml2stamprdf.model.Identifiable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BPMProcessor{
    private static final Logger LOG = LoggerFactory.getLogger(BPMProcessor.class);

    protected static Map<Object, BPMProcessor> processorRegistry = new HashMap<>();

    protected Persistence persistence;
    protected MapstructProcessor mapstructProcessor;
    protected Map<String, String> prefixMapping;

    protected Map<String, Identifiable> registry;
    protected Map<String, String> queryRules = Utils.loadQueriesAsMap();

    public static BPMProcessor getProcessor(DefaultMapper mapper){
        return processorRegistry.get(mapper);
    }

    public Map<String, Identifiable> getRegistry() {
        return registry;
    }

    public void setRegistry(Map<String, Identifiable> registry) {
        this.registry = registry;
    }

    public Map<String, String> getQueryRules() {
        return queryRules;
    }

    public void setQueryRules(Map<String, String> queryRules) {
        this.queryRules = queryRules;
    }

    /**
     * Resets or initializes the registry of image objects.
     */
    public void resetRegistry(){
        this.registry = new HashMap<>();
    }

    public MapstructProcessor getMapstructProcessor() {
        return mapstructProcessor;
    }

    public void setMapstructProcessor(MapstructProcessor mapstructProcessor) {
        this.mapstructProcessor = mapstructProcessor;
    }

    public Map<String, String> getPrefixMapping() {
        return prefixMapping;
    }

    public void setPrefixMapping(Map<String, String> prefixMapping) {
        this.prefixMapping = prefixMapping;
    }

    public void resetMapstructProcessor(Class mapperClass){
        if(mapstructProcessor != null){
            processorRegistry.remove(mapstructProcessor.getMapper());
        }
        mapstructProcessor = new MapstructProcessor(mapperClass);
        processorRegistry.put(mapstructProcessor.getMapper(), this);
    }

    public void processFiles(Stream<InputXmlStream> sources, Class mapperClass, String pkgOfModel, String outputFile) {
        process(sources.map(x -> Arrays.asList(x)), mapperClass, pkgOfModel, outputFile);
    }

    public void process(Stream<? extends Collection<InputXmlStream>> sources, Class mapperClass, String pkgOfModel, String outputFile) {
        resetMapstructProcessor(mapperClass);
        List<? extends Collection<InputXmlStream>> l = sources.collect(Collectors.toList());
        try (Persistence p = new Persistence(pkgOfModel)){
            l.stream().forEach(ns -> processXMLFile(ns));

            persistence = p;
            persistence.begin();
            persist(registry.values());
            persistence.commit();

            applyRules();

            persistence.exportToFile(outputFile, prefixMapping);
        }
    }

    public void processXMLFile(Collection<InputXmlStream> in ) { // throws IOException {
        List<JAXBUtils.UnmarshledResult> results = unmarshalXMLFiles(in);
        List<Object> objects = injectObjectReferences(results);
        List<Object> transformedObjects = mapstructTransform(objects);
    }

    public List<JAXBUtils.UnmarshledResult> unmarshalXMLFiles(Collection<InputXmlStream> in){
        List<JAXBUtils.UnmarshledResult> results = new ArrayList<>();
        for(InputXmlStream ns : in) {
            if(ns == null) continue;
            JAXBUtils.UnmarshledResult result = unmarshal(ns);
            if(result != null)
                results.add(result);
        }
        return results;
    }

    public JAXBUtils.UnmarshledResult unmarshal(InputXmlStream in){
        LOG.info("processing file \"{}\"", in.getName());
        return JAXBUtils.unmarshalXMLResolveRelations(in.getName(), in.getContent(), in.getRoot());
    }

    public void applyRules(){
        List<String> queries = Arrays.asList(
                queryRules.get(DefaultQueries.FIX_PARENTS_OF_NEXT_RELATIONS),
                queryRules.get(DefaultQueries.REMOVE_REDUNDANT_HAS_PART),
                queryRules.get(DefaultQueries.ADD_CAPABABILITIES_TO_PROCESSES)
        );
        for (String query : queries) {
            persistence.begin();
            persistence.applyRules(Arrays.asList(query));
            persistence.commit();
        }
    }

    public void persist(Collection<Identifiable> entities){
        for (Object obj : entities) {
            persistence.persist(obj);
        }
    }

    public List<Object> injectObjectReferences(Collection<JAXBUtils.UnmarshledResult> results){
        List<Object> objects = new ArrayList<>();
        results.forEach(r -> objects.addAll(r.getObjects()));
        new IDRuntime().injectReferences(objects);
        return objects;
    }

    public List<Object> mapstructTransform(Collection<Object> in ) { // throws IOException {
        return mapstructProcessor.transformAll(in);
//        return in.stream()
//                .filter(l -> l != null)
//                .map(this::mapstructTransform)
//                .filter(l -> l != null)
//                .flatMap(l -> l.stream())
//                .collect(Collectors.toList());
    }

    public String toIRI(Object in, String[] beforeId, String[] afterId){
        Class cls = in.getClass();
        return mapstructProcessor.getTransformers().stream()
//                .filter(p -> p.getValue().equals(cls))
                .filter(p -> p.getKey().isApplicableOn(in))
                .map(p -> p.getKey().getDecorators().stream()
                        .filter(d -> d instanceof IRIDecorator)
                        .map(d -> (IRIDecorator)d)
                        .findFirst().orElse(null))
                .filter(d -> d != null && isMatchingPreAndPostfix(d, beforeId, afterId))
                .map(d -> d.calculateValue(in, null))
                .findFirst()
                .orElse(null);
    }

    protected boolean isMatchingPreAndPostfix(IRIDecorator iriDecorator, String[] beforeId, String[] afterId){
        return
                (beforeId != null ? iriDecorator.equalsBeforeId(beforeId) : true) &&
                (afterId != null ? iriDecorator.equalsAfterId(afterId) : true);
    }


//    public List<Object> mapstructTransform(List<Object> in) { // throws IOException {
//        return mapstructProcessor.transformAll(in);
//    }
}
