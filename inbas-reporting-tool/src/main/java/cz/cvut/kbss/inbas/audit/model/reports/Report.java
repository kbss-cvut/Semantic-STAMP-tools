package cz.cvut.kbss.inbas.audit.model.reports;

import cz.cvut.kbss.inbas.audit.model.ReportingPhase;

import java.net.URI;

/**
 * Currently, this is just a marker interface for reports in the application.
 * <p>
 * Ultimately, this will become a common superclass for report classes, where shared properties will reside.
 */
public interface Report {

    URI getUri();

    /**
     * Gets reporting phase to which this report is relevant.
     *
     * @return Relevant reporting phase
     */
    ReportingPhase getPhase();
}
