package cz.cvut.kbss.inbas.reporting.model_new.util;

/**
 * Marker interface for entities identified by OWL key.
 */
public interface HasOwlKey {

    String getKey();

    void setKey(String key);
}
