'use strict';

describe('FactorRenderer tests', function () {

    var rewire = require('rewire'),
        FactorRenderer = rewire('../../js/components/factor/FactorRenderer'),
        Vocabulary = require('../../js/constants/Vocabulary'),
        Generator = require('../environment/Generator').default,
        GanttController,
        report;

    beforeEach(function () {
        GanttController = jasmine.createSpyObj('GanttController', ['addFactor', 'addLink', 'setOccurrenceEventId', 'setFactorParent']);
        FactorRenderer.ganttController = GanttController;
        report = {
            occurrence: {
                uri: 'http://krizik.felk.cvut.cz/ontologies/inbas-2015#Occurrence_instance319360066',
                key: '25857640490956897',
                name: 'Runway incursion',
                eventType: Generator.randomCategory().id,
                startTime: 1447144734937,
                endTime: 1447144800937,
                referenceId: 1
            },
            created: 1447144867175,
            lastModified: 1447147087897,
            summary: 'Short report summary.',
            author: {
                uri: 'http://www.inbas.cz/ontologies/reporting-tool/people#Catherine+Halsey',
                firstName: 'Catherine',
                lastName: 'Halsey',
                username: 'halsey@unsc.org'
            },
            factorGraph: {
                nodes: [1]  // occurrence referenceId
            }

        }
    });

    it('Renders occurrence when factor graph is empty', () => {
        delete report.factorGraph;
        FactorRenderer.renderFactors(report);
        expect(GanttController.addFactor.calls.count()).toEqual(1);
        expect(GanttController.setOccurrenceEventId).toHaveBeenCalled();
        var arg = GanttController.addFactor.calls.argsFor(0)[0];
        verifyRoot(arg);
    });

    function verifyRoot(arg) {
        expect(arg.text).toEqual(report.occurrence.name);
        expect(arg.start_date.getTime()).toEqual(report.occurrence.startTime);
        expect(arg.end_date.getTime()).toEqual(report.occurrence.endTime);
        expect(arg.readonly).toBeTruthy();  //TODO
        expect(arg.parent).not.toBeDefined();
    }

    it('Renders occurrence when it is the only node in factor graph', () => {
        FactorRenderer.renderFactors(report);
        expect(GanttController.addFactor.calls.count()).toEqual(1);
        expect(GanttController.setOccurrenceEventId).toHaveBeenCalled();
        var arg = GanttController.addFactor.calls.argsFor(0)[0];
        verifyRoot(arg);
    });

    it('Renders nodes of the factor graph', () => {
        Array.prototype.push.apply(report.factorGraph.nodes, Generator.generateFactorGraphNodes());
        FactorRenderer.renderFactors(report);
        expect(GanttController.addFactor.calls.count()).toEqual(report.factorGraph.nodes.length);
        verifyAddedNodes();
    });

    function verifyAddedNodes() {
        var occurrence = report.occurrence;
        verifyRoot(GanttController.addFactor.calls.argsFor(0)[0]);
        for (var i = 1, len = report.factorGraph.nodes.length; i < len; i++) {
            var node = report.factorGraph.nodes[i];
            var added = GanttController.addFactor.calls.argsFor(i)[0];
            expect(added.statement).toEqual(node);
            expect(added.start_date.getTime()).toEqual(occurrence.startTime);
            expect(added.end_date.getTime()).toEqual(occurrence.endTime);
        }
    }

    it('Renders factor graph part-of hierarchy', () => {
        Array.prototype.push.apply(report.factorGraph.nodes, Generator.generateFactorGraphNodes());
        report.factorGraph.edges = Generator.generatePartOfLinksForNodes(report, report.factorGraph.nodes);
        var referencesToIds = {};
        GanttController.addFactor.and.callFake((factor) => {
            var id = Generator.getRandomInt();
            if (factor.statement === report.occurrence) {
                id = factor.id;
            }
            referencesToIds[factor.statement.referenceId] = id;
            return id;
        });
        FactorRenderer.renderFactors(report);
        verifyPartOfHierarchy(referencesToIds);
    });

    function verifyPartOfHierarchy(referencesToFactorIds) {
        var edges = report.factorGraph.edges;
        for (var i = 0, len = edges.length; i < len; i++) {
            if (edges[i].linkType !== Vocabulary.HAS_PART) {
                continue;
            }
            expect(GanttController.setFactorParent).toHaveBeenCalledWith(referencesToFactorIds[edges[i].to], referencesToFactorIds[edges[i].from]);
        }
    }

    function verifyAddedLinks(referencesToFactorsIds) {
        var links = report.factorGraph.links;
        for (var i = 0, len = links.length; i < len; i++) {
            var added = GanttController.addLink.calls.argsFor(i)[0];
            expect(added.source).toEqual(referencesToFactorsIds[links[i].from]);
            expect(added.target).toEqual(referencesToFactorsIds[links[i].to]);
        }
    }

    xit('Renders factors with causality relationships using references', function () {
        var rootId = null, childIds = [], ids = {};
        report.rootFactor = Generator.generateFactors(report.occurrence.startTime, report.occurrence.endTime, 2);
        report.links.causes = [{from: 3, to: 4}];
        initAddFactorMock(rootId, childIds, ids);
        GanttController.setOccurrenceEventId.and.callFake(function (id) {
            rootId = id
        });
        FactorRenderer.renderFactors(report);

        expect(GanttController.addLink).toHaveBeenCalled();
        var arg = GanttController.addLink.calls.argsFor(0)[0];
        expect(arg.source).toEqual(ids[report.rootFactor.children[0].children[0].referenceId]);
        expect(arg.target).toEqual(ids[report.rootFactor.children[0].children[1].referenceId]);
        expect(arg.factorType).toEqual('cause');
    });

    function initAddFactorMock(rootId, childIds, ids) {
        GanttController.addFactor.and.callFake(function (item, parentId) {
            var id = Date.now() * 1000 + Math.floor((Math.random() * 1000) + 1);
            if (parentId === rootId) {
                childIds.push(id);
            }
            ids[item.statement.referenceId] = id;
            return id;
        });
    }

    xit('Renders factors with mitigation relationships using references', function () {
        var rootId = null, childIds = [], ids = {};
        report.rootFactor = Generator.generateFactors(report.occurrence.startTime, report.occurrence.endTime, 2);
        report.links.mitigates = [{from: 1, to: 2}, {from: 3, to: 4}];
        initAddFactorMock(rootId, childIds, ids);
        GanttController.setOccurrenceEventId.and.callFake(function (id) {
            rootId = id
        });
        FactorRenderer.renderFactors(report);

        expect(GanttController.addLink.calls.count()).toEqual(2);
        var linkOne = GanttController.addLink.calls.argsFor(0)[0],
            linkTwo = GanttController.addLink.calls.argsFor(1)[0];
        expect(linkOne.source).toEqual(ids[report.rootFactor.children[0].referenceId]);
        expect(linkOne.target).toEqual(ids[report.rootFactor.children[1].referenceId]);
        expect(linkOne.factorType).toEqual('mitigate');
        expect(linkTwo.source).toEqual(ids[report.rootFactor.children[0].children[0].referenceId]);
        expect(linkTwo.target).toEqual(ids[report.rootFactor.children[0].children[1].referenceId]);
        expect(linkTwo.factorType).toEqual('mitigate');
    });

    xit('Stores highest reference id', function () {
        var rootId = null, childIds = [], ids = {};
        report.rootFactor = Generator.generateFactors(report.occurrence.startTime, report.occurrence.endTime, 2);
        GanttController.addFactor.and.callFake(function (item, parentId) {
            var id = Date.now();
            if (parentId === rootId) {
                childIds.push(id);
            }
            ids[item.statement.referenceId] = id;
            return id;
        });
        GanttController.setOccurrenceEventId.and.callFake(function (id) {
            rootId = id
        });
        FactorRenderer.renderFactors(report);

        expect(FactorRenderer.greatestReferenceId).toEqual(6);
    })
});
