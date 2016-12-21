/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.kbss.inbas.reporting.service.data.eccairs;

import cz.cvut.kbss.eccairs.Vocabulary;
import cz.cvut.kbss.eccairs.report.model.EccairsReport;
import cz.cvut.kbss.eccairs.report.model.dao.EccairsReportDao;
import cz.cvut.kbss.eccairs.schema.dao.SingeltonEccairsAccessFactory;
import cz.cvut.kbss.inbas.reporting.model.OccurrenceReport;
import cz.cvut.kbss.inbas.reporting.model.repo.ImportEvent;
import cz.cvut.kbss.inbas.reporting.model.repo.RemoteReportRepository;
import cz.cvut.kbss.inbas.reporting.service.data.eccairs.change.EccairsReportChage;
import cz.cvut.kbss.inbas.reporting.service.data.eccairs.change.EccairsRepositoryChange;
import cz.cvut.kbss.inbas.reporting.service.data.mail.ReportImporter;
import cz.cvut.kbss.inbas.reporting.service.repository.RemoteReportRepositoryService;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import cz.cvut.kbss.jopa.model.descriptors.Descriptor;
import cz.cvut.kbss.jopa.model.descriptors.EntityDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
public class EccairsReportSynchronizationService {
    private static final Logger LOG = LoggerFactory.getLogger(EccairsReportSynchronizationService.class);
    @Autowired
    private RemoteReportRepositoryService rrrService;

    @Autowired
    private EccairsService eccairsService;

    @Autowired
    @Qualifier("eccairsPU")
    protected EntityManagerFactory eccairsEmf;

    @Autowired
    protected SingeltonEccairsAccessFactory eaf;

    @Autowired
    private ReportImporter reportImporter;

    private EccairsReportDao eccairsReportDao;


    public OccurrenceReport getNewEccairsRevision(OccurrenceReport or) {
        Objects.requireNonNull(or);
        or.getUri();
        EntityManager em = eccairsEmf.createEntityManager();
        eccairsReportDao = new EccairsReportDao(em);
        EccairsReport e = em.find(EccairsReport.class, or.getUri().toString());
        OccurrenceReport newer = null;
        if (e != null) {
            Calendar lastUpdate = getLastUpdateTime();
            EccairsRepositoryChange change = eccairsService
                    .getLatestChanges(lastUpdate, Collections.singleton(e.getEccairsKey()));
            if (!change.isEmpty()) { // found a report in eccairs no need
                LOG.trace("importing from eccairs. found change for report with eccairs key {}", e.getEccairsKey());
                if (change.size() != 1) {
                    LOG.error("found multiple changed or created reports eccairs report key {}", e.getEccairsKey());
                }
                importReportsFromChange(change);
            } else { // try to find previously loaded change
                rrrService.getLatestEccairsVersionOf(or.getUri());
            }
        } else {
            // currently non eccairs reports are not matched to eccairs repository. 
            // Matched reports are e5f and e5x reports imported from emails and e5f reports imported directly from Eccairs Repository
        }
        em.close();
        return null;
    }

    //    @Scheduled(cron = "0 1 0 * * *")//  at one minute past midnight (00:01) every day, assuming that the default shell for the cron user is Bourne shell compliant:
    @PostConstruct
    public void scheduledSynchronizeWithEccairsSystem() {
        LOG.info("importing data from eccairs server.");
        Calendar lastUpdate = getLastUpdateTime();

        EccairsRepositoryChange change = eccairsService.getLatestChanges(lastUpdate, null);
        importReportsFromChange(change);
    }

    protected Set<EccairsReport> importReportsFromChange(EccairsRepositoryChange change) {
        Set<EccairsReport> allImported = new HashSet<>();
        EntityManager eccairsEm = eccairsEmf.createEntityManager();
        eccairsReportDao = new EccairsReportDao(eccairsEm);

        for (EccairsReportChage c : change.getChanges().values()) {
            if (c.isCreated()) {
                allImported.addAll(importNewRport(c.getReportStr()));
            } else if (c.isEdited()) {
                allImported.addAll(importChangedReport(c.getReportStr()));
            }
        }

        logImportEvent(allImported, change.getCheckDate().getTime());
        eccairsEm.close();
        return allImported;
    }

    protected Calendar getLastUpdateTime() {
        Calendar start = GregorianCalendar.getInstance();
        RemoteReportRepository rrr = rrrService.getEccairsReportRepository();
        ImportEvent ie = rrr.getLastImportEvent();
        if (ie == null) {
            start.setTimeInMillis(0);
        } else {
            start.setTime(ie.getEventDate());
        }
        return start;
    }

    protected void logImportEvent(Set<EccairsReport> imported, Date start) {
        if (imported != null && !imported.isEmpty()) {
            ImportEvent ie = new ImportEvent();
            ie.setEventDate(start);
            ie.setImportedDocuments(imported.stream().map(er -> URI.create(er.getUri())).collect(Collectors.toSet()));
            LOG.info("creating and persisting import event from eccairs server. #{} reporst were imported.",
                    ie.getImportedDocuments().size());
            rrrService.addImportEvent(ie, rrrService.getEccairsReportRepository());
        }
    }

    protected List<EccairsReport> importNewReports(List<String> reports) {
        List<EccairsReport> allNewReports = new ArrayList<>();
        for (String report : reports) {
            allNewReports.addAll(importNewRport(report));
        }
        return allNewReports;
    }

    protected List<EccairsReport> importNewRport(String report) {
        List<EccairsReport> imported = importAndMatchNewReportToExisting(report);
        return imported != null ? imported : Collections.EMPTY_LIST;
    }

    protected List<EccairsReport> importAndMatchNewReportToExisting(String reportStr) {
        List<EccairsReport> importedReports = reportImporter.importE5FXmlFromString(reportStr);
        for (EccairsReport r : importedReports) {
            r.getTypes().add(Vocabulary.s_c_report_from_eccairs);
            String ruri = r.getUri();
            List<EccairsReport> matchedReports = matchReportToExisting(r);
            if (matchedReports != null && !matchedReports.isEmpty()) {
                r.getTypes().add(cz.cvut.kbss.inbas.reporting.model.Vocabulary.s_c_read_only);
            }
            Descriptor d = new EntityDescriptor(URI.create(ruri));
            eccairsReportDao.update(r, d);
        }
        return importedReports;
    }

    protected List<EccairsReport> matchReportToExisting(final EccairsReport r) {
        Objects.requireNonNull(r);
        // match 
        List<EccairsReport> matchingReports = new ArrayList<>();
        if (r.getEccairsKey() != null) {
            matchingReports.addAll(eccairsReportDao.findByEccairsKey(r.getEccairsKey()));
        }
        if (r.getTypes().contains(Vocabulary.s_c_report_from_eccairs)) {
            matchingReports.addAll(eccairsReportDao.findMatchingEccairReportByReportingEntityAndEntityFileNumber(r));
        }
        if (!matchingReports.isEmpty()) {
            r.setRevisionsOf(matchingReports.stream().filter(er -> !er.getUri().equals(r.getUri()))
                                            .map(er -> URI.create(er.getUri())).collect(Collectors.toSet()));
        }
        return matchingReports;
    }


    protected List<EccairsReport> importChangedReports(List<String> reports) {
        List<EccairsReport> allChangedReports = new ArrayList<>();
        for (String report : reports) {
            List<EccairsReport> changed = importChangedReport(report);
            if (changed != null) {
                allChangedReports.addAll(changed);
            }
        }
        return allChangedReports;
    }

    protected List<EccairsReport> importChangedReport(String reportStr) {
        List<EccairsReport> changedReports = reportImporter.importE5FXmlFromString(reportStr);
        for (EccairsReport r : changedReports) {
            r.getTypes().add(Vocabulary.s_c_report_from_eccairs);
            String key = r.getEccairsKey();
            if (key != null && !key.isEmpty()) {
                EccairsReport latestImported = getLatestImportedByKey(key);
                if (latestImported == null) {
                    matchReportToExisting(r);
                } else {
                    r.setRevisionsOf(latestImported.getRevisionsOf());
                }
            }
            Descriptor d = new EntityDescriptor(URI.create(r.getUri()));
            eccairsReportDao.update(r, d);
        }
        return changedReports != null ? changedReports : Collections.EMPTY_LIST;
    }


    protected EccairsReport getLatestImportedByKey(String key) {
        String query = "SELECT ?reprot {\n"
                + String.format("?event <%s> ?report.\n", cz.cvut.kbss.inbas.reporting.model.Vocabulary.s_p_has_target)
                +
                String.format("?event <%s> ?date.\n", cz.cvut.kbss.inbas.reporting.model.Vocabulary.s_p_has_start_time)
                + String.format("?report <%s> \"%s\"@en.", cz.cvut.kbss.eccairs.Vocabulary.hasId, key)
                + "}ORDER BY ?date\n"
                + "LIMIT 1";
        return eccairsReportDao.findByQuery(query);
    }

    public List<EccairsReport> findImportedEccairsReportByReportingEntityAndEntityFileNumber(String reportingEntity,
                                                                                             String reportingEntityFileNumber) {
        EntityManager em = eccairsEmf.createEntityManager();
        EccairsReportDao eccairsReportDao = new EccairsReportDao(em);
        em.getTransaction().begin();
        List<EccairsReport> rs = eccairsReportDao
                .findEccairReportByReportingEntityAndEntityFileNumber(reportingEntity, reportingEntity);
        em.getTransaction().commit();
        return rs;
    }

    public EccairsService getEccairsService() {
        return eccairsService;
    }

    public void setEccairsService(EccairsService eccairsService) {
        this.eccairsService = eccairsService;
    }

}
