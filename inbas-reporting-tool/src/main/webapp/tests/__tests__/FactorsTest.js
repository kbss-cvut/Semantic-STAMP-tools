'use strict';

describe('Factors component tests', function () {

    var React = require('react'),
        TestUtils = require('react-addons-test-utils'),
        rewire = require('rewire'),
        Factors = rewire('../../js/components/investigation/Factors'),
        GanttController = null,
        occurrence = {
            name: 'TestOccurrence',
            occurrenceTime: Date.now()
        };

    beforeEach(function () {
        GanttController = jasmine.createSpyObj('GanttController', ['init', 'getFactor', 'setScale', 'addFactor', 'setOccurrenceEventId', 'expandSubtree', 'updateOccurrenceEvent']);
        Factors.__set__("GanttController", GanttController);
        GanttController.getFactor.and.returnValue({
            text: occurrence.name,
            start_date: new Date(occurrence.occurrenceTime)
        });
    });

    it('Initializes gantt with minute scale on component mount', function () {
        var factors;
        factors = TestUtils.renderIntoDocument(<Factors occurrence={occurrence}/>);
        expect(GanttController.init).toHaveBeenCalled();
        expect(GanttController.setScale).toHaveBeenCalledWith('minute');
    });

    it('Adds event of the occurrence into gantt on initialization', function () {
        var factor = null;
        GanttController.addFactor.and.callFake(function (arg) {
            factor = arg;
        });
        TestUtils.renderIntoDocument(<Factors occurrence={occurrence}/>);
        expect(GanttController.addFactor).toHaveBeenCalled();
        expect(factor).toBeDefined();
        expect(factor.text).toEqual(occurrence.name);
        expect(factor.start_date).toEqual(new Date(occurrence.occurrenceTime));
    });

    it('Sets scale to seconds when seconds are selected', function () {
        var evt = {target: {value: 'second'}},
            factors = TestUtils.renderIntoDocument(<Factors occurrence={occurrence}/>);
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
        occurrence.typeAssessments = initTypeAssessments();
        TestUtils.renderIntoDocument(<Factors occurrence={occurrence}/>);

        expect(GanttController.addFactor).toHaveBeenCalled();
        expect(occurrenceEvt).toBeDefined();
        expect(added.length).toEqual(occurrence.typeAssessments.length);
        expect(occurrenceEvt.text).toEqual(occurrence.name);
        expect(occurrenceEvt.start_date).toEqual(new Date(occurrence.occurrenceTime));
        for (var i = 0, len = added.length; i < len; i++) {
            expect(added[i].start_date).toEqual(new Date(occurrence.occurrenceTime));
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
        occurrence.typeAssessments = initTypeAssessments();
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
        TestUtils.renderIntoDocument(<Factors occurrence={occurrence}/>);

        expect(added.length).toEqual(occurrence.typeAssessments.length);
        for (var i = 0, len = added.length; i < len; i++) {
            expect(added[i].statement).toBeDefined();
            expect(added[i].statement).toEqual(occurrence.typeAssessments[i]);
        }
        args = GanttController.addFactor.calls.allArgs();
        expect(GanttController.addFactor.calls.count()).toEqual(added.length + 1);
        for (var j = 1, len = args.length; j < len; j++) {
            expect(args[j][1]).toEqual(parentId);
        }
    });
});
