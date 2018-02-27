package cz.cvut.kbss.reporting.util;

public enum ConfigParam {

    /**
     * URL of the main application repository.
     */
    REPOSITORY_URL("repositoryUrl"),
    /**
     * OntoDriver class for the repository.
     */
    DRIVER("driver"),
    /**
     * Repository from which options are loaded.
     */
    EVENT_TYPE_REPOSITORY_URL("eventTypesRepository"),
    /**
     * URL of Liferay portal.
     */
    PORTAL_URL("portalUrl"),
    /**
     * Repository into which data for form generation are persisted.
     */
    FORM_GEN_REPOSITORY_URL("formGenRepositoryUrl"),
    /**
     * URL of the form generator service.
     */
    FORM_GEN_SERVICE_URL("formGenServiceUrl"),
    /**
     * URL of the text analysis service.
     */
    TEXT_ANALYSIS_SERVICE_URL("textAnalysisServiceUrl"),
    /**
     * URLs of vocabularies used for text analysis, delimited by a comma (,).
     */
    TEXT_ANALYSIS_VOCABULARIES("text-analysis.vocabularies"),
    /**
     * Location where the hidden file containing admin credentials should be saved (only one time event).
     */
    ADMIN_CREDENTIALS_LOCATION("adminCredentialsFileLocation"),
    /**
     * Whether the maximum number of unsuccessful login attempts should be restricted, boolean value.
     */
    RESTRICT_LOGIN_ATTEMPTS("restrictLoginAttempts"),
    /**
     * index.html location, used by Portal authentication
     */
    INDEX_FILE("indexFile"),
    /**
     * Directory into which report attachments should be stored.
     */
    ATTACHMENT_DIR("attachmentDir");

    private final String name;

    ConfigParam(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
