package cz.cvut.kbss.inbas.audit.model;

/**
 * Marker interface for entities identified by OWL Key.
 *
 * @author ledvima1
 */
public interface HasOwlKey {

    String getKey();

    /**
     * <b>IMPORTANT!!!</b> Only for correct DTO <-> Domain object transfer support, it should not be used generally.
     */
    void setKey(String key);

    void generateKey();
}
