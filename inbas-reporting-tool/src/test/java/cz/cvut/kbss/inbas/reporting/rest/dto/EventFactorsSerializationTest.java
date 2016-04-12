package cz.cvut.kbss.inbas.reporting.rest.dto;

import cz.cvut.kbss.inbas.reporting.dto.event.EventDto;
import cz.cvut.kbss.inbas.reporting.dto.event.EventGraph;
import cz.cvut.kbss.inbas.reporting.dto.event.EventGraphEdge;
import cz.cvut.kbss.inbas.reporting.environment.util.Generator;
import cz.cvut.kbss.inbas.reporting.model_new.*;
import cz.cvut.kbss.inbas.reporting.model_new.util.HasUri;
import cz.cvut.kbss.inbas.reporting.rest.dto.mapper.DtoMapper;
import cz.cvut.kbss.inbas.reporting.rest.dto.mapper.DtoMapperImpl;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * These tests verify correct serialization of event (or occurrence) structure serialization.
 * <p>
 * I.e. mainly correct usage of reference ids when serializing graphs with loops.
 */
public class EventFactorsSerializationTest {

    private static final URI HAS_PART_URI = URI.create(Vocabulary.p_hasPart);

    private DtoMapper dtoMapper;

    @Before
    public void setUp() {
        this.dtoMapper = new DtoMapperImpl();
    }

    @Test
    public void testSerializationOfOccurrenceWithSubEvents() throws Exception {
        final Occurrence occurrence = generateOccurrenceWithSubEvents();
        final EventGraph container = dtoMapper.occurrenceToEventGraph(occurrence);
        verifyStructure(occurrence, container);
    }

    private Occurrence generateOccurrenceWithSubEvents() {
        final Occurrence occurrence = occurrence();
        occurrence.setChildren(new HashSet<>());
        final int maxDepth = Generator.randomInt(5);
        for (int i = 0; i < Generator.randomInt(5); i++) {
            occurrence.getChildren().add(generateSubEvents(0, maxDepth));
        }
        return occurrence;
    }

    private Occurrence occurrence() {
        final Occurrence occurrence = new Occurrence();
        occurrence.setName("Test");
        occurrence.setUri(URI.create(Vocabulary.Occurrence + "#instance-" + Generator.randomInt()));
        return occurrence;
    }

    private Event generateSubEvents(int currentDepth, final int maxDepth) {
        final Event evt = event();
        if (currentDepth < maxDepth) {
            evt.setChildren(new HashSet<>());
            currentDepth++;
            for (int i = 0; i < Generator.randomInt(5); i++) {
                evt.getChildren().add(generateSubEvents(currentDepth, maxDepth));
            }
        }
        return evt;
    }

    private Event event() {
        final Event evt = new Event();
        evt.setUri(URI.create(Vocabulary.Event + "#instance-" + Generator.randomInt()));
        return evt;
    }

    private void verifyStructure(Occurrence occurrence, EventGraph graph) {
        final Map<URI, EventDto> instanceMap = new HashMap<>();
        graph.getNodes().forEach(n -> instanceMap.put(n.getUri(), n));
        final Set<URI> visited = new HashSet<>();
        visited.add(occurrence.getUri());

        verifyFactors(occurrence, occurrence.getFactors(), graph, instanceMap, visited);
        if (occurrence.getChildren() != null) {
            occurrence.getChildren().forEach(child -> {
                verifyEdge(instanceMap.get(occurrence.getUri()), instanceMap.get(child.getUri()), HAS_PART_URI, graph);
                verifyStructure(child, graph, instanceMap, visited);
            });
        }
        assertEquals(graph.getNodes().size(), visited.size());
    }

    private void verifyEdge(EventDto start, EventDto end, URI type, EventGraph graph) {
        final EventGraphEdge edge = new EventGraphEdge(start.getReferenceId(), end.getReferenceId(), type);
        assertTrue(graph.getEdges().contains(edge));
    }

    private void verifyStructure(Event event, EventGraph graph, Map<URI, EventDto> instanceMap, Set<URI> visited) {
        if (visited.contains(event.getUri())) {
            return;
        }
        visited.add(event.getUri());
        verifyFactors(event, event.getFactors(), graph, instanceMap, visited);
        if (event.getChildren() != null) {
            event.getChildren().forEach(child -> {
                verifyEdge(instanceMap.get(event.getUri()), instanceMap.get(child.getUri()), HAS_PART_URI, graph);
                verifyStructure(child, graph, instanceMap, visited);
            });
        }
    }

    private void verifyFactors(HasUri target, Set<Factor> factors, EventGraph graph, Map<URI, EventDto> instanceMap,
                               Set<URI> visited) {
        if (factors == null || factors.isEmpty()) {
            return;
        }
        for (Factor f : factors) {
            verifyEdge(instanceMap.get(f.getEvent().getUri()), instanceMap.get(target.getUri()), f.getType().getUri(),
                    graph);
            verifyStructure(f.getEvent(), graph, instanceMap, visited);
        }
    }

    @Test
    public void testSerializationOfLinksBetweenOccurrenceAndEventsAtSameLevel() throws Exception {
        final Occurrence occurrence = generateOccurrenceWithLinkChainOnSameLevel();
        final EventGraph container = dtoMapper.occurrenceToEventGraph(occurrence);
        verifyStructure(occurrence, container);
    }

    private Occurrence generateOccurrenceWithLinkChainOnSameLevel() {
        final Occurrence occurrence = occurrence();
        occurrence.setFactors(new HashSet<>());
        final int chainLength = Generator.randomInt(5);
        for (int i = 0; i < Generator.randomInt(5); i++) {
            occurrence.getFactors().add(generateChainItem(0, chainLength));
        }
        return occurrence;
    }

    private Factor generateChainItem(int currentIndex, final int maxIndex) {
        final Factor f = new Factor();
        f.setType(randomFactorType());
        f.setEvent(event());
        if (currentIndex < maxIndex) {
            f.getEvent().setFactors(new HashSet<>());
            currentIndex++;
            for (int i = 0; i < Generator.randomInt(5); i++) {
                f.getEvent().getFactors().add(generateChainItem(currentIndex, maxIndex));
            }
        }
        return f;
    }

    private FactorType randomFactorType() {
        return FactorType.values()[Generator.randomInt(FactorType.values().length) - 1];
    }

    @Test
    public void testSerializationOfOccurrenceWithSubEventsConnectedByFactors() throws Exception {
        final Occurrence occurrence = generateOccurrenceWithSubEvents();
        addFactorsToStructure(occurrence.getChildren());
        final EventGraph graph = dtoMapper.occurrenceToEventGraph(occurrence);
        verifyStructure(occurrence, graph);
    }

    private void addFactorsToStructure(Set<Event> siblings) {
        if (siblings.size() >= 2) {
            final List<Event> asList = new ArrayList<>(siblings);
            for (int i = 0; i < Generator.randomInt(asList.size()); i++) {
                final Event from = asList.get(Generator.randomIndex(asList));
                final Event to = asList.get(Generator.randomIndex(asList));
                if (from == to) {
                    continue;
                }
                if (to.getFactors() == null) {
                    to.setFactors(new HashSet<>());
                }
                final Factor f = new Factor();
                f.setEvent(from);
                f.setType(randomFactorType());
                to.getFactors().add(f);
            }
        }
        siblings.stream().filter(e -> e.getChildren() != null).forEach(e -> addFactorsToStructure(e.getChildren()));
    }

    @Test
    public void testSerializationOfOccurrenceWithFactorsConnectingEventsFromDifferentSubtrees() {
        final Occurrence occurrence = generateOccurrenceWithSubEvents();
        addCrossSubtreeFactors(occurrence.getChildren());
        final EventGraph graph = dtoMapper.occurrenceToEventGraph(occurrence);
        verifyStructure(occurrence, graph);
    }

    private void addCrossSubtreeFactors(Set<Event> siblings) {
        if (siblings.size() >= 2) {
            final List<Event> asList = new ArrayList<>(siblings);
            final Event e1 = asList.get(Generator.randomIndex(asList));
            final Event e2 = asList.get(Generator.randomIndex(asList));
            if (e1 != e2 && e1.getChildren() != null && e1.getChildren().size() >= 2 && e2.getChildren() != null &&
                    e2.getChildren().size() >= 2) {
                final List<Event> e1Children = new ArrayList<>(e1.getChildren());
                final List<Event> e2Children = new ArrayList<>(e2.getChildren());
                final Event from = e1Children.get(Generator.randomIndex(e1Children));
                final Event to = e2Children.get(Generator.randomIndex(e2Children));
                final Factor f = new Factor();
                f.setType(randomFactorType());
                f.setEvent(from);
                to.addFactor(f);
            }
        }
        siblings.stream().filter(e -> e.getChildren() != null).forEach(e -> addCrossSubtreeFactors(e.getChildren()));
    }
}
