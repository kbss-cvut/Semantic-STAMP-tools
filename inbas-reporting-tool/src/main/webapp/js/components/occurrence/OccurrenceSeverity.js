/**
 * @jsx
 */

'use strict';

var React = require('react');
var Reflux = require('reflux');

var injectIntl = require('../../utils/injectIntl');

var Select = require('../Select');
var OptionsStore = require('../../stores/OptionsStore');
var Utils = require('../../utils/Utils');
var I18nMixin = require('../../i18n/I18nMixin');

var OccurrenceSeverity = React.createClass({
    mixins: [Reflux.ListenerMixin, I18nMixin],
    getInitialState: function () {
        return {
            severity: this.props.severityAssessment ? this.props.severityAssessment : null,
            options: OptionsStore.getOccurrenceSeverityOptions()
        };
    },

    componentDidMount: function () {
        this.listenTo(OptionsStore, this.onSeveritiesLoaded);
    },

    onSeveritiesLoaded: function (type, data) {
        if (type !== 'occurrenceSeverity') {
            return;
        }
        this.setState({options: data});
    },

    onChange: function (e) {
        var value = e.target.value;
        this.props.onChange('severityAssessment', value);
        this.setState({severity: value});
    },

    _prepareOptions: function() {
        var options = this.state.options,
            toRender = [];
        for (var i = 0, len = options.length; i < len; i++) {
            toRender.push({label: Utils.constantToString(options[i], true), value: options[i]});
        }
        return toRender;
    },

    render: function () {
        return (
            <Select label={this.i18n('occurrence.class') + '*'}
                    title={this.i18n('occurrence.class-tooltip')}
                    value={this.state.severity} options={this._prepareOptions()} onChange={this.onChange}/>
        )
    }
});

module.exports = injectIntl(OccurrenceSeverity);
