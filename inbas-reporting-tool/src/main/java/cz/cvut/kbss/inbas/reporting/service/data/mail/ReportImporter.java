/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.kbss.inbas.reporting.service.data.mail;

import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import cz.cvut.kbss.jopa.model.descriptors.EntityDescriptor;
import cz.cvut.kbss.ucl.E5XMLLoader;
import cz.cvut.kbss.ucl.NamedStream;
import cz.cvut.kbss.ucl.eccairs.MappingEccairsData2Aso;
import cz.cvut.kbss.ucl.eccairs.SingeltonEccairsAccessFactory;
import cz.cvut.kbss.ucl.eccairs.report.model.EccairsReport;
import java.net.URI;
import java.util.Objects;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import org.apache.jena.rdf.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
@Service
public class ReportImporter{
    
    @Autowired
    protected E5XMLLoader e5XmlLoader;
    
    @Autowired
    protected EntityManagerFactory emf;

    @Autowired
    @Qualifier("eccairsPU")
    protected EntityManagerFactory eccairsEmf;
    
    @Autowired
    protected SingeltonEccairsAccessFactory eaf;
    
    @Autowired
    protected SesameUpdater updater;
    
    protected MappingEccairsData2Aso mapping;
    
    
    @PostConstruct
    protected void init(){
        mapping = new MappingEccairsData2Aso(eaf);
    }
    
    protected void processDelegate(Object o){
        if(o instanceof NamedStream){
            process((NamedStream)o);
        }else if(o instanceof Model){
            process((Model)o);
        }
    }
    
    public void process(NamedStream ns){
//        try {
            System.out.println("processing NamedStream");
            System.out.println(ns.emailId);
            System.out.println(ns.name);
//            byte[] bs = IOUtils.toByteArray(ns.is);
//            System.out.println(new String(bs));
            Stream<EccairsReport> rs = e5XmlLoader.loadData(ns);
            if(rs == null)
                return;
            rs.filter(Objects::nonNull).forEach(r -> {
//                if (r == null) {
//                    return;
//                }
                String suri = "http://onto.fel.cvut.cz/ontologies/report-" + r.getOriginalFile() + "-001";
                URI context = URI.create(suri);
                // TODO convert DummyReport to OccurrenceReport
                EntityManager em = eccairsEmf.createEntityManager();
                em.getTransaction().begin();
                em.persist(r, new EntityDescriptor(context));
                em.getTransaction().commit();
                updater.executeUpdate(
                        mapping.getUFOTypesQuery(r.getTaxonomyVersion(), suri),
                        mapping.generateEventsFromTypes(r.getTaxonomyVersion(), suri),
                        mapping.generatePartOfRelationBetweenEvents(r.getTaxonomyVersion(), suri),
                        mapping.fixOccurrenceReport(r.getTaxonomyVersion(), suri, r.getOccurrence()),
                        mapping.fixOccurrenceAndEvents(r.getTaxonomyVersion(), suri, r.getOccurrence()));
            });
//        } catch (IOException ex) {
//            Logger.getLogger(ReportImporter.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    public void process(Model m){
        System.out.println("processing Model");
    }
    
    
}
