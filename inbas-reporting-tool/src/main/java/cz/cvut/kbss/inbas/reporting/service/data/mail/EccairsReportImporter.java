package cz.cvut.kbss.inbas.reporting.service.data.mail;

import cz.cvut.kbss.commons.io.NamedStream;
import cz.cvut.kbss.datatools.mail.CompoundProcessor;
import cz.cvut.kbss.datatools.mail.caa.e5xml.E5XMLLocator;
import cz.cvut.kbss.datatools.mail.model.Message;
import cz.cvut.kbss.eccairs.report.e5xml.E5XMLLoader;
import cz.cvut.kbss.eccairs.report.model.EccairsReport;
import cz.cvut.kbss.eccairs.report.model.dao.EccairsReportDao;
import cz.cvut.kbss.eccairs.schema.dao.SingeltonEccairsAccessFactory;
import cz.cvut.kbss.inbas.reporting.model.Person;
import cz.cvut.kbss.inbas.reporting.model.com.EMail;
import cz.cvut.kbss.inbas.reporting.persistence.dao.EmailDao;
import cz.cvut.kbss.inbas.reporting.service.PersonService;
import cz.cvut.kbss.inbas.reporting.service.event.InvalidateCacheEvent;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import cz.cvut.kbss.jopa.model.descriptors.EntityDescriptor;
import cz.cvut.kbss.ucl.MappingEccairsData2Aso;
import org.apache.jena.rdf.model.Model;
import org.jooq.lambda.Unchecked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import javax.annotation.PostConstruct;
import java.net.URI;
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

    private static final String IMPORTER_USERNAME = "e5xml-data-importer-0001";

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
    private PersonService personService;

    protected MappingEccairsData2Aso mapping;

    private ApplicationEventPublisher eventPublisher;
    protected CompoundProcessor processor = new CompoundProcessor();

    @PostConstruct
    protected void init() {
        mapping = new MappingEccairsData2Aso(eaf);
        processor.registerMessageProcessor(new E5XMLLocator());

        createImporterUser();
//        processor.registerMessageProcessor(new CSAEmailProcessor());
//        processor.registerMessageProcessor(new TISEmailProcessor());
//        processor.registerMessageProcessor(new UZPLNEmailProcessor());
//        processor.registerMessageProcessor(new UZPLNParaEmailProcessor());
    }

    private void createImporterUser() {
        if (personService.findByUsername(IMPORTER_USERNAME) == null) {
            final Person importer = new Person();
            importer.setUsername(IMPORTER_USERNAME);
            importer.setFirstName("importer");
            importer.setLastName("0001");
            importer.setPassword("Importer0001");
            personService.persist(importer);
        }
    }

    @Override
    public List<URI> processDelegate(Object o) throws Exception {
        if (o instanceof NamedStream) {
            return process((NamedStream) o);
        } else if (o instanceof Model) {
            return process((Model) o);
        }
        return Collections.emptyList();
    }

    @Override
    public List<URI> process(NamedStream ns) throws Exception {
        LOG.trace("Processing NamedStream, emailId = {}, name = {}", ns.getEmailId(), ns.getName());
        E5XMLLoader e5XmlLoader = constructE5XMLLoader(ns);
        Stream<EccairsReport> rs = e5XmlLoader.loadData();
        if (rs == null) {
            return Collections.emptyList();
        }
        List<URI> ret = rs.filter(Objects::nonNull).map(Unchecked.function(r -> {
                    String sUri = r.getUri();//"http://onto.fel.cvut.cz/ontologies/report-" + r.getOriginFileName() + "-001";
                    URI context = URI.create(sUri);
                    EntityManager em = eccairsEmf.createEntityManager();
                    EccairsReportDao eccairsDao = new EccairsReportDao(em);
                    try {
                        em.getTransaction().begin();
                        eccairsDao.safePersist(r, new EntityDescriptor(context));
                        em.getTransaction().commit();
                    } catch (Exception e) {// rolback the transanction if something fails
                        em.getTransaction().rollback();
                        LOG.trace("Failed to persist eccairs report from file {}.", r.getOriginFileName(), e);
                        return null;
                    }

                    adjustPersistedReport(r, em);
                    eventPublisher.publishEvent(new InvalidateCacheEvent(this));
//                TODO - LogicalDocument ld = mrs.createNewRevision(Long.MIN_VALUE);
                    return context;
                }

        )).filter(Objects::nonNull).collect(Collectors.toList());
        ns.close();
        return ret;
    }

    private void adjustPersistedReport(EccairsReport r, EntityManager em) {
        final String reportUri = r.getUri();
        try {
            updater.executeUpdate(
                    mapping.getUFOTypesQuery(r.getTaxonomyVersion(), reportUri),
                    mapping.generateEventsFromTypes(r.getTaxonomyVersion(), reportUri),
                    mapping.generatePartOfRelationBetweenEvents(r.getTaxonomyVersion(), reportUri),
                    mapping.fixOccurrenceReport(r.getTaxonomyVersion(), reportUri, r.getOccurrence()),
                    mapping.fixOccurrenceAndEvents(r.getTaxonomyVersion(), reportUri, r.getOccurrence()));
//                TODO - LogicalDocument ld = mrs.createNewRevision(Long.MIN_VALUE);
        } catch (Exception e) {
            LOG.error("Mapping eccairs report {} to reporting tool report failed. Reverting changes.",
                    r.getOriginFileName(), e);
            em.getTransaction().begin();
            final Object toRemove = em.merge(r);
            em.remove(toRemove);
            em.getTransaction().commit();
        }
    }

    @Override
    public List<URI> process(Model m) throws Exception {
        LOG.trace("Processing Model");
        return Collections.emptyList();
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
                    LOG.info("Something went wrong while importing an attachment of the email with id : {}", id, e);
                }
                return Stream.of();
            }).filter(x -> x != null).collect(Collectors.toSet());

            if (imported.isEmpty()) {
                emailDao.remove(email);
            } else {
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
