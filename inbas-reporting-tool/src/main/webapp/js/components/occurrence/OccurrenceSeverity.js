/**
 * @jsx
 */

'use strict';

var React = require('react');
var Reflux = require('reflux');

var injectIntl = require('react-intl').injectIntl;

var Select = require('../Select');
var Actions = require('../../actions/Actions');
var OptionsStore = require('../../stores/OptionsStore');
var Utils = require('../../utils/Utils');
var I18nMixin = require('../../i18n/I18nMixin');

var OccurrenceSeverity = React.createClass({
    mixins: [Reflux.ListenerMixin, I18nMixin],
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
            <Select label={this.i18n('occurrence.class') + '*'}
                    title={this.i18n('occurrence.class-tooltip')}
                    value={this.state.severity} options={this.state.options} onChange={this.onChange}/>
        )
    }
});

module.exports = injectIntl(OccurrenceSeverity);
