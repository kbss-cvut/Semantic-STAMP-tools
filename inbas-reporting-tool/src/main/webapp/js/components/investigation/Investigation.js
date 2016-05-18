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
var ArmsAttributes = require('../reports/arms/ArmsAttributes');
var BasicOccurrenceInfo = require('../preliminary/BasicOccurrenceInfo');
var Factors = require('./Factors');
var InitialReports = require('../initialreport/InitialReports');
var ReportSummary = require('../preliminary/ReportSummary');
var ReportStatements = require('../preliminary/ReportStatements');
var MessageMixin = require('../mixin/MessageMixin');
var ReportValidator = require('../../validation/ReportValidator');
var I18nMixin = require('../../i18n/I18nMixin');
var ReportDetailMixin = require('../mixin/ReportDetailMixin');

var Investigation = React.createClass({
    mixins: [MessageMixin, I18nMixin, ReportDetailMixin],

    propTypes: {
        handlers: React.PropTypes.object,
        investigation: React.PropTypes.object,
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

    onSave: function (e) {
        var investigation = this.props.investigation,
            factors = this.refs.factors.getWrappedInstance();
        e.preventDefault();
        this.setState(assign(this.state, {submitting: true}));
        investigation.rootFactor = factors.getFactorHierarchy();
        investigation.links = factors.getLinks();
        Actions.updateReport(investigation, this.onSaveSuccess, this.onSaveError);
    },

    onSubmit: function () {
        this.setState({submitting: true});
        Actions.submitReport(this.props.investigation, this.onSubmitSuccess, this.onSubmitError);
    },

    render: function () {
        var investigation = this.props.investigation;

        return (
            <div>
                <Panel header={this.renderHeader()} bsStyle='primary'>
                    <form>
                        <BasicOccurrenceInfo report={investigation} revisions={this.props.revisions}
                                             onChange={this.onChange} onAttributeChange={this.onAttributeChange}/>

                        <div className='row'>
                            <div className='col-xs-12'>
                                <InitialReports report={investigation} onAttributeChange={this.onAttributeChange}/>
                            </div>
                        </div>

                        <div>
                            <Factors ref='factors' investigation={investigation} onChange={this.onChanges}/>
                        </div>

                        <div className='form-group'>
                            <ReportStatements report={investigation} onChange={this.props.handlers.onChange}
                                              show={['correctiveMeasures']}/>
                        </div>

                        <div className='form-group'>
                            <ArmsAttributes report={investigation} onChange={this.props.handlers.onChange}/>
                        </div>

                        <div className='row'>
                            <div className='col-xs-12'>
                                <ReportSummary report={investigation} onChange={this.onChange}/>
                            </div>
                        </div>

                        {this.renderButtons()}
                    </form>
                </Panel>
                {this.renderMessage()}
            </div>
        );
    },

    renderHeader: function () {
        return (
            <div>
                <h2 className='panel-title pull-left'>{this.i18n('investigation.detail.panel-title')}</h2>
                <h3 className='panel-title pull-right'>{this.i18n('fileNo') + ' ' + this.props.investigation.fileNumber}</h3>
                <div style={{clear: 'both'}}/>
            </div>
        )
    },

    renderButtons: function () {
        if (this.props.readOnly) {
            return this.renderReadOnlyButtons();
        }
        var loading = this.state.submitting,
            saveDisabled = !ReportValidator.isValid(this.props.investigation) || loading,
            saveLabel = this.i18n(loading ? 'detail.saving' : 'save');

        return (<ButtonToolbar className='float-right' style={{margin: '1em 0 0.5em 0'}}>
            <Button bsStyle='success' bsSize='small' disabled={saveDisabled} title={this.getSaveButtonTitle()}
                    onClick={this.onSave}>{saveLabel}</Button>
            <Button bsStyle='link' bsSize='small' title={this.i18n('cancel-tooltip')}
                    onClick={this.props.handlers.onCancel}>{this.i18n('cancel')}</Button>
            {this.renderSubmitButton()}
        </ButtonToolbar>);
    },

    getSaveButtonTitle: function() {
        var titleProp = 'detail.save-tooltip';
        if (this.state.submitting) {
            titleProp = 'detail.saving';
        } else if (!ReportValidator.isValid(this.props.investigation)) {
            titleProp = ReportValidator.getValidationMessage(this.props.investigation);
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

module.exports = injectIntl(Investigation);
