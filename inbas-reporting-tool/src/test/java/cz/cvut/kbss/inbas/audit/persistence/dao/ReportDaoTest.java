package cz.cvut.kbss.inbas.audit.persistence.dao;

import cz.cvut.kbss.inbas.audit.model.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author ledvima1
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class ReportDaoTest {

    private static EventType eventType;
    private static Organization organization;

    @Autowired
    private ReportDao dao;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        eventType = new EventType(URI.create(
                "http://onto.fel.cvut.cz/ontologies/2014/ECCAIRS_Aviation_1.3.0.12/eccairs-events-390#category_2200101"));
        eventType.setName("Runway incursion by an aircraft");
        organization = new Organization("CSA");
        organization.setUri(URI.create("http://onto.fel.cvut.cz/ontologies/2014/inbas/organizations#CSA"));
    }

    @Test
    public void persistenceInitialization() throws Exception {
        final Collection<EventReport> res = dao.findAll(EventReport.class);
        assertNotNull(res);
    }

    @Test
    public void persistingReportsWithSameEventTypeSavesItFirstTimeAndReusesLater() throws Exception {
        final EventReport rOne = initEventReportWithTypeAssessment(eventType);
        dao.persist(rOne);

        final EventReport rTwo = initEventReportWithTypeAssessment(eventType);
        dao.persist(rTwo);

        final EventReport resOne = dao.findByUri(EventReport.class, rOne.getUri());
        assertNotNull(resOne);
        assertEquals(1, resOne.getTypeAssessments().size());
        final EventReport resTwo = dao.findByUri(EventReport.class, rTwo.getUri());
        assertNotNull(resTwo);
        assertEquals(1, resTwo.getTypeAssessments().size());
        assertEquals(resOne.getTypeAssessments().iterator().next().getEventType(),
                resTwo.getTypeAssessments().iterator().next().getEventType());
    }

    private EventReport initEventReportWithTypeAssessment(EventType eventType) {
        final EventReport rOne = new EventReport();
        final EventTypeAssessment typeAssessment = new EventTypeAssessment();

        typeAssessment.setEventType(eventType);
        rOne.setTypeAssessments(Collections.singleton(typeAssessment));
        return rOne;
    }

    @Test
    public void persistingPersonIntruderFromExistingOrganizationUsesTheExistingOne() throws Exception {
        EventReport rOne = persistReportWithClearedAircraftAndOrganization();

        final EventReport rTwo = initEventReportWithTypeAssessment(eventType);
        final EventTypeAssessment assTwo = rTwo.getTypeAssessments().iterator().next();
        final RunwayIncursion incursionTwo = new RunwayIncursion();
        final PersonIntruder intruder = new PersonIntruder();
        intruder.setOrganization(organization);
        incursionTwo.setIntruder(new Intruder(intruder));
        assTwo.setRunwayIncursion(incursionTwo);
        dao.persist(rTwo);

        final EventReport resOne = dao.findByUri(EventReport.class, rOne.getUri());
        final EventTypeAssessment typeOne = resOne.getTypeAssessments().iterator().next();
        final Aircraft aircraft = typeOne.getRunwayIncursion().getClearedAircraft();
        final Organization org = aircraft.getOperator();
        assertEquals(organization, org);
        final EventReport resTwo = dao.findByUri(EventReport.class, rTwo.getUri());
        final EventTypeAssessment typeTwo = resTwo.getTypeAssessments().iterator().next();
        final PersonIntruder person = typeTwo.getRunwayIncursion().getIntruder().getPerson();
        assertEquals(organization, person.getOrganization());
    }

    private EventReport persistReportWithClearedAircraftAndOrganization() {
        final EventReport rOne = initEventReportWithTypeAssessment(eventType);
        final EventTypeAssessment ass = rOne.getTypeAssessments().iterator().next();
        final Aircraft cleared = new Aircraft();
        cleared.setOperator(organization);
        cleared.setOperationType("passenger");
        final RunwayIncursion incursion = new RunwayIncursion();
        incursion.setClearedAircraft(cleared);
        ass.setRunwayIncursion(incursion);
        dao.persist(rOne);
        return rOne;
    }

    @Test
    public void persistingVehicleIntruderFromExistingOrganizationUsesTheExistingOne() {
        EventReport rOne = persistReportWithClearedAircraftAndOrganization();

        final EventReport rTwo = initEventReportWithTypeAssessment(eventType);
        final EventTypeAssessment assTwo = rTwo.getTypeAssessments().iterator().next();
        final RunwayIncursion incursionTwo = new RunwayIncursion();
        final Vehicle intruder = new Vehicle();
        intruder.setOrganization(organization);
        incursionTwo.setIntruder(new Intruder(intruder));
        assTwo.setRunwayIncursion(incursionTwo);
        dao.persist(rTwo);

        final EventReport resOne = dao.findByUri(EventReport.class, rOne.getUri());
        final EventTypeAssessment typeOne = resOne.getTypeAssessments().iterator().next();
        final Aircraft aircraft = typeOne.getRunwayIncursion().getClearedAircraft();
        final Organization org = aircraft.getOperator();
        assertEquals(organization, org);
        final EventReport resTwo = dao.findByUri(EventReport.class, rTwo.getUri());
        final EventTypeAssessment typeTwo = resTwo.getTypeAssessments().iterator().next();
        final Vehicle vehicle = typeTwo.getRunwayIncursion().getIntruder().getVehicle();
        assertEquals(organization, vehicle.getOrganization());
    }

    @Test
    public void persistingAircraftIntruderFromExistingOrganizationUsesTheExistingOne() {
        EventReport rOne = persistReportWithClearedAircraftAndOrganization();

        final EventReport rTwo = initEventReportWithTypeAssessment(eventType);
        final EventTypeAssessment assTwo = rTwo.getTypeAssessments().iterator().next();
        final RunwayIncursion incursionTwo = new RunwayIncursion();
        final Aircraft intruder = new Aircraft();
        intruder.setOperator(organization);
        incursionTwo.setIntruder(new Intruder(intruder));
        assTwo.setRunwayIncursion(incursionTwo);
        dao.persist(rTwo);

        final EventReport resOne = dao.findByUri(EventReport.class, rOne.getUri());
        final EventTypeAssessment typeOne = resOne.getTypeAssessments().iterator().next();
        final Aircraft clearedAircraft = typeOne.getRunwayIncursion().getClearedAircraft();
        final Organization org = clearedAircraft.getOperator();
        assertEquals(organization, org);
        final EventReport resTwo = dao.findByUri(EventReport.class, rTwo.getUri());
        final EventTypeAssessment typeTwo = resTwo.getTypeAssessments().iterator().next();
        final Aircraft aircraft = typeTwo.getRunwayIncursion().getIntruder().getAircraft();
        assertEquals(organization, aircraft.getOperator());
    }
}