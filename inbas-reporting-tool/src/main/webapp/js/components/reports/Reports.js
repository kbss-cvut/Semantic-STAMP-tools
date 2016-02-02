/**
 * @jsx
 */

'use strict';

var React = require('react');
var Panel = require('react-bootstrap').Panel;

var injectIntl = require('../../utils/injectIntl');

var ReportsFilter = require('./ReportsFilter');
var ReportsTable = require('./ReportsTable');
var Mask = require('./../Mask');
var Routing = require('../../utils/Routing');
var I18nMixin = require('../../i18n/I18nMixin');

var Reports = React.createClass({
    mixins: [I18nMixin],

    propTypes: {
        reports: React.PropTypes.array,
        actions: React.PropTypes.object
    },

    createReport: function () {
        Routing.transitionToHome();
    },


    render: function () {
        var reports = this.props.reports;
        if (reports === null) {
            return (
                <Mask text={this.i18n('reports.loading-mask')}/>
            );
        }
        return (
            <Panel header={<h3>{this.i18n('reports.panel-title')}</h3>} bsStyle='primary'>
                <ReportsFilter onFilterChange={this.props.actions.onFilterChange}/>
                {this.renderReports()}
            </Panel>);
    },

    renderReports: function () {
        if (this.props.reports.length === 0) {
            if (this.props.filter) {
                return <div className='no-reports-notice italics'>{this.i18n('reports.filter.no-matching-found')}</div>;
            } else {
                return (
                    <div className='no-reports-notice italics'>
                        {this.i18n('reports.no-reports')}
                        <a href='#' onClick={this.createReport} title={this.i18n('reports.no-reports.link-tooltip')}>
                            {this.i18n('reports.no-reports.link')}
                        </a>
                    </div>);
            }
        }
        return <ReportsTable {...this.props}/>
    }
});

module.exports = injectIntl(Reports);
