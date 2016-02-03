package cz.cvut.kbss.inbas.audit.persistence.dao;

import cz.cvut.kbss.inbas.audit.dto.ReportRevisionInfo;
import cz.cvut.kbss.inbas.audit.environment.util.Generator;
import cz.cvut.kbss.inbas.audit.model.Occurrence;
import cz.cvut.kbss.inbas.audit.model.Person;
import cz.cvut.kbss.inbas.audit.model.reports.*;
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
    @Autowired
    private OccurrenceDao occurrenceDao;

    private Person author;

    @Before
    public void setUp() throws Exception {
        this.author = Generator.getPerson();
        super.persistPerson(author);
    }

    @Test
    public void findAllSearchesAllReportTypes() throws Exception {
        final PreliminaryReport pr = persistPreliminaryWithRevisions(null);
        final InvestigationReport ir = persistInvestigationReport(null);
        final Set<URI> uris = new HashSet<>(Arrays.asList(pr.getUri(), ir.getUri()));

        final List<OccurrenceReport> result = occurrenceReportDao.findAll();
        assertEquals(uris.size(), result.size());
        result.forEach(report -> assertTrue(uris.contains(report.getUri())));
    }

    private PreliminaryReport persistPreliminaryWithRevisions(Occurrence occurrence) {
        final PreliminaryReport rOne = Generator
                .generatePreliminaryReport(Generator.ReportType.WITHOUT_TYPE_ASSESSMENTS);
        rOne.setFileNumber(System.currentTimeMillis());
        if (occurrence != null) {
            rOne.setOccurrence(occurrence);
        }
        final PreliminaryReport rTwo = new PreliminaryReport(rOne);
        rTwo.setAuthor(author);
        rTwo.setRevision(rOne.getRevision() + 3);
        preliminaryReportDao.persist(Arrays.asList(rOne, rTwo));
        return rTwo;
    }

    private InvestigationReport persistInvestigationReport(Occurrence occurrence) throws Exception {
        final PreliminaryReport pr = Generator.generatePreliminaryReport(Generator.ReportType.WITHOUT_TYPE_ASSESSMENTS);
        pr.setFileNumber(System.currentTimeMillis());
        pr.setAuthor(author);
        if (occurrence != null) {
            pr.setOccurrence(occurrence);
        }
        preliminaryReportDao.persist(pr);

        final InvestigationReport ir = new InvestigationReport(pr);
        ir.setAuthor(author);
        ir.setRevision(pr.getRevision() + 1);
        investigationReportDao.persist(ir);
        return ir;
    }

    @Test
    public void findAllGetsReportsOrderedByOccurrenceStartDescending() {
        final PreliminaryReport prOne = persistPreliminaryWithRevisions(null);
        final PreliminaryReport prTwo = Generator.generatePreliminaryReport(Generator.ReportType.WITH_TYPE_ASSESSMENTS);
        prTwo.setOccurrenceStart(new Date(System.currentTimeMillis() + 10000));
        preliminaryReportDao.persist(prTwo);

        final List<OccurrenceReport> result = occurrenceReportDao.findAll();
        assertEquals(2, result.size());
        assertEquals(prTwo.getUri(), result.get(0).getUri());
        assertEquals(prOne.getUri(), result.get(1).getUri());
    }

    @Test
    public void findAllGetsLatestRevisionsForEveryReportChain() {
        final PreliminaryReport prOne = persistPreliminaryWithRevisions(null);
        final PreliminaryReport prTwo = persistPreliminaryWithRevisions(null);
        final InvestigationReport ir = new InvestigationReport(prOne);
        ir.setOccurrenceStart(new Date(System.currentTimeMillis() + 10000));
        ir.setAuthor(author);
        investigationReportDao.persist(ir);

        final List<OccurrenceReport> result = occurrenceReportDao.findAll();
        assertEquals(2, result.size());
        assertEquals(ir.getUri(), result.get(0).getUri());
        assertEquals(prTwo.getUri(), result.get(1).getUri());
    }

    @Test
    public void findSearchesBothInPreliminaryAndInvestigationReports() throws Exception {
        final PreliminaryReport pr = persistPreliminaryWithRevisions(null);
        final InvestigationReport ir = persistInvestigationReport(null);

        final OccurrenceReport resultInvestigation = occurrenceReportDao.find(ir.getUri());
        assertNotNull(resultInvestigation);
        assertEquals(ir.getOccurrence().getUri(), resultInvestigation.getOccurrence().getUri());
        final OccurrenceReport resultPreliminary = occurrenceReportDao.find(pr.getUri());
        assertNotNull(resultPreliminary);
    }

    @Test
    public void findAllByTypeReturnsReportsOfSpecifiedType() throws Exception {
        persistPreliminaryWithRevisions(null);
        final InvestigationReport ir = persistInvestigationReport(null);

        final List<OccurrenceReport> result = occurrenceReportDao.findAll(Vocabulary.InvestigationReport);
        assertEquals(1, result.size());
        assertEquals(ir.getUri(), result.get(0).getUri());
    }

    @Test
    public void findAllByTypeReturnsLatestRevisionsForEveryReportChain() throws Exception {
        final PreliminaryReport chainOne = persistPreliminaryWithRevisions(null);
        final PreliminaryReport chainTwo = persistPreliminaryWithRevisions(null);
        final InvestigationReport ir = new InvestigationReport(chainTwo);
        ir.setAuthor(author);
        investigationReportDao.persist(ir);
        final Set<URI> uris = new HashSet<>(Arrays.asList(chainOne.getUri(), chainTwo.getUri()));

        // Investigation has higher revision number, but does not match the type filter
        final List<OccurrenceReport> result = occurrenceReportDao.findAll(Vocabulary.PreliminaryReport);
        assertEquals(2, result.size());
        result.forEach(report -> assertTrue(uris.contains(report.getUri())));
    }

    private List<PreliminaryReport> generateReports(boolean lastRevisionsOnly) {
        Date startTime = new Date(System.currentTimeMillis() - 5000);
        final Date endTime = new Date();
        final List<PreliminaryReport> reports = new ArrayList<>();
        final Occurrence o1 = new Occurrence();
        o1.setName("SomeOccurrence");
        occurrenceDao.persist(o1);
        final PreliminaryReport r1 = new PreliminaryReport();
        r1.setAuthor(author);
        r1.setRevision(Constants.INITIAL_REVISION);
        r1.setFileNumber(System.currentTimeMillis());
        r1.setOccurrenceStart(startTime);
        r1.setOccurrenceEnd(endTime);
        r1.setOccurrence(o1);
        r1.setSeverityAssessment(OccurrenceSeverity.OCCURRENCE_WITHOUT_SAFETY_EFFECT);
        reports.add(r1);
        final PreliminaryReport r2 = new PreliminaryReport();
        r2.setAuthor(author);
        r2.setRevision(Constants.INITIAL_REVISION + 1);
        r2.setFileNumber(r1.getFileNumber());
        r2.setOccurrenceStart(startTime);
        r2.setOccurrenceEnd(endTime);
        r2.setOccurrence(o1);
        r2.setSeverityAssessment(OccurrenceSeverity.OCCURRENCE_WITHOUT_SAFETY_EFFECT);
        reports.add(r2);
        startTime = new Date(startTime.getTime() - 7000);
        final PreliminaryReport r3 = new PreliminaryReport();
        r3.setOccurrence(o1);
        r3.setAuthor(author);
        r3.setOccurrenceStart(startTime);
        r3.setOccurrenceEnd(endTime);
        r3.setRevision(Constants.INITIAL_REVISION);
        r3.setFileNumber(System.currentTimeMillis() + 1000L);
        r3.setSeverityAssessment(OccurrenceSeverity.OCCURRENCE_WITHOUT_SAFETY_EFFECT);
        reports.add(r3);
        final PreliminaryReport r4 = new PreliminaryReport();
        r4.setOccurrence(o1);
        r4.setAuthor(author);
        r4.setOccurrenceStart(startTime);
        r4.setOccurrenceEnd(endTime);
        r4.setRevision(Constants.INITIAL_REVISION + 5);
        r4.setFileNumber(r3.getFileNumber());
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
    public void findAllByTypeReturnsOnlyLatestRevisionsForEveryReportChain() throws Exception {
        final List<PreliminaryReport> reports = generateReports(true);

        final List<OccurrenceReport> result = occurrenceReportDao.findAll(Vocabulary.PreliminaryReport);
        assertEquals(reports.size(), result.size());
        for (int i = 0; i < reports.size(); i++) {
            assertEquals(reports.get(i).getUri(), result.get(i).getUri());
        }
    }

    @Test
    public void findByOccurrenceReturnsLatestRevisionsOfMatchingReportChains() throws Exception {
        final Occurrence occurrence = Generator.generateOccurrence();
        persistPreliminaryWithRevisions(null);
        final PreliminaryReport chainOne = persistPreliminaryWithRevisions(occurrence);
        final InvestigationReport chainTwo = persistInvestigationReport(occurrence);
        final Set<URI> uris = new HashSet<>(Arrays.asList(chainOne.getUri(), chainTwo.getUri()));

        final List<OccurrenceReport> result = occurrenceReportDao.findByOccurrence(occurrence);
        assertEquals(2, result.size());
        result.forEach(report -> {
            assertTrue(uris.contains(report.getUri()));
            assertEquals(occurrence.getUri(), report.getOccurrence().getUri());
        });
    }

    @Test
    public void getReportChainRevisionsReturnsRevisionsForAllTypesOfReportsInChain() throws Exception {
        final PreliminaryReport rOne = Generator
                .generatePreliminaryReport(Generator.ReportType.WITHOUT_TYPE_ASSESSMENTS);
        rOne.setCreated(new Date());
        preliminaryReportDao.persist(rOne);
        final PreliminaryReport rTwo = new PreliminaryReport(rOne);
        rTwo.setAuthor(author);
        rTwo.setRevision(rOne.getRevision() + 1);
        rTwo.setCreated(new Date());
        preliminaryReportDao.persist(rTwo);
        final InvestigationReport rThree = new InvestigationReport(rTwo);
        rThree.setAuthor(author);
        rThree.setCreated(new Date());
        investigationReportDao.persist(rThree);
        final InvestigationReport rFour = new InvestigationReport(rThree);
        rFour.setAuthor(author);
        rFour.setRevision(rThree.getRevision() + 1);
        rFour.setCreated(new Date());
        investigationReportDao.persist(rFour);
        final List<Report> reports = Arrays.asList(rFour, rThree, rTwo, rOne);

        final List<ReportRevisionInfo> result = occurrenceReportDao.getReportChainRevisions(rOne.getFileNumber());
        assertEquals(reports.size(), result.size());
        for (int i = 0; i < reports.size(); i++) {
            assertEquals(reports.get(i).getUri(), result.get(i).getUri());
            assertEquals(reports.get(i).getPhase(), result.get(i).getPhase());
        }
    }

    @Test
    public void getReportChainRevisionsSkipsReturnsOnlyKnownPhases() throws Exception {
        final PreliminaryReport rOne = Generator
                .generatePreliminaryReport(Generator.ReportType.WITHOUT_TYPE_ASSESSMENTS);
        rOne.setCreated(new Date());
        rOne.addType("http://www.w3.org/2000/01/rdf-schema#Resource");  // Inferred by RDFS rule engines
        preliminaryReportDao.persist(rOne);

        final List<ReportRevisionInfo> result = occurrenceReportDao.getReportChainRevisions(rOne.getFileNumber());
        assertEquals(1, result.size());
        assertEquals(rOne.getUri(), result.get(0).getUri());
        assertEquals(rOne.getKey(), result.get(0).getKey());
    }
}
