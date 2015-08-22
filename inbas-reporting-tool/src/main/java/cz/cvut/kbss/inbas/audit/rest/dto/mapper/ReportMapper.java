package cz.cvut.kbss.inbas.audit.rest.dto.mapper;

import cz.cvut.kbss.inbas.audit.model.Aircraft;
import cz.cvut.kbss.inbas.audit.model.reports.EventTypeAssessment;
import cz.cvut.kbss.inbas.audit.model.reports.OccurrenceReport;
import cz.cvut.kbss.inbas.audit.model.reports.incursions.Intruder;
import cz.cvut.kbss.inbas.audit.model.reports.incursions.PersonIntruder;
import cz.cvut.kbss.inbas.audit.model.reports.incursions.RunwayIncursion;
import cz.cvut.kbss.inbas.audit.model.reports.incursions.Vehicle;
import cz.cvut.kbss.inbas.audit.rest.dto.model.EventTypeAssessmentDto;
import cz.cvut.kbss.inbas.audit.rest.dto.model.GeneralEventDto;
import cz.cvut.kbss.inbas.audit.rest.dto.model.OccurrenceReportDto;
import cz.cvut.kbss.inbas.audit.rest.dto.model.incursion.*;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * @author ledvima1
 */
@Mapper(componentModel = "spring", uses = {ReferenceMapper.class})
public abstract class ReportMapper {

    public abstract OccurrenceReportDto occurrenceReportToOccurrenceReportDto(OccurrenceReport report);

    public abstract OccurrenceReport occurrenceReportDtoToOccurrenceReport(OccurrenceReportDto dto);

    public EventTypeAssessmentDto eventTypeAssessmentToEventTypeAssessmentDto(EventTypeAssessment assessment) {
        final EventTypeAssessmentDto result;
        if (assessment.getRunwayIncursion() != null) {
            result = runwayIncursionToRunwayIncursionDto(assessment.getRunwayIncursion());
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
            eta.setRunwayIncursion(runwayIncursionDtoToRunwayIncursion((RunwayIncursionDto) dto));
            eta.setEventType(dto.getEventType());
            return eta;
        }
        return null;
    }

    public abstract EventTypeAssessment generalEventDtoToEventTypeAssessment(GeneralEventDto dto);

    @Mapping(target = "lvp", source = "lowVisibilityProcedure")
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
