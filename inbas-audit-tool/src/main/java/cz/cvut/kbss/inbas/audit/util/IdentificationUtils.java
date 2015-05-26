package cz.cvut.kbss.inbas.audit.util;

import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author ledvima1
 */
public class IdentificationUtils {

    private static final MessageDigest digest = initDigest();

    private static MessageDigest initDigest() {
        try {
            return MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Generates a unique OWL key from the specified URI.
     * @param uri Key base
     * @return OWL key
     */
    public static String generateKey(URI uri) {
        final String hash = new String(digest.digest(uri.toString().getBytes()));
        digest.reset();
        return hash;
    }
}
