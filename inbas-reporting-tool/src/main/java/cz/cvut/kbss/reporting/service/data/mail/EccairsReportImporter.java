package cz.cvut.kbss.reporting.service.data.mail;

import cz.cvut.kbss.commons.io.NamedStream;
import cz.cvut.kbss.datatools.mail.CompoundProcessor;
import cz.cvut.kbss.datatools.mail.caa.e5xml.E5XMLLocator;
import cz.cvut.kbss.datatools.mail.model.Message;
import cz.cvut.kbss.eccairs.report.e5xml.E5XMLLoader;
import cz.cvut.kbss.eccairs.report.e5xml.e5f.E5FXMLParser;
import cz.cvut.kbss.eccairs.report.e5xml.e5f.E5FXMLParserEccairs;
import cz.cvut.kbss.eccairs.report.model.EccairsReport;
import cz.cvut.kbss.eccairs.report.model.dao.EccairsReportDao;
import cz.cvut.kbss.eccairs.schema.dao.SingeltonEccairsAccessFactory;
import cz.cvut.kbss.jopa.exceptions.OWLPersistenceException;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import cz.cvut.kbss.jopa.model.descriptors.EntityDescriptor;
import cz.cvut.kbss.reporting.model.com.EMail;
import cz.cvut.kbss.reporting.persistence.dao.EmailDao;
import cz.cvut.kbss.reporting.service.event.InvalidateCacheEvent;
import cz.cvut.kbss.ucl.MappingEccairsData2Aso;
import org.apache.commons.io.IOUtils;
import org.apache.jena.rdf.model.Model;
import org.jooq.lambda.Unchecked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
public class EccairsReportImporter implements ReportImporter, ApplicationEventPublisherAware {

    private static final Logger LOG = LoggerFactory.getLogger(EccairsReportImporter.class);

    @Autowired
    @Qualifier("eccairsPU")
    protected EntityManagerFactory eccairsEmf;

    @Autowired
    protected SingeltonEccairsAccessFactory eaf;

    @Autowired
    protected EmailDao emailDao;
    
    @Autowired
    private SafaImportService safaImportService;

    protected MappingEccairsData2Aso mapping;
    
    private ApplicationEventPublisher eventPublisher;
    protected CompoundProcessor processor = new CompoundProcessor();

    @PostConstruct
    protected void init() {
        mapping = new MappingEccairsData2Aso(eaf);
        processor.registerMessageProcessor(new E5XMLLocator(){
            @Override
            public Object processMessage(Message m) {
                String id = m.getId();
                Stream<NamedStream> s = (Stream<NamedStream>) super.processMessage(m);
                return s.flatMap(x -> {
                    try {
                        return processDelegate(x).stream();
                    } catch (Exception e) {
                        LOG.info(String.format("Something went wrong while importing an attachment of the email with id : %s", id), e);
                    }
                    return Stream.of();
                }).filter(x -> x != null);
            }
            
        });
        // TODO recognize safa emails, subject = "RAMP data on Czech operators", attachment to download is of name "CZ OPR RAMP data% SI_STARTTIME%.xlsx"
        processor.registerMessageProcessor(safaImportService.getSafaAuditReportImporterProcessor());
//        processor.registerMessageProcessor(new CSAEmailProcessor());
//        processor.registerMessageProcessor(new TISEmailProcessor());
//        processor.registerMessageProcessor(new UZPLNEmailProcessor());
//        processor.registerMessageProcessor(new UZPLNParaEmailProcessor());
    }


    @Override
    public List<URI> processDelegate(Object o) throws Exception {
        if (o instanceof NamedStream) {
            return process((NamedStream) o);
        }
        return Collections.emptyList();
    }

    @Override
    public List<URI> process(NamedStream ns) throws IOException {
        LOG.trace("Processing NamedStream, emailId = {}, name = {}", ns.getEmailId(), ns.getName());
        E5XMLLoader e5XmlLoader = constructE5XMLLoader(ns);
        Stream<EccairsReport> rs = e5XmlLoader.loadData();
        if (rs == null) {
            return Collections.emptyList();
        }
        List<URI> ret = parsePersistAndMap(rs).stream().map(r -> URI.create(r.getUri())).collect(Collectors.toList());
        ns.close();
        return ret;
    }
    
    private List<EccairsReport> parsePersistAndMap(Stream<EccairsReport> rs){
        return rs.filter(Objects::nonNull).map(Unchecked.function(r -> {
                    String sUri = r.createRandomUri();
                    URI context = URI.create(sUri);
                    EntityManager em = eccairsEmf.createEntityManager();
                    EccairsReportDao eccairsDao = new EccairsReportDao(em);
                    try {
                        em.getTransaction().begin();
                        eccairsDao.safePersist(r, new EntityDescriptor(context));
                        em.getTransaction().commit();
                    } catch (Exception e) {// rolback the transanction if something fails
                        em.getTransaction().rollback();
                        LOG.trace(String.format("failed to persisting eccairs report from file %s.", r.getOriginFileName()), e);
                        return null;
                    }

                    map2OccurrenceReport(r, em);
                    eventPublisher.publishEvent(new InvalidateCacheEvent(this));
//                TODO - LogicalDocument ld = mrs.createNewRevision(Long.MIN_VALUE);
                    return r;
                }

        )).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private void map2OccurrenceReport(EccairsReport r, EntityManager em) {
        final String reportUri = r.getUri();
        try {
            em.getTransaction().begin();
            mapping.mapReport(r, em, reportUri);
            em.getTransaction().commit();
        } catch (OWLPersistenceException e) {
            LOG.error(String.format("Mapping eccairs report %s to reporting tool report failed. Reverting changes.",
                    r.getOriginFileName()), e);
            if(em.getTransaction().isActive())
                em.getTransaction().rollback();

            em.getTransaction().begin();
            final Object toRemove = em.merge(r);
            em.remove(toRemove);
            em.getTransaction().commit();
        }
    }

    
    
    @Override
    public List<URI> process(Model m) throws Exception {
        throw new UnsupportedOperationException("Importing a jena model is not supported yet.");
//        LOG.trace("Processing Model");
//        return Collections.emptyList();
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;
        this.safaImportService.setApplicationEventPublisher(applicationEventPublisher);
    }
    
    /**
     * This method imports a report from a reportStr. 
     * @param reportStr string of the contents of an e5f/xml file (the unziped xml file).
     * @return
     */
    @Override
    public List<EccairsReport> importE5FXmlFromString(String reportStr){ 
        try {
            E5FXMLParser e5fXmlParser = new E5FXMLParserEccairs(eaf);
            e5fXmlParser.parseDocument(new NamedStream("imported-from-eccairs", IOUtils.toInputStream(reportStr, Charset.forName("UTF-8"))));
            EccairsReport r = e5fXmlParser.getReport();
            return parsePersistAndMap(Stream.of(r));
        } catch (IOException ex) {
            RuntimeException rex = new RuntimeException("An unexpected exception has been thrown");
            rex.addSuppressed(ex);
            throw rex;
        }
    }

    @Override
    public List<URI> process(Message m) {

        String id = m.getId();
        Object o = processor.processMessage(m); //To change body of generated methods, choose Tools | Templates.=
        if (o != null) {
            if (!(o instanceof Stream)) {
                o = Stream.of(o);
            }
            Stream<URI> s = (Stream<URI>) o;
            Set<URI> imported = s.collect(Collectors.toSet());
            
            if (!imported.isEmpty()) {// TODO: implememnt in EMailService and enclose in transaction.
                EMail email = emailDao.findByMailId(id);
                if (email == null) {
                    email = new EMail();
                    email.setId(id);
                    emailDao.persist(email);
                }
                emailDao.persist(email);
                email.getReports().addAll(imported);
                emailDao.update(email);
            }
        }
        return Collections.emptyList();
    }


    private E5XMLLoader constructE5XMLLoader(NamedStream ns) {
        return new E5XMLLoader(ns, eaf);
    }
}
