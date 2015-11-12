package cz.cvut.kbss.inbas.audit.service;

import cz.cvut.kbss.inbas.audit.environment.util.Environment;
import cz.cvut.kbss.inbas.audit.environment.util.Generator;
import cz.cvut.kbss.inbas.audit.model.Person;
import cz.cvut.kbss.inbas.audit.model.reports.CorrectiveMeasure;
import cz.cvut.kbss.inbas.audit.model.reports.InitialReport;
import cz.cvut.kbss.inbas.audit.model.reports.InvestigationReport;
import cz.cvut.kbss.inbas.audit.model.reports.PreliminaryReport;
import cz.cvut.kbss.inbas.audit.persistence.dao.PersonDao;
import cz.cvut.kbss.inbas.audit.persistence.dao.PreliminaryReportDao;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class InvestigationReportServiceTest extends BaseServiceTestRunner {

    @Autowired
    private PersonDao personDao;
    @Autowired
    private PreliminaryReportDao preliminaryReportDao;

    @Autowired
    private InvestigationReportService service;

    @Before
    public void setUp() throws Exception {
        final Person user = Generator.getPerson();
        if (personDao.findByUsername(user.getUsername()) == null) {
            personDao.persist(user);
        }
        Environment.setCurrentUser(user);
    }

    @Test
    public void testCreateInvestigationFromPreliminaryReportWithoutEventTypeAssessments() throws Exception {
        final PreliminaryReport report = Generator
                .generatePreliminaryReport(Generator.ReportType.WITHOUT_TYPE_ASSESSMENTS);
        preliminaryReportDao.persist(report);
        final InvestigationReport result = service.createFromPreliminaryReport(report);
        assertNotNull(result);
        assertEquals(report.getSeverityAssessment(), result.getSeverityAssessment());
        assertEquals(report.getCorrectiveMeasures().size(), result.getCorrectiveMeasures().size());
        boolean found;
        for (CorrectiveMeasure cm : report.getCorrectiveMeasures()) {
            found = false;
            for (CorrectiveMeasure cmActual : result.getCorrectiveMeasures()) {
                if (cm.getDescription().equals(cmActual.getDescription())) {
                    found = true;
                    break;
                }
            }
            assertTrue(found);
        }
        assertEquals(report.getInitialReports().size(), result.getInitialReports().size());
        for (InitialReport ir : report.getInitialReports()) {
            found = false;
            for (InitialReport irActual : result.getInitialReports()) {
                if (ir.getText().equals(irActual.getText())) {
                    found = true;
                    break;
                }
            }
            assertTrue(found);
        }
    }
}
