'use strict';

describe('Report factory', () => {

    var Constants = require('../../js/constants/Constants'),
        Generator = require('../environment/Generator').default,
        Vocabulary = require('../../js/constants/Vocabulary'),
        ReportFactory = require('../../js/model/ReportFactory');

    it('creates occurrence report for occurrence report Java class.', () => {
        var report = ReportFactory.createReport(Constants.OCCURRENCE_REPORT_JAVA_CLASS);
        expect(report.javaClass).toEqual(Constants.OCCURRENCE_REPORT_JAVA_CLASS);
    });

    it('creates occurrence report for occurrence report OWL class.', () => {
        var report = ReportFactory.createReport(Vocabulary.OCCURRENCE_REPORT);
        expect(report.javaClass).toEqual(Constants.OCCURRENCE_REPORT_JAVA_CLASS);
    });

    it('creates occurrence report with isEccairsReport method returning false', () => {
        let report = ReportFactory.createReport(Vocabulary.OCCURRENCE_REPORT);
        expect(report.isEccairsReport).toBeDefined();
        expect(report.isEccairsReport()).toBeFalsy();
    });

    it('creates safety issue report report for safety issue report Java class.', () => {
        var report = ReportFactory.createReport(Constants.SAFETY_ISSUE_REPORT_JAVA_CLASS);
        expect(report.javaClass).toEqual(Constants.SAFETY_ISSUE_REPORT_JAVA_CLASS);
    });

    it('creates safety issue report report for safety issue report OWL class.', () => {
        var report = ReportFactory.createReport(Vocabulary.SAFETY_ISSUE_REPORT);
        expect(report.javaClass).toEqual(Constants.SAFETY_ISSUE_REPORT_JAVA_CLASS);
    });

    it('creates safety issue based on report', () => {
        var basedOnReport = Generator.generateOccurrenceReport(),
            basedOn = basedOnReport.occurrence;
        basedOn.referenceId = 117;

        var report = ReportFactory.createReport(Vocabulary.SAFETY_ISSUE_REPORT, {
            basedOn: {
                event: basedOn,
                report: basedOnReport
            }
        });
        expect(report.safetyIssue.basedOn).toBeDefined();
    });

    it('creates safety issue report with copied factor graph.', () => {
        var basedOnReport = Generator.generateOccurrenceReport(),
            basedOn = basedOnReport.occurrence;
        basedOn.referenceId = 117;
        basedOnReport.factorGraph = {
            nodes: [basedOn.referenceId]
        };
        Array.prototype.push.apply(basedOnReport.factorGraph.nodes, Generator.generateFactorGraphNodes());
        basedOnReport.factorGraph.edges = Generator.generateFactorLinksForNodes(basedOnReport.factorGraph.nodes);
        var originalGraph = basedOnReport.factorGraph;

        var report = ReportFactory.createReport(Vocabulary.SAFETY_ISSUE_REPORT, {
            basedOn: {
                event: basedOn,
                report: basedOnReport
            }
        });
        expect(report.factorGraph).not.toBeNull();
        expect(report.factorGraph.nodes.length).toEqual(originalGraph.nodes.length);
        for (var i = 0; i < report.factorGraph.nodes.length; i++) {
            expect(report.factorGraph.nodes[i].uri).not.toBeDefined();
        }
        expect(report.factorGraph.edges.length).toEqual(originalGraph.edges.length);
    });

    it('creates safety issue which is open by default', () => {
        var report = ReportFactory.createReport(Vocabulary.SAFETY_ISSUE_REPORT);
        expect(report.safetyIssue.state).toEqual(Constants.SAFETY_ISSUE_STATE.OPEN);
    });

    it('creates audit report for audit report Java class', () => {
        var report = ReportFactory.createReport(Constants.AUDIT_REPORT_JAVA_CLASS);
        expect(report.javaClass).toEqual(Constants.AUDIT_REPORT_JAVA_CLASS);
    });

    it('creates audit report for audit report OWL class', () => {
        var report = ReportFactory.createReport(Vocabulary.AUDIT_REPORT);
        expect(report.javaClass).toEqual(Constants.AUDIT_REPORT_JAVA_CLASS);
    });

    it('sets audit start and end date', () => {
        var report = ReportFactory.createReport(Vocabulary.AUDIT_REPORT);
        expect(report.audit).toBeDefined();
        expect(report.audit.startDate).toBeDefined();
        expect(report.audit.endDate).toBeDefined();
    });
});
