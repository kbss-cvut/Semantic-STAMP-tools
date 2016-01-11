/**
 * @jsx
 */

'use strict';

var React = require('react');
var Button = require('react-bootstrap').Button;
var ButtonToolbar = require('react-bootstrap').ButtonToolbar;
var Panel = require('react-bootstrap').Panel;
var assign = require('object-assign');

var injectIntl = require('../../utils/injectIntl');

var Actions = require('../../actions/Actions');
var Constants = require('../../constants/Constants');
var InitialReports = require('./../initialreport/InitialReports');
var ReportStatements = require('./ReportStatements');
var BasicOccurrenceInfo = require('./BasicOccurrenceInfo');
var ReportSummary = require('./ReportSummary');
var Mask = require('../Mask');
var MessageMixin = require('../mixin/MessageMixin');
var ResourceNotFound = require('../ResourceNotFound');
var ReportValidator = require('../../validation/ReportValidator');
var I18nMixin = require('../../i18n/I18nMixin');

var ReportDetail = React.createClass({
    mixins: [MessageMixin, I18nMixin],

    getInitialState: function () {
        return {
            submitting: false
        };
    },

    onChange: function (e) {
        var value = e.target.value;
        var attributeName = e.target.name;
        this.onAttributeChange(attributeName, value);
    },

    onAttributeChange: function (attribute, value) {
        this.props.handlers.onChange(attribute, value);
    },

    onSave: function (e) {
        var report = this.props.report;
        e.preventDefault();
        this.setState(assign(this.state, {submitting: true}));
        if (report.isNew) {
            Actions.createPreliminary(report, this.onSuccess, this.onSaveError);
        }
        else {
            Actions.updatePreliminary(report, this.onSuccess, this.onSaveError);
        }
    },

    onSuccess: function () {
        this.setState({submitting: false});
        this.props.handlers.onSuccess();
        this.showSuccessMessage(this.i18n('save-success-message'));
    },

    onSaveError: function (error) {
        this.setState({submitting: false});
        this.showErrorMessage(this.i18n('save-failed-message') + error.message);
    },

    onSubmit: function () {
        this.setState({submitting: true});
        Actions.submitPreliminary(this.props.report, this.onSubmitSuccess, this.onSubmitError);
    },

    onSubmitSuccess: function (key) {
        this.setState({submitting: false});
        this.showSuccessMessage(this.i18n('detail.submit-success-message'));
        this.props.handlers.onSuccess(key);
    },

    onSubmitError: function (error) {
        this.setState({submitting: false});
        this.showErrorMessage(this.i18n('detail.submit-failed-message') + error.message);
    },

    _canEdit: function () {
        return this.props.report && this.props.report.occurrence.reportingPhase !== Constants.INVESTIGATION_REPORT_PHASE;
    },


    render: function () {
        if (this.props.loading) {
            return (
                <Mask text={this.i18n('preliminary.detail.loading-mask')}/>
            );
        }
        if (!this.props.report) {
            return (<ResourceNotFound resource={this.i18n('preliminary.detail.panel-title')}/>);
        }
        return this.renderDetail();
    },

    renderDetail: function () {
        var report = this.props.report;

        return (
            <div>
                <Panel header={<h2>{this.i18n('preliminary.detail.panel-title')}</h2>} bsStyle='primary'>
                    <form>
                        <BasicOccurrenceInfo report={report} onChange={this.onChange} revisions={this.props.revisions}
                                             onAttributeChange={this.onAttributeChange}/>

                        <div className='row'>
                            <div className='col-xs-12'>
                                <InitialReports report={report} onAttributeChange={this.onAttributeChange}/>
                            </div>
                        </div>

                        <div className='form-group'>
                            <ReportStatements report={report} onChange={this.props.handlers.onChange}/>
                        </div>

                        <div className='row'>
                            <div className='col-xs-12'>
                                <ReportSummary report={report} onChange={this.onChange}/>
                            </div>
                        </div>

                        {this.renderButtons()}

                        <div style={{clear: 'both'}}/>

                        {this.renderCannotModifyMessage()}
                    </form>
                </Panel>
                {this.renderMessage()}
            </div>
        );
    },

    renderButtons: function () {
        if (this.props.readOnly) {
            return (
                <div>
                    <div className='float-right'>
                        <Button bsStyle='link' bsSize='small' title={this.i18n('cancel-tooltip')}
                                onClick={this.props.handlers.onCancel}>{this.i18n('cancel')}</Button>
                    </div>
                    <div style={{clear: 'both'}}/>
                    <div className='notice-small float-right'>
                        {this.i18n('revisions.readonly-notice')}
                    </div>
                </div>);
        }
        var report = this.props.report,
            loading = this.state.submitting,
            saveDisabled = !ReportValidator.isValid(report) || loading || !this._canEdit(),
            saveTitle = this.i18n('detail.save-tooltip'),
            saveLabel = this.i18n(loading ? 'detail.saving' : 'save');
        if (loading) {
            saveTitle = this.i18n('detail.saving');
        } else if (saveDisabled) {
            saveTitle = !this._canEdit() ? this.i18n('preliminary.detail.cannot-modify') : this.i18n('detail.invalid-tooltip');
        }
        return (
            <ButtonToolbar className='float-right' style={{margin: '1em 0 0.5em 0'}}>
                <Button bsStyle='success' bsSize='small' disabled={saveDisabled}
                        ref='submit' title={saveTitle}
                        onClick={this.onSave}>{saveLabel}</Button>
                <Button bsStyle='link' bsSize='small' title={this.i18n('cancel-tooltip')}
                        onClick={this.props.handlers.onCancel}>{this.i18n('cancel')}</Button>
                {this.renderSubmitButton()}
                {this.renderInvestigateButton()}
            </ButtonToolbar>
        );
    },

    renderInvestigateButton: function () {
        if (!this.props.report || this.props.report.isNew) {
            return null;
        }
        return (<Button bsStyle='primary' bsSize='small' title={this.i18n('preliminary.table-investigate-tooltip')}
                        onClick={this.props.handlers.onInvestigate}>{this.i18n('preliminary.table-investigate')}</Button>);
    },

    renderSubmitButton: function () {
        if (!this.props.report || this.props.report.isNew) {
            return null;
        }
        return (
            <Button bsStyle='primary' bsSize='small' title={this.i18n('detail.submit-tooltip')}
                    onClick={this.onSubmit}>
                {this.i18n('detail.submit')}
            </Button>);
    },

    renderCannotModifyMessage: function () {
        if (this._canEdit()) {
            return null;
        }
        return (<div className='notice-small' style={{textAlign: 'right'}}>
            {this.i18n('preliminary.detail.cannot-modify')}
        </div>);
    }
});

module.exports = injectIntl(ReportDetail);
