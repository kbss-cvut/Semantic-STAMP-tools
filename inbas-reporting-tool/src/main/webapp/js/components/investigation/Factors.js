/**
 * @jsx
 */

'use strict';

var React = require('react');
var Input = require('../Input');

var DATE_FORMAT = '%d-%m-%y %h:%i %A';

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
            gantt.config.date_scale = '%h:%i %A';
            gantt.config.duration_unit = 'minute';
            gantt.config.min_duration = 60 * 1000;  // Duration in millis
            gantt.config.scale_height = 30;
            gantt.config.min_column_width = 70;
            gantt.config.subscales = [];
            break;
        case 'hour':
            gantt.config.scale_unit = 'hour';
            gantt.config.date_scale = '%h %A';
            gantt.config.duration_unit = 'hour';
            gantt.config.min_duration = 60 * 60 * 1000;  // Duration in millis
            gantt.config.scale_height = 30;
            gantt.config.min_column_width = 70;
            gantt.config.subscales = [];
            break;
        case 'second':
            gantt.config.scale_unit = 'second';
            gantt.config.date_scale = '%s';
            gantt.config.duration_unit = 'second';  // TODO This does not work in the lightbox form
            gantt.config.min_duration = 1000;  // Duration in millis
            gantt.config.scale_height = 54;
            gantt.config.min_column_width = 50;
            gantt.config.subscales = [
                {unit: 'minute', step: 1, date: '%h:%i %A'}
            ];
            break;
        default:
            console.warn('Unsupported gantt scale ' + scale);
            break;
    }

}

function linkAdded(id, link) {
    console.log(id);
}

function taskAdded(id) {
    resizeParentTask(id);
}

function taskDragged(id, mode) {
    if (mode !== 'resize') {
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
        gantt.attachEvent('onAfterLinkAdd', linkAdded);
        gantt.attachEvent('onTaskCreated', function (task) {
            task.text = '';
            return true;
        });
        gantt.attachEvent('onAfterTaskAdd', taskAdded);
        gantt.attachEvent('onAfterTaskDrag', taskDragged);
        initSecondsScale();
    },

    addOccurrenceEvent: function () {
        var occurrence = this.props.occurrence;
        gantt.addTask({
            id: Date.now(),
            text: occurrence.name,
            start_date: new Date(occurrence.occurrenceTime),
            duration: 1,
            readonly: true
        });
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
