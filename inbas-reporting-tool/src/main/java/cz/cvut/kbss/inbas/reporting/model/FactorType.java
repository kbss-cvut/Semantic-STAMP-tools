package cz.cvut.kbss.inbas.reporting.model;

import java.net.URI;

/**
 * Specifies type of factor relation between two events, e.g. causality.
 */
public enum FactorType {

    CAUSES(Vocabulary.s_p_causes), CONTRIBUTES_TO(Vocabulary.s_p_contributes_to), MITIGATES(
            Vocabulary.s_p_mitigates), PREVENTS(Vocabulary.s_p_prevents);

    private final URI uri;

    FactorType(String uri) {
        this.uri = URI.create(uri);
    }

    public URI getUri() {
        return uri;
    }

    public static FactorType fromUri(URI uri) {
        for (FactorType ft : values()) {
            if (ft.uri.equals(uri)) {
                return ft;
            }
        }
        throw new IllegalArgumentException("Unknown FactorType uri " + uri);
    }
}
