package cz.cvut.kbss.inbas.reporting.model;

import java.net.URI;

/**
 * Specifies type of factor relation between two events, e.g. causality.
 */
public enum FactorType {

    CAUSES(Vocabulary.p_causes), CONTRIBUTES_TO(Vocabulary.p_contributesTo), MITIGATES(
            Vocabulary.p_mitigates), PREVENTS(Vocabulary.p_prevents);

    private final URI uri;

    FactorType(String uri) {
        this.uri = URI.create(uri);
    }

    public URI getUri() {
        return uri;
    }

    @Override
    public String toString() {
        return uri.toString();
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
