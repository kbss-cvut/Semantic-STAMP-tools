'use strict';

// TODO Fix the tests
xdescribe('Factors component tests', function () {

    var React = require('react'),
        TestUtils = require('react-addons-test-utils'),
        rewire = require('rewire'),
        Factors = rewire('../../js/components/investigation/Factors'),
        FactorRenderer = rewire('../../js/components/investigation/FactorRenderer'),
        GanttController = null,
        investigation = {
            occurrence: {
                name: 'TestOccurrence',
                startTime: Date.now() - 10000,
                endTime: Date.now()
            },
            rootFactor: {
                startTime: Date.now() - 10000,
                endTime: Date.now()
            }
        };

    beforeEach(function () {
        GanttController = jasmine.createSpyObj('GanttController', ['init', 'getFactor', 'setScale', 'addFactor', 'setOccurrenceEventId', 'expandSubtree', 'updateOccurrenceEvent']);
        Factors.__set__("GanttController", GanttController);
        Factors.__set__('FactorRenderer', FactorRenderer);
        FactorRenderer.__set__('GanttController', GanttController);
        GanttController.getFactor.and.returnValue({
            text: investigation.occurrence.name,
            start_date: new Date(investigation.rootFactor.startTime)
        });
    });

    it('Initializes gantt with minute scale on component mount', function () {
        var factors;
        factors = TestUtils.renderIntoDocument(<Factors investigation={investigation}/>);
        expect(GanttController.init).toHaveBeenCalled();
        expect(GanttController.setScale).toHaveBeenCalledWith('minute');
    });

    it('Adds event of the occurrence into gantt on initialization', function () {
        var factor = null;
        GanttController.addFactor.and.callFake(function (arg) {
            factor = arg;
        });
        TestUtils.renderIntoDocument(<Factors investigation={investigation}/>);
        expect(GanttController.addFactor).toHaveBeenCalled();
        expect(factor).toBeDefined();
        expect(factor.text).toEqual(investigation.name);
        expect(factor.start_date).toEqual(new Date(investigation.rootFactor.startTime));
    });

    it('Sets scale to seconds when seconds are selected', function () {
        var evt = {target: {value: 'second'}},
            factors = TestUtils.renderIntoDocument(<Factors investigation={investigation}/>);
        factors.onScaleChange(evt);
        expect(GanttController.setScale).toHaveBeenCalledWith('second');
    });

    it('Adds event type assessments from PR into gantt with the same time as the occurrence event and having ' +
        ' the occurrence event as parent', function () {
        var occurrenceEvt = null, added = [], parentId;
        GanttController.addFactor.and.callFake(function (item) {
            if (!occurrenceEvt) {
                // The first added task is the occurrence event
                parentId = item.id;
                occurrenceEvt = item;
                GanttController.occurrenceEventId = parentId;
                return item.id;
            } else {
                added.push(item);
            }
        });
        investigation.typeAssessments = initTypeAssessments();
        TestUtils.renderIntoDocument(<Factors investigation={investigation}/>);

        expect(GanttController.addFactor).toHaveBeenCalled();
        expect(occurrenceEvt).toBeDefined();
        expect(added.length).toEqual(investigation.typeAssessments.length);
        expect(occurrenceEvt.text).toEqual(investigation.name);
        expect(occurrenceEvt.start_date).toEqual(new Date(investigation.rootFactor.rootFactor));
        for (var i = 0, len = added.length; i < len; i++) {
            expect(added[i].start_date).toEqual(new Date(investigation.rootFactor.rootFactor));
        }
    });

    function initTypeAssessments() {
        return [
            {
                eventType: {
                    id: 'http://onto.fel.cvut.cz/ontologies/2014/ECCAIRS_Aviation_1.3.0.12/eccairs-events-390#category_2200100',
                    name: 'Runway incursions',
                    dtoClass: 'cz.cvut.kbss.inbas.audit.rest.dto.model.incursion.RunwayIncursionDto'
                },
                intruder: {}
            },
            {
                eventType: {
                    id: 'http://onto.fel.cvut.cz/ontologies/2014/ECCAIRS_Aviation_1.3.0.12/eccairs-events-390#category_22001111',
                    name: 'Runway incursion by an aircraft',
                    dtoClass: 'cz.cvut.kbss.inbas.audit.rest.dto.model.incursion.RunwayIncursionDto'
                },
                intruder: {}
            },
            {
                eventType: {
                    id: 'http://onto.fel.cvut.cz/ontologies/2014/ECCAIRS_Aviation_1.3.0.12/eccairs-events-390#category_2200112',
                    name: 'Runway incursion by a person',
                    dtoClass: 'cz.cvut.kbss.inbas.audit.rest.dto.model.incursion.RunwayIncursionDto'
                },
                intruder: {}
            }
        ]
    }

    it('Adds factors with data from the event type assessment', function() {
        var occurrenceEvt = null, added = [], parentId, args;
        investigation.typeAssessments = initTypeAssessments();
        GanttController.addFactor.and.callFake(function (item) {
            if (!occurrenceEvt) {
                // The first added task is the occurrence event
                parentId = item.id;
                occurrenceEvt = item;
                GanttController.occurrenceEventId = parentId;
                return item.id;
            } else {
                added.push(item);
            }
        });
        TestUtils.renderIntoDocument(<Factors investigation={investigation}/>);

        expect(added.length).toEqual(investigation.typeAssessments.length);
        for (var i = 0, len = added.length; i < len; i++) {
            expect(added[i].statement).toBeDefined();
            expect(added[i].statement).toEqual(investigation.typeAssessments[i]);
        }
        args = GanttController.addFactor.calls.allArgs();
        expect(GanttController.addFactor.calls.count()).toEqual(added.length + 1);
        for (var j = 1, len = args.length; j < len; j++) {
            expect(args[j][1]).toEqual(parentId);
        }
    });
});
