'use strict';

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
                gantt.config.min_column_width = 70;
                gantt.config.subscales = [];
                break;
            case 'hour':
                gantt.config.scale_unit = 'hour';
                gantt.config.date_scale = '%H';
                gantt.config.duration_unit = 'hour';
                gantt.config.min_duration = 60 * 60 * 1000;  // Duration in millis
                gantt.config.scale_height = 30;
                gantt.config.min_column_width = 70;
                gantt.config.subscales = [];
                break;
            case 'second':
                gantt.config.scale_unit = 'second';
                gantt.config.date_scale = '%s';
                gantt.config.duration_unit = 'second';
                gantt.config.min_duration = 1000;  // Duration in millis
                gantt.config.scale_height = 54;
                gantt.config.min_column_width = 50;
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
    },

    configureGanttHandlers: function () {
        var me = this;
        gantt.attachEvent('onTaskCreated', this.onCreateFactor.bind(me));
        gantt.attachEvent('onTaskDblClick', this.onEditFactor.bind(me));
        gantt.attachEvent('onAfterTaskAdd', this.onFactorAdded.bind(me));
        gantt.attachEvent('onAfterTaskUpdate', this.onFactorUpdated.bind(me));
        gantt.attachEvent('onBeforeLinkAdd', this.onLinkAdded.bind(me));
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
        if (id !== this.occurrenceEventId && !factor.parent) {
            factor.parent = this.occurrenceEventId;
        }
        this.extendAncestorsIfNecessary(factor);
    },

    onFactorUpdated: function (id, factor) {
        factor.durationUnit = gantt.config.duration_unit;
        this.extendAncestorsIfNecessary(factor);
        this.updateDescendantsTimeInterval(factor);
        gantt.refreshData();
    },

    extendAncestorsIfNecessary: function (factor) {
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
            if (parent.id === this.occurrenceEventId) {
                this.props.updateOccurrence(factor.start_date.getTime(), factor.end_date.getTime());
            }
            gantt.updateTask(parent.id);
        }
    },

    /**
     * Updates descendants' time intervals. It doesn't do explicit recursion, but the updateTask call fires an update
     * event which in turn leads to calling this method again.
     * @param factor
     */
    updateDescendantsTimeInterval: function (factor) {
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
                gantt.updateTask(child.id);
            }
        }
    },

    ensureNonZeroDuration: function (event) {
        if (gantt.calculateDuration(event.start_date, event.end_date) < 1) {
            event.end_date = gantt.calculateEndDate(event.start_date, 1, gantt.config.scale_unit);
        }
    },

    onLinkAdded: function (linkId, link) {
        if (link.factorType) {
            return true;
        }
        this.props.onLinkAdded(link);
        return false;
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
    },

    getFactor: function (factorId) {
        return gantt.getTask(factorId);
    },

    expandSubtree: function (rootId) {
        gantt.open(rootId);
    }
};

module.exports = GanttController;
