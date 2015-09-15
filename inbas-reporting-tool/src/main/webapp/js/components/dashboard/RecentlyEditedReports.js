'use strict';

var React = require('react');
var Panel = require('react-bootstrap').Panel;
var Table = require('react-bootstrap').Table;

var Utils = require('../../utils/Utils');

var RECENTLY_EDITED_COUNT = 10;

var RecentlyEditedReports = React.createClass({

    filterRecentReports: function () {
        var reports = this.props.reports.slice();
        reports.sort(function (a, b) {
            return b.lastEdited - a.lastEdited;
        });
        return reports.slice(0, RECENTLY_EDITED_COUNT);
    },

    render: function () {
        var title = (<h3>Recently Edited/Added Reports</h3>);
        var recentReports = this.renderRecentReports(this.filterRecentReports());
        return (
            <Panel header={title} style={{height: '100%'}}>
                <Table striped bordered condensed hover>
                    <thead>
                    <tr>
                        <th className='col-xs-4'>Occurrence summary</th>
                        <th className='col-xs-4'>Occurrence date</th>
                        <th className='col-xs-4'>Last edited</th>
                    </tr>
                    </thead>
                    <tbody>
                    {recentReports}
                    </tbody>
                </Table>
            </Panel>
        );
    },

    renderRecentReports: function (reports) {
        var toRender = [];
        for (var i = 0, len = reports.length; i < len; i++) {
            toRender.push(<ReportRow key={reports[i].key} report={reports[i]} onOpenReport={this.props.onOpenReport}/>);
        }
        return toRender;
    }
});

var ReportRow = React.createClass({
    onOpenClick: function (e) {
        e.preventDefault();
        this.props.onOpenReport(this.props.report);
    },

    render: function () {
        var report = this.props.report;
        return (
            <tr>
                <td><a href='javascript:void(0);' onClick={this.onOpenClick}
                       title='Click to see report detail'>{report.name}</a></td>
                <td>{Utils.formatDate(new Date(report.occurrenceTime))}</td>
                <td>{Utils.formatDate(new Date(report.lastEdited))}</td>
            </tr>
        );
    }
});

module.exports = RecentlyEditedReports;
