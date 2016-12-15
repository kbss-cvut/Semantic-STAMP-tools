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
import cz.cvut.kbss.inbas.reporting.model.repo.ImportEvent;
import cz.cvut.kbss.inbas.reporting.service.data.mail.ReportImporter;
import cz.cvut.kbss.inbas.reporting.service.repository.RemoteReportRepositoryService;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import cz.cvut.kbss.jopa.model.descriptors.Descriptor;
import cz.cvut.kbss.jopa.model.descriptors.EntityDescriptor;
import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
@Service
public class EccairsReportSynchronizationService {
    
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
    
    //    @Scheduled(cron = "0 1 0 * * *")//  at one minute past midnight (00:01) every day, assuming that the default shell for the cron user is Bourne shell compliant:
    public void scheduledSynchronizeWithEccairsSystem(){
        
//        TODO import eccairs reports
//        Even
        Calendar start = GregorianCalendar.getInstance();
        Set<EccairsReport> allImported = new HashSet<>();
        EccairsService.EccairsRepositoryChange change = eccairsService.getLatestChanges(start);
        
        List<String> newlyCreatedReports = new ArrayList<>(change.getNewReports().values());
        List<String> updatedReports = new ArrayList<>(change.getChangedReports().values());
        
        EntityManager eccairsEm = eccairsEmf.createEntityManager();
        eccairsReportDao = new EccairsReportDao(eccairsEm);
        allImported.addAll(importNewReports(newlyCreatedReports));
        allImported.addAll(importChangedReports(updatedReports));
        logImportEvent(allImported, start.getTime());
    }
    
    protected void logImportEvent(Set<EccairsReport> imported, Date start){
        ImportEvent ie = new ImportEvent();
        ie.setEventDate(start);
        ie.setImportedDocuments(imported.stream().map(er -> URI.create(er.getUri())).collect(Collectors.toSet()));
        rrrService.addImportEvent(ie, rrrService.getEccairsReportRepository());
    }
    
    protected List<EccairsReport> importNewReports(List<String> reports){
        List<EccairsReport> allNewReports = new ArrayList<>();
        for(String report : reports){
            List<EccairsReport> imported = importAndMatchNewReportToExisting(report);
            if(imported != null){
                allNewReports.addAll(imported);
            }
        }
        return allNewReports;
    }
    
    protected List<EccairsReport> importAndMatchNewReportToExisting(String reportStr){
        List<EccairsReport> importedReports = reportImporter.importE5FXmlFromString(reportStr);
        for(EccairsReport r : importedReports){
            r.getTypes().add(Vocabulary.s_c_report_from_eccairs);
            String ruri = r.getUri();
            matchReportToExisting(r);
            Descriptor d = new EntityDescriptor(URI.create(ruri));
            eccairsReportDao.update(r, d);
        }
        return importedReports;
    }
    
    protected List<EccairsReport> matchReportToExisting(EccairsReport r){
        List<EccairsReport> matchingReports = eccairsReportDao.findMatchingEccairReportByReportingEntityAndEntityFileNumber(r);
        if(matchingReports != null && !matchingReports.isEmpty()){
            r.setRevisionsOf(matchingReports.stream().map(er -> URI.create(er.getUri())).collect(Collectors.toSet()));
        }
        return matchingReports;
    }
    
    
    protected List<EccairsReport> importChangedReports(List<String> reports){
        List<EccairsReport> allChangedReports = new ArrayList<>();
        for(String report : reports){
            List<EccairsReport> changed = importChangedReport(report);
            if(changed != null){
                allChangedReports.addAll(changed);
            }
        }
        return allChangedReports;
    }
    
    protected List<EccairsReport> importChangedReport(String reportStr){
        List<EccairsReport> changedReports = reportImporter.importE5FXmlFromString(reportStr);
        for(EccairsReport r : changedReports){
            r.getTypes().add(Vocabulary.s_c_report_from_eccairs);
            String key = r.getEccairsKey();
            if(key != null && !key.isEmpty()){
                EccairsReport latestImported = getLatestImportedByKey(key);
                if(latestImported == null){
                    matchReportToExisting(r);
                }else{
                    r.setRevisionsOf(latestImported.getRevisionsOf());
                }
            }
            Descriptor d = new EntityDescriptor(URI.create(r.getUri()));
            eccairsReportDao.update(r, d);
        }
        return changedReports;
    }
    
    
    protected EccairsReport getLatestImportedByKey(String key){
        String query = "SELECT ?reprot {\n"
                    + String.format("?event <%s> ?report.\n",cz.cvut.kbss.inbas.reporting.model.Vocabulary.s_p_has_target)
                    + String.format("?event <%s> ?date.\n", cz.cvut.kbss.inbas.reporting.model.Vocabulary.s_p_has_start_time)
                    + String.format("?report <%s> \"%s\"@en.",cz.cvut.kbss.eccairs.Vocabulary.hasId, key)
                    + "}ORDER BY ?date\n"
                    + "LIMIT 1";
        return eccairsReportDao.findByQuery(query);
    }
    
    public List<EccairsReport> findImportedEccairsReportByReportingEntityAndEntityFileNumber(String reportingEntity, String reportingEntityFileNumber){
        EntityManager em = eccairsEmf.createEntityManager();
        EccairsReportDao eccairsReportDao = new EccairsReportDao(em);
        em.getTransaction().begin();
        List<EccairsReport> rs = eccairsReportDao.findEccairReportByReportingEntityAndEntityFileNumber(reportingEntity, reportingEntity);
        em.getTransaction().commit();
        return rs;
    }
}
