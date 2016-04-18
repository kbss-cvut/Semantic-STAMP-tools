package cz.cvut.kbss.inbas.reporting.util;

public enum ConfigParam {

    REPOSITORY_URL("repositoryUrl"),
    EVENT_TYPE_REPOSITORY_URL("eventTypesRepository"),
    PORTAL_URL("portalUrl"),

    INDEX_FILE("indexFile");    // index.html location, used by Portal authentication

    private final String name;

    ConfigParam(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
