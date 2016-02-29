package cz.cvut.kbss.inbas.audit.util;

public final class Constants {

    private Constants() {
        throw new AssertionError();
    }

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

    // Query files

    /**
     * File with a query for getting event types.
     */
    public static final String EVENT_TYPE_QUERY_FILE = "query/eventType.sparql";

    /**
     * File with a query for getting occurrence types (categories).
     */
    public static final String OCCURRENCE_CATEGORY_QUERY_FILE = "query/occurrenceType.sparql";

    /**
     * File with a query for getting report statistics.
     */
    public static final String STATISTICS_QUERY_FILE = "query/statistics.sparql";
}
