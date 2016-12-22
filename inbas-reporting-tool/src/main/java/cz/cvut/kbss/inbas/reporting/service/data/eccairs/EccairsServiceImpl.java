package cz.cvut.kbss.inbas.reporting.service.data.eccairs;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import cz.cvut.kbss.datatools.documentation.Vocabulary;
import cz.cvut.kbss.eccairs.report.model.EccairsReport;
import cz.cvut.kbss.eccairs.webapi.EwaIWebServer;
import cz.cvut.kbss.eccairs.webapi.EwaResult;
import cz.cvut.kbss.eccairs.webapi.EwaWebServer;
import cz.cvut.kbss.eccairs.webapi.ResultReturnCode;
import cz.cvut.kbss.inbas.reporting.exception.NotFoundException;
import cz.cvut.kbss.inbas.reporting.model.OccurrenceReport;
import cz.cvut.kbss.inbas.reporting.rest.dto.model.RawJson;
import cz.cvut.kbss.inbas.reporting.service.OccurrenceReportService;
import cz.cvut.kbss.inbas.reporting.service.data.eccairs.change.EccairsReportChange;
import cz.cvut.kbss.inbas.reporting.service.data.eccairs.change.EccairsRepositoryChange;
import cz.cvut.kbss.inbas.reporting.service.data.mail.ReportImporter;
import cz.cvut.kbss.inbas.reporting.service.formgen.FormGenService;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.io.StringReader;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.*;
import org.springframework.stereotype.Service;
//import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author Petr Křemen
 */
@Service
public class EccairsServiceImpl implements EccairsService {

    private static final Logger LOG = LoggerFactory.getLogger(EccairsServiceImpl.class);

    @Autowired
    protected Environment env;
    
    @Autowired
    protected ReportImporter importer;
    
    @Autowired
    private OccurrenceReportService occurrenceReportService;

    @Autowired
    private FormGenService formGenService;
//    @Autowired
//    protected OccurrenceReportDao occurrenceReportDao;
    
    final SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    
    private EwaIWebServer service;

    private EwaIWebServer getService() {
        if ( service == null ) {
            SSLUtilities.trustAllHostnames();
            SSLUtilities.trustAllHttpsCertificates();

            service = new EwaWebServer().getBasicHttpBindingEwaIWebServer();
        }
        return service;
    }
    

    @PostConstruct
    protected void init() {
        LOG.info("ECCAIRS Service:");
        LOG.info("  - about()={}",getService().about().getData().getValue());
        LOG.info("  - getRepositories()={}",getService().getRepositories().getData().getValue());
    }

    protected String login() {
        final String pRepositoryId=env.getProperty("eccairs.repository.id");
        final String pLanguage=env.getProperty("eccairs.repository.language");
        final String pUsername=env.getProperty("eccairs.repository.username");
        final String pPassword=env.getProperty("eccairs.repository.password");

        final EwaResult login = getService().login(
                pRepositoryId,
                pUsername,
                pPassword,
                pLanguage
        );

        if (ResultReturnCode.ERROR.equals(login.getReturnCode())) {
            LOG.warn("ECCAIRS Service Login Failed: {}",login.getErrorDetails().getValue());
        } else {
            LOG.info("ECCAIRS Service Login Successful.");
        }

        return login.getUserToken().getValue();
    }

    protected String logout(String userToken) {
        final EwaResult result = service.logout(userToken);

        if (ResultReturnCode.ERROR.equals(result.getReturnCode())) {
            LOG.warn("ECCAIRS Service Logout Failed: {}",result.getErrorDetails().getValue());
        } else {
            LOG.info("ECCAIRS Service Logout Successful.");
        }

        return result.getUserToken().getValue();
    }

    private String getSingleStringValue(Model model, String query, String var) {
        final ResultSet rs = QueryExecutionFactory.
                create(query,
                        model).
                execSelect();
        if(rs.hasNext()) {
            final QuerySolution b = rs.next();
            return b.get(var).asLiteral().getLexicalForm();
        } else {
            return null;
        }
    }

    private String getSingleEccairsAttributeValue(Model model, String attributeNumber, String defaultValue) {
        String s_p_has_question_origin = "http://onto.fel.cvut.cz/ontologies/form/has-question-origin";
        String query = "SELECT * { [] <"+ Vocabulary.s_p_has_answer+">/<"+Vocabulary.s_p_has_data_value+"> ?o ; <"+s_p_has_question_origin+">/a ?attr FILTER(regex(str(?attr),\"^http://onto.fel.cvut.cz/ontologies/eccairs/aviation-.*/a-"+attributeNumber+"$\",\"\"))}";
        String s = getSingleStringValue(model,query,"o");
        return s == null ? defaultValue : s;
    }

    @Override
    public OccurrenceReport getEccairsLatestByKey(String key){
        final OccurrenceReport report = occurrenceReportService.findByKey(key);
        final RawJson s = formGenService.generateForm(report, Collections.emptyMap());
        OccurrenceReport op = getEccairsLatestByJson(s.toString());
        if(op == null)
            throw new NotFoundException(String.format("Latest eccairs report for report with key %s not found!", key));
        return op;
    }

    @Override
    public OccurrenceReport getEccairsLatestByJson(String rdfJsonLd) {
        LOG.trace("Report Model (String length): {}", rdfJsonLd.length());

        Model model = ModelFactory.createDefaultModel();
        model.read(new StringReader(rdfJsonLd),"","JSON-LD");

        // used for report identification, 4006 = RLP
        String reportingEntity = getSingleEccairsAttributeValue(model, "447", "4006");
        LOG.trace("Reporting Entity: {}", reportingEntity);

        String reportingEntityFileNumber = getSingleEccairsAttributeValue(model, "438",null);
        LOG.trace("File number: {}", reportingEntityFileNumber);

        // used for version check
        String reportLastModified = getSingleEccairsAttributeValue(model, "435", null);
        LOG.trace("Report last modified: {}", reportLastModified);

        if ( reportingEntity != null && reportingEntityFileNumber != null ) {
            LOG.info("Report matched to report: repEntity={} : repFileNumber={}", reportingEntity, reportingEntityFileNumber);

            URI reportUri = findAndLoadLatestEccairsReport(reportingEntity, reportingEntityFileNumber);
            if(reportUri != null){
                return occurrenceReportService.find(reportUri);
            }
        }

        // TODO

        return null;
    }
    
    
    @Override
    public URI findAndLoadLatestEccairsReport(String reportingEntity, String reportingEntityFileNumber){
        LOG.debug("Find and load latest ECCAIRS report: repEntity={} : repFileNumber={}", reportingEntity, reportingEntityFileNumber);
        final String reportE5F = getCurrentEccairsReportByInitialFileNumberAndReportingEntity(reportingEntity, reportingEntityFileNumber);
        if(reportE5F != null && !reportE5F.isEmpty()){
            LOG.trace("- E5F length={}", reportE5F.length());
            List<EccairsReport> reports = importer.importE5FXmlFromString(reportE5F);
            if(reports != null && !reports.isEmpty()){
                LOG.trace("- BINGO - we have {} ECCAIRS reports, returning the first one ! ", reports.size());
                return URI.create(reports.get(0).getUri());
            }
        }
        return null;
    }

    @Override
    public String getCurrentEccairsReportByInitialFileNumberAndReportingEntity(final String reportingEntity,
                                                                               final String reportNumber) {
        String userToken = login();

        final String pQueryGetOccurrenceByFileNumber = env.getProperty("eccairs.repository.query.getOccurrenceByInitialFileNumberAndReportingEntity");

        final List<String> occurrenceE5Fs = getOccurrencesByAttributeValueQuery(userToken,pQueryGetOccurrenceByFileNumber, new HashMap<String,String>() {{
            put("447", reportingEntity);
            put("438", reportNumber);
        }});

        if ( occurrenceE5Fs.size() != 1 ) {
            LOG.warn("- found {} occurrence keys instead of 1, skipping", occurrenceE5Fs.size());
        }

        return occurrenceE5Fs.stream().findFirst().orElse(null);
    }

    @Override
    public List<String> getAllOccurrences() {
        String userToken = login();

        final String pLibrary = env.getProperty("eccairs.repository.library");
        final String pQueryGetAllOccurrences = env.getProperty("eccairs.repository.query.getAllOccurrences");

        LOG.trace("- executing query {}", pQueryGetAllOccurrences);
        String result = getService().executeQueryAsTable(userToken, pLibrary, pQueryGetAllOccurrences,0).getData().getValue();
        LOG.trace("- found occurrences (string length={})", result.length());

        List<String> occurrenceKeys = EccairsQueryUtils.xpathTextContent(result,"/NewDataSet/ALL/KEY/text()");

        final List<String> occurrenceE5Fs = getFullOccurrencesForReferences(userToken,occurrenceKeys);

        logout(userToken);

        return occurrenceE5Fs;
    }

    private List<String> getOccurrencesKeysByAttributeValueQuery(final String userToken, final String queryName, final Map<String,String> attributeValueMap) {
        final String pLibrary = env.getProperty("eccairs.repository.library");

        EwaResult result = getService().getQueryObject(userToken, pLibrary, queryName,0);
        if (ResultReturnCode.ERROR.equals(result.getReturnCode())) {
            LOG.warn("- ERROR: {}", result.getErrorDetails().getValue());
            return Collections.emptyList();
        }
        final EccairsQueryUtils q = new EccairsQueryUtils();
        String queryString = q.setAttributeValuesForQueryObjectJSON(result.getData().getValue(), attributeValueMap);
        LOG.trace("- executing query {}", queryString);

        result = getService().executeQueryObject(userToken, queryString, 0, 10000000, 10000000);
        if (ResultReturnCode.ERROR.equals(result.getReturnCode())) {
            LOG.warn("- ERROR: {}", result.getErrorDetails().getValue());
            return Collections.emptyList();
        }
        DocumentContext document = JsonPath.parse(result.getData().getValue());
        final String operationID = document.read("$.OperationID");
        LOG.trace("- executing query with OperationID = {}", operationID);

        result = getService().getQueryResult(userToken,operationID,0,"");// the returned result is an error
        if (ResultReturnCode.ERROR.equals(result.getReturnCode())) {
            LOG.warn("- ERROR: {}", result.getErrorDetails().getValue());
            return Collections.emptyList();
        }
        document = JsonPath.parse(result.getData().getValue());
        final List<String> occurrenceKeys = document.read("$.Rows[*][0]");
        LOG.trace("- found occurrences with keys = {}", occurrenceKeys);
        
        result = getService().releaseQueryResult(userToken,operationID);
        if (ResultReturnCode.ERROR.equals(result.getReturnCode())) {
            LOG.warn("- ERROR: {}", result.getErrorDetails().getValue());
            return Collections.emptyList();
        }
        LOG.trace("- query operation {} released with result {}", operationID, result.getReturnCode().value());
        return occurrenceKeys;
    }
    
    private List<String> getOccurrencesByAttributeValueQuery(final String userToken, final String queryName, final Map<String,String> attributeValueMap) {
        LOG.trace("getOccurrencesByAttributeValueQuery");
        List<String> occurrenceKeys = getOccurrencesKeysByAttributeValueQuery(userToken, queryName, attributeValueMap);
        
        List<String> fullOccurrences = getFullOccurrencesForReferences(userToken,occurrenceKeys);

        return fullOccurrences;
    }

    private List<String> getFullOccurrencesForReferences(final String userToken, final List<String> occurrenceKeys ) {
        List<String> fullOccurrences = new ArrayList<>();
        for( final String occurrenceKey : occurrenceKeys) {
            String occurrenceE5F = getFullOccurrenceForReference(userToken, occurrenceKey);
            fullOccurrences.add(occurrenceE5F);
        }
        LOG.trace("- # of occurrences found = {}", fullOccurrences.size());
        return fullOccurrences;
    }
    
    private String getFullOccurrenceForReference(final String userToken, final String occurrenceKey ){
        LOG.trace("getFullOccurrenceForReference");
        EwaResult result = getService().getOccurrenceDataByKey(userToken, occurrenceKey);
        if (ResultReturnCode.ERROR.equals(result.getReturnCode())) {
            LOG.warn("- ERROR: {}", result.getErrorDetails().getValue());
        }
        String occurrenceE5F = result.getData().getValue();
        LOG.debug("- found occurrence data = {}", occurrenceE5F.length());
        return occurrenceE5F;
    }

    @Override
    public List<String> getOccurrencesAfterCreationDate(final Calendar minimumCreationDate) {
        LOG.trace("getOccurrencesAfterCreationDate(date)");
        String userToken = login();
        List<String> occurrenceKeys = getOccurrenceKeysAfterCreationDate(userToken, minimumCreationDate);
        List<String> occurrenceE5Fs = getFullOccurrencesForReferences(userToken, occurrenceKeys);
        logout(userToken);
        return occurrenceE5Fs;
    }

    protected List<String> getOccurrenceKeysAfterCreationDate(String userToken, Calendar minimumCreationDate) {
        LOG.trace("getOccurrenceKeysAfterCreationDate(userToken, date)");
        return getOccurrenceKeysByAfterDateQuery(userToken, minimumCreationDate, "eccairs.repository.query.getOccurrencesAfterCreatedDate", "434");
    }
    
    @Override
    public List<String> getOccurrencesAfterModifiedDate(final Calendar minimumModifiedDate) {
        LOG.trace("getOccurrencesAfterModifiedDate(date)");
        String userToken = login();
        List<String> occurrenceKeys = getOccurrenceKeysAfterModifiedDate(userToken, minimumModifiedDate);
        List<String> occurrenceE5Fs = getFullOccurrencesForReferences(userToken, occurrenceKeys);
        logout(userToken);
        return occurrenceE5Fs;
    }
    
    protected List<String> getOccurrenceKeysAfterModifiedDate(String userToken, Calendar minimumModifiedDate) {
        LOG.trace("getOccurrenceKeysAfterModifiedDate(userToken, date)");
        return getOccurrenceKeysByAfterDateQuery(userToken, minimumModifiedDate, "eccairs.repository.query.getOccurrencesAfterModifiedDate", "435");
    }
    
    private List<String> getOccurrenceKeysByAfterDateQuery(String userToken, final Calendar dateFrom, String queryName, String dateAttributeId){
        LOG.trace("getOccurrenceKeysByAfterDateQuery");
        Objects.nonNull(userToken);
        final String pQuery = env.getProperty(queryName);
        final List<String> occurrenceKeys = getOccurrencesKeysByAttributeValueQuery(userToken,pQuery, new HashMap<String,String>() {{
            put(dateAttributeId, formatDate(dateFrom));
        }});
        return occurrenceKeys;
    }
    
    protected String formatDate(final Calendar calendar){
        format.setTimeZone(calendar.getTimeZone());
        return format.format(calendar.getTime());
    }
    
    @Override
    public EccairsRepositoryChange getLatestChanges(final Calendar date, Set<String> eccairsOccurrenceKey){
        LOG.trace("getLatestChanges");
        EccairsRepositoryChange change = new EccairsRepositoryChange(date);
        String userToken = login();
        
        // load modified reports after date
        List<String> occurrenceKeys = getOccurrenceKeysAfterModifiedDate(userToken, date);
//        occurrenceKeys = occurrenceKeys.subList(0, 100);// DEBUG
        if(eccairsOccurrenceKey != null){
            occurrenceKeys.retainAll(eccairsOccurrenceKey);
        }
        
        for(String key : occurrenceKeys){
            String occE5f = getFullOccurrenceForReference(userToken, key);
            EccairsReportChange erc = new EccairsReportChange(key, occE5f);
            erc.setEdited(true);
            change.addChangedReport(erc);
        }
        
        // load occurrence reports created after date
        occurrenceKeys = getOccurrenceKeysAfterCreationDate(userToken, date);
//        occurrenceKeys = occurrenceKeys.subList(0, 100);// DEBUG
        // remove reports that are changed 
        if(eccairsOccurrenceKey != null){
            occurrenceKeys.retainAll(eccairsOccurrenceKey);
        }
        for(String key : occurrenceKeys){
            EccairsReportChange erc = change.getReportForEccairsKey(key);
            if(erc == null){
                String occE5f = getFullOccurrenceForReference(userToken, key);
                erc = new EccairsReportChange(key, occE5f);
                erc.setCreated(true);
                change.addChangedReport(erc);
            }else{
                erc.setCreated(true);
            }
        }
        logout(userToken);
        return change;
    }
}