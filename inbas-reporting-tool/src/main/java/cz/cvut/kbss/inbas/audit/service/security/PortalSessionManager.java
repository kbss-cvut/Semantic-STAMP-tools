package cz.cvut.kbss.inbas.audit.service.security;

/**
 * When running on portal, keeps the session alive as long as the user's session is active in the application as well.
 */
public interface PortalSessionManager {

    void keepPortalSessionAlive();
}
