/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.kbss.reporting.service.data.mail;

import cz.cvut.kbss.commons.io.NamedStream;
import cz.cvut.kbss.datatools.mail.MessageProcessor;
import cz.cvut.kbss.datatools.mail.model.Message;
import cz.cvut.kbss.reporting.data.audit.safa.AuditReportComparator;
import cz.cvut.kbss.reporting.data.audit.safa.ImportSafaReportsFromExcel;
import cz.cvut.kbss.reporting.exception.NotFoundException;
import cz.cvut.kbss.reporting.exception.ReportImportingException;
import cz.cvut.kbss.reporting.model.Organization;
import cz.cvut.kbss.reporting.model.Person;
import cz.cvut.kbss.reporting.model.audit.AuditReport;
import cz.cvut.kbss.reporting.persistence.dao.AuditReportDao;
import cz.cvut.kbss.reporting.persistence.dao.OrganizationDao;
import cz.cvut.kbss.reporting.service.event.InvalidateCacheEvent;
import cz.cvut.kbss.reporting.util.Constants;
import cz.cvut.kbss.reporting.util.IdentificationUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import java.io.IOException;
import java.net.URI;
import java.util.*;

/**
 *
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
public class SafaImportService implements ApplicationEventPublisherAware{
    
    // needs testing TODO: check if email provenance is provided
    
    private static final Logger LOG = LoggerFactory.getLogger(SafaImportService.class);
    
    @Autowired
    private AuditReportDao auditReportDao;
    
    @Autowired
    private OrganizationDao organizationDao;
    
    @Autowired
    @Qualifier("importer")
    private Person importer; 
    
    private ApplicationEventPublisher eventPublisher;
    
    private MessageProcessor safaAttachmentExtractor = new MessageProcessor() {
            @Override
            public boolean accepts(Message m) {
                return StringUtils.endsWithIgnoreCase(m.getSubject(), "RAMP data on Czech operators")
                        && StringUtils.endsWithIgnoreCase(m.getSenderEmailAddress(),"safa@easa.europa.eu");
            }
            @Override
            public Object processMessage(Message m) {
                return importSafaAuditReportsFromMessage(m).stream();
            }
        };
    
    
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;
    }
    
    /**
     * Use this method to import and persist safa audit reports from a NamedStream 
     * @param ns 
     */
    public List<URI> importReportsFromExcel(NamedStream ns){
        Objects.requireNonNull(ns);
        try {
            ImportSafaReportsFromExcel safaImporter = new ImportSafaReportsFromExcel();
            safaImporter.setImporter(importer);
            safaImporter.process(ns.getContent());
            List<URI> importedOrChangedReports = persistImportedReports(safaImporter);
            if(!importedOrChangedReports.isEmpty()){
                LOG.info("Safa audit report have been imported or changed {}", Arrays.toString(importedOrChangedReports.toArray()));
                eventPublisher.publishEvent(new InvalidateCacheEvent(this));
            }
            return importedOrChangedReports;
        } catch (IOException ex) {
            String message = String.format("Failed parsing named stream %s", ns.getName());
            LOG.error(message, ex);
            throw new ReportImportingException(message, ex);
        }
    }
    
    
    /**
     * Importing safa reports from email.
     * @param m 
     */
    public List<URI> importSafaAuditReportsFromMessage(Message m){
        Objects.requireNonNull(m);
        NamedStream ns = getAttachment(m);
        if(ns == null){
            throw new NotFoundException(String.format("No suitable safa xlsx attachment found for email with id %s", m.getId()));
        }
        return importReportsFromExcel(ns);
    }


    /**
     * Get the pipline which imports AuditReports from the xlxs attachment of the 
     * imput Email message. The
     * - imput: Email message
     * - output a list stream of URIs of the newlly imported or updated AuditReprots
     * @return 
     */
    public MessageProcessor getSafaAuditReportImporterProcessor() {
        return safaAttachmentExtractor;
    }

    private NamedStream getAttachment(Message m){
        return m.getAttachments().stream().filter(
                ns -> StringUtils.equalsIgnoreCase(
                        ns.getName(), 
                        "CZ OPR RAMP data% SI_STARTTIME%.xlsx")
        ).findFirst().orElse(null);
    }
    
    /**
     * Persists the reports imported from the by the safaImporter.
     * @param safaImporter
     * @return 
     */
    private List<URI> persistImportedReports(ImportSafaReportsFromExcel safaImporter){
        List<URI> changedReports = new ArrayList<>();
        persistOrganizations(safaImporter.getOrganizationsMap().values());
        safaImporter.getReports().values().forEach(r -> {
            try {
                boolean changed = persistOrUpdateRepor(r);
                if(changed){
                    changedReports.add(r.getUri());
                }
            } catch (Exception ex) {
                LOG.trace(String.format("Could not persist report %s.", r.getAudit().getName()), ex);
            }
        });
        return changedReports;
    }
    
    private void persistOrganizations(Collection<Organization> organizations){
        organizations.stream().
                filter(x -> !organizationDao.exists(x.getUri())).
                forEach(organizationDao::persist);
    }
    
    /**
     * Persists or updates the report if necessary.
     * @param r true if r is a new reprot or if r has changed since its last import
     * @return 
     */
    private boolean persistOrUpdateRepor(AuditReport r) {
        AuditReportComparator auditReportComparator = new AuditReportComparator();
        Objects.requireNonNull(r);
        AuditReport old = auditReportDao.find(r.getUri());
        if(old == null){
            persistReport(r);
            return true;
        }else{
            if(!auditReportComparator.equals(old, r)){
                auditReportDao.remove(old);
                persistReport(r);
                return true;
            }
        }
        return false;
    }
    
    private void persistReport(AuditReport r){
        r.setFileNumber(IdentificationUtils.generateFileNumber());
        r.setRevision(Constants.INITIAL_REVISION);
        auditReportDao.persist(r);
    }
}
