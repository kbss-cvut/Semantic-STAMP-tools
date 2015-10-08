/**
 * @jsx
 */

'use strict';

var React = require('react');
var Panel = require('react-bootstrap').Panel;
var Table = require('react-bootstrap').Table;

var ReportRow = require('./ReportRow');

var ReportsTable = React.createClass({
    render: function () {
        var title = <h3>Occurrence Reports</h3>;
        var reports = this.prepareReports();
        return (<div>
            <Panel header={title} bsStyle='primary' {...this.props}>
                <Table striped bordered condensed hover>
                    <thead>
                    <tr>
                        <th className='col-xs-2'>Occurrence name</th>
                        <th className='col-xs-2'>Occurrence date</th>
                        <th className='col-xs-6'>Description</th>
                        <th className='col-xs-2'>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {reports}
                    </tbody>
                </Table>
            </Panel>
        </div>)
    },
    prepareReports: function () {
        var result = [];
        var len = this.props.reports.length;
        for (var i = 0; i < len; i++) {
            result.push(<ReportRow report={this.props.reports[i]} key={this.props.reports[i].uri}
                                   onEditReport={this.props.onEditReport}/>);
        }
        return result;
    }
});

module.exports = ReportsTable;
