/**
 * @jsx
 */

'use strict';

var React = require('react');
var Button = require('react-bootstrap').Button;
var ButtonToolbar = require('react-bootstrap').ButtonToolbar;
var Panel = require('react-bootstrap').Panel;
var Alert = require('react-bootstrap').Alert;
var assign = require('object-assign');

var Actions = require('../../actions/Actions');
var InitialReports = require('./../initialreport/InitialReports');
var ReportStatements = require('./ReportStatements');
var BasicOccurrenceInfo = require('./BasicOccurrenceInfo');
var ReportSummary = require('./ReportSummary');
var Mask = require('../Mask');
var Routing = require('../../utils/Routing');
var Routes = require('../../utils/Routes');

var ReportDetail = React.createClass({
    getInitialState: function () {
        return {
            submitting: false,
            error: null
        };
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
        var report = this.props.report;
        e.preventDefault();
        this.setState(assign(this.state, {submitting: true}));
        if (report.isNew) {
            Actions.createReport(report, this.onSuccess, this.onSubmitError);
        }
        else {
            Actions.updateReport(report, this.onSuccess, this.onSubmitError);
        }
    },

    onSuccess: function() {
        this.setState({submitting: false});
        this.props.onSuccess();
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

    investigate: function () {
        Actions.createInvestigation(this.props.report.key);
        Routing.transitionTo(Routes.editInvestigation, {
            params: {reportKey: this.props.report.key},
            handlers: {onCancel: Routes.investigations}
        });
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
        var report = this.props.report,
            loading = this.state.submitting;
        return (
            <Panel header={<h2>Preliminary Occurrence Report</h2>} bsStyle='primary'>
                <form>
                    <BasicOccurrenceInfo report={report} onChange={this.onChange}
                                         onAttributeChange={this.onAttributeChange}/>

                    <div className='row'>
                        <div className='col-xs-12'>
                            <InitialReports report={report} onAttributeChange={this.onAttributeChange}/>
                        </div>
                    </div>

                    <div className='form-group'>
                        <ReportStatements report={report} onChange={this.props.onChange}/>
                    </div>

                    <div className='row'>
                        <div className='col-xs-12'>
                            <ReportSummary report={report} onChange={this.onChange}/>
                        </div>
                    </div>

                    <ButtonToolbar className='float-right' style={{margin: '1em 0 0.5em 0'}}>
                        <Button bsStyle='success' bsSize='small' disabled={loading}
                                ref='submit'
                                onClick={this.onSubmit}>{loading ? 'Saving...' : 'Save'}</Button>
                        <Button bsStyle='link' bsSize='small' title='Discard changes' onClick={this.props.onCancel}>Cancel</Button>
                    </ButtonToolbar>

                    {this.renderError()}

                </form>
            </Panel>
        );
    },

    renderError: function () {
        return this.state.error ? (
            <div className='form-group'>
                <Alert bsStyle='danger' onDismiss={this.handleAlertDismiss} dismissAfter={10000}>
                    <p>Unable to submit report. Server responded with message: {this.state.error.message}</p>
                </Alert>
            </div>
        ) : null;
    }
});

module.exports = ReportDetail;
