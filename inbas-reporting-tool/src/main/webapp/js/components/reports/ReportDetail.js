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
var ReportsStore = require('../../stores/ReportsStore');
var UserStore = require('../../stores/UserStore');
var router = require('../../utils/router');

// TODO How to cancel the detail without explicitly calling router?
// The same for submit

var ReportEdit = React.createClass({
    getInitialState: function () {
        return {
            report: this.initReport(),
            submitting: false,
            error: null
        };
    },
    initReport: function () {
        if (!this.isReportNew()) {
            return assign({}, ReportsStore.getReport(this.props.params.reportKey));
        } else {
            return {
                eventTime: Date.now(),
                description: '',
                author: UserStore.getCurrentUser()
            }
        }
    },
    isReportNew: function () {
        return this.props.params.reportKey == null;
    },
    onChange: function (e) {
        var value = e.target.value;
        var attributeName = e.target.name;
        this.state.report[attributeName] = value;
        this.setState(assign(this.state, {report: this.state.report}));
    },
    onDateChange: function (value) {
        this.setState(assign(this.state.report, {eventTime: new Date(Number(value))}));
    },
    onUpdateReport: function (value) {
        this.setState(assign(this.state.report, value));
    },
    onSubmit: function (e) {
        e.preventDefault();
        this.setState(assign(this.state, {submitting: true}));
        this.state.report.lastEditedBy = UserStore.getCurrentUser();
        if (this.isReportNew()) {
            Actions.createReport(this.state.report, this.onSubmitSuccess, this.onSubmitError);
        }
        else {
            Actions.updateReport(this.state.report, this.onSubmitSuccess, this.onSubmitError);
        }
    },
    onSubmitSuccess: function() {
        router.transitionTo(this.props.query.onSuccess);
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
    onCancel: function() {
        router.transitionTo(this.props.query.onCancel);
    },
    render: function () {
        var authorName = this.state.report.author.firstName + " " + this.state.report.author.lastName;
        var loading = this.state.submitting;
        var alert = this.renderError();
        var lastEdited = this.renderLastEdited();
        var author = this.isReportNew() ? null : (<div className="form-group report-detail">
            <Input type="text" value={authorName} label="Author" title="Report author" disabled/>
        </div>);
        return (
            <Panel header="Edit Event Report">
                <form>
                    <div className="form-group report-detail">
                        <Input type="text" name="name" value={this.state.report.name} onChange={this.onChange}
                               label="Report Name" title="Short descriptive name for this report"/>
                    </div>

                    {author}

                    <div className="picker-container form-group report-detail">
                        <label className="control-label">Event Time</label>
                        <DateTimePicker inputFormat="DD-MM-YY hh:mm A" dateTime={this.state.report.eventTime.toString()}
                                        onChange={this.onDateChange}
                                        inputProps={{title: 'Date and time when the event occurred'}}/>
                    </div>

                    {lastEdited}

                    <div className='form-group'>
                        <Input type='textarea' rows='3' label='Factors' name='factors' placeholder='Factors'
                               value={this.state.report.factors} onChange={this.onChange} title='Event factors'/>
                    </div>

                    <div className="form-group">
                        <Input type="textarea" rows="8" label="Description" name="description"
                               placeholder="Event description"
                               value={this.state.report.description} onChange={this.onChange}
                               title="Event description"/>
                    </div>

                    <div className="form-group">
                        <ReportStatements report={this.state.report} onUpdateReport={this.onUpdateReport}/>
                    </div>

                    <div className="form-group">
                        <Button bsStyle="success" disabled={loading || this.state.report.description === ''}
                                ref="submit"
                                onClick={this.onSubmit}>{loading ? 'Submitting...' : 'Submit'}</Button>
                        <Button bsStyle="link" title="Discard changes" onClick={this.onCancel}>Cancel</Button>
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
                <div className="report-detail-float">
                    <Input type="text" label="Last Edited" value={formattedDate}
                           title="Date of last editing of this report" disabled/>
                </div>
                <div className="report-detail-float-right">
                    <Input type="text" label="By" value={lastEditor} title="The user who edited this report last"
                           disabled/>
                </div>
            </div>
        )
    }
});

module.exports = ReportEdit;
