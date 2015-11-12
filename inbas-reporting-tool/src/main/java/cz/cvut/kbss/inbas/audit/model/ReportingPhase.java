package cz.cvut.kbss.inbas.audit.model;

/**
 * Reporting phase of an occurrence.
 * <p>
 * The order of phases is as follows: {@link #INITIAL} < {@link #PRELIMINARY} < {@link #INVESTIGATION}.
 */
public enum ReportingPhase {

    INITIAL, PRELIMINARY, INVESTIGATION
}
