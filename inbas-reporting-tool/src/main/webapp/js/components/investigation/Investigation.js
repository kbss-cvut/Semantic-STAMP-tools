'use strict';

var React = require('react');
var Button = require('react-bootstrap').Button;
var Panel = require('react-bootstrap').Panel;
var Alert = require('react-bootstrap').Alert;
var assign = require('object-assign');
var DateTimePicker = require('react-bootstrap-datetimepicker');

var Factors = require('./Factors');
var Input = require('../Input');
var Utils = require('../../utils/Utils');
var ReportStatements = require('../reports/ReportStatements');
var OccurrenceSeverity = require('../reports/OccurrenceSeverity');
var Mask = require('../Mask');

var Investigation = React.createClass({
    getInitialState: function () {
        return {
            submitting: false,
            error: null
        };
    },

    isReportNew: function () {
        return this.props.investigation.key == null;
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
        this.props.investigation.lastEditedBy = this.props.user;

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
                <Mask text='Loading report for investigation...'/>
            );
        }
        return this.renderDetail();
    },

    renderDetail: function () {
        var investigation = this.props.investigation;
        var loading = this.state.submitting;
        return (
            <Panel header='Occurrence Investigation'>
                <form>
                    <div className='row'>
                        <div className='col-xs-4'>
                            <Input type='text' name='name' value={investigation.name} onChange={this.onChange}
                                   label='Report Name' title='Short descriptive name for this investigation'/>
                        </div>
                    </div>

                    <div className='row'>
                        <div className='picker-container form-group form-group-sm col-xs-4'>
                            <label className='control-label'>Occurrence Time</label>
                            <DateTimePicker inputFormat='DD-MM-YY hh:mm A'
                                            dateTime={investigation.occurrenceTime.toString()}
                                            onChange={this.onDateChange}
                                            inputProps={{title: 'Date and time when the event occurred', bsSize: 'small'}}/>
                        </div>
                    </div>

                    <div className='row'>
                        <div className='col-xs-4'>
                            <OccurrenceSeverity onChange={this.onAttributeChange}
                                                severityAssessment={investigation.severityAssessment}/>
                        </div>
                    </div>

                    {this.renderAuthor()}

                    {this.renderLastEdited()}

                    <div className='row'>
                        <div className='col-xs-12'>
                            <Input type='textarea' rows='8' label='Initial Report' name='initialReport'
                                   placeholder='Initial investigation'
                                   value={investigation.initialReport} onChange={this.onChange}
                                   title='Initial Report'/>
                        </div>
                    </div>

                    <div className='row'>
                        <div className='col-xs-12'>
                            <div className='form-group form-group-sm'>
                                <label className='control-label'>Factors</label>
                                <Factors occurrence={investigation}/>
                            </div>
                        </div>
                    </div>

                    <div className='form-group'>
                        <ReportStatements report={investigation} onChange={this.props.onChange}/>
                    </div>

                    <div className='form-group'>
                        <Button bsStyle='success' bsSize='small' disabled={loading}
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
                    <Input type='text' value={this.getFullName(this.props.investigation.author)} label='Author'
                           title='Report author'
                           disabled/>
                </div>
            </div>);
    },

    renderError: function () {
        return this.state.error ? (
            <div className='form-group'>
                <Alert bsStyle='danger' onDismiss={this.handleAlertDismiss} dismissAfter={10000}>
                    <p>Unable to submit investigation. Server responded with message: {this.state.error.message}</p>
                </Alert>
            </div>
        ) : null;
    },

    renderLastEdited: function () {
        if (this.isReportNew()) {
            return null;
        }
        var investigation = this.props.investigation;
        var formattedDate = Utils.formatDate(new Date(investigation.lastEdited));
        var text = 'Last edited on ' + formattedDate + ' by ' + this.getFullName(investigation.lastEditedBy) + '.';
        return (
            <div className='form-group italics'>
                {text}
            </div>
        );
    }
});

module.exports = Investigation;
