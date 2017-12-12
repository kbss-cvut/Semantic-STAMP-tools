package cz.cvut.kbss.reporting.persistence.dao;

import cz.cvut.kbss.jopa.model.EntityManager;
import cz.cvut.kbss.jopa.model.EntityManagerFactory;
import cz.cvut.kbss.reporting.environment.generator.Generator;
import cz.cvut.kbss.reporting.environment.generator.OccurrenceReportGenerator;
import cz.cvut.kbss.reporting.environment.util.Environment;
import cz.cvut.kbss.reporting.filter.*;
import cz.cvut.kbss.reporting.model.*;
import cz.cvut.kbss.reporting.persistence.BaseDaoTestRunner;
import org.hamcrest.Matchers;
import org.hamcrest.number.OrderingComparison;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.net.URI;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static cz.cvut.kbss.reporting.environment.util.TestUtils.verifyAllInstancesRemoved;
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
    public void setUp() {
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
        final OccurrenceReport report = report(false);
        occurrenceReportDao.persist(report);
        return report;
    }

    private OccurrenceReport report(boolean withFactorGraph) {
        final OccurrenceReport report =
                withFactorGraph ? OccurrenceReportGenerator.generateOccurrenceReportWithFactorGraph() :
                OccurrenceReportGenerator.generateOccurrenceReport(true);
        report.setAuthor(author);
        return report;
    }

    @Test
    public void persistReportWithFactorGraphCascadesPersistToAppropriateEventInstances() {
        final OccurrenceReport report = report(true);
        occurrenceReportDao.persist(report);

        final OccurrenceReport res = occurrenceReportDao.find(report.getUri());
        verifyFactorGraph(report.getOccurrence(), res.getOccurrence());
    }

    private void verifyFactorGraph(Occurrence expected, Occurrence actual) {
        assertEquals(expected.getUri(), actual.getUri());
        if (expected.getChildren() != null) {
            assertEquals(expected.getChildren().size(), actual.getChildren().size());
        }
        if (expected.getFactors() != null) {
            assertEquals(expected.getFactors().size(), actual.getFactors().size());
        }
        final Set<URI> visited = new HashSet<>();
        visited.add(actual.getUri());
        verifyChildren(expected.getChildren(), actual.getChildren(), visited);
        verifyFactors(expected.getFactors(), actual.getFactors(), visited);
    }

    private void verifyChildren(Set<Event> expected, Set<Event> actual, Set<URI> visited) {
        final List<Event> lExpected = new ArrayList<>(expected);
        lExpected.sort(Comparator.comparing(AbstractEntity::getUri));
        final List<Event> lActual = new ArrayList<>(actual);
        lActual.sort(Comparator.comparing(AbstractEntity::getUri));
        final Iterator<Event> itExp = lExpected.iterator();
        final Iterator<Event> itAct = lActual.iterator();
        while (itExp.hasNext() && itAct.hasNext()) {
            verifyFactorGraph(itExp.next(), itAct.next(), visited);
        }
    }

    private void verifyFactors(Set<Factor> expected, Set<Factor> actual, Set<URI> visited) {
        final List<Factor> lExpected = new ArrayList<>(expected);
        lExpected.sort(Comparator.comparing(AbstractEntity::getUri));
        final List<Factor> lActual = new ArrayList<>(actual);
        lActual.sort(Comparator.comparing(AbstractEntity::getUri));
        final Iterator<Factor> itExp = lExpected.iterator();
        final Iterator<Factor> itAct = lActual.iterator();
        while (itExp.hasNext() && itAct.hasNext()) {
            verifyFactorGraph(itExp.next().getEvent(), itAct.next().getEvent(), visited);
        }
    }

    private void verifyFactorGraph(Event expected, Event actual, Set<URI> visited) {
        if (visited.contains(actual.getUri())) {
            return;
        }
        visited.add(actual.getUri());
        assertEquals(expected.getUri(), actual.getUri());
        if (expected.getChildren() != null) {
            if (expected.getChildren().isEmpty()) {
                assertTrue(actual.getChildren() == null || actual.getChildren().isEmpty());
            } else {
                assertEquals(expected.getChildren().size(), actual.getChildren().size());
                verifyChildren(expected.getChildren(), actual.getChildren(), visited);
            }
        }
        if (expected.getFactors() != null) {
            if (expected.getFactors().isEmpty()) {
                assertTrue(actual.getFactors() == null || actual.getFactors().isEmpty());
            } else {
                assertEquals(expected.getFactors().size(), actual.getFactors().size());
                verifyFactors(expected.getFactors(), actual.getFactors(), visited);
            }
        }
    }

    @Test
    public void findByOccurrenceGetsReportsWithMatchingOccurrence() {
        final OccurrenceReport report = report(false);
        occurrenceReportDao.persist(report);
        final Occurrence occurrence = report.getOccurrence();

        final OccurrenceReport result = occurrenceReportDao.findByOccurrence(occurrence);
        assertNotNull(result);
        assertEquals(report.getUri(), result.getUri());
    }

    @Test
    public void findByOccurrenceReturnsNullWhenNoMatchingReportIsFound() {
        final Occurrence occurrence = OccurrenceReportGenerator.generateOccurrence();
        occurrenceDao.persist(occurrence);

        assertNull(occurrenceReportDao.findByOccurrence(occurrence));
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
        final OccurrenceReport report = report(false);
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
                    .setParameter("measureType", URI.create(Vocabulary.s_c_corrective_measure_request))
                    .getSingleResult();
            assertEquals(report.getCorrectiveMeasures().size(), cnt.intValue());
        } finally {
            em.close();
        }
    }

    @Test
    public void updateWorksForReportsWithoutCorrectiveMeasures() {
        final OccurrenceReport report = persistReport();

        report.setSummary("New updated summary.");
        occurrenceReportDao.update(report);

        final OccurrenceReport result = occurrenceReportDao.find(report.getUri());
        assertEquals(report.getSummary(), result.getSummary());
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
    public void updateReportByAddingItemsIntoFactorGraph() {
        final OccurrenceReport report = report(true);
        occurrenceReportDao.persist(report);

        final Event addedOne = new Event();
        addedOne.setStartTime(report.getOccurrence().getStartTime());
        addedOne.setEndTime(report.getOccurrence().getEndTime());
        addedOne.setEventType(Generator.generateEventType());
        final Factor newF = new Factor();
        newF.setEvent(addedOne);
        newF.addType(Generator.randomFactorType());
        report.getOccurrence().addFactor(newF);
        final Event addedChild = new Event();
        addedChild.setStartTime(report.getOccurrence().getStartTime());
        addedChild.setEndTime(report.getOccurrence().getEndTime());
        addedChild.setEventType(Generator.generateEventType());
        report.getOccurrence().getChildren().iterator().next().addChild(addedChild);

        occurrenceReportDao.update(report);

        final OccurrenceReport result = occurrenceReportDao.find(report.getUri());
        verifyFactorGraph(report.getOccurrence(), result.getOccurrence());
    }

    @Test
    public void updateReportByRemovingItemsFromFactorGraph() {
        final OccurrenceReport report = report(true);
        occurrenceReportDao.persist(report);

        final Iterator<Event> evtRemove = report.getOccurrence().getChildren().iterator().next().getChildren()
                                                .iterator();
        evtRemove.next();
        evtRemove.remove();

        final Iterator<Event> factRemove = report.getOccurrence().getFactors().iterator().next().getEvent()
                                                 .getChildren().iterator();
        factRemove.next();
        factRemove.remove();

        occurrenceReportDao.update(report);

        final OccurrenceReport result = occurrenceReportDao.find(report.getUri());
        verifyFactorGraph(report.getOccurrence(), result.getOccurrence());
    }

    @Test
    public void removeDeletesCorrectiveMeasuresAsWell() {
        final OccurrenceReport report = prepareReportWithMeasureRequests();
        occurrenceReportDao.persist(report);

        occurrenceReportDao.remove(report);
        assertFalse(occurrenceReportDao.exists(report.getUri()));
        verifyAllInstancesRemoved(emf, Vocabulary.s_c_corrective_measure_request);
    }

    @Test
    public void removeDeletesCompleteFactorGraph() {
        final OccurrenceReport report = OccurrenceReportGenerator.generateOccurrenceReportWithFactorGraph();
        report.setAuthor(author);
        occurrenceReportDao.persist(report);

        occurrenceReportDao.remove(report);
        verifyAllInstancesRemoved(emf, Vocabulary.s_c_Occurrence);
        verifyAllInstancesRemoved(emf, Vocabulary.s_c_Event);
    }

    @Test
    public void removeDeletesCompleteQuestionAnswerTree() {
        final OccurrenceReport report = OccurrenceReportGenerator.generateOccurrenceReportWithFactorGraph();
        report.setAuthor(author);
        report.getOccurrence().setQuestion(Generator.generateQuestions(2));
        report.getOccurrence().getChildren().iterator().next().setQuestion(Generator.generateQuestions(3));
        occurrenceReportDao.persist(report);

        occurrenceReportDao.remove(report);
        verifyAllInstancesRemoved(emf, Vocabulary.s_c_question);
        verifyAllInstancesRemoved(emf, Vocabulary.s_c_answer);
    }

    @Test
    public void pageableFindAllReturnsReportsOnMatchingPage() {
        final List<OccurrenceReport> reports = generateReports();
        occurrenceReportDao.persist(reports);
        final int pageSize = 5;
        final int pageNo = 1;   // Page numbers start from 0
        final Pageable pageReq = PageRequest.of(pageNo, pageSize);
        final Page<OccurrenceReport> page = occurrenceReportDao.findAll(pageReq, Collections.emptyList());
        assertNotNull(page);
        final List<OccurrenceReport> result = page.getContent();
        assertEquals(pageSize, result.size());
        for (int i = 0; i < pageSize; i++) {
            assertEquals(reports.get(pageSize * pageNo + i).getUri(), result.get(i).getUri());
        }
    }

    private List<OccurrenceReport> generateReports() {
        final int reportCount = 15;
        final List<OccurrenceReport> reports = OccurrenceReportGenerator.generateReports(true, reportCount);
        reports.forEach(r -> r.setAuthor(author));
        return reports;
    }

    @Test
    public void pageableReturnedFromFindAllContainsPageNumberSizeAndIsLast() {
        final List<OccurrenceReport> reports = generateReports();
        occurrenceReportDao.persist(reports);
        final int pageNo = 1;
        final int pageSize = reports.size() / (pageNo + 1) + 1;
        final Pageable pageReq = PageRequest.of(pageNo, pageSize);
        final Page<OccurrenceReport> page = occurrenceReportDao.findAll(pageReq, Collections.emptyList());
        assertNotNull(page);
        assertEquals(pageNo, page.getNumber());
        assertEquals(pageSize, page.getSize());
        assertThat(page.getNumberOfElements(), OrderingComparison.lessThan(pageSize));
    }

    @Test
    public void findAllReturnsReportsWithMatchingOccurrenceCategory() {
        final URI occurrenceCategory = Generator.generateEventType();
        final List<OccurrenceReport> matching = generateReportsForFiltering(
                r -> r.getOccurrence().setEventType(occurrenceCategory));

        final ReportFilter filter = ReportFilter.create(OccurrenceCategoryFilter.KEY,
                Collections.singletonList(occurrenceCategory.toString())).get();
        final Page<OccurrenceReport> result = occurrenceReportDao
                .findAll(PageRequest.of(0, Integer.MAX_VALUE), Collections.singletonList(filter));
        assertEquals(matching.size(), result.getNumberOfElements());
        for (int i = 0; i < matching.size(); i++) {
            assertEquals(matching.get(i).getUri(), result.getContent().get(i).getUri());
        }
    }

    private List<OccurrenceReport> generateReportsForFiltering(Consumer<OccurrenceReport> transformation) {
        final List<OccurrenceReport> allReports = generateReports();
        final List<OccurrenceReport> matching = allReports.stream().filter(r -> Generator.randomBoolean())
                                                          .peek(transformation)
                                                          .sorted(Comparator.comparing(
                                                                  (OccurrenceReport r) -> r.getOccurrence()
                                                                                           .getStartTime()).reversed())
                                                          .collect(Collectors.toList());
        occurrenceReportDao.persist(allReports);
        return matching;
    }

    @Test
    public void findAllReturnsReportsWithMatchingSeverityAssessment() {
        final URI severity = Generator.generateUri();
        final List<OccurrenceReport> matching = generateReportsForFiltering(r -> r.setSeverityAssessment(severity));

        final ReportFilter filter = ReportFilter.create(SeverityAssessmentFilter.KEY,
                Collections.singletonList(severity.toString())).get();
        final Page<OccurrenceReport> result = occurrenceReportDao
                .findAll(PageRequest.of(0, Integer.MAX_VALUE), Collections.singletonList(filter));
        assertEquals(matching.size(), result.getNumberOfElements());
        for (int i = 0; i < matching.size(); i++) {
            assertEquals(matching.get(i).getUri(), result.getContent().get(i).getUri());
        }
    }

    @Test
    public void findAllReturnsReportsMatchingCombinedOccurrenceCategoryAndSeverityFilter() {
        final URI severity = Generator.generateUri();
        final URI occurrenceCategory = Generator.generateEventType();
        final List<OccurrenceReport> matching = generateReportsForFiltering(r -> {
            r.setSeverityAssessment(severity);
            r.getOccurrence().setEventType(occurrenceCategory);
        });

        final ReportFilter severityFilter = ReportFilter.create(SeverityAssessmentFilter.KEY,
                Collections.singletonList(severity.toString())).get();
        final ReportFilter categoryFilter = ReportFilter
                .create(OccurrenceCategoryFilter.KEY, Collections.singletonList(occurrenceCategory.toString())).get();
        final Page<OccurrenceReport> result = occurrenceReportDao
                .findAll(PageRequest.of(0, Integer.MAX_VALUE), Arrays.asList(severityFilter, categoryFilter));
        assertEquals(matching.size(), result.getNumberOfElements());
        for (int i = 0; i < matching.size(); i++) {
            assertEquals(matching.get(i).getUri(), result.getContent().get(i).getUri());
        }
    }

    @Test
    public void findAllReturnsReportsMatchingMultivaluedFilter() {
        final List<OccurrenceReport> reports = generateReports();
        occurrenceReportDao.persist(reports);
        final URI categoryOne = reports.get(Generator.randomIndex(reports)).getOccurrence().getEventType();
        final URI categoryTwo = reports.get(Generator.randomIndex(reports)).getOccurrence().getEventType();
        final ReportFilter filter = ReportFilter
                .create(OccurrenceCategoryFilter.KEY, Arrays.asList(categoryOne.toString(), categoryTwo.toString()))
                .get();

        final Page<OccurrenceReport> result = occurrenceReportDao
                .findAll(PageRequest.of(0, Integer.MAX_VALUE), Collections.singletonList(filter));
        assertTrue(result.getNumberOfElements() > 0);
        result.getContent().forEach(r -> assertThat(r.getOccurrence().getEventType(),
                Matchers.anyOf(Matchers.equalTo(categoryOne), Matchers.equalTo(categoryTwo))));
    }

    @Test
    public void findAllReturnsMatchingReportKeyFilter() {
        final List<OccurrenceReport> reports = generateReports();
        occurrenceReportDao.persist(reports);
        final Set<String> keys = reports.stream().filter(r -> Generator.randomBoolean()).map(AbstractReport::getKey)
                                        .collect(Collectors.toSet());
        final ReportFilter filter = ReportFilter.create(ReportKeyFilter.KEY, new ArrayList<>(keys)).get();

        final Page<OccurrenceReport> result = occurrenceReportDao
                .findAll(PageRequest.of(0, Integer.MAX_VALUE), Collections.singletonList(filter));
        assertEquals(keys.size(), result.getNumberOfElements());
        result.getContent().forEach(r -> assertTrue(keys.contains(r.getKey())));
    }

    @Test
    public void findAllReturnsReportsWithAuthorMatchingPersonFilter() {
        final List<OccurrenceReport> reports = generateReports();
        final Person anotherPerson = generateAnotherPerson();
        persistPerson(anotherPerson);
        final List<OccurrenceReport> matching = reports.stream().filter(r -> Generator.randomBoolean())
                                                       .collect(Collectors.toList());
        matching.forEach(r -> r.setAuthor(anotherPerson));
        occurrenceReportDao.persist(reports);
        Environment.setCurrentUser(anotherPerson);
        final ReportFilter filter = ReportFilter
                .create(PersonFilter.KEY, Collections.singletonList(Boolean.TRUE.toString())).get();
        final Page<OccurrenceReport> result = occurrenceReportDao
                .findAll(PageRequest.of(0, Integer.MAX_VALUE), Collections.singletonList(filter));
        assertEquals(matching.size(), result.getNumberOfElements());
        result.getContent().forEach(r -> assertEquals(anotherPerson.getUri(), r.getAuthor().getUri()));
    }

    private Person generateAnotherPerson() {
        final Person anotherPerson = new Person();
        anotherPerson.setFirstName("another");
        anotherPerson.setLastName("person");
        anotherPerson.setUsername("another@person.cz");
        anotherPerson.setPassword("aaa");
        anotherPerson.generateUri();
        return anotherPerson;
    }

    @Test
    public void findAllReturnsReportsWithLastEditorMatchingPersonFilter() {
        final List<OccurrenceReport> reports = generateReports();
        final Person anotherPerson = generateAnotherPerson();
        persistPerson(anotherPerson);
        final List<OccurrenceReport> matching = reports.stream().filter(r -> Generator.randomBoolean())
                                                       .collect(Collectors.toList());
        matching.forEach(r -> r.setLastModifiedBy(anotherPerson));
        occurrenceReportDao.persist(reports);
        Environment.setCurrentUser(anotherPerson);
        final ReportFilter filter = ReportFilter
                .create(PersonFilter.KEY, Collections.singletonList(Boolean.TRUE.toString())).get();
        final Page<OccurrenceReport> result = occurrenceReportDao
                .findAll(PageRequest.of(0, Integer.MAX_VALUE), Collections.singletonList(filter));
        assertEquals(matching.size(), result.getNumberOfElements());
        result.getContent().forEach(r -> assertEquals(anotherPerson.getUri(), r.getLastModifiedBy().getUri()));
    }

    @Test
    public void findAllReturnsReportsWithAuthorOrLastEditorMatchingPersonFilter() {
        final List<OccurrenceReport> reports = generateReports();
        final Person anotherPerson = generateAnotherPerson();
        persistPerson(anotherPerson);
        final List<OccurrenceReport> matching = reports.stream().filter(r -> Generator.randomBoolean())
                                                       .collect(Collectors.toList());
        for (OccurrenceReport r : matching) {
            if (Generator.randomBoolean()) {
                r.setAuthor(anotherPerson);
            } else {
                r.setLastModifiedBy(anotherPerson);
            }
        }
        occurrenceReportDao.persist(reports);
        Environment.setCurrentUser(anotherPerson);
        final ReportFilter filter = ReportFilter
                .create(PersonFilter.KEY, Collections.singletonList(Boolean.TRUE.toString())).get();
        final Page<OccurrenceReport> result = occurrenceReportDao
                .findAll(PageRequest.of(0, Integer.MAX_VALUE), Collections.singletonList(filter));
        assertEquals(matching.size(), result.getNumberOfElements());
        result.getContent()
              .forEach(r -> assertTrue(r.getAuthor().getUri().equals(anotherPerson.getUri()) ||
                      r.getLastModifiedBy() != null && r.getLastModifiedBy().getUri().equals(anotherPerson.getUri())));
    }
}
