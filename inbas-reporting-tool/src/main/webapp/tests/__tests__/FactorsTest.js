'use strict';

describe('Factors component tests', function () {

    var React = require('react'),
        rewire = require('rewire'),
        Environment = require('../environment/Environment'),
        Generator = require('../environment/Generator').default,
        Factors = rewire('../../js/components/factor/Factors'),
        FactorRenderer = rewire('../../js/components/factor/FactorRenderer'),
        Actions = require('../../js/actions/Actions'),
        GanttController = null,
        report = {
            occurrence: {
                name: 'TestOccurrence',
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
            text: report.occurrence.name,
            start_date: new Date(report.occurrence.startTime)
        });
        spyOn(Actions, 'loadEventTypes');
        Factors.__set__('Actions', Actions);
    });

    it('Initializes gantt with minute scale on component mount', function () {
        Environment.render(<Factors report={report}/>);
        expect(GanttController.init).toHaveBeenCalled();
        expect(GanttController.setScale).toHaveBeenCalledWith('minute');
    });
    
    it('Adds event of the occurrence into gantt on initialization', function () {
        var factor = null;
        GanttController.addFactor.and.callFake(function (arg) {
            factor = arg;
        });
        var factors = Environment.render(<Factors report={report}/>);
        factors.renderFactors({type: Actions.loadEventTypes, data: []});
        expect(GanttController.addFactor).toHaveBeenCalled();
        expect(factor).toBeDefined();
        expect(factor.text).toEqual(report.occurrence.name);
        expect(factor.start_date).toEqual(new Date(report.occurrence.startTime));
    });

    it('Sets scale to seconds when seconds are selected', function () {
        var evt = {target: {value: 'second'}},
            factors = Environment.render(<Factors report={report}/>);
        factors.onScaleChange(evt);
        expect(GanttController.setScale).toHaveBeenCalledWith('second');
    });

    it('Adds new factor without parent to gantt when factor at top level is created', () => {
        var factors = Environment.render(<Factors report={report}/>),
            newFactor = {
                duration: 2,
                durationUnit: 'minute',
                isNew: true,
                id: Generator.getRandomInt(),
                start_date: new Date(Date.now()),
                end_date: new Date(Date.now() + 2 * 60 * 60 * 1000),
                text: '2180100 - Loss of Separation',
                statement: {}
            };
        factors.renderFactors({type: Actions.loadEventTypes, data: []});
        factors.state.currentFactor = newFactor;
        factors.onSaveFactor();
        expect(GanttController.addFactor).toHaveBeenCalled();
        expect(GanttController.getFactor).not.toHaveBeenCalled();
        // First call is to add the occurrence event itself
        var added = GanttController.addFactor.calls.argsFor(1)[0];
        expect(added.id).toEqual(newFactor.id);
        expect(added.parent).not.toBeDefined();
    });

    it('Assigns reference id to new factors', () => {
        var newFactor = {
                isNew: true,
                text: 'Test',
                statement: {},
                parent: 1
            }, parent = {
                id: 1,
                statement: report.occurrence
            },
            referenceId = 117,
            component;
        report.occurrence.referenceId = referenceId;
        GanttController.getFactor.and.returnValue(parent);
        component = Environment.render(<Factors report={report}/>);
        component.renderFactors({type: Actions.loadEventTypes, data: []});
        component.setState({
            currentFactor: newFactor
        });
        component.onSaveFactor();

        expect(newFactor.statement.referenceId).toEqual(referenceId + 1);
        expect(GanttController.addFactor).toHaveBeenCalledWith(newFactor);
    });

    it('Renders factor graph only after event types have been loaded', () => {
        spyOn(FactorRenderer, 'renderFactors');
        var factors = Environment.render(<Factors report={report}/>),
            eventTypes = [
                {id: 'test'}
            ],
            TypeaheadStore = require('../../js/stores/TypeaheadStore');
        spyOn(TypeaheadStore, 'getEventTypes').and.returnValue(eventTypes);
        expect(FactorRenderer.renderFactors).not.toHaveBeenCalled();
        factors.renderFactors({
            type: Actions.loadEventTypes,
            data: eventTypes
        });
        expect(FactorRenderer.renderFactors).toHaveBeenCalledWith(report, eventTypes);
    });
});
