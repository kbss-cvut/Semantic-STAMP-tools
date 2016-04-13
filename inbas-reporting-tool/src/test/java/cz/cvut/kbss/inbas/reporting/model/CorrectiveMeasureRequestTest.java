package cz.cvut.kbss.inbas.reporting.model;

import cz.cvut.kbss.inbas.reporting.environment.util.Generator;
import org.junit.Test;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class CorrectiveMeasureRequestTest {

    @Test
    public void copyConstructorReusesResponsiblePersonsAndOrganizations() {
        final CorrectiveMeasureRequest original = new CorrectiveMeasureRequest();
        original.setDescription("Blablabla corrective measure request");
        original.setResponsiblePersons(generateResponsiblePersons());
        original.setResponsibleOrganizations(generateResponsibleOrganizations());

        final CorrectiveMeasureRequest copy = new CorrectiveMeasureRequest(original);
        assertEquals(original.getDescription(), copy.getDescription());
        assertEquals(original.getResponsiblePersons(), copy.getResponsiblePersons());
        assertEquals(original.getResponsibleOrganizations(), copy.getResponsibleOrganizations());
    }

    private Set<Person> generateResponsiblePersons() {
        final Set<Person> persons = new HashSet<>();
        for (int i = 0; i < Generator.randomInt(10); i++) {
            final Person p = new Person();
            p.setFirstName("firstName" + i);
            p.setLastName("lastName" + i);
            p.setUsername("username" + i);
            persons.add(p);
        }
        return persons;
    }

    private Set<Organization> generateResponsibleOrganizations() {
        final Set<Organization> organizations = new HashSet<>();
        for (int i = 0; i < Generator.randomInt(10); i++) {
            organizations.add(new Organization("Organization-part-" + i));
        }
        return organizations;
    }

    @Test
    public void copyConstructorReusesRelatedOccurrence() {
        final CorrectiveMeasureRequest original = new CorrectiveMeasureRequest();
        original.setDescription("blabla");
        final Occurrence occurrence = new Occurrence();
        occurrence.setName("Runway incursion");
        occurrence.setKey("12345");
        original.setBasedOnOccurrence(occurrence);

        final CorrectiveMeasureRequest copy = new CorrectiveMeasureRequest(original);
        assertEquals(original.getDescription(), copy.getDescription());
        assertEquals(original.getBasedOnOccurrence(), copy.getBasedOnOccurrence());
    }

    @Test
    public void copyConstructorReusesRelatedEvent() {
        final CorrectiveMeasureRequest original = new CorrectiveMeasureRequest();
        original.setDescription("blabla");
        final EventType et = new EventType();
        et.setName("2200101 - Runway Incursion by an Aircraft");
        et.setUri(URI.create("http://onto.fel.cvut.cz/ontologies/eccairs-3.4.0.2/vl-a-390/v-2200101"));
        final Event event = new Event();
        event.setType(et);
        original.setBasedOnEvent(event);

        final CorrectiveMeasureRequest copy = new CorrectiveMeasureRequest(original);
        assertEquals(original.getDescription(), copy.getDescription());
        assertEquals(original.getBasedOnEvent(), copy.getBasedOnEvent());
    }
}