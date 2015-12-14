/**
 * @jsx
 */
'use strict';

var React = require('react');
var injectIntl = require('react-intl').injectIntl;

var Input = require('../Input');
var I18nMixin = require('../../i18n/I18nMixin');

var ReportSummary = React.createClass({
    mixins: [I18nMixin],
    propTypes: {
        report: React.PropTypes.object.isRequired,
        onChange: React.PropTypes.func.isRequired
    },

    render: function () {
        var label = this.i18n('narrative');
        return <Input type='textarea' rows='8' label={label + '*'} name='summary'
                      placeholder={label}
                      value={this.props.report.summary} onChange={this.props.onChange}
                      title={this.i18n('preliminary.detail.narrative-tooltip')}/>;
    }
});

module.exports = injectIntl(ReportSummary);
