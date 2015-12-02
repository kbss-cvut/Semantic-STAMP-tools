/**
 * @jsx
 */

'use strict';

var React = require('react');
var Jumbotron = require('react-bootstrap').Jumbotron;

var ReportsTable = require('./ReportsTable');
var ReportRow = require('./ReportRow');
var Mask = require('./../Mask');

var Reports = React.createClass({

    propTypes: {
        reports: React.PropTypes.array,
        rowComponent: React.PropTypes.func,     // A react component
        onEdit: React.PropTypes.func
    },

    render: function () {
        var reports = this.props.reports;
        if (reports === null) {
            return (
                <Mask text='Loading reports...'/>
            );
        }
        if (reports.length === 0) {
            return (
                <div>
                    <Jumbotron>
                        <h2>INBAS Reporting</h2>

                        <p>There are no occurrence reports, yet.</p>
                    </Jumbotron>
                </div>
            );
        } else {
            var rowComponent = this.props.rowComponent ? this.props.rowComponent : ReportRow;
            return (
                <div>
                    <ReportsTable {...this.props} rowComponent={rowComponent}/>
                </div>
            );
        }

    }
});

module.exports = Reports;
