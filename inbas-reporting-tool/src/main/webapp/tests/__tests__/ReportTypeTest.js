'use strict';

describe('ReportType', function () {

    var ReportType = require('../../js/model/ReportType'),
        Constants = require('../../js/constants/Constants'),
        Generator = require('../environment/Generator').default,
        ReportFactory = require('../../js/model/ReportFactory'),
        OccurrenceReportController = require('../../js/components/report/occurrence/OccurrenceReportController'),
        Vocabulary = require('../../js/constants/Vocabulary');

    it('returns default detail controller for new report when getDetailController is called', function () {
        var report = ReportFactory.createOccurrenceReport(),

            controller = ReportType.getDetailController(report);
        expect(controller).toEqual(OccurrenceReportController);
    });

    describe('- safety issue report - addBase()', () => {

        it('adds factor graph to a new instance', () => {
            var basedOn = generateSafetyIssueBase(),
                originalGraph = basedOn.factorGraph,
                i, len, edge;

            var report = ReportFactory.createReport(Vocabulary.SAFETY_ISSUE_REPORT);
            report = ReportType.getReport(report);
            report.addBase(basedOn);
            expect(report.factorGraph).not.toBeNull();
            expect(report.factorGraph.nodes.length).toEqual(originalGraph.nodes.length);
            for (i = 0, len = report.factorGraph.nodes.length; i < len; i++) {
                expect(report.factorGraph.nodes[i].uri).not.toBeDefined();
            }
            expect(report.safetyIssue.basedOn[0].types.indexOf(Vocabulary.OCCURRENCE)).not.toEqual(-1);
            expect(report.factorGraph.edges.length).toEqual(originalGraph.edges.length);
            for (i = 0, len = report.factorGraph.edges.length; i < len; i++) {
                edge = report.factorGraph.edges[i];
                expect(report.factorGraph.nodes.indexOf(edge.from)).not.toEqual(-1);
                expect(report.factorGraph.nodes.indexOf(edge.to)).not.toEqual(-1);
            }
        });

        function generateSafetyIssueBase() {
            var basedOn = Generator.generateOccurrenceReport();
            basedOn.occurrence.referenceId = Generator.getRandomInt();
            basedOn.factorGraph = {
                nodes: [basedOn.occurrence.referenceId]
            };
            Array.prototype.push.apply(basedOn.factorGraph.nodes, Generator.generateFactorGraphNodes());
            basedOn.factorGraph.edges = Generator.generateFactorLinksForNodes(basedOn.factorGraph.nodes);
            return basedOn;
        }

        it('adds another report as base', () => {
            var basedOn = generateSafetyIssueBase(),
                newBase = generateSafetyIssueBase(),
                originalFactorGraph = basedOn.factorGraph,
                newBaseFactorGraph = newBase.factorGraph,
                report, result,
                i, len, edge;

            report = ReportFactory.createReport(Vocabulary.SAFETY_ISSUE_REPORT, {basedOn: basedOn});
            expect(report.factorGraph).not.toBeNull();
            report = ReportType.getReport(report);
            result = report.addBase(newBase);
            expect(result).toBeTruthy();
            // The occurrence will be counted twice, but we need only one, which is transformed to safety issue (the
            // graph's root)
            expect(report.factorGraph.nodes.length).toEqual(originalFactorGraph.nodes.length + newBaseFactorGraph.nodes.length - 1);
            for (i = 0, len = report.factorGraph.nodes.length; i < len; i++) {
                expect(report.factorGraph.nodes[i].uri).not.toBeDefined();
            }
            expect(report.factorGraph.edges.length).toEqual(originalFactorGraph.edges.length + newBaseFactorGraph.edges.length);
            for (i = 0, len = report.factorGraph.edges.length; i < len; i++) {
                edge = report.factorGraph.edges[i];
                expect(report.factorGraph.nodes.indexOf(edge.from)).not.toEqual(-1);
                expect(report.factorGraph.nodes.indexOf(edge.to)).not.toEqual(-1);
            }
            for (i = 0, len = report.safetyIssue.basedOn.length; i < len; i++) {
                expect(report.safetyIssue.basedOn[i].types.indexOf(Vocabulary.OCCURRENCE)).not.toEqual(-1);
            }
        });

        it('does not add base if it already exists in safety issue', () => {
            var basedOn = generateSafetyIssueBase(),
                originalFactorGraph = basedOn.factorGraph,
                report, result;

            report = ReportFactory.createReport(Vocabulary.SAFETY_ISSUE_REPORT, {basedOn: basedOn});
            expect(report.factorGraph).not.toBeNull();
            report = ReportType.getReport(report);
            expect(report.factorGraph.nodes.length).toEqual(originalFactorGraph.nodes.length);
            expect(report.factorGraph.edges.length).toEqual(originalFactorGraph.edges.length);
            expect(report.safetyIssue.basedOn.length).toEqual(1);
            result = report.addBase(basedOn);
            expect(result).toBeFalsy();
            expect(report.factorGraph.nodes.length).toEqual(originalFactorGraph.nodes.length);
            expect(report.factorGraph.edges.length).toEqual(originalFactorGraph.edges.length);
            expect(report.safetyIssue.basedOn.length).toEqual(1);
        });
    });
});
