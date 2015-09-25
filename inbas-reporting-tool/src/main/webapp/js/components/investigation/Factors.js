'use strict';

var React = require('react');

var DATE_FORMAT = '%d-%m-%y %h:%i %A';

function lightboxHeader(startDate, endDate, event) {
    var text = event.text && !event.$new ? event.text.substr(0, 70) : 'New Event';
    return gantt.templates.task_date(event.start_date) + "&nbsp;" + text;
}

function linkAdded(id, link) {
    console.log(id);
}

var Factors = React.createClass({

    propTypes: {
        occurrence: React.PropTypes.object.isRequired
    },

    componentDidMount: function () {
        this.configureGantt();
        gantt.init('factors_gantt');
        gantt.attachEvent('onTaskCreated', function (task) {
            task.text = '';
            return true;
        });
        this.addOccurrenceEvent();
    },

    configureGantt: function () {
        gantt.config.columns = [
            {name: 'text', label: 'Event', width: '*', tree: true},
            {name: 'start_date', label: 'Start time', width: '*', align: 'center'},
            {name: 'add', label: '', width: 44}
        ];
        gantt.config.scale_unit = 'minute';
        gantt.config.api_date = DATE_FORMAT;
        gantt.config.date_scale = '%h:%i %A';
        gantt.config.date_grid = DATE_FORMAT;
        gantt.config.fit_tasks = true;
        gantt.config.duration_unit = 'minute';
        gantt.config.duration_step = 1;
        gantt.config.scroll_on_click = true;
        gantt.config.show_errors = false;   // Get rid of errors in case the grid has to resize
        gantt.config.drag_progress = false;
        gantt.templates.lightbox_header = lightboxHeader;
        gantt.attachEvent('onAfterLinkAdd', linkAdded);
    },

    addOccurrenceEvent: function () {
        var occurrence = this.props.occurrence;
        gantt.addTask({
            id: 1,
            text: occurrence.name,
            start_date: new Date(occurrence.occurrenceTime),
            duration: 1,
            readonly: true
        });
    },

    render: function () {
        return (<div id='factors_gantt' className='factors-gantt'/>);
    }
});

module.exports = Factors;
