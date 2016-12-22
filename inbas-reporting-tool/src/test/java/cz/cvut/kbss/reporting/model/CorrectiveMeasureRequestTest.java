package cz.cvut.kbss.reporting.model;

import cz.cvut.kbss.reporting.environment.generator.Generator;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CorrectiveMeasureRequestTest {

    @Test
    public void copyConstructorCopiesBasicFields() {
        final CorrectiveMeasureRequest measure = Generator.generateCorrectiveMeasureRequests().iterator().next();
        final CorrectiveMeasureRequest copy = new CorrectiveMeasureRequest(measure);
        assertEquals(measure.getDescription(), copy.getDescription());
        assertEquals(measure.getDeadline(), copy.getDeadline());
        assertEquals(measure.getPhase(), copy.getPhase());
        assertEquals(measure.isImplemented(), copy.isImplemented());
    }

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
    public void copyConstructorCopiesEvaluation() {
        final CorrectiveMeasureRequest original = new CorrectiveMeasureRequest();
        final CorrectiveMeasureImplementationEvaluation evaluation = new CorrectiveMeasureImplementationEvaluation();
        evaluation.setDescription("Evaluation is ok");
        evaluation.setTypes(Collections.singleton(Generator.generateUri().toString()));
        original.setEvaluation(evaluation);

        final CorrectiveMeasureRequest copy = new CorrectiveMeasureRequest(original);
        assertNotNull(copy.getEvaluation());
        assertEquals(evaluation.getDescription(), copy.getEvaluation().getDescription());
        assertEquals(evaluation.getTypes(), copy.getEvaluation().getTypes());
    }
}