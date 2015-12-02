/**
 * @jsx
 */

'use strict';

var React = require('react');
var Reflux = require('reflux');

var Actions = require('../../actions/Actions');
var Routing = require('../../utils/Routing');
var Routes = require('../../utils/Routes');
var UserStore = require('../../stores/UserStore');
var ReportStore = require('../../stores/ReportStore');
var Dashboard = require('./Dashboard');
var WizardWindow = require('./../wizard/WizardWindow');
var InitialReportImportSteps = require('../initialreport/Steps');
var ReportType = require('../../model/ReportType');

var DashboardController = React.createClass({
    mixins: [
        Reflux.listenTo(UserStore, 'onUserLoaded'),
        Reflux.listenTo(ReportStore, 'onReportsLoaded')
    ],
    getInitialState: function () {
        return {
            firstName: UserStore.getCurrentUser() ? UserStore.getCurrentUser().firstName : '',
            reports: ReportStore.getReports() ? ReportStore.getReports() : [],
            initialReportImportOpen: false
        }
    },

    componentWillMount: function () {
        Actions.loadAllReports();
    },

    onUserLoaded: function (user) {
        this.setState({firstName: user.firstName});
    },

    onReportsLoaded: function () {
        this.setState({reports: ReportStore.getReports()});
    },

    createEmptyReport: function () {
        Routing.transitionTo(Routes.createReport, {
            handlers: {
                onSuccess: Routes.preliminary,
                onCancel: Routes.dashboard
            }
        });
    },

    cancelInitialReportImport: function () {
        this.setState({initialReportImportOpen: false});
    },

    openInitialReportImport: function () {
        this.setState({initialReportImportOpen: true});
    },

    importInitialReport: function (data, closeCallback) {
        Routing.transitionTo(Routes.createReport, {
            payload: {initialReports: [data.initialReport]},
            handlers: {onSuccess: Routes.preliminary, onCancel: Routes.dashboard}
        });
        closeCallback();
    },

    openReport: function (report) {
        var route = ReportType.getDetailRoute(report);
        Routing.transitionTo(route, {
            params: {reportKey: report.key},
            handlers: {onCancel: Routes.dashboard}
        });
    },

    showReports: function () {
        Routing.transitionTo(Routes.reports);
    },


    render: function () {
        var wizardProperties = {
            steps: InitialReportImportSteps,
            initialReport: {},
            title: 'Import Initial Report',
            onFinish: this.importInitialReport
        };
        return (
            <div>
                <WizardWindow show={this.state.initialReportImportOpen} {...wizardProperties}
                              onHide={this.cancelInitialReportImport}/>
                <Dashboard userFirstName={this.state.firstName} reports={this.state.reports}
                           showAllReports={this.showReports} createEmptyReport={this.createEmptyReport}
                           importInitialReport={this.openInitialReportImport} openReport={this.openReport}/>
            </div>
        );
    }
});

module.exports = DashboardController;
