package cz.cvut.kbss.inbas.reporting.persistence.dao;

import cz.cvut.kbss.inbas.reporting.environment.util.Environment;
import cz.cvut.kbss.inbas.reporting.environment.util.Generator;
import cz.cvut.kbss.inbas.reporting.model.*;
import cz.cvut.kbss.inbas.reporting.persistence.BaseDaoTestRunner;
import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.util.*;

import static org.junit.Assert.*;

public class OccurrenceReportDaoTest extends BaseDaoTestRunner {

    private static final String ORGANIZATION_NAME = "Czech Technical University in Prague";

    @Autowired
    private OccurrenceReportDao occurrenceReportDao;

    @Autowired
    private OccurrenceDao occurrenceDao;

    @Autowired
    private OrganizationDao organizationDao;

    @Autowired
    private EntityManagerFactory emf;

    private Person author;

    @Before
    public void setUp() throws Exception {
        this.author = Generator.getPerson();
        persistPerson(author);
    }

    @Test
    public void persistNewReportPersistsOccurrenceAsWell() {
        final OccurrenceReport report = persistReport();

        final Occurrence occurrence = occurrenceDao.find(report.getOccurrence().getUri());
        assertNotNull(occurrence);
    }

    private OccurrenceReport persistReport() {
        final OccurrenceReport report = Generator.generateOccurrenceReport(true);
        report.setAuthor(author);
        occurrenceReportDao.persist(report);
        return report;
    }

    @Test
    public void persistReportWithExistingOccurrenceReusesOccurrenceInstance() {
        final OccurrenceReport report = persistReport();

        final OccurrenceReport newReport = new OccurrenceReport(report);
        assertSame(report.getOccurrence(), newReport.getOccurrence());
        newReport.setAuthor(author);
        newReport.setRevision(report.getRevision() + 1);
        occurrenceReportDao.persist(newReport);

        final OccurrenceReport resOrig = occurrenceReportDao.find(report.getUri());
        assertNotNull(resOrig);
        final OccurrenceReport resCopy = occurrenceReportDao.find(newReport.getUri());
        assertNotNull(resCopy);
        assertEquals(resOrig.getOccurrence().getUri(), resCopy.getOccurrence().getUri());
    }

    @Test
    public void findByOccurrenceGetsReportsWithMatchingOccurrence() {
        final Occurrence occurrence = Generator.generateOccurrence();
        occurrenceDao.persist(occurrence);
        final List<OccurrenceReport> reports = persistReportsForOccurrence(occurrence);
        // This one is just so that the method does not simply select all reports
        persistReport();

        final List<OccurrenceReport> result = occurrenceReportDao.findByOccurrence(occurrence);
        assertTrue(Environment.areEqual(reports, result));
    }

    private List<OccurrenceReport> persistReportsForOccurrence(Occurrence occurrence) {
        final List<OccurrenceReport> reports = new ArrayList<>();
        for (int i = 0; i < Generator.randomInt(10); i++) {
            final OccurrenceReport r = Generator.generateOccurrenceReport(true);
            r.setOccurrence(occurrence);
            r.setAuthor(author);
            r.setOccurrenceStart(new Date(System.currentTimeMillis() + i * 1000));
            reports.add(r);
        }
        occurrenceReportDao.persist(reports);
        return reports;
    }

    @Test
    public void findByOccurrenceReturnsLatestRevisionsOfMatchingReportChains() throws Exception {
        final Occurrence occurrence = Generator.generateOccurrence();
        occurrenceDao.persist(occurrence);
        final Set<URI> reportUris = new HashSet<>();
        for (int i = 0; i < Generator.randomInt(10); i++) {
            final List<OccurrenceReport> chain = Generator.generateOccurrenceReportChain(author);
            chain.forEach(r -> r.setOccurrence(occurrence));
            occurrenceReportDao.persist(chain);
            reportUris.add(chain.get(chain.size() - 1).getUri());
        }

        final List<OccurrenceReport> result = occurrenceReportDao.findByOccurrence(occurrence);
        assertEquals(reportUris.size(), result.size());
        result.forEach(r -> assertTrue(reportUris.contains(r.getUri())));
    }

    @Test
    public void findByOccurrenceReturnsReportsOrderedByOccurrenceStartDescending() {
        final Occurrence occurrence = Generator.generateOccurrence();
        occurrenceDao.persist(occurrence);
        final List<OccurrenceReport> reports = persistReportsForOccurrence(occurrence);
        Collections.sort(reports, (a, b) -> b.getOccurrenceStart().compareTo(a.getOccurrenceStart()));  // Descending

        final List<OccurrenceReport> result = occurrenceReportDao.findByOccurrence(occurrence);
        assertTrue(Environment.areEqual(reports, result));
    }

    @Test
    public void persistPersistsReportWithCorrectiveMeasureRequestsWithResponsibleAgentsAndRelatedOccurrence() {
        final OccurrenceReport report = prepareReportWithMeasureRequests();
        occurrenceReportDao.persist(report);

        final OccurrenceReport result = occurrenceReportDao.findByKey(report.getKey());
        assertNotNull(result);
        verifyCorrectiveMeasureRequests(report.getCorrectiveMeasures(), result.getCorrectiveMeasures());
    }

    private void verifyCorrectiveMeasureRequests(Set<CorrectiveMeasureRequest> expected,
                                                 Set<CorrectiveMeasureRequest> actual) {
        assertEquals(expected.size(), actual.size());
        boolean found;
        for (CorrectiveMeasureRequest r : expected) {
            found = false;
            for (CorrectiveMeasureRequest rr : actual) {
                if (r.getUri().equals(rr.getUri())) {
                    assertEquals(r.getDescription(), rr.getDescription());
                    found = true;
                }
            }
            assertTrue(found);
        }
    }

    private OccurrenceReport prepareReportWithMeasureRequests() {
        final OccurrenceReport report = Generator.generateOccurrenceReport(true);
        report.setAuthor(author);
        final Organization org = new Organization(ORGANIZATION_NAME);
        report.setCorrectiveMeasures(new HashSet<>());
        organizationDao.persist(org);   // The organization must exist
        for (int i = 0; i < Generator.randomInt(10); i++) {
            final CorrectiveMeasureRequest req = new CorrectiveMeasureRequest();
            req.setDescription("Corrective measure request " + i);
            if (i % 2 == 0) {
                req.setResponsiblePersons(Collections.singleton(author));
            } else {
                req.setResponsibleOrganizations(Collections.singleton(org));
            }
            req.setBasedOnOccurrence(report.getOccurrence());
            report.getCorrectiveMeasures().add(req);
        }
        return report;
    }

    @Test
    public void updateUpdatesCorrectiveMeasureRequestsInReport() {
        final OccurrenceReport report = prepareReportWithMeasureRequests();
        occurrenceReportDao.persist(report);

        final CorrectiveMeasureRequest newRequest = new CorrectiveMeasureRequest();
        newRequest.setDescription("Added corrective measure request");
        newRequest.setResponsiblePersons(Collections.singleton(author));
        newRequest.setResponsibleOrganizations(Collections.singleton(organizationDao.findByName(ORGANIZATION_NAME)));
        final Iterator<CorrectiveMeasureRequest> it = report.getCorrectiveMeasures().iterator();
        it.next();
        it.remove();
        report.getCorrectiveMeasures().add(newRequest);
        occurrenceReportDao.update(report);

        final OccurrenceReport result = occurrenceReportDao.find(report.getUri());
        verifyCorrectiveMeasureRequests(report.getCorrectiveMeasures(), result.getCorrectiveMeasures());
        verifyOrphanRemoval(report);
    }

    private void verifyOrphanRemoval(OccurrenceReport report) {
        final EntityManager em = emf.createEntityManager();
        try {
            final Integer cnt = em
                    .createNativeQuery("SELECT (count(?x) as ?count) WHERE {?x a ?measureType . }", Integer.class)
                    .setParameter("measureType", URI.create(Vocabulary.CorrectiveMeasureRequest)).getSingleResult();
            assertEquals(report.getCorrectiveMeasures().size(), cnt.intValue());
        } finally {
            em.close();
        }
    }

    @Test
    public void updateWorksForReportsWithoutCorrectiveMeasures() {
        final OccurrenceReport report = persistReport();

        report.setOccurrenceStart(new Date());
        report.setOccurrenceEnd(new Date(System.currentTimeMillis() + 100000));
        occurrenceReportDao.update(report);

        final OccurrenceReport result = occurrenceReportDao.find(report.getUri());
        assertEquals(report.getOccurrenceStart(), result.getOccurrenceStart());
        assertEquals(report.getOccurrenceEnd(), result.getOccurrenceEnd());
    }

    @Test
    public void reportUpdateCascadesChangeToOccurrence() {
        final OccurrenceReport report = persistReport();

        final String newName = "UpdatedOccurrenceName";
        report.getOccurrence().setName(newName);
        occurrenceReportDao.update(report);

        final OccurrenceReport result = occurrenceReportDao.find(report.getUri());
        assertEquals(newName, result.getOccurrence().getName());
    }

    @Test
    public void removeDeletesCorrectiveMeasuresAsWell() {
        final OccurrenceReport report = prepareReportWithMeasureRequests();
        occurrenceReportDao.persist(report);

        occurrenceReportDao.remove(report);
        assertFalse(occurrenceReportDao.exists(report.getUri()));
        final EntityManager em = emf.createEntityManager();
        try {
            for (CorrectiveMeasureRequest cmr : report.getCorrectiveMeasures()) {
                final Boolean res = em.createNativeQuery("ASK WHERE { ?x a ?type . }", Boolean.class)
                                      .setParameter("x", cmr.getUri())
                                      .setParameter("type", URI.create(Vocabulary.CorrectiveMeasureRequest))
                                      .getSingleResult();
                assertFalse(res);
            }
        } finally {
            em.close();
        }
    }
}
