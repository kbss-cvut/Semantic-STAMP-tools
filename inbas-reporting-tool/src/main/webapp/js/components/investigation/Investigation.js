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
var BasicOccurrenceInfo = require('../preliminary/BasicOccurrenceInfo');
var Factors = require('./Factors');
var InitialReports = require('../initialreport/InitialReports');
var ReportSummary = require('../preliminary/ReportSummary');
var ReportStatements = require('../preliminary/ReportStatements');
var Mask = require('../Mask');
var MessageMixin = require('../mixin/MessageMixin');
var ResourceNotFound = require('../ResourceNotFound');
var InvestigationValidator = require('../../validation/InvestigationValidator');
var I18nMixin = require('../../i18n/I18nMixin');

var Investigation = React.createClass({
    mixins: [MessageMixin, I18nMixin],

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

    onChange: function (e) {
        var attributeName = e.target.name;
        this.onAttributeChange(attributeName, e.target.value);
    },

    onAttributeChange: function (attribute, value) {
        var change = {};
        change[attribute] = value;
        this.onChanges({attribute: value});
    },

    onChanges: function (changes) {
        this.props.handlers.onChange(changes);
    },

    onSave: function (e) {
        var investigation = this.props.investigation,
            factors = this.refs.factors.getWrappedElement();
        e.preventDefault();
        this.setState(assign(this.state, {submitting: true}));
        investigation.rootFactor = factors.getFactorHierarchy();
        investigation.links = factors.getLinks();
        Actions.updateInvestigation(investigation, this.onSaveSuccess, this.onSaveError);
    },

    onSaveSuccess: function () {
        this.setState({submitting: false});
        this.props.handlers.onSuccess();
        this.showSuccessMessage(this.i18n('save-success-message'));
    },

    onSaveError: function (error) {
        this.setState({submitting: false});
        this.showErrorMessage(this.i18n('save-failed-message') + error.message);
    },


    render: function () {
        if (this.props.loading) {
            return (
                <Mask text={this.i18n('investigation.detail.loading')}/>
            );
        }
        if (!this.props.investigation) {
            return (<ResourceNotFound resource={this.i18n('investigation.detail.panel-title')}/>);
        }
        return this.renderDetail();
    },

    renderDetail: function () {
        var investigation = this.props.investigation;

        return (
            <div>
                <Panel header={this.i18n('investigation.detail.panel-title')}>
                    <form>
                        <BasicOccurrenceInfo report={investigation} onChange={this.onChange}
                                             onAttributeChange={this.onAttributeChange}/>

                        <div className='row'>
                            <div className='col-xs-12'>
                                <InitialReports report={investigation} onAttributeChange={this.onAttributeChange}/>
                            </div>
                        </div>

                        <div>
                            <Factors ref='factors' investigation={investigation} onChange={this.onChanges}/>
                        </div>

                        <div className='form-group'>
                            <ReportStatements report={investigation} onChange={this.props.onChange}
                                              show={['correctiveMeasures']}/>
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

    renderButtons: function () {
        var loading = this.state.submitting,
            submitDisabled = !InvestigationValidator.isValid(this.props.investigation) || loading,
            submitTitle = this.i18n('detail.save-tooltip'),
            submitLabel = this.i18n(loading ? 'detail.saving' : 'save');
        if (loading) {
            submitTitle = this.i18n('detail.saving');
        } else if (submitDisabled) {
            submitTitle = this.i18n('detail.invalid-tooltip');
        }

        return (<ButtonToolbar className='float-right' style={{margin: '1em 0 0.5em 0'}}>
            <Button bsStyle='success' bsSize='small' disabled={submitDisabled}
                    ref='submit' title={submitTitle}
                    onClick={this.onSave}>{submitLabel}</Button>
            <Button bsStyle='link' bsSize='small' title={this.i18n('cancel-tooltip')}
                    onClick={this.props.handlers.onCancel}>{this.i18n('cancel')}</Button>
            {this.renderSubmitButton()}
        </ButtonToolbar>);
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
