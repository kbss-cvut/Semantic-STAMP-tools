package cz.cvut.kbss.inbas.audit.model;

import cz.cvut.kbss.inbas.audit.util.Vocabulary;

import java.net.URI;

/**
 * Reporting phase of an occurrence.
 * <p>
 * The order of phases is as follows: {@link #INITIAL} < {@link #PRELIMINARY} < {@link #INVESTIGATION}.
 */
public enum ReportingPhase {

    INITIAL, PRELIMINARY, INVESTIGATION;

    /**
     * Gets reporting phase for the specified ontological type URI.
     * <p>
     * E.g. {@link Vocabulary#InvestigationReport} URI is mapped to the {@link #INVESTIGATION} phase.
     *
     * @param typeUri Ontological type
     * @return Reporting phase corresponding to the type
     */
    public static ReportingPhase fromType(URI typeUri) {
        if (Vocabulary.InitialReport.equals(typeUri.toASCIIString())) {
            return INITIAL;
        } else if (Vocabulary.PreliminaryReport.equals(typeUri.toASCIIString())) {
            return PRELIMINARY;
        } else if (Vocabulary.InvestigationReport.equals(typeUri.toASCIIString())) {
            return INVESTIGATION;
        }
        throw new IllegalArgumentException("Type " + typeUri + " has no corresponding reporting phase.");
    }
}
