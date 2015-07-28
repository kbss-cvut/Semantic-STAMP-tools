/**
 * Created by ledvima1 on 26.5.15.
 */

'use strict';

var React = require('react');
var assign = require('object-assign');
var Jumbotron = require('react-bootstrap').Jumbotron;
var Button = require('react-bootstrap').Button;
var ProgressBar = require('react-bootstrap').ProgressBar;

var ReportsTable = require('./ReportsTable');
var ReportDetail = require('./ReportDetail');
var WizardWindow = require('../wizard/WizardWindow');
var Actions = require('../../actions/Actions');

// Report wizard steps
var WizardSteps = require('./wizard/Steps');

var Reports = React.createClass({
    render: function () {
        if (this.props.edit.editing) {
            return (<ReportDetail user={this.props.user} report={this.props.edit.editedReport} onCancelEdit={this.props.edit.onCancelEdit}/>);
        }
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
                        <Button bsStyle="primary" onClick={this.props.edit.onCreateReport}>Create Report</Button>
                    </Jumbotron>
                </div>
            );
        } else {
            return (
                <div>
                    <ReportsTable reports={reports} onEditReport={this.props.edit.onEditReport}/>

                    <Button bsStyle="primary" onClick={this.props.edit.onCreateReport}>Create Report</Button>
                </div>
            );
        }

    }
});

module.exports = Reports;
