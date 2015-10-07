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
            attachEvent: function() {},
            init: function() {},
            clearAll: function() {},
            addTask: function() {},
            render: function() {}
        },
        occurrence = {
            name: 'TestOccurrence',
            occurrenceTime: Date.now()
        };

    beforeEach(function () {
        window.gantt = gantt;
    });

    it('Initializes gantt with minute scale on component mount', function() {
        var factors;
        spyOn(gantt, 'init');
        factors = TestUtils.renderIntoDocument(<Factors occurrence={occurrence} />);
        expect(gantt.init).toHaveBeenCalled();
        expect(gantt.config.scale_unit).toEqual('minute');
        expect(factors.state.scale).toEqual('minute');
    });

    it('Adds event of the occurrence into gantt on initialization', function() {
        var factors, task;
        spyOn(gantt, 'addTask').andCallFake(function(arg) {
            task = arg;
        });
        factors = TestUtils.renderIntoDocument(<Factors occurrence={occurrence} />);
        expect(gantt.addTask).toHaveBeenCalled();
        expect(task).toBeDefined();
        expect(task.text).toEqual(occurrence.name);
        expect(task.start_date).toEqual(new Date(occurrence.occurrenceTime));
    });

    it('Sets scale to seconds when seconds are selected', function() {
        var evt = {target: {value: 'second'}},
            factors = TestUtils.renderIntoDocument(<Factors occurrence={occurrence} />);
        factors.onScaleChange(evt);
        expect(gantt.config.scale_unit).toEqual('second');
        expect(gantt.config.min_duration).toEqual(1000);
        expect(gantt.config.subscales).toBeDefined();
        expect(factors.state.scale).toEqual('second');
    });
});
