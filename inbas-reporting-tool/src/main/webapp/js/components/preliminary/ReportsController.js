/**
 * @jsx
 */

'use strict';

var React = require('react');
var Reflux = require('reflux');

var IntlMixin = require('react-intl').IntlMixin;

var Actions = require('../../actions/Actions');
var ReportsStore = require('../../stores/PreliminaryReportStore');
var Reports = require('../reports/Reports');
var ReportRow = require('./ReportRow');

var Routing = require('../../utils/Routing');
var Routes = require('../../utils/Routes');

var ReportsController = React.createClass({
    mixins: [
        Reflux.listenTo(ReportsStore, 'onReportsChange')
    ],

    getInitialState: function () {
        return {
            reports: ReportsStore.getReports()
        };
    },

    componentWillMount: function () {
        Actions.loadPreliminaries();
    },

    onReportsChange: function (reports) {
        this.setState({reports: reports});
    },

    onCreateReport: function () {
        Routing.transitionTo(Routes.createReport, {
            handlers: {
                onSuccess: Routes.preliminary,
                onCancel: Routes.preliminary
            }
        });
    },

    onEditReport: function (report) {
        Routing.transitionTo(Routes.editReport, {
            params: {reportKey: report.key},
            handlers: {onSuccess: Routes.preliminary, onCancel: Routes.preliminary}
        });
    },

    onRemoveReport: function (report) {
        Actions.deletePreliminary(report);
    },

    onCreateInvestigation: function (report) {
        Actions.createInvestigation(report.key, this.onOpenInvestigation);
    },

    onOpenInvestigation: function (key) {
        Routing.transitionTo(Routes.editInvestigation, {
            params: {reportKey: key},
            handlers: {onCancel: Routes.investigations}
        });
    },


    render: function () {
        var actions = {
            onEdit: this.onEditReport,
            onRemove: this.onRemoveReport,
            onInvestigate: this.onCreateInvestigation
        };
        return (
            <Reports panelTitle={this.getIntlMessage('preliminary.panel-title')} reports={this.state.reports} actions={actions}
                     rowComponent={ReportRow} tableHeader={this.renderTableHeader()}/>
        );
    },

    renderTableHeader: function () {
        return (
            <thead>
            <tr>
                <th className='col-xs-2'>{this.getIntlMessage('headline')}</th>
                <th className='col-xs-2'>{this.getIntlMessage('reports.table-date')}</th>
                <th className='col-xs-6'>{this.getIntlMessage('narrative')}</th>
                <th className='col-xs-2'>{this.getIntlMessage('table-actions')}</th>
            </tr>
            </thead>
        );
    }
});

module.exports = ReportsController;
