'use strict';

var React = require('react');
var Button = require('react-bootstrap').Button;
var ButtonToolbar = require('react-bootstrap').ButtonToolbar;
var Panel = require('react-bootstrap').Panel;
var assign = require('object-assign');
var injectIntl = require('../../../utils/injectIntl');

var Actions = require('../../../actions/Actions');
var BasicOccurrenceInfo = require('./BasicOccurrenceInfo').default;
var Department = require('./Department').default;
var Factors = require('../../factor/Factors');
var CorrectiveMeasures = require('../../correctivemeasure/CorrectiveMeasures').default;
var ArmsAttributes = require('../arms/ArmsAttributes').default;
var PhaseTransition = require('../../misc/PhaseTransition').default;
var ReportProvenance = require('../ReportProvenance').default;
var ReportSummary = require('../ReportSummary').default;
var MessageMixin = require('../../mixin/MessageMixin');
var ReportValidator = require('../../../validation/ReportValidator');
var I18nMixin = require('../../../i18n/I18nMixin');
var ReportDetailMixin = require('../../mixin/ReportDetailMixin');

var OccurrenceReport = React.createClass({
    mixins: [MessageMixin, I18nMixin, ReportDetailMixin],

    propTypes: {
        handlers: React.PropTypes.object,
        report: React.PropTypes.object,
        loading: React.PropTypes.bool
    },

    getInitialState: function () {
        return {
            submitting: false
        };
    },

    onAttributeChange: function (attribute, value) {
        var change = {};
        change[attribute] = value;
        this.onChanges(change);
    },

    onChanges: function (changes) {
        this.props.handlers.onChange(changes);
    },

    onSave: function () {
        var report = this.props.report,
            factors = this.refs.factors.getWrappedInstance();
        this.onLoading();
        report.factorGraph = factors.getFactorGraph();
        if (report.isNew) {
            Actions.createReport(report, this.onSaveSuccess, this.onSaveError);
        } else {
            Actions.updateReport(report, this.onSaveSuccess, this.onSaveError);
        }
    },

    onSubmit: function () {
        this.onLoading();
        Actions.submitReport(this.props.report, this.onSubmitSuccess, this.onSubmitError);
    },

    render: function () {
        var report = this.props.report;

        return (
            <div>
                <Panel header={this.renderHeader()} bsStyle='primary'>
                    <form>
                        <BasicOccurrenceInfo report={report} revisions={this.props.revisions}
                                             onChange={this.props.handlers.onChange}/>

                        <div>
                            <Factors ref='factors' report={report} onChange={this.onChanges}/>
                        </div>

                        <div className='form-group'>
                            <CorrectiveMeasures report={report} onChange={this.props.handlers.onChange}/>
                        </div>

                        <div className='form-group'>
                            <ArmsAttributes report={report} onChange={this.props.handlers.onChange}/>
                        </div>

                        <div className='row'>
                            <div className='col-xs-12'>
                                <ReportSummary report={report} onChange={this.onChange}/>
                            </div>
                        </div>

                        <Panel>
                            <div className='row'>
                                <div className='col-xs-4'>
                                    <Department report={report} onChange={this.props.handlers.onChange}/>
                                </div>
                            </div>
                            <ReportProvenance report={report} revisions={this.props.revisions}/>
                        </Panel>

                        {this.renderButtons()}
                    </form>
                </Panel>
                {this.renderMessage()}
            </div>
        );
    },

    renderHeader: function () {
        var fileNo = null;
        if (this.props.report.fileNumber) {
            fileNo =
                <h3 className='panel-title pull-right'>{this.i18n('fileNo') + ' ' + this.props.report.fileNumber}</h3>;
        }
        return (
            <div>
                <h2 className='panel-title pull-left'>{this.i18n('occurrencereport.title')}</h2>
                {fileNo}
                <div style={{clear: 'both'}}/>
            </div>
        )
    },

    renderButtons: function () {
        if (this.props.readOnly) {
            return this.renderReadOnlyButtons();
        }
        var loading = this.state.submitting,
            saveDisabled = !ReportValidator.isValid(this.props.report) || loading,
            saveLabel = this.i18n(loading ? 'detail.saving' : 'save');

        return <ButtonToolbar className='float-right' style={{margin: '1em 0 0.5em 0'}}>
            <Button bsStyle='success' bsSize='small' disabled={saveDisabled} title={this.getSaveButtonTitle()}
                    onClick={this.onSave}>{saveLabel}</Button>
            <Button bsStyle='link' bsSize='small' title={this.i18n('cancel-tooltip')}
                    onClick={this.props.handlers.onCancel}>{this.i18n('cancel')}</Button>
            {this.renderSubmitButton()}
            <PhaseTransition report={this.props.report} onLoading={this.onLoading}
                             onSuccess={this.onPhaseTransitionSuccess} onError={this.onPhaseTransitionError}/>
        </ButtonToolbar>;
    },

    getSaveButtonTitle: function () {
        var titleProp = 'detail.save-tooltip';
        if (this.state.submitting) {
            titleProp = 'detail.saving';
        } else if (!ReportValidator.isValid(this.props.report)) {
            titleProp = ReportValidator.getValidationMessage(this.props.report);
        }
        return this.i18n(titleProp);
    },

    renderSubmitButton: function () {
        return (
            <Button bsStyle='primary' bsSize='small' title={this.i18n('detail.submit-tooltip')}
                    onClick={this.onSubmit}>
                {this.i18n('detail.submit')}
            </Button>);
    }
});

module.exports = injectIntl(OccurrenceReport);
