package cz.cvut.kbss.inbas.audit.service;

import cz.cvut.kbss.inbas.audit.environment.util.Generator;
import cz.cvut.kbss.inbas.audit.model.reports.InvestigationReport;
import cz.cvut.kbss.inbas.audit.model.reports.PreliminaryReport;
import cz.cvut.kbss.inbas.audit.model.reports.Report;
import cz.cvut.kbss.inbas.audit.persistence.dao.InvestigationReportDao;
import cz.cvut.kbss.inbas.audit.persistence.dao.OccurrenceDao;
import cz.cvut.kbss.inbas.audit.persistence.dao.PreliminaryReportDao;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class MainReportServiceTest extends BaseServiceTestRunner {

    @Autowired
    private PreliminaryReportDao preliminaryReportDao;
    @Autowired
    private InvestigationReportDao investigationDao;
    @Autowired
    private OccurrenceDao occurrenceDao;

    @Autowired
    private ReportService reportService;

    @Test
    public void testFindPreliminaryReport() {
        final PreliminaryReport pr = persistPreliminaryReport();

        final Report result = reportService.find(pr.getUri());
        assertNotNull(result);
        assertEquals(pr.getUri(), result.getUri());
        assertTrue(result instanceof PreliminaryReport);
    }

    private PreliminaryReport persistPreliminaryReport() {
        final PreliminaryReport pr = Generator.generatePreliminaryReport(Generator.ReportType.WITH_TYPE_ASSESSMENTS);
        preliminaryReportDao.persist(pr);
        return pr;
    }

    @Test
    public void testFindInvestigation() {
        final InvestigationReport investigation = persistInvestigation();

        final Report result = reportService.find(investigation.getUri());
        assertNotNull(result);
        assertEquals(investigation.getUri(), result.getUri());
        assertTrue(result instanceof InvestigationReport);
    }

    private InvestigationReport persistInvestigation() {
        final InvestigationReport investigation = Generator.generateMinimalInvestigation();
        occurrenceDao.persist(investigation.getOccurrence());
        investigationDao.persist(investigation);
        return investigation;
    }

    @Test
    public void testFindPreliminaryReportByKey() {
        final PreliminaryReport pr = persistPreliminaryReport();

        final Report result = reportService.findByKey(pr.getKey());
        assertNotNull(result);
        assertEquals(pr.getUri(), result.getUri());
        assertTrue(result instanceof PreliminaryReport);
    }

    @Test
    public void testFindInvestigationByKey() {
        final InvestigationReport investigation = persistInvestigation();

        final Report result = reportService.findByKey(investigation.getKey());
        assertNotNull(result);
        assertEquals(investigation.getUri(), result.getUri());
        assertTrue(result instanceof InvestigationReport);
    }

    @Test
    public void testRemovePreliminaryReport() {
        final PreliminaryReport pr = persistPreliminaryReport();

        reportService.remove(pr);
        assertNull(preliminaryReportDao.find(pr.getUri()));
    }

    @Test
    public void testRemoveInvestigation() {
        final InvestigationReport investigation = persistInvestigation();

        reportService.remove(investigation);
        assertNull(investigationDao.find(investigation.getUri()));
    }
}