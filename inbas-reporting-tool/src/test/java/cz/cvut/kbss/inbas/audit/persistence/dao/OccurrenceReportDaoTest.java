package cz.cvut.kbss.inbas.audit.persistence.dao;

import cz.cvut.kbss.inbas.audit.environment.util.Generator;
import cz.cvut.kbss.inbas.audit.model.Occurrence;
import cz.cvut.kbss.inbas.audit.model.Person;
import cz.cvut.kbss.inbas.audit.model.reports.InvestigationReport;
import cz.cvut.kbss.inbas.audit.model.reports.OccurrenceReport;
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
    public void findAllReturnsReportsOrderedByOccurrenceStartTimeAndRevision() throws Exception {
        final List<PreliminaryReport> reports = generateReports();

        final List<OccurrenceReport> result = occurrenceReportDao.findAll();
        assertEquals(reports.size(), result.size());
        int i = 0;
        for (PreliminaryReport pr : reports) {
            final OccurrenceReport or = result.get(i++);
            assertEquals(pr.getUri(), or.getUri());
            assertEquals(pr.getRevision(), or.getRevision());
        }
    }

    private List<PreliminaryReport> generateReports() {
        final List<PreliminaryReport> reports = new ArrayList<>();
        final Occurrence o1 = new Occurrence();
        o1.setName("SomeOccurrence");
        o1.setStartTime(new Date(System.currentTimeMillis() - 5000));
        o1.setEndTime(new Date());
        final PreliminaryReport r1 = new PreliminaryReport();
        r1.setAuthor(author);
        r1.setRevision(Constants.INITIAL_REVISION + 1);
        r1.setOccurrence(o1);
        reports.add(r1);
        final PreliminaryReport r2 = new PreliminaryReport();
        r2.setAuthor(author);
        r2.setRevision(Constants.INITIAL_REVISION);
        r2.setOccurrence(o1);
        reports.add(r2);
        final Occurrence o2 = new Occurrence();
        o2.setName("OccurrenceTwo");
        o2.setStartTime(new Date(System.currentTimeMillis() - 12000));
        o2.setEndTime(new Date());
        final PreliminaryReport r3 = new PreliminaryReport();
        r3.setOccurrence(o2);
        r3.setAuthor(author);
        r3.setRevision(Constants.INITIAL_REVISION + 1);
        reports.add(r3);
        final PreliminaryReport r4 = new PreliminaryReport();
        r4.setOccurrence(o2);
        r4.setAuthor(author);
        reports.add(r4);
        // The set messes the order a little, to verify our ordering implementation
        preliminaryReportDao.persist(new HashSet<>(reports));
        return reports;
    }

    @Test
    public void findAllByTypeReturnsReportsOrderedByOccurrenceTimeAndRevision() throws Exception {
        final List<PreliminaryReport> reports = generateReports();

        final List<OccurrenceReport> result = occurrenceReportDao.findAll(Vocabulary.PreliminaryReport);
        assertEquals(reports.size(), result.size());
        int i = 0;
        for (PreliminaryReport pr : reports) {
            final OccurrenceReport or = result.get(i++);
            assertEquals(pr.getUri(), or.getUri());
            assertEquals(pr.getRevision(), or.getRevision());
        }
    }
}
