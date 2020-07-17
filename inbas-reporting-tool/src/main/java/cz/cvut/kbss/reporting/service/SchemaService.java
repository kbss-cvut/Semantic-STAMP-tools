package cz.cvut.kbss.reporting.service;

import cz.cvut.kbss.datatools.bpm2stampo.converters.AbstractProcessModelExporter;
import cz.cvut.kbss.datatools.bpm2stampo.converters.BPMNConverterRegistry;
import cz.cvut.kbss.reporting.util.ConfigParam;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.vocabulary.DC;
import org.eclipse.rdf4j.model.vocabulary.OWL;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQuery;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.query.Update;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class SchemaService {

    private static final Logger LOG = LoggerFactory.getLogger(SchemaService.class);

    private static final List<RDFFormat> formats =  Arrays.asList(
            RDFFormat.BINARY, RDFFormat.JSONLD, RDFFormat.N3,
            RDFFormat.NQUADS, RDFFormat.NTRIPLES, RDFFormat.RDFXML,
            RDFFormat.TURTLE, RDFFormat.TRIG, RDFFormat.TRIX
    );

    @Autowired
    private ConfigReader configReader;

    public void saveSchema(String fileName, InputStream content, boolean replaceExisting) {

        // check file extension
        String ext = fileName.substring(fileName.lastIndexOf('.') + 1, fileName.length());
        if (! (ext.equals("xml") || ext.equals("bpm")) ) {
            // transform the input
            saveSchemaRDF(fileName, content, replaceExisting);
        }else{
            saveSchemaBPM(fileName, content, replaceExisting);
        }
    }



    public void saveSchemaBPM(String fileName, InputStream inputStream, boolean replaceExisting){
        AbstractProcessModelExporter processor = BPMNConverterRegistry.get(fileName);
        InputStream rdfInputStream = processor.convertToStream(fileName, inputStream);
        saveSchemaRDF(fileName, rdfInputStream, replaceExisting);
    }


    public void saveSchemaRDF(String fileName, InputStream content, boolean replace){
        String repositoryUrl = configReader.getConfig(ConfigParam.EVENT_TYPE_REPOSITORY_URL);
        HTTPRepository r = new HTTPRepository(repositoryUrl);
        r.initialize();
        RepositoryConnection c = r.getConnection();
        if(replace) {
            // clear repo contents
            clearSchemaRepo(c);
        }

        // load new schema into repo
        loadNewSchemaIntoRepo(c, fileName, content, RDFFormat.RDFXML, repositoryUrl);
    }

    protected void clearSchemaRepo(RepositoryConnection c){
        c.begin();
        if(!c.isEmpty()){
            Update u1 = c.prepareUpdate("DELETE{?s ?p ?o}WHERE{?s ?p ?o}");
            u1.execute();
        }
        c.commit();
    }

    protected void loadNewSchemaIntoRepo(RepositoryConnection c, String fileName, InputStream content, RDFFormat ff, String repositoryUrl){
        SimpleValueFactory vf = SimpleValueFactory.getInstance();
        c.begin();
        try {
            c.add(content, null, ff);
            Resource res = vf.createBNode();
            c.add(res, RDF.TYPE, OWL.ONTOLOGY);
            c.add(res, RDFS.LABEL, vf.createLiteral(fileName));
            c.add(res, DC.DATE, vf.createLiteral(new Date()));
        } catch (IOException e) {
            LOG.warn("could not save file \"{}\" with guessed format \"{}\" to repo \"{}\".", fileName, ff, repositoryUrl);
        }
        c.commit();
    }


    public Map<String, String> getSchemaMetadata() {
        String repositoryUrl = configReader.getConfig(ConfigParam.EVENT_TYPE_REPOSITORY_URL);
        HTTPRepository r = new HTTPRepository(repositoryUrl);
        r.initialize();
        RepositoryConnection c = r.getConnection();
        SimpleValueFactory vf = SimpleValueFactory.getInstance();
        // clear repo contents
        Map<String, String> ret = null;
//        String schemaName = null;
        c.begin();
        if(!c.isEmpty()){
            TupleQuery tq = c.prepareTupleQuery("SELECT ?l ?d {?s ?a ?t. ?s ?labelProp ?l. ?s ?dateProp ?d.}");
            tq.setBinding("a", RDF.TYPE);
            tq.setBinding("t", OWL.ONTOLOGY);
            tq.setBinding("labelProp", RDFS.LABEL);
            tq.setBinding("dateProp", DC.DATE);
            TupleQueryResult res = tq.evaluate();
            while(res.hasNext()){
                BindingSet bs = res.next();
                ret = new HashMap<>();
                ret.put("name", bs.getValue("l").stringValue());
                ret.put("date", bs.getValue("d").stringValue());
                if(res.hasNext()){
                    LOG.warn("There are more than one schema names in the schema repo.");
                }
            }
        }
        c.commit();
        return ret;
    }
}
