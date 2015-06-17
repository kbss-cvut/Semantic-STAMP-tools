/**
 * Created by ledvima1 on 27.5.15.
 */

'use strict';

var React = require('react');
var Panel = require('react-bootstrap').Panel;
var Table = require('react-bootstrap').Table;

var ReportRow = require('./ReportRow');

var ReportsTable = React.createClass({
    render: function () {
        var title = <h3>Reports</h3>;
        var reports = this.prepareReports();
        return (<div>
            <Panel header={title}>
                <Table striped bordered condensed hover>
                    <thead>
                    <tr>
                        <th style={{width: '15em', minWidth: '15em'}}>Event date</th>
                        <th style={{width: '12em', minWidth: '12em'}}>Author</th>
                        <th>Description</th>
                        <th style={{width: '10em', minWidth: '10em'}}>Actions</th>
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
