package cz.cvut.kbss.inbas.audit.model;

import java.net.URI;
import java.util.Set;

/**
 * Marker interface for reporting statements.
 *
 * @author ledvima1
 */
public interface ReportingStatement {

    URI getUri();

    Set<Resource> getResources();
}
