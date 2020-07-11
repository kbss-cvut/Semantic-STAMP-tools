package cz.cvut.kbss.datatools.bpm2stampo.common.refs.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CompareUtils {

    /**
     * Compares elements of two Collections. When the input parameters are not null, the delegates to Collections::equals.
     * Null value of a parameter is interpreted as if it was an empty collection.
     * @param expected
     * @param actual
     * @param <T>
     * @return
     *
     */
    public static <T> boolean equals(Collection<T> expected, Collection<T> actual){
        return (isEmptyOrNull(expected ) && isEmptyOrNull(actual)) ||
                expected.equals(actual);
    }

    /**
     * Compares elements of two Collections as if they were sets.
     * Implementation: Creates sets from the input and delegates then to {@link CompareUtils#equals(Collection, Collection)}
     * @param expected
     * @param actual
     * @param <T>
     * @return
     */
    public static <T> boolean equalsAsSets(Collection<T> expected, Collection<T> actual){
        Set<T> expectedSet = expected != null ? new HashSet<>(expected) : null;
        Set<T> actualSet = actual != null ? new HashSet<>(actual) : null;
        return equals(expectedSet, actualSet);
    }

    public static boolean isEmptyOrNull(Collection c){
        return c == null || c.isEmpty();
    }
}
