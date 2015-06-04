package cz.cvut.kbss.inbas.audit.services.impl.validation;

import cz.cvut.kbss.inbas.audit.exceptions.InvalidReportException;
import cz.cvut.kbss.inbas.audit.model.EventReport;
import cz.cvut.kbss.inbas.audit.model.Person;
import cz.cvut.kbss.inbas.audit.services.validation.ReportValidator;
import org.junit.Test;

import java.net.URI;
import java.util.Date;

/**
 * @author ledvima1
 */
public class BaseReportValidatorTest {

    private static final Person PERSON = initPerson();

    private ReportValidator validator = new BaseReportValidator();

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
        final EventReport report = getDefaultValidReport();
        validator.validateReport(report);
    }

    private EventReport getDefaultValidReport() {
        final EventReport report = new EventReport();
        report.setAuthor(PERSON);
        report.setEventTime(new Date(System.currentTimeMillis() - 1000000));
        report.setDescription("Yadayadayada");
        return report;
    }

    @Test(expected = InvalidReportException.class)
    public void futureEventTimeThrowsException() throws Exception {
        final EventReport report = getDefaultValidReport();
        report.setEventTime(new Date(System.currentTimeMillis() + 10000));
        validator.validateReport(report);
    }
}