/**
 * @jsx
 */

'use strict';

var React = require('react');
var Reflux = require('reflux');

var Select = require('../Select');
var Actions = require('../../actions/Actions');
var OptionsStore = require('../../stores/OptionsStore');
var Utils = require('../../utils/Utils');

var OccurrenceSeverity = React.createClass({
    mixins: [Reflux.ListenerMixin],
    getInitialState: function () {
        return {
            severity: this.props.severityAssessment ? this.props.severityAssessment : null,
            options: []
        };
    },

    componentDidMount: function () {
        this.listenTo(OptionsStore, this.onSeveritiesLoaded);
        Actions.loadOccurrenceSeverityOptions();
    },

    onSeveritiesLoaded: function (type, data) {
        if (type !== 'occurrenceSeverity') {
            return;
        }
        var options = [];
        for (var i = 0, len = data.length; i < len; i++) {
            options.push({label: Utils.constantToString(data[i], true), value: data[i]});
        }
        this.setState({options: options});
    },

    onChange: function (e) {
        var value = e.target.value;
        this.props.onChange('severityAssessment', value);
        this.setState({severity: value});
    },

    render: function () {
        return (
            <Select label='Occurrence class*' title='Occurrence class - this field is required'
                    value={this.state.severity} options={this.state.options} onChange={this.onChange}/>
        )
    }
});

module.exports = OccurrenceSeverity;
