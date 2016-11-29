/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.kbss.inbas.reporting.data.audit.xls;

import cz.cvut.kbss.inbas.reporting.caa.imp.audit.old.AuditDataExtraction;
import cz.cvut.kbss.inbas.reporting.model.Organization;
import cz.cvut.kbss.inbas.reporting.model.Person;
import cz.cvut.kbss.inbas.reporting.model.audit.AuditReport;
import cz.cvut.kbss.inbas.reporting.persistence.dao.AuditReportDao;
import cz.cvut.kbss.inbas.reporting.persistence.dao.OrganizationDao;
import cz.cvut.kbss.inbas.reporting.util.Constants;
import cz.cvut.kbss.inbas.reporting.util.IdentificationUtils;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Bogdan Kostov <bogdan.kostov@fel.cvut.cz>
 */
public class AbstractExcelAuditImporter extends XLSXIterator{
    public static Date defaultDate = new Date(0);
    public static Date batchDate = new Date();
    public static final String IMPORTER_USERNAME = "e5xml-data-importer-0001";
    public static final String IMPORTER_URI = "http://onto.fel.cvut.cz/ointologies/tools/" + IMPORTER_USERNAME;
    
    protected Person importer;
    
    protected Map<String, AuditReport> reports = new HashMap<>();
    protected Map<String,Organization> organizationsMap;
    protected Map<String, List<String>> locationsMap;
    
    protected Organization defaultOrganization;
    protected String defaultLocation;
    
    public AbstractExcelAuditImporter() {
        organizationsMap = new HashMap<>();
        defaultOrganization = new Organization("Organization not selected");
        organizationsMap.put("defaultOrganization", defaultOrganization);
        defaultLocation = "http://onto.fel.cvut.cz/ontologies/location-not-specified";
    }
    
    public Map<String, AuditReport> getReports() {
        return reports;
    }

    public Map<String, Organization> getOrganizationsMap() {
        return organizationsMap;
    }

    public Person getImporter() {
        return importer;
    }

    public void setImporter(Person importer) {
        this.importer = importer;
    }
    
    @Deprecated
    public static Map<String,List<String>> loadLocationMappings(String mappingLocation){
        InputStream is = AuditDataExtraction.class.getResourceAsStream(mappingLocation);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        Map<String, List<String>> ret = new HashMap<>();
        br.lines().filter(l -> !l.startsWith("?")).forEach(l -> {
            String[] cs = l.split("\t");
            if(cs != null && cs.length == 2){
                ret.put(cs[0], Arrays.asList(cs[1].split(";")));
            }else{
                // skip, do nothing
            }
        });
        return ret;
    }
    
}
