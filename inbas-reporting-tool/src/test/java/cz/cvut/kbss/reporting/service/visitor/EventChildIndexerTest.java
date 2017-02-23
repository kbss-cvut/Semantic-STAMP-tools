package cz.cvut.kbss.reporting.service.visitor;

import cz.cvut.kbss.reporting.environment.generator.Generator;
import cz.cvut.kbss.reporting.environment.generator.OccurrenceReportGenerator;
import cz.cvut.kbss.reporting.model.Event;
import cz.cvut.kbss.reporting.model.Occurrence;
import org.junit.Test;

import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class EventChildIndexerTest {

    private final EventChildIndexer indexer = new EventChildIndexer();

    @Test
    public void ordersItemsAndSetsIndexesOnThemWhenIndexesAreMissing() {
        final Occurrence parent = OccurrenceReportGenerator.generateOccurrence();
        final Set<Event> events = generateEvents();
        parent.setChildren(events);
        indexer.visit(parent);
        events.forEach(e -> assertNotNull(e.getIndex()));
    }

    private Set<Event> generateEvents() {
        final Set<Event> events = new HashSet<>();
        for (int i = 0; i < Generator.randomInt(5, 10); i++) {
            events.add(OccurrenceReportGenerator.generateEvent());
        }
        return events;
    }

    @Test
    public void doesNothingWhenOccurrenceHasNoChildren() {
        final Occurrence parent = OccurrenceReportGenerator.generateOccurrence();
        assertNull(parent.getChildren());
        indexer.visit(parent);
        assertNull(parent.getChildren());
    }

    @Test
    public void doesNothingWhenEventHasNoChildren() {
        final Event parent = OccurrenceReportGenerator.generateEvent();
        assertNull(parent.getChildren());
        indexer.visit(parent);
        assertNull(parent.getChildren());
    }

    @Test
    public void doesNothingWhenChildrenHaveIndexesSet() {
        final Set<Event> events = generateEvents();
        final Map<URI, Integer> indexMap = new HashMap<>();
        events.forEach(e -> {
            e.setIndex(Generator.randomInt());
            e.setUri(Generator.generateUri());
            indexMap.put(e.getUri(), e.getIndex());
        });
        final Occurrence parent = OccurrenceReportGenerator.generateOccurrence();
        parent.setChildren(events);
        indexer.visit(parent);
        parent.getChildren().forEach(e -> assertEquals(indexMap.get(e.getUri()), e.getIndex()));
    }
}
