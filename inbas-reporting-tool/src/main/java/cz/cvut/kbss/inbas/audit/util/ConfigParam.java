package cz.cvut.kbss.inbas.audit.util;

public enum ConfigParam {

    REPOSITORY_URL("repositoryUrl"),
    EVENT_TYPE_REPOSITORY_URL("eventTypesRepository"),
    PORTAL_URL("portalUrl");

    private final String name;

    ConfigParam(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
