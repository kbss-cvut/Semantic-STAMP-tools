package cz.cvut.kbss.reporting.environment.util;

import cz.cvut.kbss.reporting.model.Event;
import cz.cvut.kbss.reporting.model.Factor;
import cz.cvut.kbss.reporting.model.util.factorgraph.FactorGraphItem;

import java.net.URI;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.*;

public class FactorGraphVerifier {
    
    /**
     * Verifies that the copy is truly a copy of the original factor graph.
     *
     * @param original Root of the original graph
     * @param copy     Root of the copied graph
     */
    public void verifyFactorGraph(FactorGraphItem original, FactorGraphItem copy) {
        assertNotSame(original, copy);
        final Set<URI> visited = new HashSet<>();
        verifyChildren(original.getChildren(), copy.getChildren(), visited);
        verifyFactors(original.getFactors(), copy.getFactors(), visited);
    }

    private void verifyChildren(Set<Event> original, Set<Event> copy, Set<URI> visited) {
        if (original == null) {
            assertNull(copy);
            return;
        }
        assertNotNull(copy);
        assertEquals(original.size(), copy.size());
        final Set<Event> sortedCopy = new TreeSet<>();
        sortedCopy.addAll(copy);
        final Iterator<Event> itOrig = original.iterator();
        final Iterator<Event> itCopy = sortedCopy.iterator();
        while (itOrig.hasNext()) {
            final Event origEvent = itOrig.next();
            final Event copyEvent = itCopy.next();
            verifyEvents(origEvent, copyEvent, visited);
        }
    }

    private void verifyEvents(Event original, Event copy, Set<URI> visited) {
        if (visited.contains(original.getUri())) {
            return;
        }
        visited.add(original.getUri());
        assertNotSame(original, copy);
        assertEquals(original.getIndex(), copy.getIndex());
        assertEquals(original.getEventTypes(), copy.getEventTypes());
        assertEquals(original.getStartTime(), copy.getStartTime());
        verifyChildren(original.getChildren(), copy.getChildren(), visited);
        verifyFactors(original.getFactors(), copy.getFactors(), visited);
    }

    private void verifyFactors(Set<Factor> original, Set<Factor> copy, Set<URI> visited) {
        if (original == null) {
            assertNull(copy);
            return;
        }
        assertNotNull(copy);
        assertEquals(original.size(), copy.size());
        boolean found;
        for (Factor of : original) {
            found = false;
            for (Factor cf : copy) {
                if (of.getEvent().getEventTypes().equals(cf.getEvent().getEventTypes())) {
                    found = true;
                    assertEquals(of.getTypes(), cf.getTypes());
                    verifyEvents(of.getEvent(), cf.getEvent(), visited);
                    break;
                }
            }
            assertTrue(found);
        }
    }
}
