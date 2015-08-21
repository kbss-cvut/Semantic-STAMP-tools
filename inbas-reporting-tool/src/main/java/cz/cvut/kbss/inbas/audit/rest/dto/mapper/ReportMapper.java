package cz.cvut.kbss.inbas.audit.rest.dto.mapper;

import cz.cvut.kbss.inbas.audit.model.Aircraft;
import cz.cvut.kbss.inbas.audit.model.reports.incursions.Intruder;
import cz.cvut.kbss.inbas.audit.model.reports.incursions.Vehicle;
import cz.cvut.kbss.inbas.audit.rest.dto.model.EventTypeAssessment;
import cz.cvut.kbss.inbas.audit.rest.dto.model.GeneralEvent;
import cz.cvut.kbss.inbas.audit.rest.dto.model.OccurrenceReport;
import cz.cvut.kbss.inbas.audit.rest.dto.model.incursion.AircraftIntruder;
import cz.cvut.kbss.inbas.audit.rest.dto.model.incursion.PersonIntruder;
import cz.cvut.kbss.inbas.audit.rest.dto.model.incursion.RunwayIncursion;
import cz.cvut.kbss.inbas.audit.rest.dto.model.incursion.VehicleIntruder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * @author ledvima1
 */
@Mapper(componentModel = "spring")
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

    @Mappings({
            @Mapping(target = "intruderType", constant = cz.cvut.kbss.inbas.audit.model.reports.incursions.PersonIntruder.INTRUDER_TYPE),
            @Mapping(target = "wasDoing", source = "whatWasDoing"),
            @Mapping(target = "organization", source = "organization.name")
    })
    public abstract PersonIntruder personIntruderToPersonIntruderDto(
            cz.cvut.kbss.inbas.audit.model.reports.incursions.PersonIntruder personIntruder);

    @Mappings({
            @Mapping(target = "intruderType", constant = Vehicle.INTRUDER_TYPE),
            @Mapping(target = "wasDoing", source = "whatWasDoing"),
            @Mapping(target = "organization", source = "organization.name")
    })
    public abstract VehicleIntruder vehicleToVehicleIntruder(Vehicle vehicle);

    public RunwayIncursion runwayIncursionToRunwayIncursionDto(
            cz.cvut.kbss.inbas.audit.model.reports.incursions.RunwayIncursion incursion) {
        final RunwayIncursion result = new RunwayIncursion();
        result.setUri(incursion.getUri());
        result.setLvp(incursion.getLowVisibilityProcedure());
        result.setLocation(incursion.getLocation());
        result.setConflictingAircraft(incursion.getConflictingAircraft());
        if (incursion.getIntruder() != null) {
            final Intruder intruder = incursion.getIntruder();
            if (intruder.getAircraft() != null) {
                result.setIntruder(aircraftToAircraftIntruder(intruder.getAircraft()));
            } else if (intruder.getVehicle() != null) {
                result.setIntruder(vehicleToVehicleIntruder(intruder.getVehicle()));
            } else if (intruder.getPerson() != null) {
                result.setIntruder(personIntruderToPersonIntruderDto(intruder.getPerson()));
            }
        }
        return result;
    }

    // TODO Dto to domain objects
}
