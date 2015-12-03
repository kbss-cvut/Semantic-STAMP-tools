/**
 * @jsx
 */

'use strict';

var React = require('react');
var Panel = require('react-bootstrap').Panel;
var Table = require('react-bootstrap').Table;

var ReportRow = require('./ReportRow');

var ReportsTable = React.createClass({

    propTypes: {
        reports: React.PropTypes.array.isRequired,
        panelTitle: React.PropTypes.string, // Panel title (header), optional
        tableHead: React.PropTypes.object,   // Table header, optional
        rowComponent: React.PropTypes.func     // A react component, optional
    },

    render: function () {
        var title = <h3>{this.props.panelTitle ? this.props.panelTitle : 'Occurrence reports'}</h3>;
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
                <th className='col-xs-2'>Headline</th>
                <th className='col-xs-2'>Occurrence date</th>
                <th className='col-xs-5'>Narrative</th>
                <th className='col-xs-1'>Type</th>
                <th className='col-xs-2'>Actions</th>
            </tr>
            </thead>
        );
    }
});

module.exports = ReportsTable;
