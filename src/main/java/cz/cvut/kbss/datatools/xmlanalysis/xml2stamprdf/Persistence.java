package cz.cvut.kbss.datatools.xmlanalysis.xml2stamprdf;

import cz.cvut.kbss.jopa.exceptions.OWLEntityExistsException;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.jena.graph.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.impl.ModelCom;
import org.apache.jena.riot.Lang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Persistence implements Closeable {

    private static final Logger LOG = LoggerFactory.getLogger(Persistence.class);

    protected String pkg;
    protected EntityManagerFactory emf;
    protected EntityManager em;

    public Persistence(String pkg) {
        this.pkg = pkg;
        initPersistence(pkg);
    }

    public void persist(Object o){
        try{
            em.merge(o);
        }catch (OWLEntityExistsException e){
            LOG.warn("",e);
        }catch (IllegalArgumentException e) {
            LOG.warn("", e);
        }
    }

    public void applyRules(List<String> rulesAsSparql){
        for(String query : rulesAsSparql){
            em.createNativeQuery(query).executeUpdate();
        }

//        System.out.println("-------------------------------------------------------------------------");
//        System.out.println("-------------------------------------------------------------------------");
//        List result = getResultList("SELECT ?e ?c {\n" +
//                "?e a <http://onto.fel.cvut.cz/ontologies/stamp/controlled-process>\n" +
//                "BIND(concat(str(?e),\"-capability\") as ?c)\n" +
//                "}");
//
//        List result = getResultList("SELECT ?e {?e a <http://onto.fel.cvut.cz/ontologies/stamp/controlled-process>.}");
//        print(result);
//        System.out.println("-------------------------------------------------------------------------");
//        System.out.println("-------------------------------------------------------------------------");
//
//        result = getResultList("SELECT ?c {?c a <http://onto.fel.cvut.cz/ontologies/stamp/capability>.}");
//        print(result);
//        print(getResultList("SELECT * {?s a <http://a>}"));
//
    }

    protected void initPersistence(String pkgToScan){
        String puName = "pu1";
        emf = new JOPAEMFFactory().createEntityManagerFactoryVolatileStorage(puName, pkgToScan);
        em = emf.createEntityManager();
    }


    public void begin() {
        em.getTransaction().begin();
    }

    public void commit(){
        em.getTransaction().commit();
    }

    @Override
    public void close(){
//        printContents();
        em.close();
        emf.close();
    }

    public void printContents(){
        LOG.info("Printing data in repository!");
        em.getTransaction().begin();
        em.createNativeQuery("SELECT * {?s ?p ?o}.").getResultList().forEach(
                r -> System.out.println(Stream.of((Object[])r).map(o -> String.format("%s (%s)", Objects.toString(o), o.getClass().getCanonicalName())).collect(Collectors.joining(", ")))
        );
        em.getTransaction().commit();
    }



    public void exportToFile(String file, Map<String, String> prefixMappings){
        em.getTransaction().begin();
        checkData();
        Graph g = exportData();
        em.getTransaction().commit();
        exportToFileImpl(g, file,  prefixMappings);
    }

    public void exportToFileImpl(Graph g, String file, Map<String, String> prefixMappings){
        LOG.info("Graph export size {}",g.size());
        LOG.info("To file {}", file);
        try(OutputStream os = new FileOutputStream(file)) {
            prefixMappings.entrySet().forEach(e -> g.getPrefixMapping().setNsPrefix(e.getKey(), e.getValue()));
            Model m = new ModelCom(g);
            m.write(os, Lang.RDFXML.getLabel());
        }catch (FileNotFoundException e){
            LOG.error("", e);
        }catch (IOException e){
            LOG.error("", e);
        }
    }

    protected Graph exportData(){
        Graph g = Factory.createGraphMem();

//        em.getTransaction().begin();
//        em.createNativeQuery("SELECT * WHERE {?s ?p ?o}").getResultList().forEach(
//                r -> g.add(toTriple((Object[])r))
//        );
        getResultList("SELECT * WHERE {?s ?p ?o}").forEach(
                r -> g.add(toTriple((Object[])r))
        );
        return g;
    }


    public void checkData(){
//        List<String> badElements =
//                .stream()
//                .flatMap(r -> Stream.of((Object[])r).map(o -> Objects.toString(o)))
//                .filter(s -> !StringUtils.containsIgnoreCase("http://", (String)s))
//                .collect(Collectors.toList());
        List<String> badElements = new ArrayList<>();
        for(Object r : getResultList("SELECT * WHERE {?s ?p ?o}")){
            Object[] b = (Object[])r;
            List<String> bs = Stream.of(b)
                    .filter(o -> o instanceof URI)
                    .map(o -> Objects.toString(o))
                    .filter(s -> !StringUtils.containsIgnoreCase(s, "http://"))
                    .collect(Collectors.toList());
            badElements.addAll(bs);
        }

        int size = badElements.size();
        System.out.println(size);
    }



    protected Triple toTriple(Object[] terms){
        return Triple.create(
                toNode(terms[0]),
                toNode(terms[1]),
                toNode(terms[2])
                );
    }

    protected List getResultList(String query){
        return em.createNativeQuery(query).getResultList();
    }

    protected void print(List resultList){
        if(resultList == null || resultList.isEmpty())
            return;

        Object obj = resultList.stream().filter(o -> o != null).findFirst().orElse(null);
        if(obj != null && obj.getClass().isArray()) {
            resultList.forEach(r -> System.out.println(
                    Stream.of((Object[]) r).map(Objects::toString).collect(Collectors.joining("; "))
            ));
        }else{
            resultList.forEach(o -> System.out.println(Objects.toString(o)));
        }
    }

    protected Node toNode(Object term){
        if(URI.class.isAssignableFrom(term.getClass())){
            return NodeFactory.createURI(((URI)term).toString());
        }else {
            return NodeFactory.createLiteral(term.toString());
        }
    }
}
