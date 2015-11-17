/**
 * @jsx
 */

'use strict';

var React = require('react');
var Jumbotron = require('react-bootstrap').Jumbotron;
var Button = require('react-bootstrap').Button;

var ReportsTable = require('./ReportsTable');
var ReportDetail = require('./ReportDetail');
var ReportRow = require('./ReportRow');
var Mask = require('./../Mask');

var Reports = React.createClass({
    render: function () {
        if (this.props.edit.editing) {
            return (<ReportDetail report={this.props.edit.editedReport} onCancelEdit={this.props.edit.onCancelEdit}/>);
        }
        var reports = this.props.reports;
        if (reports === null) {
            return (
                <Mask text='Loading reports...'/>
            )
        }
        if (reports.length === 0) {
            return (
                <div>
                    <Jumbotron>
                        <h2>INBAS Reporting</h2>

                        <p>There are no occurrence reports, yet.</p>
                        <Button bsStyle='primary' b sSize='small' onClick={this.props.edit.onCreateReport}>Create
                            Report</Button>
                    </Jumbotron>
                </div>
            );
        } else {
            return (
                <div>
                    <ReportsTable reports={reports} onEditReport={this.props.edit.onEditReport}
                                  rowComponent={ReportRow}/>

                    <div className='float-right'>
                        <Button bsStyle='primary' bsSize='small' onClick={this.props.edit.onCreateReport}>Create
                            Report</Button>
                    </div>
                </div>
            );
        }

    }
});

module.exports = Reports;
