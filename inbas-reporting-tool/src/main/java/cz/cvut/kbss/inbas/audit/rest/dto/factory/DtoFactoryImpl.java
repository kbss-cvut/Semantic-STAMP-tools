package cz.cvut.kbss.inbas.audit.rest.dto.factory;

import cz.cvut.kbss.inbas.audit.model.*;
import cz.cvut.kbss.inbas.audit.rest.dto.model.EventReport;
import cz.cvut.kbss.inbas.audit.rest.dto.model.incursion.AircraftIntruder;
import cz.cvut.kbss.inbas.audit.rest.dto.model.incursion.VehicleIntruder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ledvima1 TODO Tohle je strasna prasarna, predelat!!!
 */
@Service
public class DtoFactoryImpl implements DtoFactory {

    @Override
    public cz.cvut.kbss.inbas.audit.model.EventReport toDomainModel(EventReport dto) {
        final cz.cvut.kbss.inbas.audit.model.EventReport report = new cz.cvut.kbss.inbas.audit.model.EventReport();
        report.setUri(dto.getUri());
        report.setKey(dto.getKey());
        report.setLastEdited(dto.getLastEdited());
        report.setLastEditedBy(dto.getLastEditedBy());
        report.setCreated(dto.getCreated());
        report.setDescription(dto.getDescription());
        report.setEventTime(dto.getEventTime());
        report.setAuthor(dto.getAuthor());
        report.setCorrectiveMeasures(dto.getCorrectiveMeasures());
        report.setFactors(dto.getFactors());
        report.setName(dto.getName());
        report.setResource(dto.getResource());
        report.setSeverityAssessment(dto.getSeverityAssessment());
        if (dto.getTypeAssessments() != null) {
            final Set<EventTypeAssessment> assessments = new HashSet<>(dto.getTypeAssessments().size());
            assessments.addAll(dto.getTypeAssessments().stream().map(this::toDomainModel).collect(Collectors.toList()));
            report.setTypeAssessments(assessments);
        }
        return report;
    }

    @Override
    public EventTypeAssessment toDomainModel(cz.cvut.kbss.inbas.audit.rest.dto.model.incursion.RunwayIncursion dto) {
        final EventTypeAssessment typeAssessment = new EventTypeAssessment();
        typeAssessment.setEventType(dto.getEventType());
        final cz.cvut.kbss.inbas.audit.model.RunwayIncursion ri = new cz.cvut.kbss.inbas.audit.model.RunwayIncursion();
        typeAssessment.setRunwayIncursion(ri);
        ri.setLowVisibilityProcedure(dto.getLvp());
        ri.setClearedAircraft(dto.getClearedAircraft());
        final cz.cvut.kbss.inbas.audit.model.Intruder intruder = new cz.cvut.kbss.inbas.audit.model.Intruder();
        ri.setIntruder(intruder);
        switch (dto.getIntruder().getIntruderType()) {
            case "aircraft":
                intruder.setAircraft(getAircraft((AircraftIntruder) dto.getIntruder()));
                break;
            case "vehicle":
                intruder.setVehicle(getVehicle((VehicleIntruder) dto.getIntruder()));
                break;
            case "person":
                intruder.setPerson(getPersonIntruder(
                        (cz.cvut.kbss.inbas.audit.rest.dto.model.incursion.PersonIntruder) dto.getIntruder()));
                break;
        }
        return typeAssessment;
    }

    private Aircraft getAircraft(AircraftIntruder intruder) {
        final Aircraft aircraftIntruder = new Aircraft();
        aircraftIntruder.setCallSign(intruder.getCallSign());
        aircraftIntruder.setFlightNumber(intruder.getFlightNumber());
        aircraftIntruder.setFlightPhase(intruder.getFlightPhase());
        aircraftIntruder.setLastDeparturePoint(intruder.getLastDeparturePoint());
        aircraftIntruder.setPlannedDestination(intruder.getPlannedDestination());
        aircraftIntruder.setOperationType(intruder.getOperationType());
        aircraftIntruder.setRegistration(intruder.getRegistration());
        aircraftIntruder.setStateOfRegistry(intruder.getStateOfRegistry());
        return aircraftIntruder;
    }

    private Vehicle getVehicle(VehicleIntruder intruder) {
        final Vehicle vehicleIntruder = new Vehicle();
        vehicleIntruder.setVehicleType(intruder.getVehicleType());
        vehicleIntruder.setCallSign(intruder.getCallSign());
        vehicleIntruder.setIsAtsUnit(intruder.getIsAtsUnit());
        vehicleIntruder.setHasRadio(intruder.getHasRadio());
        vehicleIntruder.setWhatWasDoing(intruder.getWasDoing());
        vehicleIntruder.setOrganization(new Organization(intruder.getOrganization()));
        return vehicleIntruder;
    }

    private PersonIntruder getPersonIntruder(
            cz.cvut.kbss.inbas.audit.rest.dto.model.incursion.PersonIntruder intruder) {
        final PersonIntruder personIntruder = new PersonIntruder();
        personIntruder.setCategory(intruder.getCategory());
        personIntruder.setOrganization(new Organization(intruder.getOrganization()));
        personIntruder.setWhatWasDoing(intruder.getWasDoing());
        return personIntruder;
    }

    @Override
    public EventReport toDto(cz.cvut.kbss.inbas.audit.model.EventReport report) {
        return new EventReport(report);
    }
}
