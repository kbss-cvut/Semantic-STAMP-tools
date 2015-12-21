/**
 * @jsx
 */

'use strict';

var React = require('react');
var Panel = require('react-bootstrap').Panel;
var Table = require('react-bootstrap').Table;

var injectIntl = require('../../utils/injectIntl');

var Utils = require('../../utils/Utils');
var CollapsibleText = require('../CollapsibleText');
var ReportType = require('../../model/ReportType');
var I18nMixin = require('../../i18n/I18nMixin');

var RECENTLY_EDITED_COUNT = 10;

var RecentlyEditedReports = React.createClass({
    mixins: [I18nMixin],

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
        var title = (<h5>{this.i18n('dashboard.recent-panel-heading')}</h5>),
            recentReports = this.renderRecentReports(this.filterRecentReports()),
            content = null;
        if (recentReports.length > 0) {
            content = (<Table striped bordered condensed hover>
                <thead>
                <tr>
                    <th className='col-xs-5'>{this.i18n('dashboard.recent-table-headline')}</th>
                    <th className='col-xs-3' className='content-center'>{this.i18n('dashboard.recent-table-date')}</th>
                    <th className='col-xs-3'
                        className='content-center'>{this.i18n('dashboard.recent-table-last-edited')}</th>
                    <th className='col-xs-1' className='content-center'>{this.i18n('reports.table-type')}</th>
                </tr>
                </thead>
                <tbody>
                {recentReports}
                </tbody>
            </Table>);
        } else {
            content = (<div>{this.i18n('reports.no-occurrence-reports')}</div>);
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
    mixins: [I18nMixin],

    onOpenClick: function (e) {
        e.preventDefault();
        this.props.onOpenReport(this.props.report);
    },

    render: function () {
        var report = this.props.report,
            vAlign = {verticalAlign: 'middle'},
            type = ReportType.asString(report),
            dateEdited = report.lastEdited ? report.lastEdited : report.created;
        return (
            <tr>
                <td style={vAlign}>
                    <a href='javascript:void(0);' onClick={this.onOpenClick}
                       title={this.i18n('reports.open-tooltip')}><CollapsibleText
                        text={report.occurrence.name}
                        maxLength={20}/></a>
                </td>
                <td style={vAlign}
                    className='content-center'>{Utils.formatDate(new Date(report.occurrence.startTime))}</td>
                <td style={vAlign} className='content-center'>{Utils.formatDate(new Date(dateEdited))}</td>
                <td style={vAlign} className='content-center'>
                    <img className='report-type-icon centered' src={ReportType.getIconSrc(report)} alt={type}
                         title={type}/>
                </td>
            </tr>
        );
    }
}));

module.exports = injectIntl(RecentlyEditedReports);
