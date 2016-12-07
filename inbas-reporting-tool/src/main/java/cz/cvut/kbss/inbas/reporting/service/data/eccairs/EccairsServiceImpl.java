package cz.cvut.kbss.inbas.reporting.service.data.eccairs;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import cz.cvut.kbss.datatools.documentation.Vocabulary;
import cz.cvut.kbss.eccairs.webapi.EwaIWebServer;
import cz.cvut.kbss.eccairs.webapi.EwaResult;
import cz.cvut.kbss.eccairs.webapi.EwaWebServer;
import cz.cvut.kbss.eccairs.webapi.ResultReturnCode;
import cz.cvut.kbss.inbas.reporting.exception.NotFoundException;
import cz.cvut.kbss.inbas.reporting.model.OccurrenceReport;
import cz.cvut.kbss.inbas.reporting.rest.dto.model.RawJson;
import cz.cvut.kbss.inbas.reporting.service.OccurrenceReportService;
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
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.io.StringReader;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.*;
//import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author Petr Křemen
 */
@Configuration
@PropertySource("classpath:eccairs-config.properties")
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
    
    private EwaIWebServer service;

    private EwaIWebServer getService() {
        if ( service == null ) {
            SSLUtilities.trustAllHostnames();
            SSLUtilities.trustAllHttpsCertificates();

            service = new EwaWebServer().getBasicHttpBindingEwaIWebServer();
        }
        return service;
    }
    
//    @Scheduled(cron = "0 1 0 * * *")//  at one minute past midnight (00:01) every day, assuming that the default shell for the cron user is Bourne shell compliant:
//    public void scheduledImport(){
////        TODO import eccairs reports
////        Even
//    }

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

        if (ResultReturnCode.ERROR.equals(login.getReturnCode().value())) {
            LOG.info("ECCAIRS Service Login Failed: {}",login.getErrorDetails().getValue());
        } else {
            LOG.info("ECCAIRS Service Login Successful.");
        }

        return login.getUserToken().getValue();
    }

    protected String logout(String userToken) {
        final EwaResult result = service.logout(userToken);

        if (ResultReturnCode.ERROR.equals(result.getReturnCode().value())) {
            LOG.info("ECCAIRS Service Logout Failed: {}",result.getErrorDetails().getValue());
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
        LOG.info("Report Model (String length): {}", rdfJsonLd.length());

        Model model = ModelFactory.createDefaultModel();
        model.read(new StringReader(rdfJsonLd),"","JSON-LD");

        // used for report identification, 4006 = RLP
        String reportingEntity = getSingleEccairsAttributeValue(model, "447", "4006");
        LOG.info("Reporting Entity: {}", reportingEntity);

        String reportingEntityFileNumber = getSingleEccairsAttributeValue(model, "438",null);
        LOG.info("File number: {}", reportingEntityFileNumber);

        // used for version check
        String reportLastModified = getSingleEccairsAttributeValue(model, "435", null);
        LOG.info("Report last modified: {}", reportLastModified);

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
            List<URI> reports = importer.importE5FXmlFromString(reportE5F);
            if(reports != null && !reports.isEmpty())
                LOG.trace("- BINGO - we have {} ECCAIRS reports, returning the first one ! ", reports.size());
                return reports.get(0);
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
            LOG.info("- found {} occurrence keys instead of 1, skipping", occurrenceE5Fs.size());
        }

        return occurrenceE5Fs.stream().findFirst().orElse(null);
    }

    @Override
    public List<String> getAllOccurrences() {
        String userToken = login();

        final String pLibrary = env.getProperty("eccairs.repository.library");
        final String pQueryGetAllOccurrences = env.getProperty("eccairs.repository.query.getAllOccurrences");

        LOG.info("- executing query {}", pQueryGetAllOccurrences);
        String result = getService().executeQueryAsTable(userToken, pLibrary, pQueryGetAllOccurrences,0).getData().getValue();
        LOG.info("- found occurrences (string length={})", result.length());

        List<String> occurrenceKeys = EccairsQueryUtils.xpathTextContent(result,"/NewDataSet/ALL/KEY/text()");

        final List<String> occurrenceE5Fs = getFullOccurrencesForReferences(userToken,occurrenceKeys);

        logout(userToken);

        return occurrenceE5Fs;
    }

    private List<String> getOccurrencesByAttributeValueQuery(final String userToken, final String queryName, final Map<String,String> attributeValueMap) {
        final String pLibrary = env.getProperty("eccairs.repository.library");

        EwaResult result = getService().getQueryObject(userToken, pLibrary, queryName,0);
        if (ResultReturnCode.ERROR.equals(result.getReturnCode())) {
            LOG.info("- ERROR: {}", result.getErrorDetails().getValue());
            return Collections.emptyList();
        }
        final EccairsQueryUtils q = new EccairsQueryUtils();
        String queryString = q.setAttributeValuesForQueryObjectJSON(result.getData().getValue(), attributeValueMap);
        LOG.info("- executing query {}", queryString);

        result = getService().executeQueryObject(userToken, queryString, 0, 10000000, 10000000);
        if (ResultReturnCode.ERROR.equals(result.getReturnCode())) {
            LOG.info("- ERROR: {}", result.getErrorDetails().getValue());
            return Collections.emptyList();
        }
        DocumentContext document = JsonPath.parse(result.getData().getValue());
        final String operationID = document.read("$.OperationID");
        LOG.info("- executing query with OperationID = {}", operationID);

        result = getService().getQueryResult(userToken,operationID,0,"");
        if (ResultReturnCode.ERROR.equals(result.getReturnCode())) {
            LOG.info("- ERROR: {}", result.getErrorDetails().getValue());
            return Collections.emptyList();
        }
        document = JsonPath.parse(result.getData().getValue());
        final List<String> occurrenceKeys = document.read("$.Rows[*][0]");
        LOG.info("- found occurrences with keys = {}", occurrenceKeys);

        result = getService().releaseQueryResult(userToken,operationID);
        if (ResultReturnCode.ERROR.equals(result.getReturnCode())) {
            LOG.info("- ERROR: {}", result.getErrorDetails().getValue());
            return Collections.emptyList();
        }
        LOG.info("- query operation {} released with result {}", operationID, result.getReturnCode().value());

        List<String> fullOccurrences = getFullOccurrencesForReferences(userToken,occurrenceKeys);
        LOG.info("- # of occurrences found = {}", fullOccurrences.size());

        return fullOccurrences;
    }

    private List<String> getFullOccurrencesForReferences(final String userToken, final List<String> occurrenceKeys ) {
        List<String> fullOccurrences = new ArrayList<>();

        for( final String occurrenceKey : occurrenceKeys) {
            EwaResult result = getService().getOccurrenceDataByKey(userToken, occurrenceKey);
            if (ResultReturnCode.ERROR.equals(result.getReturnCode())) {
                LOG.info("- ERROR: {}", result.getErrorDetails().getValue());
                fullOccurrences.clear();
                break;
            }
            String occurrenceE5F = result.getData().getValue();
            LOG.debug("- found occurrence data = {}", occurrenceE5F);
            fullOccurrences.add(occurrenceE5F);
        }
        return fullOccurrences;
    }

    @Override
    public String getOccurrencesAfterCreationDate(final Calendar minimumCreationDate) {
        String userToken = login();

        final String pQuery = env.getProperty("eccairs.repository.query.getOccurrencesAfterCreatedDate");

        final SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        format.setTimeZone(minimumCreationDate.getTimeZone());

        final List<String> occurrenceE5Fs = getOccurrencesByAttributeValueQuery(userToken,pQuery, new HashMap<String,String>() {{
            put("434",format.format(minimumCreationDate.getTime()));
        }});

        return occurrenceE5Fs.size() != 1 ? null : occurrenceE5Fs.get(0);
    }

    @Override
    public String getOccurrencesAfterModifiedDate(final Calendar minimumModifiedDate) {
        String userToken = login();
//        The date when the report was last modified. This date is formatted using the standard format 'YYYY/MM/DD HH:MM:SS' e.g. '2001/01/26 09:11:27'. (en)
        final String pQuery = env.getProperty("eccairs.repository.query.getOccurrencesAfterModifiedDate");

        final SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        format.setTimeZone(minimumModifiedDate.getTimeZone());

        final List<String> occurrenceE5Fs = getOccurrencesByAttributeValueQuery(userToken,pQuery, new HashMap<String,String>() {{
            put("435", format.format(minimumModifiedDate.getTime()));
        }});

        return occurrenceE5Fs.size() != 1 ? null : occurrenceE5Fs.get(0);
    }
}