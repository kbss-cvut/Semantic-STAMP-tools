package cz.cvut.kbss.inbas.reporting.rest.dto.mapper;

import cz.cvut.kbss.inbas.reporting.dto.CorrectiveMeasureRequestDto;
import cz.cvut.kbss.inbas.reporting.dto.OccurrenceReportDto;
import cz.cvut.kbss.inbas.reporting.dto.agent.AgentDto;
import cz.cvut.kbss.inbas.reporting.dto.agent.OrganizationDto;
import cz.cvut.kbss.inbas.reporting.dto.agent.PersonDto;
import cz.cvut.kbss.inbas.reporting.dto.event.EventDto;
import cz.cvut.kbss.inbas.reporting.dto.event.EventGraph;
import cz.cvut.kbss.inbas.reporting.dto.event.EventGraphEdge;
import cz.cvut.kbss.inbas.reporting.dto.event.OccurrenceDto;
import cz.cvut.kbss.inbas.reporting.model_new.*;
import cz.cvut.kbss.inbas.reporting.model_new.util.HasUri;
import cz.cvut.kbss.inbas.reporting.util.TriConsumer;
import org.mapstruct.Mapper;

import java.net.URI;
import java.util.*;
import java.util.function.Consumer;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class})
public abstract class DtoMapper {

    private final SplittableRandom random = new SplittableRandom();
    private static final URI HAS_PART_URI = URI.create(Vocabulary.p_hasPart);

    public LogicalDocument reportToReportDto(LogicalDocument report) {
        if (report == null) {
            return null;
        }
        if (report instanceof OccurrenceReport) {
            return occurrenceReportToOccurrenceReportDto((OccurrenceReport) report);
        }
        return report;
    }

    public LogicalDocument reportDtoToReport(LogicalDocument dto) {
        if (dto == null) {
            return null;
        }
        if (dto instanceof OccurrenceReportDto) {
            return occurrenceReportDtoToOccurrenceReport((OccurrenceReportDto) dto);
        }
        return dto;
    }

    public abstract OccurrenceReportDto occurrenceReportToOccurrenceReportDto(OccurrenceReport report);

    public abstract OccurrenceReport occurrenceReportDtoToOccurrenceReport(OccurrenceReportDto dto);

    public CorrectiveMeasureRequestDto correctiveMeasureRequestToDto(CorrectiveMeasureRequest req) {
        if (req == null) {
            return null;
        }
        final CorrectiveMeasureRequestDto dto = new CorrectiveMeasureRequestDto();
        dto.setUri(req.getUri());
        dto.setDescription(req.getDescription());
        final Set<AgentDto> agents = new HashSet<>();
        if (req.getResponsibleOrganizations() != null) {
            req.getResponsibleOrganizations().forEach(o -> agents.add(organizationToOrganizationDto(o)));
        }
        if (req.getResponsiblePersons() != null) {
            req.getResponsiblePersons().forEach(p -> agents.add(personToPersonDto(p)));
        }
        dto.setResponsibleAgents(agents);
        if (req.getBasedOnEvent() != null) {
            dto.setBasedOn(eventToEventDto(req.getBasedOnEvent()));
        } else if (req.getBasedOnOccurrence() != null) {
            dto.setBasedOn(occurrenceToOccurrenceDto(req.getBasedOnOccurrence()));
        }
        return dto;
    }

    public CorrectiveMeasureRequest dtoToCorrectiveMeasureRequest(CorrectiveMeasureRequestDto dto) {
        if (dto == null) {
            return null;
        }
        final CorrectiveMeasureRequest req = new CorrectiveMeasureRequest();
        req.setUri(dto.getUri());
        req.setDescription(dto.getDescription());
        if (dto.getResponsibleAgents() != null) {
            final Set<Person> persons = new HashSet<>(dto.getResponsibleAgents().size());
            final Set<Organization> organizations = new HashSet<>(dto.getResponsibleAgents().size());
            for (AgentDto agent : dto.getResponsibleAgents()) {
                if (agent instanceof PersonDto) {
                    persons.add(personDtoToPerson((PersonDto) agent));
                } else {
                    organizations.add(organizationDtoToOrganization((OrganizationDto) agent));
                }
            }
            req.setResponsiblePersons(persons);
            req.setResponsibleOrganizations(organizations);
        }
        if (dto.getBasedOn() != null) {
            if (dto.getBasedOn() instanceof OccurrenceDto) {
                req.setBasedOnOccurrence(occurrenceDtoToOccurrence((OccurrenceDto) dto.getBasedOn()));
            } else {
                req.setBasedOnEvent(eventDtoToEvent(dto.getBasedOn()));
            }
        }
        return req;
    }

    public abstract PersonDto personToPersonDto(Person person);

    public abstract Person personDtoToPerson(PersonDto dto);

    public abstract OrganizationDto organizationToOrganizationDto(Organization organization);

    public abstract Organization organizationDtoToOrganization(OrganizationDto dto);

    public abstract EventDto eventToEventDto(Event event);

    public abstract Event eventDtoToEvent(EventDto dto);

    public abstract OccurrenceDto occurrenceToOccurrenceDto(Occurrence occurrence);

    public abstract Occurrence occurrenceDtoToOccurrence(OccurrenceDto dto);

    public EventGraph occurrenceToEventGraph(Occurrence occurrence) {
        if (occurrence == null) {
            return null;
        }
        final Map<URI, EventDto> instanceMap = new LinkedHashMap<>();
        final Set<EventGraphEdge> edges = new HashSet<>();
        // First run collects nodes and sets reference ids on them
        traverseTree(occurrence, dto -> {
            dto.setReferenceId(random.nextInt());
            instanceMap.put(dto.getUri(), dto);
        }, (a, b, c) -> {
        }, new HashSet<>());
        // Second run collects edges
        traverseTree(occurrence, (n) -> {
        }, (from, to, uri) -> {
            final EventGraphEdge e = new EventGraphEdge(instanceMap.get(from.getUri()).getReferenceId(),
                    instanceMap.get(to.getUri()).getReferenceId(), uri);
            edges.add(e);
        }, new HashSet<>());
        final EventGraph graph = new EventGraph();
        graph.setEdges(edges);
        graph.setNodes(new ArrayList<>(instanceMap.values()));
        return graph;
    }

    private void traverseTree(Occurrence occurrence, Consumer<EventDto> nodeConsumer,
                              TriConsumer<HasUri, HasUri, URI> edgeConsumer, Set<URI> visited) {
        if (visited.contains(occurrence.getUri())) {
            return;
        }
        nodeConsumer.accept(occurrenceToOccurrenceDto(occurrence));
        visited.add(occurrence.getUri());
        if (occurrence.getFactors() != null) {

            for (Factor f : occurrence.getFactors()) {
                edgeConsumer.accept(f.getEvent(), occurrence, f.getType().getUri());
                traverseTree(f.getEvent(), nodeConsumer, edgeConsumer, visited);
            }
        }
        if (occurrence.getChildren() != null) {
            occurrence.getChildren().forEach(child -> {
                edgeConsumer.accept(occurrence, child, HAS_PART_URI);
                traverseTree(child, nodeConsumer, edgeConsumer, visited);
            });
        }
    }

    private void traverseTree(Event event, Consumer<EventDto> nodeConsumer,
                              TriConsumer<HasUri, HasUri, URI> edgeConsumer, Set<URI> visited) {
        if (visited.contains(event.getUri())) {
            return;
        }
        nodeConsumer.accept(eventToEventDto(event));
        visited.add(event.getUri());
        if (event.getFactors() != null) {

            for (Factor f : event.getFactors()) {
                edgeConsumer.accept(f.getEvent(), event, f.getType().getUri());
                traverseTree(f.getEvent(), nodeConsumer, edgeConsumer, visited);
            }
        }
        if (event.getChildren() != null) {
            event.getChildren().forEach(child -> {
                edgeConsumer.accept(event, child, HAS_PART_URI);
                traverseTree(child, nodeConsumer, edgeConsumer, visited);
            });
        }
    }

    public Occurrence eventGraphToOccurrence(EventGraph graph) {
        return null;
    }
}
