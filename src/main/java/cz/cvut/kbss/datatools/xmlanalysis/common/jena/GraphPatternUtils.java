/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.kbss.datatools.xmlanalysis.common.jena;

import static cz.cvut.kbss.datatools.xmlanalysis.common.jena.ExperimentGraphPattern.createTrueModel;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolutionMap;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.shared.PrefixMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
public class GraphPatternUtils {
    
    private static final Logger LOG = LoggerFactory.getLogger(GraphPatternUtils.class);
    
    /**
     * A utility method to instantiate a graphpattern given a mapping of variables 
     * in varMap. 
     * @param varMap variable mappings
     * @param graphPattern the graph pattern to be instantiated
     * @param prefixes if the graph pattern uses non standard prefixes, standard prefixes are, e.g. rdf,rdfs {@link PrefixMapping#Standard}
     * @return 
     */
    public static Model instantiatePattern(Map<String,RDFNode> varMap, String graphPattern, PrefixMapping prefixes){
        Function<Map<String,String>,String> renderPrefixes = map -> map.entrySet().stream()
                .map(e -> String.format("PREFIX %s: <%s>",e.getKey(), e.getValue()))
                .collect(Collectors.joining("\n"));
        
        Map<String,String> allPrefixes = new HashMap<>();
        allPrefixes.putAll(PrefixMapping.Standard.getNsPrefixMap());
        if(prefixes != null)
            allPrefixes.putAll(prefixes.getNsPrefixMap());
        String prefixesStr= renderPrefixes.apply(allPrefixes);
                    
        
        String construct = String.format("%s\nCONSTRUCT WHERE{%s}", prefixesStr, graphPattern);
        LOG.debug(construct);
        Model trueModel = createTrueModel();
        QueryExecution qe = QueryExecutionFactory.create(construct, trueModel);
        QuerySolutionMap qs = new QuerySolutionMap();
        varMap.forEach((k,v) -> qs.add(k, v));
        qe.setInitialBinding(qs);
        return qe.execConstruct();
    }
}
