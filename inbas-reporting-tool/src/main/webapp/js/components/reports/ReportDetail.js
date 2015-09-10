/**
 * Created by ledvima1 on 27.5.15.
 */

'use strict';

var React = require('react');
var Button = require('react-bootstrap').Button;
var Panel = require('react-bootstrap').Panel;
var Alert = require('react-bootstrap').Alert;
var assign = require('object-assign');
var DateTimePicker = require('react-bootstrap-datetimepicker');

var Input = require('../Input');
var Actions = require('../../actions/Actions');
var Utils = require('../../utils/Utils');
var ReportStatements = require('./ReportStatements');
var OccurrenceSeverity = require('./OccurrenceSeverity');
var Mask = require('../Mask');

var ReportDetail = React.createClass({
    getInitialState: function () {
        return {
            submitting: false,
            error: null
        };
    },

    isReportNew: function () {
        return this.props.report.key == null;
    },

    onChange: function (e) {
        var value = e.target.value;
        var attributeName = e.target.name;
        this.props.onChange(attributeName, value);
    },

    onAttributeChange: function (attribute, value) {
        this.props.onChange(attribute, value);
    },

    onDateChange: function (value) {
        this.props.onChange('occurrenceTime', new Date(Number(value)));
    },

    onSubmit: function (e) {
        e.preventDefault();
        this.setState(assign(this.state, {submitting: true}));
        this.props.report.lastEditedBy = this.props.user;
        if (this.isReportNew()) {
            this.props.report.author = this.props.user;
            Actions.createReport(this.props.report, this.props.onSuccess, this.onSubmitError);
        }
        else {
            Actions.updateReport(this.props.report, this.props.onSuccess, this.onSubmitError);
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

    getFullName: function (data) {
        if (!data) {
            return '';
        }
        return data.firstName + ' ' + data.lastName;
    },


    render: function () {
        if (this.props.loading) {
            return (
                <Mask text='Loading report...'/>
            );
        }
        if (!this.props.report) {
            return (<Alert bsStyle='danger' onDismiss={this.props.onCancel}>
                <h4>Not Found</h4>

                <p>Occurrence report not found.</p>

                <p>
                    <Button onClick={this.props.onCancel}>Close</Button>
                </p>
            </Alert>);
        }
        return this.renderDetail();
    },

    renderDetail: function () {
        var report = this.props.report;
        var loading = this.state.submitting;
        return (
            <Panel header='Preliminary Occurrence Report'>
                <form>
                    <div className='row'>
                        <div className='col-xs-4'>
                            <Input type='text' name='name' value={report.name} onChange={this.onChange}
                                   label='Report Name' title='Short descriptive name for this report'/>
                        </div>
                    </div>

                    <div className='row'>
                        <div className='picker-container form-group form-group-sm col-xs-4'>
                            <label className='control-label'>Occurrence Time</label>
                            <DateTimePicker inputFormat='DD-MM-YY hh:mm A' dateTime={report.occurrenceTime.toString()}
                                            onChange={this.onDateChange}
                                            inputProps={{title: 'Date and time when the event occurred', bsSize: 'small'}}/>
                        </div>
                    </div>

                    <div className='row'>
                        <div className='col-xs-4'>
                            <OccurrenceSeverity onChange={this.onAttributeChange}
                                                severityAssessment={report.severityAssessment}/>
                        </div>
                    </div>

                    {this.renderAuthor()}

                    {this.renderLastEdited()}

                    {/*<div className='row'>
                        <div className='col-xs-12'>
                            <Input type='textarea' rows='3' label='Factors' name='factors' placeholder='Factors'
                                   value={report.factors} onChange={this.onChange} title='Event factors'/>
                        </div>
                    </div>*/}

                    <div className='row'>
                        <div className='col-xs-12'>
                            <Input type='textarea' rows='8' label='Initial Report' name='initialReport'
                                   placeholder='Initial report'
                                   value={report.initialReport} onChange={this.onChange}
                                   title='Initial Report'/>
                        </div>
                    </div>

                    <div className='form-group'>
                        <ReportStatements report={report} onChange={this.props.onChange}/>
                    </div>

                    <div className='form-group'>
                        <Button bsStyle='success' bsSize='small' disabled={loading || report.description === ''}
                                ref='submit'
                                onClick={this.onSubmit}>{loading ? 'Submitting...' : 'Submit'}</Button>
                        <Button bsStyle='link' bsSize='small' title='Discard changes' onClick={this.props.onCancel}>Cancel</Button>
                    </div>

                    {this.renderError()}

                </form>
            </Panel>
        );
    },

    renderAuthor: function () {
        return this.isReportNew() ? null : (
            <div className='row'>
                <div className='col-xs-4'>
                    <Input type='text' value={this.getFullName(this.props.report.author)} label='Author'
                           title='Report author'
                           disabled/>
                </div>
            </div>);
    },

    renderError: function () {
        return this.state.error ? (
            <div className='form-group'>
                <Alert bsStyle='danger' onDismiss={this.handleAlertDismiss} dismissAfter={10000}>
                    <p>Unable to submit report. Server responded with message: {this.state.error.message}</p>
                </Alert>
            </div>
        ) : null;
    },

    renderLastEdited: function () {
        if (this.isReportNew()) {
            return null;
        }
        var report = this.props.report;
        var formattedDate = Utils.formatDate(new Date(report.lastEdited));
        var text = 'Last edited on ' + formattedDate + ' by ' + this.getFullName(report.lastEditedBy) + '.';
        return (
            <div className='form-group italics'>
                {text}
            </div>
        );
    }
});

module.exports = ReportDetail;
