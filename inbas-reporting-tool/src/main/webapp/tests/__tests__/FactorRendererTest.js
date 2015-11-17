'use strict';

describe('FactorRenderer tests', function () {

    var rewire = require('rewire'),
        FactorRenderer = rewire('../../js/components/investigation/FactorRenderer'),
        GanttController,
        investigation;

    beforeEach(function () {
        GanttController = jasmine.createSpyObj('GanttController', ['addFactor', 'addLink', 'setOccurrenceEventId']);
        FactorRenderer.ganttController = GanttController;
        investigation = {
            occurrence: {
                uri: 'http://krizik.felk.cvut.cz/ontologies/inbas-2015#Occurrence_instance319360066',
                key: '25857640490956897',
                name: 'Runway incursion',
                startTime: 1447144734937,
                endTime: 1447144800937,
                reportingPhase: 'INVESTIGATION'
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
            }
        }
    });

    it('Renders occurrence event when no other factors are specified', function () {
        FactorRenderer.renderFactors(investigation);
        expect(GanttController.addFactor.calls.count()).toEqual(1);
        expect(GanttController.setOccurrenceEventId).toHaveBeenCalled();
        var arg = GanttController.addFactor.calls.argsFor(0)[0];
        verifyRoot(arg);
    });

    function verifyRoot(arg) {
        expect(arg.text).toEqual(investigation.occurrence.name);
        expect(arg.start_date.getTime()).toEqual(investigation.rootFactor.startTime);
        expect(arg.end_date.getTime()).toEqual(investigation.rootFactor.endTime);
        expect(arg.readonly).toBeTruthy();
        expect(arg.parent).not.toBeDefined();
    }

    it('Renders factors from new investigation report - it does not contain deeper hierarchy or cause/mitigation links', function () {
        var rootId = null;
        investigation.rootFactor.children = initChildren();
        GanttController.addFactor.and.returnValue(Date.now());
        GanttController.setOccurrenceEventId.and.callFake(function(id) {rootId = id});
        FactorRenderer.renderFactors(investigation);
        expect(GanttController.addFactor.calls.count()).toEqual(3);
        var args = GanttController.addFactor.calls.allArgs(),
            root = args[0][0],
            firstChild = args[1][0],
            secondChild = args[2][0];
        verifyRoot(root);
        verifyNode(investigation.rootFactor.children[0], firstChild, rootId);
        verifyNode(investigation.rootFactor.children[1], secondChild, rootId);
    });

    function verifyNode(expected, node, expectedParentId) {
        expect(node.start_date.getTime()).toEqual(expected.startTime);
        expect(node.end_date.getTime()).toEqual(expected.endTime);
        expect(node.statement).toEqual(expected);
        expect(node.parent).toEqual(expectedParentId);
    }

    function initChildren() {
        return [
            {
                startTime: 1447144734937,
                endTime: 1447144800937,
                assessment: {
                    eventType: {
                        id: 'http://onto.fel.cvut.cz/ontologies/eccairs-1.3.0.8/V-24-1-31-31-14-390-2000000-2200000-2200110',
                        name: '2200110 - Incursions generally',
                        type: 'http://onto.fel.cvut.cz/ontologies/eccairs/model/event-type'
                    },
                    description: 'Event type assessment with description only.'
                }
            },
            {
                startTime: 1447144734937,
                endTime: 1447144800937,
                assessment: {
                    eventType: {
                        id: 'http://onto.fel.cvut.cz/ontologies/eccairs-1.3.0.8/V-24-1-31-31-14-390-2000000-2200000-2200100',
                        name: '2200100 - Runway incursions',
                        type: 'http://onto.fel.cvut.cz/ontologies/eccairs/model/event-type'
                    },
                    runwayIncursion: {
                        lowVisibilityProcedure: 'NONE',
                        intruder: {
                            aircraft: {
                                'uri': 'http://krizik.felk.cvut.cz/ontologies/inbas-2015#Aircraft_instance994092607',
                                registration: 'OK123',
                                callSign: 'OK123',
                                flightNumber: '1234',
                                flightPhase: 'taxi_to_runway',
                                operationType: 'passenger'
                            }
                        }
                    }
                }
            }
        ]
    }

    it('Renders root factor with deeper descendant hierarchy', function() {
        var rootId = null, childIds = [];
        investigation.rootFactor.children = initChildren();
        investigation.rootFactor.children[0].children = initChildren();
        GanttController.addFactor.and.callFake(function(item, parentId) {
            var id = Date.now();
            if (parentId === rootId) {
                childIds.push(id);
            }
            return id;
        });
        GanttController.setOccurrenceEventId.and.callFake(function(id) {rootId = id});
        FactorRenderer.renderFactors(investigation);
        expect(GanttController.addFactor.calls.count()).toEqual(5);
        var args = GanttController.addFactor.calls.allArgs(),
            root = args[0][0],
            firstChild = args[1][0],
            firstChildFirstChild = args[2][0],
            firstChildSecondChild = args[3][0],
            secondChild = args[4][0];
        verifyRoot(root);
        verifyNode(investigation.rootFactor.children[0], firstChild, rootId);
        verifyNode(investigation.rootFactor.children[1], secondChild, rootId);
        verifyNode(investigation.rootFactor.children[0].children[0], firstChildFirstChild, childIds[0]);
        verifyNode(investigation.rootFactor.children[0].children[1], firstChildSecondChild, childIds[0]);
    });

    it('Renders factors with causality relationships using references', function() {
        var rootId = null, childIds = [], ids = {};
        investigation.rootFactor.children = initChildren();
        investigation.rootFactor.children[0].children = initChildren();
        investigation.rootFactor.children[0].children[0]['@id'] = 1;
        investigation.rootFactor.children[0].children[1].causes = [1];
        GanttController.addFactor.and.callFake(function(item, parentId) {
            var id = Date.now();
            if (parentId === rootId) {
                childIds.push(id);
            }
            ids[item.statement] = id;
            return id;
        });
        GanttController.setOccurrenceEventId.and.callFake(function(id) {rootId = id});
        FactorRenderer.renderFactors(investigation);

        expect(GanttController.addLink).toHaveBeenCalled();
        var arg = GanttController.addLink.calls.argsFor(0)[0];
        expect(arg.from).toEqual(ids[investigation.rootFactor.children[0].children[0]]);
        expect(arg.to).toEqual(ids[investigation.rootFactor.children[0].children[1]]);
        expect(arg.factorType).toEqual('cause');
    });

    it('Renders factors with mitigation relationships using references', function() {
        var rootId = null, childIds = [], ids = {};
        investigation.rootFactor.children = initChildren();
        investigation.rootFactor.children[0]['@id'] = 1;
        investigation.rootFactor.children[0].mitigatingFactors = [1];
        investigation.rootFactor.children[0].children = initChildren();
        investigation.rootFactor.children[0].children[0]['@id'] = 2;
        investigation.rootFactor.children[0].children[1].mitigatingFactors = [2];
        GanttController.addFactor.and.callFake(function(item, parentId) {
            var id = Date.now();
            if (parentId === rootId) {
                childIds.push(id);
            }
            ids[item.statement] = id;
            return id;
        });
        GanttController.setOccurrenceEventId.and.callFake(function(id) {rootId = id});
        FactorRenderer.renderFactors(investigation);

        expect(GanttController.addLink.calls.count()).toEqual(2);
        var linkOne = GanttController.addLink.calls.argsFor(0)[0],
            linkTwo = GanttController.addLink.calls.argsFor(1)[0];
        expect(linkOne.from).toEqual(ids[investigation.rootFactor.children[0]]);
        expect(linkOne.to).toEqual(ids[investigation.rootFactor.children[1]]);
        expect(linkOne.factorType).toEqual('mitigate');
        expect(linkTwo.from).toEqual(ids[investigation.rootFactor.children[0].children[0]]);
        expect(linkTwo.to).toEqual(ids[investigation.rootFactor.children[0].children[1]]);
        expect(linkTwo.factorType).toEqual('mitigate');
    });
});
