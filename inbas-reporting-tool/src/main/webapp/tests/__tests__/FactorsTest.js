'use strict';

describe('Factors component tests', function () {

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
        FactorRenderer.ganttController = GanttController;
        Factors.__set__('FactorRenderer', FactorRenderer);
        GanttController.getFactor.and.returnValue({
            text: investigation.occurrence.name,
            start_date: new Date(investigation.rootFactor.startTime)
        });
    });

    it('Initializes gantt with minute scale on component mount', function () {
        TestUtils.renderIntoDocument(<Factors investigation={investigation}/>);
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
        expect(factor.text).toEqual(investigation.occurrence.name);
        expect(factor.start_date).toEqual(new Date(investigation.rootFactor.startTime));
    });

    it('Sets scale to seconds when seconds are selected', function () {
        var evt = {target: {value: 'second'}},
            factors = TestUtils.renderIntoDocument(<Factors investigation={investigation}/>);
        factors.onScaleChange(evt);
        expect(GanttController.setScale).toHaveBeenCalledWith('second');
    });

    it('Assigns reference id to new factors', function () {
        var newFactor = {
                isNew: true,
                text: 'Test',
                statement: {},
                parent: 1
            }, parent = {
                id: 1,
                statement: investigation.rootFactor
            },
            referenceId = 117,
            component;
        investigation.rootFactor.referenceId = referenceId;
        GanttController.getFactor.and.returnValue(investigation.rootFactor);
        component = TestUtils.renderIntoDocument(<Factors investigation={investigation}/>);
        component.setState({
            currentFactor: newFactor
        });
        component.onSaveFactor();

        expect(newFactor.statement.referenceId).toEqual(referenceId + 1);
        expect(GanttController.addFactor).toHaveBeenCalledWith(newFactor);
    });
});
