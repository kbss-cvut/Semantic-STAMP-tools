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
        return <Input type='textarea' rows='8' label='Narrative*' name='summary'
                      placeholder='Narrative'
                      value={this.props.report.summary} onChange={this.props.onChange}
                      title='Narrative -  this field is required'/>;
    }
});

module.exports = ReportSummary;
