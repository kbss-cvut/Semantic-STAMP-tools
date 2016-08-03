package cz.cvut.kbss.inbas.reporting.environment.generator;

import cz.cvut.kbss.inbas.reporting.model.AbstractReport;
import cz.cvut.kbss.inbas.reporting.model.Person;
import cz.cvut.kbss.inbas.reporting.model.safetyissue.CorrectiveMeasure;
import cz.cvut.kbss.inbas.reporting.model.safetyissue.SafetyIssue;
import cz.cvut.kbss.inbas.reporting.model.safetyissue.SafetyIssueReport;

import java.util.*;

public class SafetyIssueReportGenerator {

    public static SafetyIssue generateSafetyIssue() {
        final SafetyIssue issue = new SafetyIssue();
        issue.setName("SafetyIssue" + Generator.randomInt());
        return issue;
    }

    public static SafetyIssueReport generateSafetyIssueReport(boolean setAttributes, boolean generateMeasures) {
        final SafetyIssueReport report = new SafetyIssueReport();
        report.setSafetyIssue(generateSafetyIssue());
        report.setSummary("Safety issue report " + Generator.randomInt());
        if (setAttributes) {
            setReportAttributes(report);
        }
        if (generateMeasures) {
            report.setCorrectiveMeasures(new HashSet<>());
            for (int i = 0; i < Generator.randomInt(5, 10); i++) {
                final CorrectiveMeasure measure = new CorrectiveMeasure();
                measure.setDescription("Safety issue corrective measure " + i);
                measure.setDeadline(new Date());
                if (Generator.randomBoolean()) {
                    measure.setResponsiblePersons(Collections.singleton(Generator.getPerson()));
                } else {
                    measure.setResponsibleOrganizations(Collections.singleton(Generator.generateOrganization()));
                }
                report.getCorrectiveMeasures().add(measure);
            }
        }
        return report;
    }

    private static void setReportAttributes(AbstractReport report) {
        report.setAuthor(Generator.getPerson());
        report.setDateCreated(new Date());
        report.setFileNumber((long) Generator.randomInt(Integer.MAX_VALUE));
        report.setRevision(1);
    }

    public static List<SafetyIssueReport> generateSafetyIssueReportChain(Person author) {
        final int count = Generator.randomInt(2, 10);
        final List<SafetyIssueReport> chain = new ArrayList<>(count);
        final SafetyIssueReport orig = generateSafetyIssueReport(true, false);
        orig.setAuthor(author);
        chain.add(orig);
        for (int i = 1; i < count; i++) {
            final SafetyIssueReport r = new SafetyIssueReport(orig);
            r.setRevision(orig.getRevision() + i);
            r.setAuthor(author);
            chain.add(r);
        }
        return chain;
    }
}
