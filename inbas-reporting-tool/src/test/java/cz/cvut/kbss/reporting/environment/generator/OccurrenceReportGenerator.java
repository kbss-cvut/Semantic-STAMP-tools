package cz.cvut.kbss.reporting.environment.generator;

import cz.cvut.kbss.reporting.model.*;
import cz.cvut.kbss.reporting.model.util.factorgraph.FactorGraphItem;

import java.net.URI;
import java.util.*;

public class OccurrenceReportGenerator {

    public static final URI BARRIER_EFFECTIVE = URI
            .create("http://onto.fel.cvut.cz/ontologies/arms/sira/barrier-effectiveness/effective");
    public static final URI BARRIER_LIMITED = URI
            .create("http://onto.fel.cvut.cz/ontologies/arms/sira/barrier-effectiveness/limited");
    public static final URI BARRIER_MINIMAL = URI
            .create("http://onto.fel.cvut.cz/ontologies/arms/sira/barrier-effectiveness/minimal");
    public static final URI BARRIER_NOT_EFFECTIVE = URI
            .create("http://onto.fel.cvut.cz/ontologies/arms/sira/barrier-effectiveness/not-effective");
    public static final URI ACCIDENT_NEGLIGIBLE = URI
            .create("http://onto.fel.cvut.cz/ontologies/arms/sira/accident-outcome/negligible");
    public static final URI ACCIDENT_MINOR = URI
            .create("http://onto.fel.cvut.cz/ontologies/arms/sira/accident-outcome/minor");
    public static final URI ACCIDENT_MAJOR = URI
            .create("http://onto.fel.cvut.cz/ontologies/arms/sira/accident-outcome/major");
    public static final URI ACCIDENT_CATASTROPHIC = URI
            .create("http://onto.fel.cvut.cz/ontologies/arms/sira/accident-outcome/catastrophic");

    /**
     * Generates occurrence report.
     * <p>
     * Revision is set to 1, {@link Generator#getPerson()} is used as author.
     *
     * @param setAttributes Whether to set attributes which should be normally set by a service, e.g. author.
     * @return OccurrenceReport
     */
    public static OccurrenceReport generateOccurrenceReport(boolean setAttributes) {
        final OccurrenceReport report = new OccurrenceReport();
        report.setOccurrence(generateOccurrence());
        report.setSummary("Some random summary " + Generator.randomInt() + ".");
        if (setAttributes) {
            report.setBarrierEffectiveness(BARRIER_EFFECTIVE);
            report.setAccidentOutcome(ACCIDENT_NEGLIGIBLE);
            report.setSeverityAssessment(
                    URI.create("http://onto.fel.cvut.cz/ontologies/eccairs/aviation-3.4.0.2/vl-a-431/v-100"));
            report.setAuthor(Generator.getPerson());
            report.setDateCreated(new Date());
            report.setFileNumber((long) Generator.randomInt(Integer.MAX_VALUE));
            report.setRevision(1);
        }
        return report;
    }

    public static OccurrenceReport generateOccurrenceReportWithFactorGraph() {
        final OccurrenceReport report = generateOccurrenceReport(true);
        final Event childOne = new Event();
        childOne.setStartTime(report.getOccurrence().getStartTime());
        childOne.setEndTime(report.getOccurrence().getEndTime());
        childOne.setEventTypes(Collections.singleton(Generator.generateEventType()));
        report.getOccurrence().addChild(childOne);
        final Event childOneOne = new Event();
        childOneOne.setStartTime(report.getOccurrence().getStartTime());
        childOneOne.setEndTime(report.getOccurrence().getEndTime());
        childOneOne.setEventTypes(Collections.singleton(Generator.generateEventType()));
        childOne.addChild(childOneOne);
        final Event fOne = new Event();
        fOne.setStartTime(report.getOccurrence().getStartTime());
        fOne.setEndTime(report.getOccurrence().getEndTime());
        fOne.setEventTypes(Collections.singleton(Generator.generateEventType()));
        final Factor f = new Factor();
        f.addType(Generator.randomFactorType());
        f.setEvent(fOne);
        report.getOccurrence().addFactor(f);
        final Event fOneChildOne = new Event();
        fOneChildOne.setStartTime(report.getOccurrence().getStartTime());
        fOneChildOne.setEndTime(report.getOccurrence().getEndTime());
        fOneChildOne.setEventTypes(Collections.singleton(Generator.generateEventType()));
        fOne.addChild(fOneChildOne);
        return report;
    }

    /**
     * Generates chain of OccurrenceReport instances with the same file number.
     *
     * @param author Report author, for all reports
     * @return The generated chain
     */
    public static List<OccurrenceReport> generateOccurrenceReportChain(Person author) {
        final OccurrenceReport first = generateOccurrenceReport(true);
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

    public static Occurrence generateOccurrence() {
        final Occurrence occurrence = new Occurrence();
        occurrence.setName(UUID.randomUUID().toString());
        final URI type = Generator.generateEventType();
        occurrence.setEventTypes(Collections.singleton(type));
        occurrence.setLocation("LKPR" + Generator.randomInt(30));
        occurrence.getTypes().add(type.toString());
        occurrence.setStartTime(new Date(System.currentTimeMillis() - 100000));
        occurrence.setEndTime(new Date());
        return occurrence;
    }

    public static Occurrence generateOccurrenceWithDescendantEvents() {
        final Occurrence occurrence = generateOccurrence();
        occurrence.setUri(URI.create("http://rootOccurrence"));
        final int maxDepth = Generator.randomInt(5);
        final int childCount = Generator.randomInt(5);
        generateChildEvents(occurrence, 0, maxDepth, childCount);
        return occurrence;
    }

    static void generateChildEvents(FactorGraphItem parent, int depth, int maxDepth, int childCount) {
        if (depth >= maxDepth) {
            return;
        }
        parent.setChildren(new LinkedHashSet<>());
        for (int i = 0; i < childCount; i++) {
            final Event child = generateEvent();
            child.setIndex(i);
            parent.getChildren().add(child);
            generateChildEvents(child, depth + 1, maxDepth, childCount);
        }
    }

    public static Event generateEvent() {
        final Event event = new Event();
        event.setStartTime(new Date());
        event.setEndTime(new Date());
        event.setUri(URI.create(Vocabulary.s_c_Event + "-instance" + Generator.randomInt()));
        event.setEventTypes(Collections.singleton(Generator.generateEventType()));
        return event;
    }

    /**
     * Generates factors for graph with the specified root.
     * <p>
     * For simplicity, factors are generated only for the root and several of its children (if present), the generator
     * does not descend deeper into the hierarchy.
     *
     * @param graphRoot Root of the factor graph
     */
    public static void generateFactors(FactorGraphItem graphRoot) {
        final Event e1 = generateEvent();
        final Factor f1 = new Factor();
        f1.setEvent(e1);
        f1.addType(Generator.randomFactorType());
        graphRoot.addFactor(f1);
        final Event e2 = generateEvent();
        e2.setIndex(0);
        e1.addChild(e2);
        final Event e3 = generateEvent();
        e3.setIndex(1);
        e1.addChild(e3);
        final Factor f2 = new Factor();
        f2.setEvent(e2);
        f2.addType(Generator.randomFactorType());
        e3.addFactor(f2);
        if (graphRoot.getChildren() != null && graphRoot.getChildren().size() > 2) {
            final List<Event> lst = new ArrayList<>(graphRoot.getChildren());
            final Event start = lst.get(Generator.randomIndex(lst));
            final Event end = lst.get(Generator.randomIndex(lst));
            if (start != end) {
                final Factor f3 = new Factor();
                f3.addType(Generator.randomFactorType());
                f3.setEvent(start);
                end.addFactor(f3);
            }
        }
    }
}
