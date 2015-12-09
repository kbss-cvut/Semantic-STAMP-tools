/**
 * @jsx
 */

'use strict';

var React = require('react');
var Panel = require('react-bootstrap').Panel;
var Table = require('react-bootstrap').Table;

var injectIntl = require('react-intl').injectIntl;

var Utils = require('../../utils/Utils');
var CollapsibleText = require('../CollapsibleText');

var RECENTLY_EDITED_COUNT = 10;

var RecentlyEditedReports = React.createClass({

    filterRecentReports: function () {
        var reports = this.props.reports.slice();
        reports.sort(function (a, b) {
            var aEdited = a.lastEdited ? a.lastEdited : a.created,
                bEdited = b.lastEdited ? b.lastEdited : b.created;
            return bEdited - aEdited;
        });
        return reports.slice(0, RECENTLY_EDITED_COUNT);
    },

    render: function () {
        var title = (<h5>{{id: 'dashboard.recent-panel-heading'}}</h5>),
            recentReports = this.renderRecentReports(this.filterRecentReports()),
            content = null;
        if (recentReports.length > 0) {
            content = (<Table striped bordered condensed hover>
                <thead>
                <tr>
                    <th className='col-xs-4'>{{id: 'dashboard.recent-table-headline'}}</th>
                    <th className='col-xs-4'>{{id: 'dashboard.recent-table-date'}}</th>
                    <th className='col-xs-4'>{{id: 'dashboard.recent-table-last-edited'}}</th>
                </tr>
                </thead>
                <tbody>
                {recentReports}
                </tbody>
            </Table>);
        } else {
            content = (<div>{{id: 'reports.no-occurrence-reports'}}</div>);
        }
        return (
            <Panel header={title} bsStyle='info' style={{height: '100%'}}>
                {content}
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

var ReportRow = injectIntl(React.createClass({

    onOpenClick: function (e) {
        e.preventDefault();
        this.props.onOpenReport(this.props.report);
    },

    render: function () {
        var report = this.props.report,
            vAlign = {verticalAlign: 'middle'},
            dateEdited = report.lastEdited ? report.lastEdited : report.created;
        return (
            <tr>
                <td style={vAlign}>
                    <a href='javascript:void(0);' onClick={this.onOpenClick}
                       title={{id: 'reports.open-tooltip'}}><CollapsibleText
                        text={report.occurrence.name}
                        maxLength={20}/></a>
                </td>
                <td style={vAlign}>{Utils.formatDate(new Date(report.occurrence.startTime))}</td>
                <td style={vAlign}>{Utils.formatDate(new Date(dateEdited))}</td>
            </tr>
        );
    }
}));

module.exports = injectIntl(RecentlyEditedReports);
