package cz.cvut.kbss.inbas.audit.service;

import cz.cvut.kbss.inbas.audit.environment.util.Generator;
import cz.cvut.kbss.inbas.audit.model.reports.InvestigationReport;
import cz.cvut.kbss.inbas.audit.model.reports.OccurrenceReport;
import cz.cvut.kbss.inbas.audit.model.reports.PreliminaryReport;
import cz.cvut.kbss.inbas.audit.model.reports.Report;
import cz.cvut.kbss.inbas.audit.persistence.dao.InvestigationReportDao;
import cz.cvut.kbss.inbas.audit.persistence.dao.OccurrenceDao;
import cz.cvut.kbss.inbas.audit.persistence.dao.PreliminaryReportDao;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.util.*;

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
    public void findAllWithNullTypeReturnsSameAsFindAllWithoutArgs() {
        final PreliminaryReport pr = persistPreliminaryReport();
        final InvestigationReport investigation = persistInvestigation();
        final Set<URI> uris = new HashSet<>(Arrays.asList(pr.getUri(), investigation.getUri()));

        final List<OccurrenceReport> allByType = reportService.findAll(null);
        final List<OccurrenceReport> all = reportService.findAll();
        assertEquals(uris.size(), allByType.size());
        assertEquals(uris.size(), all.size());
        allByType.forEach(report -> assertTrue(uris.contains(report.getUri())));
        all.forEach(report -> assertTrue(uris.contains(report.getUri())));
    }

    @Test
    public void removeReportChainRemovesAllReportsInChain() {
        final List<Report> chain = persistReportChain();
        reportService.removeReportChain(chain.get(0).getFileNumber());

        for (Report r : chain) {
            assertNull(reportService.find(r.getUri()));
        }
    }

    private List<Report> persistReportChain() {
        final List<Report> reports = new ArrayList<>();
        PreliminaryReport prevRevision = Generator
                .generatePreliminaryReport(Generator.ReportType.WITH_TYPE_ASSESSMENTS);
        prevRevision.setCreated(new Date());
        preliminaryReportDao.persist(prevRevision);
        final int cnt = Generator.randomInt(5);
        final List<PreliminaryReport> toPersist = new ArrayList<>(cnt);
        for (int i = 0; i < cnt; i++) {
            reports.add(prevRevision);
            final PreliminaryReport nextRevision = new PreliminaryReport(prevRevision);
            nextRevision.setRevision(prevRevision.getRevision() + 1);
            nextRevision.setAuthor(person);
            nextRevision.setCreated(new Date());
            toPersist.add(nextRevision);
            prevRevision = nextRevision;
        }
        preliminaryReportDao.persist(toPersist);
        InvestigationReport prevInvestigation = new InvestigationReport(toPersist.get(cnt - 1));
        prevInvestigation.setCreated(new Date());
        prevInvestigation.setAuthor(person);
        investigationDao.persist(prevInvestigation);
        final List<InvestigationReport> toPersistInv = new ArrayList<>(cnt);
        for (int i = 0; i < cnt; i++) {
            reports.add(prevInvestigation);
            final InvestigationReport nextInvestigation = new InvestigationReport(prevInvestigation);
            nextInvestigation.setRevision(prevInvestigation.getRevision() + 1);
            nextInvestigation.setAuthor(person);
            nextInvestigation.setCreated(new Date());
            toPersistInv.add(nextInvestigation);
            prevInvestigation = nextInvestigation;
        }
        investigationDao.persist(toPersistInv);
        return reports;
    }
}