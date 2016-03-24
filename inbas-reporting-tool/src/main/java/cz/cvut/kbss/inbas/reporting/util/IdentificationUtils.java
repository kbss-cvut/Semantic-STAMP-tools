package cz.cvut.kbss.inbas.reporting.util;

import java.util.Random;

public class IdentificationUtils {

    private static final int RANDOM_BOUND = 10000;

    private static final Random RANDOM = new Random();

    /**
     * Generates a pseudo-unique OWL key using current system time and a random generator.
     * @return OWL key
     */
    public static String generateKey() {
        String key = Long.toString(System.nanoTime());
        return key.concat(Integer.toString(RANDOM.nextInt(RANDOM_BOUND)));
    }
}
