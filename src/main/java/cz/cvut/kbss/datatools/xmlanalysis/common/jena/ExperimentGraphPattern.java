/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.kbss.datatools.xmlanalysis.common.jena;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.jena.ext.com.google.common.collect.Iterators;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.NodeFactory;
import org.apache.jena.graph.Triple;
import org.apache.jena.graph.impl.TripleStore;
import org.apache.jena.mem.GraphMem;
import org.apache.jena.mem.GraphMemBase;
import org.apache.jena.mem.GraphTripleStoreMem;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.QuerySolutionMap;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.impl.ModelCom;
import org.apache.jena.rdf.model.impl.PropertyImpl;
import org.apache.jena.shared.PrefixMapping;
import org.apache.jena.sparql.syntax.Element;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.util.iterator.SingletonIterator;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
public class ExperimentGraphPattern {
    
    private static final Logger LOG = LoggerFactory.getLogger(ExperimentGraphPattern.class);
    
    public static ModelCom createTrueModel(){
//        TripleStore ts = new GraphTripleStoreMem
        return new ModelCom( new GraphMem(){
            protected ExtendedIterator<Triple> currentFindIterator;
            
            @Override
            protected ExtendedIterator<Triple> graphBaseFind(Node s, Node p, Node o) {
                LOG.info("graphBaseFind(s p o) custome implementation");
                LOG.info("(s p o) = ({} {} {})", s, p, o);
//                currentFindIterator = new SingletonIterator(new Triple(s, p, o));
                return new SingletonIterator(new Triple(s, p, o));
            }

            @Override
            public ExtendedIterator<Triple> graphBaseFind(Triple t) {
                LOG.info("graphBaseFind(t) custome implementation");
                LOG.info("t = {}", t);
                return new SingletonIterator(t);
            }

            @Override
            public boolean graphBaseContains(Triple t) {
                LOG.info("graphBaseContains(t) custome implementation");
                LOG.info("t = {}", t);
                return t.isConcrete() ? true : super.graphBaseContains( t );  //To change body of generated methods, choose Tools | Templates.
            }
        });
        
        
    }
    
    
    public static void experimentWithTrueModel(){
        ModelCom m = createTrueModel();
        boolean b = m.contains(ResourceFactory.createResource("http://exp.com/example"), RDF.type, RDFS.Class);
        System.out.println(b);
    }
    
    public static void experimentWithSPARQLConstructAsTemplate(){
        // input data
        String baseURI = "http://onto.fel.cvut.cz/ontologies/aviation/";
        String className = "incident";
        String personURI = "http://onto.fel.cvut.cz/ontologies/aviation/" + className;
        // template descriptor
        String construct = String.format("PREFIX rdfs: <%s>\n", RDFS.uri)
                + "CONSTRUCT WHERE{\n"
                + "?person a rdfs:Class. \n "
                + "}";
//        String construct = String.format("PREFIX rdfs: <%s>\n", RDFS.uri)
//                + "CONSTRUCT {?person a rdfs:Class. } WHERE{\n"
//                + "BIND(IRI(concat(\"http://onto.fel.cvut.cz/ontologies/aviation/\",str(?className))) as ?person)\n"
////                + "BIND(IRI(\"http://onto.fel.cvut.cz/ontologies/aviation/incident\") as ?person)\n"
//                + "?person a rdfs:Class. \n "
//                + "}";
//        LOG.info("sparql construct query template:\n{}", construct);
        // create the template
//        Model m = ModelFactory.createDefaultModel();
//        m.add(m.getResource(), RDF.type, RDFS.Class);
        
        
        Model m = createTrueModel();
        QueryExecution qe = QueryExecutionFactory.create(construct, m);
        
        // set template parameters according to the input
        QuerySolutionMap qs = new QuerySolutionMap();
//        qs.add("person", ResourceFactory.createResource(personURI));
        qs.add("className", ResourceFactory.createResource(className));
//        qe.getQuery().getQueryPattern().
        qe.setInitialBinding(qs);
        
        // generate a template instance, i.e. a jena Model with triples corresponding to the template instantiated with the input data

        Model m2 = qe.execConstruct();
        
        // write template instance model to console
        m2.write(System.out, "TTL");
    }
    
    
    public static void testInstantiatePattern(){
        String graphPattern = "?person a rdfs:Class.";
        Map<String,RDFNode> varMap = new HashMap<>();
        varMap.put("person", ResourceFactory.createResource("http://onto.fel.cvut.cz/ontologies/person/p-1"));
        Model instance = GraphPatternUtils.instantiatePattern(varMap, graphPattern, null);
        instance.write(System.out, "TTL");
    }
    
    public static void main(String[] args) {
//        experimentWithSPARQLConstructAsTemplate();
//        experimentWithTrueModel();
        testInstantiatePattern();
        
        
    }
}
