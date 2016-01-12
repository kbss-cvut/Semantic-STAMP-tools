package cz.cvut.kbss.inbas.audit.persistence.dao;

import cz.cvut.kbss.inbas.audit.environment.util.Generator;
import cz.cvut.kbss.inbas.audit.model.Occurrence;
import cz.cvut.kbss.inbas.audit.model.Person;
import cz.cvut.kbss.inbas.audit.model.reports.InvestigationReport;
import cz.cvut.kbss.inbas.audit.model.reports.OccurrenceReport;
import cz.cvut.kbss.inbas.audit.model.reports.OccurrenceSeverity;
import cz.cvut.kbss.inbas.audit.model.reports.PreliminaryReport;
import cz.cvut.kbss.inbas.audit.persistence.BaseDaoTestRunner;
import cz.cvut.kbss.inbas.audit.util.Constants;
import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.util.*;

import static org.junit.Assert.*;

public class OccurrenceReportDaoTest extends BaseDaoTestRunner {

    @Autowired
    private OccurrenceReportDao occurrenceReportDao;


    @Autowired
    private PreliminaryReportDao preliminaryReportDao;
    @Autowired
    private InvestigationReportDao investigationReportDao;

    private Person author;
    private Occurrence occurrence;

    @Before
    public void setUp() throws Exception {
        this.author = Generator.getPerson();
        super.persistPerson(author);
        this.occurrence = Generator.generateOccurrence();
    }

    @Test
    public void findAllGetsBothPreliminaryReportsAndInvestigationReports() throws Exception {
        final PreliminaryReport pr = persistPreliminaryReport();
        final InvestigationReport ir = persistInvestigationReport();
        final Set<URI> uris = new HashSet<>(Arrays.asList(pr.getUri(), ir.getUri()));

        final List<OccurrenceReport> result = occurrenceReportDao.findAll();
        assertEquals(uris.size(), result.size());
        result.forEach(report -> assertTrue(uris.contains(report.getUri())));
    }

    private InvestigationReport persistInvestigationReport() throws Exception {
        final InvestigationReport ir = Generator.generateInvestigationWithCausesAndMitigatingFactors();
        ir.setOccurrence(occurrence);
        ir.setAuthor(author);
        investigationReportDao.persist(ir);
        return ir;
    }

    private PreliminaryReport persistPreliminaryReport() {
        final PreliminaryReport pr = Generator.generatePreliminaryReport(Generator.ReportType.WITHOUT_TYPE_ASSESSMENTS);
        pr.setOccurrence(occurrence);
        pr.setAuthor(author);
        pr.setSeverityAssessment(OccurrenceSeverity.OCCURRENCE_WITHOUT_SAFETY_EFFECT);
        preliminaryReportDao.persist(pr);
        return pr;
    }

    @Test
    public void findSearchesBothInPreliminaryAndInvestigationReports() throws Exception {
        final PreliminaryReport pr = persistPreliminaryReport();
        final InvestigationReport ir = persistInvestigationReport();

        final OccurrenceReport resultInvestigation = occurrenceReportDao.find(ir.getUri());
        assertNotNull(resultInvestigation);
        assertEquals(ir.getOccurrence().getUri(), resultInvestigation.getOccurrence().getUri());
        final OccurrenceReport resultPreliminary = occurrenceReportDao.find(pr.getUri());
        assertNotNull(resultPreliminary);
    }

    @Test
    public void findAllByTypeReturnsReportsOfSpecifiedType() throws Exception {
        persistPreliminaryReport();
        final InvestigationReport ir = persistInvestigationReport();

        final List<OccurrenceReport> result = occurrenceReportDao.findAll(Vocabulary.InvestigationReport);
        assertEquals(1, result.size());
        assertEquals(ir.getUri(), result.get(0).getUri());
    }

    @Test
    public void findAllReturnsReportsOrderedByOccurrenceStartTime() throws Exception {
        generateReports(false);

        final List<OccurrenceReport> result = occurrenceReportDao.findAll();
        assertFalse(result.isEmpty());
        Date previous = result.get(0).getOccurrenceStart();
        for (OccurrenceReport report : result) {
            assertTrue(report.getOccurrenceStart().compareTo(previous) <= 0);
            previous = report.getOccurrenceStart();
        }
    }

    private List<PreliminaryReport> generateReports(boolean lastRevisionsOnly) {
        Date startTime = new Date(System.currentTimeMillis() - 5000);
        final Date endTime = new Date();
        final List<PreliminaryReport> reports = new ArrayList<>();
        final Occurrence o1 = new Occurrence();
        o1.setName("SomeOccurrence");
        final PreliminaryReport r1 = new PreliminaryReport();
        r1.setAuthor(author);
        r1.setRevision(Constants.INITIAL_REVISION);
        r1.setOccurrenceStart(startTime);
        r1.setOccurrenceEnd(endTime);
        r1.setOccurrence(o1);
        r1.setSeverityAssessment(OccurrenceSeverity.OCCURRENCE_WITHOUT_SAFETY_EFFECT);
        reports.add(r1);
        final PreliminaryReport r2 = new PreliminaryReport();
        r2.setAuthor(author);
        r2.setRevision(Constants.INITIAL_REVISION + 1);
        r2.setOccurrenceStart(startTime);
        r2.setOccurrenceEnd(endTime);
        r2.setOccurrence(o1);
        r2.setSeverityAssessment(OccurrenceSeverity.OCCURRENCE_WITHOUT_SAFETY_EFFECT);
        reports.add(r2);
        final Occurrence o2 = new Occurrence();
        o2.setName("OccurrenceTwo");
        // The other occurrence happened before the first one
        startTime = new Date(startTime.getTime() - 7000);
        final PreliminaryReport r3 = new PreliminaryReport();
        r3.setOccurrence(o2);
        r3.setAuthor(author);
        r3.setOccurrenceStart(startTime);
        r3.setOccurrenceEnd(endTime);
        r3.setRevision(Constants.INITIAL_REVISION);
        r3.setSeverityAssessment(OccurrenceSeverity.OCCURRENCE_WITHOUT_SAFETY_EFFECT);
        reports.add(r3);
        final PreliminaryReport r4 = new PreliminaryReport();
        r4.setOccurrence(o2);
        r4.setAuthor(author);
        r4.setOccurrenceStart(startTime);
        r4.setOccurrenceEnd(endTime);
        r4.setRevision(Constants.INITIAL_REVISION + 5);
        r4.setSeverityAssessment(OccurrenceSeverity.OCCURRENCE_WITHOUT_SAFETY_EFFECT);
        reports.add(r4);
        // The set messes the order a little, to verify our ordering implementation
        preliminaryReportDao.persist(new HashSet<>(reports));
        if (lastRevisionsOnly) {
            // Remove reports with old revision number from results
            reports.remove(r1);
            reports.remove(r3);
        }
        return reports;
    }

    @Test
    public void findAllReturnsAllReportsOrderedByStartTimeAndRevision() throws Exception {
        final List<PreliminaryReport> reports = generateReports(false);

        final List<OccurrenceReport> result = occurrenceReportDao.findAll();
        assertEquals(reports.size(), result.size());
        for (int i = 1; i < reports.size(); i++) {
            final Date previousStart = result.get(i - 1).getOccurrenceStart();
            final Date currentStart = result.get(i).getOccurrenceStart();
            assertTrue(previousStart.compareTo(currentStart) >= 0);
            if (previousStart.equals(currentStart)) {
                assertTrue(result.get(i - 1).getRevision().compareTo(result.get(i).getRevision()) >= 0);
            }
        }
    }

    @Test
    public void findAllByTypeReturnsReportsOrderedByOccurrenceTime() throws Exception {
        generateReports(true);

        final List<OccurrenceReport> result = occurrenceReportDao.findAll(Vocabulary.PreliminaryReport);
        assertFalse(result.isEmpty());
        Date previous = result.get(0).getOccurrenceStart();
        for (OccurrenceReport report : result) {
            assertTrue(report.getOccurrenceStart().compareTo(previous) <= 0);
            previous = report.getOccurrenceStart();
        }
    }

    @Test
    public void findAllByTypeReturnsOnlyLatestRevisions() throws Exception {
        final List<PreliminaryReport> reports = generateReports(true);

        final List<OccurrenceReport> result = occurrenceReportDao.findAll(Vocabulary.PreliminaryReport);
        assertEquals(reports.size(), result.size());
        for (int i = 0; i < reports.size(); i++) {
            assertEquals(reports.get(i).getUri(), result.get(i).getUri());
        }
    }
}
