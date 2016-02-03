package cz.cvut.kbss.inbas.audit.model;

import cz.cvut.kbss.inbas.audit.util.Vocabulary;

import java.net.URI;
import java.util.Objects;

/**
 * Reporting phase of an occurrence.
 * <p>
 * The order of phases is as follows: {@link #INITIAL} < {@link #PRELIMINARY} < {@link #INVESTIGATION}.
 */
public enum ReportingPhase {

    INITIAL(Vocabulary.InitialReport), PRELIMINARY(Vocabulary.PreliminaryReport), INVESTIGATION(
            Vocabulary.InvestigationReport);

    private final String uri;

    ReportingPhase(String uri) {
        this.uri = uri;
    }

    /**
     * Checks whether the specified URI corresponds to any of the values in this enum.
     *
     * @param typeUri Type to examine
     * @return boolean
     */
    public static boolean isSupported(URI typeUri) {
        if (typeUri == null) {
            return false;
        }
        final String strType = typeUri.toASCIIString();
        for (ReportingPhase value : values()) {
            if (strType.equals(value.uri)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets reporting phase for the specified ontological type URI.
     * <p>
     * E.g. {@link Vocabulary#InvestigationReport} URI is mapped to the {@link #INVESTIGATION} phase.
     *
     * @param typeUri Ontological type
     * @return Reporting phase corresponding to the type
     */
    public static ReportingPhase fromType(URI typeUri) {
        Objects.requireNonNull(typeUri);
        final String strType = typeUri.toASCIIString();
        for (ReportingPhase phase : values()) {
            if (phase.uri.equals(strType)) {
                return phase;
            }
        }
        throw new IllegalArgumentException("Type " + typeUri + " has no corresponding reporting phase.");
    }
}
