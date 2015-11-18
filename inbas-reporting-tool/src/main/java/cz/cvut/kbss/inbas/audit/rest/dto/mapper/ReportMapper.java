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

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class})
public abstract class ReportMapper {

    public OccurrenceReportInfo occurrenceReportToOccurrenceReportInfo(PreliminaryReport report) {
        return new OccurrenceReportInfo(report);
    }

    public abstract PreliminaryReportDto occurrenceReportToOccurrenceReportDto(PreliminaryReport report);

    public abstract PreliminaryReport occurrenceReportDtoToOccurrenceReport(PreliminaryReportDto dto);

    public abstract InvestigationReportDto investigationReportToInvestigationReportDto(InvestigationReport report);

    public abstract InvestigationReport investigationReportDtoToInvestigationReport(InvestigationReportDto dto);

    public abstract FactorDto factorToFactorDto(Factor factor);

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
