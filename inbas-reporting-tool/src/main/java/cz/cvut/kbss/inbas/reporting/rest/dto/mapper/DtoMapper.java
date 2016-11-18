package cz.cvut.kbss.inbas.reporting.rest.dto.mapper;

import cz.cvut.kbss.inbas.reporting.dto.CorrectiveMeasureRequestDto;
import cz.cvut.kbss.inbas.reporting.dto.OccurrenceReportDto;
import cz.cvut.kbss.inbas.reporting.dto.SafetyIssueReportDto;
import cz.cvut.kbss.inbas.reporting.dto.agent.AgentDto;
import cz.cvut.kbss.inbas.reporting.dto.agent.OrganizationDto;
import cz.cvut.kbss.inbas.reporting.dto.agent.PersonDto;
import cz.cvut.kbss.inbas.reporting.dto.event.*;
import cz.cvut.kbss.inbas.reporting.dto.safetyissue.AuditFindingBase;
import cz.cvut.kbss.inbas.reporting.dto.safetyissue.OccurrenceBase;
import cz.cvut.kbss.inbas.reporting.dto.safetyissue.SafetyIssueBase;
import cz.cvut.kbss.inbas.reporting.model.*;
import cz.cvut.kbss.inbas.reporting.model.audit.AuditFinding;
import cz.cvut.kbss.inbas.reporting.model.audit.AuditReport;
import cz.cvut.kbss.inbas.reporting.model.safetyissue.SafetyIssue;
import cz.cvut.kbss.inbas.reporting.model.safetyissue.SafetyIssueReport;
import cz.cvut.kbss.inbas.reporting.model.util.factorgraph.FactorGraphItem;
import cz.cvut.kbss.inbas.reporting.model.util.factorgraph.traversal.DefaultFactorGraphTraverser;
import cz.cvut.kbss.inbas.reporting.model.util.factorgraph.traversal.FactorGraphTraverser;
import cz.cvut.kbss.inbas.reporting.service.AuditReportService;
import cz.cvut.kbss.inbas.reporting.service.OccurrenceService;
import cz.cvut.kbss.inbas.reporting.service.repository.GenericEntityService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.util.*;
import java.util.function.Function;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class})
public abstract class DtoMapper {

    private final SplittableRandom random = new SplittableRandom();
    private static final URI HAS_PART_URI = URI.create(Vocabulary.s_p_has_part);

    private static final Map<Class<?>, Class<?>> mappedClasses = initMappedClasses();

    private Map<URI, EventDto> eventDtoRegistry;

    @Autowired
    private OccurrenceService occurrenceService;

    @Autowired
    private AuditReportService auditReportService;

    @Autowired
    private GenericEntityService entityService;

    private void reset() {
        this.eventDtoRegistry = new LinkedHashMap<>();
    }

    // Don't forget to add the classes here when adding new mapping methods
    private static Map<Class<?>, Class<?>> initMappedClasses() {
        final Map<Class<?>, Class<?>> map = new HashMap<>();
        map.put(OccurrenceReport.class, OccurrenceReportDto.class);
        map.put(OccurrenceReportDto.class, OccurrenceReport.class);
        map.put(SafetyIssueReport.class, SafetyIssueReportDto.class);
        map.put(SafetyIssueReportDto.class, SafetyIssueReport.class);
        map.put(AuditReport.class, AuditReport.class);
        map.put(CorrectiveMeasureRequest.class, CorrectiveMeasureRequestDto.class);
        map.put(CorrectiveMeasureRequestDto.class, CorrectiveMeasureRequest.class);
        map.put(Person.class, PersonDto.class);
        map.put(PersonDto.class, Person.class);
        map.put(Organization.class, OrganizationDto.class);
        map.put(OrganizationDto.class, Organization.class);
        map.put(Event.class, EventDto.class);
        map.put(EventDto.class, Event.class);
        map.put(Occurrence.class, OccurrenceDto.class);
        map.put(Occurrence.class, FactorGraph.class);
        map.put(OccurrenceDto.class, Occurrence.class);
        map.put(FactorGraph.class, Occurrence.class);
        return map;
    }

    public DtoMapper() {
        reset();
    }

    /**
     * Returns true if the specified classes can be mapped by this mapper.
     *
     * @param cls The class to map
     * @return Whether the class can be mapped
     */
    public boolean canMap(Class<?> cls) {
        return mappedClasses.containsKey(cls);
    }

    public LogicalDocument reportToReportDto(LogicalDocument report) {
        if (report == null) {
            return null;
        }
        reset();
        if (report instanceof OccurrenceReport) {
            return occurrenceReportToOccurrenceReportDto((OccurrenceReport) report);
        }
        if (report instanceof SafetyIssueReport) {
            return safetyIssueReportToSafetyIssueReportDto((SafetyIssueReport) report);
        }
        if (report instanceof AuditReport) {
            return auditReportToAuditReportDto((AuditReport) report);
        }
        return report;
    }

    public LogicalDocument reportDtoToReport(LogicalDocument dto) {
        if (dto == null) {
            return null;
        }
        reset();
        if (dto instanceof OccurrenceReportDto) {
            return occurrenceReportDtoToOccurrenceReport((OccurrenceReportDto) dto);
        }
        if (dto instanceof SafetyIssueReportDto) {
            return safetyIssueReportDtoToSafetyIssueReport((SafetyIssueReportDto) dto);
        }
        if (dto instanceof AuditReport) {
            return auditReportDtoToAuditReport((AuditReport) dto);
        }
        return dto;
    }

    @Mapping(source = "occurrence", target = "occurrence")
    @Mapping(source = "occurrence", target = "factorGraph", dependsOn = "occurrence")
    public abstract OccurrenceReportDto occurrenceReportToOccurrenceReportDto(OccurrenceReport report);

    @Mapping(source = "factorGraph", target = "occurrence")
    public abstract OccurrenceReport occurrenceReportDtoToOccurrenceReport(OccurrenceReportDto dto);

    @Mapping(source = "safetyIssue", target = "safetyIssue")
    @Mapping(source = "safetyIssue", target = "factorGraph", dependsOn = "safetyIssue")
    public abstract SafetyIssueReportDto safetyIssueReportToSafetyIssueReportDto(SafetyIssueReport report);

    @Mapping(source = "factorGraph", target = "safetyIssue")
    public abstract SafetyIssueReport safetyIssueReportDtoToSafetyIssueReport(SafetyIssueReportDto dto);

    public AuditReport auditReportToAuditReportDto(AuditReport report) {
        assert report != null;
        report.addType(Vocabulary.s_c_audit_report);
        return report;
    }

    public AuditReport auditReportDtoToAuditReport(AuditReport dto) {
        assert dto != null;
        dto.getTypes().remove(Vocabulary.s_c_audit_report);
        return dto;
    }

    public CorrectiveMeasureRequestDto correctiveMeasureRequestToDto(CorrectiveMeasureRequest req) {
        if (req == null) {
            return null;
        }
        final CorrectiveMeasureRequestDto dto = new CorrectiveMeasureRequestDto();
        dto.setUri(req.getUri());
        dto.setDescription(req.getDescription());
        dto.setPhaseLastModified(req.getPhaseLastModified());
        dto.setImplemented(req.isImplemented());
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
        req.setImplemented(dto.isImplemented());
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

    public OccurrenceDto occurrenceToOccurrenceDto(Occurrence occurrence) {
        if (occurrence == null) {
            return null;
        }
        OccurrenceDto dto = new OccurrenceDto();
        dto.setUri(occurrence.getUri());
        if (occurrence.getTypes() != null) {
            dto.setTypes(new HashSet<>(occurrence.getTypes()));
        }
        dto.setKey(occurrence.getKey());
        dto.setName(occurrence.getName());
        dto.setStartTime(occurrence.getStartTime());
        dto.setEndTime(occurrence.getEndTime());
        dto.setEventTypes(occurrence.getEventTypes());
        dto.setQuestion(occurrence.getQuestion());
        dto.setReferenceId(random.nextInt());
        eventDtoRegistry.put(dto.getUri(), dto);

        return dto;
    }

    public abstract Occurrence occurrenceDtoToOccurrence(OccurrenceDto dto);

    public FactorGraph occurrenceToFactorGraph(Occurrence occurrence) {
        return serializeFactorGraph(occurrence);
    }

    private FactorGraph serializeFactorGraph(FactorGraphItem root) {
        if (root == null) {
            return null;
        }
        final DtoNodeVisitor nodeVisitor = new DtoNodeVisitor(this, random, eventDtoRegistry);
        final FactorGraphTraverser traverser = new DefaultFactorGraphTraverser(nodeVisitor, null);
        traverser.traverse(root);
        final DtoEdgeVisitor edgeVisitor = new DtoEdgeVisitor(nodeVisitor.getInstanceMap());
        traverser.setFactorGraphEdgeVisitor(edgeVisitor);
        traverser.traverse(root);
        final FactorGraph graph = new FactorGraph();
        graph.setNodes(new ArrayList<>(nodeVisitor.getInstanceMap().values()));
        graph.setEdges(edgeVisitor.getEdges());
        // Have to reset it here, because some instances may contain more than one factor graph (e.g. safety issue) and
        // the registry has to be empty before every serialization
        reset();
        return graph;
    }

    public Occurrence factorGraphToOccurrence(FactorGraph graph) {
        return deserializeFactorGraph((dto) -> {
            if (dto instanceof OccurrenceDto) {
                return occurrenceDtoToOccurrence((OccurrenceDto) dto);
            } else {
                return eventDtoToEvent(dto);
            }
        }, Occurrence.class, graph);
    }

    private <T> T deserializeFactorGraph(Function<EventDto, FactorGraphItem> rootDeserializer, Class<T> targetType,
                                         FactorGraph graph) {
        if (graph == null) {
            return null;
        }
        final Map<Integer, EventDto> dtoMap = new HashMap<>();
        graph.getNodes().forEach(n -> dtoMap.put(n.getReferenceId(), n));
        final Map<Integer, FactorGraphItem> instanceMap = new HashMap<>(dtoMap.size());
        graph.getNodes().forEach(n -> instanceMap.put(n.getReferenceId(), rootDeserializer.apply(n)));
        transformEdgesToRelations(graph, dtoMap, instanceMap);
        final Optional<FactorGraphItem> occurrence = instanceMap.values().stream()
                                                                .filter(item -> targetType
                                                                        .isAssignableFrom(item.getClass())).findFirst();
        assert occurrence.isPresent();
        return targetType.cast(occurrence.get());
    }

    private void transformEdgesToRelations(FactorGraph graph, Map<Integer, EventDto> dtoMap,
                                           Map<Integer, FactorGraphItem> instanceMap) {
        for (FactorGraphEdge e : graph.getEdges()) {
            final EventDto source = dtoMap.get(e.getFrom());
            final EventDto target = dtoMap.get(e.getTo());
            if (e.getLinkType().equals(HAS_PART_URI)) {
                assert instanceMap.get(target.getReferenceId()) instanceof Event;
                instanceMap.get(source.getReferenceId()).addChild((Event) instanceMap.get(target.getReferenceId()));
            } else {
                final Factor factor = new Factor();
                factor.addType(e.getLinkType());
                factor.setEvent((Event) instanceMap.get(source.getReferenceId()));
                instanceMap.get(target.getReferenceId()).addFactor(factor);
            }
        }
    }

    public SafetyIssueDto safetyIssueToSafetyIssueDto(SafetyIssue issue) {
        if (issue == null) {
            return null;
        }
        SafetyIssueDto dto = new SafetyIssueDto();
        dto.setUri(issue.getUri());
        if (issue.getTypes() != null) {
            dto.setTypes(new HashSet<>(issue.getTypes()));
        }
        dto.setName(issue.getName());
        dto.setState(issue.getState());
        dto.setReferenceId(random.nextInt());
        eventDtoRegistry.put(dto.getUri(), dto);
        if (issue.getBasedOnOccurrences() != null) {
            issue.getBasedOnOccurrences().forEach(o -> dto.addBase(occurrenceToSafetyIssueBase(o)));
        }
        if (issue.getBasedOnFindings() != null) {
            issue.getBasedOnFindings().forEach(f -> dto.addBase(auditFindingToSafetyIssueBase(f)));
        }
        return dto;
    }

    private SafetyIssueBase occurrenceToSafetyIssueBase(Occurrence occurrence) {
        final OccurrenceBase base = new OccurrenceBase(occurrence);
        final OccurrenceReport report = occurrenceService.findByOccurrence(occurrence);
        assert report != null;
        base.setReportKey(report.getKey());
        base.setSeverity(report.getSeverityAssessment());
        return base;
    }

    private SafetyIssueBase auditFindingToSafetyIssueBase(AuditFinding finding) {
        final AuditFindingBase base = new AuditFindingBase(finding);
        final AuditReport report = auditReportService.findByAuditFinding(finding);
        assert report != null;
        base.setReportKey(report.getKey());
        return base;
    }

    public SafetyIssue safetyIssueDtoToSafetyIssue(SafetyIssueDto dto) {
        if (dto == null) {
            return null;
        }
        SafetyIssue issue = new SafetyIssue();
        issue.setUri(dto.getUri());
        issue.setName(dto.getName());
        if (dto.getTypes() != null) {
            issue.setTypes(new HashSet<>(dto.getTypes()));
        }
        issue.setState(dto.getState());
        if (dto.getBasedOn() != null) {
            dto.getBasedOn().forEach(base -> safetyIssueDtoBaseToBase(base, issue));
        }

        return issue;
    }

    private void safetyIssueDtoBaseToBase(SafetyIssueBase base, SafetyIssue target) {
        if (base.getTypes().contains(Vocabulary.s_c_Occurrence)) {
            target.addBase(occurrenceService.find(base.getUri()));
        } else if (base.getTypes().contains(Vocabulary.s_c_audit_finding)) {
            target.addBase(entityService.find(AuditFinding.class, base.getUri()));
        }
    }

    public FactorGraph safetyIssueToFactorGraph(SafetyIssue issue) {
        return serializeFactorGraph(issue);
    }

    public SafetyIssue factorGraphToSafetyIssue(FactorGraph graph) {
        return deserializeFactorGraph((dto) -> {
            if (dto instanceof SafetyIssueDto) {
                return safetyIssueDtoToSafetyIssue((SafetyIssueDto) dto);
            } else {
                return eventDtoToEvent(dto);
            }
        }, SafetyIssue.class, graph);
    }
}
