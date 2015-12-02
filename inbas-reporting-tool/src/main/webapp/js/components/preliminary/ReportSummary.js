/**
 * @jsx
 */
'use strict';

var React = require('react');

var Input = require('../Input');

var ReportSummary = React.createClass({
    propTypes: {
        report: React.PropTypes.object.isRequired,
        onChange: React.PropTypes.func.isRequired
    },

    render: function() {
        return <Input type='textarea' rows='8' label='Report Summary' name='summary'
                      placeholder='Report summary'
                      value={this.props.report.summary} onChange={this.props.onChange}
                      title='Report summary'/>;
    }
});

module.exports = ReportSummary;
