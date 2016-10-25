'use strict';

var React = require('react');
var Button = require('react-bootstrap').Button;
var ButtonToolbar = require('react-bootstrap').ButtonToolbar;
var Panel = require('react-bootstrap').Panel;

var Actions = require('../../../actions/Actions');
var Audit = require('./Audit').default;
var I18nMixin = require('../../../i18n/I18nMixin');
var injectIntl = require('../../../utils/injectIntl');
var Input = require('../../Input').default;
var MessageMixin = require('../../mixin/MessageMixin');
var ReportDetailMixin = require('../../mixin/ReportDetailMixin');
var ReportProvenance = require('../ReportProvenance').default;
var ReportValidator = require('../../../validation/ReportValidator');

var AuditReport = React.createClass({
    mixins: [MessageMixin, I18nMixin, ReportDetailMixin],

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

    onSave: function () {
        var report = this.props.report;
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
        var report = this.props.report;

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
        var fileNo = null;
        if (this.props.report.fileNumber) {
            fileNo =
                <h3 className='panel-title pull-right'>{this.i18n('fileNo') + ' ' + this.props.report.fileNumber}</h3>;
        }
        return (
            <div>
                <h2 className='panel-title pull-left'>{this.i18n('auditreport.title')}</h2>
                {fileNo}
                <div style={{clear: 'both'}}/>
            </div>
        )
    },

    _renderButtons: function () {
        if (this.props.readOnly) {
            return this.renderReadOnlyButtons();
        }
        var loading = this.state.submitting,
            saveDisabled = !ReportValidator.isValid(this.props.report) || loading,
            saveLabel = this.i18n(loading ? 'detail.saving' : 'save');

        return <ButtonToolbar className='float-right' style={{margin: '1em 0 0.5em 0'}}>
            <Button bsStyle='success' bsSize='small' disabled={saveDisabled} title={this._getSaveButtonTitle()}
                    onClick={this.onSave}>{saveLabel}</Button>
            <Button bsStyle='link' bsSize='small' title={this.i18n('cancel-tooltip')}
                    onClick={this.props.handlers.onCancel}>{this.i18n('cancel')}</Button>
            {this._renderSubmitButton()}
            {this.renderDeleteButton()}
        </ButtonToolbar>;
    },

    _getSaveButtonTitle: function () {
        var titleProp = 'detail.save-tooltip';
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
