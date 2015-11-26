package cz.cvut.kbss.inbas.audit.rest.dto.mapper;

import cz.cvut.kbss.inbas.audit.model.Aircraft;
import cz.cvut.kbss.inbas.audit.model.reports.EventTypeAssessment;
import cz.cvut.kbss.inbas.audit.model.reports.Factor;
import cz.cvut.kbss.inbas.audit.model.reports.InvestigationReport;
import cz.cvut.kbss.inbas.audit.model.reports.PreliminaryReport;
import cz.cvut.kbss.inbas.audit.model.reports.incursions.Intruder;
import cz.cvut.kbss.inbas.audit.model.reports.incursions.PersonIntruder;
import cz.cvut.kbss.inbas.audit.model.reports.incursions.RunwayIncursion;
import cz.cvut.kbss.inbas.audit.model.reports.incursions.Vehicle;
import cz.cvut.kbss.inbas.audit.rest.dto.model.*;
import cz.cvut.kbss.inbas.audit.rest.dto.model.incursion.*;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.SplittableRandom;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class})
public abstract class ReportMapper {

    private final SplittableRandom random = new SplittableRandom();

    public OccurrenceReportInfo occurrenceReportToOccurrenceReportInfo(PreliminaryReport report) {
        return new OccurrenceReportInfo(report);
    }

    public abstract PreliminaryReportDto preliminaryReportToPreliminaryReportDto(PreliminaryReport report);

    public abstract PreliminaryReport preliminaryReportDtoToPreliminaryReport(PreliminaryReportDto dto);

    public InvestigationReportDto investigationReportToInvestigationReportDto(InvestigationReport report) {
        if (report == null) {
            return null;
        }
        final InvestigationReportDto dto = new InvestigationReportDto();
        dto.setUri(report.getUri());
        dto.setKey(report.getKey());
        dto.setOccurrence(report.getOccurrence());
        dto.setAuthor(report.getAuthor());
        dto.setCreated(report.getCreated());
        dto.setLastEditedBy(report.getLastEditedBy());
        dto.setLastEdited(report.getLastEdited());
        dto.setRevision(report.getRevision());
        dto.setSummary(report.getSummary());
        dto.setCorrectiveMeasures(report.getCorrectiveMeasures());
        dto.setInitialReports(report.getInitialReports());
        dto.setSeverityAssessment(report.getSeverityAssessment());
        dto.setRootFactor(factorToFactorDto(report.getRootFactor()));
        dto.setLinks(createLinks(dto.getRootFactor(), report.getRootFactor()));
        return dto;
    }

    private Links createLinks(FactorDto root, Factor factor) {
        if (root == null) {
            return null;
        }
        final Map<URI, FactorDto> dtos = new HashMap<>();
        mapFactors(root, dtos);
        final Links links = new Links();
        addLinks(factor, links, dtos);
        return links;
    }

    private void mapFactors(FactorDto root, Map<URI, FactorDto> map) {
        map.put(root.getUri(), root);
        if (root.getChildren() != null) {
            root.getChildren().forEach(child -> mapFactors(child, map));
        }
    }

    private void addLinks(Factor root, Links links, Map<URI, FactorDto> dtos) {
        final FactorDto rootDto = dtos.get(root.getUri());
        assert rootDto != null;

        if (!root.getCauses().isEmpty()) {
            root.getCauses().forEach(cause -> {
                final FactorDto causeDto = dtos.get(cause.getUri());
                links.addCause(new Link(causeDto, rootDto));
            });
        }
        if (!root.getMitigatingFactors().isEmpty()) {
            root.getMitigatingFactors().forEach(mitigation -> {
                final FactorDto mitigationDto = dtos.get(mitigation.getUri());
                links.addMitigates(new Link(mitigationDto, rootDto));
            });
        }
        root.getChildren().forEach(child -> addLinks(child, links, dtos));
    }

    public InvestigationReport investigationReportDtoToInvestigationReport(InvestigationReportDto dto) {
        if (dto == null) {
            return null;
        }
        final InvestigationReport report = new InvestigationReport();
        report.setUri(dto.getUri());
        report.setKey(dto.getKey());
        report.setOccurrence(dto.getOccurrence());
        report.setAuthor(dto.getAuthor());
        report.setCreated(dto.getCreated());
        report.setLastEdited(dto.getLastEdited());
        report.setLastEditedBy(dto.getLastEditedBy());
        report.setRevision(dto.getRevision());
        report.setSummary(dto.getSummary());
        report.setCorrectiveMeasures(dto.getCorrectiveMeasures());
        report.setInitialReports(dto.getInitialReports());
        report.setSeverityAssessment(dto.getSeverityAssessment());
        report.setRootFactor(factorDtoToFactor(dto.getRootFactor()));
        resolveLinks(dto, report);
        return report;
    }

    private void resolveLinks(InvestigationReportDto dto, InvestigationReport report) {
        if (report.getRootFactor() == null || report.getRootFactor().getChildren().isEmpty() ||
                dto.getLinks() == null) {
            return;
        }
        final Map<Integer, Factor> factors = new HashMap<>();
        identifyFactors(report.getRootFactor(), factors);
        for (Link cause : dto.getLinks().getCauses()) {
            final Factor target = factors.get(cause.getTo().getReferenceId());
            target.addCause(factors.get(cause.getFrom().getReferenceId()));
        }
        for (Link mitigation : dto.getLinks().getMitigates()) {
            final Factor target = factors.get(mitigation.getTo().getReferenceId());
            target.addMitigatingFactor(factors.get(mitigation.getFrom().getReferenceId()));
        }
    }

    private void identifyFactors(Factor factor, Map<Integer, Factor> factorMap) {
        factorMap.put(factor.getReferenceId(), factor);
        for (Factor child : factor.getChildren()) {
            identifyFactors(child, factorMap);
        }
    }

    public FactorDto factorToFactorDto(Factor factor) {
        final FactorDto dto = new FactorDto();
        dto.setUri(factor.getUri());
        dto.setAssessment(eventTypeAssessmentToEventTypeAssessmentDto(factor.getAssessment()));
        dto.setStartTime(factor.getStartTime());
        dto.setEndTime(factor.getEndTime());
        if (!factor.getChildren().isEmpty()) {
            dto.setChildren(new HashSet<>(factor.getChildren().size()));
            factor.getChildren().forEach(child -> dto.getChildren().add(factorToFactorDto(child)));
        }
        dto.setReferenceId(random.nextInt());
        return dto;
    }

    public abstract Factor factorDtoToFactor(FactorDto dto);

    public EventTypeAssessmentDto eventTypeAssessmentToEventTypeAssessmentDto(EventTypeAssessment assessment) {
        if (assessment == null) {
            return null;
        }
        final EventTypeAssessmentDto result;
        if (assessment.getRunwayIncursion() != null) {
            result = runwayIncursionToRunwayIncursionDto(assessment.getRunwayIncursion());
            result.setUri(assessment.getUri());
            result.setEventType(assessment.getEventType());
        } else {
            result = new GeneralEventDto(assessment);
        }
        return result;
    }

    public EventTypeAssessment eventTypeAssessmentDtoToEventTypeAssessment(EventTypeAssessmentDto dto) {
        if (dto instanceof GeneralEventDto) {
            return generalEventDtoToEventTypeAssessment((GeneralEventDto) dto);
        } else if (dto instanceof RunwayIncursionDto) {
            final EventTypeAssessment eta = new EventTypeAssessment();
            eta.setUri(dto.getUri());
            eta.setRunwayIncursion(runwayIncursionDtoToRunwayIncursion((RunwayIncursionDto) dto));
            eta.setEventType(dto.getEventType());
            return eta;
        }
        return null;
    }

    public abstract EventTypeAssessment generalEventDtoToEventTypeAssessment(GeneralEventDto dto);

    @Mappings({
            @Mapping(target = "lvp", source = "lowVisibilityProcedure"),
            @Mapping(target = "incursionUri", source = "uri")
    })
    public abstract RunwayIncursionDto runwayIncursionToRunwayIncursionDto(RunwayIncursion incursion);

    @InheritInverseConfiguration
    public abstract RunwayIncursion runwayIncursionDtoToRunwayIncursion(RunwayIncursionDto dto);

    public RunwayIntruderDto intruderToIntruderDto(Intruder intruder) {
        if (intruder.getAircraft() != null) {
            return aircraftToAircraftIntruder(intruder.getAircraft());
        } else if (intruder.getVehicle() != null) {
            return vehicleToVehicleIntruder(intruder.getVehicle());
        } else if (intruder.getPerson() != null) {
            return personIntruderToPersonIntruderDto(intruder.getPerson());
        }
        return null;
    }

    public Intruder IntruderDtoToIntruder(RunwayIntruderDto dto) {
        final Intruder intruder = new Intruder();
        if (dto == null || dto.getIntruderType() == null) {
            return null;
        }
        switch (dto.getIntruderType()) {
            case Aircraft.INTRUDER_TYPE:
                intruder.setAircraft(aircraftIntruderToAircraft((AircraftIntruderDto) dto));
                break;
            case Vehicle.INTRUDER_TYPE:
                intruder.setVehicle(vehicleIntruderToVehicle((VehicleIntruderDto) dto));
                break;
            case PersonIntruder.INTRUDER_TYPE:
                intruder.setPerson(personIntruderDtoToPersonIntruder((PersonIntruderDto) dto));
                break;
            default:
                break;
        }
        return intruder;
    }

    @Mapping(target = "intruderType", constant = Aircraft.INTRUDER_TYPE)
    public abstract AircraftIntruderDto aircraftToAircraftIntruder(Aircraft aircraft);

    public abstract Aircraft aircraftIntruderToAircraft(AircraftIntruderDto intruder);

    @Mappings({@Mapping(target = "intruderType", constant = PersonIntruder.INTRUDER_TYPE),
            @Mapping(target = "wasDoing", source = "whatWasDoing"),
            @Mapping(target = "organization", source = "organization.name")})
    public abstract PersonIntruderDto personIntruderToPersonIntruderDto(
            cz.cvut.kbss.inbas.audit.model.reports.incursions.PersonIntruder personIntruder);

    @Mapping(target = "whatWasDoing", source = "wasDoing")
    public abstract PersonIntruder personIntruderDtoToPersonIntruder(
            PersonIntruderDto intruder);

    @Mappings({@Mapping(target = "intruderType", constant = Vehicle.INTRUDER_TYPE),
            @Mapping(target = "wasDoing", source = "whatWasDoing"),
            @Mapping(target = "organization", source = "organization.name")})
    public abstract VehicleIntruderDto vehicleToVehicleIntruder(Vehicle vehicle);

    @Mapping(target = "whatWasDoing", source = "wasDoing")
    public abstract Vehicle vehicleIntruderToVehicle(VehicleIntruderDto intruder);
}
