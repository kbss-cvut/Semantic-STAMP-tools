package cz.cvut.kbss.inbas.audit.service.validation;

import cz.cvut.kbss.inbas.audit.exception.InvalidReportException;
import cz.cvut.kbss.inbas.audit.model.Person;
import cz.cvut.kbss.inbas.audit.model.reports.OccurrenceReport;
import cz.cvut.kbss.inbas.audit.service.BaseServiceTestRunner;
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
        report.setSummary("Yadayadayada");
        return report;
    }

    @Test(expected = InvalidReportException.class)
    public void futureOccurrenceTimeThrowsException() throws Exception {
        final OccurrenceReport report = getDefaultValidReport();
        report.setOccurrenceTime(new Date(System.currentTimeMillis() + 10000));
        validator.validateReport(report);
    }
}