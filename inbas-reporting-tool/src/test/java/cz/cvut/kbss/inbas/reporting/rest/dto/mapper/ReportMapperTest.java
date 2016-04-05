package cz.cvut.kbss.inbas.reporting.rest.dto.mapper;

import cz.cvut.kbss.inbas.reporting.config.RestConfig;
import cz.cvut.kbss.inbas.reporting.dto.CorrectiveMeasureRequestDto;
import cz.cvut.kbss.inbas.reporting.dto.agent.AgentDto;
import cz.cvut.kbss.inbas.reporting.dto.agent.OrganizationDto;
import cz.cvut.kbss.inbas.reporting.dto.agent.PersonDto;
import cz.cvut.kbss.inbas.reporting.dto.event.EventDto;
import cz.cvut.kbss.inbas.reporting.dto.event.OccurrenceDto;
import cz.cvut.kbss.inbas.reporting.environment.config.MockServiceConfig;
import cz.cvut.kbss.inbas.reporting.environment.config.MockSesamePersistence;
import cz.cvut.kbss.inbas.reporting.environment.util.Generator;
import cz.cvut.kbss.inbas.reporting.model_new.*;
import cz.cvut.kbss.inbas.reporting.model_new.util.HasUri;
import cz.cvut.kbss.inbas.reporting.util.IdentificationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RestConfig.class, MockServiceConfig.class, MockSesamePersistence.class})
public class ReportMapperTest {

    @Autowired
    private ReportMapper mapper;

    @Test
    public void correctiveMeasureRequestToDtoCopiesBasicAttributes() {
        final CorrectiveMeasureRequest req = generateCorrectiveMeasureRequestWithResponsibleAgents();
        final CorrectiveMeasureRequestDto dto = mapper.correctiveMeasureRequestToDto(req);
        assertNotNull(dto);
        assertEquals(req.getUri(), dto.getUri());
        assertEquals(req.getDescription(), dto.getDescription());
    }

    @Test
    public void correctiveMeasureRequestToDtoTransformsPersonsAndOrganizationsToPolymorphicAgentDtos() {
        final CorrectiveMeasureRequest req = generateCorrectiveMeasureRequestWithResponsibleAgents();
        final CorrectiveMeasureRequestDto dto = mapper.correctiveMeasureRequestToDto(req);
        assertNotNull(dto);
        assertEquals(req.getResponsibleOrganizations().size() + req.getResponsiblePersons().size(),
                dto.getResponsibleAgents().size());
        final Map<URI, HasUri> origAgents = new HashMap<>();
        req.getResponsiblePersons().forEach(p -> origAgents.put(p.getUri(), p));
        req.getResponsibleOrganizations().forEach(o -> origAgents.put(o.getUri(), o));
        for (AgentDto agent : dto.getResponsibleAgents()) {
            assertTrue(origAgents.containsKey(agent.getUri()));
            final HasUri orig = origAgents.get(agent.getUri());
            if (orig instanceof Person) {
                final Person p = (Person) orig;
                assertTrue(agent instanceof PersonDto);
                final PersonDto pDto = (PersonDto) agent;
                assertEquals(p.getFirstName(), pDto.getFirstName());
                assertEquals(p.getLastName(), pDto.getLastName());
                assertEquals(p.getUsername(), pDto.getUsername());
            } else {
                final Organization o = (Organization) orig;
                assertTrue(agent instanceof OrganizationDto);
                final OrganizationDto oDto = (OrganizationDto) agent;
                assertEquals(o.getName(), oDto.getName());
            }
        }
    }

    private CorrectiveMeasureRequest generateCorrectiveMeasureRequestWithResponsibleAgents() {
        final CorrectiveMeasureRequest request = generateCorrectiveMeasureRequest();
        final Set<Person> persons = new HashSet<>();
        for (int i = 0; i < Generator.randomInt(10); i++) {
            final Person p = new Person();
            p.setFirstName("FirstName" + i);
            p.setLastName("LastName" + i);
            p.setUsername("firstname-" + i + "@inbas.cz");
            p.setPassword("bflmpsvz");
            p.generateUri();
            persons.add(p);
        }
        request.setResponsiblePersons(persons);
        final Set<Organization> organizations = new HashSet<>();
        for (int i = 0; i < Generator.randomInt(10); i++) {
            final Organization org = new Organization();
            org.setName("Knowledge Based Software Systems Division " + i);
            org.generateUri();
            organizations.add(org);
        }
        request.setResponsibleOrganizations(organizations);
        return request;
    }

    private CorrectiveMeasureRequest generateCorrectiveMeasureRequest() {
        final CorrectiveMeasureRequest request = new CorrectiveMeasureRequest();
        request.setUri(URI.create("http://onto.fel.cvut.cz/ontologies/documentation/corrective_measure_request#req"));
        request.setDescription("Sample corrective measure.");
        return request;
    }

    @Test
    public void correctiveMeasureRequestToDtoTransformsEventToEventDto() {
        final CorrectiveMeasureRequest req = generateCorrectiveMeasureRequestBasedOnEvent();
        final CorrectiveMeasureRequestDto dto = mapper.correctiveMeasureRequestToDto(req);
        assertNotNull(dto);
        assertNotNull(dto.getBasedOn());
        final EventDto eventDto = dto.getBasedOn();
        assertEquals(req.getBasedOnEvent().getUri(), eventDto.getUri());
        assertEquals(req.getBasedOnEvent().getType(), eventDto.getType());
    }

    private CorrectiveMeasureRequest generateCorrectiveMeasureRequestBasedOnEvent() {
        final CorrectiveMeasureRequest req = generateCorrectiveMeasureRequest();
        final Event event = new Event();
        event.setUri(URI.create("http://onto.fel.cvut.cz/ontologies/ufo/Event#instance"));
        event.setType(Generator.generateEventType());
        req.setBasedOnEvent(event);
        return req;
    }

    @Test
    public void correctiveMeasureRequestToDtoTransformsOccurrenceToPolymorphicOccurrenceDto() {
        final CorrectiveMeasureRequest req = generateCorrectiveMeasureRequestBasedOnOccurrence();
        final CorrectiveMeasureRequestDto dto = mapper.correctiveMeasureRequestToDto(req);
        assertNotNull(dto);
        assertNotNull(dto.getBasedOn());
        assertTrue(dto.getBasedOn() instanceof OccurrenceDto);
        final OccurrenceDto occurrenceDto = (OccurrenceDto) dto.getBasedOn();
        assertEquals(req.getBasedOnOccurrence().getUri(), occurrenceDto.getUri());
        assertEquals(req.getBasedOnOccurrence().getName(), occurrenceDto.getName());
        assertEquals(req.getBasedOnOccurrence().getKey(), occurrenceDto.getKey());
        assertEquals(req.getBasedOnOccurrence().getType(), occurrenceDto.getType());
    }

    private CorrectiveMeasureRequest generateCorrectiveMeasureRequestBasedOnOccurrence() {
        final CorrectiveMeasureRequest req = generateCorrectiveMeasureRequest();
        final Occurrence occurrence = Generator.generateOccurrence();
        occurrence.setKey(IdentificationUtils.generateKey());
        occurrence.setUri(URI.create("http://onto.fel.cvut.cz/ontologies/documentation/occurrence#instance"));
        req.setBasedOnOccurrence(occurrence);
        return req;
    }
}