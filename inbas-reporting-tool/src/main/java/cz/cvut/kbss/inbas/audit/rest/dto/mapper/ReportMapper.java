package cz.cvut.kbss.inbas.audit.rest.dto.mapper;

import cz.cvut.kbss.inbas.audit.model.Aircraft;
import cz.cvut.kbss.inbas.audit.model.reports.incursions.Intruder;
import cz.cvut.kbss.inbas.audit.model.reports.incursions.Vehicle;
import cz.cvut.kbss.inbas.audit.rest.dto.model.EventTypeAssessment;
import cz.cvut.kbss.inbas.audit.rest.dto.model.GeneralEvent;
import cz.cvut.kbss.inbas.audit.rest.dto.model.OccurrenceReport;
import cz.cvut.kbss.inbas.audit.rest.dto.model.incursion.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * @author ledvima1
 */
@Mapper(componentModel = "spring", uses = {ReferenceMapper.class})
public abstract class ReportMapper {

    public abstract OccurrenceReport occurrenceReportToOccurrenceReportDto(
            cz.cvut.kbss.inbas.audit.model.reports.OccurrenceReport report);

    public EventTypeAssessment eventTypeAssessmentEventTypeAssessmentDto(
            cz.cvut.kbss.inbas.audit.model.reports.EventTypeAssessment assessment) {
        final EventTypeAssessment result;
        if (assessment.getRunwayIncursion() != null) {
            result = runwayIncursionToRunwayIncursionDto(assessment.getRunwayIncursion());
            result.setEventType(assessment.getEventType());
        } else {
            result = new GeneralEvent(assessment);
        }
        return result;
    }

    @Mapping(target = "intruderType", constant = Aircraft.INTRUDER_TYPE)
    public abstract AircraftIntruder aircraftToAircraftIntruder(Aircraft aircraft);

    public abstract Aircraft aircraftIntruderToAircraft(AircraftIntruder intruder);

    @Mappings({
            @Mapping(target = "intruderType", constant = cz.cvut.kbss.inbas.audit.model.reports.incursions.PersonIntruder.INTRUDER_TYPE),
            @Mapping(target = "wasDoing", source = "whatWasDoing"),
            @Mapping(target = "organization", source = "organization.name")
    })
    public abstract PersonIntruder personIntruderToPersonIntruderDto(
            cz.cvut.kbss.inbas.audit.model.reports.incursions.PersonIntruder personIntruder);

    @Mapping(target = "whatWasDoing", source = "wasDoing")
    public abstract cz.cvut.kbss.inbas.audit.model.reports.incursions.PersonIntruder personIntruderDtoToPersonIntruder(
            PersonIntruder intruder);

    @Mappings({
            @Mapping(target = "intruderType", constant = Vehicle.INTRUDER_TYPE),
            @Mapping(target = "wasDoing", source = "whatWasDoing"),
            @Mapping(target = "organization", source = "organization.name")
    })
    public abstract VehicleIntruder vehicleToVehicleIntruder(Vehicle vehicle);

    @Mappings({
            @Mapping(target = "lvp", source = "lowVisibilityProcedure")
    })
    public abstract RunwayIncursion runwayIncursionToRunwayIncursionDto(
            cz.cvut.kbss.inbas.audit.model.reports.incursions.RunwayIncursion incursion);

    public RunwayIntruder intruderToIntruderDto(Intruder intruder) {
        if (intruder.getAircraft() != null) {
            return aircraftToAircraftIntruder(intruder.getAircraft());
        } else if (intruder.getVehicle() != null) {
            return vehicleToVehicleIntruder(intruder.getVehicle());
        } else if (intruder.getPerson() != null) {
            return personIntruderToPersonIntruderDto(intruder.getPerson());
        }
        return null;
    }
    // TODO Dto to domain objects
}
