package cz.cvut.kbss.inbas.audit.environment.util;

import cz.cvut.kbss.inbas.audit.model.Aircraft;
import cz.cvut.kbss.inbas.audit.model.Location;
import cz.cvut.kbss.inbas.audit.model.Occurrence;
import cz.cvut.kbss.inbas.audit.model.Person;
import cz.cvut.kbss.inbas.audit.model.reports.*;
import cz.cvut.kbss.inbas.audit.model.reports.incursions.Intruder;
import cz.cvut.kbss.inbas.audit.model.reports.incursions.LowVisibilityProcedure;
import cz.cvut.kbss.inbas.audit.model.reports.incursions.RunwayIncursion;

import java.net.URI;
import java.util.*;

public class Generator {

    public static final String USERNAME = "halsey@unsc.org";

    private Generator() {
        throw new AssertionError();
    }

    public static Occurrence generateOccurrence() {
        final Occurrence occurrence = new Occurrence();
        occurrence.setName(UUID.randomUUID().toString());
        occurrence.setStartTime(new Date(System.currentTimeMillis() - 10000));
        occurrence.setEndTime(new Date());
        return occurrence;
    }

    public static Person getPerson() {
        final Person person = new Person();
        person.setFirstName("Catherine");
        person.setLastName("Halsey");
        person.setUsername(USERNAME);
        person.setPassword("john117");
        person.generateUri();
        return person;
    }

    public enum ReportType {
        WITHOUT_TYPE_ASSESSMENTS, WITH_TYPE_ASSESSMENTS
    }

    public static PreliminaryReport generatePreliminaryReport(ReportType type) {
        switch (type) {
            case WITHOUT_TYPE_ASSESSMENTS:
                return reportWithoutTypeAssessments();
            case WITH_TYPE_ASSESSMENTS:
                return reportWithTypeAssessments();
            default:
                throw new IllegalArgumentException();
        }
    }

    private static PreliminaryReport reportWithoutTypeAssessments() {
        final PreliminaryReport report = new PreliminaryReport();
        report.setOccurrence(generateOccurrence());
        report.setAuthor(getPerson());
        report.setLastEdited(new Date());
        report.setLastEditedBy(getPerson());
        report.setSeverityAssessment(OccurrenceSeverity.INCIDENT);
        final InitialReport irOne = new InitialReport("Test initial report one.");
        final InitialReport irTwo = new InitialReport("Test initial report two.");
        report.setInitialReports(new HashSet<>(Arrays.asList(irOne, irTwo)));
        final CorrectiveMeasure crOne = new CorrectiveMeasure("Test corrective measure one.");
        final CorrectiveMeasure crTwo = new CorrectiveMeasure("Test corrective measure two.");
        report.setCorrectiveMeasures(new HashSet<>(Arrays.asList(crOne, crTwo)));
        report.setSummary("Test preliminary report summary.");
        return report;
    }

    private static PreliminaryReport reportWithTypeAssessments() {
        final PreliminaryReport report = reportWithoutTypeAssessments();
        final Set<EventTypeAssessment> typeAssessments = new HashSet<>();
        final EventTypeAssessment etAOne = new EventTypeAssessment();
        etAOne.setDescription("Event type");
        etAOne.setEventType(new EventType(URI.create(
                "http://onto.fel.cvut.cz/ontologies/eccairs-1.3.0.8/V-24-1-31-31-14-390-2000000-2200000-2200110"),
                "2200110 - Incursions generally"));
        typeAssessments.add(etAOne);
        final EventTypeAssessment etATwo = new EventTypeAssessment();
        etATwo.setEventType(new EventType(URI.create(
                "http://onto.fel.cvut.cz/ontologies/eccairs-1.3.0.8/V-24-1-31-31-14-390-2000000-2200000-2200100"),
                "2200100 - Runway incursions"));
        final RunwayIncursion rwi = new RunwayIncursion();
        rwi.setLowVisibilityProcedure(LowVisibilityProcedure.CAT_I);
        rwi.setLocation(new Location("LKPR31"));
        final Intruder intruder = new Intruder();
        final Aircraft aircraft = new Aircraft();
        aircraft.setCallSign("OK123-32");
        aircraft.setRegistration("OK123");
        aircraft.setStateOfRegistry("CZ");
        intruder.setAircraft(aircraft);
        rwi.setIntruder(intruder);
        etATwo.setRunwayIncursion(rwi);
        typeAssessments.add(etATwo);
        report.setTypeAssessments(typeAssessments);
        return report;
    }

    public static InvestigationReport generateInvestigationWithCausesAndMitigatingFactors() throws Exception {
        final InvestigationReport report = new InvestigationReport();
        report.setUri(URI.create("http://krizik.felk.cvut.cz/ontologies/inbas-2015#Investigation_instance319360066"));
        report.setSeverityAssessment(OccurrenceSeverity.INCIDENT);
        final Date start = new Date(System.currentTimeMillis() - 10000);
        final Date end = new Date();

        final Factor root = new Factor();
        root.setUri(URI.create("http://krizik.felk.cvut.cz/ontologies/inbas-2015#Factor_instance0"));
        root.setStartTime(start);
        root.setEndTime(end);
        report.setRootFactor(root);

        final Factor childOne = new Factor();
        childOne.setUri(URI.create("http://krizik.felk.cvut.cz/ontologies/inbas-2015#Factor_instance1"));
        childOne.setStartTime(start);
        childOne.setEndTime(end);
        root.addChild(childOne);

        final Factor childTwo = new Factor();
        childTwo.setUri(URI.create("http://krizik.felk.cvut.cz/ontologies/inbas-2015#Factor_instance2"));
        childTwo.setStartTime(start);
        childTwo.setEndTime(end);
        root.addChild(childTwo);
        childTwo.addCause(childOne);    // childOne causes childTwo

        final Factor childOneOne = new Factor();
        childOneOne.setUri(URI.create("http://krizik.felk.cvut.cz/ontologies/inbas-2015#Factor_instance11"));
        childOneOne.setStartTime(start);
        childOneOne.setEndTime(end);
        childOne.addChild(childOneOne);

        final Factor childTwoOne = new Factor();
        childTwoOne.setUri(URI.create("http://krizik.felk.cvut.cz/ontologies/inbas-2015#Factor_instance21"));
        childTwoOne.setStartTime(start);
        childTwoOne.setEndTime(end);
        childTwo.addChild(childTwoOne);
        childOneOne.addMitigatingFactor(childTwoOne);   // childTwoOne mitigates childOneOne

        return report;
    }
}
