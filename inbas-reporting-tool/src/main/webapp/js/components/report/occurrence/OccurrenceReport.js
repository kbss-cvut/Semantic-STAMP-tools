'use strict';

const React = require('react');
const Reflux = require('reflux');
const Button = require('react-bootstrap').Button;
const ButtonToolbar = require('react-bootstrap').ButtonToolbar;
const DropdownButton = require('react-bootstrap').DropdownButton;
const MenuItem = require('react-bootstrap').MenuItem;
const Panel = require('react-bootstrap').Panel;
const assign = require('object-assign');
const injectIntl = require('../../../utils/injectIntl');

const Actions = require('../../../actions/Actions');
const ArmsAttributes = require('../arms/ArmsAttributes').default;
const Attachments = require('../attachment/Attachments').default;
const BasicOccurrenceInfo = require('./BasicOccurrenceInfo').default;
const CorrectiveMeasures = require('../../correctivemeasure/CorrectiveMeasures').default;
const Department = require('./Department').default;
const EccairsReportButton = require('./EccairsReportButton').default;
let Factors = require('../../factor/Factors');
const I18nMixin = require('../../../i18n/I18nMixin');
const MessageMixin = require('../../mixin/MessageMixin');
const MessageStore = require('../../../stores/MessageStore');
const PhaseTransition = require('../../misc/PhaseTransition').default;
const ReportDetailMixin = require('../../mixin/ReportDetailMixin');
const ReportProvenance = require('../ReportProvenance').default;
const ReportSummary = require('../ReportSummary').default;
const ReportValidator = require('../../../validation/ReportValidator');
const SafetyIssueSelector = require('../safetyissue/SafetyIssueSelector').default;
const WizardGenerator = require('../../wizard/generator/WizardGenerator');
const WizardWindow = require('../../wizard/WizardWindow');

const OccurrenceReport = React.createClass({
    mixins: [MessageMixin, I18nMixin, ReportDetailMixin, Reflux.listenTo(MessageStore, '_onMessage')],

    propTypes: {
        handlers: React.PropTypes.object.isRequired,
        report: React.PropTypes.object.isRequired,
        readOnly: React.PropTypes.bool
    },

    getInitialState: function () {
        return {
            submitting: false,
            loadingWizard: false,
            isWizardOpen: false,
            wizardProperties: null,
            showSafetyIssueSelector: false,
            showDeleteDialog: false
        };
    },

    componentWillUnmount: function () {
        this.cleanupMessages();
    },

    _onMessage: function (msg) {
        if (msg.source === Actions.loadEccairsReport) {
            this.showMessage(this.i18n(msg.message), msg.type);
        }
    },

    onChanges: function (changes) {
        this.props.handlers.onChange(changes);
    },

    onSave: function () {
        let report = this.props.report,
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

    _reportSummary: function () {
        this.setState({loadingWizard: true});
        let report = assign({}, this.props.report);
        report.factorGraph = this.refs.factors.getWrappedInstance().getFactorGraph();
        WizardGenerator.generateSummaryWizard(report, this.i18n('report.summary'), this.openSummaryWizard);
    },

    openSummaryWizard: function (wizardProperties) {
        wizardProperties.onFinish = this.closeSummaryWizard;
        this.setState({
            loadingWizard: false,
            isWizardOpen: true,
            wizardProperties: wizardProperties
        });
    },

    closeSummaryWizard: function () {
        this.setState({isWizardOpen: false});
    },

    _onOpenSafetyIssueSelector: function () {
        this.setState({showSafetyIssueSelector: true});
    },

    _onNewRevisionFromEccairs: function () {
        this.onLoading(Actions.newRevisionFromLatestEccairs);
        Actions.newRevisionFromLatestEccairs(this.props.report, this._onNewRevisionFromEccairsSuccess, this._onNewRevisionFromEccairsError);
    },

    _onNewRevisionFromEccairsSuccess: function (key) {
        this.onLoadingEnd();
        this.showSuccessMessage(this.i18n('report.eccairs.create-new-revision.success'));
        this.props.handlers.onSuccess(key);
    },

    _onNewRevisionFromEccairsError: function (error) {
        this.onLoadingEnd();
        this.showErrorMessage(this.i18n('report.eccairs.create-new-revision.error') + error.message);
    },

    render: function () {
        const report = this.props.report;

        return <div>
            <WizardWindow {...this.state.wizardProperties} show={this.state.isWizardOpen}
                          onHide={this.closeSummaryWizard} enableForwardSkip={true}/>

            <Panel header={this.renderHeader()} bsStyle='primary'>
                <ButtonToolbar className='float-right'>
                    <EccairsReportButton report={report}/>
                    <Button bsStyle='primary' bsSize='small' className='detail-top-button' onClick={this._reportSummary}
                            disabled={this.state.loadingWizard}>
                        {this.i18n(this.state.loadingWizard ? 'please-wait' : 'summary')}
                    </Button>
                </ButtonToolbar>
                <form>
                    <BasicOccurrenceInfo report={report} revisions={this.props.revisions}
                                         onChange={this.props.handlers.onChange}/>

                    <div>
                        <Factors ref='factors' report={report} rootAttribute='occurrence' onChange={this.onChanges}/>
                    </div>

                    <div className='form-group'>
                        <CorrectiveMeasures report={report} onChange={this.props.handlers.onChange}/>
                    </div>

                    <div className='form-group'>
                        <ArmsAttributes report={report} onChange={this.props.handlers.onChange}/>
                    </div>

                    <div className='row'>
                        <div className='col-xs-12'>
                            <ReportSummary report={report} onChange={this.onChange} required={true}/>
                        </div>
                    </div>

                    <div className='form-group'>
                        <Department report={report} onChange={this.props.handlers.onChange}/>
                    </div>

                    <div className='form-group'>
                        <Attachments report={report} onChange={this.props.handlers.onChange}/>
                    </div>

                    <Panel>
                        <ReportProvenance report={report} revisions={this.props.revisions}/>
                    </Panel>

                    {this.renderButtons()}

                    <div style={{clear: 'both'}}/>
                    {this._renderSafetyIssueSelector()}
                </form>
            </Panel>
            {this.renderMessage()}
            {this.renderDeleteDialog()}
        </div>;
    },

    renderHeader: function () {
        let fileNo = null;
        if (this.props.report.fileNumber) {
            fileNo =
                <h3 className='panel-title pull-right'>{this.i18n('fileNo') + ' ' + this.props.report.fileNumber}</h3>;
        }
        let label = this.i18n('occurrencereport.title');
        if (this.props.report.isEccairsReport()) {
            label += ' (' + this.i18n('report.eccairs.label') + ')';
        }
        return <div>
            <h2 className='panel-title pull-left'>{label}</h2>
            {fileNo}
            <div style={{clear: 'both'}}/>
        </div>;
    },

    renderButtons: function () {
        if (this.props.readOnly) {
            return this.renderReadOnlyButtons();
        }
        let loading = this.state.submitting !== false,
            saveDisabled = !ReportValidator.isValid(this.props.report) || loading;

        return <ButtonToolbar className='float-right detail-button-toolbar'>
            <Button bsStyle='success' bsSize='small' disabled={saveDisabled} title={this.getSaveButtonTitle()}
                    onClick={this.onSave}>{this._getSaveButtonLabel()}</Button>
            <Button bsStyle='link' bsSize='small' title={this.i18n('cancel-tooltip')} disabled={loading}
                    onClick={this.props.handlers.onCancel}>{this.i18n('cancel')}</Button>
            {this._renderDropdown()}
            {this.renderDeleteButton()}
        </ButtonToolbar>;
    },

    _getSaveButtonLabel: function () {
        return this.i18n(this.state.submitting === Actions.newRevisionFromLatestEccairs ? 'please-wait' :
            this.state.submitting ? 'detail.saving' : 'save');
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

    _renderDropdown: function () {
        if (this.props.report.isNew) {
            return null;
        }
        const items = [];
        items.push(<PhaseTransition key='phase-transition' report={this.props.report} onLoading={this.onLoading}
                                    menuItem={true} onSuccess={this.onPhaseTransitionSuccess}
                                    onError={this.onPhaseTransitionError}/>);
        items.push(this._renderNewRevision());
        Array.prototype.push.apply(items, this._renderCreateSafetyIssue());
        Array.prototype.push.apply(items, this._renderEccairsOverwrite());
        return <DropdownButton id='occurrence-report-actions' bsStyle='primary' bsSize='small'
                               disabled={this.state.submitting !== false} title={this.i18n('table-actions')} dropup
                               pullRight>
            {items}
        </DropdownButton>;
    },

    _renderNewRevision: function () {
        return this.props.report.isNew ? null :
            <MenuItem key='submit' title={this.i18n('detail.submit-tooltip')} onClick={this.onSubmit}>
                {this.i18n('detail.submit')}
            </MenuItem>;
    },

    _renderCreateSafetyIssue: function () {
        const items = [];
        if (!this.props.report.isNew) {
            items.push(<MenuItem key='si-divider' divider/>);
            items.push(<MenuItem key='si-header' header>{this.i18n('safetyissuereport.label')}</MenuItem>);
            items.push(<MenuItem key='si-create' onClick={this.props.handlers.onCreateSafetyIssue}
                                 title={this.i18n('occurrencereport.create-safety-issue-tooltip')}>{this.i18n('occurrencereport.create-safety-issue')}</MenuItem>);
            items.push(<MenuItem key='si-add' onClick={this._onOpenSafetyIssueSelector}
                                 title={this.i18n('occurrencereport.add-as-safety-issue-base-tooltip')}>{this.i18n('occurrencereport.add-as-safety-issue-base')}</MenuItem>);
        }
        return items;
    },

    _renderEccairsOverwrite: function () {
        const items = [];
        if (this.props.report.isEccairsReport()) {
            items.push(<MenuItem key='eccairs-divider' divider/>);
            items.push(<MenuItem key='eccairs-new-revision'
                                 title={this.i18n('report.eccairs.create-new-revision.tooltip')}
                                 onClick={this._onNewRevisionFromEccairs}>{this.i18n('report.eccairs.create-new-revision.label')}
            </MenuItem>);
        }
        return items;
    },

    _renderSafetyIssueSelector: function () {
        return this.state.showSafetyIssueSelector ?
            <div className='float-right row col-xs-3'>
                <SafetyIssueSelector report={this.props.report} event={this.props.report.occurrence}/>
            </div> : null;
    }
});

module.exports = injectIntl(OccurrenceReport);
