package cz.cvut.kbss.inbas.audit.model;

/**
 * Marker interface for entity classes with non-generated identifiers, which can be derived from their attributes.
 *
 * @author ledvima1
 */
public interface HasDerivableUri {

    /**
     * Derives a URI if it is not set on this instance.
     */
    void generateUri();
}
