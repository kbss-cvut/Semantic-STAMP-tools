/**
 * Created by ledvima1 on 26.5.15.
 */

'use strict';

var React = require('react');
var Jumbotron = require('react-bootstrap').Jumbotron;
var Button = require('react-bootstrap').Button;
var ProgressBar = require('react-bootstrap').ProgressBar;

var ReportsTable = require('./ReportsTable');
var ReportEdit = require('./ReportEdit');

var Reports = React.createClass({
    render: function () {
        if (this.props.edit.editing) {
            return (
                <ReportEdit user={this.props.user} cancelEdit={this.props.edit.cancelEdit}
                            report={this.props.edit.editedReport}/>);
        } else {
            var reports = this.props.reports;
            if (reports === null) {
                return (
                    <ProgressBar active now={50}/>
                )
            }
            if (reports.length === 0) {
                return (
                    <div>
                        <Jumbotron>
                            <h2>INBAS Reporting</h2>

                            <p>There are no reports, yet.</p>
                            <Button bsStyle="primary" onClick={this.props.edit.createReport}>Create Report</Button>
                        </Jumbotron>
                    </div>
                );
            } else {
                return (
                    <div>
                        <ReportsTable reports={reports} edit={this.props.edit.startEdit}/>
                        <Button bsStyle="primary" onClick={this.props.edit.createReport}>Create Report</Button>
                    </div>
                );
            }
        }


    }
});

module.exports = Reports;
