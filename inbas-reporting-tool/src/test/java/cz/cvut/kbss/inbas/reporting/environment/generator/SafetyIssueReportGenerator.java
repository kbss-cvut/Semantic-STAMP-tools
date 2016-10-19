package cz.cvut.kbss.inbas.reporting.environment.generator;

import cz.cvut.kbss.inbas.reporting.dto.SafetyIssueReportDto;
import cz.cvut.kbss.inbas.reporting.model.Person;
import cz.cvut.kbss.inbas.reporting.model.safetyissue.SafetyIssue;
import cz.cvut.kbss.inbas.reporting.model.safetyissue.SafetyIssueReport;
import cz.cvut.kbss.inbas.reporting.util.IdentificationUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SafetyIssueReportGenerator {

    public static SafetyIssue generateSafetyIssue() {
        final SafetyIssue issue = new SafetyIssue();
        issue.setName("SafetyIssue" + Generator.randomInt());
        issue.setState(Generator.generateUri());
        issue.setSira(Generator.generateUri());
        return issue;
    }

    public static SafetyIssueReport generateSafetyIssueReport(boolean setAttributes, boolean generateMeasures) {
        final SafetyIssueReport report = new SafetyIssueReport();
        report.setSafetyIssue(generateSafetyIssue());
        report.setSummary("Safety issue report " + Generator.randomInt());
        if (setAttributes) {
            Generator.setReportAttributes(report);
        }
        if (generateMeasures) {
            report.setCorrectiveMeasures(Generator.generateCorrectiveMeasureRequests());
        }
        return report;
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
            r.setDateCreated(new Date());
            r.setAuthor(author);
            chain.add(r);
        }
        return chain;
    }

    public static SafetyIssue generateSafetyIssueWithFactorGraph() {
        final SafetyIssue issue = generateSafetyIssue();
        issue.setUri(Generator.generateUri());
        final int maxDepth = Generator.randomInt(5);
        final int childCount = Generator.randomInt(5);
        OccurrenceReportGenerator.generateChildEvents(issue, 0, maxDepth, childCount);
        return issue;
    }

    public static SafetyIssueReportDto generateSafetyIssueReportDto() {
        final SafetyIssueReportDto dto = new SafetyIssueReportDto();
        dto.setUri(Generator.generateUri());
        dto.setKey(IdentificationUtils.generateKey());
        dto.setFileNumber(IdentificationUtils.generateFileNumber());
        dto.setAuthor(Generator.getPerson());
        return dto;
    }
}
