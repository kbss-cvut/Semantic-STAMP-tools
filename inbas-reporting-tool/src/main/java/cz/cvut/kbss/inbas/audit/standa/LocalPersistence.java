/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.kbss.inbas.audit.standa;

import cz.cvut.kbss.inbas.audit.config.PersistenceConfig;
import cz.cvut.kbss.inbas.audit.config.ServiceConfig;
import cz.cvut.kbss.inbas.audit.model.Occurrence;
import cz.cvut.kbss.inbas.audit.model.Person;
import cz.cvut.kbss.inbas.audit.model.ReportingPhase;
import cz.cvut.kbss.inbas.audit.model.reports.EventType;
import cz.cvut.kbss.inbas.audit.model.reports.EventTypeAssessment;
import cz.cvut.kbss.inbas.audit.model.reports.InitialReport;
import cz.cvut.kbss.inbas.audit.model.reports.OccurrenceSeverity;
import cz.cvut.kbss.inbas.audit.model.reports.PreliminaryReport;
import cz.cvut.kbss.inbas.audit.persistence.dao.EventTypeDao;
import cz.cvut.kbss.inbas.audit.persistence.dao.PreliminaryReportDao;
import cz.cvut.kbss.inbas.audit.security.model.AuthenticationToken;
import cz.cvut.kbss.inbas.audit.security.model.UserDetails;
import cz.cvut.kbss.inbas.audit.service.repository.RepositoryPersonService;
import cz.cvut.kbss.inbas.audit.service.repository.RepositoryPreliminaryReportService;
import cz.cvut.kbss.ucl.E5XMLLoader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 *
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
public class LocalPersistence {
    public static void main(String[] args) throws FileNotFoundException, IOException, Exception {
//        importE5F();
        importE5X();
    }
    public static void importE5X() throws FileNotFoundException, IOException, Exception {
        
        // create spring application context
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(PersistenceConfig.class, ServiceConfig.class);
        // security init the application context
//        ctx.register(ServiceConfig.class);
//        new BCryptPasswordEncoder()
        final RepositoryPreliminaryReportService reports = ctx.getBean(RepositoryPreliminaryReportService.class); 
        final RepositoryPersonService personService = ctx.getBean(RepositoryPersonService.class); 
        final EventTypeDao eventTypeDao = ctx.getBean(EventTypeDao.class);
//        EventTypeAssessment
        
        E5XMLLoader loader = new E5XMLLoader() {
            @Override
            public void occurrenceReportImported() {
                
                Occurrence o = new Occurrence();
                o.setName(ol.getTitle());
                InitialReport ir = new InitialReport("The initial report is not available");
                PreliminaryReport r = new PreliminaryReport();
                r.setOccurrence(o);
                r.getInitialReports().add(ir);
                Date d = ol.getOccurrenceStartDate();
                if(d == null){
                    d = new Date();
                }
                r.setOccurrenceStart(d);
                r.setOccurrenceEnd(d);
                String desc = ol.getDescription();
                if(desc == null || desc.trim().isEmpty()){
                    desc = "There is no narative.";
                }
                r.setSummary(desc);
                
                
                String sas = ol.getSeverityAssessment();
                if(sas != null){
                    try{
                        r.setSeverityAssessment(OccurrenceSeverity.valueOf(sas));
                    }catch(IllegalArgumentException e){
                        e.printStackTrace();
                        r.setSeverityAssessment(OccurrenceSeverity.OCCURRENCE_WITHOUT_SAFETY_EFFECT); 
                    }
                }else{
                    r.setSeverityAssessment(OccurrenceSeverity.OCCURRENCE_WITHOUT_SAFETY_EFFECT); 
                }
                
                r.setTypeAssessments(new HashSet<EventTypeAssessment>());
                
                for(Map.Entry<String,String> s : ol.getEventTypes().entrySet()){
                    EventType et = handleEvent(s.getKey(), s.getValue(), eventTypeDao);
                    EventTypeAssessment eta = new EventTypeAssessment();
                    eta.setEventType(et);
                    r.getTypeAssessments().add(eta);  
                }
                
                
                String oc = ol.getOccurrenceCategory();
                String ocl = "";
                if(oc == null){
                    oc = "http://onto.fel.cvut.cz/ontologies/eccairs-3.4.0.2/vl-a-430/v-99";
                    ocl = "UNK: Unknown or undetermined";
                }else{
                    ocl = ol.getOccurrenceCategoryLabel();
                }
                
                EventType ocet = handleEvent(oc, ocl, eventTypeDao);
                r.setOccurrenceCategory(ocet);
                
//                // ! must add name
//                String etu = ol.getEventType();
//                if(etu == null){
//                    etu = "http://onto.fel.cvut.cz/ontologies/eccairs-3.4.0.2/v-24-1-31-4-3-10-14-390-99000000";
//                }
//                String etl = ol.getEventTypeLabel();
//                if(etl == null || etl.trim().isEmpty())
//                    etl = "no label";
//                
                
                
                d = ol.getReportCreatetionDate();
                if(d == null)
                    d = new Date();
                r.setCreated(d);
                
//                Person p = new Person();
                String username = ol.getAuthorUserName();
                String n = ol.getAuthorFirstSecondName();
                String fn;
                String sn;
                
                if(username == null){
                    username = "robot1";
                    fn = "robot";
                    sn = "robot";
                }else{
                    String[] ns =  n.split(" ");
                    if(ns.length == 0)
                        fn = sn = n;
                    else if(ns.length == 1)
                        fn = sn = ns[0];
                    else{
                        fn = ns[1];
                        sn = ns[ns.length - 1];
                    }
                }
                Person p = personService.findByUsername(username);
                if(p == null){
                    p = new Person();
                    p.setUsername(username);
                    p.setFirstName(fn);
                    p.setLastName(sn);
                    p.setPassword("some_password_for_inbas_2_2016_imported_users");
                    personService.persist(p);
                }else{
                    p.setUsername(username);
                    p.setFirstName(fn);
                    p.setLastName(sn);
//                    p.setPassword("some_password_for_inbas_2_2016_imported_users");
                }
//                p.setFirstName("Bogdan");
//                p.setLastName("Kostov");
        //        p.setUsername("kostobog");
//                p.setPassword("password");
                
                r.setAuthor(p); // value is set by the reportService when persisted with reportService.persist!!!
                // reportService.persist(r); 
                // using my persistence
                persistReport(ctx, r);
                System.out.println(new ArrayList<>(reports.findAll()).size());
            }
        };
        
//        loader.processDataFolder("d:\\downloads\\inbas\\data-brno-376\\", "e5f");
        loader.processDataFolder("d:\\downloads\\inbas\\data-brno-376\\", "e5x");
        ctx.close();
        System.out.println("asdf");
        
        
    }
    public static void importE5F() throws FileNotFoundException, IOException, Exception {
        // create spring application context
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(PersistenceConfig.class, ServiceConfig.class);
        // security init the application context
        
        
//        ctx.register(ServiceConfig.class);
//        new BCryptPasswordEncoder()
        RepositoryPreliminaryReportService reports = ctx.getBean(RepositoryPreliminaryReportService.class); 
        RepositoryPersonService personService = ctx.getBean(RepositoryPersonService.class); 
        
//        EventTypeAssessment
        
        E5XMLLoader.initEccairsCache();
        
        E5XMLLoader loader = new E5XMLLoader() {
            @Override
            public void occurrenceReportImported() {
                
                Occurrence o = new Occurrence();
                o.setName(ol.getTitle());
                InitialReport ir = new InitialReport("The initial report is not available");
                PreliminaryReport r = new PreliminaryReport();
                r.setOccurrence(o);
                r.getInitialReports().add(ir);
                Date d = ol.getOccurrenceStartDate();
                if(d == null){
                    d = new Date();
                }
                r.setOccurrenceStart(d);
                r.setOccurrenceEnd(d);
                String desc = ol.getDescription();
                if(desc == null || desc.trim().isEmpty()){
                    desc = "There is no narative.";
                }
                r.setSummary(desc);
                
                r.setSeverityAssessment(OccurrenceSeverity.OCCURRENCE_WITHOUT_SAFETY_EFFECT); 
                r.setTypeAssessments(new HashSet<EventTypeAssessment>());
                
                // ! must add name
                String etu = ol.getEventType();
                if(etu == null){
                    etu = "http://onto.fel.cvut.cz/ontologies/eccairs-3.4.0.2/v-24-1-31-4-3-10-14-390-99000000";
                }
                String etl = ol.getEventTypeLabel();
                if(etl == null || etl.trim().isEmpty())
                    etl = "no label";
                EventTypeAssessment eta = new EventTypeAssessment();
                EventType et = new EventType(URI.create(etu), etl);
                eta.setEventType(et);
                r.getTypeAssessments().add(eta);
                
                
                d = ol.getReportCreatetionDate();
                if(d == null)
                    d = new Date();
                r.setCreated(d);
                
//                Person p = new Person();
                String username = ol.getAuthorUserName();
                String n = ol.getAuthorFirstSecondName();
                String fn;
                String sn;
                
                if(username == null){
                    username = "robot1";
                    fn = "robot";
                    sn = "robot";
                }else{
                    String[] ns =  n.split(" ");
                    if(ns.length == 0)
                        fn = sn = n;
                    else if(ns.length == 1)
                        fn = sn = ns[0];
                    else{
                        fn = ns[1];
                        sn = ns[ns.length - 1];
                    }
                }
                Person p = personService.findByUsername(username);
                if(p == null){
                    p = new Person();
                    p.setUsername(username);
                    p.setFirstName(fn);
                    p.setLastName(sn);
//                    p.setPassword("some_password_for_inbas_2_2016_imported_users");
                    personService.persist(p);
                }else{
                    p.setUsername(username);
                    p.setFirstName(fn);
                    p.setLastName(sn);
//                    p.setPassword("some_password_for_inbas_2_2016_imported_users");
                }
//                p.setFirstName("Bogdan");
//                p.setLastName("Kostov");
        //        p.setUsername("kostobog");
//                p.setPassword("password");
                
                r.setAuthor(p); // value is set by the reportService when persisted with reportService.persist!!!
                // reportService.persist(r); 
                // using my persistence
                persistReport(ctx, r);
                System.out.println(new ArrayList<>(reports.findAll()).size());
            }
        };
        
//        loader.processDataFolder("d:\\downloads\\inbas\\data-brno-376\\");
        loader.processDataFolder("d:\\downloads\\inbas\\cvut e5f 10.2.2016\\", "e5f");
        ctx.close();
        System.out.println("asdf");
    }
    
    
    public static EventType handleEvent(String eventUri, String eventLabel, EventTypeDao eventTypeDao){
        URI eturi = URI.create(eventUri);
        EventType et = eventTypeDao.find(eturi);
        if(et == null){
            if(eventLabel == null || eventLabel.isEmpty()){
                eventLabel = "No label";
            }
            et = new EventType(eturi, eventLabel);
            eventTypeDao.persist(et);
        }
        return et;
    }
    
    public static void persistReport(ApplicationContext ctx, PreliminaryReport report){
//        PreliminaryReportValidator reportValidator = ctx.getBean(PreliminaryReportValidator.class);
        PreliminaryReportDao preliminaryReportDao = ctx.getBean(PreliminaryReportDao.class);
        report.setCreated(new Date());
        if (report.getFileNumber() == null) {
            report.setFileNumber(System.currentTimeMillis());
        }
//        reportValidator.validate(report);
        report.getOccurrence().transitionToPhase(ReportingPhase.PRELIMINARY);
        preliminaryReportDao.persist(report);
    }
    
    
    public static void setCurrentUser(){
        // must be initialized
        UserDetailsService userdet = null; 
        RepositoryPersonService ps = null;
        
        
        
        Person p = new Person();
        p.setFirstName("Bogdan");
        p.setLastName("Kostov");
        p.setUsername("kostobog");
        ps.persist(p);
        UserDetails ud = (UserDetails)userdet.loadUserByUsername("");
        final SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new AuthenticationToken(ud.getAuthorities(), ud));
        
    }
}