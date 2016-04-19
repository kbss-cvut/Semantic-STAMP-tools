package cz.cvut.kbss.inbas.reporting.util;

public final class Constants {

    private Constants() {
        throw new AssertionError();
    }

    /**
     * Language used by the persistence unit.
     */
    public static final String PU_LANGUAGE = "en";

    /**
     * Base URI for Persons in the Reporting tool.
     */
    public static final String PERSON_BASE_URI = "http://www.inbas.cz/ontologies/reporting-tool/people#";

    /**
     * Base URI for Organizations in the Reporting tool.
     */
    public static final String ORGANIZATION_BASE_URI = "http://www.inbas.cz/ontologies/reporting-tool/organizations#";

    /**
     * Initial revision number for reports.
     */
    public static final Integer INITIAL_REVISION = 1;

    /**
     * UTF-8 encoding identifier.
     */
    public static final String UTF_8_ENCODING = "UTF-8";

    /**
     * JSON-LD MIME type.
     */
    public static final String APPLICATION_JSON_LD_TYPE = "application/ld+json";

    /**
     * Prefix for basic authentication for the Authorization HTTP header.
     */
    public static final String BASIC_AUTHORIZATION_PREFIX = "Basic ";

    /**
     * Company ID cookie name, used for portal authentication.
     */
    public static final String COMPANY_ID_COOKIE = "COMPANY_ID";

    /**
     * Default location of the index.html file, relative to the application classpath.
     */
    public static final String INDEX_FILE_LOCATION = "../../index.html";

    // Query files

    /**
     * Folder containing query files for the application
     */
    public static final String QUERY_FILES_DIRECTORY = "query";

    /**
     * File with a query for getting event types.
     */
    public static final String EVENT_TYPE_QUERY_FILE = "query/eventType.sparql";

    /**
     * File with a query for getting occurrence types (categories).
     */
    public static final String OCCURRENCE_CATEGORY_QUERY_FILE = "query/occurrenceType.sparql";

    /**
     * File with a query for getting occurrence classes (severity levels).
     */
    public static final String OCCURRENCE_CLASS_QUERY_FILE = "query/occurrenceClass.sparql";

    /**
     * File with a query for getting report statistics.
     */
    public static final String STATISTICS_QUERY_FILE = "query/statistics.sparql";

    /**
     * Minimum ARMS index value, as per <a href="http://essi.easa.europa.eu/documents/ARMS.pdf">http://essi.easa.europa.eu/documents/ARMS.pdf</a>,
     * slide 27.
     */
    public static final short ARMS_INDEX_MIN = 1;

    /**
     * Maximum ARMS index value, as per <a href="http://essi.easa.europa.eu/documents/ARMS.pdf">http://essi.easa.europa.eu/documents/ARMS.pdf</a>,
     * slide 27.
     */
    public static final short ARMS_INDEX_MAX = 2500;
}
