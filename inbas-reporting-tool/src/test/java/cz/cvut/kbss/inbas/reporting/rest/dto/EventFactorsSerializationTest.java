package cz.cvut.kbss.inbas.reporting.rest.dto;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.kbss.inbas.reporting.dto.event.OccurrenceDto;
import cz.cvut.kbss.inbas.reporting.environment.util.Generator;
import cz.cvut.kbss.inbas.reporting.model_new.*;
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

    private ObjectMapper objectMapper;

    private DtoMapper dtoMapper;

    @Before
    public void setUp() {
        this.objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.dtoMapper = new DtoMapperImpl();
    }

    @Test
    public void testSerializationOfOccurrenceWithSubEvents() throws Exception {
        final Occurrence occurrence = generateOccurrenceWithSubEvents();
        final Node occurrenceTree = buildTreeStructure(occurrence);
        final Occurrence result = serializeAndDeserialize(occurrence);
        verifyTreeStructure(occurrenceTree, result);
    }

    private Occurrence serializeAndDeserialize(Occurrence occurrence) throws java.io.IOException {
        final OccurrenceDto dto = dtoMapper.occurrenceToOccurrenceDto(occurrence);
        final String output = objectMapper.writeValueAsString(dto);
        final OccurrenceDto readDto = objectMapper.readValue(output, OccurrenceDto.class);
        return dtoMapper.occurrenceDtoToOccurrence(readDto);
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

    private Node buildTreeStructure(Occurrence occurrence) {
        final Node n = new Node(occurrence.getUri());
        if (!occurrence.getChildren().isEmpty()) {
            occurrence.getChildren().forEach(child -> n.addChild(buildTreeStructure(child)));
        }
        return n;
    }

    private Node buildTreeStructure(Event event) {
        final Node n = new Node(event.getUri());
        if (event.getChildren() != null && !event.getChildren().isEmpty()) {
            event.getChildren().forEach(child -> n.addChild(buildTreeStructure(child)));
        }
        return n;
    }

    private void verifyTreeStructure(Node expected, Occurrence actual) {
        assertEquals(expected.uri, actual.getUri());
        assertEquals(expected.children.size(), actual.getChildren().size());
        actual.getChildren().forEach(child -> {
            assertTrue(expected.children.containsKey(child.getUri()));
            verifyTreeStructure(expected.children.get(child.getUri()), child);
        });
    }

    private void verifyTreeStructure(Node expected, Event actual) {
        assertEquals(expected.uri, actual.getUri());
        if (!expected.children.isEmpty()) {
            assertEquals(expected.children.size(), actual.getChildren().size());
            actual.getChildren().forEach(child -> {
                assertTrue(expected.children.containsKey(child.getUri()));
                verifyTreeStructure(expected.children.get(child.getUri()), child);
            });
        }
    }

    @Test
    public void testSerializationOfLinksBetweenOccurrenceAndEventsAtSameLevel() throws Exception {
        final Occurrence occurrence = generateOccurrenceWithLinkChainOnSameLevel();
        final Set<Link> origLinks = resolveLinks(occurrence);
        final Occurrence result = serializeAndDeserialize(occurrence);
        final Set<Link> resultLinks = resolveLinks(result);
        assertEquals(origLinks, resultLinks);
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

    private Set<Link> resolveLinks(Occurrence occurrence) {
        final Set<Link> links = new HashSet<>();
        if (occurrence.getFactors() != null) {
            occurrence.getFactors()
                      .forEach(f -> links.add(new Link(f.getEvent().getUri(), occurrence.getUri(), f.getType())));
        }
        if (occurrence.getChildren() != null) {
            final Set<Object> visited = new HashSet<>();
            visited.add(occurrence);
            occurrence.getChildren().forEach(child -> links.addAll(resolveLinks(child, visited)));
        }
        return links;
    }

    private Set<Link> resolveLinks(Event event, Set<Object> visitedObjects) {
        if (visitedObjects.contains(event)) {
            return Collections.emptySet();
        }
        visitedObjects.add(event);
        final Set<Link> links = new HashSet<>();
        if (event.getFactors() != null) {
            event.getFactors()
                 .forEach(f -> links.add(new Link(f.getEvent().getUri(), event.getUri(), f.getType())));
        }
        if (event.getChildren() != null) {
            event.getChildren().forEach(child -> links.addAll(resolveLinks(child, visitedObjects)));
        }
        return links;
    }

    @Test
    public void testSerializationOfOccurrenceWithSubEventsConnectedByFactors() throws Exception {
        // TODO Fix the serialization issue (it causes SO)
        final Occurrence occurrence = generateOccurrenceWithSubEvents();
        addFactorsToStructure(occurrence.getChildren());
        final Node tree = buildTreeStructure(occurrence);
        final Set<Link> links = resolveLinks(occurrence);
        final Occurrence result = serializeAndDeserialize(occurrence);
        verifyTreeStructure(tree, result);
        final Set<Link> resultLinks = resolveLinks(result);
        assertEquals(links, resultLinks);
    }

    private void addFactorsToStructure(Set<Event> siblings) {
        if (siblings.size() >= 2) {
            final List<Event> asList = new ArrayList<>(siblings);
            for (int i = 0; i < Generator.randomInt(asList.size()); i++) {
                final Event from = asList.get(Generator.randomInt(asList.size()) - 1);
                final Event to = asList.get(Generator.randomInt(asList.size()) - 1);
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

    private static class Node {

        private final URI uri;

        private final Map<URI, Node> children = new HashMap<>();

        Node(URI uri) {
            this.uri = uri;
        }

        void addChild(Node child) {
            children.put(child.uri, child);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Node node = (Node) o;

            return uri.equals(node.uri);
        }

        @Override
        public int hashCode() {
            return uri.hashCode();
        }
    }

    private static class Link {
        private final URI from;
        private final URI to;
        private final FactorType type;

        Link(URI from, URI to, FactorType type) {
            this.from = from;
            this.to = to;
            this.type = type;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Link link = (Link) o;

            return from.equals(link.from) && to.equals(link.to) && type == link.type;
        }

        @Override
        public int hashCode() {
            int result = from.hashCode();
            result = 31 * result + to.hashCode();
            result = 31 * result + type.hashCode();
            return result;
        }
    }
}
