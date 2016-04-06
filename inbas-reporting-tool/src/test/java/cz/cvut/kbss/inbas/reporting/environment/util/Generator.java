package cz.cvut.kbss.inbas.reporting.environment.util;

import cz.cvut.kbss.inbas.reporting.model_new.*;
import cz.cvut.kbss.inbas.reporting.model_new.arms.AccidentOutcome;
import cz.cvut.kbss.inbas.reporting.model_new.arms.BarrierEffectiveness;

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

    /**
     * Generates an {@link EventType} instance with (pseudo) unique name and URI.
     *
     * @return EventType instance
     */
    public static EventType generateEventType() {
        final EventType et = new EventType();
        final int rand = randomInt(100000);
        et.setName(rand + " - Runway Incursion by an Aircraft");
        et.setUri(URI.create("http://onto.fel.cvut.cz/ontologies/eccairs-3.4.0.2/vl-a-390/v-" + rand));
        return et;
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
     * Generates {@link Organization} with a random name.
     *
     * @return Organization
     */
    public static Organization generateOrganization() {
        final Organization org = new Organization();
        org.setName(UUID.randomUUID().toString());
        return org;
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
            report.setBarrierEffectiveness(BarrierEffectiveness.EFFECTIVE);
            report.setAccidentOutcome(AccidentOutcome.NEGLIGIBLE);
            report.setArmsIndex((short) 5);
        }
        return report;
    }

    /**
     * Generates chain of OccurrenceReport instances with the same file number.
     *
     * @param author Report author, for all reports
     * @return The generated chain
     */
    public static List<OccurrenceReport> generateOccurrenceReportChain(Person author) {
        final OccurrenceReport first = Generator.generateOccurrenceReport(true);
        first.setAuthor(author);
        final List<OccurrenceReport> reports = new ArrayList<>();
        reports.add(first);
        OccurrenceReport previous = first;
        for (int i = 0; i < Generator.randomInt(10); i++) {
            final OccurrenceReport newRev = new OccurrenceReport(previous);
            newRev.setAuthor(author);
            newRev.setRevision(previous.getRevision() + 1);
            newRev.setDateCreated(new Date());
            reports.add(newRev);
            previous = newRev;
        }
        return reports;
    }

    public static Set<CorrectiveMeasureRequest> generateCorrectiveMeasureRequests() {
        final Set<CorrectiveMeasureRequest> set = new HashSet<>();
        for (int i = 0; i < randomInt(10); i++) {
            final CorrectiveMeasureRequest cmr = new CorrectiveMeasureRequest();
            cmr.setDescription(UUID.randomUUID().toString());
            int j = randomInt(Integer.MAX_VALUE);
            switch (j % 3) {
                case 0:
                    cmr.setResponsiblePersons(Collections.singleton(getPerson()));
                    final Event evt = new Event();
                    evt.setType(generateEventType());
                    cmr.setBasedOnEvent(evt);
                    break;
                case 1:
                    cmr.setResponsibleOrganizations(Collections.singleton(generateOrganization()));
                    cmr.setBasedOnOccurrence(generateOccurrence());
                    cmr.getBasedOnOccurrence().setType(generateEventType());
                    break;
                case 2:
                    cmr.setResponsiblePersons(Collections.singleton(getPerson()));
                    cmr.setResponsibleOrganizations(Collections.singleton(generateOrganization()));
                    break;
            }
            set.add(cmr);
        }
        return set;
    }

    /**
     * Generates a (pseudo-)random integer between 0 and the specified upper bound.
     * <p>
     * <b>IMPORTANT</b>: The lower bound (0) is not included in the generator output, so the smallest number this
     * generator returns is 1.
     *
     * @param upperBound Upper bound of the generated number
     * @return Randomly generated integer
     */
    public static int randomInt(int upperBound) {
        int rand;
        do {
            rand = random.nextInt(upperBound);
        } while (rand == 0);
        return rand;
    }

    /**
     * Generates a (pseudo-) random integer.
     * <p>
     * This version has no bounds (aside from the integer range), so the returned number may be negative or zero.
     *
     * @return Randomly generated integer
     * @see #randomInt(int)
     */
    public static int randomInt() {
        return random.nextInt();
    }
}
