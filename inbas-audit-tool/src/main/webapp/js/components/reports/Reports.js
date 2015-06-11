/**
 * Created by ledvima1 on 26.5.15.
 */

'use strict';

var React = require('react');
var assign = require('object-assign');
var Jumbotron = require('react-bootstrap').Jumbotron;
var Button = require('react-bootstrap').Button;
var ProgressBar = require('react-bootstrap').ProgressBar;
var ModalTrigger = require('react-bootstrap').ModalTrigger;
var OverlayMixin = require('react-bootstrap').OverlayMixin;

var ReportsTable = require('./ReportsTable');
var ReportEdit = require('./ReportEdit');
var WizardWindow = require('../wizard/WizardWindow');
var Actions = require('../../actions/Actions');

// Report wizard steps
var WizardSteps = require('./wizard/Steps');

var Reports = React.createClass({
    mixins: [OverlayMixin],
    submitReport: function (data, onSuccess, onError) {
        var report = data.report;
        report.lastEditedBy = this.props.user;
        if (report.uri) {
            Actions.updateReport(report, onSuccess, onError);
        }
        else {
            Actions.createReport(report, onSuccess, onError);
        }
    },
    renderOverlay: function () {
        if (this.props.edit.editing) {
            return (<WizardWindow steps={WizardSteps} title="Event Report Wizard" user={this.props.user}
                                  onFinish={this.submitReport}
                                  report={this.initReport(this.props.edit.editedReport)}
                                  onRequestHide={this.props.edit.cancelEdit}/>);
        } else {
            return <span/>
        }
    },
    render: function () {
        var reports = this.props.reports;
        if (reports === null) {
            return (
                <ProgressBar active now={50}/>
            )
        }
        var wizard = this.openWizardWindow();
        if (reports.length === 0) {
            return (
                <div>
                    <Jumbotron>
                        <h2>INBAS Reporting</h2>

                        <p>There are no reports, yet.</p>
                    </Jumbotron>
                </div>
            );
        } else {
            return (
                <div>
                    <ReportsTable reports={reports} edit={this.props.edit.startEdit}/>

                    {wizard}
                </div>
            );
        }

    },

    openWizardWindow: function () {
        var reportWizard = (<WizardWindow steps={WizardSteps} title="Event Report Wizard" user={this.props.user}
                                          onFinish={this.submitReport}
                                          report={this.initReport(this.props.edit.editedReport)}/>);
        return (
            <div>
                <ModalTrigger modal={reportWizard}>
                    <Button bsStyle="primary">Create Report</Button>
                </ModalTrigger>
            </div>
        );
        //return null;
    },
    initReport: function (report) {
        if (report !== null) {
            return assign({}, report);
        } else {
            return {
                eventTime: Date.now(),
                description: '',
                author: this.props.user
            }
        }
    }
});

module.exports = Reports;
