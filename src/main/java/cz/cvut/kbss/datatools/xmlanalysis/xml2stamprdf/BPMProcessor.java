    package cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf;

import cz.cvut.kbss.commons.io.NamedStream;
import cz.cvut.kbss.datatools.xmlanalysis.common.IOUtils;
import cz.cvut.kbss.datatools.xmlanalysis.mapstructext.MapstructProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class BPMProcessor{
    private static final Logger LOG = LoggerFactory.getLogger(BPMProcessor.class);

    protected Persistence persistence;
    protected MapstructProcessor mapstructProcessor;
    protected Map<String, String> prefixMapping;

    public Map<String, String> getPrefixMapping() {
        return prefixMapping;
    }

    public void setPrefixMapping(Map<String, String> prefixMapping) {
        this.prefixMapping = prefixMapping;
    }

    public void process(Stream<NamedStream> sources, Class mapperClass, String pkgOfModel, String outputFile) {
        mapstructProcessor = new MapstructProcessor(mapperClass);
        try (Persistence p = new Persistence(pkgOfModel)){
            persistence = p;
            persistence.begin();
            sources.forEach(ns -> processXMLFileSafe(ns));
            persistence.commit();

            persistence.begin();
            persistence.applyRules();
            persistence.commit();

            persistence.exportToFile(outputFile, prefixMapping);
        }
    }

    public void processXMLFileSafe(NamedStream ns){
        try {
            processXMLFile(ns);
        } catch (IOException ex) {
            LOG.error("",ex);
        }
    }

    public void processXMLFile(NamedStream ns ) throws IOException {
        String filePath = ns.getName();
        InputStream inputStream = ns.getContent();
        InputStream is = IOUtils.toByteInputStream(inputStream);
        JAXBUtils.UnmarshledResult result = JAXBUtils.unmarshalXMLResolveRelations(filePath, is, Object.class);
        List<Object> transformedObjects = mapstructProcessor.transformAll(result.getObjects());
        for(Object obj : transformedObjects){
            persistence.persist(obj);
        }
    }

//    protected void printFile(String fileName, String fileContent){
//        System.out.println(String.format("processing file '%s' from zip", fileName));
//        System.out.println("----------------------------------------------------------");
//        System.out.println(fileContent);
//
//        System.out.println("----------------------------------------------------------");
//        System.out.println();
//        System.out.println();
//    }
//
//
//    public static void main(String[] args) {
////        new BPMProcessor().process("c:\\Users\\user\\Documents\\skola\\projects\\10-2017-ZETA\\partners\\csat\\2.28.001 Base Maintenance Administration.bpm");
////        testOrdering();
//        testJOPAConfig();
//
//    }
//
//    public static void testJOPAConfig(){
////        cz.cvut.kbss.jopa.model.AttributeConverter
//        String puName = "pu1";
//        String pkgToScan = "cz.cvut.kbss.datatools.xmlanalysis.bpmn.model";
//        EntityManagerFactory emf = new JOPAEMFFactory().createEntityManagerFactoryVolatileStorage(puName, pkgToScan);
//
//        emf.getMetamodel().getManagedTypes().stream().forEach(t -> System.out.println(t.getJavaType().getSimpleName()));
//        EntityManager em = emf.createEntityManager();
//        em.getTransaction().begin();
//        PersonRole pr = new PersonRole();
//        pr.setLabel("asdf");
//        pr.setIri("http://asdf");
//        em.persist(pr);
//        em.getTransaction().commit();
//        em.getTransaction().begin();
//        em.createNativeQuery("SELECT * WHERE {?s ?p ?o}").getResultList().forEach(
//                r -> System.out.println(Stream.of((Object[])r).map(o -> String.format("%s (%s)", Objects.toString(o), o.getClass().getCanonicalName())).collect(Collectors.joining(", ")))
//        );
//        System.out.println(em.getDelegate());
//        System.out.println(em.getDelegate().getClass().getCanonicalName());
////        ((EntityManagerImpl)em.getDelegate()).
//
//        em.getTransaction().commit();
//        em.close();
//        emf.close();
//
//    }
//
//    public static void testOrdering(){
//        Class[] classes = {A.class,C.class, B.class};
//
//        Comparator<Class> comp = (a,b) -> a.equals(b) ? 0 : a.isAssignableFrom(b) ? 1 : -1;
//
//        Stream.of(classes).sorted(comp).forEach(x -> System.out.println(x.getSimpleName()));
//
//        Stream.of(BPMProcessor.class.getMethods())
//                .flatMap(m -> Stream.of(m.getGenericParameterTypes()).map(t -> Pair.of(m, t)))
//                .filter(p -> p.getValue() instanceof Class)
//                .sorted((a, b) -> comp.compare((Class)a.getRight(), (Class)b.getRight()))
//                .forEach(p -> System.out.println(p.getLeft()));
//    }
//
//    static class A{}
//    static class B extends A{}
//    static class C{};
//
//    static void a(A a){}
//    static void b(B a){}
//    static void c(C c){}
//    static void cc(C c,A a){}
}
