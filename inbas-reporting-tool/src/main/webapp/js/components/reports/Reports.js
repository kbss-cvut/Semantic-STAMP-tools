/**
 * @jsx
 */

'use strict';

var React = require('react');
var Jumbotron = require('react-bootstrap').Jumbotron;
var Panel = require('react-bootstrap').Panel;

var injectIntl = require('../../utils/injectIntl');

var ReportsFilter = require('./ReportsFilter');
var ReportsTable = require('./ReportsTable');
var Mask = require('./../Mask');
var I18nMixin = require('../../i18n/I18nMixin');

var Reports = React.createClass({
    mixins: [I18nMixin],

    propTypes: {
        reports: React.PropTypes.array,
        rowComponent: React.PropTypes.func,     // A react component
        onEdit: React.PropTypes.func,
        onRemove: React.PropTypes.func
    },

    render: function () {
        var reports = this.props.reports;
        if (reports === null) {
            return (
                <Mask text={this.i18n('reports.loading-mask')}/>
            );
        }
        if (reports.length === 0) {
            return (
                <div>
                    <Jumbotron>
                        <h2>INBAS Reporting</h2>

                        <p>{this.i18n('reports.no-reports')}</p>
                    </Jumbotron>
                </div>
            );
        } else {
            return (
                <Panel header={<h3>{this.i18n('reports.panel-title')}</h3>} bsStyle='primary'>
                    <ReportsFilter onFilterChange={this.props.actions.onFilterChange}/>
                    <ReportsTable {...this.props}/>
                </Panel>);
        }

    }
});

module.exports = injectIntl(Reports);
