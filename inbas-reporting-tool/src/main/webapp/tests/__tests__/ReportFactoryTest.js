'use strict';

describe('Report factory', () => {

    const Constants = require('../../js/constants/Constants'),
        Generator = require('../environment/Generator').default,
        Vocabulary = require('../../js/constants/Vocabulary'),
        ReportFactory = require('../../js/model/ReportFactory');

    it('creates occurrence report for occurrence report Java class.', () => {
        const report = ReportFactory.createReport(Constants.OCCURRENCE_REPORT_JAVA_CLASS);
        expect(report.javaClass).toEqual(Constants.OCCURRENCE_REPORT_JAVA_CLASS);
    });

    it('creates occurrence report for occurrence report OWL class.', () => {
        const report = ReportFactory.createReport(Vocabulary.OCCURRENCE_REPORT);
        expect(report.javaClass).toEqual(Constants.OCCURRENCE_REPORT_JAVA_CLASS);
    });

    it('creates occurrence report with isEccairsReport method returning false', () => {
        const report = ReportFactory.createReport(Vocabulary.OCCURRENCE_REPORT);
        expect(report.isEccairsReport).toBeDefined();
        expect(report.isEccairsReport()).toBeFalsy();
    });

    it('creates safety issue report report for safety issue report Java class.', () => {
        const report = ReportFactory.createReport(Constants.SAFETY_ISSUE_REPORT_JAVA_CLASS);
        expect(report.javaClass).toEqual(Constants.SAFETY_ISSUE_REPORT_JAVA_CLASS);
    });

    it('creates safety issue report report for safety issue report OWL class.', () => {
        const report = ReportFactory.createReport(Vocabulary.SAFETY_ISSUE_REPORT);
        expect(report.javaClass).toEqual(Constants.SAFETY_ISSUE_REPORT_JAVA_CLASS);
    });

    it('creates safety issue based on report', () => {
        const basedOnReport = Generator.generateOccurrenceReport(),
            basedOn = basedOnReport.occurrence;
        basedOn.referenceId = 117;

        const report = ReportFactory.createReport(Vocabulary.SAFETY_ISSUE_REPORT, {
            basedOn: {
                event: basedOn,
                report: basedOnReport
            }
        });
        expect(report.safetyIssue.basedOn).toBeDefined();
    });

    it('creates safety issue report with copied factor graph.', () => {
        const basedOnReport = Generator.generateOccurrenceReport(),
            basedOn = basedOnReport.occurrence;
        basedOn.referenceId = 117;
        basedOnReport.factorGraph = {
            nodes: [basedOn.referenceId]
        };
        Array.prototype.push.apply(basedOnReport.factorGraph.nodes, Generator.generateFactorGraphNodes());
        basedOnReport.factorGraph.edges = Generator.generateFactorLinksForNodes(basedOnReport.factorGraph.nodes);
        const originalGraph = basedOnReport.factorGraph;

        const report = ReportFactory.createReport(Vocabulary.SAFETY_ISSUE_REPORT, {
            basedOn: {
                event: basedOn,
                report: basedOnReport
            }
        });
        expect(report.factorGraph).not.toBeNull();
        expect(report.factorGraph.nodes.length).toEqual(originalGraph.nodes.length);
        for (let i = 0; i < report.factorGraph.nodes.length; i++) {
            expect(report.factorGraph.nodes[i].uri).not.toBeDefined();
        }
        expect(report.factorGraph.edges.length).toEqual(originalGraph.edges.length);
    });

    it('creates safety issue which is open by default', () => {
        const report = ReportFactory.createReport(Vocabulary.SAFETY_ISSUE_REPORT);
        expect(report.safetyIssue.state).toEqual(Constants.SAFETY_ISSUE_STATE.OPEN);
    });

    it('creates audit report for audit report Java class', () => {
        const report = ReportFactory.createReport(Constants.AUDIT_REPORT_JAVA_CLASS);
        expect(report.javaClass).toEqual(Constants.AUDIT_REPORT_JAVA_CLASS);
    });

    it('creates audit report for audit report OWL class', () => {
        const report = ReportFactory.createReport(Vocabulary.AUDIT_REPORT);
        expect(report.javaClass).toEqual(Constants.AUDIT_REPORT_JAVA_CLASS);
    });

    it('sets audit start and end date', () => {
        const report = ReportFactory.createReport(Vocabulary.AUDIT_REPORT);
        expect(report.audit).toBeDefined();
        expect(report.audit.startDate).toBeDefined();
        expect(report.audit.endDate).toBeDefined();
    });

    describe('addMethodsToReportInstance', () => {
        it('attaches utility methods to the specified object', () => {
            const report = {};
            ReportFactory.addMethodsToReportInstance(report);
            expect(report.isSafaReport).toBeDefined();
            expect(report.isEccairsReport).toBeDefined();
        });

        describe(' - added isEccairsReport method', () => {

            it('returns true for ECCAIRS report', () => {
                const report = Generator.generateOccurrenceReport();
                delete report.isEccairsReport;
                report.types = [Vocabulary.ECCAIRS_REPORT];
                ReportFactory.addMethodsToReportInstance(report);
                expect(report.isEccairsReport()).toBeTruthy();
            });

            it('returns false for non-ECCAIRS report', () => {
                const report = Generator.generateOccurrenceReport();
                delete report.isEccairsReport;
                report.types = [];
                ReportFactory.addMethodsToReportInstance(report);
                expect(report.isEccairsReport()).toBeFalsy();
            });
        });

        describe(' - added isSafaReport method', () => {

            it('returns true for SAFA audit report', () => {
                const report = Generator.generateAuditReport();
                delete report.isSafaReport;
                report.types = [Vocabulary.SAFA_REPORT];
                ReportFactory.addMethodsToReportInstance(report);
                expect(report.isSafaReport()).toBeTruthy();
            });

            it('returns false for non-SAFA report', () => {
                const report = Generator.generateAuditReport();
                delete report.isSafaReport;
                report.types = [];
                ReportFactory.addMethodsToReportInstance(report);
                expect(report.isSafaReport()).toBeFalsy();
            });
        });
    });
});
