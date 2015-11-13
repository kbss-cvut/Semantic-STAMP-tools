package cz.cvut.kbss.inbas.audit.service;

import cz.cvut.kbss.inbas.audit.environment.util.Environment;
import cz.cvut.kbss.inbas.audit.environment.util.Generator;
import cz.cvut.kbss.inbas.audit.model.Person;
import cz.cvut.kbss.inbas.audit.model.reports.*;
import cz.cvut.kbss.inbas.audit.model.reports.incursions.Intruder;
import cz.cvut.kbss.inbas.audit.model.reports.incursions.RunwayIncursion;
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

    @Test
    public void testCreateInvestigationFromPreliminaryReportWithEventTypeAssessments() throws Exception {
        final PreliminaryReport report = Generator
                .generatePreliminaryReport(Generator.ReportType.WITH_TYPE_ASSESSMENTS);
        preliminaryReportDao.persist(report);

        final InvestigationReport result = service.createFromPreliminaryReport(report);
        assertNotNull(result);
        assertNotNull(result.getRootFactor());
        assertEquals(report.getTypeAssessments().size(), result.getRootFactor().getChildren().size());
        for (EventTypeAssessment eta : report.getTypeAssessments()) {
            boolean found = false;
            for (Factor f : result.getRootFactor().getChildren()) {
                assertNotNull(f.getAssessment());
                final EventTypeAssessment fAssessment = f.getAssessment();
                assertNotEquals(eta.getUri(), fAssessment.getUri());
                if (!eta.getEventType().getId().equals(fAssessment.getEventType().getId())) {
                    continue;
                }
                found = true;
                if (eta.getDescription() != null) {
                    assertEquals(eta.getDescription(), fAssessment.getDescription());
                } else {
                    verifyIncursion(eta.getRunwayIncursion(), fAssessment.getRunwayIncursion());
                }
            }
            assertTrue(found);
        }
    }

    private void verifyIncursion(RunwayIncursion expected, RunwayIncursion actual) {
        assertEquals(expected.getLocation().getUri(), actual.getLocation().getUri());
        assertEquals(expected.getLowVisibilityProcedure(), actual.getLowVisibilityProcedure());
        assertNotEquals(expected.getUri(), actual.getUri());
        assertNotEquals(expected.getIntruder().getUri(), actual.getIntruder().getUri());
        final Intruder expIntruder = expected.getIntruder();
        final Intruder actIntruder = actual.getIntruder();
        // The intruder instances should be newly created
        if (expIntruder.getAircraft() != null) {
            assertNotNull(actIntruder.getAircraft());
            assertNotEquals(expIntruder.getAircraft().getUri(), actIntruder.getAircraft().getUri());
            assertEquals(expIntruder.getAircraft().getAircraft().getUri(),
                    actIntruder.getAircraft().getAircraft().getUri());
        } else if (expIntruder.getVehicle() != null) {
            assertNotNull(actIntruder.getVehicle());
            assertNotEquals(expIntruder.getVehicle().getUri(), actIntruder.getVehicle().getUri());
        } else {
            assertNotNull(actIntruder.getPerson());
            assertNotEquals(expIntruder.getPerson().getUri(), actIntruder.getPerson().getUri());
        }
    }
}
