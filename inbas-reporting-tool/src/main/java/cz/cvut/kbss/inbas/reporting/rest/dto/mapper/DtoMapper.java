package cz.cvut.kbss.inbas.reporting.rest.dto.mapper;

import cz.cvut.kbss.inbas.reporting.dto.CorrectiveMeasureRequestDto;
import cz.cvut.kbss.inbas.reporting.dto.OccurrenceReportDto;
import cz.cvut.kbss.inbas.reporting.dto.agent.AgentDto;
import cz.cvut.kbss.inbas.reporting.dto.agent.OrganizationDto;
import cz.cvut.kbss.inbas.reporting.dto.agent.PersonDto;
import cz.cvut.kbss.inbas.reporting.dto.event.EventDto;
import cz.cvut.kbss.inbas.reporting.dto.event.FactorDto;
import cz.cvut.kbss.inbas.reporting.dto.event.OccurrenceDto;
import cz.cvut.kbss.inbas.reporting.model_new.*;
import org.mapstruct.Mapper;

import java.util.HashSet;
import java.util.Set;
import java.util.SplittableRandom;

@Mapper(componentModel = "spring", uses = {ReferenceMapper.class})
public abstract class DtoMapper {

    private final SplittableRandom random = new SplittableRandom();

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

    public abstract FactorDto factorToFactorDto(Factor factor);

    public abstract Factor factorDtoToFactor(FactorDto dto);
}
