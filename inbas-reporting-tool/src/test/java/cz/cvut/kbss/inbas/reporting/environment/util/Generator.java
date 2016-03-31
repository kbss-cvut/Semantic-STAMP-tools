package cz.cvut.kbss.inbas.reporting.environment.util;

import cz.cvut.kbss.inbas.reporting.model_new.Occurrence;
import cz.cvut.kbss.inbas.reporting.model_new.OccurrenceReport;
import cz.cvut.kbss.inbas.reporting.model_new.Person;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

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
        return person;
    }

    /**
     * Generates occurrence report.
     * <p>
     * Revision is set to 1, {@link #getPerson()} is used as author.
     *
     * @param setAttributes Whether to set attributes which should be normally set by a service, e.g. author.
     * @return OccurrenceReport
     */
    public static OccurrenceReport generateOccurrenceReport(boolean setAttributes) {
        final OccurrenceReport report = new OccurrenceReport();
        report.setOccurrence(generateOccurrence());
        report.setOccurrenceStart(new Date(System.currentTimeMillis() - 100000));
        report.setOccurrenceEnd(new Date());
        if (setAttributes) {
            report.setAuthor(getPerson());
            report.setDateCreated(new Date());
            report.setFileNumber((long) randomInt(Integer.MAX_VALUE));
            report.setRevision(1);
        }
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
