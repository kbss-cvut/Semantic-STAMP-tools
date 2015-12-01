package cz.cvut.kbss.inbas.audit.persistence.dao;

import cz.cvut.kbss.inbas.audit.environment.util.Generator;
import cz.cvut.kbss.inbas.audit.model.Occurrence;
import cz.cvut.kbss.inbas.audit.model.Person;
import cz.cvut.kbss.inbas.audit.model.reports.InvestigationReport;
import cz.cvut.kbss.inbas.audit.model.reports.OccurrenceReport;
import cz.cvut.kbss.inbas.audit.model.reports.PreliminaryReport;
import cz.cvut.kbss.inbas.audit.persistence.BaseDaoTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
}
