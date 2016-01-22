package cz.cvut.kbss.inbas.audit.persistence.dao;

import cz.cvut.kbss.inbas.audit.environment.util.Generator;
import cz.cvut.kbss.inbas.audit.model.*;
import cz.cvut.kbss.inbas.audit.model.reports.*;
import cz.cvut.kbss.inbas.audit.model.reports.incursions.Intruder;
import cz.cvut.kbss.inbas.audit.model.reports.incursions.PersonIntruder;
import cz.cvut.kbss.inbas.audit.model.reports.incursions.RunwayIncursion;
import cz.cvut.kbss.inbas.audit.model.reports.incursions.Vehicle;
import cz.cvut.kbss.inbas.audit.persistence.BaseDaoTestRunner;
import cz.cvut.kbss.inbas.audit.util.Constants;
import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.util.*;

import static org.junit.Assert.*;

public class PreliminaryReportDaoTest extends BaseDaoTestRunner {

    private static EventType eventType;
    private static Organization organization;

    private Person author;

    @Autowired
    private PreliminaryReportDao dao;

    @Autowired
    private InitialReportDao irDao;
    @Autowired
    private PersonDao personDao;

    @Autowired
    private OccurrenceDao occurrenceDao;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        eventType = new EventType(URI.create(
                "http://onto.fel.cvut.cz/ontologies/2014/ECCAIRS_Aviation_1.3.0.12/eccairs-events-390#category_2200101"));
        eventType.setName("Runway incursion by an aircraft");
        organization = new Organization("CSA");
        organization.setUri(URI.create("http://onto.fel.cvut.cz/ontologies/2014/inbas/organizations#CSA"));
    }

    @Before
    public void setUp() throws Exception {
        author = Generator.getPerson();
        personDao.persist(author);
    }

    @Test
    public void persistenceInitialization() throws Exception {
        final Collection<PreliminaryReport> res = dao.findAll();
        assertNotNull(res);
    }

    @Test
    public void persistGeneratesKeyOnInstance() throws Exception {
        final PreliminaryReport report = Generator
                .generatePreliminaryReport(Generator.ReportType.WITHOUT_TYPE_ASSESSMENTS);
        assertNull(report.getKey());
        dao.persist(report);
        assertNotNull(report.getKey());
    }

    @Test
    public void persistingReportsWithSameEventTypeSavesItFirstTimeAndReusesLater() throws Exception {
        final PreliminaryReport rOne = initPreliminaryReportWithTypeAssessment(eventType);
        dao.persist(rOne);

        final PreliminaryReport rTwo = initPreliminaryReportWithTypeAssessment(eventType);
        dao.persist(rTwo);

        final PreliminaryReport resOne = dao.find(rOne.getUri());
        assertNotNull(resOne);
        assertEquals(1, resOne.getTypeAssessments().size());
        final PreliminaryReport resTwo = dao.find(rTwo.getUri());
        assertNotNull(resTwo);
        assertEquals(1, resTwo.getTypeAssessments().size());
        assertEquals(resOne.getTypeAssessments().iterator().next().getEventType(),
                resTwo.getTypeAssessments().iterator().next().getEventType());
    }

    private PreliminaryReport initPreliminaryReportWithTypeAssessment(EventType eventType) {
        final PreliminaryReport rOne = initBasicValidReport();
        final EventTypeAssessment typeAssessment = new EventTypeAssessment();

        typeAssessment.setEventType(eventType);
        rOne.setTypeAssessments(Collections.singleton(typeAssessment));
        return rOne;
    }

    @Test
    public void persistingPersonIntruderFromExistingOrganizationUsesTheExistingOne() throws Exception {
        PreliminaryReport rOne = persistReportWithConflictingAircraftAndOrganization();

        final PreliminaryReport rTwo = initPreliminaryReportWithTypeAssessment(eventType);
        final EventTypeAssessment assTwo = rTwo.getTypeAssessments().iterator().next();
        final RunwayIncursion incursionTwo = new RunwayIncursion();
        final PersonIntruder intruder = new PersonIntruder();
        intruder.setOrganization(organization);
        incursionTwo.setIntruder(new Intruder(intruder));
        assTwo.setRunwayIncursion(incursionTwo);
        dao.persist(rTwo);

        final PreliminaryReport resOne = dao.find(rOne.getUri());
        final EventTypeAssessment typeOne = resOne.getTypeAssessments().iterator().next();
        final Aircraft aircraft = typeOne.getRunwayIncursion().getConflictingAircraft();
        final Organization org = aircraft.getOperator();
        assertEquals(organization, org);
        final PreliminaryReport resTwo = dao.find(rTwo.getUri());
        final EventTypeAssessment typeTwo = resTwo.getTypeAssessments().iterator().next();
        final PersonIntruder person = typeTwo.getRunwayIncursion().getIntruder().getPerson();
        assertEquals(organization, person.getOrganization());
    }

    private PreliminaryReport persistReportWithConflictingAircraftAndOrganization() {
        final PreliminaryReport rOne = initPreliminaryReportWithTypeAssessment(eventType);
        final EventTypeAssessment ass = rOne.getTypeAssessments().iterator().next();
        final Aircraft cleared = new Aircraft();
        cleared.setOperator(organization);
        cleared.setOperationType("passenger");
        final RunwayIncursion incursion = new RunwayIncursion();
        incursion.setConflictingAircraft(cleared);
        incursion.setIntruder(new Intruder(new PersonIntruder()));
        ass.setRunwayIncursion(incursion);
        dao.persist(rOne);
        return rOne;
    }

    @Test
    public void persistingVehicleIntruderFromExistingOrganizationUsesTheExistingOne() {
        PreliminaryReport rOne = persistReportWithConflictingAircraftAndOrganization();

        final PreliminaryReport rTwo = initPreliminaryReportWithTypeAssessment(eventType);
        final EventTypeAssessment assTwo = rTwo.getTypeAssessments().iterator().next();
        final RunwayIncursion incursionTwo = new RunwayIncursion();
        final Vehicle intruder = new Vehicle();
        intruder.setOrganization(organization);
        incursionTwo.setIntruder(new Intruder(intruder));
        assTwo.setRunwayIncursion(incursionTwo);
        dao.persist(rTwo);

        final PreliminaryReport resOne = dao.find(rOne.getUri());
        final EventTypeAssessment typeOne = resOne.getTypeAssessments().iterator().next();
        final Aircraft aircraft = typeOne.getRunwayIncursion().getConflictingAircraft();
        final Organization org = aircraft.getOperator();
        assertEquals(organization, org);
        final PreliminaryReport resTwo = dao.find(rTwo.getUri());
        final EventTypeAssessment typeTwo = resTwo.getTypeAssessments().iterator().next();
        final Vehicle vehicle = typeTwo.getRunwayIncursion().getIntruder().getVehicle();
        assertEquals(organization, vehicle.getOrganization());
    }

    @Test
    public void persistingAircraftIntruderFromExistingOrganizationUsesTheExistingOne() {
        PreliminaryReport rOne = persistReportWithConflictingAircraftAndOrganization();

        final PreliminaryReport rTwo = initPreliminaryReportWithTypeAssessment(eventType);
        final EventTypeAssessment assTwo = rTwo.getTypeAssessments().iterator().next();
        final RunwayIncursion incursionTwo = new RunwayIncursion();
        final Aircraft intruder = new Aircraft();
        intruder.setOperator(organization);
        incursionTwo.setIntruder(new Intruder(intruder));
        assTwo.setRunwayIncursion(incursionTwo);
        dao.persist(rTwo);

        final PreliminaryReport resOne = dao.find(rOne.getUri());
        final EventTypeAssessment typeOne = resOne.getTypeAssessments().iterator().next();
        final Aircraft clearedAircraft = typeOne.getRunwayIncursion().getConflictingAircraft();
        final Organization org = clearedAircraft.getOperator();
        assertEquals(organization, org);
        final PreliminaryReport resTwo = dao.find(rTwo.getUri());
        final EventTypeAssessment typeTwo = resTwo.getTypeAssessments().iterator().next();
        final Aircraft aircraftEvent = typeTwo.getRunwayIncursion().getIntruder().getAircraft();
        assertEquals(organization, aircraftEvent.getOperator());
    }

    @Test
    public void persistingIncursionWithExistingLocationReusesTheExistingOne() {
        final Location loc = new Location();
        loc.setUri(URI.create("http://onto.fel.cvut.cz/ontologies/aviation-safety/LKPR"));
        loc.setName("LKPR");
        final PreliminaryReport rOne = initOccurrenceReportWithLocation(loc);
        dao.persist(rOne);

        final PreliminaryReport rTwo = initOccurrenceReportWithLocation(loc);
        dao.persist(rTwo);

        final PreliminaryReport resOne = dao.find(rOne.getUri());
        assertNotNull(resOne);
        final Location resLocOne = resOne.getTypeAssessments().iterator().next().getRunwayIncursion().getLocation();
        assertNotNull(resLocOne);
        assertEquals(loc, resLocOne);

        final PreliminaryReport resTwo = dao.find(rOne.getUri());
        assertNotNull(resTwo);
        final Location resLocTwo = resTwo.getTypeAssessments().iterator().next().getRunwayIncursion().getLocation();
        assertNotNull(resLocTwo);
        assertEquals(loc, resLocTwo);
    }

    private PreliminaryReport initOccurrenceReportWithLocation(Location location) {
        final PreliminaryReport r = initBasicValidReport();
        final EventTypeAssessment type = new EventTypeAssessment();
        type.setEventType(eventType);
        final RunwayIncursion ri = new RunwayIncursion();
        ri.setLocation(location);
        ri.setIntruder(new Intruder(new PersonIntruder()));
        type.setRunwayIncursion(ri);
        r.setTypeAssessments(Collections.singleton(type));
        return r;
    }

    @Test
    public void initialReportsArePersistedWhenOccurrenceReportIsPersisted() {
        final PreliminaryReport report = initBasicValidReport();
        final Set<InitialReport> initialReports = initInitialReports(report);

        dao.persist(report);

        for (InitialReport ir : initialReports) {
            assertNotNull(ir.getKey());
            assertNotNull(ir.getUri());
        }
        final PreliminaryReport res = dao.find(report.getUri());
        assertEquals(initialReports.size(), res.getInitialReports().size());
    }

    private PreliminaryReport initBasicValidReport() {
        final PreliminaryReport report = new PreliminaryReport();
        report.setOccurrence(Generator.generateOccurrence());
        report.setOccurrenceStart(new Date(System.currentTimeMillis() - 10000));
        report.setOccurrenceEnd(new Date());
        report.setSeverityAssessment(OccurrenceSeverity.OCCURRENCE_WITHOUT_SAFETY_EFFECT);
        report.setAuthor(author);
        report.setFileNumber(System.currentTimeMillis());
        return report;
    }

    private Set<InitialReport> initInitialReports(PreliminaryReport report) {
        final Set<InitialReport> initialReports = new HashSet<>();
        for (int i = 0; i < 3; i++) {
            initialReports.add(new InitialReport("This is a test initial report no. " + i));
        }
        report.setInitialReports(initialReports);
        return initialReports;
    }

    @Test
    public void initialReportsArePersistedWhenOccurrenceReportIsUpdated() {
        final PreliminaryReport report = initBasicValidReport();
        final Set<InitialReport> initialReports = initInitialReports(report);

        dao.persist(report);

        final PreliminaryReport toUpdate = dao.find(report.getUri());
        final String addedText = "Added initial report";
        toUpdate.getInitialReports().add(new InitialReport(addedText));
        dao.update(toUpdate);

        final PreliminaryReport result = dao.find(report.getUri());
        assertEquals(initialReports.size() + 1, result.getInitialReports().size());
        boolean found = false;
        for (InitialReport ir : result.getInitialReports()) {
            if (ir.getText().equals(addedText)) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    public void initialReportsAreUpdatedWhenOccurrenceReportIsUpdated() {
        final PreliminaryReport report = initBasicValidReport();
        initInitialReports(report);

        dao.persist(report);

        final PreliminaryReport toUpdate = dao.find(report.getUri());
        final String updatedText = "Updated initial report text.";
        final InitialReport updated = toUpdate.getInitialReports().iterator().next();
        updated.setText(updatedText);

        dao.update(toUpdate);

        final InitialReport result = irDao.find(updated.getUri());
        assertEquals(updatedText, result.getText());
    }

    @Test
    public void occurrenceIsPersistedWhenReportIsPersisted() {
        final PreliminaryReport report = initBasicValidReport();
        assertEquals(Constants.INITIAL_REVISION, report.getRevision());

        dao.persist(report);

        assertNotNull(report.getOccurrence().getKey());
        assertNotNull(report.getOccurrence().getUri());
        final Occurrence occurrence = occurrenceDao.find(report.getOccurrence().getUri());
        assertNotNull(occurrence);
        assertEquals(report.getOccurrence().getName(), occurrence.getName());
    }

    @Test
    public void setsReportRevisionToInitialWhenItIsMissing() {
        final PreliminaryReport report = initBasicValidReport();
        report.setRevision(null);

        dao.persist(report);

        assertNotNull(report.getRevision());
        assertEquals(Constants.INITIAL_REVISION, report.getRevision());
    }

    @Test
    public void addsReportToPreliminaryReportsTypesOnPersist() throws Exception {
        final PreliminaryReport report = initBasicValidReport();

        dao.persist(report);
        assertTrue(report.getTypes().contains(Vocabulary.Report));

        final PreliminaryReport result = dao.find(report.getUri());
        assertTrue(result.getTypes().contains(Vocabulary.Report));
    }

    @Test
    public void ensuresReportIsPresentInPreliminaryReportsTypesOnUpdate() throws Exception {
        final PreliminaryReport report = initBasicValidReport();

        dao.persist(report);
        assertTrue(report.getTypes().contains(Vocabulary.Report));

        final PreliminaryReport toUpdate = dao.find(report.getUri());
        toUpdate.setTypes(new HashSet<>());
        dao.update(toUpdate);

        final PreliminaryReport result = dao.find(report.getUri());
        assertTrue(result.getTypes().contains(Vocabulary.Report));
    }

    @Test
    public void findAllReturnsOnlyLatestRevisionsOfReports() throws Exception {
        final List<PreliminaryReport> latestRevisions = persistReportsWithRevisions();

        final List<PreliminaryReport> result = dao.findAll();
        assertEquals(latestRevisions.size(), result.size());
        boolean found;
        for (PreliminaryReport res : result) {
            found = false;
            for (PreliminaryReport latestRevision : latestRevisions) {
                if (res.getUri().equals(latestRevision.getUri())) {
                    found = true;
                    break;
                }
            }
            assertTrue(found);
        }
    }

    private List<PreliminaryReport> persistReportsWithRevisions() {
        final List<PreliminaryReport> result = new ArrayList<>(2);
        final PreliminaryReport repOneRevOne = initPreliminaryReportWithTypeAssessment(eventType);
        repOneRevOne.setRevision(Constants.INITIAL_REVISION);
        final PreliminaryReport repOneRevTwo = new PreliminaryReport(repOneRevOne);
        repOneRevTwo.setRevision(repOneRevOne.getRevision() + 1);
        repOneRevTwo.setAuthor(author);
        result.add(repOneRevTwo);

        dao.persist(repOneRevOne);
        dao.persist(repOneRevTwo);

        final PreliminaryReport repTwoRevOne = initBasicValidReport();
        repTwoRevOne.setRevision(Constants.INITIAL_REVISION);
        final PreliminaryReport repTwoRevTwo = new PreliminaryReport(repTwoRevOne);
        repTwoRevTwo.setRevision(repTwoRevOne.getRevision() + 1);
        repTwoRevTwo.setAuthor(author);
        result.add(repTwoRevTwo);

        dao.persist(repTwoRevOne);
        dao.persist(repTwoRevTwo);

        return result;
    }

    @Test
    public void findByOccurrenceReturnsAllReportsForOccurrenceOrderedByRevision() {
        final List<PreliminaryReport> reports = persistReportsForOccurrence();

        final List<PreliminaryReport> result = dao.findByOccurrence(reports.get(0).getOccurrence());
        assertEquals(reports.size(), result.size());
        for (int i = 0; i < reports.size(); i++) {
            assertEquals(reports.get(i).getUri(), result.get(i).getUri());
            assertEquals(reports.get(i).getRevision(), result.get(i).getRevision());
        }
    }

    private List<PreliminaryReport> persistReportsForOccurrence() {
        final List<PreliminaryReport> reports = new ArrayList<>();
        final PreliminaryReport rOne = Generator
                .generatePreliminaryReport(Generator.ReportType.WITHOUT_TYPE_ASSESSMENTS);
        rOne.setAuthor(author);
        reports.add(rOne);
        dao.persist(rOne);
        final PreliminaryReport rTwo = new PreliminaryReport(rOne);
        rTwo.setRevision(Constants.INITIAL_REVISION + 1);
        rTwo.setAuthor(author);
        reports.add(rTwo);
        dao.persist(rTwo);
        final PreliminaryReport rThree = new PreliminaryReport(rTwo);
        rThree.setRevision(rTwo.getRevision() + 1);
        rThree.setAuthor(author);
        reports.add(rThree);
        dao.persist(rThree);
        return reports;
    }
}