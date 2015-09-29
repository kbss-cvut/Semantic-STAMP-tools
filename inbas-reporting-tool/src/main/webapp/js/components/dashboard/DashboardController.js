/**
 * @jsx
 */

'use strict';

var React = require('react');
var Reflux = require('reflux');

var Actions = require('../../actions/Actions');
var router = require('../../utils/router');
var UserStore = require('../../stores/UserStore');
var ReportsStore = require('../../stores/ReportsStore');
var Dashboard = require('./Dashboard');
var WizardWindow = require('./../wizard/WizardWindow');
var InitialReportImportSteps = require('../initialreport/Steps');

var DashboardController = React.createClass({
    mixins: [
        Reflux.listenTo(UserStore, 'onUserLoaded'),
        Reflux.listenTo(ReportsStore, 'onReportsLoaded')
    ],
    getInitialState: function () {
        return {
            firstName: UserStore.getCurrentUser() ? UserStore.getCurrentUser().firstName : '',
            reports: ReportsStore.getReports() ? ReportsStore.getReports() : [],
            initialReportImportOpen: false
        }
    },

    componentWillMount: function () {
        Actions.loadReports();
    },

    onUserLoaded: function (user) {
        this.setState({firstName: user.firstName});
    },

    onReportsLoaded: function () {
        this.setState({reports: ReportsStore.getReports()});
    },

    createEmptyReport: function () {
        router.transitionTo('report_new', null, {onSuccess: 'reports', onCancel: 'dashboard'});
    },

    cancelInitialReportImport: function () {
        this.setState({initialReportImportOpen: false});
    },

    openInitialReportImport: function () {
        this.setState({initialReportImportOpen: true});
    },

    importInitialReport: function (data, closeCallback) {
        router.transitionTo('report_new', null, {
            onSuccess: 'reports',
            onCancel: 'dashboard'
        }, {initialReports: [data.initialReport]});
        closeCallback();
    },

    openReport: function (report) {
        router.transitionTo('report', {reportKey: report.key}, {onSuccess: 'reports', onCancel: 'dashboard'});
    },

    showReports: function () {
        router.transitionTo('reports');
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
