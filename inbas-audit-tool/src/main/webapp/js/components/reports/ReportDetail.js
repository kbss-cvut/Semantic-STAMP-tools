/**
 * Created by ledvima1 on 27.5.15.
 */

'use strict';

var React = require('react');
var Button = require('react-bootstrap').Button;
var Input = require('react-bootstrap').Input;
var Panel = require('react-bootstrap').Panel;
var Alert = require('react-bootstrap').Alert;
var Table = require('react-bootstrap').Table;
var assign = require('object-assign');
var DateTimePicker = require('react-bootstrap-datetimepicker');

var Actions = require('../../actions/Actions');
var Utils = require('../../utils/Utils');
var ReportStatements = require('./ReportStatements');

var ReportEdit = React.createClass({
    getInitialState: function () {
        if (!this.isReportNew()) {
            return {
                report: this.initReport(this.props.report),
                submitting: false,
                error: null
            };
        }
        return {
            report: this.initReport(null),
            submitting: false,
            error: null
        };
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
    },
    isReportNew: function () {
        return this.props.report == null;
    },
    onChange: function (e) {
        var change;
        if (e.target.name === 'description') {
            change = {description: e.target.value};
        } else {
            change = {name: e.target.value};
        }
        this.setState(assign(this.state.report, change));
    },
    onDateChange: function (value) {
        this.setState(assign(this.state.report, {eventTime: new Date(Number(value))}));
    },
    onUpdateReport: function(value) {
        this.setState(assign(this.state.report, value));
    },
    onSubmit: function (e) {
        e.preventDefault();
        this.setState(assign(this.state, {submitting: true}));
        this.state.report.lastEditedBy = this.props.user;
        if (this.isReportNew()) {
            Actions.createReport(this.state.report, this.onSubmitError);
        }
        else {
            Actions.updateReport(this.state.report, this.onSubmitError);
        }
    },
    onSubmitError: function (error) {
        this.setState(assign({}, this.state, {
            error: error,
            submitting: false
        }));
    },
    handleAlertDismiss: function () {
        this.setState(assign({}, this.state, {error: null}));
    },
    render: function () {
        var author = this.state.report.author.firstName + " " + this.state.report.author.lastName;
        var loading = this.state.submitting;
        var alert = this.renderError();
        var lastEdited = this.renderLastEdited();
        return (
            <Panel header="Edit Event Report">
                <form>
                    <div className="form-group report-detail">
                        <Input type="text" name="name" value={this.state.report.name} onChange={this.onChange}
                               label="Report Name" title="Short descriptive name for this report"/>
                    </div>
                    <div className="form-group report-detail">
                        <Input type="text" value={author} label="Author" title="Report author" disabled/>
                    </div>
                    <div className="picker-container form-group report-detail">
                        <label className="control-label">Event Time</label>
                        <DateTimePicker inputFormat="DD-MM-YY hh:mm A" dateTime={this.state.report.eventTime.toString()}
                                        onChange={this.onDateChange}
                                        inputProps={{title: 'Date and time when the event occurred'}}/>
                    </div>

                    {lastEdited}

                    <div className="form-group">
                        <Input type="textarea" rows="8" label="Description" name="description"
                               placeholder="Event description"
                               value={this.state.report.description} onChange={this.onChange}
                               title="Event description"/>
                    </div>

                    <div className="form-group">
                        <ReportStatements report={this.state.report} onUpdateReport={this.onUpdateReport} />
                        </div>

                    <div className="form-group">
                        <Button bsStyle="success" disabled={loading || this.state.report.description === ''}
                                ref="submit"
                                onClick={this.onSubmit}>{loading ? 'Submitting...' : 'Submit'}</Button>
                        <Button bsStyle="link" title="Discard changes" onClick={this.props.onCancelEdit}>Cancel</Button>
                    </div>

                    {alert}

                </form>
            </Panel>
        );
    },
    renderError: function () {
        return this.state.error ? (
            <div className="form-group">
                <Alert bsStyle="danger" onDismiss={this.handleAlertDismiss} dismissAfter={10000}>
                    <p>Unable to submit report. Server responded with message: {this.state.error.message}</p>
                </Alert>
            </div>
        ) : null;
    },
    renderLastEdited: function () {
        if (this.isReportNew()) {
            return null;
        }
        var formattedDate = Utils.formatDate(new Date(this.state.report.lastEdited));
        var lastEditor = this.state.report.lastEditedBy.firstName + ' ' + this.state.report.lastEditedBy.lastName;
        return (
            <div style={{overflow: 'hidden'}}>
                <div className="report-detail" style={{float: 'left'}}>
                    <Input type="text" label="Last Edited" value={formattedDate}
                           title="Date of last editing of this report" disabled/>
                </div>
                <div className="report-detail" style={{float: 'left', margin: '0em 0em 0em 3em'}}>
                    <Input type="text" label="By" value={lastEditor} title="The user who edited this report last"
                           disabled/>
                </div>
            </div>
        )
    }
});

module.exports = ReportEdit;
