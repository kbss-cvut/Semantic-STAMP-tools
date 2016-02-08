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
var ArmsAttributes = require('../reports/arms/ArmsAttributes');
var ReportSummary = require('./ReportSummary');
var MessageMixin = require('../mixin/MessageMixin');
var ReportValidator = require('../../validation/ReportValidator');
var I18nMixin = require('../../i18n/I18nMixin');
var ReportDetailMixin = require('../mixin/ReportDetailMixin');

var ReportDetail = React.createClass({
    mixins: [MessageMixin, I18nMixin, ReportDetailMixin],

    getInitialState: function () {
        return {
            submitting: false
        };
    },

    onAttributeChange: function (attribute, value) {
        var change = {};
        change[attribute] = value;
        this.props.handlers.onChange(change);
    },

    onSave: function (e) {
        var report = this.props.report;
        e.preventDefault();
        this.setState(assign(this.state, {submitting: true}));
        if (report.isNew) {
            Actions.createPreliminary(report, this.onSaveSuccess, this.onSaveError);
        }
        else {
            Actions.updateReport(report, this.onSaveSuccess, this.onSaveError);
        }
    },

    onSubmit: function () {
        this.setState({submitting: true});
        Actions.submitReport(this.props.report, this.onSubmitSuccess, this.onSubmitError);
    },

    _canEdit: function () {
        return this.props.report && this.props.report.occurrence.reportingPhase !== Constants.INVESTIGATION_REPORT_PHASE;
    },

    render: function () {
        var report = this.props.report;

        return (
            <div>
                <Panel header={this.renderHeader()} bsStyle='primary'>
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

                        <div className='form-group'>
                            <ArmsAttributes report={report} onAttributeChange={this.onAttributeChange}/>
                        </div>

                        <div className='row'>
                            <div className='col-xs-12'>
                                <ReportSummary report={report} onChange={this.onChange}/>
                            </div>
                        </div>

                        {this.renderButtons()}

                        <div style={{clear: 'both'}}/>
                    </form>
                </Panel>
                {this.renderMessage()}
            </div>
        );
    },

    renderHeader: function () {
        return (
            <div>
                <h2 className='panel-title pull-left'>{this.i18n('preliminary.detail.panel-title')}</h2>
                <h3 className='panel-title pull-right'>{this.i18n('fileNo') + ' ' + this.props.report.fileNumber}</h3>
                <div style={{clear: 'both'}}/>
            </div>
        )
    },

    renderButtons: function () {
        if (this.props.readOnly) {
            return this.renderReadOnlyButtons();
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
    }
});

module.exports = injectIntl(ReportDetail);
