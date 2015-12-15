/**
 * @jsx
 */

'use strict';

var React = require('react');
var Button = require('react-bootstrap').Button;
var ButtonToolbar = require('react-bootstrap').ButtonToolbar;
var Panel = require('react-bootstrap').Panel;
var assign = require('object-assign');
var injectIntl = require('react-intl').injectIntl;

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

    getInitialState: function () {
        return {
            submitting: false
        };
    },

    onChange: function (e) {
        var value = e.target.value;
        var attributeName = e.target.name;
        this.props.onChange(attributeName, value);
    },

    onAttributeChange: function (attribute, value) {
        this.props.onChange(attribute, value);
    },

    onSubmit: function (e) {
        var investigation = this.props.investigation;
        e.preventDefault();
        this.setState(assign(this.state, {submitting: true}));
        investigation.rootFactor = this.refs.factors.getFactorHierarchy();
        investigation.links = this.refs.factors.getLinks();
        Actions.updateInvestigation(investigation, this.onSubmitSuccess, this.onSubmitError);
    },

    onSubmitSuccess: function () {
        this.setState({submitting: false});
        this.props.onSuccess();
        this.showSuccessMessage(this.i18n('save-success-message'));
    },

    onSubmitError: function (error) {
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
            return (<ResourceNotFound resource='Investigation'/>);
        }
        return this.renderDetail();
    },

    renderDetail: function () {
        var investigation = this.props.investigation,
            loading = this.state.submitting,
            submitDisabled = !InvestigationValidator.isValid(investigation) || loading,
            submitTitle = this.i18n('detail.save-tooltip'),
            submitLabel = this.i18n(loading ? 'detail.saving' : 'save');
        if (loading) {
            submitTitle = this.i18n('detail.saving');
        } else if (submitDisabled) {
            submitTitle = this.i18n('detail.invalid-tooltip');
        }

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
                            <Factors ref='factors' investigation={investigation}
                                     onAttributeChange={this.onAttributeChange}/>
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

                        <ButtonToolbar className='float-right' style={{margin: '1em 0 0.5em 0'}}>
                            <Button bsStyle='success' bsSize='small' disabled={submitDisabled}
                                    ref='submit' title={submitTitle}
                                    onClick={this.onSubmit}>{submitLabel}</Button>
                            <Button bsStyle='link' bsSize='small' title={this.i18n('cancel-tooltip')}
                                    onClick={this.props.onCancel}>{this.i18n('cancel')}</Button>
                        </ButtonToolbar>
                    </form>
                </Panel>
                {this.renderMessage()}
            </div>
        );
    }
});

module.exports = injectIntl(Investigation);
