/**
 * @jsx
 */

'use strict';

var React = require('react');
var Panel = require('react-bootstrap').Panel;
var Table = require('react-bootstrap').Table;

var injectIntl = require('../../utils/injectIntl');

var ReportRow = require('./ReportRow');
var I18nMixin = require('../../i18n/I18nMixin');

var ReportsTable = React.createClass({
    mixins: [I18nMixin],

    propTypes: {
        reports: React.PropTypes.array.isRequired,
        panelTitle: React.PropTypes.string, // Panel title (header), optional
        tableHead: React.PropTypes.object,   // Table header, optional
        rowComponent: React.PropTypes.func     // A react component, optional
    },

    render: function () {
        var title = <h3>{this.props.panelTitle ? this.props.panelTitle : this.i18n('reports.panel-title')}</h3>;
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
                <th className='col-xs-2'>{this.i18n('headline')}</th>
                <th className='col-xs-2'>{this.i18n('reports.table-date')}</th>
                <th className='col-xs-5'>{this.i18n('narrative')}</th>
                <th className='col-xs-1 content-center'>{this.i18n('reports.table-type')}</th>
                <th className='col-xs-2 content-center'>{this.i18n('table-actions')}</th>
            </tr>
            </thead>
        );
    }
});

module.exports = injectIntl(ReportsTable);
