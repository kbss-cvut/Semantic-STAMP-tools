package cz.cvut.kbss.reporting.service.visitor;

import cz.cvut.kbss.reporting.model.Event;
import cz.cvut.kbss.reporting.model.Occurrence;
import cz.cvut.kbss.reporting.model.safetyissue.SafetyIssue;
import cz.cvut.kbss.reporting.model.util.factorgraph.FactorGraphNodeVisitor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * Sets indexes to item children if the indexes are missing.
 */
public class EventChildIndexer implements FactorGraphNodeVisitor {

    @Override
    public void visit(Occurrence occurrence) {
        if (occurrence.getChildren() != null) {
            addIndexesIfNecessary(occurrence.getChildren());
        }
    }

    @Override
    public void visit(SafetyIssue issue) {
        // Do nothing
    }

    @Override
    public void visit(Event event) {
        if (event.getChildren() != null) {
            addIndexesIfNecessary(event.getChildren());
        }
    }

    private void addIndexesIfNecessary(Set<Event> children) {
        final boolean indexMissing = children.stream().anyMatch(c -> c.getIndex() == null);
        if (!indexMissing) {
            return;
        }
        final List<Event> events = new ArrayList<>(children);
        events.sort(Comparator.comparingInt(a -> a.getEventTypes().hashCode()));
        for (int i = 0; i < events.size(); i++) {
            events.get(i).setIndex(i);
        }
    }
}
