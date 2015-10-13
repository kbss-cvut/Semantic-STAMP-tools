'use strict';

jest.dontMock('../../js/components/investigation/Factors');

describe('Factors component tests', function () {

    var React = require('react/addons'),
        TestUtils = React.addons.TestUtils,
        Factors = require('../../js/components/investigation/Factors'),
        gantt = {
            config: {},
            templates: {},
            date: {},
            attachEvent: function () {
            },
            init: function () {
            },
            clearAll: function () {
            },
            addTask: function () {
                return 1;
            },
            render: function () {
            },
            getTask: function (taskId) {
            },
            open: function() {
            },
            calculateDuration: function() {
                return 1;
            },
            eachTask: function() {
            },
            updateTask: function() {
            },
            refreshData: function() {
            },
            getChildren: function() {
                return [];
            }
        },
        occurrence = {
            name: 'TestOccurrence',
            occurrenceTime: Date.now()
        };

    beforeEach(function () {
        window.gantt = gantt;
        spyOn(gantt, 'getTask').andReturn({
            text: occurrence.name,
            start_date: new Date(occurrence.occurrenceTime)
        });
    });

    it('Initializes gantt with minute scale on component mount', function () {
        var factors;
        spyOn(gantt, 'init');
        factors = TestUtils.renderIntoDocument(<Factors occurrence={occurrence}/>);
        expect(gantt.init).toHaveBeenCalled();
        expect(gantt.config.scale_unit).toEqual('minute');
        expect(factors.state.scale).toEqual('minute');
    });

    it('Adds event of the occurrence into gantt on initialization', function () {
        var task;
        spyOn(gantt, 'addTask').andCallFake(function (arg) {
            task = arg;
        });
        TestUtils.renderIntoDocument(<Factors occurrence={occurrence}/>);
        expect(gantt.addTask).toHaveBeenCalled();
        expect(task).toBeDefined();
        expect(task.text).toEqual(occurrence.name);
        expect(task.start_date).toEqual(new Date(occurrence.occurrenceTime));
    });

    it('Sets scale to seconds when seconds are selected', function () {
        var evt = {target: {value: 'second'}},
            factors = TestUtils.renderIntoDocument(<Factors occurrence={occurrence}/>);
        factors.onScaleChange(evt);
        expect(gantt.config.scale_unit).toEqual('second');
        expect(gantt.config.min_duration).toEqual(1000);
        expect(gantt.config.subscales).toBeDefined();
        expect(factors.state.scale).toEqual('second');
    });

    it('Adds event type assessments from PR into gantt with the same time as the occurrence event and having ' +
        ' the occurrence event as parent', function () {
        var occurrenceEvt, added = [], parentId;
        spyOn(gantt, 'addTask').andCallFake(function (item, parent) {
            if (!occurrenceEvt) {
                // The first added task is the occurrence event
                parentId = item.id;
                occurrenceEvt = item;
                return item.id;
            } else {
                added.push(item);
            }
        });
        occurrence.typeAssessments = initTypeAssessments();
        TestUtils.renderIntoDocument(<Factors occurrence={occurrence}/>);

        expect(gantt.addTask).toHaveBeenCalled();
        expect(occurrenceEvt).toBeDefined();
        expect(added.length).toEqual(occurrence.typeAssessments.length);
        expect(occurrenceEvt.text).toEqual(occurrence.name);
        expect(occurrenceEvt.start_date).toEqual(new Date(occurrence.occurrenceTime));
        for (var i = 0, len = added.length; i < len; i++) {
            expect(added[i].start_date).toEqual(new Date(occurrence.occurrenceTime));
            expect(added[i].parent).toEqual(parentId);
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
        var occurrenceEvt, added = [], parentId;
        occurrence.typeAssessments = initTypeAssessments();
        spyOn(gantt, 'addTask').andCallFake(function (item, parent) {
            if (!occurrenceEvt) {
                // The first added task is the occurrence event
                parentId = item.id;
                occurrenceEvt = item;
                return item.id;
            } else {
                added.push(item);
            }
        });
        TestUtils.renderIntoDocument(<Factors occurrence={occurrence}/>);

        expect(added.length).toEqual(occurrence.typeAssessments.length);
        for (var i = 0, len = added.length; i < len; i++) {
            expect(added[i].parent).toEqual(parentId);
            expect(added[i].statement).toBeDefined();
            expect(added[i].statement).toEqual(occurrence.typeAssessments[i]);
        }
    });
});
