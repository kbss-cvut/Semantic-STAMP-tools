package cz.cvut.kbss.inbas.reporting.util;

public enum ConfigParam {

    REPOSITORY_URL("repositoryUrl"),
    DRIVER("driver"),
    EVENT_TYPE_REPOSITORY_URL("eventTypesRepository"),
    PORTAL_URL("portalUrl"),
    FORM_GEN_REPOSITORY_URL("formGenRepositoryUrl"),
    FORM_GEN_SERVICE_URL("formGenServiceUrl"),

    INDEX_FILE("indexFile"),    // index.html location, used by Portal authentication

    // Configuration of statistics on Pentaho
    STATISTICS_DASHBOARD("statistics.dashboard"),
    STATISTICS_GENERAL("statistics.general"),
    STATISTICS_EVENT_TYPE("statistics.eventType"),
    STATISTICS_AUDIT("statistics.audit"),
    STATISTICS_SAFETY_ISSUE("statistics.safetyIssue"),
    STATISTICS_SAG("statistics.sag");

    private final String name;

    ConfigParam(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
