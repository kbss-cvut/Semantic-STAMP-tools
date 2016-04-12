package cz.cvut.kbss.inbas.reporting.util;

@FunctionalInterface
public interface TriConsumer<T, U, V> {

    /**
     * Performs this operation o the given arguments.
     */
    void accept(T t, U u, V v);
}
