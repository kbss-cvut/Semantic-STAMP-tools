package cz.cvut.kbss.inbas.audit.model;

import java.util.Set;

/**
 * Marker interface for reporting statements.
 *
 * @author ledvima1
 */
public interface ReportingStatement {

    Set<Resource> getResources();
}
