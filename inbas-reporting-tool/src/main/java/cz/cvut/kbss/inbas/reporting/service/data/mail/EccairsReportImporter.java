package cz.cvut.kbss.inbas.reporting.service.data.mail;

import cz.cvut.kbss.inbas.reporting.service.event.InvalidateCacheEvent;
import cz.cvut.kbss.datatools.mail.CompoundProcessor;
import cz.cvut.kbss.datatools.mail.caa.e5xml.E5XMLLocator;
import cz.cvut.kbss.datatools.mail.model.Message;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import cz.cvut.kbss.jopa.model.descriptors.EntityDescriptor;
import cz.cvut.kbss.eccairs.report.e5xml.E5XMLLoader;
import cz.cvut.kbss.commons.io.NamedStream;
import cz.cvut.kbss.eccairs.report.e5xml.e5x.E5XXMLParser;
import cz.cvut.kbss.ucl.MappingEccairsData2Aso;
import cz.cvut.kbss.eccairs.schema.dao.SingeltonEccairsAccessFactory;
import cz.cvut.kbss.eccairs.report.model.EccairsReport;
import cz.cvut.kbss.inbas.reporting.model.LogicalDocument;
import cz.cvut.kbss.inbas.reporting.model.com.EMail;
import cz.cvut.kbss.inbas.reporting.persistence.dao.EmailDao;
import cz.cvut.kbss.inbas.reporting.service.MainReportService;
import org.apache.jena.rdf.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jooq.lambda.Unchecked;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

/**
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
@Service
public class EccairsReportImporter implements ReportImporter, ApplicationEventPublisherAware {

    private static final Logger LOG = LoggerFactory.getLogger(EccairsReportImporter.class);

//    @Autowired
//    protected E5XMLLoader e5XmlLoader;

    @Autowired
    protected EntityManagerFactory emf;

    @Autowired
    @Qualifier("eccairsPU")
    protected EntityManagerFactory eccairsEmf;

    @Autowired
    protected SingeltonEccairsAccessFactory eaf;

    @Autowired
    protected EmailDao emailDao;

    @Autowired
    protected SesameUpdater updater;

    @Autowired
    protected EMailService emailService;
    
    @Autowired
    protected MainReportService mrs;

    protected MappingEccairsData2Aso mapping;

    private ApplicationEventPublisher eventPublisher;
    protected CompoundProcessor processor = new CompoundProcessor();

    @PostConstruct
    protected void init() {
        mapping = new MappingEccairsData2Aso(eaf);
        processor.registerMessageProcessor(new E5XMLLocator());
//        processor.registerMessageProcessor(new CSAEmailProcessor());
//        processor.registerMessageProcessor(new TISEmailProcessor());
//        processor.registerMessageProcessor(new UZPLNEmailProcessor());
//        processor.registerMessageProcessor(new UZPLNParaEmailProcessor());
    }

    @Override
    public List<URI> processDelegate(Object o) throws Exception {
        if (o instanceof NamedStream) {
            return process((NamedStream) o);
        } else if (o instanceof Model) {
            return process((Model) o);
        }
        return Collections.EMPTY_LIST;
    }

    /**
     * This method loads a report from a NamedStream, e.g. file or email attachment.
     * 
     * @param ns
     * @return
     * @throws Exception 
     */
    @Override
    public List<URI> process(NamedStream ns) throws Exception {
//        try {
        LOG.trace("processing NamedStream, emailId = {}, name = {}", ns.getEmailId(), ns.getName());
//            byte[] bs = IOUtils.toByteArray(ns.is);
//            System.out.println(new String(bs));
        E5XMLLoader e5XmlLoader = constructE5XMLLoader();
        Stream<EccairsReport> rs = e5XmlLoader.prepareFor(ns).loadData();
        if (rs == null) {
            return Collections.EMPTY_LIST;
        }
        List<URI> ret = rs.filter(Objects::nonNull).map(Unchecked.function(r -> {
//                if (r == null) {
//                    return;
//                }
            String suri = r.getUri();//"http://onto.fel.cvut.cz/ontologies/report-" + r.getOriginFileName() + "-001";
            URI context = URI.create(suri);
            EntityManager em = eccairsEmf.createEntityManager();
            try {
                em.getTransaction().begin();
                em.persist(r, new EntityDescriptor(context));
                em.getTransaction().commit();
            } catch (Exception e) {// rolback the transanction if something fails
                em.getTransaction().rollback();
                LOG.trace(String.format("failed to persisting eccairs report from file {}.", r.getOriginFileName()),e);
                return null;
            }

            try {
                updater.executeUpdate(
                        mapping.getUFOTypesQuery(r.getTaxonomyVersion(), suri),
                        mapping.generateEventsFromTypes(r.getTaxonomyVersion(), suri),
                        mapping.generatePartOfRelationBetweenEvents(r.getTaxonomyVersion(), suri),
                        mapping.fixOccurrenceReport(r.getTaxonomyVersion(), suri, r.getOccurrence()),
                        mapping.fixOccurrenceAndEvents(r.getTaxonomyVersion(), suri, r.getOccurrence()));
                eventPublisher.publishEvent(new InvalidateCacheEvent(this));
//                TODO - LogicalDocument ld = mrs.createNewRevision(Long.MIN_VALUE);
            } catch (Exception e) {// rolback the transanction if something fails
                em.remove(r);
                LOG.trace(String.format("mapping eccairs report {} to reporting tool report failed.", r.getOriginFileName()), e);
            }
            return context;
        })).filter(Objects::nonNull).collect(Collectors.toList());
        ns.close();
        return ret;
    }

    @Override
    public List<URI> process(Model m) throws Exception {
        LOG.trace("processing Model");
        return Collections.EMPTY_LIST;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;
    }

    @Override
    public List<URI> process(Message m) {

        String id = m.getId();
        EMail email = emailDao.findByMailId(id);
        if (email == null) {
            email = new EMail();
            email.setId(id);
            emailDao.persist(email);
        }
        Object o = processor.processMessage(m); //To change body of generated methods, choose Tools | Templates.=
        if (o != null) {
            if (!(o instanceof Stream)) {
                o = Stream.of(o);
            }
            Stream<?> s = (Stream) o;
            Set<URI> imported = s.flatMap(x -> {
                try {
                    return this.processDelegate(x).stream();
                } catch (Exception e) {
                    LOG.info(String.format("Something went wron while importing an attachment of the email with id : %s", id), e);
                }
                return Stream.of();
            }).filter(x -> x != null).
               collect(Collectors.toSet());

            if (imported.isEmpty()) {
                emailDao.remove(email);
            } else {
                email.getReports().addAll(imported);
                emailDao.update(email);
            }
        }
        return Collections.EMPTY_LIST;
    }
    
    
    protected E5XMLLoader constructE5XMLLoader() {
        E5XMLLoader loader = new E5XMLLoader();
        E5XXMLParser parser = new E5XXMLParser(eaf);
        loader.setE5xParser(parser);
        return loader;
    }
}
