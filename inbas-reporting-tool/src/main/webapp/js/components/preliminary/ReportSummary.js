/**
 * @jsx
 */
'use strict';

var React = require('react');
var IntlMixin = require('react-intl').IntlMixin;

var Input = require('../Input');

var ReportSummary = React.createClass({
    mixins: [IntlMixin],
    propTypes: {
        report: React.PropTypes.object.isRequired,
        onChange: React.PropTypes.func.isRequired
    },

    render: function () {
        var label = this.getIntlMessage('narrative');
        return <Input type='textarea' rows='8' label={label + '*'} name='summary'
                      placeholder={label}
                      value={this.props.report.summary} onChange={this.props.onChange}
                      title={this.getIntlMessage('preliminary.detail.narrative-tooltip')}/>;
    }
});

module.exports = ReportSummary;
