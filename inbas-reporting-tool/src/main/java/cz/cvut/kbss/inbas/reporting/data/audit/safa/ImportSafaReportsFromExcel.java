/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.kbss.inbas.reporting.data.audit.safa;

import cz.cvut.kbss.inbas.reporting.data.audit.xls.AbstractExcelAuditImporter;
import cz.cvut.kbss.inbas.reporting.data.audit.xls.Vocabulary;
import cz.cvut.kbss.inbas.reporting.caa.imp.audit.old.AuditDataExtraction;
import cz.cvut.kbss.inbas.reporting.config.PersistenceConfig;
import cz.cvut.kbss.inbas.reporting.config.ServiceConfig;
import cz.cvut.kbss.inbas.reporting.model.Organization;
import cz.cvut.kbss.inbas.reporting.model.Person;
import cz.cvut.kbss.inbas.reporting.model.audit.Audit;
import cz.cvut.kbss.inbas.reporting.model.audit.AuditFinding;
import cz.cvut.kbss.inbas.reporting.model.audit.AuditReport;
import cz.cvut.kbss.inbas.reporting.persistence.dao.AuditDao;
import cz.cvut.kbss.inbas.reporting.persistence.dao.AuditReportDao;
import cz.cvut.kbss.inbas.reporting.persistence.dao.OrganizationDao;
import cz.cvut.kbss.inbas.reporting.persistence.dao.PersonDao;
import cz.cvut.kbss.inbas.reporting.rest.util.RestUtils;
import cz.cvut.kbss.inbas.reporting.service.repository.ReportMetadataService;
import cz.cvut.kbss.inbas.reporting.util.IdentificationUtils;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
//import org.json.JSONArray;
//import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 *
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
public class ImportSafaReportsFromExcel extends AbstractExcelAuditImporter{
    
    private static final Logger LOG = LoggerFactory.getLogger(AuditDataExtraction.class);
            
    public static String safaAuditReportType = "http://onto.fel.cvut.cz/ontologies/safa/audit-report";
    public static String auditPrefix = safaAuditReportType + "-";
    public static String safaAuditFindingType = "http://onto.fel.cvut.cz/ontologies/safa/finding";
    public static String findingPrefix = safaAuditFindingType + "-";
    public static String predefinedFindingTypePrefix = safaAuditFindingType + "/pdf-";
    public static String findingCategoryGeneralRemak = "http://onto.fel.cvut.cz/ontologies/safa/finding/category/general-remark";
    public static String aircraftPrefix = "http://onto.fel.cvut.cz/ontologies/safa/audit-aircraft-";
    
    protected Map<String, String> inspectionLocationMap = new HashMap<>();
    protected Map<String, String> aircraftMap;
    
    protected AuditReportComparator auditReportComparator = new AuditReportComparator();

//    protected Map<String, Organization> authorityMap;
//    protected Map<String, Organization> operatorMap;
    
    @Autowired
    private ReportMetadataService reportMetadataService;
    
    @Autowired
    private AuditReportDao auditReportDao;
    
    

    public void setReportMetadataService(ReportMetadataService reportMetadataService) {
        this.reportMetadataService = reportMetadataService;
    }

    public void setAuditReportDao(AuditReportDao auditReportDao) {
        this.auditReportDao = auditReportDao;
    }
    
    
//    protected void loadOrganizationMapFromJson(String file, String id, String name)throws Exception{
//        JSONObject json = (JSONObject)JSONValue.parse(new FileReader(file));
//        JSONArray array = (JSONArray)json.get("items");
//        for(int i = 0; i < array.size(); i++){
//            JSONObject obj = (JSONObject)array.get(i);
//            Organization org = new Organization(obj.getAsString(name));
//            organizationsMap.put(obj.getAsString(id), org);
//        }
//    }
//    protected void loadMapFromJson(String file, String id, String name, Map<String,String> map)throws Exception{
//        // TODO use the json library from EccairsQueryUtils
////        String jsonString = IOUtils.toString(new FileReader(file));
//        
//        JSONObject json = (JSONObject)JSONValue.parse(new FileReader(file));
//        JSONArray array = (JSONArray)json.get("items");
//        for(int i = 0; i < array.size(); i++){
//            JSONObject obj = (JSONObject)array.get(i);
//            map.put(obj.getAsString(id), "http://onto.fel.cvut.cz/ontologies/safa/location/" + obj.getAsString(id));
//        }
//    }
    
//    public static class
    
//    protected void loadTaxonomies() throws Exception{
//        String orgs1 = "c:\\Users\\user\\Documents\\skola\\projects\\tacr-beta-2015\\sisel\\audits\\safa\\safa-auditor-organizations.json";
//        String orgs2 = "c:\\Users\\user\\Documents\\skola\\projects\\tacr-beta-2015\\sisel\\audits\\safa\\safa-operators.json";
//        String locs = "c:\\Users\\user\\Documents\\skola\\projects\\tacr-beta-2015\\sisel\\audits\\safa\\safa-inspection-places-15-11-2016.json";
//        
////        loadMapFromJson(orgs1,"text", "full_description");
////        loadMapFromJson(orgs2,"code", "name");
////        loadOrganizationMapFromJson(orgs1,"text", "full_description");
////        loadOrganizationMapFromJson(orgs2,"code", "name");
//        // 
////        loadMapFromJson(locs,"code", "text",inspectionLocationMap);
//    }
    
    public static void main(String[] args) throws Exception{
        ApplicationContext app = new AnnotationConfigApplicationContext(PersistenceConfig.class, ServiceConfig.class);
        OrganizationDao od = app.getBean(OrganizationDao.class);
        AuditReportDao ard = app.getBean(AuditReportDao.class);
        AuditDao ad = app.getBean(AuditDao.class);
        PersonDao pd = app.getBean(PersonDao.class);
        ReportMetadataService rms = app.getBean(ReportMetadataService.class);
        
        ImportSafaReportsFromExcel safaImporter = new ImportSafaReportsFromExcel();

        safaImporter.setAuditReportDao(ard);
        safaImporter.setReportMetadataService(rms);
        
        InputStream is = new FileInputStream("c:\\Users\\user\\Documents\\skola\\projects\\tacr-beta-2015\\sisel\\audits\\safa\\CZ OPR RAMP data-SI_STARTTIME.xlsx");
//        System.out.println(IOUtils.toString(is));
        
//        safaImporter.loadTaxonomies();
        safaImporter.process(is);

        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("Printing all imported reports");

        

        // persist data
        Person importer = safaImporter.getImporter();
        if (!pd.exists(importer.getUri())) {
            pd.persist(importer);
        }

        safaImporter.persistOrganizations(od);
        safaImporter.getReports().values().forEach(r -> {
            try {
                safaImporter.persistOrUpdateRepor(r);
            } catch (Exception ex) {
                LOG.trace(String.format("Could not persist report %s.", r.getAudit().getName()), ex);
            }
        });
    }

    public void persistOrUpdateRepor(AuditReport r) {
        Objects.requireNonNull(r);
        AuditReport old = auditReportDao.find(r.getUri());
        if(old == null){
            persistReport(r, auditReportDao);
        }else{
            if(!auditReportComparator.equals(old, r)){
                auditReportDao.remove(old);
                persistReport(r, auditReportDao);
            }
        }
    }
    
    public void persistReport(AuditReport r){
        reportMetadataService.initMetadataForPersist(r);
        auditReportDao.persist(r);
    }
    
    
    
    public static enum AuditColumns{
        naa_code, // audit tab 
        naa_state_name, // audit tab
        org_type_code, // audit tab
        year, // audit tab
        report_identifier, // audit tab
        inspection_date, // audit tab
        inspection_place_code, // audit tab
        inspection_place_name, // audit tab
        type_of_operation_code, // audit tab
        operator_code, // audit tab, ICAO code 
        operator_name, // audit tab
        prioritized_clause, // audit tab
        operator_state_name, // audit tab
        aircraft_type_code, // audit tab
        aircraft_type_description, // audit tab
        aircraft_configuration_code_description, // audit tab
        registration_mark, // audit tab
        construction_no, // audit tab
        crew_state_name, // audit tab
        copilot_state_name, // audit tab
        
        
        number_of_findings, // audit tab
        number_of_cat_1_findings, // audit tab
        number_of_cat_2_findings, // audit tab
        number_of_cat_3_findings, // audit tab
        number_of_cat_g_findings, // audit tab
        number_of_open_findings, // audit tab
        number_of_closed_findings, // audit tab
        number_of_discarded_findings, // audit tab
        number_of_nr_findings, // audit tab
        
        
        checked_checklist_items // audit tab
    }
    
    public static enum AuditFindingColumns{
        report_identifier, // findings tab
        item_id, // findings tab
        item_code, // findings tab
        item_code_description, // findings tab
        standard_code, // findings tab
        category_code, // findings tab
        predefined_finding_id, // findings tab
        remark, // findings tab
        finding_description, // findings tab
        finding_status, // findings tab
        status_change_date, // findings tab
        status_change_justification // findings tab
    }

    public ImportSafaReportsFromExcel() {
        super("xls-safa-audit-data-importer-0001");
        addTable("audit", 0, AuditColumns.report_identifier, this::processAuditRows);
        addTable("findings", 1, AuditFindingColumns.report_identifier, this::processAuditFindingRows);
    }

    private void initOrganizations(){
        // TODO safa vocabulary including
        // - predefined finding types 
        // - predefined finding types
    }
    
    private void initMappingsToASOAndEccairs(){
        // init maps that contain the mapping between safa codes to eccairs and ASO uris
    }
    
    protected void processAuditRows(Row r){
        if(reports.size() > 0 )return;
        String auditName = getStringValue(r, AuditColumns.report_identifier);
        String auditReportUri = getAuditReportUri(r, AuditColumns.report_identifier);   
        URI reportUri = URI.create(auditReportUri);
        AuditReport ar = new AuditReport();
        IdentificationUtils.generateIdentificationFields(ar);
        
        ar.getTypes().add(safaAuditReportType);
        ar.setAuthor(importer);
        ar.setDateCreated(batchDate);
        
        Audit a = new Audit();
        a.setName(auditName);
        ar.setAudit(a);
        reports.put(auditReportUri, ar);
        
        try{
            // date parsing
            Date from = getDate(r, AuditColumns.inspection_date);
            if(from == null)
                from = defaultDate;

            a.setStartDate(from);
            a.setEndDate(from);

            // location - the location is always an airport, the airport is identified by ICAO code, 
            String locationId = getStringValue(r, AuditColumns.inspection_place_code);

            String location = getLocation(locationId);
            if(location == null)
                location = defaultLocation;
            a.setLocation(URI.create(location));

            // auditor
            String o = getStringValue(r, AuditColumns.naa_state_name);
            String oc = getStringValue(r, AuditColumns.naa_code);
            Organization org = new Organization(o + "(" + oc + ")");
            organizationsMap.put(oc,org);
            a.setAuditor(org);

            // audetee
            o = getStringValue(r, AuditColumns.operator_name);
            oc = getStringValue(r, AuditColumns.operator_code);
            org = new Organization(o);
            organizationsMap.put(oc,org);
            a.setAuditee(org);

            // audit types
            String ats = getStringValue(r, AuditColumns.checked_checklist_items);
            String auditTypePrefix = "http://onto.fel.cvut.cz/ontologies/aviation/safa/checklist/";
            if(!ats.isEmpty()){
                Stream.of(ats.split(";")).forEach(at -> {
                    at = at.trim();
                    if(!at.isEmpty()){
                        a.getTypes().add(auditTypePrefix + RestUtils.encodeUrl(at));
                    }
                });
            }

            // narrative
            String nar = getStringValue(r, AuditDataExtraction.AuditColumns.narrative).trim();
            if(!nar.isEmpty())
                ar.setSummary(nar);
        }catch(Exception ex){
            LOG.info(String.format("could not process audit report at, row #%d.", r.getRowNum()), ex);
            reports.remove(auditReportUri);
        }
    }
    
    protected String getLocation(String locationId){
//        return inspectionLocationMap.get(loc);inspectionLocationMap.get(locationId-);
        return "http://onto.fel.cvut.cz/ontologies/safa/location/" + locationId;
    }
    
    protected Date getDate(Row row, Enum column){
        Cell c = getCell(row, column);
        try{
            Date d = c.getDateCellValue();
            return d;
        } catch (Exception ex){
            LOG.info(String.format("could not parse date in column %s on row #%d.", column.name(), row.getRowNum()), ex);
            throw ex;
        }
    }
    
    protected String getAuditReportUri(Row r, Enum e){
        try {
            String fileName = getStringValue(r, e);
            return String.format("%s%s", auditPrefix, URLEncoder.encode(fileName, "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            java.util.logging.Logger.getLogger(AuditDataExtraction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    protected void processAuditFindingRows(Row r){
        String reportUri = getAuditReportUri(r, AuditFindingColumns.report_identifier);
        AuditReport report = reports.get(reportUri);
        if(report == null) return;
        try{
            Audit audit = report.getAudit();
            AuditFinding finding = new AuditFinding();
            if(audit.getFindings() == null)
                audit.setFindings(new HashSet<>());

            audit.getFindings().add(finding);

            String description = getStringValue(r, AuditFindingColumns.finding_description);
            if(description.isEmpty())
                description = "";
            finding.setDescription(description);
            String category = getStringValue(r, AuditFindingColumns.category_code);

            // Issue with category code G (general remark)
            if(StringUtils.isNumeric(category)){
                int l = Integer.parseInt(category);
                if(l >0 && l <4){
                    finding.setLevel(l);
                }
            }

            // finding type
            String pdfId = getStringValue(r, AuditFindingColumns.predefined_finding_id);
            if(!pdfId.isEmpty()){
                finding.getTypes().add(predefinedFindingTypePrefix + pdfId);
            }if(StringUtils.equalsIgnoreCase("G",category)){
                finding.getTypes().add(findingCategoryGeneralRemak);
            }

            // finding status 
            String findingStatus = getStringValue(r, AuditFindingColumns.finding_status);
            URI fsu = URI.create(Vocabulary.base + findingStatus.toLowerCase());
            finding.setStatus(fsu);

            // ??? status chage date
        }catch(Exception ex){
            LOG.info(String.format("could not process finding, row #%d.", r.getRowNum()), ex);
            reports.remove(reportUri);
        }
    }
    
}
