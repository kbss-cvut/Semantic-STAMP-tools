package cz.cvut.kbss.inbas.audit.service.validation;

import cz.cvut.kbss.inbas.audit.exception.ValidationException;
import cz.cvut.kbss.inbas.audit.model.Occurrence;
import cz.cvut.kbss.inbas.audit.model.Person;
import cz.cvut.kbss.inbas.audit.model.reports.PreliminaryReport;
import cz.cvut.kbss.inbas.audit.service.BaseServiceTestRunner;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.util.Date;

public class BaseReportValidatorTest extends BaseServiceTestRunner {

    private static final Person PERSON = initPerson();

    @Autowired
    private Validator<PreliminaryReport> validator;

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
        occurrence.setStartTime(new Date(System.currentTimeMillis() - 1000000));
        occurrence.setEndTime(new Date());
        report.setOccurrence(occurrence);
        report.setSummary("Yadayadayada");
        return report;
    }

    @Test(expected = ValidationException.class)
    public void futureOccurrenceTimeThrowsException() throws Exception {
        final PreliminaryReport report = getDefaultValidReport();
        report.getOccurrence().setStartTime(new Date(System.currentTimeMillis() + 10000));
        validator.validate(report);
    }

    @Test(expected = ValidationException.class)
    public void reportWithoutOccurrenceIsInvalid() throws Exception {
        final PreliminaryReport report = getDefaultValidReport();
        report.setOccurrence(null);
        validator.validate(report);
    }
}