package cz.cvut.kbss.inbas.audit.service;

import cz.cvut.kbss.inbas.audit.environment.util.Generator;
import cz.cvut.kbss.inbas.audit.model.Occurrence;
import cz.cvut.kbss.inbas.audit.model.Person;
import cz.cvut.kbss.inbas.audit.model.reports.InvestigationReport;
import cz.cvut.kbss.inbas.audit.model.reports.OccurrenceReport;
import cz.cvut.kbss.inbas.audit.model.reports.PreliminaryReport;
import cz.cvut.kbss.inbas.audit.persistence.dao.InvestigationReportDao;
import cz.cvut.kbss.inbas.audit.persistence.dao.OccurrenceDao;
import cz.cvut.kbss.inbas.audit.persistence.dao.PersonDao;
import cz.cvut.kbss.inbas.audit.persistence.dao.PreliminaryReportDao;
import cz.cvut.kbss.inbas.audit.service.repository.RepositoryOccurrenceReportService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

import static org.junit.Assert.*;

public class RepositoryOccurrenceReportServiceTest extends BaseServiceTestRunner {

    @Autowired
    private RepositoryOccurrenceReportService occurrenceReportService;

    @Autowired
    private PreliminaryReportDao preliminaryReportDao;
    @Autowired
    private InvestigationReportDao investigationReportDao;
    @Autowired
    private PersonDao personDao;
    @Autowired
    private OccurrenceDao occurrenceDao;

    private Person author;


    @Before
    public void setUp() {
        this.author = Generator.getPerson();
        personDao.persist(author);
    }

    @Test
    public void removeOccurrenceReportRemovesUnderlyingPreliminaryReport() throws Exception {
        final PreliminaryReport pr = persistPreliminaryReport();

        final OccurrenceReport toRemove = occurrenceReportService.find(pr.getUri());
        assertNotNull(toRemove);
        occurrenceReportService.remove(toRemove);

        assertNull(occurrenceReportService.find(pr.getUri()));
        assertFalse(preliminaryReportDao.exists(pr.getUri()));
    }

    private PreliminaryReport persistPreliminaryReport() {
        final PreliminaryReport pr = Generator.generatePreliminaryReport(Generator.ReportType.WITHOUT_TYPE_ASSESSMENTS);
        pr.setAuthor(author);
        preliminaryReportDao.persist(pr);
        return pr;
    }

    @Test
    public void removeOccurrenceReportRemovesUnderlyingInvestigationReport() throws Exception {
        final InvestigationReport ir = persistInvestigationReport();

        final OccurrenceReport toRemove = occurrenceReportService.find(ir.getUri());
        assertNotNull(toRemove);
        occurrenceReportService.remove(toRemove);

        assertNull(occurrenceReportService.find(ir.getUri()));
        assertFalse(investigationReportDao.exists(ir.getUri()));
    }

    private InvestigationReport persistInvestigationReport() throws Exception {
        final InvestigationReport ir = Generator.generateInvestigationWithCausesAndMitigatingFactors();
        final Occurrence occurrence = Generator.generateOccurrence();
        occurrenceDao.persist(occurrence);
        ir.setAuthor(author);
        ir.setOccurrence(occurrence);
        investigationReportDao.persist(ir);
        return ir;
    }

    @Test
    public void removeCollectionRemovesReportsOfAppropriateTypes() throws Exception {
        persistPreliminaryReport();
        persistInvestigationReport();

        final Collection<OccurrenceReport> toRemove = occurrenceReportService.findAll();
        assertFalse(toRemove.isEmpty());
        occurrenceReportService.remove(toRemove);
        final Collection<OccurrenceReport> result = occurrenceReportService.findAll();
        assertTrue(result.isEmpty());
    }
}
