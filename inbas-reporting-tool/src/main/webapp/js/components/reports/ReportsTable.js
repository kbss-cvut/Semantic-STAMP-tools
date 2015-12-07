/**
 * @jsx
 */

'use strict';

var React = require('react');
var Panel = require('react-bootstrap').Panel;
var Table = require('react-bootstrap').Table;

var IntlMixin = require('react-intl').IntlMixin;

var ReportRow = require('./ReportRow');

var ReportsTable = React.createClass({
    mixins: [IntlMixin],

    propTypes: {
        reports: React.PropTypes.array.isRequired,
        panelTitle: React.PropTypes.string, // Panel title (header), optional
        tableHead: React.PropTypes.object,   // Table header, optional
        rowComponent: React.PropTypes.func     // A react component, optional
    },

    render: function () {
        var title = <h3>{this.props.panelTitle ? this.props.panelTitle : this.getIntlMessage('reports.panel-title')}</h3>;
        return (<div>
            <Panel header={title} bsStyle='primary' {...this.props}>
                <Table striped bordered condensed hover>
                    {this.renderHeader()}
                    <tbody>
                    {this.renderReports()}
                    </tbody>
                </Table>
            </Panel>
        </div>);
    },

    renderReports: function () {
        var result = [],
            len = this.props.reports.length,
            rowComponent = this.props.rowComponent ? this.props.rowComponent : ReportRow;
        for (var i = 0; i < len; i++) {
            result.push(React.createElement(rowComponent, {
                report: this.props.reports[i],
                key: this.props.reports[i].uri,
                actions: this.props.actions
            }));
        }
        return result;
    },

    renderHeader: function () {
        return this.props.tableHeader ? this.props.tableHeader : (
            <thead>
            <tr>
                <th className='col-xs-2'>{this.getIntlMessage('headline')}</th>
                <th className='col-xs-2'>{this.getIntlMessage('reports.table-date')}</th>
                <th className='col-xs-5'>{this.getIntlMessage('narrative')}</th>
                <th className='col-xs-1'>{this.getIntlMessage('reports.table-type')}</th>
                <th className='col-xs-2'>{this.getIntlMessage('table-actions')}</th>
            </tr>
            </thead>
        );
    }
});

module.exports = ReportsTable;
