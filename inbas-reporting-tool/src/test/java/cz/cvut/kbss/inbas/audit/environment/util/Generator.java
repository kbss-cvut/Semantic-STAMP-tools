package cz.cvut.kbss.inbas.audit.environment.util;

import cz.cvut.kbss.inbas.audit.model.Occurrence;
import cz.cvut.kbss.inbas.audit.model.Person;
import cz.cvut.kbss.inbas.audit.model.reports.CorrectiveMeasure;
import cz.cvut.kbss.inbas.audit.model.reports.InitialReport;
import cz.cvut.kbss.inbas.audit.model.reports.OccurrenceSeverity;
import cz.cvut.kbss.inbas.audit.model.reports.PreliminaryReport;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.UUID;

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
        WITHOUT_TYPE_ASSESSMENTS
    }

    public static PreliminaryReport generatePreliminaryReport(ReportType type) {
        switch (type) {
            case WITHOUT_TYPE_ASSESSMENTS:
                return reportWithoutTypeAssessments();
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
}
