'use strict';

describe('ReportType', function () {

    const ReportType = require('../../js/model/ReportType'),
        Constants = require('../../js/constants/Constants'),
        Generator = require('../environment/Generator').default,
        ReportFactory = require('../../js/model/ReportFactory'),
        OccurrenceReportController = require('../../js/components/report/occurrence/OccurrenceReportController'),
        Vocabulary = require('../../js/constants/Vocabulary');

    it('returns default detail controller for new report when getDetailController is called', function () {
        const report = ReportFactory.createOccurrenceReport(),

            controller = ReportType.getDetailController(report);
        expect(controller).toEqual(OccurrenceReportController);
    });

    it('returns SAFA audit report instance for SAFA audit report', () => {
        const report = Generator.generateAuditReport();
        report.isSafaReport = () => {
            return true;
        };

        const result = ReportType.getReport(report);
        expect(result.getLabels().indexOf('report.safa.label')).not.toEqual(-1);
    });

    it('returns ECCAIRS report instance for ECCAIRS occurrence report', () => {
        const report = Generator.generateOccurrenceReport();
        report.isEccairsReport = () => {
            return true;
        };

        const result = ReportType.getReport(report);
        expect(result.getLabels().indexOf('report.eccairs.label')).not.toEqual(-1);
    });

    describe('- safety issue report - addBase()', () => {

        it('adds factor graph to a new instance', () => {
            const basedOnReport = generateSafetyIssueBase(),
                newBase = basedOnReport.occurrence,
                originalGraph = basedOnReport.factorGraph;
            let edge;

            let report = ReportFactory.createReport(Vocabulary.SAFETY_ISSUE_REPORT);
            report = ReportType.getReport(report);
            report.addBase(newBase, basedOnReport);
            expect(report.factorGraph).not.toBeNull();
            expect(report.factorGraph.nodes.length).toEqual(originalGraph.nodes.length);
            for (let i = 0, len = report.factorGraph.nodes.length; i < len; i++) {
                expect(report.factorGraph.nodes[i].uri).not.toBeDefined();
            }
            expect(report.safetyIssue.basedOn[0].types.indexOf(Vocabulary.OCCURRENCE)).not.toEqual(-1);
            expect(report.factorGraph.edges.length).toEqual(originalGraph.edges.length);
            for (let i = 0, len = report.factorGraph.edges.length; i < len; i++) {
                edge = report.factorGraph.edges[i];
                expect(report.factorGraph.nodes.indexOf(edge.from)).not.toEqual(-1);
                expect(report.factorGraph.nodes.indexOf(edge.to)).not.toEqual(-1);
            }
        });

        function generateSafetyIssueBase() {
            const basedOn = Generator.generateOccurrenceReport();
            basedOn.occurrence.referenceId = Generator.getRandomInt();
            basedOn.factorGraph = {
                nodes: [basedOn.occurrence.referenceId]
            };
            Array.prototype.push.apply(basedOn.factorGraph.nodes, Generator.generateFactorGraphNodes());
            basedOn.factorGraph.edges = Generator.generateFactorLinksForNodes(basedOn.factorGraph.nodes);
            return basedOn;
        }

        it('copies event types correctly when adding factor graph to new safety issue', () => {
            const basedOnReport = generateSafetyIssueBase(),
                newBase = basedOnReport.occurrence,
                originalGraph = basedOnReport.factorGraph;

            let report = ReportFactory.createReport(Vocabulary.SAFETY_ISSUE_REPORT);
            report = ReportType.getReport(report);
            report.addBase(newBase, basedOnReport);
            expect(report.factorGraph).not.toBeNull();
            expect(report.factorGraph.nodes.length).toEqual(originalGraph.nodes.length);
            for (let i = 0, len = report.factorGraph.nodes.length; i < len; i++) {
                expect(report.factorGraph.nodes[i].uri).not.toBeDefined();
                if (i > 0) {
                    expect(report.factorGraph.nodes[i].eventTypes).toEqual(basedOnReport.factorGraph.nodes[i].eventTypes);
                }
            }
        });

        it('adds another report as base', () => {
            const basedOnReport = generateSafetyIssueBase(),
                basedOn = basedOnReport.occurrence,
                newBaseReport = generateSafetyIssueBase(),
                newBase = newBaseReport.occurrence,
                originalFactorGraph = basedOnReport.factorGraph,
                newBaseFactorGraph = newBaseReport.factorGraph;
            let report, result, edge;

            report = ReportFactory.createReport(Vocabulary.SAFETY_ISSUE_REPORT, {
                basedOn: {
                    event: basedOn,
                    report: basedOnReport
                }
            });
            expect(report.factorGraph).not.toBeNull();
            report = ReportType.getReport(report);
            result = report.addBase(newBase, newBaseReport);
            expect(result).toBeTruthy();
            // The occurrence will be counted twice, but we need only one, which is transformed to safety issue (the
            // graph's root)
            expect(report.factorGraph.nodes.length).toEqual(originalFactorGraph.nodes.length + newBaseFactorGraph.nodes.length - 1);
            for (let i = 0, len = report.factorGraph.nodes.length; i < len; i++) {
                expect(report.factorGraph.nodes[i].uri).not.toBeDefined();
            }
            expect(report.factorGraph.edges.length).toEqual(originalFactorGraph.edges.length + newBaseFactorGraph.edges.length);
            for (let i = 0, len = report.factorGraph.edges.length; i < len; i++) {
                edge = report.factorGraph.edges[i];
                expect(report.factorGraph.nodes.indexOf(edge.from)).not.toEqual(-1);
                expect(report.factorGraph.nodes.indexOf(edge.to)).not.toEqual(-1);
            }
            for (let i = 0, len = report.safetyIssue.basedOn.length; i < len; i++) {
                expect(report.safetyIssue.basedOn[i].types.indexOf(Vocabulary.OCCURRENCE)).not.toEqual(-1);
            }
        });

        it('does not add base if it already exists in safety issue', () => {
            const basedOnReport = generateSafetyIssueBase(),
                basedOn = basedOnReport.occurrence,
                originalFactorGraph = basedOnReport.factorGraph;

            let report = ReportFactory.createReport(Vocabulary.SAFETY_ISSUE_REPORT, {
                basedOn: {
                    event: basedOn,
                    report: basedOnReport
                }
            });
            expect(report.factorGraph).toBeDefined();
            expect(report.factorGraph).not.toBeNull();
            report = ReportType.getReport(report);
            expect(report.factorGraph.nodes.length).toEqual(originalFactorGraph.nodes.length);
            expect(report.factorGraph.edges.length).toEqual(originalFactorGraph.edges.length);
            expect(report.safetyIssue.basedOn.length).toEqual(1);
            const result = report.addBase(basedOn);
            expect(result).toBeFalsy();
            expect(report.factorGraph.nodes.length).toEqual(originalFactorGraph.nodes.length);
            expect(report.factorGraph.edges.length).toEqual(originalFactorGraph.edges.length);
            expect(report.safetyIssue.basedOn.length).toEqual(1);
        });

        it('adds audit finding as base to a new instance', () => {
            const auditReport = Generator.generateAuditReport(),
                finding = auditReport.audit.findings[Generator.getRandomInt(auditReport.audit.findings.length)];
            let report = ReportFactory.createReport(Vocabulary.SAFETY_ISSUE_REPORT);
            report = ReportType.getReport(report);
            report.addBase(finding, auditReport);

            expect(report.factorGraph).not.toBeDefined();
            expect(report.safetyIssue.basedOn.length).toEqual(1);
            const base = report.safetyIssue.basedOn[0];
            expect(base.uri).toEqual(finding.uri);
            expect(base.javaClass).toEqual(Constants.AUDIT_FINDING_SAFETY_ISSUE_BASE_CLASS);
            expect(base.reportKey).toEqual(auditReport.key);
            expect(base.description).toEqual(finding.description);
            expect(base.level).toEqual(finding.level);
        });

        it('adds audit finding as base to existing issue with some base', () => {
            const basedOnReport = generateSafetyIssueBase(),
                basedOn = basedOnReport.occurrence;

            let report = ReportFactory.createReport(Vocabulary.SAFETY_ISSUE_REPORT, {
                basedOn: {
                    event: basedOn,
                    report: basedOnReport
                }
            });
            const auditReport = Generator.generateAuditReport(),
                finding = auditReport.audit.findings[Generator.getRandomInt(auditReport.audit.findings.length)];
            report = ReportType.getReport(report);
            const result = report.addBase(finding, auditReport);

            expect(result).toBeTruthy();
            expect(report.safetyIssue.basedOn.length).toEqual(2);
            expect(report.safetyIssue.basedOn[1].uri).toEqual(finding.uri);
        });

        it('adds finding factors to factor graph of new instance', () => {
            const auditReport = Generator.generateAuditReport(),
                finding = auditReport.audit.findings[Generator.getRandomInt(auditReport.audit.findings.length)];
            let report = ReportFactory.createReport(Vocabulary.SAFETY_ISSUE_REPORT);
            finding.factors = [];
            for (let i = 0, len = Generator.getRandomPositiveInt(2, 5); i < len; i++) {
                finding.factors.push(Generator.getRandomUri());
            }
            report = ReportType.getReport(report);
            report.addBase(finding, auditReport);

            expect(report.factorGraph).toBeDefined();
            expect(report.factorGraph).not.toBeNull();
            expect(report.factorGraph.nodes.length).toEqual(finding.factors.length + 1);    // factors + root
            expect(report.factorGraph.edges.length).toEqual(finding.factors.length);
            for (let i = 0, len = finding.factors.length; i < len; i++) {
                expect(report.factorGraph.nodes[i + 1].eventTypes).toEqual([finding.factors[i]]);
                expect(report.factorGraph.nodes[i + 1].types.indexOf(finding.factors[i])).not.toEqual(-1);
                expect(report.factorGraph.nodes[i + 1].javaClass).toEqual(Constants.EVENT_JAVA_CLASS);
                expect(report.factorGraph.edges[i].from).toEqual(report.factorGraph.nodes[0]);
                expect(report.factorGraph.edges[i].to).toEqual(report.factorGraph.nodes[i + 1]);
            }
        });
    });
});
