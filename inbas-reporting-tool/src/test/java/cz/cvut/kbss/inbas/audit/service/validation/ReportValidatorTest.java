package cz.cvut.kbss.inbas.audit.service.validation;

import cz.cvut.kbss.inbas.audit.environment.util.Generator;
import cz.cvut.kbss.inbas.audit.exception.ValidationException;
import cz.cvut.kbss.inbas.audit.model.Occurrence;
import cz.cvut.kbss.inbas.audit.model.Person;
import cz.cvut.kbss.inbas.audit.model.reports.*;
import cz.cvut.kbss.inbas.audit.service.BaseServiceTestRunner;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.util.Collections;
import java.util.Date;

public class ReportValidatorTest extends BaseServiceTestRunner {

    private static final Person PERSON = initPerson();

    @Autowired
    private Validator<ValidatableReport> validator;

    private static Person initPerson() {
        final Person p = new Person();
        p.setUsername("halse@unsc.org");
        p.setFirstName("Catherine");
        p.setLastName("Halsey");
        p.setUri(URI.create("http://krizik.felk.cvut.cz/ontologies/inbas2015#CatherineHalsey"));
        return p;
    }

    @Test
    public void testValidateValid() {
        final PreliminaryReport report = getDefaultValidReport();
        validator.validate(report);
    }

    private PreliminaryReport getDefaultValidReport() {
        final PreliminaryReport report = new PreliminaryReport();
        report.setAuthor(PERSON);
        final Occurrence occurrence = new Occurrence();
        occurrence.setName("TestOccurrence");
        report.setOccurrence(occurrence);
        report.setOccurrenceStart(new Date(System.currentTimeMillis() - 1000000));
        report.setOccurrenceEnd(new Date());
        report.setSummary("Yadayadayada");
        report.setSeverityAssessment(OccurrenceSeverity.INCIDENT);
        report.setOccurrenceCategory(Generator.getEventType());
        final EventTypeAssessment typeAssessment = new EventTypeAssessment();
        typeAssessment.setDescription("Event type assessment.");
        typeAssessment.setEventType(new EventType(URI.create(
                "http://onto.fel.cvut.cz/ontologies/eccairs-1.3.0.8/V-24-1-31-31-14-390-2000000-2200000-2200100"),
                "2200100 - Runway incursions"));
        report.setTypeAssessments(Collections.singleton(typeAssessment));
        return report;
    }

    @Test(expected = ValidationException.class)
    public void futureOccurrenceTimeThrowsException() throws Exception {
        final PreliminaryReport report = getDefaultValidReport();
        report.setOccurrenceStart(new Date(System.currentTimeMillis() + 10000));
        validator.validate(report);
    }

    @Test(expected = ValidationException.class)
    public void reportWithoutOccurrenceIsInvalid() throws Exception {
        final PreliminaryReport report = getDefaultValidReport();
        report.setOccurrence(null);
        validator.validate(report);
    }

    @Test(expected = ValidationException.class)
    public void reportWithoutSummaryIsInvalid() throws Exception {
        final PreliminaryReport report = getDefaultValidReport();
        report.setSummary("");
        validator.validate(report);
    }

    @Test(expected = ValidationException.class)
    public void reportWithoutOccurrenceClassIsInvalid() throws Exception {
        final PreliminaryReport report = getDefaultValidReport();
        report.setSeverityAssessment(null);
        validator.validate(report);
    }

    @Test(expected = ValidationException.class)
    public void reportWithoutOccurrenceCategoryIsInvalid() throws Exception {
        final PreliminaryReport report = getDefaultValidReport();
        report.setOccurrenceCategory(null);
        validator.validate(report);
    }
}