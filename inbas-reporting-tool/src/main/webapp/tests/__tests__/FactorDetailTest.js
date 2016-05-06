'use strict';


describe('Tests of the factor dialog', function () {

    var React = require('react'),
        Environment = require('../environment/Environment'),
        FactorDetail = require('../../js/components/factor/FactorDetail'),
        assign = require('object-assign'),
        callbacks,
        gantt = {
            calculateEndDate: function () {
                return new Date();
            },
            config: {
                duration_unit: 'minute'
            }
        },
        factor;

    beforeEach(function () {
        callbacks = jasmine.createSpyObj('callbacks', ['onSave', 'onClose', 'onDelete']);
        jasmine.getGlobal().gantt = gantt;
        factor = {
            id: 1,
            text: 'Test',
            start_date: new Date(),
            duration: 1
        };
    });

    it('Updates factor with new values upon save', function () {
        var detail, newDuration = 10,
            eventType = {
                name: 'Runway Incursion',
                id: 'http://incursion'
            };
        spyOn(gantt, 'calculateEndDate').and.callThrough();
        detail = Environment.render(<FactorDetail scale='minute' factor={factor} onSave={callbacks.onSave}
                                                  onClose={callbacks.onClose}
                                                  onDelete={callbacks.onDelete}/>);
        detail.onDurationSet({target: {value: newDuration}});
        detail.onEventTypeChange(eventType);
        detail.onSave();
        expect(gantt.calculateEndDate).toHaveBeenCalledWith(factor.start_date, newDuration, 'minute');
        expect(factor.end_date).toBeDefined();
        expect(callbacks.onSave).toHaveBeenCalled();
    });

    it('Preserves factor state until save is called', function () {
        var detail, newDuration = 10,
            eventType = {
                name: 'Runway Incursion',
                id: 'http://incursion'
            },
            origFactor = assign({}, factor),
            details = {
                eventType: eventType,
                intruder: {},
                lvp: 'none',
                location: 'LKPR31'
            };
        detail = Environment.render(<FactorDetail scale='minute' factor={factor} onSave={callbacks.onSave}
                                                  onClose={callbacks.onClose}
                                                  onDelete={callbacks.onDelete}/>);
        detail.onDurationSet({target: {value: newDuration}});
        detail.onEventTypeChange(eventType);
        detail.onUpdateFactorDetails({statement: details}, function () {
        });

        expect(factor).toEqual(origFactor);
        detail.onSave();
        expect(factor.end_date).toBeDefined();
        expect(factor.statement).toBeDefined();
        expect(factor.statement).toEqual(details);
    });
});
