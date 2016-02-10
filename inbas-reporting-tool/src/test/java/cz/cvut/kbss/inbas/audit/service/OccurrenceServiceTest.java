package cz.cvut.kbss.inbas.audit.service;

import cz.cvut.kbss.inbas.audit.environment.util.Generator;
import cz.cvut.kbss.inbas.audit.model.Occurrence;
import cz.cvut.kbss.inbas.audit.model.ReportingPhase;
import cz.cvut.kbss.inbas.audit.model.reports.*;
import cz.cvut.kbss.inbas.audit.persistence.dao.InvestigationReportDao;
import cz.cvut.kbss.inbas.audit.persistence.dao.OccurrenceDao;
import cz.cvut.kbss.inbas.audit.persistence.dao.PreliminaryReportDao;
import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static org.junit.Assert.*;

public class OccurrenceServiceTest extends BaseServiceTestRunner {

    @Autowired
    private OccurrenceDao occurrenceDao;

    @Autowired
    private PreliminaryReportDao preliminaryReportDao;

    @Autowired
    private InvestigationReportDao investigationReportDao;

    @Autowired
    private OccurrenceService occurrenceService;

    private Occurrence occurrence;
    private Map<ReportingPhase, Set<? extends Report>> data;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        this.occurrence = Generator.generateOccurrence();
        occurrenceDao.persist(occurrence);
        this.data = persistTestData();
    }

    private Map<ReportingPhase, Set<? extends Report>> persistTestData() {
        final Map<ReportingPhase, Set<? extends Report>> data = new HashMap<>();
        final Set<PreliminaryReport> preliminaryReports = new HashSet<>();
        final Date startTime = new Date(System.currentTimeMillis() - 10000);
        final Date endTime = new Date();
        for (int i = 0; i < 5; i++) {
            final PreliminaryReport r = new PreliminaryReport();
            r.setOccurrence(occurrence);
            r.setOccurrenceCategory(Generator.getEventType());
            r.setRevision(2);
            r.setOccurrenceStart(startTime);
            r.setOccurrenceEnd(endTime);
            r.setFileNumber(System.currentTimeMillis());
            r.setAuthor(person);
            r.setSeverityAssessment(OccurrenceSeverity.OCCURRENCE_WITHOUT_SAFETY_EFFECT);
            preliminaryReports.add(r);
        }
        preliminaryReportDao.persist(preliminaryReports);
        data.put(ReportingPhase.PRELIMINARY, preliminaryReports);
        final Set<InvestigationReport> investigationReports = new HashSet<>();
        for (int i = 0; i < 3; i++) {
            final InvestigationReport r = new InvestigationReport();
            r.setOccurrence(occurrence);
            r.setRevision(2);
            r.setOccurrenceStart(startTime);
            r.setOccurrenceEnd(endTime);
            r.setFileNumber(System.currentTimeMillis());
            r.setAuthor(person);
            investigationReports.add(r);
        }
        investigationReportDao.persist(investigationReports);
        data.put(ReportingPhase.INVESTIGATION, investigationReports);
        return data;
    }

    @Test
    public void getReportsByOccurrenceReturnsBothPreliminaryAndInvestigationReports() throws Exception {
        final Collection<OccurrenceReport> results = occurrenceService.getReports(occurrence);
        assertNotNull(results);
        boolean found = false;
        for (OccurrenceReport r : results) {
            if (r.getTypes().contains(Vocabulary.PreliminaryReport)) {
                for (Report pr : data.get(ReportingPhase.PRELIMINARY)) {
                    if (r.getUri().equals(pr.getUri())) {
                        found = true;
                        break;
                    }
                }
            } else {
                for (Report ir : data.get(ReportingPhase.INVESTIGATION)) {
                    if (r.getUri().equals(ir.getUri())) {
                        found = true;
                        break;
                    }
                }
            }
            assertTrue(found);
        }
    }

    @Test
    public void testGetReportsByPhasePreliminary() throws Exception {
        final Collection<OccurrenceReport> results = occurrenceService
                .getReports(occurrence, ReportingPhase.PRELIMINARY);
        final Set<? extends Report> preliminaryReports = data.get(ReportingPhase.PRELIMINARY);
        assertEquals(preliminaryReports.size(), results.size());
        for (Report r : preliminaryReports) {
            OccurrenceReport matching = results.stream().filter(rep -> r.getUri().equals(rep.getUri())).findFirst()
                                               .get();
            assertNotNull(matching);
        }
    }

    @Test
    public void testGetReportsByPhaseInvestigation() throws Exception {
        final Collection<OccurrenceReport> results = occurrenceService
                .getReports(occurrence, ReportingPhase.INVESTIGATION);
        final Set<? extends Report> preliminaryReports = data.get(ReportingPhase.INVESTIGATION);
        assertEquals(preliminaryReports.size(), results.size());
        for (Report r : preliminaryReports) {
            OccurrenceReport matching = results.stream().filter(rep -> r.getUri().equals(rep.getUri())).findFirst()
                                               .get();
            assertNotNull(matching);
        }
    }
}