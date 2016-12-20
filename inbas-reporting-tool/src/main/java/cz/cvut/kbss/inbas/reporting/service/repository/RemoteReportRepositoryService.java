/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.kbss.inbas.reporting.service.repository;

import cz.cvut.kbss.eccairs.report.model.EccairsReport;
import cz.cvut.kbss.inbas.reporting.model.Vocabulary;
import cz.cvut.kbss.inbas.reporting.model.repo.ImportEvent;
import cz.cvut.kbss.inbas.reporting.model.repo.RemoteReportRepository;
import cz.cvut.kbss.inbas.reporting.persistence.dao.GenericDao;
import cz.cvut.kbss.inbas.reporting.persistence.dao.ImportEventDao;
import cz.cvut.kbss.inbas.reporting.persistence.dao.RemoteReportRepositoryDao;
import java.net.URI;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
@Service
public class RemoteReportRepositoryService extends BaseRepositoryService<RemoteReportRepository>{

    @Autowired
    private RemoteReportRepositoryDao remoteReportRepositoryDao;
    
    @Autowired
    private ImportEventDao importEventDao;
    
    private RemoteReportRepository eccairsReportRepository;
    private RemoteReportRepository uclIBEmailRepository;
    
    @PostConstruct
    public void init(){
        // eccairs repository
        URI eccairsReportRepositoryUri = URI.create("http://onto.fel.cvut.cz/ontologies/uzpln/eccairs-report-repository");
        eccairsReportRepository = remoteReportRepositoryDao.find(eccairsReportRepositoryUri);
        if(eccairsReportRepository == null){
            eccairsReportRepository = new RemoteReportRepository();
            eccairsReportRepository.setUri(eccairsReportRepositoryUri);
            eccairsReportRepository.setName("Eccairs Report Repository managed by UZPLN");
        }
        
        // ib email repository
        URI uclIBEmailRepositoryUri = URI.create("http://onto.fel.cvut.cz/ontologies/ucl/ib-email-report-repository");
        uclIBEmailRepository = remoteReportRepositoryDao.find(uclIBEmailRepositoryUri);
        if(uclIBEmailRepository == null){
            uclIBEmailRepository = new RemoteReportRepository();
            uclIBEmailRepository.setUri(eccairsReportRepositoryUri);
            uclIBEmailRepository.setName("Eccairs Report Repository managed by UZPLN");
        }
    }
    
    @Override
    protected GenericDao<RemoteReportRepository> getPrimaryDao() {
        return remoteReportRepositoryDao;
    }
    
    public void addImportEvent(ImportEvent ie, RemoteReportRepository reportRepo){
        importEventDao.persist(ie);
        reportRepo.addImportEvent(ie);
        remoteReportRepositoryDao.update(reportRepo);
        importEventDao.update(ie);
    }
    
    public RemoteReportRepository getEccairsReportRepository(){
        return eccairsReportRepository;
    } 

    public RemoteReportRepository getUclIBEmailRepository() {
        return uclIBEmailRepository;
    }
    
    public String getLatestEccairsVersionOf(URI uri){
        return importEventDao.getLatestEccairsReport(uri);
    }
    
}
