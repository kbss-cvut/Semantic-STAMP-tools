/**
 * @author ledvima1
 */

'use strict';

var React = require('react');
var Reflux = require('reflux');

var Select = require('../Select');
var Actions = require('../../actions/Actions');
var OptionsStore = require('../../stores/OptionsStore');

var EventSeverity = React.createClass({
    mixins: [Reflux.ListenerMixin],
    getInitialState: function () {
        return {
            severity: this.props.severityAssessment ? this.props.severityAssessment.severity : null,
            options: []
        };
    },

    componentDidMount: function () {
        this.listenTo(OptionsStore, this.onSeveritiesLoaded);
        Actions.loadEventSeverityOptions();
    },

    onSeveritiesLoaded: function (severities) {
        var options = [];
        for (var i = 0, len = severities.length; i < len; i++) {
            options.push({label: severities[i], value: severities[i]});
        }
        this.setState({options: options});
    },

    onChange: function(e) {
        var value = e.target.value;
        var change = {severity: value};
        this.props.onChange('severityAssessment', change);
        this.setState(change);
    },

    render: function () {
        return (
            <Select label='Event Severity Assessment' value={this.state.severity} options={this.state.options}
                    onChange={this.onChange}/>
        )
    }
});

module.exports = EventSeverity;
