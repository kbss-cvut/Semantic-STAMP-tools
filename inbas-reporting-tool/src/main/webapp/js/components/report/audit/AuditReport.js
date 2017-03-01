'use strict';

const React = require('react');
const Button = require('react-bootstrap').Button;
const ButtonToolbar = require('react-bootstrap').ButtonToolbar;
const Panel = require('react-bootstrap').Panel;
const IfAnyGranted = require('react-authorization').IfAnyGranted;

const Actions = require('../../../actions/Actions');
const Attachments = require('../attachment/Attachments').default;
const Audit = require('./Audit').default;
const I18nMixin = require('../../../i18n/I18nMixin');
const injectIntl = require('../../../utils/injectIntl');
const Input = require('../../Input').default;
const MessageMixin = require('../../mixin/MessageMixin');
const ReportDetailMixin = require('../../mixin/ReportDetailMixin');
const ReportProvenance = require('../ReportProvenance').default;
const ReportValidator = require('../../../validation/ReportValidator');
const UserStore = require('../../../stores/UserStore');
const Vocabulary = require('../../../constants/Vocabulary');

const AuditReport = React.createClass({
    mixins: [MessageMixin, I18nMixin, ReportDetailMixin],

    propTypes: {
        handlers: React.PropTypes.object,
        report: React.PropTypes.object.isRequired,
        loading: React.PropTypes.bool,
        readOnly: React.PropTypes.bool,
        readOnlyMessage: React.PropTypes.string
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

    onSave: function () {
        const report = this.props.report;
        this.onLoading();
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
            <Panel header={this._renderHeader()} bsStyle='primary'>
                <form>
                    <Audit audit={report.audit} onChange={this.props.handlers.onChange} report={report}/>
                    <div className='row'>
                        <div className='col-xs-12'>
                            <Input type='textarea' rows='8' label={this.i18n('audit.remarks')} name='summary'
                                   placeholder={this.i18n('audit.remarks.placeholder')}
                                   value={report.summary} onChange={this.onChange}
                                   title={this.i18n('audit.remarks.placeholder')}/>
                        </div>
                    </div>

                    <div className='form-group'>
                        <Attachments report={report} onChange={this.props.handlers.onChange}/>
                    </div>

                    <Panel>
                        <ReportProvenance report={report} revisions={this.props.revisions}/>
                    </Panel>

                    {this._renderButtons()}
                </form>
            </Panel>
            {this.renderMessage()}
            {this.renderDeleteDialog()}
        </div>;
    },

    _renderHeader: function () {
        let fileNo = null;
        if (this.props.report.fileNumber) {
            fileNo =
                <h3 className='panel-title pull-right'>{this.i18n('fileNo') + ' ' + this.props.report.fileNumber}</h3>;
        }
        let label = this.i18n('auditreport.title');
        if (this.props.report.isSafaReport()) {
            label += ' (' + this.i18n('report.safa.label') + ')';
        }
        return <div>
            <h2 className='panel-title pull-left'>{label}</h2>
            {fileNo}
            <div style={{clear: 'both'}}/>
        </div>;
    },

    _renderButtons: function () {
        if (this.props.readOnly) {
            return this.renderReadOnlyButtons(this.props.readOnlyMessage);
        }
        const loading = this.state.submitting,
            saveDisabled = !ReportValidator.isValid(this.props.report) || loading,
            saveLabel = this.i18n(loading ? 'detail.saving' : 'save');

        return <IfAnyGranted expected={[Vocabulary.ROLE_ADMIN, Vocabulary.ROLE_USER]}
                             actual={UserStore.getCurrentUser().types}
                             unauthorized={this.renderReadOnlyButtons('authorization.read-only.message')}>
            <ButtonToolbar className='float-right detail-button-toolbar'>
                <Button bsStyle='success' bsSize='small' disabled={saveDisabled} title={this._getSaveButtonTitle()}
                        onClick={this.onSave}>{saveLabel}</Button>
                <Button bsStyle='link' bsSize='small' title={this.i18n('cancel-tooltip')}
                        onClick={this.props.handlers.onCancel}>{this.i18n('cancel')}</Button>
                {this._renderSubmitButton()}
                {this.renderDeleteButton()}
            </ButtonToolbar>
        </IfAnyGranted>;
    },

    _getSaveButtonTitle: function () {
        let titleProp = 'detail.save-tooltip';
        if (this.state.submitting) {
            titleProp = 'detail.saving';
        } else if (!ReportValidator.isValid(this.props.report)) {
            titleProp = ReportValidator.getValidationMessage(this.props.report);
        }
        return this.i18n(titleProp);
    },

    _renderSubmitButton: function () {
        return this.props.report.isNew ? null :
            <Button bsStyle='primary' bsSize='small' title={this.i18n('detail.submit-tooltip')} onClick={this.onSubmit}>
                {this.i18n('detail.submit')}
            </Button>;
    }
});

module.exports = injectIntl(AuditReport);
