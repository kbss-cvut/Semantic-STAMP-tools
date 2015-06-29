package cz.cvut.kbss.inbas.audit.model;

/**
 * Marker interface for entities identified by OWL Key.
 *
 * @author ledvima1
 */
public interface HasOwlKey {

    String getKey();

    void generateKey();
}
