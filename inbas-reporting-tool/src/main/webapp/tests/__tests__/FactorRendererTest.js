'use strict';

xdescribe('FactorRenderer tests', function () {

    var rewire = require('rewire'),
        FactorRenderer = rewire('../../js/components/investigation/FactorRenderer'),
        Generator = require('../environment/Generator'),
        GanttController,
        report;

    beforeEach(function () {
        GanttController = jasmine.createSpyObj('GanttController', ['addFactor', 'addLink', 'setOccurrenceEventId']);
        FactorRenderer.ganttController = GanttController;
        report = {
            occurrence: {
                uri: 'http://krizik.felk.cvut.cz/ontologies/inbas-2015#Occurrence_instance319360066',
                key: '25857640490956897',
                name: 'Runway incursion',
                startTime: 1447144734937,
                endTime: 1447144800937
            },
            created: 1447144867175,
            lastEdited: 1447147087897,
            summary: 'Short report summary.',
            author: {
                uri: 'http://www.inbas.cz/ontologies/reporting-tool/people#Catherine+Halsey',
                firstName: 'Catherine',
                lastName: 'Halsey',
                username: 'halsey@unsc.org'
            },
            rootFactor: {
                startTime: 1447144734937,
                endTime: 1447144800937
            },
            links: {}
        }
    });

    it('Renders occurrence event when no other factors are specified', function () {
        FactorRenderer.renderFactors(report);
        expect(GanttController.addFactor.calls.count()).toEqual(1);
        expect(GanttController.setOccurrenceEventId).toHaveBeenCalled();
        var arg = GanttController.addFactor.calls.argsFor(0)[0];
        verifyRoot(arg);
    });

    function verifyRoot(arg) {
        expect(arg.text).toEqual(report.occurrence.name);
        expect(arg.start_date.getTime()).toEqual(report.rootFactor.startTime);
        expect(arg.end_date.getTime()).toEqual(report.rootFactor.endTime);
        expect(arg.readonly).toBeTruthy();
        expect(arg.parent).not.toBeDefined();
    }

    it('Renders factors from new investigation report - it does not contain deeper hierarchy or cause/mitigation links', function () {
        var rootId = null;
        report.rootFactor = Generator.generateFactors(report.occurrence.startTime, report.occurrence.endTime, 1);
        GanttController.addFactor.and.returnValue(Date.now());
        GanttController.setOccurrenceEventId.and.callFake(function (id) {
            rootId = id
        });
        FactorRenderer.renderFactors(report);
        expect(GanttController.addFactor.calls.count()).toEqual(3);
        var args = GanttController.addFactor.calls.allArgs(),
            root = args[0][0],
            firstChild = args[1][0],
            secondChild = args[2][0];
        verifyRoot(root);
        verifyNode(report.rootFactor.children[0], firstChild, rootId);
        verifyNode(report.rootFactor.children[1], secondChild, rootId);
    });

    function verifyNode(expected, node, expectedParentId) {
        expect(node.start_date.getTime()).toEqual(expected.startTime);
        expect(node.end_date.getTime()).toEqual(expected.endTime);
        expect(node.statement).toEqual(expected);
        expect(node.parent).toEqual(expectedParentId);
    }

    it('Renders root factor with deeper descendant hierarchy', function () {
        var rootId = null, childIds = [];
        report.rootFactor = Generator.generateFactors(report.occurrence.startTime, report.occurrence.endTime, 2);
        GanttController.addFactor.and.callFake(function (item, parentId) {
            var id = Generator.getRandomInt();
            if (parentId === rootId) {
                childIds.push(id);
            }
            return id;
        });
        GanttController.setOccurrenceEventId.and.callFake(function (id) {
            rootId = id
        });
        FactorRenderer.renderFactors(report);
        expect(GanttController.addFactor.calls.count()).toEqual(7);
        var args = GanttController.addFactor.calls.allArgs(),
            root = args[0][0],
            firstChild = args[1][0],
            firstChildFirstChild = args[2][0],
            firstChildSecondChild = args[3][0],
            secondChild = args[4][0],
            secondChildFirstChild = args[5][0],
            secondChildSecondChild = args[6][0];
        verifyRoot(root);
        verifyNode(report.rootFactor.children[0], firstChild, rootId);
        verifyNode(report.rootFactor.children[1], secondChild, rootId);
        verifyNode(report.rootFactor.children[0].children[0], firstChildFirstChild, childIds[0]);
        verifyNode(report.rootFactor.children[0].children[1], firstChildSecondChild, childIds[0]);
        verifyNode(report.rootFactor.children[1].children[0], secondChildFirstChild, childIds[1]);
        verifyNode(report.rootFactor.children[1].children[1], secondChildSecondChild, childIds[1]);
    });

    it('Renders factors with causality relationships using references', function () {
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

    it('Renders factors with mitigation relationships using references', function () {
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

    it('Stores highest reference id', function () {
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
