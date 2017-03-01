'use strict';

const React = require('react');
const Reflux = require('reflux');
const Button = require('react-bootstrap').Button;
const ButtonToolbar = require('react-bootstrap').ButtonToolbar;
const Panel = require('react-bootstrap').Panel;
const assign = require('object-assign');
const classNames = require('classnames');
const IfAnyGranted = require('react-authorization').IfAnyGranted;

const Actions = require('../../../actions/Actions');
const Attachments = require('../attachment/Attachments').default;
const BasedOn = require('./BasedOn').default;
const Constants = require('../../../constants/Constants');
const CorrectiveMeasures = require('../../correctivemeasure/CorrectiveMeasures').default;
const Factors = require('../../factor/Factors');
const I18nMixin = require('../../../i18n/I18nMixin');
const injectIntl = require('../../../utils/injectIntl');
const Input = require('../../Input').default;
const MessageMixin = require('../../mixin/MessageMixin');
const MessageStore = require('../../../stores/MessageStore');
const ReportDetailMixin = require('../../mixin/ReportDetailMixin');
const ReportProvenance = require('../ReportProvenance').default;
const ReportSummary = require('../ReportSummary').default;
const ReportValidator = require('../../../validation/ReportValidator');
const Sira = require('./Sira').default;
const UserStore = require('../../../stores/UserStore');
const Vocabulary = require('../../../constants/Vocabulary');
const WizardWindow = require('../../wizard/WizardWindow');

const SafetyIssueReport = React.createClass({
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
            wizardProperties: null,
            showDeleteDialog: false
        };
    },

    componentWillUnmount: function () {
        this.cleanupMessages();
    },

    onMessage: function (msg) {
        if (msg.source === Actions.addSafetyIssueBase) {
            this.showMessage(this.i18n(msg.message), msg.type);
        }
    },

    onNameChange: function (e) {
        const issue = assign({}, this.props.report.safetyIssue);
        issue.name = e.target.value;
        this.onChanges({safetyIssue: issue});
    },

    _isIssueActive: function () {
        return this.props.report.safetyIssue.state === Constants.SAFETY_ISSUE_STATE.OPEN;
    },

    _onSafetyIssueStatusChange: function () {
        const issue = assign({}, this.props.report.safetyIssue);
        issue.state = this._isIssueActive() ? Constants.SAFETY_ISSUE_STATE.CLOSED : Constants.SAFETY_ISSUE_STATE.OPEN;
        this.onChanges({safetyIssue: issue});
    },

    _onBaseRemove: function (base) {
        const issue = assign({}, this.props.report.safetyIssue),
            basedOn = issue.basedOn.slice();
        basedOn.splice(basedOn.indexOf(base), 1);
        issue.basedOn = basedOn;
        this.onChanges({safetyIssue: issue});
    },

    onChanges: function (changes) {
        this.props.handlers.onChange(changes);
    },

    onSave: function () {
        const report = this.props.report,
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
        const report = this.props.report;

        return <div>
            <WizardWindow {...this.state.wizardProperties} show={this.state.isWizardOpen}
                          onHide={this.closeSummaryWizard} enableForwardSkip={true}/>

            <Panel header={this.renderHeader()} bsStyle='primary'>
                {this._renderStateChanger()}
                <form>
                    <div className='form-group'>
                        <div className='row'>
                            <div className='col-xs-4'>
                                <Input type='text' name='name' value={report.safetyIssue.name}
                                       onChange={this.onNameChange}
                                       label={this.i18n('name') + '*'}
                                       title={this.i18n('safety-issue.name-tooltip')}/>
                            </div>
                        </div>
                    </div>

                    <div className='form-group'>
                        <Sira report={report} onChange={this.props.handlers.onChange}/>
                    </div>

                    <div className='form-group'>
                        <Factors ref='factors' report={report} rootAttribute='safetyIssue' enableDetails={false}
                                 enableScaleChange={false} onChange={this.props.handlers.onChange}/>
                    </div>

                    <div className='form-group'>
                        <BasedOn report={report} onRemove={this._onBaseRemove}/>
                    </div>

                    <div className='form-group'>
                        <CorrectiveMeasures report={report} onChange={this.props.handlers.onChange}/>
                    </div>

                    <div className='row'>
                        <div className='col-xs-12'>
                            <ReportSummary report={report} onChange={this.onChange}/>
                        </div>
                    </div>

                    <div className='form-group'>
                        <Attachments report={report} onChange={this.props.handlers.onChange}/>
                    </div>

                    <Panel>
                        <ReportProvenance report={report} revisions={this.props.revisions}/>
                    </Panel>

                    {this.renderButtons()}
                </form>
            </Panel>
            {this.renderMessage()}
            {this.renderDeleteDialog()}
        </div>;
    },

    renderHeader: function () {
        const isActive = this.props.report.safetyIssue.state === Constants.SAFETY_ISSUE_STATE.OPEN,
            titleClass = classNames('panel-title', 'pull-left', {'italics': !isActive});
        let fileNo = null;
        if (this.props.report.fileNumber) {
            fileNo =
                <h3 className='panel-title pull-right'>{this.i18n('fileNo') + ' ' + this.props.report.fileNumber}</h3>;
        }
        return <div>
            <h2 className={titleClass}
                title={this.i18n(isActive ? 'safety-issue.panel.active-tooltip' : 'safety-issue.panel.inactive-tooltip')}>
                {this.i18n('safetyissuereport.title')}
            </h2>
            {fileNo}
            <div style={{clear: 'both'}}/>
        </div>;
    },

    _renderStateChanger: function () {
        if (this.props.report.isNew) {
            return null;
        }
        const isActive = this._isIssueActive();
        return <ButtonToolbar className='float-right'>
            <Button bsStyle='primary' onClick={this._onSafetyIssueStatusChange}
                    title={this.i18n(isActive ? 'safety-issue.deactivate-tooltip' : 'safety-issue.activate-tooltip')}>
                {this.i18n(isActive ? 'safety-issue.deactivate' : 'safety-issue.activate')}
            </Button>
        </ButtonToolbar>;
    },

    renderButtons: function () {
        if (this.props.readOnly) {
            return this.renderReadOnlyButtons();
        }
        const loading = this.state.submitting,
            saveDisabled = !ReportValidator.isValid(this.props.report) || loading,
            saveLabel = this.i18n(loading ? 'detail.saving' : 'save');

        return <IfAnyGranted expected={[Vocabulary.ROLE_ADMIN, Vocabulary.ROLE_USER]}
                             actual={UserStore.getCurrentUser().types}
                             unauthorized={this.renderReadOnlyButtons('authorization.read-only.message')}>
            <ButtonToolbar className='float-right detail-button-toolbar'>
                <Button bsStyle='success' bsSize='small' disabled={saveDisabled} title={this.getSaveButtonTitle()}
                        onClick={this.onSave}>{saveLabel}</Button>
                <Button bsStyle='link' bsSize='small' title={this.i18n('cancel-tooltip')}
                        onClick={this.props.handlers.onCancel}>{this.i18n('cancel')}</Button>
                {this.renderSubmitButton()}
                {this.renderDeleteButton()}
            </ButtonToolbar>
        </IfAnyGranted>;
    },

    getSaveButtonTitle: function () {
        let titleProp = 'detail.save-tooltip';
        if (this.state.submitting) {
            titleProp = 'detail.saving';
        } else if (!ReportValidator.isValid(this.props.report)) {
            titleProp = ReportValidator.getValidationMessage(this.props.report);
        }
        return this.i18n(titleProp);
    },

    renderSubmitButton: function () {
        return this.props.report.isNew ? null :
            <Button bsStyle='primary' bsSize='small' title={this.i18n('detail.submit-tooltip')} onClick={this.onSubmit}>
                {this.i18n('detail.submit')}
            </Button>;
    }
});

module.exports = injectIntl(SafetyIssueReport);
