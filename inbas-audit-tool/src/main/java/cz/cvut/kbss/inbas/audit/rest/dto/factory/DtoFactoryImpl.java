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
        et.setName(dto.getEventType());
        typeAssessment.setEventType(et);
        final RunwayIncursion ri = new RunwayIncursion();
        typeAssessment.setRunwayIncursion(ri);
        ri.setLowVisibilityProcedure(dto.getLvp());
        ri.setClearedAircraft(dto.getClearedAircraft());
        final Intruder intruder = new Intruder();
        ri.setIntruder(intruder);
        if (dto.getIntruder().getIntruderType().equals("aircraft")) {
            final Aircraft aircraftIntruder = new Aircraft();
            aircraftIntruder.setCallSign(dto.getIntruder().getCallSign());
            aircraftIntruder.setFlightNumber(dto.getIntruder().getFlightNumber());
            aircraftIntruder.setFlightPhase(dto.getIntruder().getFlightPhase());
            aircraftIntruder.setLastDeparturePoint(dto.getIntruder().getLastDeparturePoint());
            aircraftIntruder.setPlannedDestination(dto.getIntruder().getPlannedDestination());
            aircraftIntruder.setOperationType(dto.getIntruder().getOperationType());
            if (dto.getIntruder().getOrganization() != null) {
                final Organization org = new Organization();
                org.setName(dto.getIntruder().getOrganization());
                aircraftIntruder.setOperator(org);
            }
            aircraftIntruder.setRegistration(dto.getIntruder().getRegistration());
            aircraftIntruder.setStateOfRegistry(dto.getIntruder().getStateOfRegistry());
            intruder.setAircraft(aircraftIntruder);
        }
        return typeAssessment;
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
                rw.setEventType(eta.getEventType().getName());
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
        }
        return dto;
    }
}
