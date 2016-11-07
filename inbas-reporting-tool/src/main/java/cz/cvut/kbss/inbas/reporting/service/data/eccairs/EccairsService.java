package cz.cvut.kbss.inbas.reporting.service.data.eccairs;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import cz.cvut.kbss.datatools.documentation.Vocabulary;
import cz.cvut.kbss.eccairs.webapi.EwaIWebServer;
import cz.cvut.kbss.eccairs.webapi.EwaResult;
import cz.cvut.kbss.eccairs.webapi.EwaWebServer;
import cz.cvut.kbss.eccairs.webapi.ResultReturnCode;
import cz.cvut.kbss.inbas.reporting.model.OccurrenceReport;
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
import javax.xml.xpath.XPath;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Petr Křemen
 */
@Configuration
@PropertySource("classpath:eccairs-config.properties")
public class EccairsService {

    private static final Logger LOG = LoggerFactory.getLogger(EccairsService.class);

    @Autowired
    protected Environment env;

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

    public OccurrenceReport getEccairsLatest(String rdfJsonLd) {
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

            final String reportE5F = getCurrentEccairsReportByInitialFileNumberAndReportingEntity(reportingEntity, reportingEntityFileNumber);
        }

        // TODO

        return null;
    }

    /**
     * Returns an ECCAIRS occurrence that is backed an initial report with the given identification.
     *
     * @return an E5F serialization of the report
     */
    public String getCurrentEccairsReportByInitialFileNumberAndReportingEntity(final String reportingEntity, final String reportNumber) {
        String userToken = login();

        final String pLibrary = env.getProperty("eccairs.repository.library");
        final String pQueryGetOccurrenceByFileNumber = env.getProperty("eccairs.repository.query.getOccurrenceByInitialFileNumberAndReportingEntity");

        String query1 = getService().getQueryObject(userToken, pLibrary, pQueryGetOccurrenceByFileNumber,0).getData().getValue();
        final EccairsQueryUtils q = new EccairsQueryUtils();
        query1 = q.setAttributeValuesForQueryObjectJSON(query1, new HashMap<String,String>() {{
            put("447", reportingEntity);
            put("438", reportNumber);
        }});

        EwaResult result = getService().executeQueryObject(userToken, query1, 0, 1, 1);
        DocumentContext document = JsonPath.parse(result.getData().getValue());
        final String operationID = document.read("$.OperationID");
        LOG.info("- executing query with OperationID = {}", operationID);

        result = getService().getQueryResult(userToken,operationID,0,"");
        document = JsonPath.parse(result.getData().getValue());
        final List<String> occurrenceKeys = document.read("$.Rows[*][0]");
        if ( occurrenceKeys.size() != 1 ) {
            LOG.info("- found {} occurrence keys{}, skipping", occurrenceKeys.size(),occurrenceKeys.size() > 1 ? " "+ occurrenceKeys: "");
        }
        String occurrenceKey = occurrenceKeys.get(0);
        LOG.info("- found occurrence with key = {}", occurrenceKey);

        result = getService().releaseQueryResult(userToken,operationID);
        LOG.info("- query operation {} released with result {}", operationID, result.getReturnCode().value());

        result = getService().getOccurrenceDataByKey(userToken, occurrenceKey);
        String occurrenceE5F = result.getData().getValue();
        LOG.info("- found occurrence data = {}", occurrenceE5F);

        logout(userToken);

        return occurrenceE5F;
    }

    /**
     * Returns all occurrences (AAII occurrence reports integrating possibly multiple records of occurrences) from ECCAIRS
     *
     * @return a list of E5F serializations in strings
     */
    public List<String> getAllOccurrences() {
        final List<String> occurrenceE5Fs = new ArrayList<>();

        String userToken = login();

        final String pLibrary = env.getProperty("eccairs.repository.library");
        final String pQueryGetAllOccurrences = env.getProperty("eccairs.repository.query.getAllOccurrences");

        LOG.info("- executing query {}", pQueryGetAllOccurrences);
        String result = getService().executeQueryAsTable(userToken, pLibrary, pQueryGetAllOccurrences,0).getData().getValue();
        LOG.info("- found occurrences (string length={})", result.length());

        List<String> keys = EccairsQueryUtils.xpathTextContent(result,"/NewDataSet/ALL/KEY/text()");

        for( final String occurrenceKey : keys) {
            EwaResult eResult = getService().getOccurrenceDataByKey(userToken, occurrenceKey);
            String occurrenceE5F = eResult.getData().getValue();
            LOG.debug("- found occurrence data = {}", occurrenceE5F);
            occurrenceE5Fs.add(occurrenceE5F);
        }

        logout(userToken);

        return occurrenceE5Fs;
    }

    // TODO getReportsAfterModifiedDate(Date from)
    //
    // TODO getReportsAfterCreatedDate(Date from)

}