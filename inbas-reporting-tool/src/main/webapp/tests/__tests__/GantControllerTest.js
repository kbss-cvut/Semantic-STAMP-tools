'use strict';

jest.dontMock('../../js/components/investigation/GanttController');

describe('Tests for the gantt component controller', function () {

    var GanttController = require('../../js/components/investigation/GanttController'),
        gantt = jasmine.createSpyObj('gantt', ['init', 'clearAll', 'attachEvent', 'updateTask', 'refreshData']),
        props = jasmine.createSpyObj('props', ['onLinkAdded', 'onCreateFactor', 'onEditFactor', 'updateOccurrence']);

    beforeEach(function () {
        gantt.config = {};
        gantt.templates = {};
        gantt.date = {};
        // The function that we want to mock have to exist
        gantt.getTask = function() {};
        gantt.calculateDuration = function(start, end) {
            return end.getTime() - start.getTime();
        };
        gantt.calculateEndDate = function(start, duration, unit) {
            return new Date(start.getTime() + duration);
        };
        gantt.getChildren = function(id) {
            return [];
        };
        window.gantt = gantt;
        GanttController.init(props);
    });

    it('Prevents editing of the occurrence event', function () {
        var id = 1;
        spyOn(gantt, 'getTask');
        GanttController.occurrenceEventId = id;
        GanttController.onEditFactor(id, {
            preventDefault: function () {
            }
        });
        expect(gantt.getTask).not.toHaveBeenCalled();
        expect(props.onEditFactor).not.toHaveBeenCalled();
    });

    it('Initializes new factor with default values', function () {
        var factor = {};
        gantt.config.duration_unit = 'minute';
        GanttController.onCreateFactor(factor);
        expect(props.onCreateFactor).toHaveBeenCalledWith(factor);
        expect(factor.isNew).toBeTruthy();
        expect(factor.text).toEqual('');
        expect(factor.durationUnit).toEqual('minute');
    });

    it('Updates occurrence event when updated occurrence is passed in', function () {
        var occurrence = {
            occurrenceTime: Date.now(),
            name: 'Test occurrence'
        }, occurrenceEvent = {
            start_date: new Date(occurrence.occurrenceTime + 10000),
            end_date: new Date(occurrence.occurrenceTime + 20000),
            text: 'Test'
        };
        spyOn(gantt, 'getTask').andReturn(occurrenceEvent);
        GanttController.updateOccurrenceEvent(occurrence);

        expect(occurrenceEvent.text).toEqual(occurrence.name);
        expect(occurrenceEvent.start_date.getTime()).toEqual(occurrence.occurrenceTime);
    });

    it('Ensures that the occurrence event has never a duration less than 1 time unit', function() {
        var occurrence = {
            occurrenceTime: Date.now(),
            name: 'Test occurrence'
        }, occurrenceEvent = {
            start_date: new Date(occurrence.occurrenceTime + 100),
            end_date: new Date(occurrence.occurrenceTime + 200),
            text: 'Test'
        };
        spyOn(gantt, 'getTask').andReturn(occurrenceEvent);
        spyOn(gantt, 'calculateDuration').andCallFake(function(start, end) {
            return (end.getTime() - start.getTime()) / 1000;
        });
        spyOn(gantt, 'calculateEndDate').andReturn(new Date());
        GanttController.updateOccurrenceEvent(occurrence);

        expect(gantt.calculateEndDate).toHaveBeenCalled();
    });

    it('Ensures parent event contains a newly added child event by expanding its time interval', function() {
        var parent = {
            id: 1,
            start_date: new Date(),
            end_date: new Date(Date.now() + 10000)
        }, child = {
            id: 2,
            parent: 1,
            start_date: new Date(Date.now() - 1000),
            end_date: new Date(Date.now() + 1000)
        };
        spyOn(gantt, 'getTask').andReturn(parent);
        spyOn(GanttController, 'extendAncestorsIfNecessary').andCallThrough();
        GanttController.onFactorAdded(2, child);

        expect(GanttController.extendAncestorsIfNecessary).toHaveBeenCalled();
        expect(parent.start_date).toEqual(child.start_date);
    });

    it('Ensures parent event contains an updated child by expanding its time interval', function() {
        var parent = {
            id: 1,
            start_date: new Date(),
            end_date: new Date(Date.now() + 10000)
        }, child = {
            id: 2,
            parent: 1,
            start_date: new Date(Date.now() - 1000),
            end_date: new Date(Date.now() + 200000)
        };
        spyOn(gantt, 'getTask').andReturn(parent);
        spyOn(GanttController, 'extendAncestorsIfNecessary').andCallThrough();
        spyOn(GanttController, 'updateDescendantsTimeInterval');
        GanttController.onFactorUpdated(2, child);

        expect(GanttController.extendAncestorsIfNecessary).toHaveBeenCalled();
        expect(GanttController.updateDescendantsTimeInterval).toHaveBeenCalled();
        expect(parent.start_date).toEqual(child.start_date);
        expect(parent.end_date).toEqual(child.end_date);
        expect(gantt.refreshData).toHaveBeenCalled();
    });

    it('Ensures child events shrink when parent event time interval is decreased', function() {
        var parent = {
            id: 1,
            start_date: new Date(),
            end_date: new Date(Date.now() + 10000)
        }, child = {
            id: 2,
            parent: 1,
            start_date: new Date(Date.now() - 1000),
            end_date: new Date(Date.now() + 2000)
        };
        spyOn(gantt, 'getTask').andReturn(child);
        spyOn(gantt, 'getChildren').andReturn([child]);
        spyOn(GanttController, 'ensureNonZeroDuration');
        spyOn(GanttController, 'extendAncestorsIfNecessary');
        spyOn(GanttController, 'updateDescendantsTimeInterval').andCallThrough();
        GanttController.onFactorUpdated(1, parent);

        expect(GanttController.extendAncestorsIfNecessary).toHaveBeenCalled();
        expect(GanttController.updateDescendantsTimeInterval).toHaveBeenCalled();
        expect(GanttController.ensureNonZeroDuration).toHaveBeenCalled();
        expect(child.start_date).toEqual(parent.start_date);
        expect(gantt.updateTask).toHaveBeenCalledWith(child.id);
    });
});
