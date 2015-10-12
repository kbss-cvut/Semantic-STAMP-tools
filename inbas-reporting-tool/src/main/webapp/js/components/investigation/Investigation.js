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
var DateTimePicker = require('react-bootstrap-datetimepicker');

var Factors = require('./Factors');
var BasicOccurrenceInfo = require('../reports/BasicOccurrenceInfo');
var InitialReports = require('../initialreport/InitialReports');
var ReportSummary = require('../reports/ReportSummary');
var ReportStatements = require('../reports/ReportStatements');
var Mask = require('../Mask');

var Investigation = React.createClass({
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


    render: function () {
        if (this.props.loading) {
            return (
                <Mask text='Loading report for investigation...'/>
            );
        }
        return this.renderDetail();
    },

    renderDetail: function () {
        var investigation = this.props.investigation,
            loading = this.state.submitting;
        return (
            <Panel header='Occurrence Investigation'>
                <form>
                    <BasicOccurrenceInfo report={investigation} onChange={this.onChange}
                                         onAttributeChange={this.onAttributeChange}/>

                    <div className='row'>
                        <div className='col-xs-12'>
                            <InitialReports report={investigation} onAttributeChange={this.onAttributeChange}/>
                        </div>
                    </div>

                    <div>
                        <Factors occurrence={investigation} onAttributeChange={this.onAttributeChange}/>
                    </div>

                    <div className='form-group'>
                        <ReportStatements report={investigation} onChange={this.props.onChange} show={['correctiveMeasures']}/>
                    </div>

                    <div className='row'>
                        <div className='col-xs-12'>
                            <ReportSummary report={investigation} onChange={this.onChange}/>
                        </div>
                    </div>

                    <ButtonToolbar className='float-right' style={{margin: '1em 0 0.5em 0'}}>
                        <Button bsStyle='success' bsSize='small' disabled={loading}
                                ref='submit'
                                onClick={this.onSubmit}>{loading ? 'Submitting...' : 'Submit'}</Button>
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
                    <p>Unable to submit investigation. Server responded with message: {this.state.error.message}</p>
                </Alert>
            </div>
        ) : null;
    }
});

module.exports = Investigation;
