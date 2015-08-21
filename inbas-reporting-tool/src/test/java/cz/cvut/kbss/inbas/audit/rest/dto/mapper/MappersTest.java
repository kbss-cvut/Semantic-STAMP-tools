package cz.cvut.kbss.inbas.audit.rest.dto.mapper;

import cz.cvut.kbss.inbas.audit.config.RestConfig;
import cz.cvut.kbss.inbas.audit.config.ServiceConfig;
import cz.cvut.kbss.inbas.audit.model.Aircraft;
import cz.cvut.kbss.inbas.audit.model.Location;
import cz.cvut.kbss.inbas.audit.model.Organization;
import cz.cvut.kbss.inbas.audit.model.reports.*;
import cz.cvut.kbss.inbas.audit.model.reports.incursions.Intruder;
import cz.cvut.kbss.inbas.audit.model.reports.incursions.LowVisibilityProcedure;
import cz.cvut.kbss.inbas.audit.model.reports.incursions.RunwayIncursion;
import cz.cvut.kbss.inbas.audit.rest.dto.model.GeneralEvent;
import cz.cvut.kbss.inbas.audit.rest.dto.model.incursion.AircraftIntruder;
import cz.cvut.kbss.inbas.audit.test.config.TestPersistenceConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;

import static org.junit.Assert.*;

/**
 * @author ledvima1
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RestConfig.class, ServiceConfig.class, TestPersistenceConfig.class})
public class MappersTest {

    @Autowired
    private ReportMapper reportMapper;

    @Test
    public void testMapAircraftToAircraftIntruder() {
        final Aircraft aircraft = initAircraft();
        final AircraftIntruder result = reportMapper.aircraftToAircraftIntruder(aircraft);
        assertNotNull(result);
        assertEquals(aircraft.getUri(), result.getUri());
        assertEquals(aircraft.getCallSign(), result.getCallSign());
        assertEquals(aircraft.getOperationType(), result.getOperationType());
        assertEquals(aircraft.getOperator(), result.getOperator());
        assertEquals(aircraft.getStateOfRegistry(), result.getStateOfRegistry());
        assertEquals(aircraft.getRegistration(), result.getRegistration());
    }

    private Aircraft initAircraft() {
        final Aircraft aircraft = new Aircraft();
        aircraft.setUri(URI.create("http://krizik.felk.cvut.cz/ontologies/inbas#aircraftTest"));
        aircraft.setCallSign("OK12345");
        aircraft.setOperationType("passenger flight");
        aircraft.setOperator(new Organization("CSA"));
        aircraft.setStateOfRegistry("CZ");
        aircraft.setRegistration("OK-123");
        return aircraft;
    }

    @Test
    public void eventTypeAssessmentWithRunwayIncursionIsMappedToRunwayIncursionDto() {
        final EventTypeAssessment assessment = initEventTypeAssessment(true);
        final cz.cvut.kbss.inbas.audit.rest.dto.model.EventTypeAssessment result = reportMapper
                .eventTypeAssessmentEventTypeAssessmentDto(assessment);
        assertNotNull(result);
        assertEquals(assessment.getRunwayIncursion().getUri(), result.getUri());
        assertTrue(result instanceof cz.cvut.kbss.inbas.audit.rest.dto.model.incursion.RunwayIncursion);
        final cz.cvut.kbss.inbas.audit.rest.dto.model.incursion.RunwayIncursion ri = (cz.cvut.kbss.inbas.audit.rest.dto.model.incursion.RunwayIncursion) result;
        assertEquals(assessment.getEventType().getId(), ri.getEventType().getId());
        assertTrue(ri.getIntruder() instanceof AircraftIntruder);
        assertEquals(assessment.getRunwayIncursion().getLocation(), ri.getLocation());
        assertEquals(assessment.getRunwayIncursion().getLowVisibilityProcedure(), ri.getLvp());
    }

    private EventTypeAssessment initEventTypeAssessment(boolean addRunwayIncursion) {
        final EventTypeAssessment eta = new EventTypeAssessment();
        eta.setUri(URI.create("http://krizik.felk.cvut.cz/ontologies/inbas#eta"));
        eta.setEventType(new EventType(URI.create(
                "http://onto.fel.cvut.cz/ontologies/2014/ECCAIRS_Aviation_1.3.0.12/eccairs-events-390#category_2200000")));
        if (addRunwayIncursion) {
            eta.setDescription("Runway incursion assessment.");
            final RunwayIncursion ri = new RunwayIncursion();
            ri.setUri(URI.create("http://krizik.felk.cvut.cz/ontologies/inbas#ri"));
            ri.setLocation(new Location());
            ri.setLowVisibilityProcedure(LowVisibilityProcedure.CAT_III);
            ri.setIntruder(new Intruder(initAircraft()));
            eta.setRunwayIncursion(ri);
        }
        return eta;
    }

    @Test
    public void eventTypeAssessmentWithoutRunwayIncursionIsMappedToGeneralEvent() {
        final EventTypeAssessment assessment = initEventTypeAssessment(false);
        final cz.cvut.kbss.inbas.audit.rest.dto.model.EventTypeAssessment result = reportMapper
                .eventTypeAssessmentEventTypeAssessmentDto(
                        assessment);
        assertNotNull(result);
        assertTrue(result instanceof GeneralEvent);
        final GeneralEvent ge = (GeneralEvent) result;
        assertEquals(assessment.getUri(), ge.getUri());
        assertEquals(assessment.getEventType(), ge.getEventType());
        assertEquals(assessment.getDescription(), ge.getDescription());
    }

    @Test
    public void testMapOccurrenceReportToOccurrenceReportDto() {
        final OccurrenceReport report = initOccurrenceReport();
        final cz.cvut.kbss.inbas.audit.rest.dto.model.OccurrenceReport result = reportMapper.occurrenceReportToOccurrenceReportDto(report);
        assertNotNull(result);
        assertEquals(report.getDescription(), result.getDescription());
        assertEquals(report.getSeverityAssessment(), result.getSeverityAssessment());
        assertEquals(report.getCreated(), result.getCreated());
        boolean riFound = false, geFound = false;
        for (cz.cvut.kbss.inbas.audit.rest.dto.model.EventTypeAssessment et : result.getTypeAssessments()) {
            if (et instanceof cz.cvut.kbss.inbas.audit.rest.dto.model.incursion.RunwayIncursion) {
                riFound = true;
                assertTrue(((cz.cvut.kbss.inbas.audit.rest.dto.model.incursion.RunwayIncursion) et).getIntruder() instanceof AircraftIntruder);
            } else if (et instanceof GeneralEvent) {
                geFound = true;
            }
        }
        assertTrue(riFound);
        assertTrue(geFound);
    }

    private OccurrenceReport initOccurrenceReport() {
        final OccurrenceReport or = new OccurrenceReport();
        or.setTypeAssessments(
                new HashSet<>(Arrays.asList(initEventTypeAssessment(true), initEventTypeAssessment(false))));
        or.setDescription("Something happened.");
        or.setCreated(new Date());
        or.setSeverityAssessment(new SeverityAssessment(OccurrenceSeverity.MAJOR_INCIDENT));
        or.setCorrectiveMeasures(Collections.singleton(new CorrectiveMeasure("Pilot was fired.")));
        return or;
    }
}
