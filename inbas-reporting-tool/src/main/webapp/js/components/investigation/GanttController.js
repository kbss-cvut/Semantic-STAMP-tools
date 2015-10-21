'use strict';

var FactorStyleInfo = require('../../utils/FactorStyleInfo');

var DATE_FORMAT = '%d-%m-%y %H:%i';

/**
 * Initializes time scale in seconds.
 */
function initSecondsScale() {
    gantt.date.second_start = function (date) {
        date.setMilliseconds(0);
        return date;
    };
    gantt.date.add_second = function (date, inc) {
        return new Date(date.valueOf() + 1000 * inc);
    }
}

/**
 * Deals with management of the gantt component.
 */
var GanttController = {

    occurrenceEventId: null,
    props: {},

    setScale: function (scale) {
        switch (scale) {
            case 'minute':
                gantt.config.scale_unit = 'minute';
                gantt.config.date_scale = '%H:%i';
                gantt.config.duration_unit = 'minute';
                gantt.config.min_duration = 60 * 1000;  // Duration in millis
                gantt.config.scale_height = 30;
                gantt.config.min_column_width = 50;
                gantt.config.subscales = [];
                break;
            case 'hour':
                gantt.config.scale_unit = 'hour';
                gantt.config.date_scale = '%H';
                gantt.config.duration_unit = 'hour';
                gantt.config.min_duration = 60 * 60 * 1000;  // Duration in millis
                gantt.config.scale_height = 30;
                gantt.config.min_column_width = 50;
                gantt.config.subscales = [];
                break;
            case 'second':
                gantt.config.scale_unit = 'second';
                gantt.config.date_scale = '%s';
                gantt.config.duration_unit = 'second';
                gantt.config.min_duration = 1000;  // Duration in millis
                gantt.config.scale_height = 54;
                gantt.config.min_column_width = 23;
                gantt.config.subscales = [
                    {unit: 'minute', step: 1, date: '%H:%i'}
                ];
                break;
            default:
                console.warn('Unsupported gantt scale ' + scale);
                break;
        }
        gantt.render();
    },

    init: function (config) {
        this.props = config;
        this.configureGanttConfig();
        this.configureGanttHandlers();
        this.configureGanttTemplates();
        initSecondsScale();
        gantt.init('factors_gantt');
        gantt.clearAll();
    },

    configureGanttConfig: function () {
        gantt.config.columns = [
            {name: 'text', label: 'Event', width: '*', tree: true},
            {name: 'start_date', label: 'Start time', width: '*', align: 'center'},
            {name: 'add', label: '', width: 44}
        ];
        gantt.config.api_date = DATE_FORMAT;
        gantt.config.date_grid = DATE_FORMAT;
        gantt.config.fit_tasks = true;
        gantt.config.duration_step = 1;
        gantt.config.scroll_on_click = true;
        gantt.config.show_errors = false;   // Get rid of errors in case the grid has to resize
        gantt.config.drag_progress = false;
        gantt.config.link_line_width = 3;
        gantt.config.link_arrow_size = 8;
    },

    configureGanttTemplates: function () {
        gantt.templates.link_class = function (link) {
            if (link.factorType === 'cause') {
                return 'gantt-link-causes';
            } else {
                return 'gantt-link-mitigates';
            }
        };
        gantt.templates.task_class = function(start, end, task) {
            var eventType;
            if (!task.statement) {
                return 'factor-occurrence-event';
            }
            eventType = task.statement.eventType;
            return FactorStyleInfo.getStyleInfo(eventType.type).cls;
        }
    },

    configureGanttHandlers: function () {
        var me = this;
        gantt.attachEvent('onTaskCreated', this.onCreateFactor.bind(me));
        gantt.attachEvent('onTaskDblClick', this.onEditFactor.bind(me));
        gantt.attachEvent('onAfterTaskAdd', this.onFactorAdded.bind(me));
        gantt.attachEvent('onAfterTaskDrag', this.onFactorUpdated.bind(me));
        gantt.attachEvent('onBeforeLinkAdd', this.onLinkAdded.bind(me));
        gantt.attachEvent('onLinkDblClick', this.onDeleteLink.bind(me));
    },

    onCreateFactor: function (factor) {
        factor.isNew = true;
        factor.text = '';
        factor.durationUnit = gantt.config.duration_unit;
        this.props.onCreateFactor(factor);
        return false;
    },

    onEditFactor: function (id, e) {
        if (!id) {
            return true;
        }
        e.preventDefault();
        if (Number(id) === this.occurrenceEventId) {
            return;
        }
        var factor = gantt.getTask(id);
        this.props.onEditFactor(factor);
    },

    onFactorAdded: function (id, factor) {
        var updates = [];
        if (id !== this.occurrenceEventId && !factor.parent) {
            factor.parent = this.occurrenceEventId;
        }
        this.extendAncestorsIfNecessary(factor, updates);
        this.applyUpdates(updates);
    },

    onFactorUpdated: function (id) {
        var updates = [],
            factor = gantt.getTask(id);
        factor.durationUnit = gantt.config.duration_unit;
        this.extendAncestorsIfNecessary(factor, updates);
        this.updateDescendantsTimeInterval(factor, updates);
        this.shrinkRootIfNecessary(updates);
        this.applyUpdates(updates);
    },

    extendAncestorsIfNecessary: function (factor, updates) {
        var parent, changed;
        if (!factor.parent) {
            return;
        }
        parent = gantt.getTask(factor.parent);
        if (factor.start_date < parent.start_date) {
            parent.start_date = factor.start_date;
            changed = true;
        }
        if (factor.end_date > parent.end_date) {
            parent.end_date = factor.end_date;
            changed = true;
        }
        if (changed) {
            updates.push(parent.id);
            this.extendAncestorsIfNecessary(parent, updates);
        }
    },

    /**
     * Updates descendants' time intervals.
     *
     * Does recursion on changed children and adds the changes to the updates array, so that they can be applied in a
     * batch later.
     */
    updateDescendantsTimeInterval: function (factor, updates) {
        var children = gantt.getChildren(factor.id),
            child, changed;
        for (var i = 0, len = children.length; i < len; i++) {
            child = gantt.getTask(children[i]);
            changed = false;
            if (child.start_date < factor.start_date) {
                child.start_date = factor.start_date;
                changed = true;
            }
            if (child.end_date > factor.end_date) {
                child.end_date = factor.end_date;
                changed = true;
            }
            if (changed) {
                this.ensureNonZeroDuration(child);
                updates.push(child.id);
                this.updateDescendantsTimeInterval(child, updates);
            }
        }
    },

    ensureNonZeroDuration: function (event) {
        if (gantt.calculateDuration(event.start_date, event.end_date) < 1) {
            event.end_date = gantt.calculateEndDate(event.start_date, 1, gantt.config.scale_unit);
        }
    },

    shrinkRootIfNecessary: function (updates) {
        var root = gantt.getTask(this.occurrenceEventId),
            children = gantt.getChildren(root.id),
            lowestStart, highestEnd, changed, child;
        if (!children || children.length === 0) {
            return;
        }
        child = gantt.getTask(children[0]);
        lowestStart = child.start_date;
        highestEnd = child.end_date;
        for (var i = 1, len = children.length; i < len; i++) {
            child = gantt.getTask(children[i]);
            if (child.start_date < lowestStart) {
                lowestStart = child.start_date;
            }
            if (child.end_date > highestEnd) {
                highestEnd = child.end_date;
            }
        }
        if (root.start_date < lowestStart) {
            root.start_date = lowestStart;
            changed = true;
        }
        if (root.end_date > highestEnd) {
            root.end_date = highestEnd;
            changed = true;
        }
        if (changed) {
            var duration = gantt.calculateDuration(root.start_date, root.end_date);
            root.duration = duration;
            root.end_date = gantt.calculateEndDate(root.start_date, duration, gantt.config.scale_unit);
            updates.push(root.id);
        }
    },

    applyUpdates: function (updates) {
        var me = this, updateOccurrenceEvt = false;
        gantt.batchUpdate(function () {
            for (var i = 0, len = updates.length; i < len; i++) {
                gantt.updateTask(updates[i]);
                if (updates[i] === me.occurrenceEventId) {
                    updateOccurrenceEvt = true;
                }
            }
        });
        if (updateOccurrenceEvt) {
            var root = gantt.getTask(this.occurrenceEventId);
            this.props.updateOccurrence(root.start_date.getTime(), root.end_date.getTime());
        }
        gantt.refreshData();
    },

    onLinkAdded: function (linkId, link) {
        var linkTypes = gantt.config.links;
        // Only links from end to start are supported
        if (link.type !== linkTypes.finish_to_start) {
            return false;
        }
        if (link.factorType) {
            return true;
        }
        this.props.onLinkAdded(link);
        return false;
    },

    onDeleteLink: function (linkId) {
        var link = gantt.getLink(linkId),
            source = gantt.getTask(link.source),
            target = gantt.getTask(link.target);
        this.props.onDeleteLink(link, source, target);
    },

    updateOccurrenceEvent: function (occurrence) {
        var occurrenceEvt = gantt.getTask(this.occurrenceEventId),
            changes = false, startDate;
        if (occurrenceEvt.text !== occurrence.name) {
            occurrenceEvt.text = occurrence.name;
            changes = true;
        }
        startDate = new Date(occurrence.occurrenceTime);
        if (occurrenceEvt.start_date !== startDate) {
            occurrenceEvt.start_date = startDate;
            changes = true;
        }
        if (changes) {
            this.ensureNonZeroDuration(occurrenceEvt);
            gantt.updateTask(occurrenceEvt.id);
        }
    },

    setOccurrenceEventId: function (id) {
        this.occurrenceEventId = id;
    },

    addFactor: function (factor, parentId) {
        return gantt.addTask(factor, parentId);
    },

    addLink: function (link) {
        gantt.addLink(link);
    },

    updateFactor: function (factor) {
        gantt.updateTask(factor.id);
        this.onFactorUpdated(factor.id);
    },

    getFactor: function (factorId) {
        return gantt.getTask(factorId);
    },

    expandSubtree: function (rootId) {
        gantt.open(rootId);
    },

    deleteLink: function (linkId) {
        gantt.deleteLink(linkId);
    },

    deleteFactor: function (factorId) {
        gantt.deleteTask(factorId);
    }
};

module.exports = GanttController;
