/**
 * @jsx
 */

'use strict';

var React = require('react');
var Input = require('../Input');
var Utils = require('../../utils/Utils');

var DATE_FORMAT = '%d-%m-%y %H:%i';

var occurrenceGanttId = null;

function lightboxHeader(startDate, endDate, event) {
    var text = event.text && !event.$new ? event.text.substr(0, 70) : 'New Event';
    return gantt.templates.task_date(event.start_date) + "&nbsp;" + text;
}

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

function setGanttScale(scale) {
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

}

function taskAdded(id, item) {
    if (!item.parent) {
        item.parent = occurrenceGanttId;
    }
    resizeParentTask(id);
    item.durationUnit = gantt.config.duration_unit;
}

function taskDragged(id, mode) {
    if (mode !== 'resize' && mode !== 'move') {
        return;
    }
    resizeParentTask(id);
}

/**
 * Resizes parent task (and recursively all ancestor tasks) so that they always contain all of their subtasks
 * (time-wise).
 * @param taskId The last updated task
 * @param preventRefresh Whether gantt data refresh should be prevented
 */
function resizeParentTask(taskId, preventRefresh) {
    var task = gantt.getTask(taskId),
        parent;
    if (!task.parent) {
        return;
    }
    parent = gantt.getTask(task.parent);
    if (task.start_date < parent.start_date) {
        parent.start_date = task.start_date;
    }
    if (task.end_date > parent.end_date) {
        parent.end_date = task.end_date;
    }
    if (parent.parent) {
        resizeParentTask(parent.id, true);
    }
    if (!preventRefresh) {
        gantt.refreshData();
    }
}

function setLightboxDurationUnit(taskId) {
    // Don't do this at home, kids
    var durationContainer = document.getElementsByClassName('gantt_duration')[0],
        durationInput = durationContainer.childNodes[1],
        durationUnitTextNode = durationContainer.childNodes[3],
        task = gantt.getTask(taskId);
    durationInput.value = convertTaskDurationToCurrentUnit(task);
    switch (gantt.config.duration_unit) {
        case 'minute':
            durationUnitTextNode.textContent = ' Minutes ';
            break;
        case 'hour':
            durationUnitTextNode.textContent = ' Hours ';
            break;
        case 'second':
            durationUnitTextNode.textContent = ' Seconds ';
            break;
        default:
            console.warn('Unknown duration unit ' + gantt.config.duration_unit);
            break;
    }
    return true;
}

function convertTaskDurationToCurrentUnit(task) {
    var targetUnit = gantt.config.duration_unit;
    return Utils.convertTime(task.durationUnit, targetUnit, task.duration);
}

var Factors = React.createClass({

    propTypes: {
        occurrence: React.PropTypes.object.isRequired
    },

    getInitialState: function () {
        return {
            scale: 'minute'
        }
    },

    componentDidMount: function () {
        this.configureGantt();
        gantt.init('factors_gantt');
        gantt.clearAll();
        this.addOccurrenceEvent();
    },

    configureGantt: function () {
        gantt.config.columns = [
            {name: 'text', label: 'Event', width: '*', tree: true},
            {name: 'start_date', label: 'Start time', width: '*', align: 'center'},
            {name: 'add', label: '', width: 44}
        ];
        setGanttScale('minute');
        gantt.config.api_date = DATE_FORMAT;
        gantt.config.date_grid = DATE_FORMAT;
        gantt.config.fit_tasks = true;
        gantt.config.duration_step = 1;
        gantt.config.scroll_on_click = true;
        gantt.config.show_errors = false;   // Get rid of errors in case the grid has to resize
        gantt.config.drag_progress = false;
        gantt.templates.lightbox_header = lightboxHeader;
        gantt.attachEvent('onTaskCreated', function (task) {
            task.text = '';
            return true;
        });
        gantt.attachEvent('onAfterTaskAdd', taskAdded);
        gantt.attachEvent('onAfterTaskDrag', taskDragged);
        gantt.attachEvent('onAfterTaskUpdate', function (id, item) {
            item.durationUnit = gantt.config.duration_unit;
        });
        gantt.attachEvent('onLightbox', setLightboxDurationUnit);
        initSecondsScale();
    },

    addOccurrenceEvent: function () {
        var occurrence = this.props.occurrence,
            id = Date.now();
        gantt.addTask({
            id: id,
            text: occurrence.name,
            start_date: new Date(occurrence.occurrenceTime),
            duration: 1,
            readonly: true
        });
        occurrenceGanttId = id;
    },

    onScaleChange: function (e) {
        var scale = e.target.value;
        this.setState({scale: scale});
        setGanttScale(scale);
        gantt.render();
    },

    render: function () {
        return (
            <div>
                <div id='factors_gantt' className='factors-gantt'/>
                <div className='gantt-zoom'>
                    <div className='gantt-zoom-label bold'>Scale:</div>
                    <div className='col-xs-1'>
                        <Input type='radio' label='Seconds' value='second' checked={this.state.scale == 'second'}
                               onChange={this.onScaleChange}/>
                    </div>
                    <div className='col-xs-1'>
                        <Input type='radio' label='Minutes' value='minute' checked={this.state.scale == 'minute'}
                               onChange={this.onScaleChange}/>
                    </div>
                    <div className='col-xs-1'>
                        <Input type='radio' label='Hours' value='hour' checked={this.state.scale == 'hour'}
                               onChange={this.onScaleChange}/>
                    </div>
                </div>
            </div>);
    }
});

module.exports = Factors;
