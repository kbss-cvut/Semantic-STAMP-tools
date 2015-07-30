package cz.cvut.kbss.inbas.audit.services.impl.validation;

import cz.cvut.kbss.inbas.audit.exceptions.InvalidReportException;
import cz.cvut.kbss.inbas.audit.model.OccurrenceReport;
import cz.cvut.kbss.inbas.audit.model.Person;
import cz.cvut.kbss.inbas.audit.services.BaseServiceTestRunner;
import cz.cvut.kbss.inbas.audit.services.validation.ReportValidator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.util.Date;

/**
 * @author ledvima1
 */
public class BaseReportValidatorTest extends BaseServiceTestRunner {

    private static final Person PERSON = initPerson();

    @Autowired
    private ReportValidator validator;

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
        final OccurrenceReport report = getDefaultValidReport();
        validator.validateReport(report);
    }

    private OccurrenceReport getDefaultValidReport() {
        final OccurrenceReport report = new OccurrenceReport();
        report.setAuthor(PERSON);
        report.setOccurrenceTime(new Date(System.currentTimeMillis() - 1000000));
        report.setDescription("Yadayadayada");
        return report;
    }

    @Test(expected = InvalidReportException.class)
    public void futureEventTimeThrowsException() throws Exception {
        final OccurrenceReport report = getDefaultValidReport();
        report.setOccurrenceTime(new Date(System.currentTimeMillis() + 10000));
        validator.validateReport(report);
    }
}