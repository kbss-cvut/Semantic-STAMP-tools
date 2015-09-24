'use strict';

var React = require('react');

var Utils = require('../../utils/Utils');

var DATE_FORMAT = '%d-%m-%y %h:%i %A';

var Factors = React.createClass({

    propTypes: {
        occurrence: React.PropTypes.object.isRequired
    },

    componentDidMount: function () {
        this.configureGantt();
        gantt.init('factors_gantt');
        this.addOccurrenceEvent();
    },

    configureGantt: function () {
        gantt.config.columns = [
            {name: "text", label: "Event", width: "*", tree: true},
            {name: "start_date", label: "Start time", align: "center"},
            {name: "duration", label: "Duration", align: "center"},
            {name: "add", label: "", width: 44}
        ];
        gantt.config.scale_unit = 'minute';
        gantt.config.api_date = DATE_FORMAT;
        gantt.config.date_scale = '%h:%i %A';
        gantt.config.fit_tasks = true;
        gantt.config.duration_unit = 'minute';
        gantt.config.duration_step = 1;
        gantt.config.scroll_on_click = true;
        //gantt.config.drag_resize = true;
        gantt.config.drag_progress = false;
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
        var style = {
            width: '100%',
            height: '400px'
        };
        return (<div id='factors_gantt' style={style}></div>);
    }
});

module.exports = Factors;

