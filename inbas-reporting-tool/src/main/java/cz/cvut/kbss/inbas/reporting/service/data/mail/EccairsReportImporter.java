package cz.cvut.kbss.inbas.reporting.service.data.mail;

import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import cz.cvut.kbss.jopa.model.descriptors.EntityDescriptor;
import cz.cvut.kbss.eccairs.report.e5xml.E5XMLLoader;
import cz.cvut.kbss.eccairs.report.e5xml.commons.NamedStream;
import cz.cvut.kbss.ucl.MappingEccairsData2Aso;
import cz.cvut.kbss.eccairs.schema.dao.SingeltonEccairsAccessFactory;
import cz.cvut.kbss.eccairs.report.model.EccairsReport;
import org.apache.jena.rdf.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.Objects;
import java.util.stream.Stream;
import org.jooq.lambda.Unchecked;

/**
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
@Service
public class EccairsReportImporter implements ReportImporter {

    private static final Logger LOG = LoggerFactory.getLogger(EccairsReportImporter.class);

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
    
    @Autowired
    protected EMailService emailService;

    protected MappingEccairsData2Aso mapping;

    @PostConstruct
    protected void init() {
        mapping = new MappingEccairsData2Aso(eaf);
    }

    @Override
    public Stream<String> processDelegate(Object o) throws Exception {
        if (o instanceof NamedStream) {
            return process((NamedStream) o);
        } else if (o instanceof Model) {
            return process((Model) o);
        }
        return Stream.of();
    }

    @Override
    public Stream<String> process(NamedStream ns) throws Exception {
//        try {
        LOG.trace("processing NamedStream, emailId = {}, name = {}", ns.emailId, ns.name);
//            byte[] bs = IOUtils.toByteArray(ns.is);
//            System.out.println(new String(bs));
        Stream<EccairsReport> rs = e5XmlLoader.prepareFor(ns).loadData();
        if (rs == null) {
            return Stream.of();
        }
        return rs.filter(Objects::nonNull).map(Unchecked.function(r -> {
//                if (r == null) {
//                    return;
//                }
            String suri = "http://onto.fel.cvut.cz/ontologies/report-" + r.getOriginFileName() + "-001";
            URI context = URI.create(suri);
            // TODO convert DummyReport to OccurrenceReport
            EntityManager em = eccairsEmf.createEntityManager();
            try{
                em.getTransaction().begin();
                em.persist(r, new EntityDescriptor(context));
                em.getTransaction().commit();
            }catch(Exception e){// rolback the transanction if something fails
                em.getTransaction().rollback();
                throw e;
            }
            
            try{
                updater.executeUpdate(
                mapping.getUFOTypesQuery(r.getTaxonomyVersion(), suri),
                mapping.generateEventsFromTypes(r.getTaxonomyVersion(), suri),
                mapping.generatePartOfRelationBetweenEvents(r.getTaxonomyVersion(), suri),
                mapping.fixOccurrenceReport(r.getTaxonomyVersion(), suri, r.getOccurrence()),
                mapping.fixOccurrenceAndEvents(r.getTaxonomyVersion(), suri, r.getOccurrence()));
            }catch(Exception e){// rolback the transanction if something fails
                em.remove(r);
                throw e;
            }
            return suri;
        }));
    }

    @Override
    public Stream<String> process(Model m) throws Exception {
        LOG.trace("processing Model");
        return Stream.of();
    }
}
