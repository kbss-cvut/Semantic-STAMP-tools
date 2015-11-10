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
}
