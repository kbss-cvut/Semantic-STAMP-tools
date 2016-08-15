'use strict';

var React = require('react');
var Reflux = require('reflux');
var Button = require('react-bootstrap').Button;
var ButtonToolbar = require('react-bootstrap').ButtonToolbar;
var Panel = require('react-bootstrap').Panel;
var assign = require('object-assign');

var Actions = require('../../../actions/Actions');
var BasedOn = require('./BasedOn').default;
var Constants = require('../../../constants/Constants');
var CorrectiveMeasures = require('../../correctivemeasure/CorrectiveMeasures').default;
var Factors = require('../../factor/Factors');
var I18nMixin = require('../../../i18n/I18nMixin');
var injectIntl = require('../../../utils/injectIntl');
var Input = require('../../Input');
var MessageMixin = require('../../mixin/MessageMixin');
var MessageStore = require('../../../stores/MessageStore');
var ReportDetailMixin = require('../../mixin/ReportDetailMixin');
var ReportProvenance = require('../ReportProvenance').default;
var ReportSummary = require('../ReportSummary').default;
var ReportValidator = require('../../../validation/ReportValidator');
var WizardWindow = require('../../wizard/WizardWindow');

var SafetyIssueReport = React.createClass({
    mixins: [MessageMixin, I18nMixin, ReportDetailMixin, Reflux.listenTo(MessageStore, 'onMessage')],

    propTypes: {
        handlers: React.PropTypes.object,
        report: React.PropTypes.object.isRequired,
        loading: React.PropTypes.bool
    },

    getInitialState: function () {
        return {
            submitting: false,
            loadingWizard: false,
            isWizardOpen: false,
            wizardProperties: null
        };
    },

    onMessage: function (msg) {
        if (msg.source !== Actions.addSafetyIssueBase) {
            return;
        }
        switch (msg.type) {
            case Constants.MESSAGE_TYPE.SUCCESS:
                this.showSuccessMessage(this.i18n(msg.message));
                break;
            case Constants.MESSAGE_TYPE.WARNING:
                this.showWarnMessage(this.i18n(msg.message));
                break;
        }
    },

    onNameChange: function (e) {
        var issue = assign({}, this.props.report.safetyIssue);
        issue.name = e.target.value;
        this.onChanges({safetyIssue: issue});
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

        return <div>
            <WizardWindow {...this.state.wizardProperties} show={this.state.isWizardOpen}
                          onHide={this.closeSummaryWizard} enableForwardSkip={true}/>

            <Panel header={this.renderHeader()} bsStyle='primary'>
                <form>
                    <div className='form-group'>
                        <div className='row'>
                            <div className='col-xs-4'>
                                <Input type='text' name='name' value={report.safetyIssue.name}
                                       onChange={this.onNameChange}
                                       label={this.i18n('name') + '*'}
                                       title={this.i18n('safety-issue.headline-tooltip')}/>
                            </div>
                        </div>
                    </div>

                    <div className='form-group'>
                        <Factors ref='factors' report={report} rootAttribute='safetyIssue' enableDetails={false}
                                 enableScaleChange={false} onChange={this.props.handlers.onChange}/>
                    </div>

                    <div className='form-group'>
                        <BasedOn report={report}/>
                    </div>

                    <div className='form-group'>
                        <CorrectiveMeasures report={report} onChange={this.props.handlers.onChange}/>
                    </div>

                    <div className='row'>
                        <div className='col-xs-12'>
                            <ReportSummary report={report} onChange={this.onChange}/>
                        </div>
                    </div>

                    <Panel>
                        <ReportProvenance report={report} revisions={this.props.revisions}/>
                    </Panel>

                    {this.renderButtons()}
                </form>
            </Panel>
            {this.renderMessage()}
        </div>;
    },

    renderHeader: function () {
        var fileNo = null;
        if (this.props.report.fileNumber) {
            fileNo =
                <h3 className='panel-title pull-right'>{this.i18n('fileNo') + ' ' + this.props.report.fileNumber}</h3>;
        }
        return (
            <div>
                <h2 className='panel-title pull-left'>{this.i18n('safetyissuereport.title')}</h2>
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

module.exports = injectIntl(SafetyIssueReport);
