'use strict';

// TODO Update the tests to reflect the new usage of gantt controller
// And figure out how to handle updates to occurrence start and end time
describe('Tests for the gantt component controller', function () {

    var GanttController = require('../../js/components/investigation/GanttController'),
        gantt = jasmine.createSpyObj('gantt', ['init', 'clearAll', 'attachEvent', 'updateTask', 'refreshData']),
        props = jasmine.createSpyObj('props', ['onLinkAdded', 'onCreateFactor', 'onEditFactor', 'onDeleteLink']),
        investigation = {
            occurrence: {
                name: 'Test occurrence',
                startTime: Date.now(),
                endTime: Date.now() + 10000
            }
        };

    beforeEach(function () {
        props.updateOccurrence = function () {
        };
        gantt.config = {};
        gantt.templates = {};
        gantt.date = {};
        // The function that we want to mock have to exist
        gantt.getTask = function () {
        };
        gantt.getLink = function () {
        };
        gantt.calculateDuration = function (start, end) {
            return end.getTime() - start.getTime();
        };
        gantt.calculateEndDate = function (start, duration) {
            return new Date(start.getTime() + duration);
        };
        gantt.getChildren = function () {
            return [];
        };
        gantt.addTask = function (task) {
            return task.id;
        };
        gantt.batchUpdate = function (callback) {
            callback();
        };
        gantt.config.links = {
            "finish_to_start": "0",
            "start_to_start": "1",
            "finish_to_finish": "2",
            "start_to_finish": "3"
        };
        jasmine.getGlobal().gantt = gantt;
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
        var occurrence = investigation.occurrence, occurrenceEvent = {
            start_date: new Date(occurrence.startTime),
            end_date: new Date(occurrence.endTime),
            text: 'Test'
        };
        spyOn(gantt, 'getTask').and.returnValue(occurrenceEvent);
        occurrence.name = 'Updated text';
        GanttController.updateOccurrenceEvent(occurrence);

        expect(occurrenceEvent.text).toEqual(occurrence.name);
        expect(occurrenceEvent.start_date.getTime()).toEqual(occurrence.startTime);
    });

    it('Ensures that the occurrence event has never a duration less than 1 time unit', function () {
        var occurrence = investigation.occurrence, occurrenceEvent = {
            start_date: new Date(occurrence.startTime + 100),
            end_date: new Date(occurrence.startTime + 200),
            text: 'Test'
        };
        spyOn(gantt, 'getTask').and.returnValue(occurrenceEvent);
        spyOn(gantt, 'calculateDuration').and.callFake(function (start, end) {
            return (end.getTime() - start.getTime()) / 1000;
        });
        spyOn(gantt, 'calculateEndDate').and.returnValue(new Date());
        GanttController.updateOccurrenceEvent(occurrence);

        expect(gantt.calculateEndDate).toHaveBeenCalled();
    });

    it('Ensures parent event contains a newly added child event by expanding its time interval', function () {
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
        spyOn(gantt, 'getTask').and.returnValue(parent);
        spyOn(GanttController, 'extendAncestorsIfNecessary').and.callThrough();
        GanttController.onFactorAdded(2, child);

        expect(GanttController.extendAncestorsIfNecessary).toHaveBeenCalled();
        expect(parent.start_date).toEqual(child.start_date);
    });

    it('Ensures parent event contains an updated child by expanding its time interval', function () {
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
        spyOn(gantt, 'getTask').and.callFake(function (id) {
            return id === parent.id ? parent : child;
        });
        spyOn(GanttController, 'extendAncestorsIfNecessary').and.callThrough();
        spyOn(GanttController, 'updateDescendantsTimeInterval');
        GanttController.onFactorUpdated(2);

        expect(GanttController.extendAncestorsIfNecessary).toHaveBeenCalled();
        expect(GanttController.updateDescendantsTimeInterval).toHaveBeenCalled();
        expect(parent.start_date).toEqual(child.start_date);
        expect(parent.end_date).toEqual(child.end_date);
        expect(gantt.refreshData).toHaveBeenCalled();
    });

    it('Ensures child events shrink when parent event time interval is decreased', function () {
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
        spyOn(gantt, 'getTask').and.callFake(function (id) {
            return id === parent.id ? parent : child;
        });
        spyOn(gantt, 'getChildren').and.returnValue([child]);
        spyOn(GanttController, 'ensureNonZeroDuration');
        spyOn(GanttController, 'extendAncestorsIfNecessary');
        spyOn(GanttController, 'updateDescendantsTimeInterval').and.callThrough();
        GanttController.onFactorUpdated(1);

        expect(GanttController.extendAncestorsIfNecessary).toHaveBeenCalled();
        expect(GanttController.updateDescendantsTimeInterval).toHaveBeenCalled();
        expect(GanttController.ensureNonZeroDuration).toHaveBeenCalled();
        expect(child.start_date).toEqual(parent.start_date);
        expect(gantt.updateTask).toHaveBeenCalledWith(child.id);
    });

    it('Extends occurrence event accordingly if event is added which starts after occurrence event end', function () {
        var start = new Date(),
            occurrenceEvt = {
                id: 1,
                start_date: start,
                end_date: new Date(Date.now() + 1000)
            }, added = {
                id: 2,
                parent: 1,
                start_date: new Date(Date.now() + 2000),
                end_date: new Date(Date.now() + 3000)
            };
        spyOn(gantt, 'getTask').and.returnValue(occurrenceEvt);
        spyOn(gantt, 'addTask').and.callFake(function (task) {
            GanttController.onFactorAdded(task.id, task);
            return task.id;
        });
        spyOn(props, 'updateOccurrence');
        GanttController.addFactor(added, occurrenceEvt.id);

        expect(gantt.addTask).toHaveBeenCalled();
        expect(occurrenceEvt.start_date).toEqual(start);
        expect(occurrenceEvt.end_date).toEqual(added.end_date);
        expect(props.updateOccurrence).toHaveBeenCalledWith(occurrenceEvt.start_date.getTime(), added.end_date.getTime());
    });

    it('Passes the link, its source and target to the delete link handler', function () {
        var evt = {
            id: 1
        }, link = {
            id: 2,
            source: 1,
            target: 1
        };
        spyOn(gantt, 'getTask').and.returnValue(evt);
        spyOn(gantt, 'getLink').and.returnValue(link);
        GanttController.onDeleteLink(link.id);

        expect(props.onDeleteLink).toHaveBeenCalledWith(link, evt, evt);
    });

    it('Shrinks the occurrence event to span exactly its children', function () {
        var start = new Date(),
            occurrenceEvt = {
                id: 1,
                start_date: start,
                end_date: new Date(start.getTime() + 10000)
            }, child = {
                id: 2,
                parent: 1,
                start_date: start,
                end_date: new Date(start.getTime() + 5000)
            };
        spyOn(gantt, 'getTask').and.callFake(function (id) {
            return id === occurrenceEvt.id ? occurrenceEvt : child;
        });
        spyOn(gantt, 'getChildren').and.returnValue([child.id]);
        spyOn(GanttController, 'shrinkRootIfNecessary').and.callThrough();
        GanttController.onFactorUpdated(2);

        expect(occurrenceEvt.start_date).toEqual(start);
        expect(occurrenceEvt.end_date).toEqual(child.end_date);
        expect(GanttController.shrinkRootIfNecessary).toHaveBeenCalled();
    });

    it('Supports only adding links from finish to start', function () {
        var invalidLink = {
                id: 2,
                source: 1,
                target: 1,
                type: '1'
            },
            validLink = {
                id: 3,
                source: 1,
                target: 1,
                type: '0'
            };
        GanttController.onLinkAdded(invalidLink.id, invalidLink);

        expect(props.onLinkAdded).not.toHaveBeenCalled();

        GanttController.onLinkAdded(validLink.id, validLink);
        expect(props.onLinkAdded).toHaveBeenCalledWith(validLink);
    });

    it('Resizes factors when occurrence start time changes', function () {
        var occurrence = investigation.occurrence,
            start = new Date(occurrence.startTime),
            occurrenceEvt = {
                id: 1,
                start_date: start,
                duration: 2000,
                end_date: new Date(start.getTime() + 2000)
            }, child = {
                id: 2,
                parent: 1,
                start_date: start,
                duration: 1000,
                end_date: new Date(start.getTime() + 1000)
            },
            timeDiff = 1000;
        occurrence.endTime = occurrence.startTime + 2000;
        spyOn(gantt, 'getTask').and.callFake(function (id) {
            return id === occurrenceEvt.id ? occurrenceEvt : child;
        });
        spyOn(gantt, 'getChildren').and.callFake(function (id) {
            return id === occurrenceEvt.id ? [child] : [];
        });
        spyOn(GanttController, 'moveFactor').and.callThrough();
        spyOn(GanttController, 'applyUpdates').and.callThrough();
        GanttController.occurrenceEventId = occurrenceEvt.id;

        occurrence.startTime += timeDiff;
        GanttController.updateOccurrenceEvent(occurrence);

        expect(GanttController.moveFactor).toHaveBeenCalled();
        expect(GanttController.applyUpdates).toHaveBeenCalled();
        expect(occurrenceEvt.start_date).toEqual(new Date(start.getTime() + timeDiff));
        expect(occurrenceEvt.end_date).toEqual(new Date(occurrence.endTime));
        expect(occurrenceEvt.duration).toEqual(1000);
        expect(child.start_date).toEqual(new Date(start.getTime() + timeDiff));
    });

    xit('Moves factors by the same amount of time as the occurrence start time changes (backwards)', function () {
        var occurrence = investigation.occurrence,
            start = new Date(occurrence.startTime),
            occurrenceEvt = {
                id: 1,
                start_date: start,
                duration: 10000,
                end_date: new Date(occurrence.endTime)
            }, child = {
                id: 2,
                parent: 1,
                start_date: start,
                duration: 1000,
                end_date: new Date(start.getTime() + 1000)
            },
            timeDiff = -10000;
        spyOn(gantt, 'getTask').and.callFake(function (id) {
            return id === occurrenceEvt.id ? occurrenceEvt : child;
        });
        spyOn(gantt, 'getChildren').and.callFake(function (id) {
            return id === occurrenceEvt.id ? [child] : [];
        });
        spyOn(GanttController, 'moveFactor').and.callThrough();
        spyOn(GanttController, 'applyUpdates').and.callThrough();
        GanttController.occurrenceEventId = occurrenceEvt.id;

        occurrence.startTime = start.getTime() - timeDiff;
        GanttController.updateOccurrenceEvent(occurrence);

        expect(GanttController.moveFactor).toHaveBeenCalled();
        expect(GanttController.applyUpdates).toHaveBeenCalled();
        expect(occurrenceEvt.start_date).toEqual(new Date(start.getTime() + timeDiff));
        expect(occurrenceEvt.duration).toEqual(occurrence.endTime - (start.getTime() + timeDiff));
        expect(child.start_date).toEqual(new Date(start.getTime() + timeDiff));
        expect(child.end_date).toEqual(new Date(start.getTime() + timeDiff + child.duration));
    });

    it('Prevents occurrence event update when updates are being applied', function () {
        var oldName = 'Old name',
            occurrenceEvt = {
                id: 1,
                text: 'Old name',
                start_date: new Date(),
                end_date: new Date(Date.now() + 1000)
            },
            occurrence = {
                name: occurrenceEvt.text,
                occurrenceTime: occurrenceEvt.start_date.getTime()
            };
        spyOn(gantt, 'getTask').and.returnValue(occurrenceEvt);
        spyOn(props, 'updateOccurrence').and.callFake(function () {
            occurrence.name = 'Updated name';
            expect(GanttController.applyChangesRunning).toBeTruthy();
            GanttController.updateOccurrenceEvent(occurrence);
        });

        GanttController.applyUpdates([occurrenceEvt.id], false);

        expect(props.updateOccurrence).toHaveBeenCalled();
        expect(occurrenceEvt.text).toEqual(oldName);
        expect(GanttController.applyChangesRunning).toBeFalsy();
    });
});
