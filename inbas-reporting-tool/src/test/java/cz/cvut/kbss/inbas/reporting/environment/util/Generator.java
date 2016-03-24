package cz.cvut.kbss.inbas.reporting.environment.util;

import cz.cvut.kbss.inbas.reporting.model.*;
import cz.cvut.kbss.inbas.reporting.model.reports.*;
import cz.cvut.kbss.inbas.reporting.model.reports.incursions.Intruder;
import cz.cvut.kbss.inbas.reporting.model.reports.incursions.LowVisibilityProcedure;
import cz.cvut.kbss.inbas.reporting.model.reports.incursions.RunwayIncursion;

import java.net.URI;
import java.util.*;

public class Generator {

    public static final String USERNAME = "halsey@unsc.org";
    public static final String PASSWORD = "john117";

    private static Random random = new Random();

    private Generator() {
        throw new AssertionError();
    }

    public static Occurrence generateOccurrence() {
        final Occurrence occurrence = new Occurrence();
        occurrence.setName(UUID.randomUUID().toString());
        return occurrence;
    }

    public static Person getPerson() {
        final Person person = new Person();
        person.setFirstName("Catherine");
        person.setLastName("Halsey");
        person.setUsername(USERNAME);
        person.setPassword(PASSWORD);
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
        report.setOccurrenceCategory(getEventType());
        report.getOccurrence().transitionToPhase(ReportingPhase.PRELIMINARY);
        report.setOccurrenceStart(new Date(System.currentTimeMillis() - 10000));
        report.setOccurrenceEnd(new Date());
        report.setAuthor(getPerson());
        report.setLastEdited(new Date());
        report.setLastEditedBy(getPerson());
        report.setFileNumber(System.currentTimeMillis());
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

    public static EventType getEventType() {
        return new EventType(URI.create(
                "http://onto.fel.cvut.cz/ontologies/eccairs-1.3.0.8/V-24-1-31-31-14-390-2000000-2200000-2200110"),
                "2200110 - Incursions generally");
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
        report.setOccurrence(generateOccurrence());
        report.setOccurrenceCategory(Generator.getEventType());
        report.setFileNumber(System.currentTimeMillis());
        final Date start = new Date(System.currentTimeMillis() - 10000);
        report.setOccurrenceStart(start);
        final Date end = new Date();
        report.setOccurrenceEnd(end);
        report.setAuthor(getPerson());

        final Factor root = new Factor();
        root.setUri(URI.create("http://krizik.felk.cvut.cz/ontologies/inbas-2015#Factor_instance0"));
        root.setStartTime(start);
        root.setEndTime(end);
        report.setRootFactor(root);

        final Factor childOne = new Factor();
        childOne.setUri(URI.create("http://krizik.felk.cvut.cz/ontologies/inbas-2015#Factor_instance1"));
        childOne.setStartTime(new Date(start.getTime() + 100));
        childOne.setEndTime(end);
        root.addChild(childOne);

        final Factor childTwo = new Factor();
        childTwo.setUri(URI.create("http://krizik.felk.cvut.cz/ontologies/inbas-2015#Factor_instance2"));
        childTwo.setStartTime(new Date(start.getTime() + 200));
        childTwo.setEndTime(end);
        root.addChild(childTwo);
        childTwo.addCause(childOne);    // childOne causes childTwo

        final Factor childOneOne = new Factor();
        childOneOne.setUri(URI.create("http://krizik.felk.cvut.cz/ontologies/inbas-2015#Factor_instance11"));
        childOneOne.setStartTime(new Date(start.getTime() + 100));
        childOneOne.setEndTime(end);
        childOne.addChild(childOneOne);

        final Factor childTwoOne = new Factor();
        childTwoOne.setUri(URI.create("http://krizik.felk.cvut.cz/ontologies/inbas-2015#Factor_instance21"));
        childTwoOne.setStartTime(new Date(start.getTime() + 200));
        childTwoOne.setEndTime(end);
        childTwo.addChild(childTwoOne);
        childOneOne.addMitigatingFactor(childTwoOne);   // childTwoOne mitigates childOneOne

        return report;
    }

    public static InvestigationReport generateMinimalInvestigation() {
        final InvestigationReport report = new InvestigationReport();
        report.setSeverityAssessment(OccurrenceSeverity.INCIDENT);
        report.setOccurrence(generateOccurrence());
        report.setOccurrenceCategory(getEventType());
        report.setCreated(new Date());
        final Date start = new Date(System.currentTimeMillis() - 10000);
        report.setOccurrenceStart(start);
        final Date end = new Date();
        report.setOccurrenceEnd(end);
        report.setAuthor(getPerson());
        report.setFileNumber(System.currentTimeMillis());

        final Factor root = new Factor();
        root.setStartTime(start);
        root.setEndTime(end);
        report.setRootFactor(root);
        return report;
    }

    /**
     * No cause/mitigate relationships, just the hierarchy.
     */
    public static InvestigationReport generateInvestigationWithFactorHierarchy() {
        final InvestigationReport report = generateMinimalInvestigation();
        final Date start = report.getOccurrenceStart();
        final Date end = report.getOccurrenceEnd();

        final Factor childOne = new Factor();
        childOne.setStartTime(start);
        childOne.setEndTime(end);
        report.getRootFactor().addChild(childOne);

        final Factor childTwo = new Factor();
        childTwo.setStartTime(start);
        childTwo.setEndTime(end);
        report.getRootFactor().addChild(childTwo);

        final Factor childOneOne = new Factor();
        childOneOne.setStartTime(start);
        childOneOne.setEndTime(end);
        childOne.addChild(childOneOne);

        final Factor childOneOneOne = new Factor();
        childOneOneOne.setStartTime(start);
        childOneOneOne.setEndTime(end);
        childOneOne.addChild(childOneOneOne);
        return report;
    }

    public static int randomInt(int upperBound) {
        int rand;
        do {
            rand = random.nextInt(upperBound);
        } while (rand == 0);
        return rand;
    }
}
