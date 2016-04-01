package cz.cvut.kbss.inbas.reporting.util;

import java.util.Random;

public class IdentificationUtils {

    private static final int RANDOM_BOUND = 10000;

    private static final Random RANDOM = new Random();

    /**
     * Generates a pseudo-unique OWL key using current system time and a random generator.
     *
     * @return OWL key
     */
    public static String generateKey() {
        String key = Long.toString(System.nanoTime());
        return key.concat(Integer.toString(RANDOM.nextInt(RANDOM_BOUND)));
    }

    /**
     * Generates a file number (used to identify report chains).
     * <p>
     * Currently, the file numbers are simply current system time in milliseconds. A more elaborate strategy can be
     * employed if necessary.
     *
     * @return New file number
     */
    public static Long generateFileNumber() {
        return System.currentTimeMillis();
    }
}
