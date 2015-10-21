'use strict';

jest.dontMock('../../js/components/investigation/FactorDetail');
jest.dontMock('../../js/components/reports/wizard/event-type/EventTypeWizardSelector');
jest.dontMock('../../js/utils/FactorStyleInfo');

describe('Tests of the factor dialog', function () {

    var React = require('react/addons'),
        TestUtils = React.addons.TestUtils,
        FactorDetail = require('../../js/components/investigation/FactorDetail'),
        assign = require('object-assign'),
        callbacks = {
            onSave: function () {
            },
            onClose: function () {
            },
            onDelete: function () {
            }
        },
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
        window.gantt = gantt;
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
        spyOn(callbacks, 'onSave');
        spyOn(gantt, 'calculateEndDate').andCallThrough();
        detail = TestUtils.renderIntoDocument(<FactorDetail scale='minute' factor={factor} onSave={callbacks.onSave}
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
        detail = TestUtils.renderIntoDocument(<FactorDetail scale='minute' factor={factor} onSave={callbacks.onSave}
                                                            onClose={callbacks.onClose}
                                                            onDelete={callbacks.onDelete}/>);
        detail.onDurationSet({target: {value: newDuration}});
        detail.onEventTypeChange(eventType);
        detail.onUpdateFactorDetails({statement: details}, function () {
        });

        expect(factor).toEqual(origFactor);
        detail.onSave();
        expect(factor.end_date).toBeDefined();
        expect(factor.statement).toEqual(details);
    });
});
