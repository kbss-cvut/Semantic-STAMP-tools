/**
 * @jsx
 */

'use strict';

var React = require('react');
var Table = require('react-bootstrap').Table;

var injectIntl = require('../../utils/injectIntl');

var ReportRow = require('./ReportRow');
var I18nMixin = require('../../i18n/I18nMixin');

var ReportsTable = React.createClass({
    mixins: [I18nMixin],

    propTypes: {
        reports: React.PropTypes.array.isRequired
    },

    render: function () {
        return (
            <Table striped bordered condensed hover>
                {this.renderHeader()}
                <tbody>
                {this.renderReports()}
                </tbody>
            </Table>);
    },

    renderReports: function () {
        var result = [],
            len = this.props.reports.length,
            report;
        for (var i = 0; i < len; i++) {
            report = this.props.reports[i];
            result.push(<ReportRow report={report} key={report.uri} actions={this.props.actions}/>);
        }
        return result;
    },

    renderHeader: function () {
        return (
            <thead>
            <tr>
                <th className='col-xs-2 content-center'>{this.i18n('headline')}</th>
                <th className='col-xs-2 content-center'>{this.i18n('reports.table-date')}</th>
                <th className='col-xs-5 content-center'>{this.i18n('narrative')}</th>
                <th className='col-xs-1 content-center'>{this.i18n('reports.table-type')}</th>
                <th className='col-xs-2 content-center'>{this.i18n('table-actions')}</th>
            </tr>
            </thead>
        );
    }
});

module.exports = injectIntl(ReportsTable);
