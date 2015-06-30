package cz.cvut.kbss.inbas.audit.rest.dto.factory;

import cz.cvut.kbss.inbas.audit.model.*;
import cz.cvut.kbss.inbas.audit.rest.dto.model.EventReportDto;
import cz.cvut.kbss.inbas.audit.rest.dto.model.IntruderDto;
import cz.cvut.kbss.inbas.audit.rest.dto.model.RunwayIncursionDto;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ledvima1
 * TODO Tohle je strasna prasarna, predelat!!!
 */
@Service
public class DtoFactoryImpl implements DtoFactory {

    @Override
    public EventReport toDomainModel(EventReportDto dto) {
        final EventReport report = new EventReport();
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
    public EventTypeAssessment toDomainModel(RunwayIncursionDto dto) {
        final EventTypeAssessment typeAssessment = new EventTypeAssessment();
        final EventType et = new EventType();
        typeAssessment.setEventType(dto.getEventType());
        final RunwayIncursion ri = new RunwayIncursion();
        typeAssessment.setRunwayIncursion(ri);
        ri.setLowVisibilityProcedure(dto.getLvp());
        ri.setClearedAircraft(dto.getClearedAircraft());
        final Intruder intruder = new Intruder();
        ri.setIntruder(intruder);
        switch (dto.getIntruder().getIntruderType()) {
            case "aircraft":
                intruder.setAircraft(getAircraft(dto.getIntruder()));
                break;
            case "vehicle":
                intruder.setVehicle(getVehicle(dto.getIntruder()));
            case "person":
                intruder.setPerson(getPersonIntruder(dto.getIntruder()));
        }
        return typeAssessment;
    }

    private Aircraft getAircraft(IntruderDto intruder) {
        final Aircraft aircraftIntruder = new Aircraft();
        aircraftIntruder.setCallSign(intruder.getCallSign());
        aircraftIntruder.setFlightNumber(intruder.getFlightNumber());
        aircraftIntruder.setFlightPhase(intruder.getFlightPhase());
        aircraftIntruder.setLastDeparturePoint(intruder.getLastDeparturePoint());
        aircraftIntruder.setPlannedDestination(intruder.getPlannedDestination());
        aircraftIntruder.setOperationType(intruder.getOperationType());
        if (intruder.getOrganization() != null) {
            final Organization org = new Organization();
            org.setName(intruder.getOrganization());
            aircraftIntruder.setOperator(org);
        }
        aircraftIntruder.setRegistration(intruder.getRegistration());
        aircraftIntruder.setStateOfRegistry(intruder.getStateOfRegistry());
        return aircraftIntruder;
    }

    private Vehicle getVehicle(IntruderDto intruder) {
        final Vehicle vehicleIntruder = new Vehicle();
        vehicleIntruder.setVehicleType(intruder.getType());
        vehicleIntruder.setCallSign(intruder.getCallSign());
        vehicleIntruder.setIsAtsUnit(intruder.getIsAtsUnit());
        vehicleIntruder.setHasRadio(intruder.getHasRadio());
        vehicleIntruder.setWhatWasDoing(intruder.getWasDoing());
        vehicleIntruder.setOrganization(new Organization(intruder.getOrganization()));
        return vehicleIntruder;
    }

    private PersonIntruder getPersonIntruder(IntruderDto intruder) {
        final PersonIntruder personIntruder = new PersonIntruder();
        personIntruder.setCategory(intruder.getPersonCategory());
        personIntruder.setOrganization(new Organization(intruder.getOrganization()));
        personIntruder.setWhatWasDoing(intruder.getWasDoing());
        return personIntruder;
    }

    @Override
    public EventReportDto toDto(EventReport report) {
        final EventReportDto dto = new EventReportDto();
        dto.setUri(report.getUri());
        dto.setKey(report.getKey());
        dto.setName(report.getName());
        dto.setCreated(report.getCreated());
        dto.setEventTime(report.getEventTime());
        dto.setAuthor(report.getAuthor());
        dto.setLastEdited(report.getLastEdited());
        dto.setLastEditedBy(report.getLastEditedBy());
        dto.setDescription(report.getDescription());
        dto.setFactors(report.getFactors());
        dto.setResource(report.getResource());
        dto.setCorrectiveMeasures(report.getCorrectiveMeasures());
        dto.setSeverityAssessment(report.getSeverityAssessment());
        if (report.getTypeAssessments() != null) {
            dto.setTypeAssessments(new HashSet<>());
            for (EventTypeAssessment eta : report.getTypeAssessments()) {
                final RunwayIncursion incursion = eta.getRunwayIncursion();
                if (incursion == null) {
                    continue;
                }
                final RunwayIncursionDto rw = new RunwayIncursionDto();
                rw.setClearedAircraft(incursion.getClearedAircraft());
                rw.setEventType(eta.getEventType());
                rw.setLvp(incursion.getLowVisibilityProcedure());
                rw.setIntruder(toDto(incursion.getIntruder()));
                dto.getTypeAssessments().add(rw);
            }
        }
        return dto;
    }

    public IntruderDto toDto(Intruder intruder) {
        final IntruderDto dto = new IntruderDto();
        if (intruder.getAircraft() != null) {
            final Aircraft aircraft = intruder.getAircraft();
            dto.setRegistration(aircraft.getRegistration());
            dto.setStateOfRegistry(aircraft.getStateOfRegistry());
            dto.setCallSign(aircraft.getCallSign());
            dto.setOperator(aircraft.getOperator() != null ? aircraft.getOperator().getName() : null);
            dto.setFlightNumber(aircraft.getFlightNumber());
            dto.setFlightPhase(aircraft.getFlightPhase());
            dto.setOperationType(aircraft.getOperationType());
            dto.setLastDeparturePoint(aircraft.getLastDeparturePoint());
            dto.setPlannedDestination(aircraft.getPlannedDestination());
            dto.setIntruderType(Aircraft.INTRUDER_TYPE);
        } else if (intruder.getVehicle() != null) {
            dto.setType(intruder.getVehicle().getVehicleType());
            dto.setCallSign(intruder.getVehicle().getCallSign());
            dto.setIsAtsUnit(intruder.getVehicle().getIsAtsUnit());
            dto.setHasRadio(intruder.getVehicle().getHasRadio());
            dto.setOrganization(
                    intruder.getVehicle().getOrganization() != null ? intruder.getVehicle().getOrganization()
                                                                              .getName() : null);
            dto.setIntruderType(Vehicle.INTRUDER_TYPE);
        } else if (intruder.getPerson() != null) {
            dto.setPersonCategory(intruder.getPerson().getCategory());
            dto.setOrganization(intruder.getPerson().getOrganization() != null ? intruder.getPerson().getOrganization()
                                                                                         .getName() : null);
            dto.setWasDoing(intruder.getPerson().getWhatWasDoing());
            dto.setIntruderType(PersonIntruder.INTRUDER_TYPE);
        }
        return dto;
    }
}
